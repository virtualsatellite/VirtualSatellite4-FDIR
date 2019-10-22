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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.IMarkovScheduler;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;

/**
 * 
 * @author yoge_re
 *
 */
public class StormScheduler implements IMarkovScheduler<MarkovState> {
	private StormExecutionEnvironment stormExecutionEnvironment;
	final double delta = 0.01;
	/**
	 * 
	 * @param stormExecutionEnvironment
	 *            stormExecutionEnvironment
	 */
	public StormScheduler(StormExecutionEnvironment stormExecutionEnvironment) {
		super();
		this.stormExecutionEnvironment = stormExecutionEnvironment;
	}

	@Override
	public Map<MarkovState, Set<MarkovTransition<MarkovState>>> computeOptimalScheduler(MarkovAutomaton<MarkovState> ma,
			MarkovState initialMa) {

		Storm storm = new Storm(ma, delta, MTTF.MTTF);
		Map<MarkovState, Set<MarkovTransition<MarkovState>>> mapScheduler = new HashMap<>();
		StormRunner<Double> stormRunner = new StormRunner<Double>(storm, stormExecutionEnvironment);
		try {
			stormRunner.run();
			Map<Integer, Integer> stormResults = storm.extractSchedule();
			for (MarkovState fromState : ma.getStates()) {
				if (!fromState.isMarkovian()) {
					int bestTransition = stormResults.get(fromState.getIndex());
					Set<MarkovTransition<MarkovState>> transitionGroup = new HashSet<>();
					transitionGroup.add(ma.getSuccTransitions(fromState).get(bestTransition));
					mapScheduler.put(fromState, transitionGroup);
				}
			}

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return mapScheduler;
	}

}
