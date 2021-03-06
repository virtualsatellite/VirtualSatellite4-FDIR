/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.scheduler;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IQuantitativeMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;

public class ScheduleQuery<S extends MarkovState> {
	private MarkovAutomaton<S> ma;
	private S initialState;
	private IQuantitativeMetric objectiveMetric;
	
	public ScheduleQuery(MarkovAutomaton<S> ma, S initialState) {
		this.ma  = ma;
		this.initialState = initialState;
		this.objectiveMetric = MeanTimeToFailure.MTTF;
	}
	
	public MarkovAutomaton<S> getMa() {
		return ma;
	}
	
	public S getInitialState() {
		return initialState;
	}
	
	public IMetric getObjectiveMetric() {
		return objectiveMetric;
	}
	
	public void setObjectiveMetric(IQuantitativeMetric objectiveMetric) {
		this.objectiveMetric = objectiveMetric;
	}
}
