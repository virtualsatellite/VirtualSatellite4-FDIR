/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

/**
 * Selects the optimal transition groups for a given state in terms of fail probability
 * @author khan_ax
 *
 * @param <S>
 */

public class OptimalTransitionsSelector<S extends MarkovState> {
	
	/**
	 * Selects the optimal transition groups for a given state in terms of fail probability
	 * @author khan_ax
	 * @param ma the markov automaton
	 * @param state the state
	 * @return the optimal transition groups of successor states
	 */
	public List<MarkovTransition<S>> selectOptimalTransitions(MarkovAutomaton<S> ma, S state) {
		List<MarkovTransition<S>> bestTransitions = new ArrayList<>();
		double bestTransitionProbFail = 1;
		
		Map<Object, List<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
		
		for (List<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
			double transitionGroupProbFail = 0;
			
			for (MarkovTransition<S> transition : transitionGroup) {
				double prob = transition.getRate();
				MarkovState toState = transition.getTo();
				double failProb = toState.getMapFailLabelToProb().getOrDefault(FailLabel.FAILED, 0d);
				transitionGroupProbFail += prob * failProb;
			}
			
			if (transitionGroupProbFail <= bestTransitionProbFail) {
				boolean isNewBestTransition = bestTransitions.isEmpty() || (transitionGroupProbFail < bestTransitionProbFail);
				
				if (isNewBestTransition) {
					bestTransitions.clear();
					bestTransitionProbFail = transitionGroupProbFail;
				}
				
				bestTransitions.addAll(transitionGroup);
			}
		}
	
		return bestTransitions;
	}

}
