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

import java.util.Collections;
import java.util.List;

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
	 * Gets the first states that should be considered for state space generation.
	 * By default this is only the initial state.
	 * Special logic for generating special states only for the initial state can be put here.
	 * @param initialState the initial state
	 * @return the starting states
	 */
	public List<S> getStartingStates(S initialState) {
		return Collections.singletonList(initialState);
	}
	
	/**
	 * Creates the initial state
	 * @param ma the markov automaton where the intial state should be created in
	 * @return the new intial belief state
	 */
	public abstract S createInitialState();
	
	/**
	 * Takes a belief state and generates the successor states for it
	 * @param ma the markov automaton where the generated states will be inserted
	 * @param beliefState the belief state to consider
	 * @return the newly generated states
	 */
	public abstract List<S> generateSuccs(S state);
}
