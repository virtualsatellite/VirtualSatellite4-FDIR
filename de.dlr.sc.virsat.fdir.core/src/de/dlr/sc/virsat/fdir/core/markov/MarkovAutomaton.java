/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;

/**
 * Implementation of a markov automaton.
 * 
 * @author muel_s8
 *
 * @param <S>
 */

public class MarkovAutomaton<S extends MarkovState> {
	private List<S> states = new ArrayList<>();
	private Map<S, Double> finalStateProbs = new HashMap<>();
	private Set<Object> events = new HashSet<>();
	private Map<Object, List<MarkovTransition<S>>> mapEventToTransitions = new HashMap<>();
	private Map<S, List<MarkovTransition<S>>> mapStateToSuccTransitions = new HashMap<>();
	private Map<S, List<MarkovTransition<S>>> mapStateToPredTransitions = new HashMap<>();

	/**
	 * Gets the states of the markov automaton
	 * 
	 * @return the states of the markov automaton
	 */
	public List<S> getStates() {
		return states;
	}

	/**
	 * Gets the list of all possible events
	 * 
	 * @return the list of all possible events
	 */

	public Set<Object> getEvents() {
		return events;
	}

	/**
	 * Get the transitions of the markov automaton for a specific event
	 * 
	 * @param event the event
	 * @return a list of markov transitions
	 */
	public List<MarkovTransition<S>> getTransitions(Object event) {
		return mapEventToTransitions.computeIfAbsent(event, key -> new ArrayList<>());
	}

	/**
	 * Get all transitions of the markov automaton
	 * 
	 * @return all transitions
	 */
	public List<MarkovTransition<S>> getTransitions() {
		return mapEventToTransitions.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
	}

	/**
	 * outputs the list of events from a state to its successors
	 * 
	 * @param state
	 * @return list of events of a state
	 */
	public List<Object> getSuccEvents(Object state) {
		List<Object> stateLabels = new ArrayList<Object>();
		List<MarkovTransition<S>> stateOutgoingTransitions = getSuccTransitions(state);
		for (MarkovTransition<S> stateOutgoingTransition : stateOutgoingTransitions) {
			stateLabels.add(stateOutgoingTransition.getEvent());
		}
		return stateLabels;
	}

	/**
	 * outputs the list of events to a state from its predecessors
	 * 
	 * @param state
	 * @return list of events of a state
	 */
	public List<Object> getPredEvents(Object state) {
		List<Object> stateLabels = new ArrayList<Object>();
		List<MarkovTransition<S>> stateOutgoingTransitions = getPredTransitions(state);
		for (MarkovTransition<S> stateOutgoingTransition : stateOutgoingTransitions) {
			stateLabels.add(stateOutgoingTransition.getEvent());
		}
		return stateLabels;
	}

	/**
	 * Gets a list of transitions leading to successors from a given state
	 * 
	 * @param state the source state
	 * @return a list of transitions
	 */
	public List<MarkovTransition<S>> getSuccTransitions(Object state) {
		return mapStateToSuccTransitions.getOrDefault(state, Collections.emptyList());
	}

	/**
	 * Gets the successor transition of a given state grouped by the event label
	 * 
	 * @param state the state
	 * @return a map from event label to transition set
	 */
	public Map<Object, List<MarkovTransition<S>>> getGroupedSuccTransitions(Object state) {
		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		return succTransitions.stream().collect(Collectors.groupingBy(MarkovTransition::getEvent, Collectors.toList()));
	}

	/**
	 * Gets a list of transitions leading to a given state from some predecessors
	 * 
	 * @param state the target state
	 * @return a list of transitions
	 */
	public List<MarkovTransition<S>> getPredTransitions(Object state) {
		return mapStateToPredTransitions.getOrDefault(state, Collections.emptyList());
	}

	/**
	 * Adds a state to the state space
	 * 
	 * @param state the state to be added
	 */
	public void addState(S state) {
		state.index = states.size();
		states.add(state);

		mapStateToSuccTransitions.put(state, new ArrayList<>());
		mapStateToPredTransitions.put(state, new ArrayList<>());
	}

	/**
	 * Adds a state to the state space with a specified index since reducing the state space can lead to duplicate indices
	 * @param state the state to be added
	 * @param index the index that should be assigned to the state
	 */
	public void addState(S state, int index) {
		state.index = index;
		states.add(state);
		
		mapStateToSuccTransitions.put(state, new ArrayList<>());
		mapStateToPredTransitions.put(state, new ArrayList<>());
	}
	
	/**
	 * Add a new markov transition to the automaton
	 * 
	 * @param event       the transition event
	 * @param from        the starting state of the transition
	 * @param to          the end state of the transition
	 * @param rate        the transition rate
	 * @param isMarkovian whether the transition is markovian
	 * @return the created transition
	 */
	public MarkovTransition<S> addTransition(Object event, S from, S to, double rate, MarkovStateType type) {
		getEvents().add(event);

		MarkovTransition<S> t = new MarkovTransition<>(from, to, rate, event);
		List<MarkovTransition<S>> transitions = getTransitions(event);

		transitions.add(t);
		from.setType(type);

		this.mapStateToSuccTransitions.get(from).add(t);
		this.mapStateToPredTransitions.get(to).add(t);

		return t;
	}

	/**
	 * Adds a labeled Markovian transition
	 * 
	 * @param event the transition event
	 * @param from  the starting state of the transition
	 * @param to    the end state of the transition
	 * @param rate  the transition rate
	 * @return the created transition
	 */
	public MarkovTransition<S> addMarkovianTransition(Object event, S from, S to, double rate) {
		return addTransition(event, from, to, rate, MarkovStateType.MARKOVIAN);
	}

	/**
	 * Adds a labeled nondeterministic transition
	 * 
	 * @param event the transition event
	 * @param from  the starting state of the transition
	 * @param to    the end state of the transition
	 * @param prob  the probability of the nondeterministic transition
	 * @return the created transition
	 */
	public MarkovTransition<S> addNondeterministicTransition(Object event, S from, S to, double prob) {
		return addTransition(event, from, to, prob, MarkovStateType.NONDET);
	}

	/**
	 * Adds a labeled probabilistic transition
	 * 
	 * @param event the transition event
	 * @param from  the starting state of the transition
	 * @param to    the end state of the transition
	 * @param prob  the probability of the probabilistic transition
	 * @return the created transition
	 */
	public MarkovTransition<S> addProbabilisticTransition(Object event, S from, S to, double prob) {
		return addTransition(event, from, to, prob, MarkovStateType.PROBABILISTIC);
	}

	/**
	 * Adds a labeled nondeterministic transition
	 * 
	 * @param event the transition event
	 * @param from  the starting state of the transition
	 * @param to    the end state of the transition
	 * @return the created transition
	 */
	public MarkovTransition<S> addNondeterministicTransition(Object event, S from, S to) {
		return addNondeterministicTransition(event, from, to, 1);
	}

	/**
	 * Removes a state from the Markov automaton
	 * 
	 * @param state the state to remove
	 */
	public void removeState(S state) {
		states.remove(state);
		finalStateProbs.remove(state);

		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		for (MarkovTransition<S> transition : succTransitions) {
			List<MarkovTransition<S>> transitions = getTransitions(transition.getEvent());
			transitions.remove(transition);
			mapStateToPredTransitions.getOrDefault(transition.getTo(), Collections.emptyList()).remove(transition);
		}

		List<MarkovTransition<S>> predTransitions = getPredTransitions(state);
		for (MarkovTransition<S> transition : predTransitions) {
			List<MarkovTransition<S>> transitions = getTransitions(transition.getEvent());
			transitions.remove(transition);
			mapStateToSuccTransitions.getOrDefault(transition.getFrom(), Collections.emptyList()).remove(transition);
		}

		mapStateToSuccTransitions.remove(state);
		mapStateToPredTransitions.remove(state);
	}

	/**
	 * Due to merging of states for the Bisimulation, the successor and predecessor
	 * transitions should get updated based on their deleted states
	 * 
	 * @param state having failed successorStates that are deleted from Markov
	 *              Automta state
	 */
	public void removeInvalidTransitions(S state) {
		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		for (int i = succTransitions.size() - 1; i >= 0; i--) {
			MarkovTransition<S> outgoingTransition = succTransitions.get(i);
			MarkovState toState = outgoingTransition.getTo();
			if (!getStates().contains(toState)) {
				mapStateToSuccTransitions.get(state).remove(outgoingTransition);
			}
		}

		List<MarkovTransition<S>> predTransitions = getPredTransitions(state);
		for (int i = predTransitions.size() - 1; i >= 0; i--) {
			MarkovTransition<S> incomingTransition = predTransitions.get(i);
			MarkovState fromState = incomingTransition.getFrom();
			if (!getStates().contains(fromState)) {
				mapStateToPredTransitions.get(state).remove(incomingTransition);
			}
		}

	}

	/**
	 * This method deletes the markov transitions if a state has same transitions to
	 * a same state witha same event
	 * @param state
	 */
	public void removeDuplicateTransitions(S state) {
		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		for (int i = succTransitions.size() - 1; i > 0; i--) {
			MarkovTransition<S> outgoingTransition1 = succTransitions.get(i);
			MarkovTransition<S> outgoingTransition2 = succTransitions.get(i - 1);
			MarkovState toState1 = outgoingTransition1.getTo();
			MarkovState toState2 = outgoingTransition2.getTo();
			Object stateEvent1 = outgoingTransition1.getEvent();
			Object stateEvent2 = outgoingTransition2.getEvent();
			if (toState1 == toState2 && stateEvent1 == stateEvent2) {
				List<MarkovTransition<S>> transitions = getTransitions(stateEvent1);
				transitions.remove(outgoingTransition1);
				mapStateToSuccTransitions.get(state).remove(outgoingTransition1);
			}
		}

		List<MarkovTransition<S>> predTransitions = getPredTransitions(state);
		for (int i = predTransitions.size() - 1; i > 0; i--) {
			MarkovTransition<S> incomingTransition1 = predTransitions.get(i);
			MarkovTransition<S> incomingTransition2 = predTransitions.get(i - 1);
			MarkovState fromState1 = incomingTransition1.getFrom();
			MarkovState fromState2 = incomingTransition2.getFrom();
			Object stateEvent1 = incomingTransition1.getEvent();
			Object stateEvent2 = incomingTransition2.getEvent();
			if (fromState1 == fromState2 && stateEvent1 == stateEvent2) {
				List<MarkovTransition<S>> transitions = getTransitions(stateEvent1);
				transitions.remove(incomingTransition1);
				mapStateToSuccTransitions.get(state).remove(incomingTransition1);
			}
		}
	}

	/**
	 * Computes the total exit rate for a markovian state
	 * 
	 * @param state Markov state
	 * @return Returns exit rate for state
	 */
	public double getExitRateForState(MarkovState state) {
		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		return succTransitions.stream().mapToDouble(MarkovTransition::getRate).reduce(0, Double::sum);
	}

	@Override
	public String toString() {
		return toDot();
	}

	/**
	 * Computes a string representation of the markov automaton in the .dot format
	 * 
	 * @return a string representation the markov automaton in the .dot format
	 */

	public String toDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph ma {\n");
		sb.append(getStates().stream().map(MarkovState::toString).collect(Collectors.joining("\n")) + "\n");
		sb.append(getTransitions().stream()
				.map(transition -> transition.getFrom().getIndex() + "->" + transition.getTo().getIndex() + " [label=\""
						+ String.valueOf(transition.getEvent()) + " : " + transition.getRate() + "\"]")
				.sorted().collect(Collectors.joining("\n")) + "\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Checks if the Markov Model is a Continuous Time Markov Chain
	 * 
	 * @return true iff the Markov Automaton is a CTMC
	 */
	public boolean isCTMC() {
		return getStates().stream().allMatch(MarkovState::isMarkovian);
	}

	/**
	 * Checks if a given rate is a valid markovian rate
	 * 
	 * @param rate the rate
	 * @return true iff the rate is a real number > 0
	 */
	public static boolean isRateDefined(double rate) {
		return Double.isFinite(rate) && rate > 0;
	}

	/**
	 * Gets the states in the ma which have the labels from the label provider
	 * 
	 * @param failLabelProvider the label provider
	 * @return all states which have at least all states listed in the label
	 *         provider
	 */
	public Set<S> getStatesWithLabels(FailLabelProvider failLabelProvider) {
		Set<S> statesWithLabel = new HashSet<>();

		for (S state : getStates()) {
			if (state.getFailLabels().containsAll(failLabelProvider.getFailLabels())) {
				statesWithLabel.add(state);
			}
		}

		return statesWithLabel;
	}

	/**
	 * Gets the initial MTTF according to the Bellman equations with MTTF(s) = 0 if
	 * s is a fail state and MTTF(s) = 1/ExitRate(s) if s is not a fail state
	 * 
	 * @param states            states that are also in this list will be considered
	 * @param failLabelProvider the provider defining what is a fail state
	 * @return the initial probability distribution
	 */
	public double[] getNonFailSoujornTimes(List<? extends MarkovState> states, FailLabelProvider failLabelProvider) {
		int countStates = states.size();
		double[] inititalVector = new double[countStates];

		Set<MarkovState> failReachableStates = getStatesWithReachableFailState(failLabelProvider);

		for (int i = 0; i < countStates; ++i) {
			MarkovState state = states.get(i);
			if (state.isMarkovian() && !state.getFailLabels().containsAll(failLabelProvider.getFailLabels())) {
				inititalVector[i] = failReachableStates.contains(state) ? 1 / getExitRateForState(state)
						: Double.POSITIVE_INFINITY;
			}
		}
		return inititalVector;
	}

	/**
	 * Gets an array with only the soujourn times of fail states.
	 * 
	 * @param states            only fail states that are also in this list will be
	 *                          considered
	 * @param failLabelProvider the provider defining what is a fail state
	 * @return the soujourn times
	 */
	public double[] getFailSoujournTimes(List<MarkovState> states, FailLabelProvider failLabelProvider) {
		int countStates = states.size();
		double[] inititalVector = new double[countStates];

		for (int i = 0; i < countStates; ++i) {
			MarkovState state = states.get(i);
			if (state.isMarkovian() && state.getFailLabels().containsAll(failLabelProvider.getFailLabels())) {
				inititalVector[i] = 1 / getExitRateForState(state);
			}
		}
		return inititalVector;
	}

	/**
	 * Gets the soujourn time of any markovian state.
	 * 
	 * @param states
	 * @return the soujourn times
	 */
	public double[] getSoujournTimes(List<MarkovState> states) {
		return getFailSoujournTimes(states, FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER);
	}

	/**
	 * Computes the set of states that can reach a fail state
	 * 
	 * @param mc the markov chain
	 * @return the set of states that can reach a fail state
	 */
	public Set<MarkovState> getStatesWithReachableFailState(FailLabelProvider failLabelProvider) {
		Queue<MarkovState> toProcess = new LinkedList<>();
		toProcess.addAll(getStatesWithLabels(failLabelProvider));
		Set<MarkovState> failReachableStates = new HashSet<>();

		while (!toProcess.isEmpty()) {
			MarkovState state = toProcess.poll();
			if (failReachableStates.add(state)) {
				List<MarkovTransition<S>> transitions = getPredTransitions(state);
				for (int j = 0; j < transitions.size(); ++j) {
					MarkovTransition<S> transition = transitions.get(j);
					toProcess.add(transition.getFrom());
				}
			}
		}
		return failReachableStates;
	}
}
