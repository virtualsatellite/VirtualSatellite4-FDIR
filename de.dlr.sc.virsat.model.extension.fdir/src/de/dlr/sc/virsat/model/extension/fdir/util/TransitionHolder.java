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
	private Edge edge;
	private Label label;
	
	/**
	 * Standard constructor
	 * @param raHolder the ra holder
	 * @param transition the transition to hold
	 */
	public TransitionHolder(RecoveryAutomatonHolder raHolder, Transition transition) {
		this.raHolder = raHolder;
		this.transition = transition;
		this.edge = new Edge();
		this.label = new Label();
		
		if (transition instanceof TimeoutTransition) {
			TimeoutTransition timeoutTransition = (TimeoutTransition) transition;
			StateHolder fromStateHolder = raHolder.getMapStateToStateHolder().get(timeoutTransition.getFrom());
			fromStateHolder.setTimeoutTransition(timeoutTransition);
		}
	}

	public Transition getTransition() {
		return transition;
	}
	
	public Set<FaultTreeNode> getGuards() {
		return label.guards;
	}
	
	public List<RecoveryAction> getRecoveryActions() {
		return label.recoveryActions;
	}
	
	public State getTo() {
		return edge.to;
	}
	
	public State getFrom() {
		return edge.from;
	}
	
	public String getActionLabel() {
		return label.actionLabel;
	}
	
	public String getGuardLabel() {
		return label.guardLabel;
	}
	
	public boolean isLoop() {
		return edge.to.equals(edge.from);
	}
	
	public boolean isEpsilonTransition() {
		return label.recoveryActions.isEmpty();
	}
	
	public boolean isEpsilonLoop() {
		return isLoop() && isEpsilonTransition();
	}
	
	/**
	 * Checks whether the transition held here is equivalent to the transition held by another holder
	 * @param other the other transition holder
	 * @return true iff they are equivalent
	 */
	public boolean isEquivalent(TransitionHolder other) {
		return edge.isEquivalent(other.edge) && label.isEquivalent(other.label);
	}
	
	/**
	 * Sets the from state of the transition and updates all
	 * data structures to reflect the change.
	 * @param state the new from state
	 */
	public void setFrom(State state) {
		StateHolder stateHolder = raHolder.getStateHolder(edge.from);
		stateHolder.getOutgoingTransitions().remove(transition);
		
		if (transition instanceof TimeoutTransition) {
			stateHolder.setTimeoutTransition(null);
		}
		
		stateHolder = raHolder.getStateHolder(state);
		stateHolder.getOutgoingTransitions().add(transition);
		
		if (transition instanceof TimeoutTransition) {
			stateHolder.setTimeoutTransition((TimeoutTransition) transition);
		}
		
		edge.from = state;
		transition.setFrom(state);
	}
	
	/**
	 * Sets the to state of the transition and updates all
	 * data structures to reflect the change.
	 * @param state the new to state
	 */
	public void setTo(State state) {
		StateHolder stateHolder = raHolder.getStateHolder(edge.to);
		stateHolder.getIncomingTransitions().remove(transition);
		
		stateHolder = raHolder.getStateHolder(state);
		stateHolder.getIncomingTransitions().add(transition);
		
		edge.to = state;
		transition.setTo(state);
	}
	
	private class Edge {
		private State to;
		private State from;
		
		/**
		 * Standard constructor.
		 */
		Edge() {
			this.to = transition.getTo();
			this.from = transition.getFrom();
		}
		
		public boolean isEquivalent(Edge other) {
			return to.equals(other.to) && from.equals(other.from);
		}
	}
	
	private class Label {
		private Set<FaultTreeNode> guards;
		private String actionLabel;
		private String guardLabel;
		private List<RecoveryAction> recoveryActions;
		
		/**
		 * Standard constructor.
		 */
		Label() {
			this.actionLabel = transition.getActionLabel();
			this.guardLabel = transition.getGuardLabel();
			
			this.recoveryActions = new ArrayList<>();
			for (RecoveryAction recoveryAction : transition.getRecoveryActions()) {
				this.recoveryActions.add(recoveryAction);
			}
			
			if (transition instanceof FaultEventTransition) {
				this.guards = new HashSet<>(((FaultEventTransition) transition).getGuards());
			}
		}
		
		public boolean isEquivalent(Label other) {
			return guardLabel.equals(other.guardLabel) && actionLabel.equals(other.actionLabel);
		}
	}
}
