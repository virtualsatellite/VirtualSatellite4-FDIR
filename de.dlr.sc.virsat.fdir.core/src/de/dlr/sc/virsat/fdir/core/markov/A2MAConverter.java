/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.markov;

/**
 * This class is an abstract base class for converting some object to a markov automaton representation
 * @author muel_s8
 *
 * @param <S> the type of the state space
 * @param <G> the type of the state space generator
 */
public abstract class A2MAConverter<S extends MarkovState, G extends AStateSpaceGenerator<S>> {
	protected G stateSpaceGenerator = createStateSpaceGenerator();
	protected MarkovAutomatonBuilder<S> maBuilder = new MarkovAutomatonBuilder<S>();
	
	protected abstract G createStateSpaceGenerator();
	
	/**
	 * Gets the state space generator
	 * @return the state space generator
	 */
	public G getStateSpaceGenerator() {
		return stateSpaceGenerator;
	}
	
	/**
	 * Gets the markov automaton builder
	 * @return the markov automaton builder
	 */
	public MarkovAutomatonBuilder<S> getMaBuilder() {
		return maBuilder;
	}
}
