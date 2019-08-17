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
 * A visitor for all derived metrics
 * @author muel_s8
 *
 */

public interface IDerivedMetricVisitor {	
	/**
	 * Handle the case of a mean time to failure metric
	 * @param mttfMetric the mttf metric
	 */
	void visit(MTTF mttfMetric);
	
	/**
	 * Handle the case of a mean time to steady state availability metric
	 * @param steadyStateAvailabilityMetric the steadyStateAvailability metric
	 */
	void visit(SteadyStateAvailability steadyStateAvailabilityMetric);
	
	void visit(Detectability detectabilityMetrc);
	
	void visit(MeanTimeToDetection meanTimeToDetectionMetric);
	
	void visit(SteadyStateDetectability steadyStateDetectability);
}
