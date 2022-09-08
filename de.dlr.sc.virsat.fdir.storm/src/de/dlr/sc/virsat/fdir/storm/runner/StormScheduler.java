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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingStatistics;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.IMarkovScheduler;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.ScheduleQuery;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;

/**
 * 
 * @author yoge_re
 *
 */
public class StormScheduler implements IMarkovScheduler<MarkovState> {
	private StormExecutionEnvironment stormExecutionEnvironment;
	private static final double DELTA = 0.01;
	
	/**
	 * 
	 * @param stormExecutionEnvironment
	 *            stormExecutionEnvironment
	 */
	public StormScheduler(StormExecutionEnvironment stormExecutionEnvironment) {
		this.stormExecutionEnvironment = stormExecutionEnvironment;
	}

	@Override
	public Map<MarkovState, List<MarkovTransition<MarkovState>>> computeOptimalScheduler(ScheduleQuery<MarkovState> scheduleQuery, SubMonitor subMonitor) {
		Storm storm = new Storm(scheduleQuery.getMa(), DELTA, MeanTimeToFailure.MTTF);
		Map<MarkovState, List<MarkovTransition<MarkovState>>> mapScheduler = new HashMap<>();
		Map<Integer, Integer> stormResults = runStormScheduler(storm);
		for (MarkovState fromState : scheduleQuery.getMa().getStates()) {
			if (fromState.isNondet()) {
				int bestTransition = stormResults.get(fromState.getIndex());
				List<MarkovTransition<MarkovState>> transitionGroup = new ArrayList<>();
				transitionGroup.add(scheduleQuery.getMa().getSuccTransitions(fromState).get(bestTransition));
				mapScheduler.put(fromState, transitionGroup);
			}
		}

		
		return mapScheduler;
	}
	
	/**
	 * Actually runs storm.
	 * Overwrite for test purposes
	 * @param storm the storm program
	 * @return the computed schedule
	 */
	protected Map<Integer, Integer> runStormScheduler(Storm storm) {
		StormRunner<Double> stormRunner = new StormRunner<Double>(storm, stormExecutionEnvironment);
		try {
			stormRunner.run();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		return storm.extractSchedule();
	}

	@Override
	public ModelCheckingStatistics getStatistics() {
		return new ModelCheckingStatistics();
	}
}
