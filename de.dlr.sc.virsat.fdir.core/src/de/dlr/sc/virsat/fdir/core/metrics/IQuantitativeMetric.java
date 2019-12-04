/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

/**
 * Interface for quantitative metrics
 * @author muel_s8
 *
 */

public interface IQuantitativeMetric extends IMetric {

	/**
	 * Computes accumulated poisson binomial probability mass function
	 * @param probabilities the individual event probabilities
	 * @param k the number of events that have to occur
	 * @return the composed event probability
	 */
	static double composeProbabilities(double[] probabilities, long k) {
		double composedProbability = 1;
		if (k == 1) {
			for (double failRate : probabilities) {
				composedProbability *= 1 - failRate;
			}
			composedProbability = 1 - composedProbability;
		} else {
			for (double failRate : probabilities) {
				composedProbability *= failRate;
			}
		}
		
		return composedProbability;
	}
}
