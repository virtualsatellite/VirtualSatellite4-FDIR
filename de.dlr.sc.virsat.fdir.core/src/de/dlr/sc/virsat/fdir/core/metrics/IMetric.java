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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

/**
 * Interface for metrics
 * @author sascha
 *
 */

public interface IMetric {

	/**
	 * Splits the given set of metrics into a set of basic and derived metrics.
	 * Also, if necessary, adds new metrics that are required to perform the composition.
	 * @param deriveIfPossible if a metric is both a base metric and a derived metric, then this flag
	 * will prioritize derivation. If set to false, the metric will be considered a base metric.
	 * @param metrics the original metrics
	 * @return a map with the partitioned metrics
	 */
	static Map<FailLabelProvider, IMetric[]> partitionMetrics(boolean deriveIfPossible, IMetric... metrics) {
		Map<FailLabelProvider, List<IMetric>> partitioningWithList = new HashMap<>();
		partitioningWithList.put(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER, new ArrayList<>());
		
		Queue<IMetric> toProcess = new LinkedList<>(Arrays.asList(metrics));
		
		while (!toProcess.isEmpty()) {
			IMetric metric = toProcess.poll();
			if (metric instanceof IBaseMetric && !(metric instanceof IDerivedMetric && deriveIfPossible)) {
				partitioningWithList.computeIfAbsent(new FailLabelProvider(FailLabel.FAILED), v -> new ArrayList<>()).add(metric);
			} else if (metric instanceof IDerivedMetric) {
				IDerivedMetric derivedMetric = (IDerivedMetric) metric;
				partitioningWithList.get(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER).add(metric); 
				
				for (Entry<FailLabelProvider, Set<IMetric>> entry : derivedMetric.getDerivedFrom().entrySet()) {
					List<IMetric> existingMetrics = partitioningWithList.getOrDefault(entry.getKey(), Collections.emptyList());
					for (IMetric existingMetric : entry.getValue()) {
						existingMetrics.removeIf(existingMetricOther -> existingMetricOther.getClass().equals(existingMetric.getClass()));
					}
					
					for (IMetric derivingMetric : entry.getValue()) {
						if (derivingMetric instanceof IBaseMetric) {
							partitioningWithList.computeIfAbsent(entry.getKey(), v -> new ArrayList<>()).add(derivingMetric);
						} else {
							toProcess.add(derivingMetric);
						}
					}
				}
			} 
		}
		
		Map<FailLabelProvider, IMetric[]> partitioningWithArray = new HashMap<>();
		for (Entry<FailLabelProvider, List<IMetric>> entry : partitioningWithList.entrySet()) {
			IMetric[] arrayType = entry.getKey().equals(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER) 
					? new IDerivedMetric[0] : new IBaseMetric[0];
			partitioningWithArray.put(entry.getKey(), entry.getValue().toArray(arrayType));
		}
		return partitioningWithArray;
	}
	
}