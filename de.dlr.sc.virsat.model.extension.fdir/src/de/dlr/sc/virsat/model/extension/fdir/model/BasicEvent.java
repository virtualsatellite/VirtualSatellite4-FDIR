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
public  class BasicEvent extends ABasicEvent {
	
	/**
	 * Constructor of Concept Class
	 */
	public BasicEvent() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public BasicEvent(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public BasicEvent(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Gets the fault containing this basic event
	 * @return the fault containing this basic event
	 */
	public Fault getFault() {
		return new Fault((CategoryAssignment) getTypeInstance().eContainer().eContainer().eContainer());
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public FaultTreeNodeType getFaultTreeNodeType() {
		return FaultTreeNodeType.BASIC_EVENT;
	}
	
	@Override
	public boolean hasSameProperties(FaultTreeNode other) {
		if (!super.hasSameProperties(other)) {
			return false;
		}
		
		BasicEvent be = (BasicEvent) other;
		return getHotFailureRateBean().getValueToBaseUnit() == be.getHotFailureRateBean().getValueToBaseUnit();
	}
}
