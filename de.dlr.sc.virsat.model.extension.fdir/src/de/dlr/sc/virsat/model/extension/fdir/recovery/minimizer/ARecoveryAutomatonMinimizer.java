/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Abstract base class for algorithms for minimizing recovery automata
 * @author muel_s8
 *
 */

public abstract class ARecoveryAutomatonMinimizer {

	private MinimizationStatistics statistics;

	/**
	 * Main method to override and perform the actual minimization
	 * @param raHolder the recovery automaton to minimize
	 * @param ftHolder the fault tree upon which the recovery automaton is based. Choose null
	 * if no additional fault tree information is supplied.
	 */
	protected abstract void minimize(RecoveryAutomatonHolder raHolder, FaultTreeHolder ftHolder);
	
	/**
	 * Main interface minimization method
	 * @param ra the recovery automaton to be minimized
	 * @param root the root of the associated fault tree
	 */
	public void minimize(RecoveryAutomaton ra, FaultTreeNode root) {
		statistics = new MinimizationStatistics();
		statistics.time = System.currentTimeMillis();
		statistics.removedStates = ra.getStates().size();
		statistics.removedTransitions = ra.getTransitions().size();
		
		minimize(new RecoveryAutomatonHolder(ra), new FaultTreeHolder(root));
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		statistics.removedStates = statistics.removedStates - ra.getStates().size();
		statistics.removedTransitions = statistics.removedTransitions - ra.getTransitions().size();
	}
	
	/**
	 * Main interface minimization method
	 * @param ra the recovery automaton to be minimized
	 */
	public void minimize(RecoveryAutomaton ra) {
		minimize(new RecoveryAutomatonHolder(ra), null);
	}
	
	/**
	 * Gets the internal statistics for the last minimization call
	 * @return the statistics for the last minimization call
	 */
	public MinimizationStatistics getStatistics() {
		return statistics;
	}
}
