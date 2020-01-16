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
		
		boolean hasPrimaryFailed = hasPrimaryFailed(state, children);
		boolean currentClaimWorks = !hasPrimaryFailed;
		for (FaultTreeNode spare : spares) {
			FaultTreeNode spareGate = state.getMapSpareToClaimedSpares().get(spare);
			if (spareGate != null && spareGate.equals(node)) {
				if (!currentClaimWorks && !state.hasFaultTreeNodeFailed(spare)) {
					currentClaimWorks = true;
					
					if (!isSingleClaim()) {
						performFree((SPARE) node, spare, state, generationResult);
					}
				} else {
					performFree((SPARE) node, spare, state, generationResult);
				}
			}
		}
		
		if (currentClaimWorks) {
			return state.setFaultTreeNodeFailed(node, false);
		}
		
		boolean canClaim = canClaim(pred, state, node, ftHolder);
		boolean foundSpare = false;
		
		if (canClaim) {
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
			updatePermanence(state, node, ftHolder);
			return state.setFaultTreeNodeFailed(node, hasPrimaryFailed);
		} 
		
		return false;
	}
	
	/**
	 * Checks if all primary units have failed
	 * @param state the current state
	 * @param children the children
	 * @return true iff all primaries have failed
	 */
	protected boolean hasPrimaryFailed(DFTState state, List<FaultTreeNode> children) {
		for (FaultTreeNode child : children) {
			if (!state.hasFaultTreeNodeFailed(child) && state.isNodeActive(child)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Updates the permanence of the node in the current state
	 * @param state the state
	 * @param node the node
	 * @param ftHolder the fault tree holder
	 */
	protected void updatePermanence(DFTState state, FaultTreeNode node, FaultTreeHolder ftHolder) {
		if (state.areFaultTreeNodesPermanent(ftHolder.getMapNodeToChildren().get(node))) {
			state.setFaultTreeNodePermanent(node, true);
		}
	}
	
	/**
	 * Only spares gates whose spares / children have changed their state are allowed to claim
	 * @param pred predecessor state
	 * @param state current state
	 * @param node the node that needs claiming
	 * @param ftHolder the fault tree holder
	 * @return true iff the node is allowed to perform a claim
	 */
	protected boolean canClaim(DFTState pred, DFTState state, FaultTreeNode node, FaultTreeHolder ftHolder) {
		return state.hasFailStateChanged(pred, ftHolder.getMapNodeToSubNodes().get(node));
	}
	
	/**
	 * Checks if the node should be failed.
	 * @param foundSpare whether or not a spare was found
	 * @return true iff no spare was found
	 */
	protected boolean hasFailed(boolean foundSpare) {
		return !foundSpare;
	}
	
	/***
	 * Checks if one working claim is enough to satisfy a spare gate
	 * @return true iff the semantics are single claim
	 */
	protected boolean isSingleClaim() {
		return true;
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
		state.setNodeActivation(spare, true);
		return true;
	}
	
	/**
	 * Frees a spare from all claims
	 * @param spare the spare to be freed
	 * @param state the current state
	 * @param generationResult accumulator for state space generation results
	 * @return constant true
	 */
	protected void performFree(SPARE node, FaultTreeNode spare, DFTState state, GenerationResult generationResult) {
		FaultTreeNode claimingSpareGate = state.getMapSpareToClaimedSpares().get(spare);
		state.getMapSpareToClaimedSpares().remove(spare);
		state.setNodeActivation(spare, false);
		
		for (FaultTreeNode primary : state.getFTHolder().getMapNodeToChildren().getOrDefault(claimingSpareGate, Collections.emptyList())) {
			state.setNodeActivation(primary, true);
		}
	}
}
