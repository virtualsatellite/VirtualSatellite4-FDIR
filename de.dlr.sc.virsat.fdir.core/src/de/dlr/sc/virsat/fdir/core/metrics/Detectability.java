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

import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

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
		mapFailLabelProviderToMetrics.put(new FailLabelProvider(FailLabel.FAILED), Collections.singleton(new Availability(time)));
		mapFailLabelProviderToMetrics.put(new FailLabelProvider(FailLabel.OBSERVED), Collections.singleton(new Availability(time)));
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
	 * @param detectabiityCurve the derived detectability curve
	 */
	public void derive(List<Double> unobservedAvailabilityCurve, List<Double> observedAvailabilityCurve, List<Double> detectabiityCurve) {
		for (int i = 0; i < unobservedAvailabilityCurve.size(); ++i) {
			Double unobservedAvailability = unobservedAvailabilityCurve.get(i);
			Double observedAvailability = observedAvailabilityCurve.get(i);
			Double detectability = (1 - observedAvailability) / (1 - unobservedAvailability);
			detectabiityCurve.add(detectability);
		}
	}
}
