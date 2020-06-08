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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.LinearProgramIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.MarkovAutomatonValueIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.SSAIterator;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

/**
 * Metric representing the covergent steady state probability
 * @author yoge_re
 *
 */
public class SteadyStateAvailability implements IQuantitativeMetric, IDerivedMetric, IBaseMetric {
	public static final SteadyStateAvailability STEADY_STATE_AVAILABILITY = new SteadyStateAvailability();

	/**
	 * 
	 * hidden constructor
	 */
	private SteadyStateAvailability() {
	}

	@Override
	public void accept(IBaseMetricVisitor visitor, SubMonitor subMonitor) {
		visitor.visit(this);
	}
	
	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		return Collections.singletonMap(new FailLabelProvider(FailLabel.FAILED), Collections.singleton(Availability.INF_AVAILABILITY));
	}
	
	/**
	 * Creates an iterator for computing the steady state availability on the given subset of states in the markov automaton
	 * @param matrix the matrix representation of the ma
	 * @param ma the markov automaton
	 * @param states a subset of states of the ma on which the ssa will be computed
	 * @return an iterator that converges towards the ssa on the given state subset
	 */
	public IMatrixIterator iterator(IMatrix matrix, MarkovAutomaton<? extends MarkovState> ma, List<MarkovState> states) {
		double[] baseFailCosts = ma.getFailSoujournTimes(states);
		double[] baseTotalCosts = ma.getSoujournTimes(states);
		IMatrixIterator ssaIterator = new MarkovAutomatonValueIterator<>(
				new SSAIterator<>(matrix, baseFailCosts, baseTotalCosts), ma, states, false
		);
		
		return ssaIterator;
	}
	
	/**
	 * Creates an iterator for computing the steady state availability of the markov automaton being given the SSAs for the end SCCs.
	 * @param matrix the matrix representation of the ma with the end SCCs being terminal nodes
	 * @param ma the markov automaton
	 * @param ssas the steady state availability of the strongly connected end components
	 * @return an iterator that converges towards the overall SSA of the ma
	 */
	public IMatrixIterator iterator(IMatrix matrix, MarkovAutomaton<? extends MarkovState> ma, double[] ssas) {
		return new LinearProgramIterator(matrix, ssas);
	}
}
