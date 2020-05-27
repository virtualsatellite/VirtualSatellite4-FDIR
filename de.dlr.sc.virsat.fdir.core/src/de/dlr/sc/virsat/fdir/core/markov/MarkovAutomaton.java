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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a markov automaton.
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
	 * @return the states of the markov automaton
	 */
	public List<S> getStates() {
		return states;
	}
	
	/**
	 * Gets a set of all final states
	 * @return a set of final states
	 */
	public Set<S> getFinalStates() {
		return finalStateProbs.keySet();
	}
	
	/**
	 * Gets the mapping from state to the probability of being a final state
	 * @return the final state probablity
	 */
	public Map<S, Double> getFinalStateProbs() {
		return finalStateProbs;
	}
	
	/**
	 * Gets the list of all possible events
	 * @return the list of all possible events
	 */
	
	public Set<Object> getEvents() {
		return events;
	}
	
	/**
	 * Get the transitions of the markov automaton for a specific event
	 * @param event the event
	 * @return a list of markov transitions
	 */
	public List<MarkovTransition<S>> getTransitions(Object event) {
		List<MarkovTransition<S>> transitions = mapEventToTransitions.get(event);
		
		if (transitions == null) {
			transitions = new ArrayList<>();
			mapEventToTransitions.put(event, transitions);
		}
		
		return transitions;
	}

	/**
	 * Get all transitions of the markov automaton
	 * @return all transitions
	 */
	public List<MarkovTransition<S>> getTransitions() {
		return mapEventToTransitions.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
	}
	
	/**
	 * Gets a list of transitions leading to successors from a given state
	 * @param state the source state
	 * @return a list of transitions
	 */
	public List<MarkovTransition<S>> getSuccTransitions(Object state) {
		return mapStateToSuccTransitions.getOrDefault(state, Collections.emptyList());
	}
	
	/**
	 * Gets the successor transition of a given state grouped by the event label
	 * @param state the state
	 * @return a map from event label to transition set
	 */
	public Map<Object, List<MarkovTransition<S>>> getGroupedSuccTransitions(Object state) {
		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		return succTransitions.stream().collect(Collectors.groupingBy(MarkovTransition::getEvent, Collectors.toList()));
	}
	
	/**
	 * Gets a list of transitions leading to a given state from some predecessors
	 * @param state the target state
	 * @return a list of transitions
	 */
	public List<MarkovTransition<S>> getPredTransitions(Object state) {
		return mapStateToPredTransitions.getOrDefault(state, Collections.emptyList());
	}
	
	/**
	 * Adds a state to the state space
	 * @param state the state to be added
	 */
	public void addState(S state) {
		state.index = states.size();
		states.add(state);
		
		mapStateToSuccTransitions.put(state, new ArrayList<>());
		mapStateToPredTransitions.put(state, new ArrayList<>());
	}
	
	/**
	 * Adds a state with the given final prob.
	 * If the final prob is non-zero, the state is added to the final states.
	 * @param state the state to add
	 * @param finalProb the final state prob
	 */
	public void addState(S state, double finalProb) {
		addState(state);
		
		if (finalProb > 0) {
			getFinalStateProbs().put(state, finalProb);
		}
	}
	
	/**
	 * Add a new markov transition to the automaton
	 * @param event the transition event
	 * @param from the starting state of the transition
	 * @param to the end state of the transition
	 * @param rate the transition rate
	 * @param isMarkovian whether the transition is markovian
	 * @return the created transition
	 */
	private MarkovTransition<S> addTransition(Object event, S from, S to, double rate, boolean isMarkovian) {
		MarkovTransition<S> t = new MarkovTransition<>(from, to, rate, event, isMarkovian);
		List<MarkovTransition<S>> transitions = getTransitions(event);
		
		transitions.add(t);
		from.setMarkovian(isMarkovian);
		
		this.mapStateToSuccTransitions.get(from).add(t);
		this.mapStateToPredTransitions.get(to).add(t);
		
		return t;
	}
	
	/**
	 * Adds a labeled Markovian transition
	 * @param event the transition event
	 * @param from the starting state of the transition
	 * @param to the end state of the transition
	 * @param rate the transition rate
	 * @return the created transition
	 */
	public MarkovTransition<S> addMarkovianTransition(Object event, S from, S to, double rate) {
		return addTransition(event, from, to, rate, true);
	}
	
	/**
	 * Adds a labeled nondeterministic transition
	 * @param event the transition event
	 * @param from the starting state of the transition
	 * @param to the end state of the transition
	 * @param prob the probability of the nondeterministic transition
	 * @return the created transition
	 */
	public MarkovTransition<S> addNondeterministicTransition(Object event, S from, S to, double prob) {
		return addTransition(event, from, to, prob, false);
	}
	
	/**
	 * Adds a labeled nondeterministic transition
	 * @param event the transition event
	 * @param from the starting state of the transition
	 * @param to the end state of the transition
	 * @return the created transition
	 */
	public MarkovTransition<S> addNondeterministicTransition(Object event, S from, S to) {
		return addNondeterministicTransition(event, from, to, 1);
	}
	
	/**
	 * Removes a state from the Markov automaton
	 * @param state the state to remove
	 */
	public void removeState(S state) {
		states.remove(state);
		finalStateProbs.remove(state);
		
		List<MarkovTransition<S>> succTransitions = getSuccTransitions(state);
		for (MarkovTransition<S> transition : succTransitions) {
			List<MarkovTransition<S>> transitions = getTransitions(transition.getEvent());
			transitions.remove(transition);
		}
		
		List<MarkovTransition<S>> predTransitions = getPredTransitions(state);
		for (MarkovTransition<S> transition : predTransitions) {
			List<MarkovTransition<S>> transitions = getTransitions(transition.getEvent());
			transitions.remove(transition);
		}
		
		mapStateToSuccTransitions.remove(state);
		mapStateToPredTransitions.remove(state);
	}
	
	
	/**
	 * @param state Markov state
	 * @return Returns exit rate for state 
	 */
	public double getExitRateForState(MarkovState state) {
		List<?> transitions = getSuccTransitions(state);
		
		double exitRate = 0;
		for (int j = 0; j < transitions.size(); ++j) {
			@SuppressWarnings("unchecked")
			MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
			exitRate += transition.getRate();
		}
		return exitRate;
	}
	
	@Override
	public String toString() {
		return toDot();
	}
	
	/**
	 * Computes a string representation of the markov automaton in the .dot format
	 * @return a string representation the markov automaton in the .dot format
	 */
	
	public String toDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph ma {\n");
		
		for (MarkovState state : states) {
			sb.append(state.toString() + "\n");
		}
		
		for (MarkovTransition<S> transition : getTransitions()) {
			sb.append(transition.getFrom().getIndex() + "->" + transition.getTo().getIndex() + " [label=\"" + (transition.getEvent() != null ? transition.getEvent().toString() : "") + " : " + transition.getRate() +  "\"]\n");
		}
		
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Checks if the Markov Model is a Continuous Time Markov Chain
	 * @return true iff the Markov Automaton is a CTMC
	 */
	public boolean isCTMC() {
		for (MarkovState state : getStates()) {
			for (MarkovTransition<? extends MarkovState> transition : getSuccTransitions(state)) {
				if (!transition.isMarkovian()) {
					return false;
				}
			}
		}	
		return true;
	}

	/**
	 * Checks if a given rate is a valid markovian rate
	 * @param rate the rate
	 * @return true iff the rate is a real number > 0
	 */
	public static boolean isRateDefined(double rate) {
		return Double.isFinite(rate) && rate > 0;
	}
}
