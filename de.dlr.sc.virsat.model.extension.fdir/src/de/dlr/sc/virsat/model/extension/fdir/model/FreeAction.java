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
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFTState;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;

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
public  class FreeAction extends AFreeAction {
	
	/**
	 * Constructor of Concept Class
	 */
	public FreeAction() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FreeAction(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FreeAction(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	@Override
	public void execute(ExplicitDFTState state) {
		
	}
	
	@Override
	public String getActionLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append("Free(");
		
		FaultTreeNode spareGate = getFreeSpare();
		
		if (spareGate != null) {
			sb.append(spareGate.getUuid());
		} else {
			sb.append("null");
		}
		
		sb.append(")");
		
		return sb.toString();
	}
}
