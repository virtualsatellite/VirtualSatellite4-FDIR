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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

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
	
	/**
	 * @param mc Markov Chain
	 */
	public TransitionMatrix(MarkovAutomaton<? extends MarkovState> mc) {
		int countStates = mc.getStates().size();
		this.diagonal = new double[countStates];
		this.statePredIndices = new int[countStates][];
		this.statePredRates = new double[countStates][];
	}
	
	/**
	 * @param vector vector
	 * @param result result buffer
	 * @return result
	 */
	public double multiply(double[] vector, double[] result) {
		int countStates = vector.length;
		double res = 0;

		for (int i = 0; i < countStates; ++i) {
			result[i] = vector[i] * this.getDiagonal()[i];
			int[] predIndices = this.getStatePredIndices()[i];
			double[] predRates = this.getStatePredRates()[i];

			for (int j = 0; j < predIndices.length; ++j) {
				double change = vector[predIndices[j]] * predRates[j];
				res += change * change;
				result[i] += change;
			}
		}
		return res;
	}
	
	
	/**
	 * @return Diagonal
	 */
	public double[] getDiagonal() {
		return diagonal;
	}	

	/**
	 * @return StatePredIndices
	 */
	public int[][] getStatePredIndices() {
		return statePredIndices;
	}	
	
	/**
	 * @return StatePredRates
	 */
	public double[][] getStatePredRates() {
		return statePredRates;
	}

	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double delta, double eps) {
		TransitionMatrixIterator tmi = new TransitionMatrixIterator(this, probabilityDistribution, delta, eps);
		return tmi;
	}	
}
