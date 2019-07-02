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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.storm.runner.StormExecutionEnvironment;

/**
 * This class tests the IntanceFileGenerator
 * @author muel_s8
 *
 */

public class InstanceFileGeneratorTest {

	@Test
	public void testCreateFilePathDocker() {
		InstanceFileGenerator instanceFileGenerator = new InstanceFileGenerator(StormExecutionEnvironment.Docker);
		String filePath = instanceFileGenerator.createFilePath("c:\\test");
		
		final String EXPECTED_FILED_PATH = "/data/test";
		assertEquals(EXPECTED_FILED_PATH, filePath);
	}

	@Test
	public void testCreateFilePathLocal() {
		InstanceFileGenerator instanceFileGenerator = new InstanceFileGenerator(StormExecutionEnvironment.Local);
		String filePath = instanceFileGenerator.createFilePath("c:\\test");
		
		final String EXPECTED_FILED_PATH = "c:\\test";
		assertEquals(EXPECTED_FILED_PATH, filePath);
	}
	
	@Test
	public void testGenerateInstanceFileLocal() throws IOException {
		InstanceFileGenerator instanceFileGenerator = new InstanceFileGenerator(StormExecutionEnvironment.Local);
		String filePath = instanceFileGenerator.generateInstanceFile("dft");
		
		File file = new File(filePath);
		assertTrue(file.exists());
	}
	
}
