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
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.MarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingQuery;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IQualitativeMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MetricsStateDeriver;

/**
 * Implementation of Value Iteration algorithm for computing a optimal schedule on a given ma
 * @author muel_s8
 *
 * @param <S>
 */

public class MarkovScheduler<S extends MarkovState> implements IMarkovScheduler<S> {
	
	public static final double EPS = 0.0000000001;
	
	private MarkovModelChecker modelChecker = new MarkovModelChecker(0, EPS);
	private Map<MarkovState, Double> results;
	private MetricsStateDeriver metricStateDeriver = new MetricsStateDeriver();
	
	@Override
	public Map<S, List<MarkovTransition<S>>> computeOptimalScheduler(ScheduleQuery<S> scheduleQuery, SubMonitor subMonitor) {
		results = computeValues(scheduleQuery.getMa(), scheduleQuery.getInitialState(), scheduleQuery.getObjectiveMetric(), subMonitor);
		Queue<S> toProcess = new LinkedList<>();
		toProcess.offer(scheduleQuery.getInitialState());
		Set<S> handledNonDetStates = new HashSet<>();
		
		Map<S, List<MarkovTransition<S>>> schedule = new HashMap<>();
		while (!toProcess.isEmpty()) {
			S state = toProcess.poll();
			
			List<MarkovTransition<S>> succTransitions = state.isNondet() 
					? selectOptimalTransitionGroup(scheduleQuery.getMa(), state) : scheduleQuery.getMa().getSuccTransitions(state);
			
			if (succTransitions != null) {
				schedule.put(state, succTransitions);
				for (MarkovTransition<S> transition : succTransitions) {
					S nextState = transition.getTo();
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
	public Map<MarkovState, Double> getResults() {
		return results;
	}
	
	/**
	 * Computes a utility value for every node
	 * @param ma the markov automaton
	 * @param initialMa the initial stae
	 * @return a mapping from state to its utility value
	 */
	private Map<MarkovState, Double> computeValues(MarkovAutomaton<S> ma, S initialMa, IMetric metric, SubMonitor subMonitor) {
		Map<FailLabelProvider, IMetric[]> partitioning = IMetric.partitionMetrics(false, metric);
		Map<FailLabelProvider, Map<IMetric, Map<MarkovState, ?>>> results = new HashMap<>();
		
		for (Entry<FailLabelProvider, IMetric[]> entry : partitioning.entrySet()) {
			FailLabelProvider failLabelProvider = entry.getKey();
			if (!failLabelProvider.equals(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER)) {
				Map<IMetric, Map<MarkovState, ?>> mapMetricToResults = new HashMap<>();
				results.put(failLabelProvider, mapMetricToResults);
				
				IBaseMetric[] metrics = (IBaseMetric[]) entry.getValue();
				for (IBaseMetric baseMetric : metrics) {
					ModelCheckingQuery<S> modelCheckingQuery = new ModelCheckingQuery<>(ma, failLabelProvider, baseMetric);
					modelChecker.checkModel(modelCheckingQuery, subMonitor);
					
					if (baseMetric instanceof IQualitativeMetric) {
						Map<MarkovState, Object> values = modelChecker.getQualitativeResults();
						mapMetricToResults.put(baseMetric, values);
					} else {
						double[] values = modelChecker.getQuantitativeResults();
						Map<MarkovState, Object> resultMap = createResultMap(ma, values);
						mapMetricToResults.put(baseMetric, resultMap);
					}
				}
			}
		}
		
		if (metric instanceof IDerivedMetric && !(metric instanceof IBaseMetric)) {
			metricStateDeriver.derive(results, (IDerivedMetric) metric);
		}
		
		@SuppressWarnings("unchecked")
		Map<MarkovState, Double> numericResults = (Map<MarkovState, Double>) results.get(FailLabelProvider.SINGLETON_FAILED).get(metric);
		
		return numericResults;
	}
	
	/**
	 * Transforms the value vector into the value map
	 * @param ma the markov automaton
	 * @param states 
	 * @param values the values from the value iteration
	 * @return a mapping from states to their value
	 */
	private Map<MarkovState, Object> createResultMap(MarkovAutomaton<S> ma, double[] values) {
		Map<MarkovState, Object> resultMap = new LinkedHashMap<MarkovState, Object>();
		
		for (int i = 0; i < ma.getStates().size(); ++i) {	
			MarkovState state = ma.getStates().get(i);
			double value = values[i];		
			if (Double.isNaN(value)) {
				value = Double.POSITIVE_INFINITY;
			} else if (state.getMapFailLabelToProb().containsKey(FailLabel.FAILED) && state.getMapFailLabelToProb().get(FailLabel.FAILED) == 1) {
				// To differentiate between fail states we also compute their MTTF
				// A fail state is one that contains the FAILED label with a probability of 100%
				List<MarkovTransition<S>> succTransitions = ma.getSuccTransitions(state);
				double exitRate = ma.getExitRateForState(state);
				for (MarkovTransition<S> transition : succTransitions) {
					MarkovState toState = transition.getTo();
					if (!toState.getFailLabels().contains(FailLabel.FAILED) || toState.getMapFailLabelToProb().get(FailLabel.FAILED) != 1) {
						// The toState should not be a fail state
						double toValue = values[toState.getValuesIndex()];
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
	private List<MarkovTransition<S>> selectOptimalTransitionGroup(MarkovAutomaton<S> ma, S state) {
		List<MarkovTransition<S>> bestTransitionGroup = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		double bestTransitionProbFail = 1;
		
		Map<Object, List<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
		
		for (List<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
			double expectationValue = 0;
			double transitionGroupProbFail = 0;
			
			for (MarkovTransition<S> transition : transitionGroup) {
				double prob = transition.getRate();
				MarkovState toState = transition.getTo();
				double failProb = toState.getMapFailLabelToProb().getOrDefault(FailLabel.FAILED, 0d);
				// Due to rounding, the probability may be greater than 1. This leads to no transition being selected in the scheduler.
				prob = Math.min(prob, 1);
				transitionGroupProbFail += prob * failProb;
				
				double toValue = results.getOrDefault(toState, Double.NEGATIVE_INFINITY);
				expectationValue += prob * toValue;
			}
			
			if ((transitionGroupProbFail < bestTransitionProbFail)
					|| (expectationValue + EPS >= bestValue && bestTransitionProbFail >= transitionGroupProbFail)) {
				boolean isNewBestTransition = bestTransitionGroup == null || (transitionGroupProbFail < bestTransitionProbFail) || expectationValue > bestValue + EPS;
				
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
	private boolean checkMinimality(List<MarkovTransition<S>> transitionGroup, List<MarkovTransition<S>> bestTransitionGroup) {
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
