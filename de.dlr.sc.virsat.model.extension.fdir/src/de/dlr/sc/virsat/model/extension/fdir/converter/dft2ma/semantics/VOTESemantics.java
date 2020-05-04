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

import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Standard VOTE gate semantics.
 * @author muel_s8
 *
 */

public class VOTESemantics implements INodeSemantics {

	@Override
	public boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred,
			GenerationResult generationResult) {
		
		if (!(node instanceof VOTE)) {
			throw new IllegalArgumentException("Expected node of type VOTE but got node " + node);
		}
		
		FaultTreeHolder ftHolder = state.getFTHolder();
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		
		int failed = 0;
		int permanentlyFailed = 0;
		
		for (FaultTreeNode child : children) {
			if (state.hasFaultTreeNodeFailed(child)) {
				failed++;
				if (state.isFaultTreeNodePermanent(child)) {
					permanentlyFailed++;
				}
			}
		}
		
		long votingThreshold = ((VOTE) node).getVotingThreshold();
		boolean hasFailed = failed >= votingThreshold;
		
		if (hasFailed) {
			boolean hasPermanentlyFailed = permanentlyFailed >= votingThreshold;
			if (hasPermanentlyFailed) {
				state.setFaultTreeNodePermanent(node, true);
			} 
		}
		
		return state.setFaultTreeNodeFailed(node, hasFailed);
	}
}
