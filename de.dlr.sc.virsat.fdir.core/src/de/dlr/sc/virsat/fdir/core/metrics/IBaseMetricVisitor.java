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
 * Interface for visiting a metric
 * @author muel_s8
 *
 */

public interface IBaseMetricVisitor {
	
	/**
	 * Handle the case of a reliability metric
	 * @param reliabilityMetric the reliabilitymetric
	 */
	void visit(Reliability reliabilityMetric, SubMonitor subMonitor);
	
	/**
	 * Handle the case of a mean time to failure metric
	 * @param mttfMetric the mttf metric
	 */
	void visit(MTTF mttfMetric);
	
	/**
	 * Handle the case of a point availability metric
	 * @param pointAvailabilityMetric the point availabilityMetric
	 */
	void visit(Availability pointAvailabilityMetric, SubMonitor subMonitor);
	
	/**
	 * Handle the case of a steadyStateAvailabilityMetric metric
	 * @param steadyStateAvailabilityMetric the steadyStateAvailabilityMetric
	 */
	void visit(SteadyStateAvailability steadyStateAvailabilityMetric);

	/**
	 * Handle the case of a minimum cut set metric
	 * @param minimumCutSet the minimum cut set metric
	 */
	void visit(MinimumCutSet minimumCutSet);
}
