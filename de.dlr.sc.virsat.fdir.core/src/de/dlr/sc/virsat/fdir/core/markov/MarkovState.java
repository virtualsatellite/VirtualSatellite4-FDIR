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

/**
 * Abstract class for representing a state in a Markov structure
 * @author muel_s8
 *
 */
public class MarkovState {
	protected int index;
	private boolean markovian = true;
	
	/**
	 * Gets the index of this state
	 * @return the index of this state
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets whether or not this state is markovian
	 * @param markovian set to true to make the state markovian, set to false to make it immediate nondeterministic
	 */
	public void setMarkovian(boolean markovian) {
		this.markovian = markovian;
	}
	
	/**
	 * Checks if this state is markovian
	 * @return true iff the state is markovian, false iff the state is immediate nondeterministic
	 */
	public boolean isMarkovian() {
		return markovian;
	}
}
