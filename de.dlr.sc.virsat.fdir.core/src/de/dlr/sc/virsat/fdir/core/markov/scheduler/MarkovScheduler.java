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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;

/**
 * Implementation of Value Iteration algorithm for computing a optimal schedule on a given ma
 * @author muel_s8
 *
 * @param <S>
 */

public class MarkovScheduler<S extends MarkovState> implements IMarkovScheduler<S> {

	public static final double EPS = 0.0000000001;
	
	private Map<S, Double> results;
	
	@Override
	public Map<S, Set<MarkovTransition<S>>> computeOptimalScheduler(MarkovAutomaton<S> ma, S initialMa) {
		results = computeValues(ma, initialMa);
		//results.forEach((state, value) -> System.out.println(state.getIndex() + ": " + value));
		
		Queue<S> toProcess = new LinkedList<>();
		toProcess.offer(initialMa);
		Set<S> handledNonDetStates = new HashSet<>();
		
		Map<S, Set<MarkovTransition<S>>> schedule = new HashMap<>();
		while (!toProcess.isEmpty()) {
			S state = toProcess.poll();
			
			if (!state.isMarkovian()) {
				Set<MarkovTransition<S>> bestTransitionGroup = selectOptimalTransitionGroup(ma, state);
				
				if (bestTransitionGroup != null) {
					schedule.put(state, bestTransitionGroup);
					for (MarkovTransition<S> transition : ma.getSuccTransitions(state)) {
						S nextState = transition.getTo();
						if (handledNonDetStates.add(nextState)) {
							toProcess.offer(nextState);
						} 
					}
				}
			} else {
				List<MarkovTransition<S>> succTransitions = ma.getSuccTransitions(state);
				for (MarkovTransition<S> markovianTransition : succTransitions) {
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
	 * Gets the computed values for each state from the last call to the scheduler
	 * @return the computed values
	 */
	public Map<S, Double> getResults() {
		return results;
	}
	
	/**
	 * Computes a utility value for every node
	 * @param ma the markov automaton
	 * @param initialMa the initial stae
	 * @return a mapping from state to its utility value
	 */
	private Map<S, Double> computeValues(MarkovAutomaton<S> ma, S initialMa) {
		IMatrixIterator valueIterator = createValueIterator(ma);
		
		boolean converged = false;		
		while (!converged) {
			valueIterator.iterate();
			
			double change = valueIterator.getChange();
			if (change < EPS || Double.isNaN(change)) {
				converged = true;
			}
		}
		
		double[] values = valueIterator.getValues();
		Map<S, Double> resultMap = createResultMap(ma, values);
		
		return resultMap;
	}
	
	/**
	 * Creates the value iterator for the markov automaton
	 * @param ma the markov automaton
	 * @return the value iterator
	 */
	private IMatrixIterator createValueIterator(MarkovAutomaton<S> ma) {
		MatrixFactory matrixFactory = new MatrixFactory();
		BellmanMatrix bellmanMatrix = matrixFactory.getBellmanMatrix(ma);
		
		double[] values = BellmanMatrix.getInitialMTTFVector(ma);
		IMatrixIterator bellmanIterator = bellmanMatrix.getIterator(values, EPS);		
		MarkovAutomatonValueIterator<S> valueIterator = new MarkovAutomatonValueIterator<S>(bellmanIterator, ma);
		return valueIterator;
	}
	
	/**
	 * Transforms the value vector into the value map
	 * @param ma the markov automaton
	 * @param values the values from the value iteration
	 * @return a mapping from states to their value
	 */
	private Map<S, Double> createResultMap(MarkovAutomaton<S> ma, double[] values) {
		Map<S, Double> resultMap = new LinkedHashMap<S, Double>();
		
		for (S state : ma.getStates()) {			
			double value = values[state.getIndex()];			
			if (Double.isNaN(value)) {
				value = Double.POSITIVE_INFINITY;
			} else if (ma.getFinalStates().contains(state)) {
				// To differentiate between fail states we also compute their MTTF
				List<MarkovTransition<S>> succTransitions = ma.getSuccTransitions(state);
				double exitRate = ma.getExitRateForState(state);
				for (MarkovTransition<S> transition : succTransitions) {
					MarkovState toState = transition.getTo();
					if (!ma.getFinalStates().contains(toState)) {
						double toValue = values[toState.getIndex()];
						value += toValue * transition.getRate() / exitRate;
					}
				}
			}
			resultMap.put(state, value);
		}
		
		return resultMap;
	}
	
	/**
	 * Selects the optimal transition group for a given state
	 * @param ma the markov automaton
	 * @param state the state
	 * @return the optimal transition group of successor states
	 */
	private Set<MarkovTransition<S>> selectOptimalTransitionGroup(MarkovAutomaton<S> ma, S state) {
		Set<MarkovTransition<S>> bestTransitionGroup = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		double bestTransitionProbFail = 1;
		
		Map<Object, Set<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
		
		for (Set<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
			double expectationValue = 0;
			double transitionGroupProbFail = 0;
			
			for (MarkovTransition<S> transition : transitionGroup) {
				double prob = transition.getRate();
				MarkovState toState = transition.getTo();
				boolean isFailSuccessor = ma.getFinalStates().contains(toState);
				if (isFailSuccessor) {
					transitionGroupProbFail += prob;
				} 
				
				double toValue = results.get(toState);
				expectationValue += prob * toValue;
			}
			
			if ((transitionGroupProbFail < bestTransitionProbFail)
					|| (expectationValue >= bestValue && bestTransitionProbFail >= transitionGroupProbFail)) {
				boolean isNewBestTransition = bestTransitionGroup == null || (transitionGroupProbFail < bestTransitionProbFail) || expectationValue > bestValue;
				
				if (!isNewBestTransition) {
					isNewBestTransition = checkMinimality(transitionGroup, bestTransitionGroup);
				}
				
				if (isNewBestTransition) {
					bestValue = expectationValue;
					bestTransitionGroup = transitionGroup;
					bestTransitionProbFail = transitionGroupProbFail;
				}
			}
		}
	
		return bestTransitionGroup;
	}
	
	/**
	 * Checks if a transition group is considered "smaller" than the currently best transition group.
	 * This is based on the label of the first transition in the group, which works if all transitions in the group
	 * have the same label. The label is smaller if there are less entries or if the number of entries is the same
	 * and the string label is smaller.
	 * @param transitionGroup a transition group
	 * @param bestTransitionGroup the currently best transition group
	 * @return true if the transition group is smaller
	 */
	private boolean checkMinimality(Set<MarkovTransition<S>> transitionGroup, Set<MarkovTransition<S>> bestTransitionGroup) {
		// Prefer to keep the fewer actions over any other actions in case of the same value
		Object event1 = transitionGroup.iterator().next().getEvent();
		Object event2 = bestTransitionGroup.iterator().next().getEvent();
		
		if (event1 instanceof Collection && event2 instanceof Collection) {
			Collection<?> eventCollection1 = (Collection<?>) event1;
			Collection<?> eventCollection2 = (Collection<?>) event2;
			
			// To ensure that there is no nondeterminism in the scheduling
			// the final deciding factor is the string representation of the events
			if (eventCollection2.size() == eventCollection1.size()) {
				return eventCollection1.toString().compareTo(eventCollection2.toString()) < 0;
			} else {
				return eventCollection1.size() < eventCollection2.size();
			}
		}
		
		return false;
	}
}
