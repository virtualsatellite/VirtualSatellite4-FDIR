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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.storm.files.InstanceFileGenerator;

/**
 * This class tests the StormRunner implementation.
 * @author muel_s8
 *
 */

public class StormRunnerTest {

	private static final List<Integer> MOCK_RESULT = Arrays.asList(1);
	
	/**
	 * Mock storm program for testing purposes
	 * @author muel_s8
	 *
	 */
	private class MockStormProgram implements IStormProgram<Integer> {

		@Override
		public String getExecutableName() {
			return "Mock Program";
		}

		@Override
		public String[] createInstanceFiles(InstanceFileGenerator fileGenerator) throws IOException {
			return new String[] { "mock.file" };
		}

		@Override
		public String[] buildCommandWithArgs(String[] instanceFilePath, boolean schedule) {
			return instanceFilePath;
		}

		@Override
		public List<Integer> extractResult(List<String> result) {
			return MOCK_RESULT;
		}

	}
	
	@Test
	public void testRun() throws IOException, URISyntaxException {
		StormRunner<Integer> stormRunner = new StormRunner<Integer>(new MockStormProgram(), StormExecutionEnvironment.Local) {
			@Override
			protected InputStream executeCommand(String[] commandWithArgs) throws IOException {
				return new ByteArrayInputStream("test data".getBytes());
			}
		};
		List<Integer> result = stormRunner.run();
		assertEquals(MOCK_RESULT, result);
	}
}
