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
	
	private double[] stateCosts;
	private double[] result;
	private double[] oldValues;

	public BellmanIterator(IMatrix matrix, double[] initialValues) {
		super(matrix, initialValues);
		stateCosts = initialValues.clone();
		result = new double[initialValues.length];
		oldValues = new double[initialValues.length];
	}

	@Override
	public void iterate() {
		for (int i = 0; i < values.length; ++i) {
			oldValues[i] = values[i];
		}
		
		matrix.multiply(values, result);
		
		for (int i = 0; i < stateCosts.length; ++i) {
			result[i] += stateCosts[i];
		}
		
		double[] tmp = values;
		values = result;
		result = tmp;
	}
	
	@Override
	public double[] getOldValues() {
		return oldValues;
	}
}
