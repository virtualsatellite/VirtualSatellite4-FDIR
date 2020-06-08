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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

/**
 * @author piet_ci
 * 
 * This class acts as a factory for different types of matrices.
 *
 */
public class MatrixFactory {
	
	private static final int[] EMPTY_INDEX_LIST = new int[0];
	private static final double[] EMPTY_RATES_LIST = new double[0];
	
	/**
	 * Creates the generator matrix of a markov automaton
	 * @param ma the markov automaton
	 * @param failStatesAreTerminal failStatesAreTerminal
	 * @param delta the matrix will be scaled accoriding to the time delta
	 * @return the generator matrix
	 */
	public IMatrix createGeneratorMatrix(MarkovAutomaton<? extends MarkovState> ma, boolean failStatesAreTerminal, double delta) {		
		SparseMatrix matrix = new SparseMatrix(ma.getStates().size());
		int countStates = ma.getStates().size();
		
		for (Object event : ma.getEvents()) {
			for (MarkovTransition<? extends MarkovState> transition : ma.getTransitions(event)) {
				int fromIndex = transition.getFrom().getIndex();
				if (!failStatesAreTerminal || !ma.getFinalStates().contains(transition.getFrom())) {
					matrix.getDiagonal()[fromIndex] -= transition.getRate() * delta;
				}
			}
		}
		
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = ma.getStates().get(i);
			List<?> transitions = ma.getPredTransitions(state);
			
			matrix.getStatePredIndices()[state.getIndex()] = new int[transitions.size()];
			matrix.getStatePredRates()[state.getIndex()] = new double[transitions.size()];
			for (int j = 0; j < transitions.size(); ++j) {
				MarkovTransition<?> transition = (MarkovTransition<?>) transitions.get(j);
				if (!failStatesAreTerminal || !ma.getFinalStates().contains(transition.getFrom())) {
					MarkovState fromState = (MarkovState) transition.getFrom();
					matrix.getStatePredIndices()[state.getIndex()][j] = fromState.getIndex();
					matrix.getStatePredRates()[state.getIndex()][j] = transition.getRate() * delta;
				}
			}
		}		
		return matrix;
	}
	
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
	public IMatrix createBellmanMatrix(MarkovAutomaton<? extends MarkovState> ma, List<? extends MarkovState> states, Set<? extends MarkovState> terminalStates, boolean invertEdgeDirection) {		
		SparseMatrix matrix = new SparseMatrix(states.size());
		
		Map<MarkovState, Integer> mapStateToIndex = new HashMap<>();
		for (int i = 0; i < states.size(); ++i) {
			mapStateToIndex.put(states.get(i), i);
		}
		
		for (MarkovState state : states) {
			int index = mapStateToIndex.get(state);

			if (state.isMarkovian() && (!invertEdgeDirection || !terminalStates.contains(state))) {
				List<?> transitions = invertEdgeDirection ? ma.getSuccTransitions(state) : ma.getPredTransitions(state);
				matrix.getStatePredIndices()[index] = new int[transitions.size()];
				matrix.getStatePredRates()[index] = new double[transitions.size()];

				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
					if (invertEdgeDirection || !terminalStates.contains(transition.getFrom())) {
						matrix.getStatePredIndices()[index][j] = mapStateToIndex.get(invertEdgeDirection ? transition.getTo() : transition.getFrom());
						double exitRate = ma.getExitRateForState(invertEdgeDirection ? state : transition.getFrom());
						matrix.getStatePredRates()[index][j] = transition.getRate() / exitRate;
					}
				}
			} else {
				matrix.getStatePredIndices()[index] = EMPTY_INDEX_LIST;
				matrix.getStatePredRates()[index] = EMPTY_RATES_LIST;
			}
		}
		
		// Make terminal states absorbing
		for (MarkovState terminalState : terminalStates) {
			int index = mapStateToIndex.get(terminalState);
			matrix.getDiagonal()[index] = 1;
		}
		
		return matrix;
	}
}
