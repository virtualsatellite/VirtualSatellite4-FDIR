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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

public class StateHolder {
	private RecoveryAutomatonHolder raHolder;
	private State state;
	private List<Transition> outgoingTransitions;
	private List<Transition> incomingTransitions;
	private TimeoutTransition timeoutTransition;
	private Map<Set<FaultTreeNode>, String> guardProfile;
	
	public StateHolder(RecoveryAutomatonHolder raHolder, State state) {
		this.raHolder = raHolder;
		this.state = state;
		this.incomingTransitions = new ArrayList<>();
		this.outgoingTransitions = new ArrayList<>();
	}
	
	public State getState() {
		return state;
	}
	
	public List<Transition> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	
	public List<Transition> getIncomingTransitions() {
		return incomingTransitions;
	}
	
	public TimeoutTransition getTimeoutTransition() {
		return timeoutTransition;
	}
	
	public void setTimeoutTransition(TimeoutTransition timeoutTransition) {
		this.timeoutTransition = timeoutTransition;
	}
	
	/**
	 * A guard profile is the mapping from guards to action labels of a transition.
	 * Gets the guard profile for a transition.
	 * If it doesnt exist yet, a new one will be created.
	 * @return the guard profile for the transition
	 */
	public Map<Set<FaultTreeNode>, String> getGuardProfile() {
		if (guardProfile == null) {
			guardProfile = new HashMap<>();
			for (Transition transition : outgoingTransitions) {
				TransitionHolder transitionHolder = raHolder.getTransitionHolder(transition);
				String actionLabels = transitionHolder.getActionLabel();
				if (!actionLabels.isEmpty()) {
					guardProfile.put(transitionHolder.getGuards(), actionLabels);
				}
			}
		}
		return guardProfile;
	}
}
