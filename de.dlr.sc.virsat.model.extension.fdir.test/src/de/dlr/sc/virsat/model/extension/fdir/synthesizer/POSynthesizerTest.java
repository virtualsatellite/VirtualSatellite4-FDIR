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

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class POSynthesizerTest extends ATestCase {
	
	@Test
	public void testEvaluateObsCsp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsCsp2.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}
	
	@Test
	public void testEvaluateObsCsp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsCsp2Delayed.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}

	@Test
	public void testEvaluateObsOr2Csp2() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}
	
	@Test
	public void testEvaluateObsOr2Csp2Delayed() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2Delayed.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		assertEquals(1, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
	}
	
	@Test
	public void testEvaluateObsOr2Csp2ObsBE() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBE.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		final int EXPECTED_NUMBER_STATES = 3;
		final int EXPECTED_NUMBER_TRANSITIONS = 3;
		assertEquals(EXPECTED_NUMBER_STATES, ra.getStates().size());
		assertEquals(EXPECTED_NUMBER_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testEvaluateObsOr2Csp2ObsBEUnreliable() throws IOException {
		Fault fault = createDFT("/resources/galileoObs/obsOr2Csp2ObsBEUnreliable.dft");
		POSynthesizer synthesizer = new POSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		final int EXPECTED_NUMBER_STATES = 4;
		final int EXPECTED_NUMBER_TRANSITIONS = 6;
		assertEquals(EXPECTED_NUMBER_STATES, ra.getStates().size());
		assertEquals(EXPECTED_NUMBER_TRANSITIONS, ra.getTransitions().size());
	}
}
