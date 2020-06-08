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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class MarkovAutomatonBuilder<S extends MarkovState> {
	
	private MarkovAutomatonBuildStatistics statistics = new MarkovAutomatonBuildStatistics();
	private S initialState;

	/**
	 * Builds a markov automaton using the provided state space generator
	 * @return the new markov automaton
	 */
	public MarkovAutomaton<S> build(AStateSpaceGenerator<S> stateSpaceGenerator, SubMonitor monitor) {
		beginStatisticsRecord();
		
		// Create a new markov automaton and inform the state space generator about it
		MarkovAutomaton<S> ma = new MarkovAutomaton<>();
		stateSpaceGenerator.init(ma);
		
		// Generate the initial state
		initialState = stateSpaceGenerator.createInitialState();
		ma.addState(initialState);
		
		monitor = SubMonitor.convert(monitor, 1);
		monitor.setTaskName("Generating Markov automaton state space");
		
		Queue<S> toProcess = new LinkedList<>();
		toProcess.add(initialState);
		
		// Actual state space generation loop
		while (!toProcess.isEmpty()) {
			// Eclipse trick for doing progress updates with unknown ending time
			final int PROGRESS_COUNT = 100;
			monitor.setWorkRemaining(PROGRESS_COUNT).split(1);
			
			S state = toProcess.poll();
			List<S> generatedNewSuccs = stateSpaceGenerator.generateSuccs(state);
			toProcess.addAll(generatedNewSuccs);
		}
		
		endStatisticsRecord(ma);
		
		return ma;
	}
	
	/**
	 * Starts a new statistics measurement
	 */
	private void beginStatisticsRecord() {
		statistics = new MarkovAutomatonBuildStatistics();
		statistics.time = System.currentTimeMillis();
	}
	
	/**
	 * Finishes an ongoing statistics measurement
	 * @param ma the finished markov automaton
	 */
	private void endStatisticsRecord(MarkovAutomaton<S> ma) {
		statistics.maxStates = ma.getStates().size();
		statistics.maxTransitions = ma.getTransitions().size();
		statistics.time = System.currentTimeMillis() - statistics.time;
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
