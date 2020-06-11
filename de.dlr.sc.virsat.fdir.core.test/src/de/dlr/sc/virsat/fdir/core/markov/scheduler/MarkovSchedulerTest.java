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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovStateType;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;

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
		
		bad.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		MarkovTransition<MarkovState> correctChoice = ma.addNondeterministicTransition("a", initial, good);
		ma.addNondeterministicTransition("b", initial, bad);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertThat(schedule.get(initial), allOf(hasItem(correctChoice), hasSize(1)));
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
		
		sink.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		MarkovTransition<MarkovState> correctChoice = ma.addNondeterministicTransition("a", initial, good);
		ma.addNondeterministicTransition("b", initial, bad);
		
		ma.addMarkovianTransition("a", good, sink, 1);
		ma.addMarkovianTransition("b", bad, sink, 2);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertThat(schedule.get(initial), allOf(hasItem(correctChoice), hasSize(1)));
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
		
		bad.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		MarkovTransition<MarkovState> correctChoice = ma.addNondeterministicTransition("a", initial, good, 1);
		// CHECKSTYLE:OFF
		ma.addNondeterministicTransition("b", initial, good, 0.5);
		ma.addNondeterministicTransition("b", initial, bad, 0.5);
		// CHECKSTYLE:ON
		
		ma.addMarkovianTransition("c", good, bad, 1);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertThat(schedule.get(initial), allOf(hasItem(correctChoice), hasSize(1)));
		
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
		
		bad.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		MarkovTransition<MarkovState> correctChoice = ma.addNondeterministicTransition(Collections.emptyList(), initial, bad, 1);
		ma.addNondeterministicTransition("b", initial, bad, 1);
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertThat(schedule.get(initial), allOf(hasItem(correctChoice), hasSize(1)));
	}
	
	@Test
	public void testScheduleSSAConstrained() {
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
		
		MarkovTransition<MarkovState> choiceA = ma.addNondeterministicTransition("a", initial, bad);
		MarkovTransition<MarkovState> choiceB = ma.addNondeterministicTransition("b", initial, good);
		
		// CHECKSTYLE:OFF
		ma.addMarkovianTransition("m", bad, badOk, 1);
		ma.addMarkovianTransition("m", good, goodOk, 2);
		
		ma.addMarkovianTransition("m", badOk, badFail, 1);
		ma.addMarkovianTransition("m", goodOk, goodFail, 1);
		
		ma.addMarkovianTransition("m", badFail, badOk, 3);
		ma.addMarkovianTransition("m", goodFail, goodOk, 1);
		// CHECKSTYLE:ON
		
		badFail.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		goodFail.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		// Without any constraints, the correct choice is choiceA
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertThat(schedule.get(initial), allOf(hasItem(choiceA), hasSize(1)));
		
		// With an SSA >= 50% constraint, the correct choice is choiceB
		ScheduleQuery<MarkovState> constrainedQuery = new ScheduleQuery<>(ma, initial);
		final double MINIMUM_SSA = 0.5;
		constrainedQuery.getConstraints().put(SteadyStateAvailability.SSA, MINIMUM_SSA);
		schedule = scheduler.computeOptimalScheduler(constrainedQuery);
		assertThat(schedule.get(initial), allOf(hasItem(choiceB), hasSize(1)));
	}
	
	@Test
	public void testScheduleSSAObjective() {
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
		
		MarkovTransition<MarkovState> choiceA = ma.addNondeterministicTransition("a", initial, bad);
		MarkovTransition<MarkovState> choiceB = ma.addNondeterministicTransition("b", initial, good);
		
		// CHECKSTYLE:OFF
		ma.addMarkovianTransition("m", bad, badOk, 1);
		ma.addMarkovianTransition("m", good, goodOk, 2);
		
		ma.addMarkovianTransition("m", badOk, badFail, 1);
		ma.addMarkovianTransition("m", goodOk, goodFail, 1);
		
		ma.addMarkovianTransition("m", badFail, badOk, 3);
		ma.addMarkovianTransition("m", goodFail, goodOk, 1);
		// CHECKSTYLE:ON
		
		badFail.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		goodFail.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		// By default the scheduler maximizes MTTF
		ScheduleQuery<MarkovState> maxMTTFQuery = new ScheduleQuery<>(ma, initial);
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(maxMTTFQuery);
		assertThat(schedule.get(initial), allOf(hasItem(choiceA), hasSize(1)));
		
		// Setting the obejctive to SSA should make the scheduler choose the option that maximizes SSA
		ScheduleQuery<MarkovState> maxSSAQuery = new ScheduleQuery<>(ma, initial);
		maxSSAQuery.setObjectiveMetric(SteadyStateAvailability.SSA);
		schedule = scheduler.computeOptimalScheduler(maxSSAQuery);
		assertThat(schedule.get(initial), allOf(hasItem(choiceB), hasSize(1)));
	}
	
	@Test
	public void testScheduleSSDObjective() {
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
		
		MarkovTransition<MarkovState> choiceA = ma.addNondeterministicTransition("a", initial, bad);
		MarkovTransition<MarkovState> choiceB = ma.addNondeterministicTransition("b", initial, good);
		
		// CHECKSTYLE:OFF
		ma.addMarkovianTransition("m", bad, badOk, 1);
		ma.addMarkovianTransition("m", good, goodOk, 2);
		
		ma.addMarkovianTransition("m", badOk, badFail, 1);
		ma.addMarkovianTransition("m", goodOk, goodFail, 1);
		
		ma.addMarkovianTransition("m", badFail, badOk, 3);
		ma.addMarkovianTransition("m", goodFail, goodOk, 1);
		// CHECKSTYLE:ON
		
		badFail.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		goodFail.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		goodFail.getMapFailLabelToProb().put(FailLabel.OBSERVED, 1d);
		
		// By default the scheduler maximizes MTTF
		ScheduleQuery<MarkovState> maxMTTFQuery = new ScheduleQuery<>(ma, initial);
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(maxMTTFQuery);
		assertThat(schedule.get(initial), allOf(hasItem(choiceA), hasSize(1)));
		
		// Setting the obejctive to SSD should make the scheduler choose the option that maximizes SSD
		ScheduleQuery<MarkovState> maxSSDQuery = new ScheduleQuery<>(ma, initial);
		maxSSDQuery.setObjectiveMetric(SteadyStateDetectability.SSD);
		schedule = scheduler.computeOptimalScheduler(maxSSDQuery);
		assertThat(schedule.get(initial), allOf(hasItem(choiceB), hasSize(1)));
	}
	
	@Test
	public void testScheduleFaultToleranceObjective() {
		// Construct the following MA:
		// init --- a ---> bad --- 100 --> fail
		//      --- b ---> good1 -- 1 ---> good2 -- 1 ---> fail
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState bad = new MarkovState();
		MarkovState good1 = new MarkovState();
		MarkovState good2 = new MarkovState();
		MarkovState fail = new MarkovState();
		
		ma.addState(initial);
		ma.addState(bad);
		ma.addState(good1);
		ma.addState(good2);
		ma.addState(fail);
		
		// CHECKSTYLE:OFF
		ma.addNondeterministicTransition("a", initial, bad);
		MarkovTransition<MarkovState> choiceB = ma.addNondeterministicTransition("b", initial, good1);
		ma.addMarkovianTransition("m1", bad, fail, 100);
		ma.addMarkovianTransition("m1", good1, good2, 1);
		ma.addMarkovianTransition("m2", good2, fail, 1);
		// CHECKSTYLE:ON
		
		// Setting the objective to fault tolerance should make the scheduler pick "b" since
		// a needs 1 event to occur and b needs 2
		ScheduleQuery<MarkovState> maxSSAQuery = new ScheduleQuery<>(ma, initial);
		//maxSSAQuery.setObjectiveMetric(FaultTolerance.FAULT_TOLERANCE);
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(maxSSAQuery);
		assertThat(schedule.get(initial), allOf(hasItem(choiceB), hasSize(1)));
	}
}
