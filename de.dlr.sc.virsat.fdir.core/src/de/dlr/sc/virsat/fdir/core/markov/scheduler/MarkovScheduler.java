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
import java.util.Collection;
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
		
		Queue<S> toProcess = new LinkedList<>();
		toProcess.offer(initialMa);
		Set<S> handledNonDetStates = new HashSet<>();
		
		Map<S, Set<MarkovTransition<S>>> schedule = new HashMap<>();
		while (!toProcess.isEmpty()) {
			S state = toProcess.poll();
			
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
			if (Double.isNaN(value)) {
				value = Double.POSITIVE_INFINITY;
			} else if (ma.getFinalStates().contains(state)) {
				// To differentiate between fail states we also compute their MTTF
				List<MarkovTransition<S>> succTransitions = ma.getSuccTransitions(state);
				double exitRate = ma.getExitRateForState(state);
				for (MarkovTransition<S> transition : succTransitions) {
					MarkovState toState = transition.getTo();
					if (!ma.getFinalStates().contains(toState)) {
						double toValue = probabilityDistribution[toState.getIndex()];
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
	 * @param results the utility valuation
	 * @param state the state
	 * @return the optimal transition group of successor states
	 */
	private Set<MarkovTransition<S>> selectOptimalTransitionGroup(MarkovAutomaton<S> ma, Map<S, Double> results, S state) {
		Set<MarkovTransition<S>> bestTransitionGroup = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		double bestTransitionProbFail = 1;
		
		Map<Object, Set<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
		
		for (Set<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
			double expectationValue = 0;
			double transitionGroupProbFail = 0;
			
			boolean hasOnlyFailSuccessors = hasOnlyFinalSuccessors(ma, transitionGroup);
			
			for (MarkovTransition<S> transition : transitionGroup) {
				double prob = transition.getRate();
				MarkovState toState = transition.getTo();
				boolean isFailSuccessor = ma.getFinalStates().contains(toState);
				if (isFailSuccessor) {
					transitionGroupProbFail += prob;
				} 
				
				if (!isFailSuccessor || hasOnlyFailSuccessors) {
					double toValue = results.get(toState);
					expectationValue += prob * toValue;
				}
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
	 * Checks if every transition in a transition group is going to a final state
	 * @param ma the markov automaton
	 * @param transitionGroup a group of transitions
	 * @return true iff all transitions in the group lead to a final state
	 */
	private boolean hasOnlyFinalSuccessors(MarkovAutomaton<S> ma, Set<MarkovTransition<S>> transitionGroup) {
		for (MarkovTransition<S> transition : transitionGroup) {
			if (!ma.getFinalStates().contains(transition.getTo())) {
				return false;
			}
		}
		
		return true;
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
			
			boolean isSmaller = eventCollection1.size() < eventCollection2.size();
			
			// To ensure that there is no nondeterminism in the scheduling
			// the final deciding factor is the string representation of the events
			if (eventCollection2.size() == eventCollection1.size()) {
				isSmaller = eventCollection1.toString().compareTo(eventCollection2.toString()) < 0;
			}
			
			return isSmaller;
		}
		
		return false;
	}
}
