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

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
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
	 * @param failLabelProvider the fail criteria
	 * @param metrics the evaluation metrics
	 * @return the result of the evaluation
	 */
	ModelCheckingResult evaluateFaultTree(FaultTreeNode root, FailLabelProvider failLabelProvider, IMetric... metrics);

	/**
	 * As {@link IFaultTreeEvaluator#evaluateFaultTree(FaultTreeNode, FailLabelProvider, IMetric...)} but with a default
	 * failLabelProvider.
	 * @param root the root of the fault tree to be evaluated
	 * @param metrics the evaluation metrics
	 * @return the result of the evaluation
	 */
	ModelCheckingResult evaluateFaultTree(FaultTreeNode root, IMetric... metrics);
	
	/**
	 * Sets the recovery strategy for the fault tree evaluation
	 * @param recoveryStrategy the recovery strategy
	 */
	void setRecoveryStrategy(RecoveryStrategy recoveryStrategy);
	
	/**
	 * Gets the internal statistics of the last call to the evaluation method
	 * @return the statistics of the last call of the evaluation method
	 */
	Object getStatistics();
}
