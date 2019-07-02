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
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
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
		
		RecoveryAutomatonHelper raHelper = new RecoveryAutomatonHelper(concept);
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		
		Queue<MarkovState> toProcess = new LinkedList<>();
		toProcess.offer(initialMa);
		List<Transition> createdTransitions = new ArrayList<>();
		Set<MarkovState> handledNonDetStates = new HashSet<>();
		
		while (!toProcess.isEmpty()) {
			MarkovState state = toProcess.poll();
			
			List<MarkovTransition<S>> markovianTransitions = ma.getSuccTransitions(state);
			
			boolean hasNonInternalTransition = false;
			for (MarkovTransition<S> markovianTransition : markovianTransitions) {
				Object event = markovianTransition.getEvent();
				if (event instanceof Collection<?>) {
					Collection<? extends FaultTreeNode> guards = (Collection<? extends FaultTreeNode>) event;
					if (!guards.isEmpty()) {
						hasNonInternalTransition = true;
						break;
					} else {
						hasNonInternalTransition = true;
					}
				}
			}
			
			for (MarkovTransition<S> markovianTransition : markovianTransitions) {
				Object event = markovianTransition.getEvent();
				if (hasNonInternalTransition && event instanceof Collection<?>) {
					Collection<? extends FaultTreeNode> guards = (Collection<? extends FaultTreeNode>) event;
					if (guards.isEmpty()) {
						mapMarkovStateToRaState.put(markovianTransition.getTo(), getOrCreateRecoveryAutomatonState(ra, state));
						
						if (handledNonDetStates.add(markovianTransition.getTo())) {
							toProcess.offer(markovianTransition.getTo());
						} 
						continue;
					}
				}
				
				if (!markovianTransition.getTo().isMarkovian()) {
					Set<MarkovTransition<S>> bestTransitionSet = schedule.getOrDefault(markovianTransition.getTo(), Collections.EMPTY_SET);
					
					for (MarkovTransition<S> bestTransition : bestTransitionSet) {
						Transition raTransition = new Transition(concept);
						State fromRaState = getOrCreateRecoveryAutomatonState(ra, markovianTransition.getFrom());
						State toRaState = getOrCreateRecoveryAutomatonState(ra, bestTransition.getTo());
						raTransition.setFrom(fromRaState);
						raTransition.setTo(toRaState);
						if (event instanceof Collection<?>) {
							raTransition.getGuards().addAll((Collection<? extends FaultTreeNode>) event);
						} else {
							raTransition.getGuards().add(((IDFTEvent) event).getNode());
						}
						raTransition.setName(fromRaState.getName() + toRaState.getName());
						
						List<RecoveryAction> recoveryActions = (List<RecoveryAction>) bestTransition.getEvent();
						for (RecoveryAction recoveryAction : recoveryActions)  {
							raTransition.getRecoveryActions().add(raHelper.copyRecoveryAction(recoveryAction));
						}
						createdTransitions.add(raTransition);
						
						MarkovState nextState = bestTransition.getTo();
						if (handledNonDetStates.add(nextState)) {
							toProcess.offer(nextState);
						} 
					}
				} else {
					Transition raTransition = new Transition(concept);
					State fromRaState = getOrCreateRecoveryAutomatonState(ra, markovianTransition.getFrom());
					State toRaState = getOrCreateRecoveryAutomatonState(ra, markovianTransition.getTo());
					raTransition.setFrom(fromRaState);
					raTransition.setTo(toRaState);
					if (event instanceof Collection<?>) {
						raTransition.getGuards().addAll((Collection<? extends FaultTreeNode>) event);
					} else {
						raTransition.getGuards().add(((IDFTEvent) event).getNode());
					}
					raTransition.setName(fromRaState.getName() + toRaState.getName());
					createdTransitions.add(raTransition);
					
					S nextState = markovianTransition.getTo();
					if (handledNonDetStates.add(nextState)) {
						toProcess.offer(nextState);
					} 
				}
			}
		}
		
		ra.getTransitions().addAll(createdTransitions);
		ra.setInitial(getOrCreateRecoveryAutomatonState(ra, initialMa));
		
		return ra;
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
