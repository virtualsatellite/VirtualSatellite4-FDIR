/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

/**
 * Interface for converting a Fault tree to a Markov automaton.
 * @author muel_s8
 *
 */
public interface IDFT2MAConverter {
	
	/**
	 * Sets the node semantics for the converter
	 * @param dftSemantics the node semantics of the dft nodes
	 */
	void setSemantics(DFTSemantics dftSemantics);

	/**
	 * Converts a fault tree with the passed node as a root to a
	 * Markov automaton.
	 * @param root a fault tree node used as a root node for the conversion
	 * @return the generated Markov automaton resulting from the conversion
	 */
	MarkovAutomaton<DFTState> convert(FaultTreeNode root);

	/**
	 * Sets the recovery strategy
	 * @param recoveryStrategy the recovery strategy
	 */
	void setRecoveryStrategy(RecoveryStrategy recoveryStrategy);
}
