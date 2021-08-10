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

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.A2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;

/**
 * This class creates a belief markov automaton out of a regular markov automaton.
 * A belief markov automaton has a state space where each state holds
 * a probability distribution over a set of states of the original markov automaton.
 *
 */
public class MA2BeliefMAConverter  extends A2MAConverter<BeliefState, BeliefStateSpaceGenerator> {
	
	public MA2BeliefMAConverter() {
	}
	
	public MA2BeliefMAConverter(boolean filterTransitions, boolean simplifyFailStates) {
		stateSpaceGenerator = createStateSpaceGenerator(filterTransitions, simplifyFailStates);
	}
	
	/**
	 * Creates a belief markov automaton out of the given markov automaton
	 * @param ma the markov automaton
	 * @param initialState the initial state of the markov automaton
	 * @param monitor the monitor
	 * @return the belief markov automaton
	 */
	public MarkovAutomaton<BeliefState> convert(MarkovAutomaton<DFTState> ma, PODFTState initialStateMa, SubMonitor monitor) {
		stateSpaceGenerator.configure(ma, initialStateMa);
		MarkovAutomaton<BeliefState> beliefMa = maBuilder.build(stateSpaceGenerator, monitor);
		return beliefMa;
	}

	@Override
	protected BeliefStateSpaceGenerator createStateSpaceGenerator() {
		return new BeliefStateSpaceGenerator();
	}
	
	protected BeliefStateSpaceGenerator createStateSpaceGenerator(boolean filterTransitions, boolean simplifyFailStates) {
		return new BeliefStateSpaceGenerator(filterTransitions, simplifyFailStates);
	}
}
