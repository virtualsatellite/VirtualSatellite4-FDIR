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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Class that minimizes a recovery automaton by merging final states to previous states provided that the epsilon transitions cannot be executed  
 * @author mika_li
 *
 */
public class FinalStateMinimizer extends ARecoveryAutomatonMinimizer {
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder) {
		RecoveryAutomaton ra = raHolder.getRa();
		
		RecoveryAutomatonHelper raHelper = raHolder.getRaHelper();
		
		Map<State, List<Transition>> mapStateToPredecessors = raHelper.getPreviousTransitions(ra);
		Map<State, List<Transition>> mapStateToSuccessors = raHelper.getCurrentTransitions(ra);
		
		Set<State> finalStates = new HashSet<>();
		
		for (State state1 : ra.getStates()) {
			List<Transition> outgoingTransitions1 = mapStateToSuccessors.get(state1); 
			if (!raHelper.isFinalState(ra, state1, outgoingTransitions1) && raHelper.isFinalStateEquivalent(ra, outgoingTransitions1)) {
				
				State state2 = outgoingTransitions1.get(0).getTo(); 
				List<Transition> outgoingTransitions2 = mapStateToSuccessors.get(state2);
				
				if (!state1.equals(state2) && raHelper.isFinalState(ra, state2, outgoingTransitions2)) {

					finalStates.add(state2);
					
					// create new mergedState and redirect transitions 
					List<Transition> incomingTransitions2 = mapStateToPredecessors.get(state2);
					List<Transition> transitionsToRemove = new ArrayList<>();
					for (Transition incomingTransition : incomingTransitions2) {
						if (incomingTransition.getFrom().equals(state1)) {
							transitionsToRemove.add(incomingTransition);
							if (incomingTransition.getRecoveryActions().isEmpty()) {
								ra.getTransitions().remove(incomingTransition);
							} else {
								incomingTransition.setTo(state1);
							}
						}
					}
					
					for (Transition outgoingTransition1 : outgoingTransitions1) {
						outgoingTransition1.setTo(state1);
					}
					
					for (Transition transition : transitionsToRemove) {
						incomingTransitions2.remove(transition);
					}
				}

			}
			
		}
		
		for (State state : finalStates) {
			if (!Objects.equals(ra.getInitial(), state)) {
				// remove the states that cannot be reached
				if (mapStateToSuccessors.get(state).equals(mapStateToPredecessors.get(state))) {
					ra.getStates().remove(state); 
					ra.getTransitions().removeAll(mapStateToPredecessors.get(state));
					ra.getTransitions().removeAll(mapStateToSuccessors.get(state));
					mapStateToPredecessors.remove(state);
					mapStateToSuccessors.remove(state);
				}
			}
		}
	}
}
