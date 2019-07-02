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

import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;

/**
 * This class tests the StormDFT Program class
 * @author muel_s8
 *
 */

public class StormDFTTest {

	@Test
	public void testBuildCommandWithArgs() {
		StormDFT stormDFT = new StormDFT();
		stormDFT.addMetric(new Reliability(1));
		stormDFT.addMetric(MTTF.MTTF);
		stormDFT.setDelta(1);
		String[] instancePath = {"//c/Users/test"};
		String[] commandWithArgs = stormDFT.buildCommandWithArgs(instancePath);
		
		assertArrayEquals(
			new String[] { "storm-dft", "-dft", "//c/Users/test", "-symred", "--timepoints", "0", "1.0", "1.0", "-mttf" }, 
			commandWithArgs
		); 
	}
	
	@Test
	public void testExtractResult() {
		StormDFT stormDFT = new StormDFT();
		List<String> result = Arrays.asList("Some header information\n", "\n", "Result: [0.0, 1.0]");
		
		List<Double> extractedResult = stormDFT.extractResult(result);
		
		assertEquals(Arrays.asList(0.0, 1.0), extractedResult);
	}

}
