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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.TransitionHolder;

/**
 * Class that minimizes a recovery automaton by merging final states to previous states provided that the epsilon transitions cannot be executed  
 * @author mika_li
 *
 */
public class FinalStateMinimizer extends ARecoveryAutomatonMinimizer {
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder, FaultTreeHolder ftHolder) {
		RecoveryAutomaton ra = raHolder.getRa();
		
		RecoveryAutomatonHelper raHelper = raHolder.getRaHelper();
		
		Set<State> finalStates = new HashSet<>();
		List<Transition> transitionsToRemove = new ArrayList<>();
		
		for (State state1 : ra.getStates()) {
			List<Transition> outgoingTransitions1 = new ArrayList<>(raHolder.getStateHolder(state1).getOutgoingTransitions());
			if (raHelper.isFinalState(ra, state1, outgoingTransitions1) || !raHelper.isFinalStateEquivalent(ra, outgoingTransitions1)) {
				continue;
			}
				
			State state2 = outgoingTransitions1.get(0).getTo(); 
			List<Transition> outgoingTransitions2 = raHolder.getStateHolder(state2).getOutgoingTransitions();
				
			if (state1.equals(state2) || !raHelper.isFinalState(ra, state2, outgoingTransitions2)) {
				continue;
			}

			finalStates.add(state2);
			
			// create new mergedState and redirect transitions 
			List<Transition> incomingTransitions2 = new ArrayList<>(raHolder.getStateHolder(state2).getIncomingTransitions());
			for (Transition incomingTransition : incomingTransitions2) {
				if (incomingTransition.getFrom().equals(state1)) {
					TransitionHolder incomingTransitionHolder = raHolder.getTransitionHolder(incomingTransition);
					if (incomingTransitionHolder.isEpsilonTransition()) {
						transitionsToRemove.add(incomingTransition);
					} else {
						incomingTransitionHolder.setTo(state1);
					}
				}
			}
			
			for (Transition outgoingTransition1 : outgoingTransitions1) {
				raHolder.getTransitionHolder(outgoingTransition1).setTo(state1);
			}
		}
		
		for (State state : finalStates) {
			if (Objects.equals(ra.getInitial(), state)) {
				continue;
			}
			
			// remove the states that cannot be reached
			List<Transition> outgoingTransitions = raHolder.getStateHolder(state).getOutgoingTransitions();
			List<Transition> incomingTransitions = raHolder.getStateHolder(state).getIncomingTransitions();
			if (outgoingTransitions.equals(incomingTransitions)) {
				transitionsToRemove.addAll(outgoingTransitions);
				transitionsToRemove.addAll(incomingTransitions);
				raHolder.removeStates(Collections.singleton(state));
			}
		}
		
		raHolder.removeTransitions(transitionsToRemove);
	}
}
