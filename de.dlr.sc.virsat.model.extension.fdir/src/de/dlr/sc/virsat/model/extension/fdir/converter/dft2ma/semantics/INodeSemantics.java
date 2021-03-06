/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;


/**
 * Handles the update of a given fault tree node according to the implemented semantics.
 *
 */

public interface INodeSemantics {
	
	/**
	 * Update method.
	 * @param node the node to update
	 * @param state the current state
	 * @param pred the predecessor state
	 * @param generationResult accumulator for state space generation results
	 * @return true iff the node state changed
	 */
	boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred, GenerationResult generationResult);
}
