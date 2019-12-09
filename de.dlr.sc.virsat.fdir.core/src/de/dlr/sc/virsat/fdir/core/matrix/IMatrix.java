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
 * Interface for matrices
 * @author piet
 * 
 * Matrix Interface for different types of matrices
 *
 */
public interface IMatrix {
	/**
	 * @param vector vector
	 * @param result resultbuffer
	 */
	void multiply(double[] vector, double[] result);

	/**
	 * Returns an iterator for specific matrix type.
	 * 
	 * @param probabilityDistribution probabilityDistribution
	 * @param delta delta
	 * @param eps epsilon
	 * @return MatrixIterator
	 */
	MatrixIterator getIterator(double[] probabilityDistribution, double delta, double eps);

	int getCountStates();

	double[] getDiagonal();

	int[][] getStatePredIndices();

	double[][] getStatePredRates();

	void setDiagonal(double[] diagonal);

	void setStatePredIndices(int[][] statePredIndices);

	void setStatePredRates(double[][] statePredRates);

	IMatrix copy();
}
