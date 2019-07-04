/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.storm.runner.IStormProgram;
import de.dlr.sc.virsat.fdir.storm.runner.StormExecutionEnvironment;
import de.dlr.sc.virsat.fdir.storm.runner.StormRunner;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;

/**
 * This class tests the StormEvaluator implementation
 * @author muel_s8
 *
 */
public class StormEvaluatorTest {

	@Test
	public void testEvaluateFaultTree() {
		String conceptXmiPluginPath = "de.dlr.sc.virsat.model.extension.fdir/concept/concept.xmi";
		Concept concept = de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader.loadConceptFromPlugin(conceptXmiPluginPath);
		
		StormEvaluator stormEvaluator = new StormEvaluator(1) {
			// Mock the creation of the StormRunner to simulate the Storm program returning some values
			@Override
			protected StormRunner<Double> createStormRunner(IStormProgram<Double> storm) {
				return new StormRunner<Double>(storm, StormExecutionEnvironment.Local) {
					@Override
					public List<Double> run() throws IOException, URISyntaxException {
						return Arrays.asList(1d, 0d);
					}
				};
			}
		};
		
		Fault fault = new Fault(concept);
		stormEvaluator.evaluateFaultTree(fault, MTTF.MTTF, new Reliability(1));
		
		final double EPS = 0.001;
		assertEquals(1, stormEvaluator.getMeanTimeToFailure(), EPS);
		assertEquals(1, stormEvaluator.getFailRates().size());
		assertEquals(0, stormEvaluator.getFailRates().get(0), EPS);
	}
	
}
