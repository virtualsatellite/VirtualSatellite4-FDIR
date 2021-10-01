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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Container class for the results of a model checking call
 * @author muel_s8
 *
 */

public class ModelCheckingResult {

	protected List<Double> failRates = new ArrayList<>();
	private double meanTimeToFailure;
	protected List<Double> availability = new ArrayList<>();
	private double steadyStateAvailability;
	protected Set<Set<Object>> minCutSets = new HashSet<>();
	private double meanTimeToDetection;
	private double meanTimeToObservedFailure;
	private double steadyStateDetectability;
	protected List<Double> detectabiity = new ArrayList<>();
	private long faultTolerance;
	
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
	 * Sets the mean time to detection
	 * @param meanTimeToDetection the mean time to detection
	 */
	public void setMeanTimeToDetection(double meanTimeToDetection) {
		this.meanTimeToDetection = meanTimeToDetection;
	}
	
	/**
	 * Sets the mean time to detection
	 * @param meanTimeToDetection the mean time to detection
	 */
	public void setMeanTimeToObservedFailure(double meanTimeToObservedFailure) {
		this.meanTimeToObservedFailure = meanTimeToObservedFailure;
	}
	
	/**
	 * Sets the steady state detectability
	 * @param steadyStateDetectability the steady state detectability
	 */
	public void setSteadyStateDetectability(double steadyStateDetectability) {
		this.steadyStateDetectability = steadyStateDetectability;
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
	public List<Double> getAvailability() {
		return availability;
	}

	/**
	 * Gets the computed steady state availability
	 * @return the steady state availability
	 */
	public double getSteadyStateAvailability() {
		return steadyStateAvailability;
	}
	
	/**
	 * Gets the computed mincut sets
	 * @return the computed mincut sets
	 */
	public Set<Set<Object>> getMinCutSets() {
		return minCutSets;
	}
	
	/**
	 * Gets the mean time to detection
	 * @return the mean time to detection
	 */
	public double getMeanTimeToDetection() {
		return meanTimeToDetection;
	}
	
	/**
	 * Gets the mean time to detection
	 * @return the mean time to detection
	 */
	public double getMeanTimeToObservedFailure() {
		return meanTimeToObservedFailure;
	}
	
	/**
	 * Gets the steady state detectability
	 * @return the steady state detectability
	 */
	public double getSteadyStateDetectability() {
		return steadyStateDetectability;
	}
	
	/**
	 * Gets the detectability curve
	 * @return the detectability curve
	 */
	public List<Double> getDetectabiity() {
		return detectabiity;
	}

	/**
	 * Gets the computed fault tolerance
	 * @return the fault tolerance
	 */
	public long getFaultTolerance() {
		return faultTolerance;
	}
	
	/**
	 * Sets the fault tolerance
	 * @param derivedFaultTolerance the fault tolerance
	 */
	public void setFaultTolerance(long derivedFaultTolerance) {
		this.faultTolerance = derivedFaultTolerance;
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
		
		if (!availability.isEmpty()) {
			availability = availability.stream().limit(steps).collect(Collectors.toList());
			while (availability.size() < steps) {
				availability.add(1d);
			}
		}
	}
}
