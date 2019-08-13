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

// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyPL;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;

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
public  class FDIRParameters extends AFDIRParameters {
	
	/**
	 * Constructor of Concept Class
	 */
	public FDIRParameters() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FDIRParameters(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FDIRParameters(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Sets the probability level thresholds to the default ones
	 */
	public void setDefaultProbablityThresholds() {
		for (int i = 0; i < OpClassifyPL.DEFAULT_PL_THRESHOLDS.length; ++i) {
			BeanPropertyFloat beanFloat = getProbabilityLevels().get(i);
			beanFloat.setValueAsBaseUnit(OpClassifyPL.DEFAULT_PL_THRESHOLDS[i]);
		}
	}
}
