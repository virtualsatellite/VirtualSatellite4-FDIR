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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.TimedTransition;
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
		
		final int EXPECTED_COUNT_STATES = 2;
		final int EXPECTED_COUNT_TRANSITIONS = 1;
		final double EXPECTED_MTTF = 1;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testSynthesizeObsOr2Csp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 3;
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
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 5;
		final int EXPECTED_COUNT_TRANSITIONS = 6;
		final double EXPECTED_MTTF = 1.125;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBE() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBE.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 4;
		final int EXPECTED_COUNT_TRANSITIONS = 5;
		final double EXPECTED_MTTF = 0.916666;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBEUnreliable() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBEUnreliable.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 8;
		final int EXPECTED_COUNT_TRANSITIONS = 12;
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
		TimedTransition timedTransition = null;
		for (Transition transition : transitions) {
			if (transition instanceof TimedTransition) {
				timedTransition = (TimedTransition) transition;
			}
		}
		
		assertNotNull(timedTransition);
		final int EXPECTED_TRANSITION_TIME = 10000;
		assertEquals(EXPECTED_TRANSITION_TIME, timedTransition.getTime(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Repair1() throws IOException {
		Fault fault = createDFT("/resources/galileoObsRepair/obsCsp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int EXPECTED_COUNT_STATES = 3;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		final double EXPECTED_MTTF = 1.7;
		
		assertEquals(EXPECTED_COUNT_STATES, ra.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ra.getTransitions().size());
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
}
