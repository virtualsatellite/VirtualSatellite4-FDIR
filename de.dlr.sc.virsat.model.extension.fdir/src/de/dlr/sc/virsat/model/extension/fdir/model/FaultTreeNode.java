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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.command.CreateDeleteWithReferencingFDIRCategoryAssignmentsCommand;

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
public class FaultTreeNode extends AFaultTreeNode {
	
	/**
	 * Constructor of Concept Class
	 */
	public FaultTreeNode() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FaultTreeNode(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FaultTreeNode(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Gets the fault tree node type of this node
	 * @return the fault tree node type of this node
	 */
	public FaultTreeNodeType getFaultTreeNodeType() {
		return null;	
	}
	
	/**
	 * Gets the fault this fault tree node is associated with
	 * @return the fault this fault tree node is associated with
	 */
	public Fault getFault() {
		return null;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public Command delete(EditingDomain ed) {
		return new CreateDeleteWithReferencingFDIRCategoryAssignmentsCommand().create(ed, ti);
	}

	/**
	 * Checks if this node has the same properties as another node
	 * @param other the other node
	 * @return true iff the node has the same properties as the other passed node
	 */
	public boolean hasSameProperties(FaultTreeNode other) {
		return getFaultTreeNodeType().equals(other.getFaultTreeNodeType());
	}
}
