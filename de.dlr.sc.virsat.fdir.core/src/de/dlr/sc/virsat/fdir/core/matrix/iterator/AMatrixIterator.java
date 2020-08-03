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
public abstract class AMatrixIterator implements IMatrixIterator {
	protected IMatrix matrix;
	protected double[] values;
	
	/**
	 * MatrixIterator constructor
	 * 
	 * @param matrix matrix
	 * @param initialValues probabilityDistribution
	 * @param eps epsilon
	 */
	public AMatrixIterator(IMatrix matrix, double[] initialValues) {
		this.matrix = matrix;
		this.values = initialValues;
	}
	
	@Override
	public double[] getValues() {
		return values;
	}
}
