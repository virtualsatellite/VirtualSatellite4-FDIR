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
 * This is an abstract class for probability curve metrics
 * @author muel_s8
 *
 */

public abstract class AProbabilityCurve implements IQuantitativeMetric {

	protected double time;
	
	/**
	 * Standard constructor
	 * @param time the time
	 */
	public AProbabilityCurve(double time) {
		this.time = time;
	}

	/**
	 * Gets the end time of the curve
	 * @return the end time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Composes a list of probability curves into a combined probability curve. The
	 * individual probabilities are combines point wise
	 * 
	 * @param probabilityCurves the probability curves
	 * @param resultCurve the holder of the result curve
	 * @param k the distribution parameter
	 * @param stopValue if this stop value is hit, the probability curve will be cut off here
	 */
	public void composeProbabilityCurve(List<List<Double>> probabilityCurves, List<Double> resultCurve, long k,
			double stopValue) {
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
