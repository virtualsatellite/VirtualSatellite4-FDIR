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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;
import org.mandas.docker.client.messages.HostConfig;
import org.mandas.docker.client.messages.HostConfig.Bind;

/**
 * This class tests the DockerHelper class
 
import org.mandas.docker.client.messages.HostConfig.Bind;* @author muel_s8
 *
 */

public class DockerHelperTest {

	@Test
	public void testConvertDOSPathToDockerPath() {
		String dosPath = "C:\\Users\\test\\";
		String dockerPath = DockerHelper.convertDOSPathToDockerPath(dosPath);
		assertEquals("//c/Users/test/", dockerPath);
	}
	
	@Test
	public void testGetDockerHostConfig() {
		HostConfig hostConfig = DockerHelper.getDockerHostConfig(Bind.builder().from("//c/").to("//d/").build());
		assertNotNull(hostConfig);
		assertEquals(Arrays.asList("//c/://d/"), hostConfig.binds());
	}

}
