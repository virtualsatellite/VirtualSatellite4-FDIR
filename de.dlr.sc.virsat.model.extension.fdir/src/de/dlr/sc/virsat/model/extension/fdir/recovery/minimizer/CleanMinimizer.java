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

import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.TransitionHolder;

/**
 * Clean the transitions by removing all epsilon loops and duplicate transitions
 * @author muel_s8
 *
 */
public class CleanMinimizer extends ARecoveryAutomatonMinimizer {

	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder) {
		List<Transition> transitionsToRemove = new ArrayList<>();
		
		for (State state : raHolder.getMapStateToStateHolder().keySet()) {
			List<Transition> outgoingTransitions = raHolder.getStateHolder(state).getOutgoingTransitions();
			
			for (int i = 0; i < outgoingTransitions.size(); i++) {
				Transition transition1 = outgoingTransitions.get(i);
				TransitionHolder transitionHolder1 = raHolder.getTransitionHolder(transition1);
				
				if (transitionHolder1.isEpsilonLoop()) {
					transitionsToRemove.add(transition1);
				} else {
					// Check for duplicate
					for (int j = i + 1; j < outgoingTransitions.size(); j++) {
						TransitionHolder transitionHolder2 = raHolder.getTransitionHolder(outgoingTransitions.get(j));
						if (transitionHolder1.isEquivalent(transitionHolder2)) {
							transitionsToRemove.add(transition1);
						}
					}
				}
			}
		}
		 
		raHolder.removeTransitions(transitionsToRemove);
	}
}
