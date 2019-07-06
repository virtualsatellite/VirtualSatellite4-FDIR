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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Wrapper turning a recovery automaton into an actual recovery strategy.
 * @author muel_s8
 *
 */

public class RecoveryAutomatonStrategy extends ARecoveryStrategy {
	
	private RecoveryAutomaton ra;
	private State currentState;
	private Map<State, Set<Transition>> mapStateToOutTransitions;
	
	/**
	 * Default constructor.
	 */
	
	private RecoveryAutomatonStrategy() {
		
	}
	
	/**
	 * Constructor for wrapping a recovery automaton
	 * @param ra the recovery automaton to wrap
	 */
	
	public RecoveryAutomatonStrategy(RecoveryAutomaton ra) {
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
	
	@Override
	public String toString() {
		return currentState.toString();
	}

	@Override
	public IRecoveryStrategy onFaultsOccured(Collection<FaultTreeNode> faults) {
		if (mapStateToOutTransitions.get(currentState) != null) {
			Set<String> faultUUIDs = faults.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
			for (Transition transition : mapStateToOutTransitions.get(currentState)) {
				if (transition instanceof FaultEventTransition) {
					Set<String> guardUUIDs = ((FaultEventTransition) transition).getGuards()
							.stream().map(FaultTreeNode::getUuid).collect(Collectors.toSet());
					if (guardUUIDs.equals(faultUUIDs)) {
						RecoveryAutomatonStrategy ras = new RecoveryAutomatonStrategy();
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
}
