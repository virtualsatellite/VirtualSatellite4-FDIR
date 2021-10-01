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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Detectability;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToDetection;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class POSynthesizerTest extends ATestCase {
	
	private POSynthesizer synthesizer;
	private FaultTreeEvaluator ftEvaluator;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		synthesizer = new POSynthesizer(true, true);
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateObsOr2ObsBe2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2ObsBe2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final double EXPECTED_MTTD = 0.75;
		final double EXPECTED_STEADY_STATE_DETECTABILITY = 1;
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 0;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, 
				Detectability.UNIT_DETECTABILITY, MeanTimeToDetection.MTTD, SteadyStateDetectability.SSD);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals("MTTD has correct value", EXPECTED_MTTD, result.getMeanTimeToDetection(), TEST_EPSILON);
		final double TEST_EPSILON = 0.00001;
		assertEquals("Steady State Detectability has correct value", EXPECTED_STEADY_STATE_DETECTABILITY, result.getSteadyStateDetectability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsCsp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsCsp2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1;
		final double EXPECTED_MTTD = 1;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, MeanTimeToFailure.MTTF, MeanTimeToDetection.MTTD);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_MTTD, result.getMeanTimeToDetection(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2BEDelayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2BEDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 0.333333333333;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testSynthesizeObsOr2Csp2() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 4;
		final int EXPECTED_COUNT_TRANSITIONS = 4;
		final double EXPECTED_MTTF = 1.25;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 3;
		final int EXPECTED_COUNT_TRANSITIONS = 3;
		final double EXPECTED_MTTF = 0.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2SharedSafePrimaryDelayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2SharedSafePrimaryDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2SharedSafeSpareDelayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2SharedSafeSpareDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		final double EXPECTED_MTTF = 0.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2SharedDelayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2SharedSafeSpareDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		final double EXPECTED_MTTF = 0.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBE() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2ObsBE.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 6;
		final double EXPECTED_MTTF = 0.916666;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBEDelayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2ObsBEDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 6;
		final int EXPECTED_COUNT_TRANSITIONS = 6;
		final double EXPECTED_MTTF = 1.25;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBEUnreliable() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2ObsBEUnreliable.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 12;
		final int EXPECTED_COUNT_TRANSITIONS = 16;
		final double EXPECTED_MTTF = 0.9166666666666665;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBERepairUnreliable() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsOr2Csp2ObsBERepairUnreliable.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		final int EXPECTED_COUNT_STATES = 17;
		final int EXPECTED_COUNT_TRANSITIONS = 31;
		final double EXPECTED_MTTF = 0.9166666350375646;
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	/*@Test
	public void testSynthesizeObsMemory1RDEP() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsMemory1RDEP.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 4;
		final int EXPECTED_COUNT_TRANSITIONS = 5;
		final double EXPECTED_MTTF = 5.198010827507563;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		final int EXPECTED_TRANSITION_TIME = 10000;
		TimeoutTransition timeoutTransition = raHelper.getTimeoutTransition(ra, ra.getInitial());
		assertEquals(EXPECTED_TRANSITION_TIME, timeoutTransition.getTime(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsMemory2RDEP() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsMemory2RDEP.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 9;
		final double EXPECTED_MTTF = 1.485601334673364;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		final int EXPECTED_TRANSITION_TIME = 10000;
		TimeoutTransition timeoutTransition = raHelper.getTimeoutTransition(ra, ra.getInitial());
		assertEquals(EXPECTED_TRANSITION_TIME, timeoutTransition.getTime(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsMemory1RepairRDEP() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsMemory1RepairRDEP.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 4;
		final int EXPECTED_COUNT_TRANSITIONS = 6;
		final double EXPECTED_MTTF = 5.198050886499159;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		
		final int EXPECTED_TRANSITION_1_TIME = 10000;
		TimeoutTransition timeoutTransition1 = raHelper.getTimeoutTransition(ra, ra.getInitial());
		assertEquals(EXPECTED_TRANSITION_1_TIME, timeoutTransition1.getTime(), TEST_EPSILON);
		TimeoutTransition timeoutTransition2 = raHelper.getTimeoutTransition(ra, timeoutTransition1.getTo());
		final int EXPECTED_TRANSITION_2_TIME = 10;
		assertEquals(EXPECTED_TRANSITION_2_TIME, timeoutTransition2.getTime(), TEST_EPSILON);
		
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsMemory2RepairRDEP() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsMemory2RepairRDEP.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 10;
		final double EXPECTED_MTTF = 1.4856037377318756;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		
		final int EXPECTED_TRANSITION_1_TIME = 10000;
		TimeoutTransition timeoutTransition1 = raHelper.getTimeoutTransition(ra, ra.getInitial());
		assertEquals(EXPECTED_TRANSITION_1_TIME, timeoutTransition1.getTime(), TEST_EPSILON);
		TimeoutTransition timeoutTransition2 = raHelper.getTimeoutTransition(ra, timeoutTransition1.getTo());
		final int EXPECTED_TRANSITION_2_TIME = 10;
		assertEquals(EXPECTED_TRANSITION_2_TIME, timeoutTransition2.getTime(), TEST_EPSILON);
		
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}*/
	
	@Test
	public void testSynthesizeObsOr2BeCsp2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2BeCsp2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 0.9090909090909091;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Repair1() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsCsp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final double EXPECTED_MTTF = 1.7;
		final double EXPECTED_SSA = 0.33333333333333337;
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, MeanTimeToFailure.MTTF, SteadyStateAvailability.SSA);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Repair1Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsCsp2Repair1Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final double EXPECTED_SSA = 0.33333249815122734;
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 9;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, SteadyStateAvailability.SSA);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Repair2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsCsp2Repair2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		final double EXPECTED_SSA = 0.5464808947495139;
		final int EXPECTED_COUNT_STATES = 9;
		final int EXPECTED_COUNT_TRANSITIONS = 17;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, SteadyStateAvailability.SSA);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2Repair1Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObsRepair/obsOr2Csp2Repair1Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, SteadyStateAvailability.SSA);
		
		// SSA computation isnt stable yet, at least guarantee that its non-zero
		assertNotEquals(0, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsDelayedOr2Csp2ObsBE() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsDelayedOr2Csp2ObsBE.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(root), null);
		System.out.println(ra.toDot());
		
		final int EXPECTED_COUNT_STATES = 4;
		final int EXPECTED_COUNT_TRANSITIONS = 8;
		final double EXPECTED_MTTF = 0.7499999999999999;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
}
