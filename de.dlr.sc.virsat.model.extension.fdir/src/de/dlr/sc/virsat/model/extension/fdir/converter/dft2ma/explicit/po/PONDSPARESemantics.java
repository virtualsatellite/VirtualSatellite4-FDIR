/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.po;

import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.IStateGenerator;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class implements the partially observable nondeterministic spare gate semantics
 * based on the default spare gate semantics. In this semantics
 * each spare may be claimed nondeterministically.
 * @author muel_s8
 *
 */

public class PONDSPARESemantics extends NDSPARESemantics {
	
	private boolean propagateWithoutClaiming = false;
	
	/**
	 * Standard constructor
	 * @param stateGenerator the state generator
	 */
	public PONDSPARESemantics(IStateGenerator stateGenerator) {
		super(stateGenerator);
	}
	
	@Override
	public boolean handleUpdate(FaultTreeNode node, ExplicitDFTState state, ExplicitDFTState pred,
			FaultTreeHolder ftHolder, GenerationResult generationResult) {
		
		List<FaultTreeNode> spares = ftHolder.getMapNodeToSpares().get(node);
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		
		if (!propagateWithoutClaiming) {
			for (FaultTreeNode spare : spares) {
				if (!state.hasFaultTreeNodeFailed(spare) && !state.getMapSpareToClaimedSpares().containsKey(spare)) {
					performClaim((SPARE) node, spare, state, generationResult);
				}
			}
			
			if (state.hasFaultTreeNodeFailed(node)) {
				if (state.areFaultTreeNodesPermanent(children)) {
					state.setFaultTreeNodePermanent(node, true);
				}
				
				return true;
			}
			
			return false;
		}

		for (FaultTreeNode child : children) {
			if (!state.hasFaultTreeNodeFailed(child)) {
				return state.setFaultTreeNodeFailed(node, false);
			}
		}
		
		for (FaultTreeNode spare : spares) {
			if (!state.hasFaultTreeNodeFailed(spare)) {
				FaultTreeNode spareGate = state.getMapSpareToClaimedSpares().get(spare);
				if (spareGate != null && spareGate.equals(node)) {
					return false;
				}
			} 
		}
		
		return state.setFaultTreeNodeFailed(node, true);
	}
	
	/**
	 * Sets the propagation flag
	 * @param propagateWithoutClaiming whether this gate should prevent the propagation or not
	 */
	public void setPropagateWithoutClaiming(boolean propagateWithoutClaiming) {
		this.propagateWithoutClaiming = propagateWithoutClaiming;
	}
}
