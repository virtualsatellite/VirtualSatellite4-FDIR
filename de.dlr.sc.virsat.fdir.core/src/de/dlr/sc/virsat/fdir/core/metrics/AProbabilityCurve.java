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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.MarkovAutomatonValueIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.SPSIterator;

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
		int countProbabilites = getComposedProbabilityCurveSize(probabilityCurves, k);

		double[] childProbabilities = new double[probabilityCurves.size()];
		for (int i = 0; i < countProbabilites; ++i) {

			for (int j = 0; j < probabilityCurves.size(); ++j) {
				List<Double> probabilityCurve = probabilityCurves.get(j);
				childProbabilities[j] = i < probabilityCurve.size() ? probabilityCurve.get(i) : 1;
			}

			double composedProbability = IQuantitativeMetric.composeProbabilities(childProbabilities, k);
			resultCurve.add(composedProbability);

			if (composedProbability == stopValue) {
				break;
			}
		}
	}
	
	/**
	 * Computes the needed size of the composed probability curve, when given a list of probability curves.
	 * @param probabilityCurves the probability curves to compose
	 * @param k the distribution parameter
	 * @return the needed length of the composed probability curve
	 */
	private int getComposedProbabilityCurveSize(List<List<Double>> probabilityCurves, long k) {
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
		
		return countProbabilites;
	}
	
	/**
	 * Creates an iterator that computes for each iteration the current curve value after passgae of 1 abstract time unit.
	 * @param matrix the matrix encoding the transition behavior of the ma and the time
	 * @param ma the markov automaton
	 * @param initialDistribution the initial probability distribution
	 * @param eps the precision
	 * @return an iterator that gives the curve value after 1 abstract time unit after each iteration
	 */
	public IMatrixIterator iterator(IMatrix matrix, MarkovAutomaton<? extends MarkovState> ma, double[] initialDistribution, double eps) {
		return new MarkovAutomatonValueIterator<>(new SPSIterator(matrix, initialDistribution, eps), ma);
	}
}
