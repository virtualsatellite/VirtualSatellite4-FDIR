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

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.TimedTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class POSynthesizerTest extends ATestCase {
	
	@Test
	public void testSynthesizeObsCsp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsCsp2.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}
	
	@Test
	public void testSynthesizeObsCsp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsCsp2Delayed.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}

	@Test
	public void testSynthesizeObsOr2Csp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2Delayed.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBE() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBE.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		System.out.println(ra.toDot());
		// TODO: correct assertions
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2ObsBEUnreliable() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBEUnreliable.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		System.out.println(ra.toDot());
		// TODO: correct assertions
	}
	
	@Test
	public void testSynthesizeObsMemory2RDEP() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsMemory2RDEP.dft");
		ASynthesizer synthesizer = new POSynthesizer();
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
}
