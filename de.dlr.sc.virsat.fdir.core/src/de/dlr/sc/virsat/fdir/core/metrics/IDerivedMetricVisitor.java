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
	void visit(MeanTimeToFailure mttfMetric);
	
	/**
	 * Handle the case of a detectability metric
	 * @param detectabilityMetric the detectability metric
	 */
	void visit(Detectability detectabilityMetric);
	
	/**
	 * Handle the case of a mean time to detection metric
	 * @param meanTimeToDetectionMetric the mean time to detection metric
	 */
	void visit(MeanTimeToDetection meanTimeToDetectionMetric);
	
	/**
	 * Handle the case of a mean time to detection metric
	 * @param meanTimeToDetectionMetric the mean time to detection metric
	 */
	void visit(MeanTimeToObservedFailure meanTimeToObservedFailureMetric);
	
	/**
	 * Handle the case of a steady state detectability metric
	 * @param steadyStateDetectability the mean time to detection metric
	 */
	void visit(SteadyStateDetectability steadyStateDetectability);

	/**
	 * Handle the case of a fault tolerance metric
	 * @param faultTolerance the fault tolerance metric
	 */
	void visit(FaultTolerance faultTolerance);
}
