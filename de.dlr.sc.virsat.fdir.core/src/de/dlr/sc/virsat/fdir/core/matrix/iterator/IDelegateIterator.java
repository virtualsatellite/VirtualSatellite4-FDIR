/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

/**
 * This iterator delegates iteration updates due to probabilistic transitions.
 * 
 * @author muel_s8
 *
 * @param <S>
 */

public interface IDelegateIterator<S extends MarkovState> {
	
	/**
	 * Get informaed of a probabliistic update due to a transition group.
	 * @param stateIndex the index of the state
	 * @param value the new value of the state
	 * @param transitions the probabilistic transition group
	 */
	void delegateProbabilisticUpdate(int stateIndex, double value, List<MarkovTransition<S>> transitions);
}
