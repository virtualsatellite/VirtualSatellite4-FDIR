/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

public class BeliefStateEquivalence {

	private Map<Set<FaultTreeNode>, List<BeliefState>> mapObservationsToBeliefStates = new HashMap<>();
	private double eps;
	
	/**
	 * Standard constructor
	 * @param eps the minimum belief accuracy
	 */
	public BeliefStateEquivalence(double eps) {
		this.eps = eps;
	}
	
	/**
	 * Adds the state into the equivalence relation
	 * @param state the state to add
	 * @return a list of potentially equivalent states
	 */
	public List<BeliefState> addState(BeliefState state) {
		Set<FaultTreeNode> observations = state.representant.getObservedFailedNodes();
		List<BeliefState> beliefStates = mapObservationsToBeliefStates.get(observations);
		if (beliefStates == null) {
			beliefStates = new ArrayList<>();
			beliefStates.add(state);
			mapObservationsToBeliefStates.put(observations, beliefStates);
		}
		
		return beliefStates;
	}
	
	/**
	 * Check if there is an equivalent state of the passed state and return it.
	 * @param state the state for which we want to check if an equivalent one exists
	 * @return an equivalent state to the passed one or the state itself if not equivalent state exists
	 */
	public BeliefState getEquivalentState(BeliefState state) {
		List<BeliefState> states = addState(state);
		
		for (BeliefState other : states) {
			if (other != state && state.isEquivalent(other, eps)) {
				return other;
			}
		}
		
		states.add(state);
		
		return state;
	}
	
	
	/**
	 * Gets the number of states that the BeliefStateEquivalence has encountered.
	 * @return the number of unique states in mapObservationsToBeliefStates
	 */
	public int getMapValuesSetSize() {
		HashSet<BeliefState> set = new HashSet<>();
		for (List<BeliefState> value : mapObservationsToBeliefStates.values()) {
			set.addAll(value);
		}
		return set.size();
	}
}
