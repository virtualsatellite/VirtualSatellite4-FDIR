/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovStateType;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;

/**
 * This class tests the Markov Scheduler implementation.
 * @author muel_s8
 *
 */

public class MarkovSchedulerTest {

	private MarkovScheduler<MarkovState> scheduler = new MarkovScheduler<MarkovState>();
	
	@Test
	public void testScheduleNoTransitions() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState initial = new MarkovState();
		initial.setType(MarkovStateType.NONDET);
		ma.addState(initial);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.isEmpty());
	}
	
	@Test
	public void testScheduleOnlyNondet() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState good = new MarkovState();
		MarkovState bad = new MarkovState();
		
		ma.addState(initial);
		ma.addState(good);
		ma.addState(bad);
		
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		
		ma.getFinalStateProbs().put(bad, 1d);
		
		Object correctChoice = ma.addNondeterministicTransition("a", initial, good);
		Object falseChoice = ma.addNondeterministicTransition("b", initial, bad);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.get(initial).contains(correctChoice));
		assertFalse(schedule.get(initial).contains(falseChoice));
	}
	
	@Test
	public void testScheduleWithMarkovian() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState good = new MarkovState();
		MarkovState bad = new MarkovState();
		MarkovState sink = new MarkovState();
		
		ma.addState(initial);
		ma.addState(good);
		ma.addState(bad);
		ma.addState(sink);
		
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		
		ma.getFinalStateProbs().put(sink, 1d);
		
		Object correctChoice = ma.addNondeterministicTransition("a", initial, good);
		Object falseChoice = ma.addNondeterministicTransition("b", initial, bad);
		
		ma.addMarkovianTransition("a", good, sink, 1);
		ma.addMarkovianTransition("b", bad, sink, 2);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.get(initial).contains(correctChoice));
		assertFalse(schedule.get(initial).contains(falseChoice));
	}

	@Test
	public void testScheduleWithProb() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState good = new MarkovState();
		MarkovState bad = new MarkovState();
		
		ma.addState(initial);
		ma.addState(good);
		ma.addState(bad);
		
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		
		ma.getFinalStateProbs().put(bad, 1d);
		
		Object correctChoice = ma.addNondeterministicTransition("a", initial, good, 1);
		// CHECKSTYLE:OFF
		Object falseChoice1 = ma.addNondeterministicTransition("b", initial, good, 0.5);
		Object falseChoice2 = ma.addNondeterministicTransition("b", initial, bad, 0.5);
		// CHECKSTYLE:ON
		
		ma.addMarkovianTransition("c", good, bad, 1);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.get(initial).contains(correctChoice));
		assertFalse(schedule.get(initial).contains(falseChoice1));
		assertFalse(schedule.get(initial).contains(falseChoice2));
		
		Map<MarkovState, Double> values = scheduler.getResults();
		assertEquals(1, values.get(initial), 0);
		assertEquals(1, values.get(good), 0);
		assertEquals(0, values.get(bad), 0);
	}
	
	@Test
	public void testScheduleNoActionOverAction() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState bad = new MarkovState();
		
		ma.addState(initial);
		ma.addState(bad);
		
		ma.getEvents().add(Collections.emptyList());
		ma.getEvents().add("b");
		
		ma.getFinalStateProbs().put(bad, 1d);
		
		Object correctChoice = ma.addNondeterministicTransition(Collections.emptyList(), initial, bad, 1);
		Object falseChoice = ma.addNondeterministicTransition("b", initial, bad, 1);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.get(initial).contains(correctChoice));
		assertFalse(schedule.get(initial).contains(falseChoice));
	}
	
	@Test
	public void testScheduleConstraintSSA() {
		// Construct the following MA:
		// init --- a ---> bad --- 1 ---> badOk --- 1 ---> badFail --- 3 ---> badOk
		//      --- b ---> good -- 2 ---> goodOk -- 1 ---> goodFail -- 1 ---> goodOk
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState bad = new MarkovState();
		MarkovState good = new MarkovState();
		MarkovState badOk = new MarkovState();
		MarkovState goodOk = new MarkovState();
		MarkovState badFail = new MarkovState();
		MarkovState goodFail = new MarkovState();
		
		ma.addState(initial);
		ma.addState(bad);
		ma.addState(good);
		ma.addState(badOk);
		ma.addState(goodOk);
		ma.addState(badFail);
		ma.addState(goodFail);
		
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		
		Object choiceA = ma.addNondeterministicTransition("a", initial, bad);
		Object choiceB = ma.addNondeterministicTransition("b", initial, good);
		
		// CHECKSTYLE:OFF
		ma.addMarkovianTransition("m", bad, badOk, 1);
		ma.addMarkovianTransition("m", good, goodOk, 2);
		
		ma.addMarkovianTransition("m", badOk, badFail, 1);
		ma.addMarkovianTransition("m", goodOk, goodFail, 1);
		
		ma.addMarkovianTransition("m", badFail, badOk, 3);
		ma.addMarkovianTransition("m", goodFail, goodOk, 1);
		// CHECKSTYLE:ON
		
		ma.getFinalStateProbs().put(badFail, 1d);
		ma.getFinalStateProbs().put(goodFail, 1d);
		
		System.out.println(ma.toDot());
		
		// Without any constraints, the correct choice is choiceA
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.get(initial).contains(choiceA));
		assertFalse(schedule.get(initial).contains(choiceB));
		
		// With an SSA >= 50% constraint, the correct choice is choiceB
		ScheduleQuery<MarkovState> constrainedQuery = new ScheduleQuery<>(ma, initial);
		final double MINIMUM_SSA = 0.5;
		constrainedQuery.getConstraints().put(SteadyStateAvailability.STEADY_STATE_AVAILABILITY, MINIMUM_SSA);
		schedule = scheduler.computeOptimalScheduler(constrainedQuery);
		assertFalse(schedule.get(initial).contains(choiceA));
		assertTrue(schedule.get(initial).contains(choiceB));
	}
}
