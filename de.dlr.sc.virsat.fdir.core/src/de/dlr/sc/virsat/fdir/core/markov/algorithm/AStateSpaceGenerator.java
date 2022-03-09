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

import java.util.List;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public abstract class AStateSpaceGenerator<S extends MarkovState> {
	
	private static final long MEMORY_THRESHOLD = 1024 * 1024 * 1024 * 2;
	
	protected MarkovAutomaton<S> targetMa;
	
	/**
	 * Initializes the state space generator with the given markov automaton
	 * @param ma the markov automaton where the generated states should be put
	 */
	public void init(MarkovAutomaton<S> targetMa) {
		this.targetMa = targetMa;
	}
	
	/**
	 * Creates the initial state
	 * @param ma the markov automaton where the intial state should be created in
	 * @return the new intial belief state
	 */
	public abstract S createInitialState();
	
	/**
	 * Takes a belief state and generates the successor states for it
	 * @param state the current state
	 * @param monitor a monitor
	 * @return the newly generated states
	 */
	public abstract List<S> generateSuccs(S state, SubMonitor monitor);
	
	/**
	 * Removes the filtered-out states if any
	 */
	public void removeBadStates() {
	}
	
	private long maxMemory = Runtime.getRuntime().maxMemory();
	
	/**
	 * Checks if this long running operation should cancelled / has been cancelled
	 * and gives user feedback that the operation is still running.
	 * @param monitor a monitor
	 */
	protected void checkCancellation(SubMonitor monitor) {
		// Eclipse trick for doing progress updates with unknown ending time
		final int PROGRESS_COUNT = 100;
		monitor.setWorkRemaining(PROGRESS_COUNT).split(1);
		
		long freeMemory = maxMemory - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		if (freeMemory < MEMORY_THRESHOLD) {
			throw new RuntimeException("Close to out of memory. Aborting so we can still maintain an operational state.");
		}
	}
}
