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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

/**
 * Encodes the transition matrix of the markov chain
 * @author muel_s8
 *
 */
public class Matrix implements IMatrix {
	
	private double[] diagonal;
	private int[][] statePredIndices;
	private double[][] statePredRates;
	private MarkovAutomaton<? extends MarkovState> mc;
	
	
	/**
	 * Constructor creating the transition matrix for the mc
	 * @param mc Markov Chain
	 */
	public Matrix(MarkovAutomaton<? extends MarkovState> mc) {
		this.mc = mc;
		int countStates = mc.getStates().size();
		setDiagonal(new double[countStates]);
		setStatePredIndices(new int[countStates][]);
		setStatePredRates(new double[countStates][]);
	}
	
	/**
	 * Creates a transition matrix
	 * 
	 * @param failStatesAreTerminal whether fail states should be treated as
	 *                              terminal states
	 * @param delta Delta
	 * @return a transition matrix
	 */
	public Matrix createTransitionMatrix(boolean failStatesAreTerminal, double delta) {
		int countStates = mc.getStates().size();
		
		for (Object event : mc.getEvents()) {
			for (MarkovTransition<? extends MarkovState> transition : mc.getTransitions(event)) {
				int fromIndex = transition.getFrom().getIndex();
				if (!failStatesAreTerminal || !mc.getFinalStates().contains(transition.getFrom())) {
					this.getDiagonal()[fromIndex] -= transition.getRate() * delta;
				}
			}
		}
		
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			List<?> transitions = mc.getPredTransitions(state);
			
			this.getStatePredIndices()[state.getIndex()] = new int[transitions.size()];
			this.getStatePredRates()[state.getIndex()] = new double[transitions.size()];
			for (int j = 0; j < transitions.size(); ++j) {
				@SuppressWarnings("unchecked")
				MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
				if (!failStatesAreTerminal || !mc.getFinalStates().contains(transition.getFrom())) {
					this.getStatePredIndices()[state.getIndex()][j] = transition.getFrom().getIndex();
					this.getStatePredRates()[state.getIndex()][j] = transition.getRate() * delta;
				}
			}
		}
		
		return this;
	}
	
	/**
	 * Creates the iteration matrix for computing the Mean Time To Failure (MTTF)
	 * according to the Bellman equation: MTTF(s) = 0 if s is a fail state MTTF(s) =
	 * 1/ExitRate(s) + SUM(s' successor of s: Prob(s, s') * MTTF(s')
	 * 
	 * @return the matrix representing the fixpoint iteration
	 */
	public Matrix createBellmanMatrix() {
		int countStates = mc.getStates().size();

		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			List<?> transitions = mc.getSuccTransitions(state);

			this.getStatePredIndices()[state.getIndex()] = new int[transitions.size()];
			this.getStatePredRates()[state.getIndex()] = new double[transitions.size()];

			if (!mc.getFinalStates().contains(state)) {
				double exitRate = 0;
				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions
							.get(j);
					exitRate += transition.getRate();
				}

				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions
							.get(j);
					this.getStatePredIndices()[state.getIndex()][j] = transition.getTo().getIndex();
					this.getStatePredRates()[state.getIndex()][j] = transition.getRate() / exitRate;
				}
			}
		}

		return this;
	}

	/**
	 * @return Diagonal
	 */
	public double[] getDiagonal() {
		return diagonal;
	}

	/**
	 * @param diagonal Diagonal
	 */
	public void setDiagonal(double[] diagonal) {
		this.diagonal = diagonal;
	}

	/**
	 * @return StatePredIndices
	 */
	public int[][] getStatePredIndices() {
		return statePredIndices;
	}

	/**
	 * @param statePredIndices StatePredIndices
	 */
	public void setStatePredIndices(int[][] statePredIndices) {
		this.statePredIndices = statePredIndices;
	}
	
	
	/**
	 * @return StatePredRates
	 */
	public double[][] getStatePredRates() {
		return statePredRates;
	}

	/**
	 * @param statePredRates StatePredRates
	 */
	public void setStatePredRates(double[][] statePredRates) {
		this.statePredRates = statePredRates;
	}

	@Override
	public void multiply(double[] vector, double[] result) {
		// TODO Auto-generated method stub
	}

	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double delta, double eps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCountStates() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IMatrix copy() {
		// TODO Auto-generated method stub
		return null;
	}
}
