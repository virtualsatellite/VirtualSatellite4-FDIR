/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

/**
 * Interface for evaluating fault trees.
 * @author muel_s8
 *
 */

public interface IFaultTreeEvaluator {
	
	/**
	 * Main evaluation function evaluating a fault tree identified by its root.
	 * @param root the root of the fault tree to be evaluated.
	 * @param metrics the evaluation metrics
	 */
	void evaluateFaultTree(FaultTreeNode root, IMetric... metrics);

	/**
	 * Gets the total sum of probability mass in the fail state
	 * @return the total probability mass in the fail state
	 */
	List<Double> getFailRates();
	
	/**
	 * Computes the mean time to failure
	 * @return the expected time to failure
	 */
	double getMeanTimeToFailure();
	
	/**
	 * Computes the minimum cut sets
	 * @return the minimum cut sets
	 */
	Set<Set<BasicEvent>> getMinimumCutSets();
	
	/**
	 * Sets the recovery strategy for the fault tree evaluation
	 * @param recoveryStrategy the recovery strategy
	 */
	void setRecoveryStrategy(RecoveryStrategy recoveryStrategy);
	
	/**
	 * Computes the long run availability
	 * @return the steady state availability
	 */
	double getSteadyStateAvailability();
	
	/**
	 * Gets the availability at different points of time
	 * @return the availability at different instances
	 */
	List<Double> getPointAvailability();
}
