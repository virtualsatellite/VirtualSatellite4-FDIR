/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.core.runtime.SubMonitor;

public class MarkovAutomatonBuilder<S extends MarkovState> {
	
	private MarkovAutomatonBuildStatistics statistics = new MarkovAutomatonBuildStatistics();
	private S initialState;

	/**
	 * Builds a markov automaton using the provided state space generator
	 * @return the new markov automaton
	 */
	public MarkovAutomaton<S> build(AStateSpaceGenerator<S> stateSpaceGenerator, SubMonitor monitor) {
		statistics = new MarkovAutomatonBuildStatistics();
		statistics.time = System.currentTimeMillis();
		
		MarkovAutomaton<S> ma = new MarkovAutomaton<>();
		stateSpaceGenerator.init(ma);
		
		initialState = stateSpaceGenerator.createInitialState();
		ma.addState(initialState);
		
		Queue<S> toProcess = new LinkedList<>();
		List<S> startingStates = stateSpaceGenerator.getStartingStates(initialState);
		statistics.countGeneratedStates += startingStates.size();
		
		monitor = SubMonitor.convert(monitor, startingStates.size());
		monitor.setTaskName("Generating Markov automaton state space");
		
		toProcess.addAll(startingStates);
		
		while (!toProcess.isEmpty()) {
			S state = toProcess.poll();
			List<S> generatedNewSuccs = stateSpaceGenerator.generateSuccs(state);
			toProcess.addAll(generatedNewSuccs);
			statistics.countGeneratedStates += generatedNewSuccs.size();
			
			final int PROGRESS_COUNT = 100;
			monitor.setWorkRemaining(PROGRESS_COUNT).split(1);
		}
		
		statistics.maxStates = ma.getStates().size();
		statistics.maxTransitions = ma.getTransitions().size();
		statistics.time = System.currentTimeMillis() - statistics.time;
		
		return ma;
	}
	
	/**
	 * Gets the initial belief state corresponding to the initial Ma state
	 * @return the initial belief state
	 */
	public S getInitialState() {
		return initialState;
	}
	
	/**
	 * Gets the internal statistics to the last call of the builder
	 * @return the statistics of the last call of the builder
	 */
	public MarkovAutomatonBuildStatistics getStatistics() {
		return statistics;
	}
}
