/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

/**
 * Abstract class for representing a state in a Markov structure
 * @author muel_s8
 *
 */
public class MarkovState {
	protected int index;
	private MarkovStateType type = MarkovStateType.MARKOVIAN;
	private Map<FailLabel, Double> mapFailLabelToProb = new HashMap<>();
	// Sentinel value that ensures the correct index is returned when accessing the mttf values array
	protected int valuesIndex = -1;
	
	/**
	 * Gets the index of this state
	 * @return the index of this state
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets the index to the state when in between states get merged due to Bismilulation
	 * @param i
	 */
	public void setIndex(int i) {
		this.index = i;
	}
	
	/**
	 * Sets the index of the mttf values array that corresponds to this state due to the mismatch between state index and array index when the state space is reduced
	 * @param valuesIndex the index of the mttf values array that corresponds to this state
	 */
	public void setValuesIndex(int valuesIndex) {
		this.valuesIndex = valuesIndex;
	}
	/**
	 * Gets the index of the mttf values array that corresponds to this state due to the mismatch between state index and array index when the state space is reduced
	 * @return the index of the mttf values array that corresponds to this state
	 */
	public int getValuesIndex() {
		if (valuesIndex == -1) {
			return getIndex();
		} else {
			return valuesIndex;
		}
	}
	
	/**
	 * Sets the type of this state
	 * @param type the new type of this state
	 */
	public void setType(MarkovStateType type) {
		this.type = type;
	}
	
	/**
	 * Gets the type of this state
	 * @return the type of this state
	 */
	public MarkovStateType getType() {
		return type;
	}
	
	public boolean isMarkovian() {
		return type.equals(MarkovStateType.MARKOVIAN);
	}
	
	public boolean isNondet() {
		return type.equals(MarkovStateType.NONDET);
	}
	
	public boolean isProbabilisic() {
		return type.equals(MarkovStateType.PROBABILISTIC);
	}
	
	public Map<FailLabel, Double> getMapFailLabelToProb() {
		return mapFailLabelToProb;
	}
	
	public Set<FailLabel> getFailLabels() {
		return mapFailLabelToProb.keySet();
	}
	
	@Override
	public String toString() {
		return String.valueOf(index);
	}
	
	/**
	 * Standard comparator for MarkovStates.
	 * Usefule for sorting lists and ensuring deterministic behavior.
	 */
	public static final Comparator<MarkovState> MARKOVSTATE_COMPARATOR = new Comparator<MarkovState>() {
		public int compare(MarkovState state1, MarkovState state2) {
			return Integer.compare(state1.index, state2.index);
		};
	};
}
