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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

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
public class Transition extends ATransition {
	
	/**
	 * Constructor of Concept Class
	 */
	public Transition() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public Transition(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public Transition(CategoryAssignment categoryAssignment) {
		super(categoryAssignment); 
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		String faultType = getIsRepair() ? "R" : "F";
		sb.append("{");
		
		for (FaultTreeNode guard : getGuards()) {
			StructuralElementInstance guardSei = VirSatEcoreUtil.getEContainerOfClass(guard.getATypeInstance(), StructuralElementInstance.class);
			String parentPrefix = guardSei != null ? (guard.getParent().getName() + ".") : "";
			sb.append(faultType + "(" +  parentPrefix + guard.toString() + ")");
		}

		sb.append("}");
		sb.append(" : ");
		
		if (getRecoveryActions().isEmpty()) {
			sb.append("[]");
		} else {
			for (RecoveryAction ra : getRecoveryActions()) {
				sb.append(ra.toString());
				sb.append(" ");
			}
		}

		
		return sb.toString();
	}
	
	/**
	 * Checks whether transitions contais equivalent recovery actions
	 * @param transition to check the recovery actions 
	 * @return true if contains, false otherwise 
	 */
	public boolean hasEquivalentRecoveryActions(Transition transition) {
		return new FaultTreeHelper(concept).hasEquivalentRecoveryActions(getRecoveryActions(), transition.getRecoveryActions());
	}
	
	/**
	 * Checks whether transitions have equivalent guards 
	 * @param transition to which the guards with 
	 * @return true if guards are equivalent, and false otherwise  
	 */
	public boolean hasEquivalentGuards(Transition transition) {
		Set<FaultTreeNode> guards = new HashSet<>(getGuards());
		Set<FaultTreeNode> guardsOther = new HashSet<>(transition.getGuards());
		
		return guards.equals(guardsOther);
	}
	
	/**
	 * Checks whether transitions are equivalent 
	 * @param transition to check the equivalence 
	 * @return true if equivalent, false otherwise 
	 */
	public boolean isEquivalentTransition(Transition transition) {
		return Objects.equals(getFrom(), transition.getFrom()) && Objects.equals(getTo(), transition.getTo()) && hasEquivalentGuards(transition) && hasEquivalentRecoveryActions(transition);
	}
}
