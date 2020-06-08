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

public class BellmanIterator extends LinearProgramIterator {
	
	private double[] stateCosts;

	public BellmanIterator(IMatrix matrix, double[] initialValues) {
		super(matrix, initialValues.clone());
		stateCosts = initialValues;
	}

	@Override
	public void iterate() {
		super.iterate();
		
		for (int i = 0; i < stateCosts.length; ++i) {
			getValues()[i] += stateCosts[i];
		}
	}
}
