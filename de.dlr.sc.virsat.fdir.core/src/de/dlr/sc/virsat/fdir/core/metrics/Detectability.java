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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This metric represents time bounded detectability, which is the probability
 * that a fault is detected given the that it has occurred.
 * @author muel_s8
 *
 */

public class Detectability extends AProbabilityCurve implements IDerivedMetric {

	public static final Detectability UNIT_DETECTABILITY = new Detectability(1);

	/**
	 * Standard constructor
	 * @param time the time bound
	 */
	public Detectability(double time) {
		super(time);
	}
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		Map<FailLabelProvider, Set<IMetric>> mapFailLabelProviderToMetrics = new HashMap<>();
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_FAILED, Collections.singleton(new Availability(time)));
		mapFailLabelProviderToMetrics.put(FailLabelProvider.SINGLETON_OBSERVED, Collections.singleton(new Availability(time)));
		return mapFailLabelProviderToMetrics;
	}

	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Derives the detectability curve from the availability curves
	 * @param unobservedAvailabilityCurve the availability curve for the failure
	 * @param observedAvailabilityCurve the availability curve for the observed failure
	 * @param detectabilityCurve the derived detectability curve
	 */
	public void derive(List<Double> unobservedAvailabilityCurve, List<Double> observedAvailabilityCurve, List<Double> detectabilityCurve) {
		for (int i = 0; i < unobservedAvailabilityCurve.size(); ++i) {
			Double unobservedUnavailability = 1 - unobservedAvailabilityCurve.get(i);
			Double observedUnavailability = 1 - observedAvailabilityCurve.get(i);
			if (observedUnavailability == 0) {
				// If the probability of observation is 0, while the probability of failure is 0 as well,
				// then we have full detectability (1). If the probability of observation is 0, while the
				// probabiltiy of failure is not 0, then we have no detectability at all (0)
				detectabilityCurve.add(unobservedUnavailability == 0 ? 1d : 0d);
			} else {
				Double detectability = observedUnavailability / unobservedUnavailability;
				detectabilityCurve.add(detectability);
			}
		}
	}
}
