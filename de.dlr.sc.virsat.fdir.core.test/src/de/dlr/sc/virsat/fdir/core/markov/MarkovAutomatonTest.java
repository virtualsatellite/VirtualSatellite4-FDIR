/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the MarkovAutomaton class
 * @author muel_s8
 *
 */

public class MarkovAutomatonTest {

	@Test
	public void testAddNonderministicTransition() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		
		ma.addState(state1);
		ma.addState(state2);
		
		assertTrue(state1.isMarkovian());
		assertTrue(state2.isMarkovian());
		
		ma.addNondeterministicTransition("a", state1, state2);
		
		assertFalse(state1.isMarkovian());
		assertTrue(state2.isMarkovian());
		
		assertEquals(1, ma.getSuccTransitions(state1).size());
		assertEquals(1, ma.getPredTransitions(state2).size());
		assertEquals(1, ma.getTransitions("a").size());
	}
	
	@Test
	public void testAddState() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state = new MarkovState();
		ma.addState(state);
		
		assertEquals(1, ma.getStates().size());
	}
	
	@Test
	public void testRemoveState() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		
		ma.addState(state1);
		ma.addState(state2);
		ma.addMarkovianTransition("a", state1, state2, 1.0);
		
		ma.removeState(state1);
		
		assertEquals(1, ma.getStates().size());
		assertTrue(ma.getTransitions("a").isEmpty());
	}
	
	@Test
	public void testIsCTMC() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		
		ma.addState(state1);
		ma.addState(state2);
		ma.addMarkovianTransition("a", state1, state2, 1.0);
		assertTrue(ma.isCTMC());
		
		ma.addNondeterministicTransition("b", state2, state1);
		assertFalse(ma.isCTMC());
	}
	
	@Test
	public void testGetExitRateForState() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		
		MarkovState state3 = new MarkovState();
		MarkovState state4 = new MarkovState();

		
		ma.getFinalStates().add(state2);
		
		ma.addState(state1);
		ma.addState(state2);
		ma.addState(state3);
		ma.addState(state4);
		
		final double RATE1 = 0.8;
		final double RATE2 = 0.2;
		final double RATE3 = 0.1;
		final double RATE4 = 0.4;
		final double RATE5 = 0.3;
		
		final double EXPECTEDEXITRATE = 1.25;

		
		ma.addMarkovianTransition("a", state1, state2, RATE1);
		ma.addMarkovianTransition("b", state1, state2, RATE2);
		
		ma.addMarkovianTransition("c", state3, state3, RATE3);
		ma.addMarkovianTransition("c", state3, state4, RATE4);
		ma.addMarkovianTransition("d", state3, state4, RATE5);


		
		assertEquals(1, ma.getExitRateForState(state1), 0);
		assertEquals(0, ma.getExitRateForState(state2), 0);
		assertEquals(EXPECTEDEXITRATE, ma.getExitRateForState(state3), 0);

		
	}
}
