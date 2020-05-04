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

import java.util.Collections;
import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class FDEPSemantics implements INodeSemantics {
	@Override
	public boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred,
			GenerationResult generationResult) {
		FaultTreeHolder ftHolder = state.getFTHolder();
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		List<FaultTreeNode> parents = ftHolder.getMapNodeToParents().get(node);

		boolean stateChanged = false;
		boolean hasFailed = false;
		// Permanent Nodes
		List<FaultTreeNode> basicEvents = ftHolder.getMapFaultToBasicEvents().getOrDefault(node, Collections.emptyList());
		for (FaultTreeNode be : basicEvents) {
			int nodeID = ftHolder.getNodeIndex(be);
			if (state.getFailedNodes().get(nodeID)) {
				hasFailed = true;
				break;
			}
		}

		if (hasFailed) {
			boolean hasPermanentlyFailed = false;

			for (FaultTreeNode be : basicEvents) {
				if (state.isFaultTreeNodePermanent(be)) {
					hasPermanentlyFailed = true;
					break;
				}
			}

			if (hasPermanentlyFailed) {
				state.setFaultTreeNodePermanent(node, true);
				stateChanged = state.setFaultTreeNodeFailed(node, true);
			}
		}

		for (FaultTreeNode child : children) {
			if (state.hasFaultTreeNodeFailed(child)) {
				hasFailed = true;
				state.setFaultTreeNodePermanent(node, true);
				stateChanged = state.setFaultTreeNodeFailed(node, true);
				for (FaultTreeNode parent : parents) {
					hasFailed = true;
					state.setFaultTreeNodePermanent(parent, true);
					state.setFaultTreeNodeFailed(parent, true);
				}
			} else {
				for (FaultTreeNode parent : parents) {

					hasFailed = true;

					state.setFaultTreeNodePermanent(parent, false);
					state.setFaultTreeNodeFailed(parent, false);

				}
			}
		}

		return stateChanged;
	}

}
