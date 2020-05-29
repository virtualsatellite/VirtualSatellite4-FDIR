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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
	Map<PODFTState, Double> mapStateToBelief = new TreeMap<>(MarkovState.MARKOVSTATE_COMPARATOR);
	PODFTState representant;
	
	/**
	 * Standard constructor
	 * @param representant the representant
	 */
	BeliefState(PODFTState representant) {
		this.representant = representant;
		setMarkovian(representant.isMarkovian());
	}
	
	@Override
	public String toString() {
		String beliefs = mapStateToBelief.entrySet().stream()
				.map(entry -> String.format("%.2f", entry.getValue()) + ": " +  entry.getKey().getLabel())
				.collect(Collectors.joining(","));
		
		String label = index + " [label=\"[" + index + " " + beliefs + "]\"";
		if (getFailProb() > 0) {
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
	 * @param transitions a list of transitions
	 * @return the total rate of the transition set
	 */
	public double getTotalRate(List<MarkovTransition<DFTState>> transitions) {
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

	/**
	 * Gets the total probability of currently being in a fail state
	 * @return the probability of being in a fail state
	 */
	public double getFailProb() {
		double failProb = 0;
		for (Entry<PODFTState, Double> entry : mapStateToBelief.entrySet()) {
			if (entry.getKey().getFailState()) {
				failProb += entry.getValue();
			}
		}
		return failProb;
	}

	/** 
	 * Adds belief of being in a concrete state.
	 * @param state the state we believe to be in
	 * @param belief the probability of being in the state
	 */
	void addBelief(PODFTState state, double belief) {
		mapStateToBelief.merge(state, belief, Double::sum);
	}
}
