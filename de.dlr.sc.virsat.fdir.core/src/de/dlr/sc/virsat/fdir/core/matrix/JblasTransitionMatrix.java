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

/**
 * @author piet_ci
 * 
 * A Jblas Double Matrix class
 */
public class JblasTransitionMatrix extends DoubleMatrix implements IMatrix {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param countStates markov chain state count
	 */
	public JblasTransitionMatrix(int countStates) {
		super(countStates, countStates);
	}

	@Override
	public double multiply(double[] vector, double[] result) {
		int countStates = vector.length;
		double res = 0;

		for (int i = 0; i < countStates; ++i) {
			result[i] = vector[i] * this.get(i, i);	
					
			double[] row = this.getRow(i).toArray();
			
			for (int j = 0; j < row.length; ++j) {
				
				if (row[j] < 0) {
					row[j] = 0;
				}
				
				double change = row[j] * vector[j];
				res += change * change;
				result[i] += change;
			}
		}
		return res;
	}

	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double delta, double eps) {
		return new JblasTransitionMatrixIterator(this, probabilityDistribution, delta, eps);
	}
}
