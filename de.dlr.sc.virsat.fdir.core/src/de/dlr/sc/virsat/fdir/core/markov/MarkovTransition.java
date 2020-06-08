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

import java.util.List;
import java.util.Map;

/**
 * A transition in a markov automaton
 * @author muel_s8
 *
 * @param <S> State type
 */

public class MarkovTransition<S> {
	private S from;
	private S to;
	private double rate;
	private Object event;
	
	/**
	 * Constructor creating a new markov automaton transition
	 * @param from the starting state of the transitition
	 * @param to the end state of the transition
	 * @param rate the transition rate
	 * @param event the transition event
	 */
	
	public MarkovTransition(S from, S to, double rate, Object event) {
		this.from = from;
		this.to = to;
		this.rate = rate;
		this.event = event;
	}
	
	/**
	 * Copy functionality
	 * @return a copy of this markov transition
	 */
	public MarkovTransition<S> copy() {
		return new MarkovTransition<S>(from, to, rate, event);
	}
	
	/**
	 * Set the starting state of this transition
	 * @param from the starting state of this transition
	 */
	public void setFrom(S from) {
		this.from = from;
	}
	
	/**
	 * Get the starting state of this transition
	 * @return the starting state of this transition
	 */
	public S getFrom() {
		return from;
	}
	
	/**
	 * Sets the end state of this transition
	 * @param to the end state of this transition
	 */
	public void setTo(S to) {
		this.to = to;
	}
	
	/**
	 * Gets the end state of this transition
	 * @return the end state of this transition
	 */
	
	public S getTo() {
		return to;
	}
	
	/**
	 * Sets the transition rate
	 * @param rate the transition rate
	 */
	
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	/**
	 * Gets the transition rate
	 * @return the transition rate
	 */
	
	public double getRate() {
		return rate;
	}
	
	/**
	 * Sets the transition event
	 * @param event the transition event
	 */
	
	public void setEvent(Object event) {
		this.event = event;
	}
	
	/**
	 * Gets the transition event
	 * @return the transition event
	 */
	public Object getEvent() {
		return event;
	}
	
	@Override
	public String toString() {
		return from.toString() + " --- " + event.toString() + ", " + rate + " ---> " + to.toString();
	}
	
	/**
	 * Computes the expectation value for a given transition group
	 * @param transitionGroup the transition group
	 * @return the expectation value of the transition group
	 */
	public static <S extends MarkovState> double getExpectationValue(List<MarkovTransition<S>> transitionGroup, 
			Map<MarkovState, Integer> mapStateToIndex, double[] values) {
		double expectationValue = 0;
		for (MarkovTransition<S> transition : transitionGroup) {
			double succValue = values[mapStateToIndex.get(transition.getTo())];
			expectationValue += transition.getRate() * succValue;
		}
		return expectationValue;
	}
}
