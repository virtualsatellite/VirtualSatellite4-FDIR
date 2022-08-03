/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IRepairableEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateFaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * Creates a Recovery Automaton representation of a given schedule
 * @author muel_s8
 *
 * @param <S>
 */

public class Schedule2RAConverter<S extends MarkovState> {

	private Map<MarkovState, State> mapMarkovStateToRaState;
	private MarkovAutomaton<S> ma;
	private Concept concept;
	
	private RecoveryAutomatonHelper raHelper;
	private RecoveryAutomaton ra;
	
	/**
	 * Standard constructor
	 * @param ma the markov automaton
	 * @param concept the concept
	 */
	public Schedule2RAConverter(MarkovAutomaton<S> ma, Concept concept) {
		this.ma = ma;
		this.concept = concept;
	}
	
	/**
	 * Performs the conversion
	 * @param schedule the schedule
	 * @param initialMa the initial state in the ma
	 * @param subMonitor a monitor
	 * @return a recovery automaton represenation of the given schedule
	 */
	public RecoveryAutomaton convert(Map<S, List<MarkovTransition<S>>> schedule, S initialMa, SubMonitor subMonitor) {
		mapMarkovStateToRaState = new HashMap<>();
		raHelper = new RecoveryAutomatonHelper(concept);
		ra = new RecoveryAutomaton(concept);
		
		subMonitor = SubMonitor.convert(subMonitor);
		
		Queue<S> toProcess = new LinkedList<>();
		
		toProcess.offer(initialMa);
		List<Transition> createdTransitions = new ArrayList<>();
		Set<S> handledNonDetStates = new HashSet<>();
		
		while (!toProcess.isEmpty()) {
			final int PROGRESS_COUNT = 100;
			subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
			
			S state = toProcess.poll();
			List<S> nextStates = handleState(state, schedule, createdTransitions);
			for (S nextState : nextStates) {
				if (handledNonDetStates.add(nextState)) {
					toProcess.offer(nextState);
				}
			}
		}
		
		ra.getTransitions().addAll(createdTransitions);
		ra.setInitial(getOrCreateRecoveryAutomatonState(ra, initialMa));
		
		return ra;
	}
	
	/**
	 * Creates or gets the RA state and transitions corresponding to the markov automaton state and outgoing transitions
	 * @param state the ma state to insert into the ra
	 * @param schedule the schedule
	 * @param createdTransitions the set of all created transitions
	 * @return the set of next ma states that should be inserted into the ra
	 */
	private List<S> handleState(S state, Map<S, List<MarkovTransition<S>>> schedule, List<Transition> createdTransitions) {
		List<MarkovTransition<S>> markovianTransitions = ma.getSuccTransitions(state);
		
		MarkovTransition<S> internalTransition = getInternalOutgoingTransition(state);
		if (internalTransition != null) {
			markovianTransitions = new ArrayList<>(markovianTransitions);
			createPseudoSynchronizationTransitions(state, internalTransition, markovianTransitions);
		}
		
		List<S> nextStates = new ArrayList<>();
		for (MarkovTransition<S> markovianTransition : markovianTransitions) {
			S toState = markovianTransition.getTo();
			
			Object event = markovianTransition.getEvent();
			if (event instanceof ImmediateFaultEvent) {
				ImmediateFaultEvent immediateEvent = (ImmediateFaultEvent) event;
				if (immediateEvent.isNegative()) {
					mapMarkovStateToRaState.put(toState, getOrCreateRecoveryAutomatonState(ra, state));
					nextStates.add(toState);
					continue;
				}
			}
			
			if (markovianTransition.getTo().isNondet()) {
				// Markovian and Probabilistic states do not have best transition groups
				List<MarkovTransition<S>> bestTransitionGroup = schedule.getOrDefault(toState, Collections.emptyList());
				MarkovTransition<S> representativeTransition = bestTransitionGroup.iterator().next();
				
				Transition raTransition = createTransition(markovianTransition, representativeTransition);
				createdTransitions.add(raTransition);
				
				for (MarkovTransition<S> bestTransition : bestTransitionGroup) {
					toState = bestTransition.getTo();
					mapMarkovStateToRaState.put(toState, raTransition.getTo());
					nextStates.add(toState);
				}
			} else {
				Transition raTransition = createTransition(markovianTransition, null);
				createdTransitions.add(raTransition);
				
				nextStates.add(toState);
			}
		}
		
		return nextStates;
	}
	
	/**
	 * Provide event synchronization for internal transitions:
	 * If an event that is only enabled for a later transition occurs,
	 * it means we have to synchronize the recovery automaton with this event.
	 * That means we need to create a corresponding transition in the recovery automaton
	 * to react to the event.
	 * @param state the state to create the pseudo transitions for
	 * @param the internal transition of the state
	 * @param markovianTransitions the current markovian transitions
	 */
	protected void createPseudoSynchronizationTransitions(S state, MarkovTransition<S> internalTransition, List<MarkovTransition<S>> markovianTransitions) {
		Set<S> internalStates = new HashSet<>();
		internalStates.add(state);
		Queue<MarkovTransition<S>> internalTransitionsToProcess = new LinkedList<>();
		internalTransitionsToProcess.add(internalTransition);
		
		while (!internalTransitionsToProcess.isEmpty()) {
			internalTransition = internalTransitionsToProcess.poll();
			
			if (internalTransition == null) {
				continue;
			}
			
			S internalState = internalTransition.getTo();
			
			if (internalStates.add(internalState)) {
				List<MarkovTransition<S>> internalStateSuccs = ma.getSuccTransitions(internalState);
				for (MarkovTransition<S> internalMarkovTransition : internalStateSuccs) {
					if (!isInternalTransition(internalMarkovTransition) 
							&& !hasEvent(internalMarkovTransition.getEvent(), markovianTransitions)) {
						MarkovTransition<S> pseudoTransition = internalMarkovTransition.copy();
						pseudoTransition.setFrom(state);
						markovianTransitions.add(pseudoTransition);
					}
				}
				
				internalTransitionsToProcess.add(getInternalOutgoingTransition(internalState));
			}
		}
	}
	
	/**
	 * Checks if a given transition is internal (no guards have been observed)
	 * @param markovianTransition the transition
	 * @return true iff the transition has no guards
	 */
	@SuppressWarnings("unchecked")
	protected boolean isInternalTransition(MarkovTransition<S> markovianTransition) {
		Object event = markovianTransition.getEvent();
		if (event instanceof Entry) {
			Entry<Collection<? extends FaultTreeNode>, Boolean> genericEvent = (Entry<Collection<? extends FaultTreeNode>, Boolean>) event;
			Collection<? extends FaultTreeNode> guards = genericEvent.getKey();
			return guards.isEmpty();
		}
		
		return false;
	}
	
	/**
	 * Checks for an internal outgoing transition
	 * @param state set of markovian state
	 * @return an internal transition if there is one, null otherwise
	 */
	protected MarkovTransition<S> getInternalOutgoingTransition(MarkovState state) {
		List<MarkovTransition<S>> markovianTransitions = ma.getSuccTransitions(state);
		for (MarkovTransition<S> markovianTransition : markovianTransitions) {
			if (isInternalTransition(markovianTransition)) {
				return markovianTransition;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if a given list of markov transitions has a transition for a specified event
	 * @param event the event
	 * @param markovTransitions the list of markov transitions
	 * @return true iff an event in the list corresponds to the given event
	 */
	protected boolean hasEvent(Object event, List<MarkovTransition<S>> markovTransitions) {		
		return markovTransitions.stream().anyMatch(transition -> transition.getEvent().equals(event));
	}

	/**
	 * Creates a recovery transition in the recovery automaton
	 * @param markovianTransition the markovian transition
	 * @param nondetTransition optional nondeterministic transition following the markovian transition
	 * @return the recovery transition
	 */
	protected Transition createTransition(MarkovTransition<S> markovianTransition, MarkovTransition<S> nondetTransition) {
		S fromState = markovianTransition.getFrom();
		S toState = nondetTransition == null ? markovianTransition.getTo() : nondetTransition.getTo();
		Object event = markovianTransition.getEvent();
		
		State fromRaState = getOrCreateRecoveryAutomatonState(ra, fromState);
		State toRaState = getOrCreateRecoveryAutomatonState(ra, toState);
		
		// Probabilistic states do not generate time outs
		Transition transition = isInternalTransition(markovianTransition) && !fromState.isProbabilisic()
				? createTimeoutTransition(fromRaState, toRaState, 1 / markovianTransition.getRate())
				: createFaultEventTransition(fromRaState, toRaState, event);

		if (nondetTransition != null) {
			@SuppressWarnings("unchecked")
			List<RecoveryAction> recoveryActions = (List<RecoveryAction>) nondetTransition.getEvent();
			for (RecoveryAction recoveryAction : recoveryActions)  {
				transition.getRecoveryActions().add(recoveryAction.copy());
			}
		}
		
		return transition;
	}
	
	/**
	 * Creates an RA timed transition corresponding to an internal Markovian Transition
	 * @param fromRaState the from state
	 * @param toRaState the to state
	 * @param time the time
	 * @return the created RA transition
	 */
	protected TimeoutTransition createTimeoutTransition(State fromRaState, State toRaState, double time) {
		TimeoutTransition raTransition = raHelper.createTimeoutTransition(ra, fromRaState, toRaState, time);
		raTransition.setName(fromRaState.getName() + toRaState.getName());
		return raTransition;
	}
	
	/**
	 * Creates an RA transition corresponding to a Markovian Transition
	 * @param fromRaState the from state
	 * @param toRaState the to state
	 * @param event the event
	 * @return the created RA transition
	 */
	@SuppressWarnings("unchecked")
	protected FaultEventTransition createFaultEventTransition(State fromRaState, State toRaState, Object event) {
		FaultEventTransition raTransition = raHelper.createFaultEventTransition(ra, fromRaState, toRaState);
		
		if (event instanceof Entry) {
			Entry<Collection<? extends FaultTreeNode>, Boolean> genericEvent = (Entry<Collection<? extends FaultTreeNode>, Boolean>) event;
			raTransition.getGuards().addAll(genericEvent.getKey());
			raTransition.setIsRepair(genericEvent.getValue());
		} else if (event instanceof IDFTEvent) {
			IDFTEvent dftEvent = (IDFTEvent) event;
			raTransition.getGuards().addAll(dftEvent.getNodes());
			
			if (event instanceof IRepairableEvent) {
				raTransition.setIsRepair(((IRepairableEvent) event).isRepair());
			}
		}
		
		raTransition.setName(fromRaState.getName() + toRaState.getName());
		return raTransition;
	}
	
	/**
	 * Gets the recovery automaton state corresponding to the markov automaton state
	 * or creates a new one if one doesnt exist yet
	 * @param ra the recovery automaton
	 * @param maState the markov automaton state
	 * @return a recovery automaton state corresponding to the markov automaton state
	 */
	protected State getOrCreateRecoveryAutomatonState(RecoveryAutomaton ra, MarkovState maState) {
		State raState = mapMarkovStateToRaState.get(maState);
		if (raState == null) {
			raState = new State(concept);
			raState.setName("q_" + ra.getStates().size()); 
			mapMarkovStateToRaState.put(maState, raState);
			ra.getStates().add(raState);
		}
		
		return raState;
	}
}
