/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.dlr.sc.virsat.model.extension.fdir.converter.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryAutomatonStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.test.TestActivator;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class BasicSynthesizerTest extends ATestCase {

	@Test
	public void testEvaluateCsp2() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/csp2.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		System.out.println(ra.toDot());
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}

	@Test
	public void testEvaluate2Csp2Shared() throws IOException {
		final double[] EXPECTED = {
			1.55e-05,
			0.0001194,
			0.0003891,
			0.0008909
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/2csp2Shared.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
	@Test
	public void testEvaluateHECS11() throws IOException {
		InputStream is = TestActivator.getResourceContentAsString("/resources/benchmarks/hecs/hecs_1_1_0_np.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		System.out.println("\nPRINTING INFO\n===============================");
		for (State s : ra.getStates()) {
			System.out.println(s.getName());
		}
		for (Transition t : ra.getTransitions()) {
			System.out.println("Name: " + t.toString() + ", from: " + t.getFrom().getName() + ", to: " + t.getTo().getName() + ", isRepair?: " + t.getIsRepair());
		}
		
		final int NUM_STATES = 2;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
}
