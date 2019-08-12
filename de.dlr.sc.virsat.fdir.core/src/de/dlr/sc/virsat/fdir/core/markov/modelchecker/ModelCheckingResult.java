/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Container class for the results of a model checking call
 * @author muel_s8
 *
 */

public class ModelCheckingResult {

	protected List<Double> failRates = new ArrayList<>();
	private double meanTimeToFailure;
	protected List<Double> pointAvailability = new ArrayList<>();
	private double steadyStateAvailability;
	
	/**
	 * Sets the mean time to failure
	 * @param meanTimeToFailure the mean time to failure
	 */
	public void setMeanTimeToFailure(double meanTimeToFailure) {
		this.meanTimeToFailure = meanTimeToFailure;
	}
	
	/**
	 * Sets the steady state availability
	 * @param steadyStateAvailability the steady state availability
	 */
	public void setSteadyStateAvailability(double steadyStateAvailability) {
		this.steadyStateAvailability = steadyStateAvailability;
	}
	
	/**
	 * Gets the computed failure rates
	 * @return the failure rates
	 */
	public List<Double> getFailRates() {
		return failRates;
	}
	
	/**
	 * Gets the computed mean time to failure
	 * @return the the mean time to failure
	 */
	public double getMeanTimeToFailure() {
		return meanTimeToFailure;
	}
	
	/**
	 * Gets the computed point availability
	 * @return the point availability
	 */
	public List<Double> getPointAvailability() {
		return pointAvailability;
	}

	/**
	 * Gets the computed steady state availability
	 * @return the steady state availability
	 */
	public double getSteadyStateAvailability() {
		return steadyStateAvailability;
	}

	/**
	 * Limits all point wise metrics to the given number of entries
	 * @param steps the number of points the metric should be limited to
	 */
	public void limitPointMetrics(int steps) {
		if (!failRates.isEmpty()) {
			failRates = failRates.stream().limit(steps).collect(Collectors.toList());
			while (failRates.size() < steps) {
				failRates.add(1d);
			}
		}
		
		if (!pointAvailability.isEmpty()) {
			pointAvailability = pointAvailability.stream().limit(steps).collect(Collectors.toList());
			while (pointAvailability.size() < steps) {
				pointAvailability.add(1d);
			}
		}
	}
}
