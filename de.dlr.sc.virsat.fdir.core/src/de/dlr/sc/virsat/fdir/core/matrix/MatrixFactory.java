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
public class MatrixFactory implements IMatrixFactory {
	
	@Override
	public IMatrix createGeneratorMatrix(MarkovAutomaton<? extends MarkovState> ma, Set<? extends MarkovState> terminalStates, double delta) {		
		SparseMatrix matrix = new SparseMatrix(ma.getStates().size());
		
		for (MarkovState state : ma.getStates()) {
			if (state.isMarkovian() && !terminalStates.contains(state)) {
				int index = state.getIndex();
				double exitRate = ma.getExitRateForState(state);
				matrix.getDiagonal()[index] = -1 * exitRate * delta;
			}
		}
		
		int countStates = ma.getStates().size();
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = ma.getStates().get(i);
			List<?> transitions = ma.getPredTransitions(state);
			
			matrix.getStatePredIndices()[state.getIndex()] = new int[transitions.size()];
			matrix.getStatePredRates()[state.getIndex()] = new double[transitions.size()];
			for (int j = 0; j < transitions.size(); ++j) {
				MarkovTransition<?> transition = (MarkovTransition<?>) transitions.get(j);
				MarkovState fromState = (MarkovState) transition.getFrom();
				if (fromState.isMarkovian() && !terminalStates.contains(fromState)) {
					matrix.getStatePredIndices()[state.getIndex()][j] = fromState.getIndex();
					matrix.getStatePredRates()[state.getIndex()][j] = transition.getRate() * delta;
				}
			}
		}		
		return matrix;
	}
	
	@Override
	public IMatrix createBellmanMatrix(MarkovAutomaton<? extends MarkovState> ma, List<? extends MarkovState> states, Set<? extends MarkovState> terminalStates, boolean invertEdgeDirection) {		
		SparseMatrix matrix = new SparseMatrix(states.size());
		
		Map<MarkovState, Integer> mapStateToIndex = new HashMap<>();
		for (int i = 0; i < states.size(); ++i) {
			mapStateToIndex.put(states.get(i), i);
			// If the state space is reduced, there may be a mismatch between the indices of the state and the arrays. So, we assign the index of the array to the state.
			states.get(i).setValuesIndex(i);
		}
		
		for (MarkovState state : states) {
			int index = mapStateToIndex.get(state);

			// The state may be either Markovian or Probabilistic
			if (!state.isNondet() && (!invertEdgeDirection || !terminalStates.contains(state))) {
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
