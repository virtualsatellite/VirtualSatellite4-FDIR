/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

/**
 * This class tests the markov automaton builder class
 * @author muel_s8
 *
 */
public class MarkovAutomatonBuilderTest {
	
	@Test
	public void testBuild() {
		MarkovAutomatonBuilder<MarkovState> maBuilder = new MarkovAutomatonBuilder<MarkovState>();
		
		MarkovState initialState = new MarkovState();
		List<MarkovState> initialStateSuccs = Arrays.asList(new MarkovState(), new MarkovState());
		
		AStateSpaceGenerator<MarkovState> mockStateSpaceGenerator = new AStateSpaceGenerator<MarkovState>() {
			@Override
			public List<MarkovState> generateSuccs(MarkovState state) {
				try {
					final long SLEEP_TIME = 100;
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					// Nothing to do here
				}
				
				if (state == initialState) {
					for (MarkovState succ : initialStateSuccs) {
						targetMa.addState(succ);
						targetMa.addMarkovianTransition(null, state, succ, 1);
					}
					return initialStateSuccs;
				} else {
					return Collections.emptyList();
				}
			}
			
			@Override
			public MarkovState createInitialState() {
				return initialState;
			}
		};
		
		MarkovAutomaton<MarkovState> ma = maBuilder.build(mockStateSpaceGenerator, null);
		
		final int EXPECTED_COUNT_STATES = 3;
		final int EXPECTED_COUNT_TRANSITIONS = 2;
		
		assertEquals("Correct number of states generated", EXPECTED_COUNT_STATES, ma.getStates().size());
		assertEquals("Correct number of transitions generated", EXPECTED_COUNT_TRANSITIONS, ma.getTransitions().size());
		assertEquals("Correct number of states logged in statistics", EXPECTED_COUNT_STATES, maBuilder.getStatistics().maxStates);
		assertEquals("Correct number of transitions logged in statistics", EXPECTED_COUNT_TRANSITIONS, maBuilder.getStatistics().maxTransitions);
		assertNotEquals("Logged that some time has passed", maBuilder.getStatistics().time);
	}
}
