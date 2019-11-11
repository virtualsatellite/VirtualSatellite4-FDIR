/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

/**
 * @author piet_ci
 * 
 * This class acts as a factory for different types of matrices.
 *
 */
public class MatrixFactory {
	
	private MarkovAutomaton<? extends MarkovState> mc;
	
	/**
	 * @param mc MarkovChain
	 */
	public MatrixFactory(MarkovAutomaton<? extends MarkovState> mc) {
		this.mc = mc;		
	}

	
	/**
	 * @param failStatesAreTerminal failStatesAreTerminal
	 * @param delta delta
	 * @param eps eps
	 * @return transition matrix
	 */
	public TransitionMatrix getTransitionMatrix(boolean failStatesAreTerminal, double delta, double eps) {
		TransitionMatrix tm = new TransitionMatrix(mc);
		tm.setDelta(delta);
		tm.setEps(eps);
		tm.setFailStatesAreTerminal(failStatesAreTerminal);
		tm.createTransitionMatrix();
		return tm;
	}
	
	
}
