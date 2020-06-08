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
	 * @return returns number of states
	 */
	int size();

	/**
	 * @return returns matrix diagonal
	 */
	double[] getDiagonal();

	/**
	 * @param diagonal sets new diagonal for this matrix
	 */
	void setDiagonal(double[] diagonal);

	/**
	 * @return returns copy of this matrix object
	 */
	IMatrix copy();
}
