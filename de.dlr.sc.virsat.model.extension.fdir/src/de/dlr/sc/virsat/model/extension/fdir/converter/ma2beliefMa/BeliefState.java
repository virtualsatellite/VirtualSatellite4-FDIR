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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
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
	
	/**
	 * Computes the total rate for a transition set
	 * @param transitions a set of transitions
	 * @return the total rate of the transition set
	 */
	public double getTotalRate(Set<MarkovTransition<DFTState>> transitions) {
		double getTotalRate = 0;
		for (MarkovTransition<DFTState> transition : transitions) {
			getTotalRate += transition.getRate() * mapStateToBelief.get(transition.getFrom());
		}
		return getTotalRate;
	}
	
	/**
	 * Checks if two belief states are belief equivalent
	 * @param other the other belief state
	 * @param eps the accuracy
	 * @return true if the two belief states are belief equivalent
	 */
	public boolean isEquivalent(BeliefState other, double eps) {
		if (isMarkovian() != other.isMarkovian() || mapStateToBelief.size() != other.mapStateToBelief.size()) {
			return false;
		}
		
		Set<DFTState> dftStates = new HashSet<>(mapStateToBelief.keySet());
		dftStates.addAll(other.mapStateToBelief.keySet());
		boolean isEquivalent = other.representant.getObservedFailed().equals(representant.getObservedFailed()) 
				&& other.representant.getMapSpareToClaimedSpares().equals(representant.getMapSpareToClaimedSpares());
		
		if (!isEquivalent) {
			return false;
		}
		
		for (DFTState dftState : dftStates) {
			double prob = mapStateToBelief.getOrDefault(dftState, Double.NaN);
			double probOther = other.mapStateToBelief.getOrDefault(dftState, Double.NaN);
			double diff = Math.abs(prob - probOther);
			
			if (Double.isNaN(diff) || diff > eps) {
				return false;
			}
		}
		
		return true;
	}
}
