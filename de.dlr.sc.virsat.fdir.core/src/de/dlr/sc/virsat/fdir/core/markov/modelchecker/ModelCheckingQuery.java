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

import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;

public class ModelCheckingQuery<S extends MarkovState> {
	
	public static final FailLabelProvider DEFAULT_FAIL_LABEL_PROVIDER = FailLabelProvider.SINGLETON_FAILED;
	
	private MarkovAutomaton<? extends MarkovState> ma;
	private IBaseMetric[] metrics;
	private FailLabelProvider failLabelProvider;
	private Set<S> failStates;
	
	public ModelCheckingQuery(MarkovAutomaton<S> ma, FailLabelProvider failLabelProvider, IBaseMetric... metrics) {
		this.ma = ma;
		this.metrics = metrics;
		this.failLabelProvider = failLabelProvider != null ? failLabelProvider : DEFAULT_FAIL_LABEL_PROVIDER;
		this.failStates = ma.getStatesWithLabels(this.failLabelProvider);
	}
	
	public MarkovAutomaton<? extends MarkovState> getMa() {
		return ma;
	}
	
	public IBaseMetric[] getMetrics() {
		return metrics;
	}
	
	public FailLabelProvider getFailLabelProvider() {
		return failLabelProvider;
	}
	
	public Set<S> getFailStates() {
		return failStates;
	}
}
