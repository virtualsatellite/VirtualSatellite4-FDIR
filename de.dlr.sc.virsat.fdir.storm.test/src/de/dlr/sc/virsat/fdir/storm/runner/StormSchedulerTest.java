/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.runner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.ScheduleQuery;

/**
 * This class tests the storm scheduler implementation
 * @author yoge_re
 *
 */
public class StormSchedulerTest {
	@Test
	public void testScheduleWithStorm() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState good = new MarkovState();
		MarkovState bad = new MarkovState();
		MarkovState sink = new MarkovState();
		
		ma.addState(initial);
		ma.addState(good);
		ma.addState(bad);
		ma.addState(sink);
		
		ma.getFinalStateProbs().put(sink, 1d);
		
		Object correctChoice = ma.addNondeterministicTransition("a", initial, good);
		Object falseChoice = ma.addNondeterministicTransition("b", initial, bad);
		
		ma.addMarkovianTransition("a", good, sink, 1);
		ma.addMarkovianTransition("b", bad, sink, 2);
		
		StormScheduler scheduler = new StormScheduler(StormExecutionEnvironment.Docker) {
			@Override
			protected Map<Integer, Integer> runStormScheduler(Storm storm) {
				Map<Integer, Integer> mapStateToChoice = new HashMap<>();
				mapStateToChoice.put(0, 0);
				mapStateToChoice.put(1, 1);
				return mapStateToChoice;
			}
		};
		
		Map<MarkovState, List<MarkovTransition<MarkovState>>> schedule = scheduler.computeOptimalScheduler(new ScheduleQuery<>(ma, initial));
		assertTrue(schedule.get(initial).contains(correctChoice));
		assertFalse(schedule.get(initial).contains(falseChoice));
	}
}
