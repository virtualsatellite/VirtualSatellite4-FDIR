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
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.TimeEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimedTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Wrapper turning a recovery automaton into an actual recovery strategy.
 * @author muel_s8
 *
 */

public class RecoveryStrategy {
	
	private String recoveryActionsLabel;
	private State currentState;
	private RecoveryAutomatonHolder raHolder;
	
	/**
	 * Standard constructor.
	 * @param ras base recovery strategy
	 * @param currentState the state of the strategy
	 * @param recoveryActionsLabel the label of the recovery actions that should be taken
	 */
	private RecoveryStrategy(RecoveryStrategy ras, State currentState, String recoveryActionsLabel) {
		this.raHolder = ras.raHolder;
		this.currentState = currentState;
		this.recoveryActionsLabel = recoveryActionsLabel;
	}
	
	/**
	 * Constructor for wrapping a recovery automaton
	 * @param ra the recovery automaton to wrap
	 */
	
	public RecoveryStrategy(RecoveryAutomaton ra) {
		raHolder = new RecoveryAutomatonHolder(ra);
		currentState = ra.getInitial();
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
		if (raHolder.getMapStateToOutgoingTransitions().get(currentState) != null) {
			Set<String> faultUUIDs = faults.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
			for (Transition transition : raHolder.getMapStateToOutgoingTransitions().get(currentState)) {
				if (transition instanceof FaultEventTransition) {
					Set<String> guardUUIDs = ((FaultEventTransition) transition).getGuards()
							.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
					if (guardUUIDs.equals(faultUUIDs)) {
						RecoveryStrategy ras = new RecoveryStrategy(this,
								raHolder.getMapTransitionToTo().get(transition), 
								raHolder.getMapTransitionToActionLabels().get(transition));
						return ras;
					}
				}
			}
		}
		
		RecoveryStrategy ras = new RecoveryStrategy(this, currentState, null);
		return ras;
	}

	/**
	 * React to the occurence of a time event
	 * @param time the time event
	 * @return the recovery strategy after reading the time event
	 */
	public RecoveryStrategy onTime(double time) {
		if (raHolder.getMapStateToOutgoingTransitions().get(currentState) != null) {
			for (Transition transition : raHolder.getMapStateToOutgoingTransitions().get(currentState)) {
				if (transition instanceof TimedTransition) {
					TimedTransition timedTransition = (TimedTransition) transition;
					if (timedTransition.getTimeBean().getValueToBaseUnit() == time) {
						RecoveryStrategy ras = new RecoveryStrategy(this,
								raHolder.getMapTransitionToTo().get(transition), 
								raHolder.getMapTransitionToActionLabels().get(transition));
						return ras;
					}
				}
			}
		}
		
		RecoveryStrategy ras = new RecoveryStrategy(this, currentState, null);
		return ras;
	}

	/**
	 * Creates a set of events that can occur from the recovery side
	 * @return the set of events
	 */
	public List<IDFTEvent> createEventSet() {
		List<IDFTEvent> timeEvents = new ArrayList<>();
		for (Entry<State, List<Transition>> entry : raHolder.getMapStateToOutgoingTransitions().entrySet()) {
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
	public String getRecoveryActionsLabel() {
		return recoveryActionsLabel != null ? recoveryActionsLabel : "";
	}

	/**
	 * Reset the recovery strategy
	 * @return the recovery strategy in its initial state
	 */
	public RecoveryStrategy reset() {
		RecoveryStrategy ras = new RecoveryStrategy(this, raHolder.getRa().getInitial(), null);
		return ras;
	}
}
