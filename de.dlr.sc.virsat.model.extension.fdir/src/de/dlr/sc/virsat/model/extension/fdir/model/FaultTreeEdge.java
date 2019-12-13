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

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
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
public class FaultTreeEdge extends AFaultTreeEdge {
	
	/**
	 * Constructor of Concept Class
	 */
	public FaultTreeEdge() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FaultTreeEdge(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FaultTreeEdge(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Method to access the RPI which will be needed by the Validator
	 * to indicate the correct target of where warning come from.
	 * @return the EMF ReferencePropertyInstance
	 */
	public ReferencePropertyInstance getFromReferenceProperty() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance(PROPERTY_FROM);
		return propertyInstance;
	}
	
	/**
	 * Method to access the RPI which will be needed by the Validator
	 * to indicate the correct target of where warning come from.
	 * @return the EMF ReferencePropertyInstance
	 */
	public ReferencePropertyInstance getToReferenceProperty() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance(PROPERTY_TO);
		return propertyInstance;
	}
}
