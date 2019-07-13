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
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimedTransition;
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
	 * @return a recovery automaton represenation of the given schedule
	 */
	@SuppressWarnings("unchecked")
	public RecoveryAutomaton convert(Map<S, Set<MarkovTransition<S>>> schedule, S initialMa) {
		mapMarkovStateToRaState = new HashMap<>();
		
		raHelper = new RecoveryAutomatonHelper(concept);
		ra = new RecoveryAutomaton(concept);
		
		Queue<MarkovState> toProcess = new LinkedList<>();
		toProcess.offer(initialMa);
		List<Transition> createdTransitions = new ArrayList<>();
		Set<MarkovState> handledNonDetStates = new HashSet<>();
		
		while (!toProcess.isEmpty()) {
			MarkovState state = toProcess.poll();
			
			List<MarkovTransition<S>> markovianTransitions = ma.getSuccTransitions(state);
			for (MarkovTransition<S> markovianTransition : markovianTransitions) {
				Object event = markovianTransition.getEvent();
				S fromState = markovianTransition.getFrom();
				S toState = markovianTransition.getTo();
				
				if (!markovianTransition.getTo().isMarkovian()) {
					Set<MarkovTransition<S>> bestTransitionSet = schedule.getOrDefault(toState, Collections.EMPTY_SET);
					
					for (MarkovTransition<S> bestTransition : bestTransitionSet) {
						toState = bestTransition.getTo();
						FaultEventTransition raTransition = createFaultEventTransition(fromState, toState, event);
					
						List<RecoveryAction> recoveryActions = (List<RecoveryAction>) bestTransition.getEvent();
						for (RecoveryAction recoveryAction : recoveryActions)  {
							raTransition.getRecoveryActions().add(raHelper.copyRecoveryAction(recoveryAction));
						}
						
						createdTransitions.add(raTransition);
						
						if (handledNonDetStates.add(toState)) {
							toProcess.offer(toState);
						} 
					}
				} else {
					if (event instanceof Collection<?>) {
						Collection<? extends FaultTreeNode> guards = (Collection<? extends FaultTreeNode>) event;
						if (guards.isEmpty()) {
							createdTransitions.add(createTimedTransition(fromState, toState, 1 / markovianTransition.getRate()));
							
							if (handledNonDetStates.add(toState)) {
								toProcess.offer(toState);
							}
							
							continue;
						}
					}
					
					createdTransitions.add(createFaultEventTransition(fromState, toState, event));
					
					if (handledNonDetStates.add(toState)) {
						toProcess.offer(toState);
					} 
				}
			}
		}
		
		ra.getTransitions().addAll(createdTransitions);
		ra.setInitial(getOrCreateRecoveryAutomatonState(ra, initialMa));
		
		return ra;
	}
	
	/**
	 * Checks for internal transitions
	 * @param markovianTransitions set of markovian transitions
	 * @return an internal transition if there is one, null otherwise
	 */
	@SuppressWarnings("unchecked")
	protected MarkovTransition<S> getInternalTransition(List<MarkovTransition<S>> markovianTransitions) {
		for (MarkovTransition<S> markovianTransition : markovianTransitions) {
			Object event = markovianTransition.getEvent();
			if (event instanceof Collection<?>) {
				Collection<? extends FaultTreeNode> guards = (Collection<? extends FaultTreeNode>) event;
				if (guards.isEmpty()) {
					return markovianTransition;
				} 
			}
		}
		
		return null;
	}

	/**
	 * Creates an RA timed transition corresponding to an internal Markovian Transition
	 * @param from the from state
	 * @param to the to state
	 * @param time the time
	 * @return the created RA transition
	 */
	protected TimedTransition createTimedTransition(S from, S to, double time) {
		State fromRaState = getOrCreateRecoveryAutomatonState(ra, from);
		State toRaState = getOrCreateRecoveryAutomatonState(ra, to);
		TimedTransition raTransition = raHelper.createTimedTransition(ra, fromRaState, toRaState, time);
		raTransition.setName(fromRaState.getName() + toRaState.getName());
		return raTransition;
	}
	
	/**
	 * Creates an RA transition corresponding to a Markovian Transition
	 * @param from the from state
	 * @param to the to state
	 * @param event the event
	 * @return the created RA transition
	 */
	@SuppressWarnings("unchecked")
	protected FaultEventTransition createFaultEventTransition(S from, S to, Object event) {
		State fromRaState = getOrCreateRecoveryAutomatonState(ra, from);
		State toRaState = getOrCreateRecoveryAutomatonState(ra, to);
		FaultEventTransition raTransition = raHelper.createFaultEventTransition(ra, fromRaState, toRaState);
		
		if (event instanceof Collection<?>) {
			raTransition.getGuards().addAll((Collection<? extends FaultTreeNode>) event);
		} else {
			raTransition.getGuards().add(((IDFTEvent) event).getNode());
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
