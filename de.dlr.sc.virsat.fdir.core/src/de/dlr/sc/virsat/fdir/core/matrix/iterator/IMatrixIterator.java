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

public interface IMatrixIterator {
	/**
	 * Iterate performs one update iteration.
	 */
	void iterate();
	
	/**
	 * Gets the current value distribution
	 * @return probability distribution at current time
	 */
	double[] getValues();
	
	/**
	 * Gets the value distribution of the previous iteration
	 * @return the value distribution of the previous iterator
	 */
	default double[] getOldValues() {
		return null;
	}
	
	/**
	 * Gets the change between the distributions
	 * @return the change between the distributions
	 */
	default double getChangeSquared() {
		double change = 0;
		for (int i = 0; i < getValues().length; ++i) {
			double localChange = getOldValues()[i] - getValues()[i];
			change += localChange * localChange;
		}
		return change;
	}
	
	/**
	 * Performs iterations until the iterator the values have converged
	 * @param eps the precision
	 * @return the converged values
	 */
	default double[] converge(double eps) {
		boolean convergence = false;
		while (!convergence) {
			iterate();
			double change = getChangeSquared();
			if (change < eps * eps || Double.isNaN(change)) {
				convergence = true;
			}
		}
		
		return getValues();
	}
}
