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

/**
 * @author piet_ci
 * 
 * Class representing a transition matrix
 *
 */
public class SparseMatrix implements IMatrix {
	
	private double[] diagonal;
	private int[][] statePredIndices;
	private double[][] statePredRates;
	private int countStates;
	
	/**
	 * @param countStates markov chain state count
	 */
	public SparseMatrix(int countStates) {
		this.countStates = countStates;
		this.setDiagonal(new double[countStates]);
		this.setStatePredIndices(new int[countStates][]);
		this.setStatePredRates(new double[countStates][]);
	}
	
	/**
	 * @param vector vector
	 * @param result result buffer
	 */
	@Override
	public void multiply(double[] vector, double[] result) {
		int countStates = vector.length;
		
		for (int i = 0; i < countStates; ++i) {
			result[i] = vector[i] * this.getDiagonal()[i];
			int[] predIndices = this.getStatePredIndices()[i];
			double[] predRates = this.getStatePredRates()[i];

			for (int j = 0; j < predIndices.length; ++j) {
				result[i] += vector[predIndices[j]] * predRates[j];
			}
		}
	}	
	
	/**
	 * @return Diagonal
	 */
	@Override
	public double[] getDiagonal() {
		return diagonal;
	}
	
	/**
	 * @param diagonal matrix diagonal
	 */
	public void setDiagonal(double[] diagonal) {
		this.diagonal = diagonal.clone();
	}

	/**
	 * @return StatePredIndices
	 */
	public int[][] getStatePredIndices() {
		return statePredIndices;
	}
	
	/**
	 * @param statePredIndices statePredIndices
	 */
	public void setStatePredIndices(int[][] statePredIndices) {
		this.statePredIndices = statePredIndices.clone();
	}	
	
	/**
	 * @return StatePredRates
	 */
	public double[][] getStatePredRates() {
		return statePredRates;
	}
	
	/**
	 * @param statePredRates statePredRates
	 */
	public void setStatePredRates(double[][] statePredRates) {
		this.statePredRates = statePredRates.clone();
	}
	
	/**
	 * @return countStates
	 */
	@Override
	public int size() {
		return countStates;
	}

	@Override
	public IMatrix copy() {
		SparseMatrix t = new SparseMatrix(this.size());
		t.setDiagonal(this.getDiagonal());
		t.setStatePredIndices(this.getStatePredIndices());
		t.setStatePredRates(this.getStatePredRates());
		return t;
	}

	@Override
	public double getValue(int column, int row) {
		return diagonal[column];
	}

	@Override
	public void setValue(int column, int row, double value) {
		diagonal[column] = value;
	}
}
