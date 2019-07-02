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
 * Enables storm-dft to compute reliability values
 * @author sascha
 *
 */

public class Reliability implements IMetric {
	
	public static final Reliability UNIT_RELIABILITY = new Reliability(1);
	
	private double time;
	
	/**
	 * Default constructor
	 * @param time the end time
	 */
	public Reliability(double time) {
		this.time = time;
	}
	
	/**
	 * Gets the time horizon
	 * @return the time horizon
	 */
	public double getTime() {
		return time;
	}

	@Override
	public void accept(IMetricVisitor visitor) {
		visitor.visit(this);
	}
}
