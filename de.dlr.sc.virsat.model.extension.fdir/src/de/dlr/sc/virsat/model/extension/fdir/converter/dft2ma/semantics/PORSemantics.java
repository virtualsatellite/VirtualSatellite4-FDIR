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
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Standard POR Gate semantics
 * @author muel_s8
 *
 */

public class PORSemantics implements INodeSemantics {

	@Override
	public boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred,
			GenerationResult generationResult) {
		FaultTreeHolder ftHolder = state.getFTHolder();
		List<FaultTreeNode> children = ftHolder.getNodes(node, EdgeType.CHILD);
		boolean hasChanged = false;
		
		FaultTreeNode firstChild = children.get(0);
		boolean firstChildFailed = state.hasFaultTreeNodeFailed(firstChild);
		
		if (!pred.hasFaultTreeNodeFailed(node)) {
			for (int i = 1; i < children.size(); ++i) {
				if (state.hasFaultTreeNodeFailed(children.get(i))) {
					// There exists another child besides the fist one that has failed
					hasChanged |= state.setFaultTreeNodeFailed(node, false);
					if (state.isFaultTreeNodePermanent(children.get(i))) {
						hasChanged |= state.setFaultTreeNodePermanent(node, true);
					}
					return hasChanged;
				}
			}
		}
		
		
		if (firstChildFailed) {
			if (state.isFaultTreeNodePermanent(firstChild)) {
				hasChanged |= state.setFaultTreeNodePermanent(node, true);
			}
			hasChanged |= state.setFaultTreeNodeFailed(node, true);
		}
		
		return hasChanged;
	}

}
