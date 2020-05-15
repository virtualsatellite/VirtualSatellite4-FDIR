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

import java.util.Objects;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
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
public abstract class Transition extends ATransition {
	
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
	 * Checks whether transitions are equivalent 
	 * @param transition to check the equivalence 
	 * @return true if equivalent, false otherwise 
	 */
	public boolean isEquivalentTransition(Transition transition) {
		return Objects.equals(getFrom(), transition.getFrom()) 
				&& Objects.equals(getTo(), transition.getTo()) 
				&& hasEquivalentRecoveryActions(transition);

	}
	
	/**
	 * Gets a string representation for the actions of this transition
	 * @return a string representing the actions that should be performed upon executing this transition
	 */
	public String getActionLabels() {
		return getRecoveryActions().stream()
				.map(RecoveryAction::getActionLabel).collect(Collectors.joining());
	}
}
