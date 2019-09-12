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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

/**
 * This class tests the Storm program
 * @author muel_s8
 *
 */

public class StormTest {

	MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
	
	@Test
	public void testBuildCommandWithArgs() {
		Storm storm = new Storm(ma, 0);
		String[] command = storm.buildCommandWithArgs(new String[] { "a.drn", "b.prop" }, false);
		final String[] EXPECTED_COMMAND = { "storm", "--explicit-drn", "a.drn", "--io:to-nondet", "--prop", "b.prop" };
		assertArrayEquals(EXPECTED_COMMAND, command);
	}
	
	@Test
	public void testExtractResults() {
		List<String> resultsString = Arrays.asList("1", "Result (for initial states): 0.1, inf");
		
		Storm storm = new Storm(ma, 0);
		List<Double> resultsDouble = storm.extractResult(resultsString);
		
		final List<Double> EXPECTED_RESULTS_DOUBLE = Arrays.asList(0.1, Double.POSITIVE_INFINITY);
		assertEquals(EXPECTED_RESULTS_DOUBLE, resultsDouble);
	}

}
