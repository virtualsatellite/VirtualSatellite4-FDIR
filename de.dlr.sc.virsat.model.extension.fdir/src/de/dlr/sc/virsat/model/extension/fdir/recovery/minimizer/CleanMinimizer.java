/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Clean the transitions by removing all epsilon loops and duplicate transitions
 * @author muel_s8
 *
 */
public class CleanMinimizer extends ARecoveryAutomatonMinimizer {

	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder) {
		RecoveryAutomaton ra = raHolder.getRa();
		Map<State, List<Transition>> mapStateToOutgoingTransitions = raHolder.getMapStateToOutgoingTransitions();
		Map<State, List<Transition>> mapStateToIncomingTransitions = raHolder.getMapStateToIncomingTransitions();
		
		List<Transition> transitionsToRemove = new ArrayList<>();
		
		for (State state : ra.getStates()) {
			List<Transition> outgoingTransitions = mapStateToOutgoingTransitions.get(state);
			List<Transition> incomingTransitions = mapStateToIncomingTransitions.get(state);
			List<Transition> localTransitionsToRemove = new ArrayList<>();
			
			for (int i = 0; i < outgoingTransitions.size(); i++) {
				Transition transition1 = outgoingTransitions.get(i);
				
				if (incomingTransitions.contains(transition1) && transition1.getRecoveryActions().isEmpty()) {
					transitionsToRemove.add(transition1);
					localTransitionsToRemove.add(transition1);
				} else {
					// Check for duplicate
					for (int j = i + 1; j < outgoingTransitions.size(); j++) {
						if (transition1.isEquivalentTransition(outgoingTransitions.get(j))) {
							transitionsToRemove.add(transition1);
							localTransitionsToRemove.add(transition1);
						}
					}
				}
			}
			
			outgoingTransitions.removeAll(localTransitionsToRemove);
		}
		 
		ra.getTransitions().removeAll(transitionsToRemove); 
	}
}
