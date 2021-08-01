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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovStateType;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.AStateSpaceGenerator;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;

public class BeliefStateSpaceGenerator extends AStateSpaceGenerator<BeliefState> {
	
	private static final double EPSILON = 0.2;
	
	private static final double EULERECIPROCAL = Math.exp(-1);
	private static final double COEULERECIPROCAL = 1 - EULERECIPROCAL;
	
	private MarkovAutomaton<DFTState> ma;
	private PODFTState initialStateMa;
	private BeliefStateEquivalence beliefStateEquivalence;
	
	private OptimalTransitionsSelector<BeliefState> optimalTransitionsSelector;
	private HashSet<BeliefState> goodStates;
	private HashMap<BeliefState, List<MarkovTransition<BeliefState>>> badStates;
	
	BeliefStateSpaceGenerator() {
	}
	
	BeliefStateSpaceGenerator(OptimalTransitionsSelector<BeliefState> anOptimalTransitionsSelector) {
		optimalTransitionsSelector = anOptimalTransitionsSelector;
		goodStates = new HashSet<>();
		badStates = new HashMap<>();
	}
	
	/**
	 * Configures the belief state space generation
	 * @param ma the totally observable markov automaton
	 * @param initialStateMa the initial state of the markov automaton
	 */
	public void configure(MarkovAutomaton<DFTState> ma, PODFTState initialStateMa) {
		this.ma = ma;
		this.initialStateMa = initialStateMa;
	}
	
	@Override
	public void init(MarkovAutomaton<BeliefState> targetMa) {
		super.init(targetMa);
		beliefStateEquivalence = new BeliefStateEquivalence(EPSILON);
	}
	
	@Override
	public BeliefState createInitialState() {
		BeliefState initialBeliefState = new BeliefState(initialStateMa);
		initialBeliefState.mapStateToBelief.put(initialStateMa, 1d);
		beliefStateEquivalence.addState(initialBeliefState);
		return initialBeliefState;
	}
	
	@Override
	public List<BeliefState> generateSuccs(BeliefState beliefState, SubMonitor monitor) {
		List<BeliefState> generatedSuccs = new ArrayList<>();
		Map<PODFTState, List<MarkovTransition<DFTState>>> mapObsertvationSetToTransitions = createMapRepresentantToTransitions(ma, beliefState);

		for (Entry<PODFTState, List<MarkovTransition<DFTState>>> entry : mapObsertvationSetToTransitions.entrySet()) {
			// Eclipse trick for doing progress updates with unknown ending time
			final int PROGRESS_COUNT = 100;
			monitor.setWorkRemaining(PROGRESS_COUNT).split(1);
			
			BeliefState beliefSucc = new BeliefState(entry.getKey());
			
			beliefSucc.setType(beliefSucc.representant.getType());
			
			BeliefState equivalentBeliefSucc = null;
			List<MarkovTransition<DFTState>> succTransitions = entry.getValue();
			
			if (beliefState.isProbabilisic()) {
				Object event = succTransitions.iterator().next().getEvent();
				if (event instanceof ObservationEvent && !(event instanceof ImmediateObservationEvent)) {
					continue;
				}
			}
			
			if (!beliefState.isNondet()) {
				Entry<Set<Object>, Boolean> observationEvent = beliefState.representant.extractObservationEvent(beliefSucc.representant, succTransitions);
				
				if (beliefState.isProbabilisic()) {
					if (observationEvent.getKey().isEmpty()) {
						double totalProb = 0;
						totalProb = fillProbabilisticStateSucc(beliefState, beliefSucc, observationEvent, succTransitions);
						beliefSucc.representant = beliefSucc.mapStateToBelief.keySet().iterator().next();
						for (PODFTState candidate : beliefSucc.mapStateToBelief.keySet()) {
							if (candidate.isProbabilisic()) {
								beliefSucc.representant = candidate;
								break;
							}
						}
						beliefSucc.setType(beliefSucc.representant.getType());
						equivalentBeliefSucc = addBeliefState(beliefSucc);
						if (beliefState != equivalentBeliefSucc) {
							makeStateGood(equivalentBeliefSucc, generatedSuccs);
							targetMa.addProbabilisticTransition(observationEvent, beliefState, equivalentBeliefSucc, totalProb);
						}
					} else {
						double prob = fillProbabilisticStateSucc(beliefState, beliefSucc, observationEvent, succTransitions);
						equivalentBeliefSucc = addBeliefState(beliefSucc);
						if (beliefState != equivalentBeliefSucc) {
							makeStateGood(equivalentBeliefSucc, generatedSuccs);
							targetMa.addProbabilisticTransition(observationEvent, beliefState, equivalentBeliefSucc, prob);
						}
					}
				} else {
					double exitRate = beliefState.getTotalRate(entry.getValue());
					exitRate = fillMarkovianStateSucc(beliefState, beliefSucc, exitRate, observationEvent, succTransitions);
					beliefSucc.representant = beliefSucc.mapStateToBelief.keySet().iterator().next();
					for (PODFTState candidate : beliefSucc.mapStateToBelief.keySet()) {
						if (candidate.isProbabilisic()) {
							beliefSucc.representant = candidate;
							break;
						}
					}
					beliefSucc.setType(beliefSucc.representant.getType());
					equivalentBeliefSucc = addBeliefState(beliefSucc);
					if (beliefState != equivalentBeliefSucc) {
						makeStateGood(equivalentBeliefSucc, generatedSuccs);
						targetMa.addMarkovianTransition(observationEvent, beliefState, equivalentBeliefSucc, exitRate);
					}
				}
			} else {
				fillNonDeterministicStateSucc(beliefState, beliefSucc, succTransitions);
				beliefSucc.representant = beliefSucc.mapStateToBelief.keySet().iterator().next();
				for (PODFTState candidate : beliefSucc.mapStateToBelief.keySet()) {
					if (candidate.isProbabilisic()) {
						beliefSucc.representant = candidate;
						break;
					}
				}
				beliefSucc.setType(beliefSucc.representant.getType());
				equivalentBeliefSucc = addBeliefState(beliefSucc);
				if (badStates.containsKey(equivalentBeliefSucc)) {
					targetMa.addState(equivalentBeliefSucc, equivalentBeliefSucc.getIndex());
					for (MarkovTransition<BeliefState> transition : badStates.get(equivalentBeliefSucc)) {
						targetMa.addNondeterministicTransition(transition.getEvent(), transition.getFrom(), transition.getTo(), transition.getRate());
					}
				}
				addNondeterministicTransitions(succTransitions, beliefState, equivalentBeliefSucc);
			}
			
			if (beliefSucc == equivalentBeliefSucc) {
				generatedSuccs.add(beliefSucc);
			}
		}
		
		if (optimalTransitionsSelector != null && beliefState.isNondet()) {
			List<BeliefState> optimalSuccs = new ArrayList<>();
			List<MarkovTransition<BeliefState>> optimalTransitions = optimalTransitionsSelector.selectOptimalTransitions(targetMa, beliefState);
			for (MarkovTransition<BeliefState> optimalTransition : optimalTransitions) {
				optimalSuccs.add(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()));
				goodStates.add(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()));
				if (badStates.containsKey(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()))) {
					generatedSuccs.add(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()));
					targetMa.addState(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()), beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()).getIndex());
					for (MarkovTransition<BeliefState> transition : badStates.get(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()))) {
						targetMa.addNondeterministicTransition(transition.getEvent(), transition.getFrom(), transition.getTo(), transition.getRate());
					}
					badStates.remove(beliefStateEquivalence.getEquivalentState(optimalTransition.getTo()));
				}
			}
			for (BeliefState generatedSucc : generatedSuccs) {
				if (!goodStates.contains(generatedSucc)) {
					if (badStates.containsKey(generatedSucc)) {
						badStates.get(generatedSucc).addAll(targetMa.getPredTransitions(generatedSucc));
					} else {
						badStates.put(generatedSucc, targetMa.getPredTransitions(generatedSucc));
					}
					targetMa.removeState(generatedSucc);
				}
			}
			generatedSuccs = generatedSuccs.stream().filter(optimalSuccs::contains).collect(Collectors.toList());
		}

		if (optimalTransitionsSelector != null && !beliefState.isNondet()) {
			List<BeliefState> suboptimalSuccs = new ArrayList<>();
			for (BeliefState generatedSucc : generatedSuccs) {
				boolean isSuboptimal = true;
				for  (PODFTState belief : generatedSucc.mapStateToBelief.keySet()) {
					if (!belief.isFaultTreeNodePermanent(belief.getFTHolder().getRoot())) {
						isSuboptimal = false;
						break;
					}
				}
				if (isSuboptimal) {
					suboptimalSuccs.add(generatedSucc);
					generatedSucc.setType(MarkovStateType.MARKOVIAN);
					generatedSucc.getMapFailLabelToProb().put(FailLabel.FAILED, 1.0);
				}
			}
			generatedSuccs.removeAll(suboptimalSuccs);
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
	private Map<PODFTState, List<MarkovTransition<DFTState>>> createMapRepresentantToTransitions(MarkovAutomaton<DFTState> ma, BeliefState beliefState) {
		Map<PODFTState, List<MarkovTransition<DFTState>>> mapRepresentantToTransitions = new TreeMap<>(MarkovState.MARKOVSTATE_COMPARATOR);
		for (Entry<PODFTState, Double> entry : beliefState.mapStateToBelief.entrySet()) {
			PODFTState fromState = entry.getKey();
			
			List<MarkovTransition<DFTState>> succTransitions = ma.getSuccTransitions(fromState);
			for (MarkovTransition<DFTState> succTransition : succTransitions) {
				PODFTState succState = (PODFTState) succTransition.getTo();
				List<MarkovTransition<DFTState>> transitions = null;
				
				for (Entry<PODFTState, List<MarkovTransition<DFTState>>> representantEntry : mapRepresentantToTransitions.entrySet()) {
					PODFTState representant = representantEntry.getKey();
					if (!representant.getType().equals(MarkovStateType.NONDET) && !succState.getType().equals(MarkovStateType.NONDET)) {
						if (representant.getObservedFailedNodes().equals(succState.getObservedFailedNodes()) 
								&& representant.getMapSpareToClaimedSpares().equals(succState.getMapSpareToClaimedSpares())) {
							transitions = representantEntry.getValue();
						}
					} else {
						if (representant.getObservedFailedNodes().equals(succState.getObservedFailedNodes()) 
								&& representant.getMapSpareToClaimedSpares().equals(succState.getMapSpareToClaimedSpares())
								&& representant.getType().equals(succState.getType())) {
							transitions = representantEntry.getValue();
						}
					}
				}
				
				if (transitions == null) {
					transitions = new ArrayList<>();
					mapRepresentantToTransitions.put(succState, transitions);
				}
			
				transitions.add(succTransition);
			}
		}
		
		return mapRepresentantToTransitions;
	}
	
	/**
	 * Inserts the appropirate nondeterminisic transitions into the belief ma
	 * by considering the probability of the transition * probability of the state
	 * @param succTransitions the succ transitions to insert
	 * @param beliefState the current state
	 * @param beliefSucc the successor state
	 */
	private void addNondeterministicTransitions(List<MarkovTransition<DFTState>> succTransitions, BeliefState beliefState, BeliefState beliefSucc) {
		Map<Object, List<MarkovTransition<DFTState>>> groupedSuccTransitions = succTransitions
				.stream().collect(Collectors.groupingBy(MarkovTransition::getEvent, Collectors.toList()));
		
		for (Entry<Object, List<MarkovTransition<DFTState>>> succTransitionGroup : groupedSuccTransitions.entrySet()) {	
			double prob = 0;
			for (MarkovTransition<DFTState> succTransition : succTransitionGroup.getValue()) {
				prob += beliefState.mapStateToBelief.get(succTransition.getFrom());
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
	private double fillMarkovianStateSucc(BeliefState beliefState, BeliefState beliefSucc, 
			double exitRate, Entry<Set<Object>, Boolean> observationEvent, List<MarkovTransition<DFTState>> succTransitions) {
		boolean isInternalTransition = observationEvent.getKey().isEmpty();
		
		//Set<PODFTState> statesWithNoTransitions = new HashSet<>(beliefState.mapStateToBelief.keySet());
		
		if (isInternalTransition) {
			for (Entry<PODFTState, Double> entry : beliefState.mapStateToBelief.entrySet()) {
				beliefSucc.addBelief(entry.getKey(), entry.getValue() * EULERECIPROCAL);
			}
			for (MarkovTransition<DFTState> transition : succTransitions) {
				PODFTState fromState = (PODFTState) transition.getFrom();
				PODFTState toState = (PODFTState) transition.getTo();
				
				double oldProb = beliefState.mapStateToBelief.get(fromState);
				double prob = oldProb * transition.getRate() / exitRate;
				
				prob *= COEULERECIPROCAL;
				
				if (prob > 0) {
					toState = getTargetState(toState);
					beliefSucc.addBelief(toState, prob);
				}
			}
			return exitRate / COEULERECIPROCAL;
		} else {
			for (MarkovTransition<DFTState> transition : succTransitions) {
				PODFTState fromState = (PODFTState) transition.getFrom();
				PODFTState toState = (PODFTState) transition.getTo();
				
				double oldProb = beliefState.mapStateToBelief.get(fromState);
				double prob = oldProb * (oldProb * transition.getRate() / exitRate);
				
				if (prob > 0) {
					beliefSucc.addBelief(toState, prob);
				}
			}
		}

		/*for (MarkovTransition<DFTState> transition : succTransitions) {
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
					beliefSucc.addBelief(fromState, residueProb);
				}
			}
			
			if (prob > 0) {
				if (isInternalTransition) {
					toState = getTargetState(toState);
				}
				beliefSucc.addBelief(toState, prob);
			}
		}*/
		/*
		double totalExitProb = 0;
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
				totalExitProb += exitProb;
				residueProb *= remainProb;
				prob *= exitProb;
				
				if (residueProb > 0) {
					beliefSucc.addBelief(fromState, residueProb);
				}
			}
		}
		if (totalExitProb != 0) {
			System.out.println(totalExitProb);
			exitRate = exitRate / totalExitProb;
		}
		for (MarkovTransition<DFTState> transition : succTransitions) {
			PODFTState fromState = (PODFTState) transition.getFrom();
			PODFTState toState = (PODFTState) transition.getTo();
			statesWithNoTransitions.remove(fromState);
			
			double oldProb = beliefState.mapStateToBelief.get(fromState);
			double prob = oldProb * (oldProb * transition.getRate() / exitRate);
			
			if (prob > 0) {
				if (isInternalTransition) {
					toState = getTargetState(toState);
				}
				beliefSucc.addBelief(toState, prob);
			}
		}*/

		/*if (isInternalTransition) {
			for (PODFTState state : statesWithNoTransitions) {
				beliefSucc.addBelief(state, beliefState.mapStateToBelief.get(state));
			}
		}*/
		
		return exitRate;
	}

	/**
	 * Checks the target state and updates it if necessary
	 * @param toState the current target state
	 * @return the updated target state
	 */
	private PODFTState getTargetState(PODFTState toState) {
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
	private void fillNonDeterministicStateSucc(BeliefState beliefState, BeliefState beliefSucc, List<MarkovTransition<DFTState>> succTransitions) {
		for (MarkovTransition<DFTState> succTransition : succTransitions) {
			PODFTState succState = (PODFTState) succTransition.getTo();
			PODFTState fromState = (PODFTState) succTransition.getFrom();
			
			double prob = succTransition.getRate() * beliefState.mapStateToBelief.get(fromState);
			beliefSucc.mapStateToBelief.put(succState, prob);
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
		
		BeliefState equivalentBeliefState = beliefStateEquivalence.getEquivalentState(beliefState);
		boolean isNewState = beliefState == equivalentBeliefState;
		
		if (isNewState) {
			targetMa.addState(beliefState, beliefStateEquivalence.getMapValuesSetSize());
			
			double failProb = beliefState.getFailProb();
			if (failProb > 0) {
				beliefState.getMapFailLabelToProb().put(FailLabel.FAILED, failProb);
			}
			
			if (beliefState.representant.getFailLabels().contains(FailLabel.OBSERVED)) {
				beliefState.getMapFailLabelToProb().put(FailLabel.OBSERVED, 1d);
			}
		}
		
		return equivalentBeliefState;
	}
	
	/**
	 * Initializes the data of a successor of a probabilistic state
	 * @author khan_ax
	 * @param beliefState the current state
	 * @param beliefSucc the successor
	 * @param observationEvent the observation event
	 * @param succTransitions the successor transitions
	 */
	private double fillProbabilisticStateSucc(BeliefState beliefState, BeliefState beliefSucc, 
			Entry<Set<Object>, Boolean> observationEvent, List<MarkovTransition<DFTState>> succTransitions) {
		boolean isInternalTransition = observationEvent.getKey().isEmpty();
		
		HashSet<PODFTState> seenStates = new HashSet<>();
		
		double totalProb = 0;
		for (MarkovTransition<DFTState> transition : succTransitions) {
			PODFTState fromState = (PODFTState) transition.getFrom();
			PODFTState toState = (PODFTState) transition.getTo();
			
			double oldProb = beliefState.mapStateToBelief.get(fromState);
			double prob = oldProb * transition.getRate();
			
			if (isInternalTransition) {
				if (seenStates.add(fromState)) {
					beliefSucc.addBelief(fromState, oldProb);
					totalProb += oldProb;
				}
			} else if (prob > 0) {
				//toState = getTargetState(toState);
				beliefSucc.addBelief(toState, prob);
				totalProb += prob;
			}
		}
		
		return totalProb;
	}
	
	/**
	 * Makes a state a good if it is bad
	 * @author khan_ax
	 * @param state the bad state to be made good
	 */
	private void makeStateGood(BeliefState state, List<BeliefState> generatedSuccs) {
		if (badStates.containsKey(state)) {
			generatedSuccs.add(state);
			targetMa.addState(state, state.getIndex());
			for (MarkovTransition<BeliefState> transition : badStates.get(state)) {
				targetMa.addNondeterministicTransition(transition.getEvent(), transition.getFrom(), transition.getTo(), transition.getRate());
			}
			badStates.remove(state);
			goodStates.add(state);
		}
	}
}
