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
public abstract class MatrixIterator {
	protected IMatrix matrix;
	protected double[] probabilityDistribution;
	protected double eps;	
	/**
	 * MatrixIterator constructor
	 * 
	 * @param matrix matrix
	 * @param probabilityDistribution probabilityDistribution
	 * @param eps epsilon
	 */
	public MatrixIterator(IMatrix matrix, double[] probabilityDistribution, double eps) {
		this.matrix = matrix;
		this.probabilityDistribution = probabilityDistribution;
		this.eps = eps;
	}
	
	/**
	 * abstract iterate method for different matrix iterators. Iterate performs one update iteration.
	 */
	public abstract void iterate();
	
	/**
	 * @return probability distribution at current time
	 */
	public double[] getProbabilityDistribution() {
		return probabilityDistribution;
	}

	public double[] getOldProbabilityDistribution() {
		return null;
	}

	public double getChange() {
		return 0;
	}
}
