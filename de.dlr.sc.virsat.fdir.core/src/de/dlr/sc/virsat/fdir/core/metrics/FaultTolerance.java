/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FaultTolerance implements IQuantitativeMetric, IDerivedMetric {

	public static final FaultTolerance FAULT_TOLERANCE = new FaultTolerance();
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		Map<FailLabelProvider, Set<IMetric>> mapFailLabelProviderToMetrics = new HashMap<>();
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_FAILED, Collections.singleton(MinimumCutSet.MINCUTSET));
		return mapFailLabelProviderToMetrics;
	}

	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Derives the fault tolerance from the minimum cut sets
	 * @param minCutSets the minimum cut sets
	 * @return the derived fault tolerance
	 */
	public long derive(Set<Set<Object>> minCutSets) {
		return minCutSets.stream().mapToLong(Set::size).min().orElse(1) - 1;
	}
	
}
