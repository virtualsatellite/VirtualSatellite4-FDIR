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

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;

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
public abstract class RecoveryAction extends ARecoveryAction {
	
	protected String actionLabel;
	
	/**
	 * Constructor of Concept Class
	 */
	public RecoveryAction() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public RecoveryAction(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public RecoveryAction(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	
	/**
	 * Executes the recovery action on the given DFT state
	 * @param state the current DFT state
	 */
	public abstract void execute(DFTState state);
	
	/**
	 * Create a copy of this recovery action
	 * @return a copy of the recovery action
	 */
	public abstract RecoveryAction copy();
	
	/**
	 * Gets an action label from this recovery action such that
	 * if r1.getActionLabel().equals(r2.getActionLabel()) it holds that
	 * r1 and r2 are equivalent recovery actions.
	 * @return action label
	 */
	public abstract String getActionLabel();
	
	/**
	 * Gets the nodes whose fail state might be affected
	 * by the recovery action
	 * @param state the current states
	 * @return the list of affected nodes
	 */
	public abstract List<FaultTreeNode> getAffectedNodes(DFTState state);
}
