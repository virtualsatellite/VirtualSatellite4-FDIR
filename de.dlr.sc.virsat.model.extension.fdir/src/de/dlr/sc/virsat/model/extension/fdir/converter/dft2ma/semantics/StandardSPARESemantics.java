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
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Standard DFT Spare gate semantics. In this semantics spares are claimed
 * from left to right.
 * @author muel_s8
 *
 */

public class StandardSPARESemantics implements INodeSemantics {
	
	@Override
	public boolean handleUpdate(FaultTreeNode node, DFTState state, DFTState pred,
			GenerationResult generationResult) {
		
		if (!(node instanceof SPARE)) {
			throw new IllegalArgumentException("Expected node of type SPARE but instead got node " + node);
		}
		
		FaultTreeHolder ftHolder = state.getFTHolder();
		SPARE spareGate = (SPARE) node;
		
		List<FaultTreeNode> spares = ftHolder.getNodes(spareGate, EdgeType.SPARE);
		List<FaultTreeNode> children = ftHolder.getNodes(spareGate, EdgeType.CHILD);
		
		boolean hasPrimaryFailed = hasPrimaryFailed(state, children);
		boolean isClaiming = false;
		for (FaultTreeNode spare : spares) {
			FaultTreeNode spareGateOther = state.getMapSpareToClaimedSpares().get(spare);
			if (spareGateOther != null && spareGateOther.equals(spareGate)) {
				isClaiming = true;
			}
		}
		boolean currentClaimWorks = !isClaiming && !hasPrimaryFailed;
		for (FaultTreeNode spare : spares) {
			FaultTreeNode spareGateOther = state.getMapSpareToClaimedSpares().get(spare);
			if (spareGateOther != null && spareGateOther.equals(spareGate)) {
				if (!currentClaimWorks && !state.hasFaultTreeNodeFailed(spare)) {
					currentClaimWorks = true;
					
					if (!isSingleClaim()) {
						performFree(spare, state, generationResult);
					}
				} else {
					performFree(spare, state, generationResult);
				}
			}
		}
		
		if (currentClaimWorks && isSingleClaim()) {
			return state.setFaultTreeNodeFailed(spareGate, false);
		}
		
		boolean canClaim = canClaim(pred, state, spareGate, ftHolder);
		boolean foundSpare = false;
		
		if (canClaim) {
			for (FaultTreeNode spare : spares) {
				if (performClaim(spareGate, spare, state, generationResult)) {
					foundSpare = true;
					break;
				}
			}
		}
		
		if (hasFailed(foundSpare)) {
			updatePermanence(state, spareGate, ftHolder);
			return state.setFaultTreeNodeFailed(spareGate, !currentClaimWorks);
		} 
		
		if (foundSpare) {
			return state.setFaultTreeNodeFailed(node, false);
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
			if (!state.hasFaultTreeNodeFailed(child) && state.isNodeActive(child.getFault())) {
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
		if (state.areFaultTreeNodesPermanent(ftHolder.getNodes(node, EdgeType.CHILD))
				&& state.areFaultTreeNodesPermanent(ftHolder.getNodes(node, EdgeType.SPARE))) {
			state.setFaultTreeNodePermanent(node, true);
		}
	}
	
	/**
	 * Override to restrict when a spare gate is allowed to claim
	 * @param pred predecessor state
	 * @param state current state
	 * @param node the node that needs claiming
	 * @param ftHolder the fault tree holder
	 * @return true iff the node is allowed to perform a claim
	 */
	protected boolean canClaim(DFTState pred, DFTState state, FaultTreeNode node, FaultTreeHolder ftHolder) {
		return true;
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
		
		if (state.hasFaultTreeNodeFailed(spare)) {
			return false;
		}
		
		if (state.getMapSpareToClaimedSpares().containsKey(spare)) {
			return false;
		}
			
		state.getMapSpareToClaimedSpares().put(spare, node);
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
	protected void performFree(FaultTreeNode spare, DFTState state, GenerationResult generationResult) {
		FaultTreeNode claimingSpareGate = state.getMapSpareToClaimedSpares().get(spare);
		state.getMapSpareToClaimedSpares().remove(spare);
		state.setNodeActivation(spare, false);
		
		for (FaultTreeNode primary : state.getFTHolder().getNodes(claimingSpareGate, EdgeType.CHILD)) {
			state.setNodeActivation(primary, true);
		}
	}
}
