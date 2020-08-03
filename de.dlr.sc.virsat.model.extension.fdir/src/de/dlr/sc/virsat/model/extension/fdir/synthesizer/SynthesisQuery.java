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
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class SynthesisQuery {
	private FaultTreeHolder ftHolder;
	private IMetric objectiveMetric = MeanTimeToFailure.MTTF;
	
	public SynthesisQuery(FaultTreeNode root) {
		this.ftHolder = new FaultTreeHolder(root.getFault());
	}
	
	public FaultTreeHolder getFTHolder() {
		return ftHolder;
	}
	
	public void setObjectiveMetric(IMetric objectiveMetric) {
		this.objectiveMetric = objectiveMetric;
	}
	
	public IMetric getObjectiveMetric() {
		return objectiveMetric;
	}
}
