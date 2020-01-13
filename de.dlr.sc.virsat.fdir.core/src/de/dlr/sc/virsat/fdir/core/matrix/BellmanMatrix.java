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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

public class BellmanMatrix extends TransitionMatrix {

	public BellmanMatrix(int countStates) {
		super(countStates);
	}
	
	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double eps) {
		return new BellmanIterator(this, probabilityDistribution, eps);
	}
		
	/**
	 * Gets the initial MTTF according to the Bellman equations with
	 * MTTF(s) = 0 if s is a fail state and 
	 * MTTF(s) = 1/ExitRate(s) if s is not a fail state
	 * @param mc 
	 * @return the initial probability distribution
	 */
	public static double[] getInitialMTTFVector(MarkovAutomaton<? extends MarkovState> mc) {
		int countStates = mc.getStates().size();
		double[] inititalVector = new double[countStates];

		Queue<MarkovState> toProcess = new LinkedList<>();
		toProcess.addAll(mc.getFinalStates());
		Set<MarkovState> failableStates = new HashSet<>();

		while (!toProcess.isEmpty()) {
			MarkovState state = toProcess.poll();
			if (failableStates.add(state)) {
				List<?> transitions = mc.getPredTransitions(state);
				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions
							.get(j);
					toProcess.add(transition.getFrom());
				}
			}
		}

		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			if (!mc.getFinalStates().contains(state)) {
				if (!failableStates.contains(state)) {
					inititalVector[i] = Double.POSITIVE_INFINITY;
					continue;
				}
				inititalVector[i] = mc.getExitRateForState(state);
			}
		}
		return inititalVector;
	}
}