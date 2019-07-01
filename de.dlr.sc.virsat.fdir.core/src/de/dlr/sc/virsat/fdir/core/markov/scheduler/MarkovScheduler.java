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
	
	private Map<S, Double> computeValues(MarkovAutomaton<S> ma, S initialMa) {
		boolean converged = false;
		List<Map<S, Double>> values = new ArrayList<>();
		int iteration = 0;
		final double EPS = 0.0000000001;
		
		while (!converged) {			
			Map<S, Double> oldValues = values.isEmpty() ? null : values.get(iteration - 1);
			Map<S, Double> currentValues = new HashMap<>();
			values.add(currentValues);
			
			if (oldValues != null) {
				converged = true;
			}
			
			for (S state : ma.getStates()) {
				double value = 0;
				double maxValue = Double.NEGATIVE_INFINITY;
				
				if (state.isMarkovian()) {
					List<MarkovTransition<S>> transitions = ma.getSuccTransitions(state);
					for (MarkovTransition<S> transition : transitions) {
						double reward = ma.getFinalStates().contains(transition.getTo()) ? -1 : 0;
						double prevValue = oldValues != null ? oldValues.get(transition.getTo()) : 0;
						double newValue = reward + prevValue;
						value += transition.getRate() * newValue;
					} 
				} else {
					Map<Object, Set<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
					for (Set<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
						double expectationValue = 0;
						for (MarkovTransition<S> transition : transitionGroup) {
							double reward = ma.getFinalStates().contains(transition.getTo()) ? -1 : 0;
							double prevValue = oldValues != null ? oldValues.get(transition.getTo()) : 0;
							double newValue = reward + prevValue;
							expectationValue += transition.getRate() * newValue;
						} 
						
						maxValue = Math.max(expectationValue, maxValue);
					}
					
					
					if (maxValue != Double.NEGATIVE_INFINITY) {
						value += maxValue;
					}
				}
				
				currentValues.put(state, value);
				if (oldValues != null) {
					double change = value - oldValues.get(state);
					if (Math.abs(change) >= EPS) {
						converged = false;
					} 
				}
			}
	
			if (!converged) {
				++iteration;
			}
		}
		
		return values.get(iteration);
	}
	
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
