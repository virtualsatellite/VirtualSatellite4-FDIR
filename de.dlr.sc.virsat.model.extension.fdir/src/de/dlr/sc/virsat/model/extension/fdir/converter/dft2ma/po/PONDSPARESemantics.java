/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class implements the partially observable nondeterministic spare gate semantics
 * based on the default spare gate semantics. In this semantics
 * each spare may be claimed nondeterministically.
 * @author muel_s8
 *
 */

public class PONDSPARESemantics extends NDSPARESemantics {
	
	@Override
	protected void updatePermanence(DFTState state, FaultTreeNode node, FaultTreeHolder ftHolder) {
		if (state.areFaultTreeNodesPermanent(ftHolder.getMapNodeToSubNodes().get(node))) {
			state.setFaultTreeNodePermanent(node, true);
		}
	}
	
	@Override
	protected boolean canClaim(DFTState pred, DFTState state, FaultTreeNode node, FaultTreeHolder ftHolder) {
		return !propagateWithoutActions;
	}
}
