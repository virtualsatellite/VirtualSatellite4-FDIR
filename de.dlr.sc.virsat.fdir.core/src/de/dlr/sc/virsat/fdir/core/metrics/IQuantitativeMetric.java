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

import java.util.List;

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
		
		// TODO: Else for general k
		
		return composedProbability;
	}
	
	/**
	 * Composes a list of probability curves into a combined probability curve.
	 * The individual probabilities are combines point wise
	 * @param probabilityCurves the probability curves
	 * @param resultCurve the holder of the result curve
	 * @param k the distribution parameter
	 * @param stopValue if this stop value is hit, the probability curve will be cut off here
	 */
	static void composeProbabilityCurve(List<List<Double>> probabilityCurves, List<Double> resultCurve, long k, double stopValue) {
		int countProbabilites = 0;
		if (k == 1) {
			countProbabilites = Integer.MAX_VALUE;
			for (List<Double> probabilityCurve : probabilityCurves) {
				countProbabilites = Math.min(countProbabilites, probabilityCurve.size());
			}
		} else {
			for (List<Double> probabilityCurve : probabilityCurves) {
				countProbabilites = Math.max(countProbabilites, probabilityCurve.size());
			}
		}
		
		double[] childProbabilities = new double[probabilityCurves.size()];
		for (int i = 0; i < countProbabilites; ++i) {
			
			for (int j = 0; j < probabilityCurves.size(); ++j) {
				List<Double> probabilityCurve = probabilityCurves.get(j);
				if (i < probabilityCurve.size()) {
					childProbabilities[j] = probabilityCurve.get(i);
				} else {
					childProbabilities[j] = 1;
				}
			}
			
			double composedProbability = IQuantitativeMetric.composeProbabilities(childProbabilities, k);
			resultCurve.add(composedProbability);
			
			if (composedProbability == stopValue) {
				break;
			}
		}
	}
}
