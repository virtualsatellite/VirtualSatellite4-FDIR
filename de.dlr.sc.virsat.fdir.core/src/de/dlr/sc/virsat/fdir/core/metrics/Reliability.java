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

import org.eclipse.core.runtime.SubMonitor;

/**
 * Metric representing time bounded reliability
 * @author sascha
 *
 */

public class Reliability extends AProbabilityCurve implements IBaseMetric {
	
	public static final Reliability UNIT_RELIABILITY = new Reliability(1);
	public static final Reliability INF_RELIABILITY = new Reliability(Double.POSITIVE_INFINITY);
	
	/**
	 * Default constructor
	 * @param time the end time
	 */
	public Reliability(double time) {
		super(time);
	}

	@Override
	public void accept(IBaseMetricVisitor visitor, SubMonitor subMonitor) {
		visitor.visit(this, subMonitor);
	}
}
