/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;

/**
 * @author piet_ci
 * 
 * Abstract matrix iterator class   
 */
public abstract class MatrixIterator implements IMatrixIterator {
	protected IMatrix matrix;
	protected double[] values;
	protected double eps;	
	
	/**
	 * MatrixIterator constructor
	 * 
	 * @param matrix matrix
	 * @param initialValues probabilityDistribution
	 * @param eps epsilon
	 */
	public MatrixIterator(IMatrix matrix, double[] initialValues, double eps) {
		this.matrix = matrix;
		this.values = initialValues;
		this.eps = eps;
	}
	
	@Override
	public double[] getValues() {
		return values;
	}
}
