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

public class MeanTimeToDetection implements IDerivedMetric, IQuantitativeMetric {

	public static final MeanTimeToDetection MTTD = new MeanTimeToDetection();

	/**
	 * Hidden private constructor
	 */
	
	private MeanTimeToDetection() {
		
	}
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		Map<FailLabelProvider, Set<IMetric>> mapFailLabelProviderToMetrics = new HashMap<>();
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_FAILED, Collections.singleton(MeanTimeToFailure.MTTF));
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_OBSERVED, Collections.singleton(MeanTimeToFailure.MTTF));
		return mapFailLabelProviderToMetrics;
	}
	
	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Derives the mean time to failure from the failure times
	 * @param unobservedMTTF the mean time to failure
	 * @param observedMTTF the mean time to an observed failure
	 * @return the derived mean time to detection
	 */
	public double derive(double unobservedMTTF, double observedMTTF) {
		// If no failure can ever be observed, then the MTTD is 0
		if (Double.isInfinite(unobservedMTTF)) {
			return 0;
		}
		double meanTimeToDetection = Double.isInfinite(observedMTTF) ? Double.POSITIVE_INFINITY : observedMTTF - unobservedMTTF;
		return meanTimeToDetection;
	}
}
