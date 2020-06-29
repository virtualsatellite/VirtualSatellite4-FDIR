/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

public class SynthesisQuery {
	private FaultTreeNode root;
	private IMetric objectiveMetric = MeanTimeToFailure.MTTF;
	
	public SynthesisQuery(FaultTreeNode root) {
		this.root = root;
	}
	
	public FaultTreeNode getRoot() {
		return root;
	}
	
	public void setObjectiveMetric(IMetric objectiveMetric) {
		this.objectiveMetric = objectiveMetric;
	}
	
	public IMetric getObjectiveMetric() {
		return objectiveMetric;
	}
}
