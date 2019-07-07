/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.TimeEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimedTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Wrapper turning a recovery automaton into an actual recovery strategy.
 * @author muel_s8
 *
 */

public class RecoveryStrategy {
	
	private List<RecoveryAction> recoveryAction;
	private RecoveryAutomaton ra;
	private State currentState;
	private Map<State, Set<Transition>> mapStateToOutTransitions;
	
	/**
	 * Default constructor.
	 */
	
	private RecoveryStrategy() {
		
	}
	
	/**
	 * Constructor for wrapping a recovery automaton
	 * @param ra the recovery automaton to wrap
	 */
	
	public RecoveryStrategy(RecoveryAutomaton ra) {
		this.ra = ra;
		
		mapStateToOutTransitions = new HashMap<>();
		
		for (State state : ra.getStates()) {
			mapStateToOutTransitions.put(state, new HashSet<>());
		}
		
		for (Transition transition : ra.getTransitions()) {
			mapStateToOutTransitions.get(transition.getFrom()).add(transition);
		}
		
		currentState = ra.getInitial();
		recoveryAction = new ArrayList<>();
	}
	
	/**
	 * Gets the current state
	 * @return the current state
	 */
	public State getCurrentState() {
		return currentState;
	}
	
	@Override
	public String toString() {
		return currentState.toString();
	}

	/**
	 * React to the occurrence of a set of faults
	 * @param faults the occurred faults
	 * @return the recovery strategy after reading the fault
	 */
	public RecoveryStrategy onFaultsOccured(Collection<FaultTreeNode> faults) {
		if (mapStateToOutTransitions.get(currentState) != null) {
			Set<String> faultUUIDs = faults.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
			for (Transition transition : mapStateToOutTransitions.get(currentState)) {
				if (transition instanceof FaultEventTransition) {
					Set<String> guardUUIDs = ((FaultEventTransition) transition).getGuards()
							.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
					if (guardUUIDs.equals(faultUUIDs)) {
						RecoveryStrategy ras = new RecoveryStrategy();
						ras.ra = ra;
						ras.currentState = transition.getTo();
						ras.recoveryAction = transition.getRecoveryActions();
						ras.mapStateToOutTransitions = mapStateToOutTransitions;
						return ras;
					}
				}
			}
		}
		
		return this;
	}

	/**
	 * React to the occurence of a time event
	 * @param time the time event
	 * @return the recovery strategy after reading the time event
	 */
	public RecoveryStrategy onTime(double time) {
		if (mapStateToOutTransitions.get(currentState) != null) {
			for (Transition transition : mapStateToOutTransitions.get(currentState)) {
				if (transition instanceof TimedTransition) {
					TimedTransition timedTransition = (TimedTransition) transition;
					if (timedTransition.getTimeBean().getValueToBaseUnit() == time) {
						RecoveryStrategy ras = new RecoveryStrategy();
						ras.ra = ra;
						ras.currentState = transition.getTo();
						ras.recoveryAction = transition.getRecoveryActions();
						ras.mapStateToOutTransitions = mapStateToOutTransitions;
						return ras;
					}
				}
			}
		}
		
		return this;
	}

	/**
	 * Creates a set of events that can occur from the recovery side
	 * @return the set of events
	 */
	public List<IDFTEvent> createEventSet() {
		List<IDFTEvent> timeEvents = new ArrayList<>();
		for (Entry<State, Set<Transition>> entry : mapStateToOutTransitions.entrySet()) {
			for (Transition transition : entry.getValue()) {
				if (transition instanceof TimedTransition) {
					TimedTransition timedTranstion = (TimedTransition) transition;
					TimeEvent timeEvent = new TimeEvent(timedTranstion.getTimeBean().getValueToBaseUnit(), entry.getKey());
					timeEvents.add(timeEvent);
				}
			}
		}
		return timeEvents;
	}
	
	/**
	 * Get the currently recommended recovery actions.
	 * @return A list of recommened recovery actions.
	 */
	public List<RecoveryAction> getRecoveryActions() {
		return recoveryAction;
	}
}
