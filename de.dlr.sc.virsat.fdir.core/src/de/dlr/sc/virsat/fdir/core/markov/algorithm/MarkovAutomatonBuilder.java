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
import de.dlr.sc.virsat.fdir.core.util.IStatistics;

public class MarkovAutomatonBuilder<S extends MarkovState> {
	
	private MarkovAutomatonBuildStatistics statistics = new MarkovAutomatonBuildStatistics();
	private S initialState;

	/**
	 * Builds a markov automaton using the provided state space generator
	 * @return the new markov automaton
	 */
	public MarkovAutomaton<S> build(AStateSpaceGenerator<S> stateSpaceGenerator, SubMonitor monitor) {
		statistics = new MarkovAutomatonBuildStatistics();
		long startTime = System.currentTimeMillis();
		statistics.time = IStatistics.TIMEOUT;
		
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
			S state = toProcess.poll();
			List<S> generatedNewSuccs = stateSpaceGenerator.generateSuccs(state, monitor);
			toProcess.addAll(generatedNewSuccs);
		}
		
		// Removes the filtered-out states if any
		stateSpaceGenerator.removeBadStates();
		
		statistics.maxStates = ma.getStates().size();
		statistics.maxTransitions = ma.getTransitions().size();
		statistics.time = System.currentTimeMillis() - startTime;
		
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
