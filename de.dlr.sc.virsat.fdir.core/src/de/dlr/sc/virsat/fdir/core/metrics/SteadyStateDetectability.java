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
 * This class represents the steady state detectability, which is the time bounded
 * detectability with time bound to infinity.
 * @author muel_s8
 *
 */

public class SteadyStateDetectability implements IDerivedMetric {
	public static final SteadyStateDetectability SSD = new SteadyStateDetectability();

	/**
	 * Hidden private constructor
	 */
	private SteadyStateDetectability() {
		
	}
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		Map<FailLabelProvider, Set<IMetric>> mapFailLabelProviderToMetrics = new HashMap<>();
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_FAILED, Collections.singleton(SteadyStateAvailability.SSA));
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_OBSERVED, Collections.singleton(SteadyStateAvailability.SSA));
		return mapFailLabelProviderToMetrics;
	}

	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Derives the steady state detectability from the steady state availability values
	 * @param steadyStateAvailability the steady state availability of the unobserved failure
	 * @param observedSteadyStateAvailability the steady state availability of the observed failure
	 * @return the steady state detectability
	 */
	public double derive(double steadyStateAvailability, double observedSteadyStateAvailability) {
		double observedUnavailability = 1 - observedSteadyStateAvailability;
		double unavailability = 1 - steadyStateAvailability;
		
		if (observedUnavailability == 0 && unavailability == 0) {
			return 1;
		}
		
		double steadyStateDetectability = observedUnavailability / unavailability;
		return steadyStateDetectability;
	}
}
