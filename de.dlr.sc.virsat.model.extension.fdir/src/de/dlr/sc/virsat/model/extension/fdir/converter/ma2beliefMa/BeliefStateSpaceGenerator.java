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
	
	private static final double DEFAULT_EPSILON = Math.exp(-1);
	private double epsilon = DEFAULT_EPSILON;
	
	private static final double EULERECIPROCAL = Math.exp(-1);
	private static final double COEULERECIPROCAL = 1 - EULERECIPROCAL;
	
	private MarkovAutomaton<DFTState> ma;
	private PODFTState initialStateMa;
	private BeliefStateEquivalence beliefStateEquivalence;
	
	private OptimalTransitionsSelector<BeliefState> optimalTransitionsSelector;
	private HashSet<BeliefState> goodStates;
	private HashSet<BeliefState> badStates;
	private boolean failStateSimplification = false;
	
	BeliefStateSpaceGenerator() {
	}
	
	
	/**
	 * Generates a BeliefStateSpaceGenerator
	 * @param filterTransitions whether to filter non-deterministic transitions
	 * @param simplifyFailStates whether to simplify fail states
	 */
	BeliefStateSpaceGenerator(boolean filterTransitions, boolean simplifyFailStates) {
		if (filterTransitions) {
			optimalTransitionsSelector = new OptimalTransitionsSelector<BeliefState>();
			goodStates = new HashSet<>();
			badStates = new HashSet<>();
		}
		if (simplifyFailStates) {
			failStateSimplification = true;
		}
	}
	
	BeliefStateSpaceGenerator(boolean filterTransitions, boolean simplifyFailStates, double epsilon) {
		this(filterTransitions, simplifyFailStates);
		this.epsilon = epsilon;
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
		beliefStateEquivalence = new BeliefStateEquivalence(epsilon);
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
			
			// Make sure type is up to date
			beliefSucc.setType(beliefSucc.representant.getType());
			
			BeliefState equivalentBeliefSucc = null;
			List<MarkovTransition<DFTState>> succTransitions = entry.getValue();
			
			if (beliefState.isProbabilisic()) {
				// Markovian ObservationEvents do not take place when the state is Probabilistic
				Object event = succTransitions.iterator().next().getEvent();
				if (event instanceof ObservationEvent && !(event instanceof ImmediateObservationEvent)) {
					continue;
				}
			}
			
			if (!beliefState.isNondet()) {
				Entry<Set<Object>, Boolean> observationEvent = beliefState.representant.extractObservationEvent(beliefSucc.representant, succTransitions);
				
				if (beliefState.isProbabilisic()) {
					equivalentBeliefSucc = generateProbabilisticSucc(beliefState, generatedSuccs, beliefSucc, succTransitions, observationEvent);
				} else {
					equivalentBeliefSucc = generateMarkovianSucc(beliefState, generatedSuccs, entry, beliefSucc, succTransitions, observationEvent);
				}
			} else {
				equivalentBeliefSucc = generateNondetSucc(beliefState, beliefSucc, succTransitions);
			}
			
			if (beliefSucc == equivalentBeliefSucc) {
				generatedSuccs.add(beliefSucc);
			}
		}
		
		if (optimalTransitionsSelector != null && beliefState.isNondet()) {
			generatedSuccs = applyOptimalTransitionSelector(beliefState, generatedSuccs);
		}

		if (failStateSimplification && !beliefState.isNondet()) {
			performFailStateSimplification(generatedSuccs);
		}
		
		return generatedSuccs;
	}

	/**
	 * Removes unnecessary fail state choices from the list of generated successors
	 * @param generatedSuccs the currently generated successors
	 */
	private void performFailStateSimplification(List<BeliefState> generatedSuccs) {
		List<BeliefState> suboptimalSuccs = new ArrayList<>();
		for (BeliefState generatedSucc : generatedSuccs) {
			boolean isSuboptimal = true;
			for  (PODFTState belief : generatedSucc.mapStateToBelief.keySet()) {
				// All Beliefs must be permanently failed in order for a BeliefState to be considered permanently failed
				if (!belief.isFaultTreeNodePermanent(belief.getFTHolder().getRoot())) {
					isSuboptimal = false;
					break;
				}
			}
			if (isSuboptimal) {
				suboptimalSuccs.add(generatedSucc);
				generatedSucc.setType(MarkovStateType.MARKOVIAN);
				// It can be that the generatedSucc is non-deterministic, which normally do not have fail labels
				generatedSucc.getMapFailLabelToProb().put(FailLabel.FAILED, 1.0);
			}
		}
		generatedSuccs.removeAll(suboptimalSuccs);
	}

	/**
	 * Applies the optimal transition selector to the available nondet choices
	 * @param beliefState the current belief state
	 * @param generatedSuccs the generated successors
	 * @return the successor states that pass the transition selector
	 */
	private List<BeliefState> applyOptimalTransitionSelector(BeliefState beliefState, List<BeliefState> generatedSuccs) {
		List<BeliefState> optimalSuccs = new ArrayList<>();
		List<MarkovTransition<BeliefState>> optimalTransitions = optimalTransitionsSelector.selectOptimalTransitions(targetMa, beliefState);
		for (MarkovTransition<BeliefState> optimalTransition : optimalTransitions) {
			BeliefState equivalentToState = beliefStateEquivalence.getEquivalentState(optimalTransition.getTo());
			optimalSuccs.add(equivalentToState);
			goodStates.add(equivalentToState);
			if (badStates.contains(equivalentToState)) {
				// It is possible that a previously bad state is the best transition for this non-deterministic state
				generatedSuccs.add(equivalentToState);
				badStates.remove(equivalentToState);
			}
		}
		for (BeliefState generatedSucc : generatedSuccs) {
			if (!goodStates.contains(generatedSucc)) {
				badStates.add(generatedSucc);
			}
		}
		generatedSuccs = generatedSuccs.stream().filter(optimalSuccs::contains).collect(Collectors.toList());
		return generatedSuccs;
	}


	/**
	 * Generates a nondeterministic successor state
	 * @param beliefState the current belief state
	 * @param beliefSucc the successor state to be filled with data
	 * @param succTransitions the current successor transitions
	 * @return the updated or an equivalent nondeterministic state
	 */
	private BeliefState generateNondetSucc(BeliefState beliefState, BeliefState beliefSucc,
			List<MarkovTransition<DFTState>> succTransitions) {
		BeliefState equivalentBeliefSucc;
		fillNonDeterministicStateSucc(beliefState, beliefSucc, succTransitions);
		beliefSucc.representant = getNewRepresentant(beliefSucc);
		beliefSucc.setType(beliefSucc.representant.getType());
		equivalentBeliefSucc = addBeliefState(beliefSucc);
		addNondeterministicTransitions(succTransitions, beliefState, equivalentBeliefSucc);
		return equivalentBeliefSucc;
	}

	/**
	 * Generates a markovian successor
	 * @param beliefState the current state
	 * @param generatedSuccs the currently generated successors
	 * @param entry the current list of markovian transitins in the MA
	 * @param beliefSucc the successor belief state to be filled with data
	 * @param succTransitions the generated successor transitions
	 * @param observationEvent the observation event for triggering the Markovian successor
	 * @return the generated belief state or an equivalent state
	 */
	private BeliefState generateMarkovianSucc(BeliefState beliefState, List<BeliefState> generatedSuccs,
			Entry<PODFTState, List<MarkovTransition<DFTState>>> entry, BeliefState beliefSucc,
			List<MarkovTransition<DFTState>> succTransitions, Entry<Set<Object>, Boolean> observationEvent) {
		BeliefState equivalentBeliefSucc;
		double exitRate = beliefState.getTotalRate(entry.getValue());
		exitRate = fillMarkovianStateSucc(beliefState, beliefSucc, exitRate, observationEvent, succTransitions);
		beliefSucc.representant = getNewRepresentant(beliefSucc);
		beliefSucc.setType(beliefSucc.representant.getType());
		equivalentBeliefSucc = addBeliefState(beliefSucc);
		if (beliefState != equivalentBeliefSucc) {
			makeStateGood(equivalentBeliefSucc, generatedSuccs);
			targetMa.addMarkovianTransition(observationEvent, beliefState, equivalentBeliefSucc, exitRate);
		}
		return equivalentBeliefSucc;
	}

	/**
	 * Generates a probablistic successor state
	 * @param beliefState the current belief state
	 * @param generatedSuccs the generated succesors
	 * @param beliefSucc the successor state to be filled
	 * @param succTransitions the generated successor transitions
	 * @param observationEvent the observed event
	 * @return the succesor belief state or an equivalent state
	 */
	private BeliefState generateProbabilisticSucc(BeliefState beliefState, List<BeliefState> generatedSuccs,
			BeliefState beliefSucc, List<MarkovTransition<DFTState>> succTransitions,
			Entry<Set<Object>, Boolean> observationEvent) {
		BeliefState equivalentBeliefSucc;
		if (observationEvent.getKey().isEmpty()) {
			// Internal Transition
			double totalProb = 0;
			totalProb = fillProbabilisticStateSucc(beliefState, beliefSucc, observationEvent, succTransitions);
			// Make sure representant is up to date
			beliefSucc.representant = getNewRepresentant(beliefSucc);
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
		return equivalentBeliefSucc;
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
				//statesWithNoTransitions.remove(fromState);
				
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
				double prob = oldProb * transition.getRate() / exitRate;
				
				if (prob > 0) {
					beliefSucc.addBelief(toState, prob);
				}
			}
		}
		
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
		if (optimalTransitionsSelector != null && badStates.contains(state)) {
			generatedSuccs.add(state);
			badStates.remove(state);
			goodStates.add(state);
		}
	}
	
	@Override
	public void removeBadStates() {
		if (optimalTransitionsSelector != null) {
			for (BeliefState badState : badStates) {
				targetMa.removeState(badState);
			}
		}
	}
	
	/**
	 * Returns a correct representant
	 * @author khan_ax
	 * @param beliefState the state for which a suitable representant is returned
	 */
	private PODFTState getNewRepresentant(BeliefState beliefState) {
		for (PODFTState candidate : beliefState.mapStateToBelief.keySet()) {
			if (candidate.isProbabilisic()) {
				// Probabilistic States can contain non-Probabilistic Beliefs and Probability take precedence
				return candidate;
			}
		}
		return beliefState.mapStateToBelief.keySet().iterator().next();
	}
}
