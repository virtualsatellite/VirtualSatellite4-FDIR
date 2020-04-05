/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.docker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.google.common.collect.ImmutableSet;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.HostConfig.Bind;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.PortBinding;

import de.dlr.sc.virsat.fdir.storm.files.IFileProvider;

/**
 * Helper class for interacting with docker
 * 
 * @author muel_s8
 *
 */

public class DockerHelper {

	private DockerClient client;

	/**
	 * Standard constructor
	 * 
	 * @param client
	 *            the docker client
	 */
	public DockerHelper(DockerClient client) {
		this.client = client;
	}

	/**
	 * Checks if an image has been pulled
	 * 
	 * @param imageName
	 *            the name of the image
	 * @return true iff the image has been pulled
	 * @throws DockerException
	 *             if docker fails to list the available images
	 * @throws InterruptedException
	 *             if docker fails to list the available images
	 */
	public boolean isImagePulled(String imageName) throws DockerException, InterruptedException {
		List<Image> images = client.listImages();
		for (Image image : images) {
			if (image.repoTags().contains(imageName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Completely removes the container from the docker system
	 * 
	 * @param id
	 *            the id of the container
	 * @throws DockerException dockerexception
	 * @throws InterruptedException interruptedException
	 */
	public void destroyContainer(String id) throws DockerException, InterruptedException {
		client.killContainer(id);
		client.removeContainer(id);
		client.close();
	}

	/**
	 * Executes the given command in the docker container of the given ID
	 * 
	 * @param id
	 *            the docker container ID
	 * @param commandWithArgs
	 *            the command to execute
	 * @return the result of the command
	 * @throws DockerException dockerException
	 * @throws InterruptedException interruptedException
	 * @throws IOException ioException
	 */
	public List<String> run(String id, String[] commandWithArgs)
			throws DockerException, InterruptedException, IOException {
		ExecCreation execCreation = client.execCreate(id, commandWithArgs, DockerClient.ExecCreateParam.attachStdout(),
				DockerClient.ExecCreateParam.attachStderr());
		LogStream output = client.execStart(execCreation.id());
		return Arrays.asList(output.readFully().split("\n"));
	}

	public static final char SEPARATOR_CHAR = '/';
	public static final String SEPARATOR = "" + SEPARATOR_CHAR;
	public static final char PATH_SEPARATOR_CHAR = ':';
	public static final String PATH_SEPARATOR = "" + PATH_SEPARATOR_CHAR;

	/**
	 * Converts a windows path to a docker compatible path
	 * 
	 * @param dosPath
	 *            the windows dos path
	 * @return a docker compatible path
	 */
	public static String convertDOSPathToDockerPath(String dosPath) {
		String[] wPathSplit = dosPath.split(PATH_SEPARATOR);
		String result = SEPARATOR + SEPARATOR + wPathSplit[0].toLowerCase()
				+ wPathSplit[1].replace(PATH_SEPARATOR, "").replace('\\', SEPARATOR_CHAR);
		return result;
	}

	/**
	 * Creates a standard host configurtion
	 * 
	 * @param binds
	 *            volume binds
	 * @return the host configuration
	 */
	public static HostConfig getDockerHostConfig(Bind... binds) {
		// Bind container ports to host ports
		final String[] ports = { "80", "22" };
		final Map<String, List<PortBinding>> portBindings = new HashMap<>();
		for (String port : ports) {
			List<PortBinding> hostPorts = new ArrayList<>();
			hostPorts.add(PortBinding.of("0.0.0.0", port));
			portBindings.put(port, hostPorts);
		}

		// Bind container port 443 to an automatically allocated available host port.
		List<PortBinding> randomPort = new ArrayList<>();
		randomPort.add(PortBinding.randomPort("0.0.0.0"));
		portBindings.put("443", randomPort);

		return HostConfig.builder().portBindings(portBindings).appendBinds(binds).build();
	}
	
	/**
	 * 
	 * @param id container id
	 * @return fileContent
	 */
	public IFileProvider createFileProvider(String id) {
		return new IFileProvider() {
			@Override
			public String getFile(String fileName) {
				String result = null;
				try {
					client.info();
					ImmutableSet.Builder<String> files = ImmutableSet.builder();
					try (TarArchiveInputStream tarStream = new TarArchiveInputStream(
							client.archiveContainer(id, "/opt/storm/build/bin"))) {
						TarArchiveEntry entry;
						while ((entry = tarStream.getNextTarEntry()) != null) {
							files.add(entry.getName());
							if (entry.getName().contains(fileName)) {
								try (BufferedReader reader = new BufferedReader(new InputStreamReader(tarStream))) {
									result = reader.lines()
											.collect(Collectors.joining("\n"));
								}
							}
						}
					}
				} catch (DockerException | InterruptedException | IOException e) {
					e.printStackTrace();
				}
				return result;
			}
		};
	}
}
