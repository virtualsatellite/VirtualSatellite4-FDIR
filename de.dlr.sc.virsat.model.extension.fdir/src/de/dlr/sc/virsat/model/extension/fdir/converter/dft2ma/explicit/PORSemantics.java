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

import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Standard POR Gate semantics
 * @author muel_s8
 *
 */

public class PORSemantics implements INodeSemantics {

	@Override
	public boolean handleUpdate(FaultTreeNode node, ExplicitDFTState state, ExplicitDFTState pred,
			FaultTreeHolder ftHolder, GenerationResult generationResult) {
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		
		boolean firstChildFailed = state.hasFaultTreeNodeFailed(children.get(0));
		boolean existsOtherChildThatFailed = false;
		for (int i = 1; i < children.size(); ++i) {
			if (state.hasFaultTreeNodeFailed(children.get(i))) {
				existsOtherChildThatFailed = true;
				if (state.isFaultTreeNodePermanent(children.get(i))) {
					state.setFaultTreeNodePermanent(node, true);
				}
				break;
			}
		}
		
		if (existsOtherChildThatFailed) {
			return state.setFaultTreeNodeFailed(node, false);
		} else if (firstChildFailed) {
			if (state.isFaultTreeNodePermanent(children.get(0))) {
				state.setFaultTreeNodePermanent(node, true);
			}
			return state.setFaultTreeNodeFailed(node, true);
		}
		
		return false;
	}

}
