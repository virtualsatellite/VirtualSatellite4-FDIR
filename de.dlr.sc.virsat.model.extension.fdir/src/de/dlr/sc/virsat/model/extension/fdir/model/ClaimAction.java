/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import java.util.Collections;
import java.util.List;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public  class ClaimAction extends AClaimAction {
	
	/**
	 * Constructor of Concept Class
	 */
	public ClaimAction() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public ClaimAction(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public ClaimAction(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	@Override
	public String toString() {
		if (getSpareGate() == null || getClaimSpare() == null) {
			return "Claim()";
		} else {
			StructuralElementInstance spareSei = VirSatEcoreUtil.getEContainerOfClass(getSpareGate().getATypeInstance(), StructuralElementInstance.class);
			StructuralElementInstance claimSei = VirSatEcoreUtil.getEContainerOfClass(getClaimSpare().getATypeInstance(), StructuralElementInstance.class);
			String sparePrefix = spareSei != null ? (getSpareGate().getParent().getName() + ".") : "";
			String claimPrefix = claimSei != null ? (getClaimSpare().getParent().getName() + ".") : "";
			return "Claim(" + sparePrefix +  getSpareGate().getName() +  ", " 
							+ claimPrefix + getClaimSpare().getName() + ")";	
		}
	}

	private FaultTreeNode claimSpare;
	private FaultTreeNode spareGate;
	
	public FaultTreeNode getClaimSpareByUUID(DFTState state) {
		FaultTreeNode claimSpareOriginal = getClaimSpare();
		return state.getFTHolder().getNodes().stream()
				.filter(node -> node.getUuid().equals(claimSpareOriginal.getUuid()))
				.findFirst().get();
	}
	
	public FaultTreeNode getSpareGateByUUID(DFTState state) {
		SPARE spareGateOriginal = getSpareGate();
		return state.getFTHolder().getNodes().stream()
				.filter(node -> node.getUuid().equals(spareGateOriginal.getUuid()))
				.findFirst().get();
	}
	
	@Override
	public void execute(DFTState state) {
		if (claimSpare == null) {
			claimSpare = getClaimSpareByUUID(state);
		}
		
		if (spareGate == null) {
			spareGate = getSpareGateByUUID(state);
		}
		
		state.getSpareClaims().put(claimSpare, spareGate);
		state.setNodeActivation(claimSpare, true);
		for (FaultTreeNode primary : state.getFTHolder().getMapNodeToChildren().getOrDefault(spareGate, Collections.emptyList())) {
			state.setNodeActivation(primary, false);
		}
	}
	
	@Override
	public String getActionLabel() {
		if (actionLabel != null) {
			return actionLabel;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Claim(");
		
		FaultTreeNode spareGate = getSpareGate();
		FaultTreeNode claimSpare = getClaimSpare();
		
		if (spareGate != null) {
			sb.append(spareGate.getUuid());
		} else {
			sb.append("null");
		}
		
		sb.append(",");
		
		if (claimSpare != null) {
			sb.append(claimSpare.getUuid());
		} else {
			sb.append("null");
		}
		
		sb.append(")");
		
		actionLabel = sb.toString();
		
		return actionLabel;
	}

	@Override
	public RecoveryAction copy() {
		ClaimAction copyClaimAction = new ClaimAction(getConcept());
		copyClaimAction.setClaimSpare(getClaimSpare());
		copyClaimAction.setSpareGate(getSpareGate());
		return copyClaimAction;
	}

	@Override
	public List<FaultTreeNode> getAffectedNodes(DFTState state) {
		if (spareGate == null) {
			spareGate = getSpareGateByUUID(state);
		}
		
		return Collections.singletonList(spareGate);
	}
}
