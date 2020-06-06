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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

/**
 * Extends an iterator to nondeterministic states
 * @author muel_s8
 *
 * @param <S>
 */
public class MarkovAutomatonValueIterator<S extends MarkovState> extends DecoratedIterator {
	
	private Map<S, Collection<List<MarkovTransition<S>>>> mapNondetStateToTransitionGroups;
	private List<S> nondeterministicStates;
	private List<S> probabilisticStates;
	private MarkovAutomaton<S> ma;
	
	/**
	 * Standard constructor
	 * @param deterministicIterator the iterator for handling the update of deterministic states
	 * @param ma the markov automaton
	 */
	public MarkovAutomatonValueIterator(IMatrixIterator deterministicIterator, MarkovAutomaton<S> ma) {
		super(deterministicIterator);
		
		this.ma = ma;
		
		nondeterministicStates = new ArrayList<>();
		probabilisticStates = new ArrayList<>();
		mapNondetStateToTransitionGroups = new HashMap<>();
		
		for (S state : ma.getStates()) {
			if (state.isNondet()) {
				Map<Object, List<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
				mapNondetStateToTransitionGroups.put(state, transitionGroups.values());
				nondeterministicStates.add(state);
			} else if (state.isProbabilisic()) {
				probabilisticStates.add(state);
			}
		}
	}

	@Override
	public void iterate() {
		super.iterate();
		
		for (S nondeterministicState : nondeterministicStates) {
			double maxValue = Double.NEGATIVE_INFINITY;
			Collection<List<MarkovTransition<S>>> transitionGroups = mapNondetStateToTransitionGroups.get(nondeterministicState);
			
			for (List<MarkovTransition<S>> transitionGroup : transitionGroups) {
				double expectationValue = getExpectationValue(transitionGroup);
				maxValue = Math.max(expectationValue, maxValue);
			}
			
			getValues()[nondeterministicState.getIndex()] = maxValue;
		}
		
		for (S probabilisticState : probabilisticStates) {
			List<MarkovTransition<S>> succTransitions = ma.getSuccTransitions(probabilisticState);
			double expectationValue = getExpectationValue(succTransitions);
			getValues()[probabilisticState.getIndex()] = expectationValue;
		}
	}
	
	/**
	 * Computes the expectation value for a given transition group
	 * @param transitionGroup the transition group
	 * @return the expectation value of the transition group
	 */
	private double getExpectationValue(List<MarkovTransition<S>> transitionGroup) {
		double expectationValue = 0;
		for (MarkovTransition<S> transition : transitionGroup) {
			double succValue = getValues()[transition.getTo().getIndex()];
			expectationValue += transition.getRate() * succValue;
		}
		return expectationValue;
	}
}
