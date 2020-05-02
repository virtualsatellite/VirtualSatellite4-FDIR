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
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Simple propagation semantics. A node with this semantics fails iff a child fails.
 * @author muel_s8
 *
 */

public class DelaySemantics implements INodeSemantics {

	@Override
	public boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred,
			GenerationResult generationResult) {
		FaultTreeHolder ftHolder = state.getFTHolder();
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		
		for (FaultTreeNode child : children) {
			if (state.hasFaultTreeNodeFailed(child)) {
				return state.setFaultTreeNodeFailing(node, true);
			}
		}
		
		return state.setFaultTreeNodeFailing(node, false) | state.setFaultTreeNodeFailed(node, false);
	}

}
