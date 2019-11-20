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

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 * @author piet_ci
 * 
 * Class representing an Iterator, iterating a matrix and probability distribution over time
 * using matrix exponentials.
 *
 */
public class JblasExponentialMatrixIterator extends MatrixIterator {	

	/**
	 * @param tmTerminal transition matrix
	 * @param probabilityDistribution probability distribution
	 * @param delta delta
	 * @param eps epsilon
	 */
	public JblasExponentialMatrixIterator(JblasTransitionMatrix tmTerminal, double[] probabilityDistribution, double delta, double eps) {
		super(tmTerminal, probabilityDistribution, delta, eps);
	}
	
	/**
	 * Performs one update iteration
	 */
	public void iterate() {
		DoubleMatrix transitionMatrix = ((JblasTransitionMatrix) (matrix)).mmul(iterationCounter);
		DoubleMatrix expMatrix = MatrixFunctions.expm(transitionMatrix);
		DoubleMatrix probDisMatrix = new DoubleMatrix(initialProbabilityDistribution);
		
		DoubleMatrix result = expMatrix.mmul(probDisMatrix);
		probabilityDistribution = result.toArray();
		iterationCounter++;
	}	
}
