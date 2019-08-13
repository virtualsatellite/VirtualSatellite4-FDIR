/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.HostConfig.Bind;

import de.dlr.sc.virsat.fdir.storm.docker.DockerHelper;
import de.dlr.sc.virsat.fdir.storm.files.InstanceFileGenerator;

/**
 * Storm runners, takes care of executing a storm program
 * taking the exeuction environment (e.g. docker or local)
 * into consideration
 * @author sascha
 *
 * @param <S> Return type of programs that this runner can execute
 */

public class StormRunner<S> {
	
	public static final String DOCKER_IMAGE_STORM = "movesrwth/storm:travis";
	public static final String DOCKER_TEMP_PATH = "C:\\Users\\" + System.getProperty("user.name") + "\\TEMP";
	public static final String DOCKER_DATA_PATH = "/data";
	public static final String DOCKER_STORM_WORKING_DIR = "/opt/storm/build/bin";

	private StormExecutionEnvironment executionEnvironment;
	private IStormProgram<S> stormProgram;
	private InstanceFileGenerator instanceFileGenerator;
	
	/**
	 * Standard constructor
	 * @param stormProgram the storm program to execute
	 * @param executionEnvironment the execution environemnt for the runner
	 */
	public StormRunner(IStormProgram<S> stormProgram, StormExecutionEnvironment executionEnvironment) {
		this.stormProgram = stormProgram;
		this.executionEnvironment = executionEnvironment;
		this.instanceFileGenerator = new InstanceFileGenerator(executionEnvironment);
	}
	
	/**
	 * Run a storm command
	 * @param commandWithArgs the command + its arguments
	 * @return returns the console output
	 * @throws IOException if the execution failed
	 * @throws URISyntaxException 
	 */
	private List<S> run(String[] commandWithArgs) throws IOException, URISyntaxException {
		if (executionEnvironment.equals(StormExecutionEnvironment.Docker)) {
			return runStormInDocker(commandWithArgs);
		} else {
			return runStormLocally(commandWithArgs);
		}
	}
	
	/**
	 * Runs STORM in a local execution environment
	 * @param commandWithArgs the STORM command to execute
	 * @return the results of the execution
	 * @throws IOException 
	 */
	private List<S> runStormLocally(String[] commandWithArgs) throws IOException {
		InputStream resultStream = executeCommand(commandWithArgs);
		List<S> results = stormProgram.extractResult(readResult(resultStream));
		return results;
	}
	
	/**
	 * Overwriteable method for simplifying testing
	 * @param commandWithArgs the command to execute
	 * @return the runtime to execute a command line command
	 * @throws IOException 
	 */
	protected InputStream executeCommand(String[] commandWithArgs) throws IOException {
		Process process = Runtime.getRuntime().exec(commandWithArgs);
		return process.getInputStream();
	}

	/**
	 * Runs STORM in a docker execution environment
	 * @param commandWithArgs the STORM command to execute
	 * @return the results of the execution
	 * @throws IOException 
	 */
	private List<S> runStormInDocker(String[] commandWithArgs) throws IOException  {
		try {
			// Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars,
			// If you have just setup docker, do a system restart before this, otherwise
			// we cant read the enviroment variables for building the docker client!
			DockerClient docker = DefaultDockerClient.fromEnv().build();
			DockerHelper dockerHelper = new DockerHelper(docker);
			
			// Adjust the formatting of the command
			commandWithArgs[0] = "./" + commandWithArgs[0];
			
			pullStormImageIfNeeded(docker);
			
			String id = createContainer(docker);
			System.out.println("Created new Docker Container with ID " +  id);
			System.out.println("Executing command: " + String.join(" ", commandWithArgs));
			List<String> result = dockerHelper.run(id, commandWithArgs);
			dockerHelper.destroyContainer(id);
			System.out.println("Docker cleanup complete");
			return stormProgram.extractResult(result);
		} catch (DockerCertificateException | DockerException | InterruptedException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Checks if the STORM image has been pulled, and if not pulls it
	 * @param docker the docker instance
	 * @throws DockerException 
	 * @throws InterruptedException 
	 */
	private void pullStormImageIfNeeded(DockerClient docker) throws DockerException, InterruptedException {
		DockerHelper dockerHelper = new DockerHelper(docker);
		if (!dockerHelper.isImagePulled(DOCKER_IMAGE_STORM)) {
			System.out.print("Docker Image " +  DOCKER_IMAGE_STORM + " has not been pulled. Pulling... ");
			docker.pull(DOCKER_IMAGE_STORM);
			System.out.println("successfull");
		}
	}
	
	/**
	 * Creates & setups a STORM docker container
	 * @param docker the docker instance
	 * @return ID of the created STORM docker container
	 * @throws DockerException 
	 * @throws InterruptedException 
	 */
	private String createContainer(DockerClient docker) throws DockerException, InterruptedException {
		String mount = DockerHelper.convertDOSPathToDockerPath(DOCKER_TEMP_PATH);
		HostConfig hostConfig = DockerHelper.getDockerHostConfig(Bind.from(mount).to(DOCKER_DATA_PATH).build());
		
		// Create container with exposed ports
		ContainerConfig containerConfig = ContainerConfig.builder()
		    .hostConfig(hostConfig)
		    .image(DOCKER_IMAGE_STORM)
		    .cmd("sh", "-c", "while :; do sleep 1; done")
		    .workingDir(DOCKER_STORM_WORKING_DIR)
		    .build();
		
		ContainerCreation creation = docker.createContainer(containerConfig);
		String id = creation.id();
		docker.startContainer(id);
		return id;
	}
	
	/**
	 * Main method for calling the storm binary
	 * @return the textual result of the call
	 * @throws IOException thrown if there is a file problem
	 * @throws URISyntaxException thrown if there is a file problem
	 */
	public List<S> run() throws IOException, URISyntaxException {
		String[] localInstanceFilePaths = stormProgram.createInstanceFiles(instanceFileGenerator);
		List<String> instanceFilePathsList = new ArrayList<>();
		for (String localPaths : localInstanceFilePaths) {
			instanceFilePathsList.add(instanceFileGenerator.createFilePath(localPaths));
		}
		
		String[] commandWithArgs = stormProgram.buildCommandWithArgs(instanceFilePathsList.stream().toArray(String[]::new));
		List<S> result = run(commandWithArgs);
		return result;
	}

	
	/**
	 * Reads the result from the storm call
	 * @param is the stream containing the storm call output
	 * @return the result of the call by means of a list of lines
	 * @throws IOException thrown if there was a problem in reading the process output stream
	 */
	protected List<String> readResult(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return br.lines().collect(Collectors.toList());
	}
}
