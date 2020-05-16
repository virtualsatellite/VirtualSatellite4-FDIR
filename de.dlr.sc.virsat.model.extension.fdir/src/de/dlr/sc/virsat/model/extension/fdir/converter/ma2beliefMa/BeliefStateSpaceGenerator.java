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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.AStateSpaceGenerator;
import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTStateEquivalence;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.ObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

public class BeliefStateSpaceGenerator extends AStateSpaceGenerator<BeliefState> {
	
	private static final double EPSILON = 0.2;
	
	private MarkovAutomaton<DFTState> ma;
	private PODFTState initialStateMa;

	private DFTStateEquivalence dftStateEquivalence;
	private BeliefStateEquivalence beliefStateEquivalence;
	
	public void configure(MarkovAutomaton<DFTState> ma, PODFTState initialStateMa) {
		this.ma = ma;
		this.initialStateMa = initialStateMa;
	}
	
	@Override
	public void init(MarkovAutomaton<BeliefState> targetMa) {
		super.init(targetMa);
		
		beliefStateEquivalence = new BeliefStateEquivalence(EPSILON);
		dftStateEquivalence = new DFTStateEquivalence();
		for (DFTState dftState : ma.getStates()) {
			dftStateEquivalence.getEquivalentState(dftState, true);
		}
	}
	
	@Override
	public BeliefState createInitialState() {
		BeliefState initialBeliefState = new BeliefState(targetMa, initialStateMa);
		initialBeliefState.mapStateToBelief.put(initialStateMa, 1d);
		beliefStateEquivalence.addState(initialBeliefState);
		return initialBeliefState;
	}
	
	@Override
	public List<BeliefState> generateSuccs(BeliefState beliefState) {
		List<BeliefState> generatedSuccs = new ArrayList<>();
		Map<PODFTState, Set<MarkovTransition<DFTState>>> mapObsertvationSetToTransitions = createMapRepresentantToTransitions(ma, beliefState);
		
		for (Entry<PODFTState, Set<MarkovTransition<DFTState>>> entry : mapObsertvationSetToTransitions.entrySet()) {
			BeliefState beliefSucc = new BeliefState(targetMa, entry.getKey());
			BeliefState equivalentBeliefSucc = null;
			Set<MarkovTransition<DFTState>> succTransitions = entry.getValue();
			
			if (beliefState.isMarkovian()) {
				Entry<Set<Object>, Boolean> observationEvent = extractObservationEvent(beliefState, beliefSucc, succTransitions);
				
				double exitRate = beliefState.getTotalRate(entry.getValue());
				fillMarkovianStateSucc(beliefState, beliefSucc, exitRate, observationEvent, succTransitions);
				equivalentBeliefSucc = addBeliefState(beliefSucc);
				
				if (beliefState != equivalentBeliefSucc) {
					targetMa.addMarkovianTransition(observationEvent, beliefState, equivalentBeliefSucc, exitRate);
				}
			} else {
				fillNonDeterministicStateSucc(beliefState, beliefSucc, succTransitions);
				equivalentBeliefSucc = addBeliefState(beliefSucc);
				addNondeterministicTransitions(succTransitions, beliefState, equivalentBeliefSucc);
			}
			
			if (beliefSucc == equivalentBeliefSucc) {
				generatedSuccs.add(beliefSucc);
			}
		}
		
		return generatedSuccs;
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
			PODFTState fromState = entry.getKey();
			if (fromState.isMarkovian() != beliefState.isMarkovian()) {
				continue;
			}
			
			List<MarkovTransition<DFTState>> succTransitions = ma.getSuccTransitions(fromState);
			for (MarkovTransition<DFTState> succTransition : succTransitions) {
				PODFTState succState = (PODFTState) succTransition.getTo();
				
				Set<MarkovTransition<DFTState>> transitions = null;
				
				for (Entry<PODFTState, Set<MarkovTransition<DFTState>>> representantEntry : mapRepresentantToTransitions.entrySet()) {
					PODFTState representant = representantEntry.getKey();
					if (representant.getObservedFailed().equals(succState.getObservedFailed()) 
							&& representant.getMapSpareToClaimedSpares().equals(succState.getMapSpareToClaimedSpares())
							&& representant.isMarkovian() == succState.isMarkovian()) {
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
	 * Extracts the set observation event containing the observed events and the information if this is a repair
	 * event or not
	 * @param succTransitions the successor transitions from a belief state to the successor belief state
	 * @return the set of observation event
	 */
	private Entry<Set<Object>, Boolean> extractObservationEvent(BeliefState beliefState, BeliefState beliefSucc, Set<MarkovTransition<DFTState>> succTransitions) {
		Set<Object> observationSet = new HashSet<>();
		
		MarkovTransition<DFTState> representantTransition = succTransitions.iterator().next();
		Object event = representantTransition.getEvent();
		if (event instanceof ObservationEvent) {
			ObservationEvent obsEvent = (ObservationEvent) event;
			observationSet.add(obsEvent.getNode());
			Entry<Set<Object>, Boolean> observationEvent = new SimpleEntry<>(observationSet, obsEvent.getIsRepair());
			return observationEvent;
		}
		
		// obtain all newly observed failed nodes
		Set<FaultTreeNode> succObservedFailedNodes = beliefSucc.representant.getObservedFailedNodes();
		Set<FaultTreeNode> currentObservedFailedNodes = beliefState.representant.getObservedFailedNodes();
		observationSet.addAll(succObservedFailedNodes);
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
			
			Set<DFTState> succStates = new HashSet<>();
			
			for (MarkovTransition<DFTState> succTransition : succTransitionGroup.getValue()) {
				succStates.add(succTransition.getFrom());
				prob += beliefState.mapStateToBelief.get(succTransition.getFrom());
			}
			
			for (Entry<PODFTState, Double> entry : beliefState.mapStateToBelief.entrySet()) {
				PODFTState state = entry.getKey();
				if (!succStates.contains(state)) {
					if (state.getFailedBasicEvents().isEmpty()) {
						prob += entry.getValue();
					}
				}
			}
			
			targetMa.addNondeterministicTransition(succTransitionGroup.getKey(), beliefState, beliefSucc, prob);
		}
	}
	
	/**
	 * Initializes the data of a successor of a markovian state
	 * @param beliefState the current state
	 * @param beliefSucc the successor
	 * @param exitRate the exit rate of the current state
	 * @param observationEvent the observation event
	 * @param succTransitions the successor transitions
	 */
	private void fillMarkovianStateSucc(BeliefState beliefState, BeliefState beliefSucc, 
			double exitRate, Entry<Set<Object>, Boolean> observationEvent, Set<MarkovTransition<DFTState>> succTransitions) {
		boolean isMarkovian = true;
		boolean isInternalTransition = observationEvent.getKey().isEmpty();
		
		Set<PODFTState> statesWithNoTransitions = new HashSet<>(beliefState.mapStateToBelief.keySet());
		
		for (MarkovTransition<DFTState> transition : succTransitions) {
			PODFTState fromState = (PODFTState) transition.getFrom();
			PODFTState toState = (PODFTState) transition.getTo();
			statesWithNoTransitions.remove(fromState);
			
			double oldProb = beliefState.mapStateToBelief.get(fromState);
			double prob = oldProb * (oldProb * transition.getRate() / exitRate);
			
			if (isInternalTransition) {
				double exitRateFromState = ma.getExitRateForState(fromState);
				double time = 1 / transition.getRate();
				double residueProb = oldProb;
				
				double remainProb = Math.exp(-exitRateFromState * time);
				double exitProb = 1 - remainProb;
				residueProb *= remainProb;
				prob *= exitProb;
				
				if (residueProb > 0) {
					beliefSucc.mapStateToBelief.merge(fromState, residueProb, (p1, p2) -> p1 + p2);
				}
			}
			
			if (prob > 0) {
				if (isInternalTransition) {
					toState = getTargetState(ma, toState);
				}
				beliefSucc.mapStateToBelief.merge(toState, prob, (p1, p2) -> p1 + p2);
			}
			
			if (!toState.isMarkovian()) {
				isMarkovian = false;
			} 
		}
		beliefSucc.setMarkovian(isMarkovian);
		
		if (isInternalTransition) {
			for (PODFTState state : statesWithNoTransitions) {
				beliefSucc.mapStateToBelief.merge(state, beliefState.mapStateToBelief.get(state), (p1, p2) -> p1 + p2);
			}
		}
	}
	
	/**
	 * Checks the target state and updates it if necessary
	 * @param ma the markov automaton
	 * @param toState the current target state
	 * @return the updated target state
	 */
	private PODFTState getTargetState(MarkovAutomaton<DFTState> ma, PODFTState toState) {
		if (!toState.isMarkovian()) {
			List<MarkovTransition<DFTState>> transitions = ma.getSuccTransitions(toState);
			toState = (PODFTState) transitions.stream()
					.filter(t -> t.getEvent().equals(Collections.emptyList()))
					.map(t -> t.getTo())
					.findFirst().orElse(toState);
		}
		
		return toState;
	}
	
	/**
	 * Fills the successor of a non deterministic state with data.
	 * @param beliefState the current state
	 * @param beliefSucc the successor state
	 * @param succTransitions the transitions to reach the successor state
	 */
	private void fillNonDeterministicStateSucc(BeliefState beliefState, BeliefState beliefSucc, Set<MarkovTransition<DFTState>> succTransitions) {
		Set<PODFTState> statesWithNoTransitions = new HashSet<>(beliefState.mapStateToBelief.keySet());
		
		beliefSucc.setMarkovian(true);
		for (MarkovTransition<DFTState> succTransition : succTransitions) {
			PODFTState succState = (PODFTState) succTransition.getTo();
			PODFTState fromState = (PODFTState) succTransition.getFrom();
			
			double prob = succTransition.getRate() * beliefState.mapStateToBelief.get(fromState);
			beliefSucc.mapStateToBelief.put(succState, prob);
			
			statesWithNoTransitions.remove(fromState);
		}
		
		for (PODFTState stateWithNoTransition : statesWithNoTransitions) {
			if (stateWithNoTransition.getObservedFailedNodes().equals(beliefSucc.representant.getObservedFailedNodes())) {
				PODFTState succState = stateWithNoTransition;
				
				DFTState copy = stateWithNoTransition.copy();
				copy.setMapSpareToClaimedSpares(beliefSucc.representant.getMapSpareToClaimedSpares());
				succState = (PODFTState) dftStateEquivalence.getEquivalentState(copy, false);
				if (succState == copy) {
					continue;
				}
				
				double prob = beliefState.mapStateToBelief.get(stateWithNoTransition);
				beliefSucc.mapStateToBelief.put(succState, prob);
			}
		}
	}
	
	/**
	 * Adds the belief state into the belief markov automaton if no equivalent 
	 * state exists. Otherwise returns the equivalent state
	 * @param beliefState the belief state
	 * @param isFinal whether it is a final state
	 * @return the equivalent belief state
	 */
	private BeliefState addBeliefState(BeliefState beliefState) {
		beliefState.normalize();
		
		boolean isFinal = true;
		for (Entry<PODFTState, Double> entry : beliefState.mapStateToBelief.entrySet()) {
			isFinal &= ma.getFinalStates().contains(entry.getKey());
		}
		
		BeliefState equivalentBeliefState = beliefStateEquivalence.getEquivalentState(beliefState);
		boolean isNewState = beliefState == equivalentBeliefState;
		
		if (isNewState) {
			targetMa.addState(beliefState);
			
			if (isFinal) {
				targetMa.getFinalStates().add(beliefState);
			}
		}
		
		return equivalentBeliefState;
	}
}
