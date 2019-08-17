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

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;

/**
 * Container class for the results of a model checking call
 * @author muel_s8
 *
 */

public class ModelCheckingResult {

	protected Map<IMetric, Object> mapMetricToValue;

	/**
	 * Directly gets a double value associated to a metric
	 * @param metric the metric
	 * @return the value
	 */
	public Double getDouble(IMetric metric) {
		return (Double) mapMetricToValue.get(metric);
	}
	
	/**
	 * Directly gets a double list value associated to a metric
	 * @param metric the metric
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public List<Double> getDoubleCurve(IMetric metric) {
		return (List<Double>) mapMetricToValue.get(metric);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Set<Object>> getSetOfSets(IMetric metric) {
		return (Set<Set<Object>>) mapMetricToValue.get(metric);
	}
	
	/**
	 * Associate a metric to a value
	 * @param metric the metric
	 * @param value the value
	 */
	public void addMetricValue(IMetric metric, Object value) {
		mapMetricToValue.put(metric, value);
	}

	/**
	 * Limits all point wise metrics to the given number of entries
	 * @param steps the number of points the metric should be limited to
	 */
	public void limitPointMetrics(int steps) {
		/*
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
		*/
	}
}
