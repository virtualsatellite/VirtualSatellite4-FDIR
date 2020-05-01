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

public class BellmanIterator extends MatrixIterator {
	
	private double[] baseMTTFs;
	private double[] result;
	private double[] oldValues;

	public BellmanIterator(IMatrix matrix, double[] initialValues, double eps) {
		super(matrix, initialValues, eps);
		baseMTTFs = initialValues.clone();
		result = new double[initialValues.length];
	}

	@Override
	public void iterate() {
		oldValues = values.clone();
		matrix.multiply(values, result);
		
		for (int i = 0; i < baseMTTFs.length; ++i) {
			result[i] += baseMTTFs[i];
		}
		values = result.clone();	
	}
	
	@Override
	public double[] getOldValues() {
		return oldValues;
	}
	
	@Override
	public double getChange() {
		return Math.abs(oldValues[0] - values[0]);	
	}
}
