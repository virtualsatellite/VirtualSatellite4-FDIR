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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.TimeoutEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.StateHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.TransitionHolder;

/**
 * Wrapper turning a recovery automaton into an actual recovery strategy.
 * @author muel_s8
 *
 */

public class RecoveryStrategy {
	
	private List<RecoveryAction> recoveryActions;
	private State currentState;
	private RecoveryAutomatonHolder raHolder;
	
	/**
	 * Standard constructor.
	 * @param raHolder the recovery automaton holder
	 * @param currentState the state of the strategy
	 * @param recoveryActions the label of the recovery actions that should be taken
	 */
	private RecoveryStrategy(RecoveryAutomatonHolder raHolder, State currentState, List<RecoveryAction> recoveryActions) {
		this.raHolder = raHolder;
		this.currentState = currentState;
		this.recoveryActions = recoveryActions;
	}
	
	/**
	 * Constructor for wrapping a recovery automaton
	 * @param ra the recovery automaton to wrap
	 */
	
	public RecoveryStrategy(RecoveryAutomaton ra) {
		raHolder = new RecoveryAutomatonHolder(ra);
		currentState = ra.getInitial();
		recoveryActions = Collections.emptyList();
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
	 * Executes the current strategy on a state
	 * @param state the state to execute the strategy on
	 */
	public List<FaultTreeNode> execute(DFTState state) {
		List<FaultTreeNode> affectedNodes = new ArrayList<>();
		state.setRecoveryStrategy(this);
		for (RecoveryAction ra : getRecoveryActions()) {
			ra.execute(state);
			affectedNodes.addAll(ra.getAffectedNodes(state));
		}
		
		return affectedNodes;
	}

	private RecoveryStrategy onTransitionTriggered(TransitionHolder transitionHolder) {
		return new RecoveryStrategy(raHolder, transitionHolder.getTo(), transitionHolder.getRecoveryActions());
	}
	
	/**
	 * React to the occurrence or the repair of a set of faults
	 * @param faults the occurred or repaired faults
	 * @param whether it was a repair or a failure occurrence
	 * @return the recovery strategy after reading the fault
	 */
	public RecoveryStrategy onFaultsOccured(Collection<FaultTreeNode> faults, boolean isRepair) {
		StateHolder stateHolder = raHolder.getStateHolder(currentState);
		if (!stateHolder.getOutgoingTransitions().isEmpty()) {
			Set<String> faultUUIDs = faults.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
			for (Transition transition : stateHolder.getOutgoingTransitions()) {
				if (transition instanceof FaultEventTransition) {
					TransitionHolder transitionHolder = raHolder.getTransitionHolder(transition);
					FaultEventTransition fet = (FaultEventTransition) transition;
					Set<String> guardUUIDs = transitionHolder.getGuards()
							.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
					if (guardUUIDs.equals(faultUUIDs) && isRepair == fet.getIsRepair()) {
						return onTransitionTriggered(transitionHolder);
					}
				}
			}
		}
		
		return new RecoveryStrategy(raHolder, currentState, Collections.emptyList());
	}

	/**
	 * React to the occurence of a time event
	 * @param time the time event
	 * @return the recovery strategy after reading the time event
	 */
	public RecoveryStrategy onTimeout() {
		TimeoutTransition timeoutTransition = raHolder.getStateHolder(currentState).getTimeoutTransition();
		return onTransitionTriggered(raHolder.getTransitionHolder(timeoutTransition));
	}

	/**
	 * Creates a set of events that can occur from the recovery side
	 * @return the set of events
	 */
	public List<IDFTEvent> createEventSet() {
		List<IDFTEvent> timeEvents = new ArrayList<>();
		for (StateHolder stateHolder : raHolder.getMapStateToStateHolder().values()) {
			TimeoutTransition timeoutTranstion = stateHolder.getTimeoutTransition();
			if (timeoutTranstion != null) {
				TimeoutEvent timeEvent = new TimeoutEvent(timeoutTranstion.getTimeBean().getValueToBaseUnit(), stateHolder.getState());
				timeEvents.add(timeEvent);
			}
		}
		return timeEvents;
	}
	
	/**
	 * Get the currently recommended recovery actions.
	 * @return A list of recommened recovery actions.
	 */
	public List<RecoveryAction> getRecoveryActions() {
		return recoveryActions;
	}

	/**
	 * Reset the recovery strategy
	 * @return the recovery strategy in its initial state
	 */
	public RecoveryStrategy reset() {
		return new RecoveryStrategy(raHolder, raHolder.getRa().getInitial(), Collections.emptyList());
	}
}
