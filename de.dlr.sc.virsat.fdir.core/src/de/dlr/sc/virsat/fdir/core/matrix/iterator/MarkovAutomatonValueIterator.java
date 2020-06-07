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
import java.util.Arrays;
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
public class MarkovAutomatonValueIterator<S extends MarkovState> extends DecoratedIterator implements IDelegateIterator<S> {
	
	private Map<S, Collection<List<MarkovTransition<S>>>> mapNondetStateToTransitionGroups;
	private List<S> nondeterministicStates;
	private List<S> probabilisticStates;
	private MarkovAutomaton<S> ma;
	private boolean maximize;
	private Map<MarkovState, Integer> mapStateToIndex;
	
	/**
	 * Standard constructor
	 * @param deterministicIterator the iterator for handling the update of deterministic states
	 * @param ma the markov automaton
	 * @param maximize whether the iterator will try to maximize or minimize the values
	 */
	public MarkovAutomatonValueIterator(IMatrixIterator deterministicIterator, MarkovAutomaton<S> ma, List<? extends MarkovState> states, boolean maximize) {
		super(deterministicIterator);
		
		this.ma = ma;
		this.maximize = maximize;
		
		nondeterministicStates = new ArrayList<>();
		probabilisticStates = new ArrayList<>();
		mapNondetStateToTransitionGroups = new HashMap<>();
		mapStateToIndex = new HashMap<>();
		
		for (int i = 0; i < states.size(); ++i) {
			@SuppressWarnings("unchecked")
			S state = (S) states.get(i);
			mapStateToIndex.put(state, i);
			if (state.isNondet()) {
				Map<Object, List<MarkovTransition<S>>> transitionGroups = ma.getGroupedSuccTransitions(state);
				mapNondetStateToTransitionGroups.put(state, transitionGroups.values());
				nondeterministicStates.add(state);
			} else if (state.isProbabilisic()) {
				probabilisticStates.add(state);
			}
		}
	}
	
	/**
	 * Standard constructor for maximizing iterators.
	 * @param deterministicIterator the iterator for handling the update of deterministic states
	 * @param ma the markov automaton
	 */
	public MarkovAutomatonValueIterator(IMatrixIterator deterministicIterator, MarkovAutomaton<S> ma) {
		this(deterministicIterator, ma, ma.getStates(), true);
	}

	@Override
	public void iterate() {
		System.out.println(Arrays.toString(getValues()));
		
		super.iterate();
		
		for (S nondeterministicState : nondeterministicStates) {
			Collection<List<MarkovTransition<S>>> transitionGroups = mapNondetStateToTransitionGroups.get(nondeterministicState);
			
			List<MarkovTransition<S>> bestTransitionGroup = null;
			double bestValue = maximize ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			for (List<MarkovTransition<S>> transitionGroup : transitionGroups) {
				double expectationValue = MarkovTransition.getExpectationValue(transitionGroup, mapStateToIndex, getValues());
				if ((maximize && expectationValue > bestValue) || expectationValue < bestValue) {
					bestValue = expectationValue;
					bestTransitionGroup = transitionGroup;
				}
			}
			
			delegateProbabilisticUpdate(mapStateToIndex, mapStateToIndex.get(nondeterministicState), bestValue, bestTransitionGroup);
		}
		
		for (S probabilisticState : probabilisticStates) {
			List<MarkovTransition<S>> succTransitions = ma.getSuccTransitions(probabilisticState);
			double expectationValue = MarkovTransition.getExpectationValue(succTransitions, mapStateToIndex, getValues());
			delegateProbabilisticUpdate(mapStateToIndex, mapStateToIndex.get(probabilisticState), expectationValue, succTransitions);
		}
	}
	
	public void setMaximize(boolean maximize) {
		this.maximize = maximize;
	}
	
	public boolean isMaximize() {
		return maximize;
	}

	@Override
	public void delegateProbabilisticUpdate(Map<MarkovState, Integer> mapStateToIndex, int index, double value, List<MarkovTransition<S>> transitions) {
		getValues()[index] = value;
		IMatrixIterator decoratedItr = getDecoratedIterator();
		if (decoratedItr instanceof IDelegateIterator) {
			@SuppressWarnings("unchecked")
			IDelegateIterator<S> delegateItr = (IDelegateIterator<S>) decoratedItr;
			delegateItr.delegateProbabilisticUpdate(mapStateToIndex, index, value, transitions);
		}
	}
}
