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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Simple propagation semantics. A node with this semantics fails iff a child fails.
 * @author muel_s8
 *
 */

public class FaultSemantics implements INodeSemantics {

	@Override
	public boolean handleUpdate(FaultTreeNode node, ExplicitDFTState state, ExplicitDFTState pred,
			FaultTreeHolder ftHolder, GenerationResult generationResult) {
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		boolean hasFailed = false;
		
		Set<BasicEvent> basicEvents = ftHolder.getMapFaultToBasicEvents().getOrDefault(node, Collections.emptySet());
		for (BasicEvent be : basicEvents) {
			int nodeID = ftHolder.getNodeIndex(be);
			if (state.getFailedNodes().get(nodeID)) {
				hasFailed = true;
				break;
			}
		}
		
		if (hasFailed) {
			boolean hasPermanentlyFailed = false;
			
			for (BasicEvent be : basicEvents) {
				if (state.isFaultTreeNodePermanent(be)) {
					hasPermanentlyFailed = true;
					break;
				}
			}
			
			if (hasPermanentlyFailed) {
				state.setFaultTreeNodePermanent(node, true);
				return state.setFaultTreeNodeFailed(node, true);
			}
		}
		
		for (FaultTreeNode child : children) {
			if (state.hasFaultTreeNodeFailed(child)) {
				hasFailed = true;
				if (state.isFaultTreeNodePermanent(child)) {
					state.setFaultTreeNodePermanent(node, true);
					return state.setFaultTreeNodeFailed(node, true);
				}
			}
		}
		
		return state.setFaultTreeNodeFailed(node, hasFailed);
	}

}
