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
}
