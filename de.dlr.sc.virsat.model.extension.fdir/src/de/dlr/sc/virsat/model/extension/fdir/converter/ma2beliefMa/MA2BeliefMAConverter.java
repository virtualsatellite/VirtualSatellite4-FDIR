/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * This class creates a belief markov automaton out of a regular markov automaton.
 * A belief markov automaton has a state space where each state holds
 * a probability distribution over a set of states of the original markov automaton.
 *
 */

public class MA2BeliefMAConverter {
	
	private MarkovAutomaton<BeliefState> beliefMa;
	private BeliefState initialBeliefState;
	
	/**
	 * Creates a belief markov automaton out of the given markov automaton
	 * @param ma the markov automaton
	 * @param initialState the initial state of the markov automaton
	 * @return the belief markov automaton
	 */
	public MarkovAutomaton<BeliefState> convert(MarkovAutomaton<DFTState> ma, PODFTState initialState) {
		beliefMa = new MarkovAutomaton<>();
		
		initialBeliefState = new BeliefState(beliefMa, initialState);
		initialBeliefState.mapStateToBelief.put(initialState, 1d);
		beliefMa.addState(initialBeliefState);
		
		Queue<BeliefState> toProcess = new LinkedList<>();
		toProcess.offer(initialBeliefState);
		
		while (!toProcess.isEmpty()) {
			BeliefState beliefState = toProcess.poll();
			Map<PODFTState, Set<MarkovTransition<DFTState>>> mapObsertvationSetToTransitions = createMapRepresentantToTransitions(ma, beliefState);
			
			for (Entry<PODFTState, Set<MarkovTransition<DFTState>>> entry : mapObsertvationSetToTransitions.entrySet()) {
				BeliefState beliefSucc = new BeliefState(beliefMa, entry.getKey());
				boolean isSuccNewState = false;
				boolean isFinal = false;
				
				if (beliefState.isMarkovian()) {
					Entry<Set<Object>, Boolean> observationEvent = extractObservationEvent(beliefState, beliefSucc);
					double exitRate = getTotalRate(beliefState, entry.getValue());
					
					boolean isMarkovian = true;
					
					for (MarkovTransition<DFTState> transition : entry.getValue()) {
						double oldProb = beliefState.mapStateToBelief.get(transition.getFrom());
						double prob = oldProb * transition.getRate() / exitRate;
						isFinal |= ma.getFinalStates().contains(transition.getTo());
						
						if (observationEvent.getKey().isEmpty()) {
							double time = 1 / exitRate;
							PODFTState residueState = (PODFTState) transition.getFrom();
							double residueProb = oldProb;
							
							double remainProb = Math.exp(-transition.getRate() * time);
							double exitProb = 1 - remainProb;
							residueProb *= remainProb;
							prob *= exitProb;
							
							residueProb += beliefSucc.mapStateToBelief.getOrDefault(residueState, 0d);
							beliefSucc.mapStateToBelief.put(residueState, residueProb);
						} 
						
						prob += beliefSucc.mapStateToBelief.getOrDefault(transition.getTo(), 0d);
						beliefSucc.mapStateToBelief.put((PODFTState) transition.getTo(), prob);
						if (!transition.getTo().isMarkovian()) {
							isMarkovian = false;
						}
					}
					
					beliefSucc.setMarkovian(isMarkovian);
					
					beliefSucc.normalize();
					BeliefState equivalentBeliefSucc = getEquivalentBeliefState(beliefSucc);
					isSuccNewState = beliefSucc == equivalentBeliefSucc;
					
					if (beliefState != equivalentBeliefSucc) {
						beliefMa.addMarkovianTransition(observationEvent, beliefState, equivalentBeliefSucc, exitRate);
					}
					
				} else {
					beliefSucc.setMarkovian(true);
					
					Set<MarkovTransition<DFTState>> succTransitions = entry.getValue();
					for (MarkovTransition<DFTState> succTransition : succTransitions) {
						PODFTState succState = (PODFTState) succTransition.getTo();
						
						double prob = succTransition.getRate() * beliefState.mapStateToBelief.get(succTransition.getFrom());
						beliefSucc.mapStateToBelief.put(succState, prob);
						
						isFinal |= ma.getFinalStates().contains(succState);
					}
					
					beliefSucc.normalize();
					BeliefState equivalentBeliefSucc = getEquivalentBeliefState(beliefSucc);
					isSuccNewState = beliefSucc == equivalentBeliefSucc;
					
					addNondeterministicTransitions(succTransitions, beliefSucc, equivalentBeliefSucc);
				}
				
				if (isSuccNewState) {
					beliefMa.addState(beliefSucc);
					toProcess.offer(beliefSucc);
					
					if (isFinal) {
						beliefMa.getFinalStates().add(beliefSucc);
					}
				}
			}
		}
		
		return beliefMa;
	}
	
	/**
	 * Gets the initial belief state corresponding to the initial Ma state
	 * @return the initial belief state
	 */
	public BeliefState getInitialBeliefState() {
		return initialBeliefState;
	}
	
	/**
	 * Checks for all MA states in the belief state how the observation set of their successors look like
	 * and groups all possible successors according to their observation sets
	 * @param beliefState the belief state
	 * @param ma the markov automaton
	 * @return a mapping from an observation set to the transitions that lead to states with this observation set
	 */
	private Map<PODFTState, Set<MarkovTransition<DFTState>>> createMapRepresentantToTransitions(MarkovAutomaton<DFTState> ma, BeliefState beliefState) {
		Map<PODFTState, Set<MarkovTransition<DFTState>>> mapRepresentantToTransitions = new HashMap<>();
		for (Entry<PODFTState, Double> entry : beliefState.mapStateToBelief.entrySet()) {
			List<MarkovTransition<DFTState>> succTransitions = ma.getSuccTransitions(entry.getKey());
			for (MarkovTransition<DFTState> succTransition : succTransitions) {
				PODFTState succState = (PODFTState) succTransition.getTo();
				Set<MarkovTransition<DFTState>> transitions = null;
				
				for (Entry<PODFTState, Set<MarkovTransition<DFTState>>> representantEntry : mapRepresentantToTransitions.entrySet()) {
					PODFTState representant = representantEntry.getKey();
					if (representant.getObservedFailed().equals(succState.getObservedFailed()) 
							&& representant.getMapSpareToClaimedSpares().equals(succState.getMapSpareToClaimedSpares())) {
						transitions = representantEntry.getValue();
					}
				}
				
				if (transitions == null) {
					transitions = new HashSet<>();
					mapRepresentantToTransitions.put(succState, transitions);
				}
			
				transitions.add(succTransition);
			}
		}
		
		return mapRepresentantToTransitions;
	}
	
	/**
	 * Computes the total rate for a transition set
	 * @param beliefState the current belief state
	 * @param transitions a set of transitions
	 * @return the total rate of the transition set
	 */
	private double getTotalRate(BeliefState beliefState, Set<MarkovTransition<DFTState>> transitions) {
		double getTotalRate = 0;
		for (MarkovTransition<DFTState> transition : transitions) {
			getTotalRate += transition.getRate() * beliefState.mapStateToBelief.get(transition.getFrom());
		}
		return getTotalRate;
	}

	private static final double EPSILON = 0.05;
	
	/**
	 * Gets an equivalent belief state
	 * @param state a belief state
	 * @return an equivalent belief state or the input state if no equivalent state exists
	 */
	private BeliefState getEquivalentBeliefState(BeliefState state) {
		for (BeliefState other : beliefMa.getStates()) {
			if (state.isMarkovian() != other.isMarkovian()) {
				continue;
			}
			
			Set<DFTState> dftStates = new HashSet<>(state.mapStateToBelief.keySet());
			dftStates.addAll(other.mapStateToBelief.keySet());
			boolean isEquivalent = other.representant.getObservedFailed().equals(state.representant.getObservedFailed()) 
					&& other.representant.getMapSpareToClaimedSpares().equals(state.representant.getMapSpareToClaimedSpares());
			
			if (isEquivalent) {
				for (DFTState dftState : dftStates) {
					
					double prob = state.mapStateToBelief.getOrDefault(dftState, Double.NaN);
					double probOther = other.mapStateToBelief.getOrDefault(dftState, Double.NaN);
					double diff = Math.abs(prob - probOther);
					
					if (Double.isNaN(diff) || diff > EPSILON) {
						isEquivalent = false;
						break;
					}
				}
			}
			
			if (isEquivalent) {
				return other;
			}
		}
		
		return state;
	}

	/**
	 * Extracts the set observation event containing the observed events and the information if this is a repair
	 * event or not
	 * @return the set of observation event
	 */
	private Entry<Set<Object>, Boolean> extractObservationEvent(BeliefState beliefState, BeliefState beliefSucc) {
		// obtain all newly observed failed nodes
		Set<FaultTreeNode> succObservedFailedNodes = beliefSucc.representant.getObservedFailedNodes();
		Set<FaultTreeNode> currentObservedFailedNodes = beliefState.representant.getObservedFailedNodes();
		Set<Object> observationSet = new HashSet<>(succObservedFailedNodes);
		observationSet.removeAll(currentObservedFailedNodes);
		
		// obtain all newly observed repaired nodes in the event that no failures were observed
		boolean isRepair = false;
		if (observationSet.isEmpty()) {
			for (FaultTreeNode node : beliefState.representant.getFTHolder().getNodes()) {
				if (currentObservedFailedNodes.contains(node) && !succObservedFailedNodes.contains(node)) {
					observationSet.add(node);
				}
			}
			
			isRepair = !observationSet.isEmpty();
		}
		
		Entry<Set<Object>, Boolean> observationEvent = new SimpleEntry<>(observationSet, isRepair);
		return observationEvent;
	}
	
	/**
	 * Inserts the appropirate nondeterminisic transitions into the belief ma
	 * by considering the probability of the transition * probability of the state
	 * @param succTransitions the succ transitions to insert
	 * @param beliefState the current state
	 * @param beliefSucc the successor state
	 */
	private void addNondeterministicTransitions(Set<MarkovTransition<DFTState>> succTransitions, BeliefState beliefState, BeliefState beliefSucc) {
		Map<Object, Set<MarkovTransition<DFTState>>> groupedSuccTransitions = succTransitions
				.stream().collect(Collectors.groupingBy(MarkovTransition::getEvent, Collectors.toSet()));
		
		for (Entry<Object, Set<MarkovTransition<DFTState>>> succTransitionGroup : groupedSuccTransitions.entrySet()) {	
			double prob = 0;
			for (MarkovTransition<DFTState> succTransition : succTransitionGroup.getValue()) {
				prob += beliefState.mapStateToBelief.get(succTransition.getFrom());
			}
			beliefMa.addNondeterministicTransition(succTransitionGroup.getKey(), beliefState, beliefSucc, prob);
		}
	}
}
