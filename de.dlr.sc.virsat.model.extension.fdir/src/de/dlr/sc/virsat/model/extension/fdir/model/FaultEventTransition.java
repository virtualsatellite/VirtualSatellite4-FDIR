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
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public  class FaultEventTransition extends AFaultEventTransition {
	
	/**
	 * Constructor of Concept Class
	 */
	public FaultEventTransition() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FaultEventTransition(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FaultEventTransition(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	

	@Override
	public String getGuardLabel() {
		String faultType = getIsRepair() ? "R" : "F";
		
		String guards = getGuards().stream().map(guard -> {
			StructuralElementInstance guardSei = VirSatEcoreUtil.getEContainerOfClass(guard.getATypeInstance(), StructuralElementInstance.class);
			String parentPrefix = guardSei != null ? (guard.getParent().getName() + ".") : "";
			return faultType + "(" +  parentPrefix + guard.toString() + ")";
		}).sorted().collect(Collectors.joining());
		
		return "{" + guards + "}";
	}

}
