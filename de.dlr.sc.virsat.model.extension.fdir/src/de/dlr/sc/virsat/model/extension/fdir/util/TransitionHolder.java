/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

public class TransitionHolder {
	private RecoveryAutomatonHolder raHolder;
	private Transition transition;
	private Set<FaultTreeNode> guards;
	private String actionLabel;
	private List<RecoveryAction> recoveryActions;
	private State to;
	private State from;
	
	/**
	 * Standard constructor
	 * @param raHolder the ra holder
	 * @param transition the transition to hold
	 */
	public TransitionHolder(RecoveryAutomatonHolder raHolder, Transition transition) {
		this.raHolder = raHolder;
		this.transition = transition;
		this.to = transition.getTo();
		this.from = transition.getFrom();
		this.actionLabel = transition.getActionLabels();
		
		this.recoveryActions = new ArrayList<>();
		for (RecoveryAction recoveryAction : transition.getRecoveryActions()) {
			this.recoveryActions.add(recoveryAction);
		}
		
		if (transition instanceof TimeoutTransition) {
			TimeoutTransition timeoutTransition = (TimeoutTransition) transition;
			StateHolder fromStateHolder = raHolder.getMapStateToStateHolder().get(timeoutTransition.getFrom());
			fromStateHolder.setTimeoutTransition(timeoutTransition);
		} else if (transition instanceof FaultEventTransition) {
			this.guards = new HashSet<>(((FaultEventTransition) transition).getGuards());
		}
	}

	public Transition getTransition() {
		return transition;
	}
	
	public Set<FaultTreeNode> getGuards() {
		return guards;
	}
	
	public List<RecoveryAction> getRecoveryActions() {
		return recoveryActions;
	}
	
	public State getTo() {
		return to;
	}
	
	public State getFrom() {
		return from;
	}
	
	/**
	 * Sets the from state of the transition and updates all
	 * data structures to reflect the change.
	 * @param state the new from state
	 */
	public void setFrom(State state) {
		StateHolder stateHolder = raHolder.getStateHolder(from);
		stateHolder.getOutgoingTransitions().remove(transition);
		
		if (transition instanceof TimeoutTransition) {
			stateHolder.setTimeoutTransition(null);
		}
		
		stateHolder = raHolder.getStateHolder(state);
		stateHolder.getOutgoingTransitions().add(transition);
		
		if (transition instanceof TimeoutTransition) {
			stateHolder.setTimeoutTransition((TimeoutTransition) transition);
		}
		
		this.from = state;
		transition.setFrom(state);
	}
	
	/**
	 * Sets the to state of the transition and updates all
	 * data structures to reflect the change.
	 * @param state the new to state
	 */
	public void setTo(State state) {
		StateHolder stateHolder = raHolder.getStateHolder(to);
		stateHolder.getIncomingTransitions().remove(transition);
		
		stateHolder = raHolder.getStateHolder(state);
		stateHolder.getIncomingTransitions().add(transition);
		
		this.to = state;
		transition.setTo(state);
	}
	
	public String getActionLabel() {
		return actionLabel;
	}
}
