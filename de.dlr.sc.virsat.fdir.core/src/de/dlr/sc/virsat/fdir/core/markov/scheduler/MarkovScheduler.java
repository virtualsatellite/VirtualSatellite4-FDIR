/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.markov.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.matrix.BellmanMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.MatrixFactory;
import de.dlr.sc.virsat.fdir.core.matrix.MatrixIterator;

/**
 * Implementation of Value Iteration algorithm for computing a optimal schedule on a given ma
 * @author muel_s8
 *
 * @param <S>
 */

public class MarkovScheduler<S extends MarkovState> implements IMarkovScheduler<S> {

	@Override
	public Map<S, Set<MarkovTransition<S>>> computeOptimalScheduler(MarkovAutomaton<S> ma, S initialMa) {
		Map<S, Double> results = computeValues(ma, initialMa);
		for (S failState : ma.getFinalStates()) {
			results.put(failState, Double.NEGATIVE_INFINITY);
		}
		
		Queue<S> toProcess = new LinkedList<>();
		toProcess.offer(initialMa);
		Set<S> handledNonDetStates = new HashSet<>();
		
		Map<S, Set<MarkovTransition<S>>> schedule = new HashMap<>();
		while (!toProcess.isEmpty()) {
			S state = toProcess.poll();
			
			for (MarkovTransition<S> markovianTransition : ma.getSuccTransitions(state)) {
				if (!state.isMarkovian()) {
					Set<MarkovTransition<S>> bestTransitionGroup = selectOptimalTransitionGroup(ma, results, state);
					
					if (bestTransitionGroup != null) {
						schedule.put(state, bestTransitionGroup);
						for (MarkovTransition<S> transition : bestTransitionGroup) {
							S nextState = transition.getTo();
							if (handledNonDetStates.add(nextState)) {
								toProcess.offer(nextState);
							} 
						}
					}
				} else {
					S nextState = markovianTransition.getTo();
					if (handledNonDetStates.add(nextState)) {
						toProcess.offer(nextState);
					} 
				}
			}
		}	
		return schedule;
	}
	
	/**
	 * Computes a utility value for every node
	 * @param ma the markov automaton
	 * @param initialMa the initial stae
	 * @return a mapping from state to its utility value
	 */
	private Map<S, Double> computeValues(MarkovAutomaton<S> ma, S initialMa) {
		boolean converged = false;
		final double EPS = 0.0000000001;
		
		Map<S, Double> resultMap = new HashMap<S, Double>();
		List<S> nondeterministicStates = new ArrayList<S>();
		
		MatrixFactory matrixFactory = new MatrixFactory();
		BellmanMatrix bellmanMatrix = matrixFactory.getBellmanMatrix(ma);
		
		double[] probabilityDistribution = BellmanMatrix.getInitialMTTFVector(ma);		
		MatrixIterator mxIterator = bellmanMatrix.getIterator(probabilityDistribution, EPS);		
		
		for (S state : ma.getStates()) {
			if (!state.isMarkovian()) {
				nondeterministicStates.add(state);
			}
		}
		
		while (!converged) {
			mxIterator.iterate();
			for (S nondeterministicState : nondeterministicStates) {
				double maxValue = Double.NEGATIVE_INFINITY;
				
				Map<Object, Set<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(nondeterministicState);
				for (Set<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
					double expectationValue = 0;
					for (MarkovTransition<S> transition : transitionGroup) {
						double succValue = probabilityDistribution[transition.getTo().getIndex()];
						expectationValue += transition.getRate() * succValue;
					}					
					maxValue = Math.max(expectationValue, maxValue);
				}
				
				probabilityDistribution[nondeterministicState.getIndex()] = maxValue;
			}
			
			double change = mxIterator.getChange();
			if (change < EPS || Double.isNaN(change)) {
				converged = true;
			}
		}
		
		probabilityDistribution = mxIterator.getProbabilityDistribution();
		for (S state : ma.getStates()) {			
			double value = probabilityDistribution[state.getIndex()];			
			if (Double.isNaN(value) || Double.isInfinite(value)) {
				value = 0.0;
			}			
			resultMap.put(state, value);
		}
		return resultMap;
	}
	
	/**
	 * Selects the optimal transition group for a given state
	 * @param ma the markov automaton
	 * @param results the utility valuation
	 * @param state the state
	 * @return the optimal transition group of successor states
	 */
	private Set<MarkovTransition<S>> selectOptimalTransitionGroup(MarkovAutomaton<S> ma, Map<S, Double> results, S state) {
		Set<MarkovTransition<S>> bestTransitionGroup = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		
		Map<Object, Set<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
		
		for (Set<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
			double expectationValue = 0;
			for (MarkovTransition<S> transition : transitionGroup) {
				expectationValue += transition.getRate() * results.get(transition.getTo());
			}
			if (expectationValue >= bestValue) {
				bestValue = expectationValue;
				bestTransitionGroup = transitionGroup;
			}
		}		
		return bestTransitionGroup;
	}
}
