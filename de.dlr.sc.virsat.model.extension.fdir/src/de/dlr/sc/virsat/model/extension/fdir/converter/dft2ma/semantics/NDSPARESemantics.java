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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FreeAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class implements the nondeterministic spare gate semantics
 * based on the default spare gate semantics. In this semantics
 * each spare may be claimed nondeterministically.
 * @author muel_s8
 *
 */

public class NDSPARESemantics extends StandardSPARESemantics {
	
	private Map<FaultTreeNode, Map<FaultTreeNode, ClaimAction>> mapNodeToNodeToClaimAction = new HashMap<>();
	private Map<FaultTreeNode, FreeAction> mapNodeToFreeAction = new HashMap<>();
	
	protected boolean propagateWithoutActions = false;

	@Override
	protected boolean hasFailed(boolean foundSpare) {
		return true;
	}

	@Override
	protected boolean isSingleClaim() {
		return propagateWithoutActions;
	}
	
	@Override
	protected boolean canClaim(DFTState pred, DFTState state, FaultTreeNode node, FaultTreeHolder ftHolder) {
		return !propagateWithoutActions && super.canClaim(pred, state, node, ftHolder);
	}
	
	@Override
	protected boolean performClaim(SPARE node, FaultTreeNode spare, DFTState state,
			GenerationResult generationResult) {
		
		DFTState newState = null;
		List<RecoveryAction> extendedRecoveryActions = null;
		List<RecoveryAction> recoveryActions = generationResult.getMapStateToRecoveryActions().get(state);
		
		for (RecoveryAction ra : recoveryActions) {
			if (ra instanceof ClaimAction) {
				ClaimAction otherClaimAction = (ClaimAction) ra;
				if (otherClaimAction.getClaimSpare().equals(spare)) {
					return false;
				}
			} else if (ra instanceof FreeAction) {
				FreeAction freeAction = (FreeAction) ra;
				if (freeAction.getFreeSpare().equals(spare)) {
					return false;
				}
			}
		}
		
		DFTState generatorState = generationResult.getGeneratorState();
		FaultTreeNode currentClaimantGenerator = generatorState.getMapSpareToClaimedSpares().get(spare);
		
		if (currentClaimantGenerator != null && currentClaimantGenerator.equals(node)) {
			return false;
		}
		
		FaultTreeNode currentClaimant = state.getMapSpareToClaimedSpares().get(spare);
		boolean reclaim = currentClaimant != null;
		
		if (reclaim && currentClaimant.equals(node)) {
			return false;
		}
		
		if (reclaim) {
			performFree(node, spare, state, generationResult);
			newState = generationResult.getGeneratedStates().get(generationResult.getGeneratedStates().size() - 1);
			extendedRecoveryActions = generationResult.getMapStateToRecoveryActions().get(newState);
		} else {
			newState = state.copy();
			extendedRecoveryActions = new ArrayList<>(recoveryActions);
		}
		
		ClaimAction ca = getOrCreateClaimAction(node, spare, mapNodeToNodeToClaimAction);
		ca.execute(newState);

		extendedRecoveryActions.add(ca);
		generationResult.getMapStateToRecoveryActions().put(newState, extendedRecoveryActions);
		
		if (!reclaim) {
			generationResult.getGeneratedStates().add(newState);
		}
		
		return false;
	}
	
	/**
	 * Gets or creates a new claim action
	 * @param gate the gate doing the claiming
	 * @param spare the spare getting claimed
	 * @param mapNodeToNodeToClaimAction a cashing of existing claim actions
	 * @return a claim action
	 */
	protected ClaimAction getOrCreateClaimAction(SPARE gate, FaultTreeNode spare, Map<FaultTreeNode, Map<FaultTreeNode, ClaimAction>> mapNodeToNodeToClaimAction) {
		Map<FaultTreeNode, ClaimAction> mapNodeToClaimAction = mapNodeToNodeToClaimAction.get(gate);
		if (mapNodeToClaimAction == null) {
			mapNodeToClaimAction = new HashMap<>();
			mapNodeToNodeToClaimAction.put(gate, mapNodeToClaimAction);
		}
		
		ClaimAction ca = mapNodeToClaimAction.get(spare);
		if (ca == null) {
			ca = new ClaimAction(spare.getConcept());
			ca.setSpareGate(gate);
			ca.setClaimSpare(spare);
			mapNodeToClaimAction.put(spare, ca);
		}
		
		return ca;
	}
	
	/**
	 * Frees a spare from all claims
	 * @param spare the spare to be freed
	 * @param state the current state
	 * @param generationResult accumulator for state space generation results
	 * @return constant true
	 */
	protected void performFree(SPARE node, FaultTreeNode spare, DFTState state, GenerationResult generationResult) {
		if (!propagateWithoutActions) {
			List<RecoveryAction> recoveryActions = generationResult.getMapStateToRecoveryActions().get(state);
	
			FreeAction fa = getOrCreateFreeAction(spare);
			DFTState newState = state.copy();
			fa.execute(newState);
	
			newState.setFaultTreeNodeFailed(node, hasPrimaryFailed(newState, newState.getFTHolder().getNodes(node, EdgeType.CHILD)));
			List<RecoveryAction> extendedRecoveryActions = new ArrayList<>(recoveryActions);
	
			extendedRecoveryActions.add(fa);
			generationResult.getMapStateToRecoveryActions().put(newState, extendedRecoveryActions);
	
			generationResult.getGeneratedStates().add(newState);
		}
	}

	/**
	 * Creates a new free action or recycles an existing one if possible
	 * @param spare the spare to free
	 * @return the created free action
	 */
	protected FreeAction getOrCreateFreeAction(FaultTreeNode spare) {
		FreeAction fa = mapNodeToFreeAction.get(spare);
		if (fa == null) {
			fa = new FreeAction(spare.getConcept());
			fa.setFreeSpare(spare);
			mapNodeToFreeAction.put(spare, fa);
		}
		return fa;
	}
	
	/**
	 * Sets the propagation flag
	 * @param propagateWithoutActions whether this gate should prevent the propagation or not
	 */
	public void setPropagateWithoutActions(boolean propagateWithoutActions) {
		this.propagateWithoutActions = propagateWithoutActions;
	}
}
