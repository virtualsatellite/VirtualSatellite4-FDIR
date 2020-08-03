/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;

public class LinearProgramIterator extends AMatrixIterator {
	
	private double[] result;
	private double[] oldValues;

	public LinearProgramIterator(IMatrix matrix, double[] initialValues) {
		super(matrix, initialValues);
		result = new double[initialValues.length];
		oldValues = new double[initialValues.length];
	}

	@Override
	public void iterate() {
		for (int i = 0; i < values.length; ++i) {
			oldValues[i] = values[i];
		}
		
		matrix.multiply(values, result);
		
		double[] tmp = values;
		values = result;
		result = tmp;
	}
	
	@Override
	public double[] getOldValues() {
		return oldValues;
	}
}
