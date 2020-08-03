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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * This class tests the Schedule to Recovery Automaton converter.
 * @author muel_s8
 *
 */

public class Schedule2RAConverterTest extends ATestCase {
	
	@Test
	public void testNoInternalTransition() {
		SPARE fault = new SPARE(concept);
		ClaimAction ca1 = new ClaimAction(concept);
		ca1.setSpareGate(fault);
		ClaimAction ca2 = new ClaimAction(concept);		
		
		// Build the model:
		// initial ---- fault -----> nondet ------ ca1 -----> correct
		//									\----- ca2 -----> wrong
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState nondet = new MarkovState();
		MarkovState correct = new MarkovState();
		MarkovState wrong = new MarkovState();
		
		ma.addState(initial);
		ma.addState(nondet);
		ma.addState(correct);
		ma.addState(wrong);
		
		ma.addMarkovianTransition(new SimpleEntry<>(Collections.singleton(fault), false), initial, nondet, 1);
		MarkovTransition<MarkovState> choice = ma.addNondeterministicTransition(Collections.singletonList(ca1), nondet, correct);
		ma.addNondeterministicTransition(Collections.singletonList(ca2), nondet, wrong);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = new HashMap<>();
		schedule.put(nondet, Collections.singletonList(choice));
		
		Schedule2RAConverter<MarkovState> converter = new Schedule2RAConverter<>(ma, concept);
		RecoveryAutomaton ra = converter.convert(schedule, initial);
		
		// Expected recovery automaton:
		// initial ---- fault : ca1 -----> x
		
		assertEquals(2, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
		assertEquals(ra.getInitial(), ra.getStates().get(0));
		
		FaultEventTransition transition = (FaultEventTransition) ra.getTransitions().get(0);
		assertEquals(ra.getStates().get(0), transition.getFrom());
		assertEquals(ra.getStates().get(1), transition.getTo());
		assertThat(transition.getGuards(), hasItem(fault));
		assertEquals(1, transition.getRecoveryActions().size());
		assertEquals(fault, ((ClaimAction) transition.getRecoveryActions().get(0)).getSpareGate());
	}
	
	@Test
	public void testInternalTransition() {
		SPARE fault = new SPARE(concept);
		ClaimAction ca1 = new ClaimAction(concept);
		ca1.setSpareGate(fault);
		ClaimAction ca2 = new ClaimAction(concept);
		
		// Build the model:
		// initial1 ------ RATE_INTERNAL -----> initial2
		//     |									|
		//	RATE_EXTERNAL						RATE_EXTERNAL
		//     |									|
		//	   v									v
		// 	nondet1 -- ca1 --> correct <-- ca2 -- nondet2
		//	    \----- ca2 -->  wrong <-- ca1 ------/     									
	
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial1 = new MarkovState();
		MarkovState initial2 = new MarkovState();
		MarkovState nondet1 = new MarkovState();
		MarkovState nondet2 = new MarkovState();
		MarkovState correct = new MarkovState();
		MarkovState wrong = new MarkovState();
		
		ma.addState(initial1);
		ma.addState(initial2);
		ma.addState(nondet1);
		ma.addState(nondet2);
		ma.addState(correct);
		ma.addState(wrong);
		
		final double RATE_EXTERNAL = 1;
		final double RATE_INTERNAL = 0.5;
		ma.addMarkovianTransition(new SimpleEntry<>(Collections.EMPTY_SET, false), initial1, initial2, RATE_INTERNAL);
		ma.addMarkovianTransition(new SimpleEntry<>(Collections.singleton(fault), false), initial1, nondet1, RATE_EXTERNAL);
		ma.addMarkovianTransition(new SimpleEntry<>(Collections.singleton(fault), false), initial2, nondet2, RATE_EXTERNAL);		
		MarkovTransition<MarkovState> choice1 = ma.addNondeterministicTransition(Collections.singletonList(ca1), nondet1, correct);
		MarkovTransition<MarkovState> choice2 = ma.addNondeterministicTransition(Collections.singletonList(ca2), nondet2, correct);
		ma.addNondeterministicTransition(Collections.singletonList(ca2), nondet1, wrong);
		ma.addNondeterministicTransition(Collections.singletonList(ca1), nondet2, wrong);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = new HashMap<>();
		schedule.put(nondet1, Collections.singletonList(choice1));
		schedule.put(nondet2, Collections.singletonList(choice2));
		
		Schedule2RAConverter<MarkovState> converter = new Schedule2RAConverter<>(ma, concept);
		RecoveryAutomaton ra = converter.convert(schedule, initial1);
		
		// Expected recovery automaton:
		// initial --------------- TRANSITION_TIME -------------> timedState
		// 	   \----- fault : ca1 --> finalState <-- fault : ca2 -----/ 
		
		final int EXPECTED_NUMBER_STATES = 3;
		final int EXPECTED_NUMBER_TRANSITIONS = 3;
		assertEquals(EXPECTED_NUMBER_STATES, ra.getStates().size());
		assertEquals(EXPECTED_NUMBER_TRANSITIONS, ra.getTransitions().size());
		assertEquals(ra.getInitial(), ra.getStates().get(0));
		
		RecoveryAutomatonHelper raHelper = new RecoveryAutomatonHelper(concept);
		State finalState = ra.getStates()
				.stream()
				.filter(state -> raHelper.getCurrentTransitions(ra).get(state).isEmpty())
				.findFirst().get();
		State timedState = ra.getStates()
				.stream()
				.filter(state -> !state.equals(ra.getInitial()) && !state.equals(finalState))
				.findFirst().get();
		
		TimeoutTransition timeoutTransition = (TimeoutTransition) ra.getTransitions()
				.stream()
				.filter(transition -> transition.getFrom().equals(ra.getInitial()) && transition.getTo().equals(timedState))
				.findFirst().get();
		
		final double EXPTED_TRANSITION_TIME = 1 / RATE_INTERNAL;
		assertEquals(EXPTED_TRANSITION_TIME, timeoutTransition.getTime(), TEST_EPSILON);
		assertTrue(raHelper.isConnected(ra, ra.getInitial(), finalState));
		assertTrue(raHelper.isConnected(ra, timedState, finalState));
	}
	
	@Test
	public void testInternalTransitionMissingChoices() {
		SPARE fault = new SPARE(concept);
		ClaimAction ca1 = new ClaimAction(concept);
		ca1.setSpareGate(fault);
		ClaimAction ca2 = new ClaimAction(concept);
		
		// Build the model:
		// initial1 ------ RATE_INTERNAL -----> initial2
		//     										|
		//			 							RATE_EXTERNAL
		//     										|
		//	   										v
		// 					 correct <-- ca2 -- nondet2
		//	    			  wrong <-- ca1 ------/     									
	
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial1 = new MarkovState();
		MarkovState initial2 = new MarkovState();
		MarkovState nondet = new MarkovState();
		MarkovState correct = new MarkovState();
		MarkovState wrong = new MarkovState();
		
		ma.addState(initial1);
		ma.addState(initial2);
		ma.addState(nondet);
		ma.addState(correct);
		ma.addState(wrong);
		
		final double RATE_EXTERNAL = 1;
		final double RATE_INTERNAL = 0.5;
		ma.addMarkovianTransition(new SimpleEntry<>(Collections.emptySet(), false), initial1, initial2, RATE_INTERNAL);
		ma.addMarkovianTransition(new SimpleEntry<>(Collections.singleton(fault), false), initial2, nondet, RATE_EXTERNAL);		
		MarkovTransition<MarkovState> choice = ma.addNondeterministicTransition(Collections.singletonList(ca1), nondet, correct);
		ma.addNondeterministicTransition(Collections.singletonList(ca2), nondet, wrong);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = new HashMap<>();
		schedule.put(nondet, Collections.singletonList(choice));
		
		Schedule2RAConverter<MarkovState> converter = new Schedule2RAConverter<>(ma, concept);
		RecoveryAutomaton ra = converter.convert(schedule, initial1);
		
		// Expected recovery automaton:
		// initial ---- fault : ca1 -----> x
		
		final int EXPECTED_NUMBER_STATES = 3;
		final int EXPECTED_NUMBER_TRANSITIONS = 3;
		assertEquals(EXPECTED_NUMBER_STATES, ra.getStates().size());
		assertEquals(EXPECTED_NUMBER_TRANSITIONS, ra.getTransitions().size());
	}
}
