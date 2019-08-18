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

/**
 * Metric representing time bounded availability
 * @author yoge_re
 *
 */
public class Availability implements IQuantitativeMetric, IBaseMetric {
	public static final Availability UNIT_AVAILABILITY = new Availability(1);
	public static final Availability INF_AVAILABILITY = new Availability(Double.POSITIVE_INFINITY);

	private double time;

	/**
	 * 
	 * @param time
	 *            the end time
	 */
	public Availability(double time) {
		this.time = time;
	}

	/**
	 * 
	 * @return the time horizon
	 */
	public double getTime() {
		return time;
	}

	@Override
	public void accept(IBaseMetricVisitor visitor) {
		visitor.visit(this);
	}
}
