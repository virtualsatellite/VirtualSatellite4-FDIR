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

import jeigen.DenseMatrix;

/**
 * @author piet_ci
 * 
 * Class representing an Iterator, iterating a matrix and probability distribution over time
 * using matrix exponentials.
 *
 */

public class JEigenExponentialMatrixIterator extends MatrixIterator {
	
	DenseMatrix transitionMatrix;
	DenseMatrix expMatrix;

	/**
	 * @param tmTerminal transition matrix
	 * @param probabilityDistribution probability distribution
	 * @param delta delta
	 * @param eps epsilon
	 */
	public JEigenExponentialMatrixIterator(JEigenTransitionMatrix tmTerminal, double[] probabilityDistribution, double delta, double eps) {
		super(tmTerminal, probabilityDistribution, delta, eps);
		transitionMatrix = (DenseMatrix) matrix;
		expMatrix = transitionMatrix.mexp();
	}
	
	/**
	 * Performs one update iteration
	 */
	public void iterate() {		
		double[][] values = {probabilityDistribution};
		DenseMatrix probDisMatrix = new DenseMatrix(values); 
		probDisMatrix = probDisMatrix.t();
		DenseMatrix result = expMatrix.mmul(probDisMatrix);
		probabilityDistribution = result.getValues();
		iterationCounter++;
	}	
}