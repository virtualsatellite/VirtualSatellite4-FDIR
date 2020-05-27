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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
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
	
	private Map<State, List<Transition>> mapStateToOutgoingTransitions;
	private Map<State, List<Transition>> mapStateToIncomingTransitions;
	private Map<State, TimeoutTransition> mapStateToTimeoutTransition;
	private Map<Transition, Set<FaultTreeNode>> mapTransitionToGuards;
	private Map<Transition, String> mapTransitionToActionLabels;
	private Map<Transition, List<RecoveryAction>> mapTransitionToRecoveryActions;
	private Map<Transition, State> mapTransitionToTo;
	private Map<State, Map<Set<FaultTreeNode>, String>> mapStateToGuardProfile;
	private List<Transition> transitions;
	
	/**
	 * Standard constructor
	 * @param ra the recovery automaton
	 */
	public RecoveryAutomatonHolder(RecoveryAutomaton ra) {
		this.ra = ra;
		this.raHelper = new RecoveryAutomatonHelper(ra.getConcept());
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
	 * Gets the transitions of the wrapped recovery automaton
	 * @return the transitions
	 */
	public List<Transition> getTransitions() {
		if (transitions == null) {
			transitions = new ArrayList<>();
			for (Transition transition : ra.getTransitions()) {
				transitions.add(transition);
			}
		}
		return transitions;
	}
	
	/**
	 * Gets a map from a transition to its action labels
	 * @return a mapping from a transition to its action labels
	 */
	public Map<Transition, String> getMapTransitionToActionLabels() {
		if (mapTransitionToActionLabels == null) {
			mapTransitionToActionLabels = new HashMap<>();
			for (Transition transition : getTransitions()) {
				mapTransitionToActionLabels.put(transition, transition.getActionLabels());
			}
		}
		return mapTransitionToActionLabels;
	}
	
	/**
	 * Gets a map from a transition to its action labels
	 * @return a mapping from a transition to its action labels
	 */
	public Map<Transition, List<RecoveryAction>> getMapTransitionToRecoveryActions() {
		if (mapTransitionToRecoveryActions == null) {
			mapTransitionToRecoveryActions = new HashMap<>();
			for (Transition transition : getTransitions()) {
				List<RecoveryAction> recoveryActions = new ArrayList<>();
				for (RecoveryAction recoveryAction : transition.getRecoveryActions()) {
					recoveryActions.add(recoveryAction);
				}
				mapTransitionToRecoveryActions.put(transition, recoveryActions);
			}
		}
		return mapTransitionToRecoveryActions;
	}
	
	/**
	 * Gets a map from a state to its incoming transitions
	 * @return a map from a state to its incoming transitions
	 */
	public Map<State, List<Transition>> getMapStateToIncomingTransitions() {
		if (mapStateToIncomingTransitions == null) {
			mapStateToIncomingTransitions = raHelper.getPreviousTransitions(ra);
		}
		return mapStateToIncomingTransitions;
	}
	
	/**
	 * Gets a map from a state to its outgoing transitions
	 * @return a map from a state to its outgoing transitions
	 */
	public Map<State, List<Transition>> getMapStateToOutgoingTransitions() {
		if (mapStateToOutgoingTransitions == null) {
			mapStateToOutgoingTransitions = raHelper.getCurrentTransitions(ra);
		}
		return mapStateToOutgoingTransitions;
	}
	
	/**
	 * Gets a map from a state to its timeout transition
	 * @return a map from a state to its timeout transition
	 */
	public Map<State, TimeoutTransition> getMapStateToTimeoutTransition() {
		if (mapStateToTimeoutTransition == null) {
			mapStateToTimeoutTransition = new HashMap<State, TimeoutTransition>();
			for (State state : ra.getStates()) {
				mapStateToTimeoutTransition.put(state, raHelper.getTimeoutTransition(ra, state));
			}
		}
		return mapStateToTimeoutTransition;
	}
	
	/**
	 * Gets a map from a transition to its guards
	 * @return a map from a transition to its guards
	 */
	public Map<Transition, Set<FaultTreeNode>> getMapTransitionToGuards() {
		if (mapTransitionToGuards == null) {
			mapTransitionToGuards = new HashMap<>();
			
			for (Transition transition : getTransitions()) {
				if (transition instanceof FaultEventTransition) {
					mapTransitionToGuards.put(transition, new HashSet<>(((FaultEventTransition) transition).getGuards()));
				}
			}
		}
		return mapTransitionToGuards;
	}
	
	/**
	 * Gets a map from a transition to its target state
	 * @return a map from a transition to its target state
	 */
	public Map<Transition, State> getMapTransitionToTo() {
		if (mapTransitionToTo == null) {
			mapTransitionToTo = new HashMap<>();
			
			for (Transition transition : getTransitions()) {
				mapTransitionToTo.put(transition, transition.getTo());
			}
		}
		
		return mapTransitionToTo;
	}
	
	/**
	 * Gets a map from a state to its input output mapping
	 * @return a map from a state to its input output mapping
	 */
	public Map<State, Map<Set<FaultTreeNode>, String>> getMapStateToGuardProfile() {
		if (mapStateToGuardProfile == null) {
			mapStateToGuardProfile = new HashMap<>();
			
			for (State state : ra.getStates()) {
				Map<Set<FaultTreeNode>, String> mapGuardToActionLabel = new HashMap<>();
				List<Transition> outgoingTransitions = getMapStateToOutgoingTransitions().getOrDefault(state, Collections.emptyList());
				for (Transition transition : outgoingTransitions) {
					String actionLabels = getMapTransitionToActionLabels().get(transition);
					if (!actionLabels.isEmpty()) {
						mapGuardToActionLabel.put(getMapTransitionToGuards().get(transition), actionLabels);
					}
				}
				mapStateToGuardProfile.put(state, mapGuardToActionLabel);
			}
		}
		return mapStateToGuardProfile;
	}
}
