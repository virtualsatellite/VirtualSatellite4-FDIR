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

/**
 * Absract base class for fault tree evaluators
 * @author muel_s8
 *
 */

public abstract class AFaultTreeEvaluator implements IFaultTreeEvaluator {

	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		return evaluateFaultTree(root, null, metrics);
	}

	@Override
	public Object getStatistics() {
		return new Object();
	}
}
