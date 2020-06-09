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
import de.dlr.sc.virsat.fdir.core.matrix.iterator.BellmanIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.MarkovAutomatonValueIterator;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

/**
 * Metric representing the Mean Time To Failure
 * @author sascha
 *
 */
public class MeanTimeToFailure implements IQuantitativeMetric, IBaseMetric, IDerivedMetric {
	public static final MeanTimeToFailure MTTF = new MeanTimeToFailure();
	
	/**
	 * Hidden constructor
	 */
	private MeanTimeToFailure() {
		
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
		return Collections.singletonMap(new FailLabelProvider(FailLabel.FAILED), Collections.singleton(Reliability.INF_RELIABILITY));
	}
	
	/**
	 * Creates an iterator that converges towards the mean time to failure of the markov automaton.
	 * @param matrix the matrix representation of the ma
	 * @param ma the markov automaton
	 * @param states a subset of states in the amrkov automaton on which the iterator will operate
	 * @return an iterator that converges towards the MTTF
	 */
	public IMatrixIterator iterator(IMatrix matrix, MarkovAutomaton<? extends MarkovState> ma, List<? extends MarkovState> states) {
		double[] nonFailSoujournTimes = ma.getNonFailSoujornTimes(states); 
		return new MarkovAutomatonValueIterator<>(new BellmanIterator(matrix, nonFailSoujournTimes), ma);
	}
}
