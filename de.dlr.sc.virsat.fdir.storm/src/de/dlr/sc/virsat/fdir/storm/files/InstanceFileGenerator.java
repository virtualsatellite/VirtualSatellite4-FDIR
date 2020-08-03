/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.storm.files;

import java.io.File;
import java.io.IOException;

import de.dlr.sc.virsat.fdir.storm.docker.DockerHelper;
import de.dlr.sc.virsat.fdir.storm.runner.StormExecutionEnvironment;
import de.dlr.sc.virsat.fdir.storm.runner.StormRunner;

/**
 * This class manages the creation of input files for storm, taking possible
 * dockerization into account.
 * @author muel_s8
 *
 */

public class InstanceFileGenerator {

	public static final String DOCKER_TEMP_PATH = "C:\\Users\\" + System.getProperty("user.name") + "\\TEMP";
	
	private StormExecutionEnvironment executionEnvironment;

	/**
	 * Standard constructor
	 * @param executionEnvironment the execution environemnt
	 */
	public InstanceFileGenerator(StormExecutionEnvironment executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}
	
	/**
	 * Creates a new instance file of the given file type
	 * @param fileType the type of the file
	 * @return the path to the file
	 * @throws IOException if the file creation fails
	 */
	public String generateInstanceFile(String fileType) throws IOException {
		File stormFile;

		if (executionEnvironment.equals(StormExecutionEnvironment.Docker)) {
			System.out.println("Creating folder for temporary STORM input files at " + DOCKER_TEMP_PATH);
			File tempFolder = new File(DOCKER_TEMP_PATH);
			if (!tempFolder.exists()) {
				tempFolder.mkdirs();
			}
			stormFile = File.createTempFile("storm_instance_", "." + fileType, tempFolder);
		} else {
			stormFile = File.createTempFile("storm_instance_", "." + fileType);
		}

		stormFile.deleteOnExit();

	    String instanceFilePath = stormFile.getAbsolutePath();
	    
	    System.out.println("Created temporary STORM input file " + instanceFilePath);

	    return instanceFilePath;
	}
	/**
	 * 
	 * @param instanceFilePath local File Path
	 * @return filePath
	 */
	public String createFilePath(String instanceFilePath) {
		if (executionEnvironment.equals(StormExecutionEnvironment.Docker)) {
	    	String instanceFileDockerPath = DockerHelper.convertDOSPathToDockerPath(instanceFilePath);
	    	String[] instanceFileDockerPathSplit = instanceFileDockerPath.split("/");
	    	return  StormRunner.DOCKER_DATA_PATH + "/" + instanceFileDockerPathSplit[instanceFileDockerPathSplit.length - 1];
	    } else {
	    	return instanceFilePath;
	    }
	}
}
