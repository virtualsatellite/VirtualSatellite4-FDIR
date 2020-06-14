/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

public class BasicSynthesizerTest extends ATestCase {

	protected BasicSynthesizer synthesizer;
	protected FaultTreeEvaluator ftEvaluator;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		synthesizer = new BasicSynthesizer();
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
	}

	@Test
	public void testEvaluateCsp1() throws IOException {
		final double EXPECTED_MTTF = 1;
		
		FaultTreeNode root = createBasicDFT("/resources/galileo/csp1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int NUM_STATES = 1;
		final int NUM_TRANSITIONS = 0;
		assertEquals(NUM_STATES, ra.getStates().size());
		assertEquals(NUM_TRANSITIONS, ra.getTransitions().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCsp2() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		
		FaultTreeNode root = createBasicDFT("/resources/galileo/csp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(root).getFailRates(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair1() throws IOException {
		final double[] EXPECTED = {
			0.9999016501226037,
			0.9996130705668653, 
			0.9991436794433161, 
			0.9985025233486501
		};
		final double EXPECTED_SSA = 0.5000005000000001;
		
		FaultTreeNode root = createBasicDFT("/resources/galileoRepair/csp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.SSA);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}

	@Test
	public void testSynthesizeCsp2Repair2BadPrimary() throws IOException {
		final double[] EXPECTED = {
			0.9999019760517075, 
			0.9996156202699334, 
			0.999152094789556, 
			0.9985220320778355
		};
		final double EXPECTED_SSA = 0.7142864724603194;
		
		FaultTreeNode root = createBasicDFT("/resources/galileoRepair/csp2Repair2BadPrimary.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int NUM_STATES = 4;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.SSA);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair2BadSpare() throws IOException {
		final double[] EXPECTED = {
			0.9999018132903527, 
			0.9996143485896306, 
			0.999147902777298, 
			0.9985123259997293
		};
		final double EXPECTED_SSA = 0.6470577570952566;
		
		FaultTreeNode root = createBasicDFT("/resources/galileoRepair/csp2Repair2BadSpare.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int NUM_STATES = 5;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.SSA);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}
	
	@Test
	public void testEvaluateCsp2Or2Exp1Prob1Exp1() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoUniform/csp2Or2Exp1Prob1Exp1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int NUM_STATES = 2;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		final double EXPECTED_MTTF = 0.4833333333333333;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
}
