/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Handles the update of a given fault tree node according to the implemented semantics.
 * @author muel_s8
 *
 */

public interface INodeSemantics {
	
	/**
	 * Update method.
	 * @param node the node to update
	 * @param state the current state
	 * @param pred the predecessor state
	 * @param ftHolder fault tree data
	 * @param generationResult accumulator for state space generation results
	 * @return true iff the node state changed
	 */
	boolean handleUpdate(FaultTreeNode node, ExplicitDFTState state, ExplicitDFTState pred, FaultTreeHolder ftHolder, GenerationResult generationResult);
}
