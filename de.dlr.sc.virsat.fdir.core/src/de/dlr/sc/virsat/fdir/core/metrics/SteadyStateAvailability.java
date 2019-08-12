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
import java.util.List;

/**
 * Metric representing the covergent steady state probability
 * @author yoge_re
 *
 */
public class SteadyStateAvailability implements IQuantitativeMetric {
	public static final SteadyStateAvailability STEADY_STATE_AVAILABILITY = new SteadyStateAvailability();

	/**
	 * 
	 * hidden constructor
	 */
	private SteadyStateAvailability() {
	}

	@Override
	public void accept(IMetricVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<IMetric> getDerivedFrom() {
		return Collections.singletonList(PointAvailability.INF_POINTAVAILABILITY);
	}
}
