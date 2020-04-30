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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;

/**
 * Represents a belief state
 * @author muel_s8
 *
 */
public class BeliefState extends MarkovState {
	Map<PODFTState, Double> mapStateToBelief = new HashMap<>();
	PODFTState representant;
	MarkovAutomaton<BeliefState> beliefMa;
	
	/**
	 * Standard constructor
	 * @param the associated belief ma
	 * @param representant the representant
	 */
	BeliefState(MarkovAutomaton<BeliefState> beliefMa, PODFTState representant) {
		this.beliefMa = beliefMa;
		this.representant = representant;
	}
	
	@Override
	public String toString() {
		String beliefs = mapStateToBelief.entrySet().stream()
				.map(entry -> String.format("%.2f", entry.getValue()) + ": " +  entry.getKey().getLabel())
				.collect(Collectors.joining(","));
		
		String label = index + " [label=\"[" + index + " " + beliefs + "]\"";
		if (beliefMa.getFinalStates().contains(this)) {
			label += ", color=\"red\"";
		} else if (!isMarkovian()) {
			label += ", color=\"blue\"";
		}
		label += "]";
		
		return label;
	}
	
	/**
	 * Normalizes the beliefs in the belief state
	 */
	public void normalize() {
		double totalBelief = getTotalBelief();
		mapStateToBelief.replaceAll((state, belief) -> belief / totalBelief);
	}
	
	/**
	 * Computes the total belief in a belief state.
	 * Should be 1 after normalization.
	 * @return the total belief in the belief state
	 */
	public double getTotalBelief() {
		double totalBelief = 0;
		for (Double belief : mapStateToBelief.values()) {
			totalBelief += belief;
		}
		return totalBelief;
	}
}
