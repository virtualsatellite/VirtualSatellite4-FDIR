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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Detectability;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToDetection;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

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
		synthesizer = new POSynthesizer();
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateObsOr2ObsBe2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2ObsBe2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final double EXPECTED_MTTD = 0.75;
		final double EXPECTED_STEADY_STATE_DETECTABILITY = 1;
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 0;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, 
				Detectability.UNIT_DETECTABILITY, MeanTimeToDetection.MTTD, SteadyStateDetectability.STEADY_STATE_DETECTABILITY);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals("MTTD has correct value", EXPECTED_MTTD, result.getMeanTimeToDetection(), TEST_EPSILON);
		final double TEST_EPSILON = 0.00001;
		assertEquals("Steady State Detectability has correct value", EXPECTED_STEADY_STATE_DETECTABILITY, result.getSteadyStateDetectability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsCsp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsCsp2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1;
		final double EXPECTED_MTTD = 2.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, MTTF.MTTF, MeanTimeToDetection.MTTD);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_MTTD, result.getMeanTimeToDetection(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2BEDelayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2BEDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 0.333333333333;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testSynthesizeObsOr2Csp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		final double EXPECTED_MTTF = 1.125;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 3;
		final int EXPECTED_COUNT_TRANSITIONS = 3;
		final double EXPECTED_MTTF = 0.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2SharedSafePrimaryDelayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2SharedSafePrimaryDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2SharedSafeSpareDelayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2SharedSafeSpareDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		final double EXPECTED_MTTF = 0.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2SharedDelayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2SharedSafeSpareDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		final double EXPECTED_MTTF = 0.5;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBE() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBE.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 3;
		final int EXPECTED_COUNT_TRANSITIONS = 4;
		final double EXPECTED_MTTF = 0.916666;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBEDelayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBEDelayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 5;
		final double EXPECTED_MTTF = 1.15;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBEUnreliable() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBEUnreliable.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 7;
		final int EXPECTED_COUNT_TRANSITIONS = 10;
		final double EXPECTED_MTTF = 0.9126984126984126;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsMemory2RDEP() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsMemory2RDEP.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		List<Transition> transitions = new RecoveryAutomatonHelper(concept).getCurrentTransitions(ra).get(ra.getInitial());
		
		final int EXPECTED_NUMBER_TRANSITIONS = 3;
		assertEquals(EXPECTED_NUMBER_TRANSITIONS, transitions.size());
		TimeoutTransition timeoutTransition = null;
		for (Transition transition : transitions) {
			if (transition instanceof TimeoutTransition) {
				timeoutTransition = (TimeoutTransition) transition;
			}
		}
		
		assertNotNull(timeoutTransition);
		final int EXPECTED_TRANSITION_TIME = 10000;
		assertEquals(EXPECTED_TRANSITION_TIME, timeoutTransition.getTime(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2BeCsp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2BeCsp2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 1;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 0.9090909090909091;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Repair1() throws IOException {
		Fault fault = createDFT("/resources/galileoObsRepair/obsCsp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final double EXPECTED_MTTF = 1.7;
		final double EXPECTED_SSA = 0.3333997640435906;
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, MTTF.MTTF, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Repair1Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObsRepair/obsCsp2Repair1Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final double EXPECTED_SSA = 0.3253747856518212;
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 7;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2ObsRepair2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObsRepair/obsCsp2ObsRepair2Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final double EXPECTED_SSA = 0.15405986627847423;
		final int EXPECTED_COUNT_STATES = 9;
		final int EXPECTED_COUNT_TRANSITIONS = 17;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2Repair1Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObsRepair/obsOr2Csp2Repair1Delayed.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		// SSA computation isnt stable yet, at least guarantee that its non-zero
		assertNotEquals(0, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
}
