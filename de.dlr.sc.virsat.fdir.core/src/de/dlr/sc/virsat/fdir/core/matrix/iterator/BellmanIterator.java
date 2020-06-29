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

	/**
	 * Standard constructor
	 * @param matrix the bellman matrix
	 * @param stateCosts the costs of being in a state per each time unit
	 */
	public BellmanIterator(IMatrix matrix, double[] stateCosts) {
		super(matrix, stateCosts.clone());
		this.stateCosts = stateCosts;
	}

	@Override
	public void iterate() {
		super.iterate();
		
		for (int i = 0; i < stateCosts.length; ++i) {
			getValues()[i] += stateCosts[i];
		}
	}
}
