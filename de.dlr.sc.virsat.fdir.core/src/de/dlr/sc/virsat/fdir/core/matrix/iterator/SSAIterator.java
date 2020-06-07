/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;

/**
 * Performs a long run average computation of the Steady State unAvailability. 
 * Only applicable for strongly connected end components.
 * The algortihm is based on the paper:
 * 
 * Guck, Dennis, et al. "Modelling, reduction and analysis of Markov automata (extended version)." arXiv preprint arXiv:1305.7050 (2013).
 * 
 * @author muel_s8
 * 
 * @param <S> State space type
 *
 */

public class SSAIterator<S extends MarkovState> extends AMatrixIterator implements IDelegateIterator<S> {

	private double[] oldValues;
	private IMatrixIterator failCostIterator;
	private IMatrixIterator totalCostIterator;

	public SSAIterator(IMatrix matrix, double[] baseCostFails, double[] baseCostTotal) {
		super(matrix, new double[baseCostFails.length]);
		
		failCostIterator = new BellmanIterator(matrix, baseCostFails);
		totalCostIterator = new BellmanIterator(matrix, baseCostTotal);
		oldValues = values.clone();
	}

	@Override
	public void iterate() {
		for (int i = 0; i < values.length; ++i) {
			oldValues[i] = values[i];
		}
		
		failCostIterator.iterate();
		totalCostIterator.iterate();
		
		for (int i = 0; i < values.length; ++i) {
			values[i] = failCostIterator.getValues()[i] / totalCostIterator.getValues()[i];
		}
	}
	
	@Override
	public void delegateProbabilisticUpdate(Map<MarkovState, Integer> mapStateToIndex, int stateIndex, double value, List<MarkovTransition<S>> transitions) {
		failCostIterator.getValues()[stateIndex] = MarkovTransition.getExpectationValue(transitions, mapStateToIndex, failCostIterator.getOldValues());
		totalCostIterator.getValues()[stateIndex] = MarkovTransition.getExpectationValue(transitions, mapStateToIndex, totalCostIterator.getOldValues());
	}
	
	@Override
	public double getChangeSquared() {
		boolean allValuesEqual = true;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] != values[0]) {
				allValuesEqual = false;
			}
		}
		
		if (allValuesEqual) {
			return 0;
		}
		
		return super.getChangeSquared();
	}
	
	@Override
	public double[] getOldValues() {
		return oldValues;
	}
}
