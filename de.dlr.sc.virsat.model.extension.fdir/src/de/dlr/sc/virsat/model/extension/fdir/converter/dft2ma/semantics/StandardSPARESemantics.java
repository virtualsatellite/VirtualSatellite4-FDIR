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
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Standard DFT Spare gate semantics. In this semantics spares are claimed
 * from left to right.
 * @author muel_s8
 *
 */

public class StandardSPARESemantics implements INodeSemantics {
	
	@Override
	public boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred, FaultTreeHolder ftHolder,
			GenerationResult generationResult) {
		List<FaultTreeNode> spares = ftHolder.getMapNodeToSpares().get(node);
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		
		for (FaultTreeNode child : children) {
			if (!state.hasFaultTreeNodeFailed(child)) {
				for (FaultTreeNode spare : spares) {
					FaultTreeNode claimingGate = state.getMapSpareToClaimedSpares().get(spare);
					if (claimingGate != null && claimingGate.equals(node)) {
						state.getMapSpareToClaimedSpares().remove(spare);
					}
				}
				return state.setFaultTreeNodeFailed(node, false);
			}
		}
		
		for (FaultTreeNode spare : spares) {
			if (!state.hasFaultTreeNodeFailed(spare)) {
				FaultTreeNode spareGate = state.getMapSpareToClaimedSpares().get(spare);
				if (spareGate != null && spareGate.equals(node)) {
					return false;
				}
			} else {
				state.getMapSpareToClaimedSpares().remove(spare);
			}
		}
		
		boolean childHasChanged = state.hasFailStateChanged(pred, children) || state.hasFailStateChanged(pred, spares);
		boolean foundSpare = false;
		
		// Only spares gates whose spares / children have changed their state are allowed to claim
		if (childHasChanged) {
			for (FaultTreeNode spare : spares) {
				if (!state.hasFaultTreeNodeFailed(spare) && !state.getMapSpareToClaimedSpares().containsKey(spare)) {
					if (performClaim((SPARE) node, spare, state, generationResult)) {
						foundSpare = true;
						break;
					}
				}
			}
		}
		
		if (hasFailed(foundSpare)) {
			if (state.areFaultTreeNodesPermanent(children)) {
				state.setFaultTreeNodePermanent(node, true);
			}
			return state.setFaultTreeNodeFailed(node, true);
		} 
		
		return false;
	}
	
	/**
	 * Checks if the node should be failed.
	 * @param foundSpare whether or not a spare was found
	 * @return true iff no spare was found
	 */
	protected boolean hasFailed(boolean foundSpare) {
		return !foundSpare;
	}
	
	/**
	 * Restores a failed spare gate node by switching to a spare.
	 * @param node the spare gate node
	 * @param spare the spare to switch to
	 * @param state the current state
	 * @param generationResult accumulator for state space generation results
	 * @return constant true
	 */
	protected boolean performClaim(SPARE node, FaultTreeNode spare, DFTState state, 
			GenerationResult generationResult) {
		state.getSpareClaims().put(spare, node);
		state.activateNode(spare);
		return true;
	}
}