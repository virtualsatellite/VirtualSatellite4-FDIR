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
	
	private MatrixIterator mi;

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double delta, double eps) {
		// TODO Auto-generated method stub
		return null;
	}
}
