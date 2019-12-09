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

import jeigen.SparseMatrixLil;

/**
 * @author piet_ci
 *
 */
public class JEigSparseTransitionMatrix extends SparseMatrixLil implements IMatrix {

	/**
	 * @param rows rows
	 * @param cols cols
	 */
	public JEigSparseTransitionMatrix(int rows, int cols) {
		super(rows, cols);
	}

	@Override
	public void multiply(double[] vector, double[] result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double delta, double eps) {
		return new SPSIterator(this, probabilityDistribution, delta, eps);
	}

	@Override
	public int getCountStates() {
		return getSize();
	}

	@Override
	public double[] getDiagonal() {
		return null;
	}

	@Override
	public int[][] getStatePredIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][] getStatePredRates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDiagonal(double[] diagonal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatePredIndices(int[][] statePredIndices) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatePredRates(double[][] statePredRates) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMatrix copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
