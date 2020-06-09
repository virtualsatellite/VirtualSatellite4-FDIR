/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;


import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;

/**
 * 
 * @author yoge_re
 *
 */
public class MarkovModelCheckerTest {
	private static final double DELTA = 0.01;
	private static final double EPSILON =  0.001;
	
	@Test 
	public void testReliability() {
		final double EXPECTED_FAIL_RATE = 0.9975212478; 
		final double EXPECTED_MTTF = 0.166666666;
		MarkovModelChecker modelChecker = new MarkovModelChecker(DELTA, EPSILON * EPSILON);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		final double RATE = 6;
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		ma.addState(state1);
		ma.addState(state2);
		ma.getFinalStateProbs().put(state2, 1d);
		ma.addMarkovianTransition("a", state1, state2, RATE);
		ma.addMarkovianTransition("b", state2, state1, 1);
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, Reliability.UNIT_RELIABILITY, MTTF.MTTF), null);
		
		int timepoint = (int) (1 / DELTA) - 1;
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), EPSILON);
		assertEquals(EXPECTED_FAIL_RATE, result.getFailRates().get(timepoint), EPSILON);
	}

	@Test
	public void testAvailability1SCC() {
		final List<Double> EXPECTED_POINT_AVAILABILITY = new ArrayList<>();
		final double AVAIL_RATE = 0.5453611068892465;
		EXPECTED_POINT_AVAILABILITY.add((double) 1);
		EXPECTED_POINT_AVAILABILITY.add(AVAIL_RATE);
		final double EXPECTED_STEADY_STATE_AVAILABILITY = 0.5;
		MarkovModelChecker modelChecker = new MarkovModelChecker(1, EPSILON);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		ma.addState(state1);
		ma.addState(state2);
		ma.getFinalStateProbs().put(state2, 1d);
		
		final double RATE1 = 1.2;
		final double RATE2 = 1.2;
		ma.addMarkovianTransition("a", state1, state2, RATE1);
		ma.addMarkovianTransition("b", state2, state1, RATE2);
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, Availability.UNIT_AVAILABILITY,
				SteadyStateAvailability.STEADY_STATE_AVAILABILITY), null);
		
		assertEquals(EXPECTED_STEADY_STATE_AVAILABILITY, result.getSteadyStateAvailability(), EPSILON);
		assertEquals(EXPECTED_POINT_AVAILABILITY, result.getAvailability()); 
	}

	@Test
	public void testSteadyStateAvailability1SCCWithNondet() {
		final double EXPECTED_STEADY_STATE_AVAILABILITY = 0.677419354;
		MarkovModelChecker modelChecker = new MarkovModelChecker(1, EPSILON);
		
		// Construct the following MA:
		// init --- 5 ---> nondet --- b ---> good --- 2 ---> fail --- 3 ---> init
		// 				   nondet --- a ------------------.> fail
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		ma.getEvents().add("m");
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		MarkovState init = new MarkovState();
		MarkovState nondet = new MarkovState();
		MarkovState good = new MarkovState();
		MarkovState fail = new MarkovState();
		
		ma.addState(init);
		ma.addState(nondet);
		ma.addState(good);
		ma.addState(fail);
		ma.getFinalStateProbs().put(fail, 1d);
		
		final double RATE_INIT_TO_NONDET = 5;
		final double RATE_GOOD_TO_FAIL = 2;
		final double RATE_FAIL_TO_INIT = 3;
		
		ma.addMarkovianTransition("m", init, nondet, RATE_INIT_TO_NONDET);
		ma.addMarkovianTransition("m", good, fail, RATE_GOOD_TO_FAIL);
		ma.addMarkovianTransition("m", fail, init, RATE_FAIL_TO_INIT);
		
		ma.addNondeterministicTransition("a", nondet, good);
		ma.addNondeterministicTransition("b", nondet, fail);
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, SteadyStateAvailability.STEADY_STATE_AVAILABILITY), null);
		
		assertEquals(EXPECTED_STEADY_STATE_AVAILABILITY, result.getSteadyStateAvailability(), EPSILON);
	}
	
	@Test
	public void testSteadyStateAvailability2SCC() {
		final double EXPECTED_STEADY_STATE_AVAILABILITY = 0.4363636363636364;
		MarkovModelChecker modelChecker = new MarkovModelChecker(1, EPSILON);
		
		// Construct the following MA:
		// init --- 3 ---> good1 --- 2 ---> fail1 --- 4 ---> good1
		//          2 ---> good2 --- 5 ---> fail2 --- 0.5 -> good2
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		ma.getEvents().add("m");
		MarkovState init = new MarkovState();
		MarkovState good1 = new MarkovState();
		MarkovState fail1 = new MarkovState();
		MarkovState good2 = new MarkovState();
		MarkovState fail2 = new MarkovState();
		
		ma.addState(init);
		ma.addState(good1);
		ma.addState(fail1);
		ma.addState(good2);
		ma.addState(fail2);
		ma.getFinalStateProbs().put(fail1, 1d);
		ma.getFinalStateProbs().put(fail2, 1d);
		
		final double RATE_INIT_TO_GOOD_1 = 3;
		final double RATE_GOOD_1_TO_FAIL_1 = 2;
		final double RATE_FAIL_1_TO_GOOD_1 = 4;
		
		ma.addMarkovianTransition("m", init, good1, RATE_INIT_TO_GOOD_1);
		ma.addMarkovianTransition("m", good1, fail1, RATE_GOOD_1_TO_FAIL_1);
		ma.addMarkovianTransition("m", fail1, good1, RATE_FAIL_1_TO_GOOD_1);
		
		final double RATE_INIT_TO_GOOD_2 = 2;
		final double RATE_GOOD_2_TO_FAIL_2 = 5;
		final double RATE_FAIL_2_TO_GOOD_2 = 0.5;
		
		ma.addMarkovianTransition("m", init, good2, RATE_INIT_TO_GOOD_2);
		ma.addMarkovianTransition("m", good2, fail2, RATE_GOOD_2_TO_FAIL_2);
		ma.addMarkovianTransition("m", fail2, good2, RATE_FAIL_2_TO_GOOD_2);
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, SteadyStateAvailability.STEADY_STATE_AVAILABILITY), null);
		
		assertEquals(EXPECTED_STEADY_STATE_AVAILABILITY, result.getSteadyStateAvailability(), EPSILON);
	}
	
	@Test 
	public void testMinCutSet() {
		MarkovModelChecker modelChecker = new MarkovModelChecker(DELTA, EPSILON * EPSILON);

		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState init = new MarkovState();
		MarkovState fail = new MarkovState();
		MarkovState inter = new MarkovState();
		
		ma.getEvents().add("a");
		ma.getEvents().add("b");
		
		ma.addState(init);
		ma.addState(fail);
		ma.addState(inter);
		ma.getFinalStateProbs().put(fail, 1d);
		
		ma.addMarkovianTransition("c", init, fail, 1);
		ma.addMarkovianTransition("b", init, inter, 1);
		ma.addMarkovianTransition("a", inter, fail, 1);
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, MinimumCutSet.MINCUTSET), null);
		
		final int COUNT_EXPECTED_MINCUT_SETS = 2;
		assertEquals(COUNT_EXPECTED_MINCUT_SETS, result.getMinCutSets().size());
		assertThat(result.getMinCutSets(), hasItem(Collections.singleton("c")));
		assertThat(result.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("a", "b"))));
		
		result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, new MinimumCutSet(1)), null);
		
		final int COUNT_EXPECTED_MINCUT_SETS_RESTRICTED = 1;
		assertEquals(COUNT_EXPECTED_MINCUT_SETS_RESTRICTED, result.getMinCutSets().size());
		assertThat(result.getMinCutSets(), hasItem(Collections.singleton("c")));
	}
}
