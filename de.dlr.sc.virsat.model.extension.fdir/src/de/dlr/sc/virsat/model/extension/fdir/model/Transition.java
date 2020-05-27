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

import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;

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
		String humanReadableActionLabel = getRecoveryActions().stream().map(RecoveryAction::toString).collect(Collectors.joining());
		return getGuardLabel() + " : " + (humanReadableActionLabel.isEmpty() ? "[]" : humanReadableActionLabel);
	}
	
	/**
	 * Gets a string representation for the actions of this transition
	 * @return a string representing the actions that should be performed upon executing this transition
	 */
	public String getActionLabel() {
		return getRecoveryActions().stream()
				.map(RecoveryAction::getActionLabel)
				.sorted()
				.collect(Collectors.joining());
	}
	
	public String getGuardLabel() {
		return "";
	}
}
