/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;

public class ModelCheckingQuery<S extends MarkovState> {
	private MarkovAutomaton<? extends MarkovState> ma;
	private IBaseMetric[] metrics;
	private List<S> states;
	
	public ModelCheckingQuery(MarkovAutomaton<S> ma, IBaseMetric... metrics) {
		this.ma = ma;
		this.metrics = metrics;
		this.states = ma.getStates();
	}
	
	public MarkovAutomaton<? extends MarkovState> getMa() {
		return ma;
	}
	
	public IBaseMetric[] getMetrics() {
		return metrics;
	}
	
	public List<S> getStates() {
		return states;
	}
	
	public void setStates(List<S> states) {
		this.states = states;
	}
}