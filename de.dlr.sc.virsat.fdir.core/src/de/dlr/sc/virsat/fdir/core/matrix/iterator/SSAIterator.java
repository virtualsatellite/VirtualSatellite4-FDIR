package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;

/**
 * Performs a lung run average computation. Only applicable for strongly connected end components.
 * The algortihm is based on the paper:
 * 
 * Guck, Dennis, et al. "Modelling, reduction and analysis of Markov automata (extended version)." arXiv preprint arXiv:1305.7050 (2013).
 * 
 * @author muel_s8
 *
 */

public class SSAIterator<S extends MarkovState> extends MatrixIterator {

	private double[] oldValues;
	private IMatrixIterator failCostIterator;
	private IMatrixIterator totalCostIterator;
	
	private Map<S, Collection<List<MarkovTransition<S>>>> mapNondetStateToTransitionGroups;
	private List<S> nondeterministicStates;
	private List<S> probabilisticStates;

	public SSAIterator(IMatrix matrix, double[] baseCostFails, double[] baseCostTotal, MarkovAutomaton<S> ma) {
		super(matrix, new double[baseCostFails.length]);
		
		failCostIterator = new BellmanIterator(matrix, baseCostFails);
		totalCostIterator = new BellmanIterator(matrix, baseCostTotal);
		oldValues = values.clone();
		
		nondeterministicStates = new ArrayList<>();
		probabilisticStates = new ArrayList<>();
		mapNondetStateToTransitionGroups = new HashMap<>();
		
		for (S state : ma.getStates()) {
			if (state.isNondet()) {
				Map<Object, List<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
				mapNondetStateToTransitionGroups.put(state, transitionGroups.values());
				nondeterministicStates.add(state);
			} else if (state.isProbabilisic()) {
				probabilisticStates.add(state);
			}
		}
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
		
		for (S nondeterministicState : nondeterministicStates) {
			double minValue = Double.POSITIVE_INFINITY;
			List<MarkovTransition<S>> bestTransitionGroup = null;
			Collection<List<MarkovTransition<S>>> transitionGroups = mapNondetStateToTransitionGroups.get(nondeterministicState);
			
			for (List<MarkovTransition<S>> transitionGroup : transitionGroups) {
				double expectationValue = getExpectationValue(transitionGroup, getValues());
				if (expectationValue < minValue) {
					minValue = expectationValue;
					bestTransitionGroup = transitionGroup;
				}
			}
			
			int stateIndex = nondeterministicState.getIndex();
			getValues()[stateIndex] = minValue;
			if (bestTransitionGroup != null) {
				failCostIterator.getValues()[stateIndex] = getExpectationValue(bestTransitionGroup, failCostIterator.getValues());
				totalCostIterator.getValues()[stateIndex] = getExpectationValue(bestTransitionGroup, totalCostIterator.getValues());
			}
		}
	}
	
	/**
	 * Computes the expectation value for a given transition group
	 * @param transitionGroup the transition group
	 * @return the expectation value of the transition group
	 */
	private double getExpectationValue(List<MarkovTransition<S>> transitionGroup, double[] values) {
		double expectationValue = 0;
		for (MarkovTransition<S> transition : transitionGroup) {
			double succValue = values[transition.getTo().getIndex()];
			expectationValue += transition.getRate() * succValue;
		}
		return expectationValue;
	}
	
	@Override
	public double[] getOldValues() {
		return oldValues;
	}
}
