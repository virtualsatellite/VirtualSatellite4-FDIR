/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.DecoratedIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;

/**
 * Extends an iterator to nondeterministic states
 * @author muel_s8
 *
 * @param <S>
 */
public class MarkovAutomatonValueIterator<S extends MarkovState> extends DecoratedIterator {
	
	private Map<S, Map<Object, Set<MarkovTransition<S>>>> mapNondetStateToTransitionGroups;
	private List<S> nondeterministicStates;
	
	/**
	 * Standard constructor
	 * @param deterministicIterator the iterator for handling the update of deterministic states
	 * @param ma the markov automaton
	 */
	public MarkovAutomatonValueIterator(IMatrixIterator deterministicIterator, MarkovAutomaton<S> ma) {
		super(deterministicIterator);
		
		nondeterministicStates = new ArrayList<>();
		mapNondetStateToTransitionGroups = new HashMap<>();
		for (S state : ma.getStates()) {
			if (!state.isMarkovian()) {
				Map<Object, Set<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
				mapNondetStateToTransitionGroups.put(state, transitionGroups);
			}
		}
	}

	@Override
	public void iterate() {
		super.iterate();
		for (S nondeterministicState : nondeterministicStates) {
			double maxValue = Double.NEGATIVE_INFINITY;
			Map<Object, Set<MarkovTransition<S>>> transitionGroups = mapNondetStateToTransitionGroups.get(nondeterministicState);
			
			for (Set<MarkovTransition<S>> transitionGroup : transitionGroups.values()) {
				double expectationValue = 0;
				for (MarkovTransition<S> transition : transitionGroup) {
					double succValue = getValues()[transition.getTo().getIndex()];
					expectationValue += transition.getRate() * succValue;
				}					
				maxValue = Math.max(expectationValue, maxValue);
			}
			
			getValues()[nondeterministicState.getIndex()] = maxValue;
		}
	}
}
