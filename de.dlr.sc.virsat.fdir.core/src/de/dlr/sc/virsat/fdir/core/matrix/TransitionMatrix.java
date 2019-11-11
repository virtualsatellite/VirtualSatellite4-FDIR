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
 * @author piet_ci
 * 
 * Class representing a transition matrix
 *
 */
public class TransitionMatrix implements IMatrix {
	
	private double[] diagonal;
	private int[][] statePredIndices;
	private double[][] statePredRates;
	private MarkovAutomaton<? extends MarkovState> mc;
	private double delta;
	private double eps;
	private boolean failStatesAreTerminal;
	
	private double[] resultBuffer;
	
	/**
	 * @param mc Markov Chain
	 */
	public TransitionMatrix(MarkovAutomaton<? extends MarkovState> mc) {
		this.mc = mc;
		int countStates = mc.getStates().size();
		setDiagonal(new double[countStates]);
		setStatePredIndices(new int[countStates][]);
		setStatePredRates(new double[countStates][]);
	}
	
	
	/**
	 * Performs one update iteration
	 * 
	 * @param probabilityDistribution probabilityDistribution
	 */
	public void iterate(double[] probabilityDistribution) {
		double[] res = probabilityDistribution;
		probabilityDistribution = new double[probabilityDistribution.length];

		double lambda = 1;
		int i = 0;
		boolean convergence = false;
		while (!convergence) {
			for (int j = 0; j < probabilityDistribution.length; ++j) {
				probabilityDistribution[j] += res[j] * lambda;
			}

			lambda = lambda / (i + 1);
			double change = lambda * multiply(this, res, resultBuffer) / delta;

			// Swap the discrete time buffers
			double[] tmp = res;
			res = resultBuffer;
			resultBuffer = tmp;

			if (change < getEps() * getEps() || !Double.isFinite(change)) {
				for (int j = 0; j < probabilityDistribution.length; ++j) {
					probabilityDistribution[j] += res[j] * lambda;
				}

				convergence = true;
			} else {
				++i;
			}
		}
	}
	
	/**
	 * Performs a discrete time abstract step.
	 * @param tm the transition matrix
	 * @param vector the current probability distribution
	 * @param result the vector to put the result into
	 * @return squared length of the result vector
	 */
	private double multiply(TransitionMatrix tm, double[] vector, double[] result) {
		int countStates = vector.length;
		double res = 0;

		for (int i = 0; i < countStates; ++i) {
			result[i] = vector[i] * tm.getDiagonal()[i];
			int[] predIndices = tm.getStatePredIndices()[i];
			double[] predRates = tm.getStatePredRates()[i];

			for (int j = 0; j < predIndices.length; ++j) {
				double change = vector[predIndices[j]] * predRates[j];
				res += change * change;
				result[i] += change;
			}
		}

		return res;
	}
	
	
	/**
	 * Creates a transition matrix
	 * 
	 * @return a transition matrix
	 */
	public TransitionMatrix createTransitionMatrix() {
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




	/**
	 * @return delta
	 */
	public double getDelta() {
		return delta;
	}




	/**
	 * @param delta delta
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}




	/**
	 * @return failStatesAreTerminal
	 */
	public boolean isFailStatesAreTerminal() {
		return failStatesAreTerminal;
	}




	/**
	 * @param failStatesAreTerminal failStatesAreTerminal
	 */
	public void setFailStatesAreTerminal(boolean failStatesAreTerminal) {
		this.failStatesAreTerminal = failStatesAreTerminal;
	}


	/**
	 * @return eps
	 */
	public double getEps() {
		return eps;
	}


	/**
	 * @param eps eps
	 */
	public void setEps(double eps) {
		this.eps = eps;
	}
}
