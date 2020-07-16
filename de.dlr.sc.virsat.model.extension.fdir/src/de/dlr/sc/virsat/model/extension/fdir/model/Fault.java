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

import java.util.List;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

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
public class Fault extends AFault {
	
	/**
	 * Constructor of Concept Class
	 */
	public Fault() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public Fault(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public Fault(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public FaultTreeNodeType getFaultTreeNodeType() {
		return FaultTreeNodeType.FAULT;
	}
	
	@Override
	public Fault getFault() {
		return this;
	}
	
	/**
	 * Checks if a given fault is locally (that is within the context of the sei it is attached to)
	 * a top level fault.
	 * @return true iff the given fault is a local top level fault
	 */
	public boolean isLocalTopLevelFault() {
		StructuralElementInstance parentSei = VirSatEcoreUtil.getEContainerOfClass(ti, StructuralElementInstance.class);
		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance(parentSei);
		List<Fault> otherFaults = parent.getAll(Fault.class);
		
		for (Fault potentialParentFault : otherFaults) {
			FaultTreeHolder ftHolder = new FaultTreeHolder(potentialParentFault);
			List<Fault> childFaults = ftHolder.getChildFaults(potentialParentFault);
			if (childFaults.contains(this)) {
				return false;
			}
		}
		
		return true;
	}
	
}
