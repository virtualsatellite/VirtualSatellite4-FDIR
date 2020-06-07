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
	
	private MarkovAutomaton<? extends MarkovState> mc;
	
	/**
	 * @param mc markov chain
	 * @param failStatesAreTerminal failStatesAreTerminal
	 * @param delta delta
	 * @return transition matrix
	 */
	public TransitionMatrix getTransitionMatrix(MarkovAutomaton<? extends MarkovState> mc, boolean failStatesAreTerminal, double delta) {		
		this.mc = mc;
		TransitionMatrix tm = new TransitionMatrix(mc.getStates().size());
		tm = createTransitionMatrix(tm, failStatesAreTerminal, delta);
		return tm;
	}
	
	/**
	 * @param mc markov chain
	 * @param failStatesAreTerminal whether the final states are terminal states
	 * @return transition matrix
	 */
	public BellmanMatrix getBellmanMatrix(MarkovAutomaton<? extends MarkovState> mc, List<? extends MarkovState> states, Set<? extends MarkovState> terminalStates, boolean invertEdgeDirection) {		
		this.mc = mc;
		BellmanMatrix tm = new BellmanMatrix(states.size());
		tm = createBellmanMatrix(tm, states, terminalStates, invertEdgeDirection);
		return tm;
	}
	/**
	 * Creates a transition matrix
	 * @param tm transition matrix
	 * @param failStatesAreTerminal failStatesAreTerminal
	 * @param delta delta
	 * @return transition matrix
	 */
	private TransitionMatrix createTransitionMatrix(TransitionMatrix tm, boolean failStatesAreTerminal, double delta) {
		int countStates = mc.getStates().size();
		
		for (Object event : mc.getEvents()) {
			for (MarkovTransition<? extends MarkovState> transition : mc.getTransitions(event)) {
				int fromIndex = transition.getFrom().getIndex();
				if (!failStatesAreTerminal || !mc.getFinalStates().contains(transition.getFrom())) {
					tm.getDiagonal()[fromIndex] -= transition.getRate() * delta;
				}
			}
		}
		
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			List<?> transitions = mc.getPredTransitions(state);
			
			tm.getStatePredIndices()[state.getIndex()] = new int[transitions.size()];
			tm.getStatePredRates()[state.getIndex()] = new double[transitions.size()];
			for (int j = 0; j < transitions.size(); ++j) {
				@SuppressWarnings("unchecked")
				MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
				if (!failStatesAreTerminal || !mc.getFinalStates().contains(transition.getFrom())) {
					tm.getStatePredIndices()[state.getIndex()][j] = transition.getFrom().getIndex();
					tm.getStatePredRates()[state.getIndex()][j] = transition.getRate() * delta;
				}
			}
		}		
		return tm;
	}
	
	private static final int[] EMPTY_INDEX_LIST = new int[0];
	private static final double[] EMPTY_RATES_LIST = new double[0];
	
	/**
	 * Creates the iteration matrix for computing the Mean Time To Failure (MTTF)
	 * according to the Bellman equation: MTTF(s) = 0 if s is a fail state MTTF(s) =
	 * 1/ExitRate(s) + SUM(s' successor of s: Prob(s, s') * MTTF(s')
	 * 
	 * @param tm TransitionMatrix
	 * @param states 
	 * @param failStatesAreTerminal whether fail states are terminal
	 * @return the matrix representing the fixpoint iteration
	 */
	public BellmanMatrix createBellmanMatrix(BellmanMatrix tm, List<? extends MarkovState> states, Set<? extends MarkovState> terminalStates, boolean invertEdgeDirection) {
		Map<MarkovState, Integer> mapStateToIndex = new HashMap<>();
		for (int i = 0; i < states.size(); ++i) {
			mapStateToIndex.put(states.get(i), i);
		}
		
		for (MarkovState state : states) {
			int index = mapStateToIndex.get(state);

			if (state.isMarkovian() && (!invertEdgeDirection || !terminalStates.contains(state))) {
				List<?> transitions = invertEdgeDirection ? mc.getSuccTransitions(state) : mc.getPredTransitions(state);
				tm.getStatePredIndices()[index] = new int[transitions.size()];
				tm.getStatePredRates()[index] = new double[transitions.size()];

				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
					if (invertEdgeDirection || !terminalStates.contains(transition.getFrom())) {
						tm.getStatePredIndices()[index][j] = mapStateToIndex.get(invertEdgeDirection ? transition.getTo() : transition.getFrom());
						double exitRate = mc.getExitRateForState(invertEdgeDirection ? state : transition.getFrom());
						tm.getStatePredRates()[index][j] = transition.getRate() / exitRate;
					}
				}
			} else {
				tm.getStatePredIndices()[index] = EMPTY_INDEX_LIST;
				tm.getStatePredRates()[index] = EMPTY_RATES_LIST;
			}
		}
		
		// Make terminal states absorbing
		for (MarkovState terminalState : terminalStates) {
			int index = mapStateToIndex.get(terminalState);
			tm.getDiagonal()[index] = 1;
		}
		
		return tm;
	}	
}
