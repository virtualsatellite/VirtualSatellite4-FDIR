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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Interface for metrics
 * @author sascha
 *
 */

public interface IMetric {

	/**
	 * Splits the given set of metrics into a set of basic and derived metrics.
	 * Also, if necessary, adds new metrics that are required to perform the composition.
	 * @param metrics the original metrics
	 * @param deriveIfPossible if a metric is both a base metric and a derived metric, then this flag
	 * will prioritize derivation. If set to false, the metric will be considered a base metric.
	 * @return a map with the partitioned metrics
	 */
	static Map<Class<?>, IMetric[]> partitionMetrics(IMetric[] metrics, boolean deriveIfPossible) {
		List<IBaseMetric> baseMetrics = new ArrayList<>();
		List<IDerivedMetric> derivedMetrics = new ArrayList<>();
		
		Queue<IMetric> toProcess = new LinkedList<>(Arrays.asList(metrics));
		
		while (!toProcess.isEmpty()) {
			IMetric metric = toProcess.poll();
			if (metric instanceof IDerivedMetric) {
				if (metric instanceof IBaseMetric && !deriveIfPossible) {
					baseMetrics.add((IBaseMetric) metric);
				} else {
					IDerivedMetric derivedMetric = (IDerivedMetric) metric;
					toProcess.addAll(derivedMetric.getDerivedFrom());
					derivedMetrics.add(derivedMetric);
					
					baseMetrics = baseMetrics.stream()
							.filter(composableMetric -> !derivedMetric.getDerivedFrom().stream().anyMatch(other -> other.getClass().equals(composableMetric.getClass())))
							.collect(Collectors.toList());
				}
			} else if (metric instanceof IBaseMetric) {
				baseMetrics.add((IBaseMetric) metric);
			}
		}
		
		IBaseMetric[] baseMetricsArray = new IBaseMetric[baseMetrics.size()];
		IDerivedMetric[] derivedMetricsArray = new IDerivedMetric[derivedMetrics.size()];
	
		Map<Class<?>, IMetric[]> partitionedMetrics = new HashMap<>();
		partitionedMetrics.put(IBaseMetric.class, baseMetrics.toArray(baseMetricsArray));
		partitionedMetrics.put(IDerivedMetric.class, derivedMetrics.toArray(derivedMetricsArray));
		
		return partitionedMetrics;
	}
	
}