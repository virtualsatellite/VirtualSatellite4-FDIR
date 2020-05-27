/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Class for holding data for RAs to avoid recomputations
 * @author muel_s8
 *
 */

public class RecoveryAutomatonHolder {
	private RecoveryAutomaton ra;
	private RecoveryAutomatonHelper raHelper;
	
	private Map<State, StateHolder> mapStateToStateHolder = new HashMap<State, StateHolder>();
	private Map<Transition, TransitionHolder> mapTransitionToTransitionHolder = new HashMap<Transition, TransitionHolder>();
	
	/**
	 * Standard constructor
	 * @param ra the recovery automaton
	 */
	public RecoveryAutomatonHolder(RecoveryAutomaton ra) {
		this.ra = ra;
		this.raHelper = new RecoveryAutomatonHelper(ra.getConcept());
		
		Map<State, List<Transition>> mapStateToIncomingTransitions = raHelper.getPreviousTransitions(ra);
		Map<State, List<Transition>> mapStateToOutgoingTransitions = raHelper.getCurrentTransitions(ra);

		for (State state : ra.getStates()) {
			StateHolder stateHolder = new StateHolder(this, state);
			stateHolder.getIncomingTransitions().addAll(mapStateToIncomingTransitions.get(state));
			stateHolder.getOutgoingTransitions().addAll(mapStateToOutgoingTransitions.get(state));
			mapStateToStateHolder.put(state, stateHolder);
		}
		
		for (Transition transition : ra.getTransitions()) {
			TransitionHolder transitionHolder = new TransitionHolder(this, transition);
			mapTransitionToTransitionHolder.put(transition, transitionHolder);
		}
	}
	
	/**
	 * Gives access to the underlying recovery automaton
	 * @return the recovery automaton
	 */
	public RecoveryAutomaton getRa() {
		return ra;
	}
	
	/**
	 * Gives access to the underyling ra helper
	 * @return the ra helper
	 */
	public RecoveryAutomatonHelper getRaHelper() {
		return raHelper;
	}
	
	/**
	 * Gets the underlying mapping from states to state holdes
	 * @return the mapping
	 */
	public Map<State, StateHolder> getMapStateToStateHolder() {
		return mapStateToStateHolder;
	}
	
	/**
	 * Gets the underlying mapping from transitions to transition holders
	 * @return the mapping
	 */
	public Map<Transition, TransitionHolder> getMapTransitionToTransitionHolder() {
		return mapTransitionToTransitionHolder;
	}
	
	/**
	 * Gets the state holder for a state
	 * @param state the state
	 * @return the holder of the state
	 */
	public StateHolder getStateHolder(State state) {
		return mapStateToStateHolder.get(state);
	}
	
	/**
	 * Gets the transition holder for a transition
	 * @param transition the transition
	 * @return the holder of the transition
	 */
	public TransitionHolder getTransitionHolder(Transition transition) {
		return mapTransitionToTransitionHolder.get(transition);
	}
	
	/**
	 * Removes a set of states. Doesnt do any further
	 * updating of the holder structures.
	 * @param states the states to remove
	 */
	public void removeStates(Collection<State> states) {
		ra.getStates().removeAll(states);
		mapStateToStateHolder.keySet().removeAll(states);
	}
	
	/**
	 * Removes a set of transitions and updates all holder structures
	 * to reflect the changes
	 * @param transitions the transitions to remove
	 */
	public void removeTransitions(Collection<Transition> transitions) {
		for (Transition transition : transitions) {
			TransitionHolder transitionHolder = getTransitionHolder(transition);
			
			StateHolder stateHolderFrom = getStateHolder(transitionHolder.getFrom());
			StateHolder stateHolderTo = getStateHolder(transitionHolder.getTo());
			
			if (stateHolderFrom != null) {
				stateHolderFrom.getOutgoingTransitions().remove(transition);
				
				if (transition instanceof TimeoutTransition) {
					stateHolderFrom.setTimeoutTransition(null);
				}
			}
			
			if (stateHolderTo != null) {
				stateHolderTo.getIncomingTransitions().remove(transition);
			}
		}
		
		ra.getTransitions().removeAll(transitions);
		mapTransitionToTransitionHolder.keySet().removeAll(transitions);
	}
}
