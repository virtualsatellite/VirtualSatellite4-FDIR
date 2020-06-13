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
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;

public class SynthesisQuery {
	private Fault fault;
	private IMetric objectiveMetric = MeanTimeToFailure.MTTF;
	
	public SynthesisQuery(Fault fault) {
		this.fault = fault;
	}
	
	public Fault getFault() {
		return fault;
	}
	
	public void setObjectiveMetric(IMetric objectiveMetric) {
		this.objectiveMetric = objectiveMetric;
	}
	
	public IMetric getObjectiveMetric() {
		return objectiveMetric;
	}
}
