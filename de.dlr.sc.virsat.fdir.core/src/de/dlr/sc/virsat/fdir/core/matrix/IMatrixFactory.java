/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix;

import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public interface IMatrixFactory {

	/**
	 * Creates the generator matrix of a markov automaton
	 * @param ma the markov automaton
	 * @param failStatesAreTerminal failStatesAreTerminal
	 * @param delta the matrix will be scaled accoriding to the time delta
	 * @return the generator matrix
	 */
	IMatrix createGeneratorMatrix(MarkovAutomaton<? extends MarkovState> ma, boolean failStatesAreTerminal,
			double delta);

	/**
	 * This creates a matrix representing bellman equations on the induced Markov Decision Process of a Markov Automaton.
	 * 
	 * The equations are as follows:
	 * 
	 * value(terminalState) = 0
	 * value(state) = costs(state) + SUM(s' successor of s: Prob(s, s') * value(s')
	 * 
	 * @param ma the markov automaton
	 * @param states a subset of states from the markov automaton. Only the states in this list will be used to construct the matrix.
	 * @param terminalStates a set of states that will be considered terminal for the matrix (outgoing transitions are ignored)
	 * @param invertEdgeDirection if set to true, the costs will be backwards propagated starting from the terminal states.
	 * If set to true, the costs will be forward propagated with no particular starting place dictated by the matrix.
	 * @return the matrix representing the equation system
	 */
	IMatrix createBellmanMatrix(MarkovAutomaton<? extends MarkovState> ma, List<? extends MarkovState> states,
			Set<? extends MarkovState> terminalStates, boolean invertEdgeDirection);

}