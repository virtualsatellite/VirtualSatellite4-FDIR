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

public class BellmanIterator extends MatrixIterator {
	
	private double[] baseMTTFs;
	private double[] result;
	private double[] oldProbabilityDistribution;

	public BellmanIterator(IMatrix matrix, double[] probabilityDistribution, double eps) {
		super(matrix, probabilityDistribution, eps);
		baseMTTFs = probabilityDistribution.clone();
		result = new double[probabilityDistribution.length];
	}

	@Override
	public void iterate() {
		oldProbabilityDistribution = probabilityDistribution.clone();
		matrix.multiply(probabilityDistribution, result);
		
		for (int i = 0; i < baseMTTFs.length; ++i) {
			result[i] += baseMTTFs[i];
		}
		probabilityDistribution = result.clone();	
	}
	
	@Override
	public double[] getOldProbabilityDistribution() {
		return oldProbabilityDistribution;
	}
	
	@Override
	public double getChange() {
		return Math.abs(oldProbabilityDistribution[0] - probabilityDistribution[0]);	
	}
}
