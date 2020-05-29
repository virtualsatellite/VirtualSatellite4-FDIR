/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;

public class DFTStateEquivalence {

	private Map<Set<BasicEvent>, List<DFTState>> mapUnorderedBesToDFTStates = new HashMap<>();
	
	/**
	 * Adds an equivalence class for a given state into the equivalence relation
	 * @param state whose class should be added
	 * @return the existing equivalence class
	 */
	private List<DFTState> addStateClass(DFTState state) {
		List<DFTState> dftStates = mapUnorderedBesToDFTStates.get(state.getUnorderedBes());
		if (dftStates == null) {
			dftStates = new ArrayList<>();
			mapUnorderedBesToDFTStates.put(state.getUnorderedBes(), dftStates);
		}
		
		return dftStates;
	}
	
	/**
	 * Check if there is an equivalent state of the passed state and return it.
	 * @param state the state for which we want to check if an equivalent one exists
	 * @return an equivalent state to the passed one or the state itself if not equivalent state exists
	 */
	public DFTState getEquivalentState(DFTState state) {
		List<DFTState> states = addStateClass(state);
		
		for (DFTState other : states) {
			if (state.isEquivalent(other)) {
				return other;
			}
		}	
	
		states.add(state);
		return state;
	}
}
