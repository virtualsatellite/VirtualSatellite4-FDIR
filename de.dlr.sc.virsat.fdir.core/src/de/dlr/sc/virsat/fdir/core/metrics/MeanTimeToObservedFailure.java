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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the mean time to detection metric, which
 * is the mean time interval from the occurence of a fault to its detection
 * @author muel_s8
 *
 */

public class MeanTimeToObservedFailure implements IDerivedMetric, IQuantitativeMetric {

	public static final MeanTimeToObservedFailure MTTOF = new MeanTimeToObservedFailure();

	/**
	 * Hidden private constructor
	 */
	
	private MeanTimeToObservedFailure() {
		
	}
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		Map<FailLabelProvider, Set<IMetric>> mapFailLabelProviderToMetrics = new HashMap<>();
		mapFailLabelProviderToMetrics.put(FailLabelProvider.FAILED_OBSERVED, Collections.singleton(MeanTimeToFailure.MTTF));
		return mapFailLabelProviderToMetrics;
	}
	
	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}
}
