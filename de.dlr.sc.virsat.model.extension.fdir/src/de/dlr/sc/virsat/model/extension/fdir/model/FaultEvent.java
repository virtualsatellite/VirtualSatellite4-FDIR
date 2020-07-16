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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

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
public abstract class FaultEvent extends AFaultEvent {
	
	/**
	 * Constructor of Concept Class
	 */
	public FaultEvent() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FaultEvent(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FaultEvent(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	/**
	 * Gets all recovery actions directly contained in this fault tree
	 * that can contribute to recovering the top level fault of this fault tree
	 * @param parentFault 
	 * @return a list of all local recovery actions
	 */
	public List<String> getCompensations(FaultTreeHolder ftHolder) {
		FaultTreeNode root = ftHolder.getRoot();
		List<FaultTreeNode> intermediateNodes = new ArrayList<>();
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		
		toProcess.add(this);
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode currentNode = toProcess.poll();
			
			// Get the parents that 
			List<FaultTreeNode> parents = ftHolder.getNodes(currentNode, EdgeType.PARENT);
			for (FaultTreeNode parent : parents) {
				Set<FaultTreeNode> allParents = ftHolder.getMapNodeToAllParents().get(parent);
				if (allParents.contains(root)) {
					if (!intermediateNodes.contains(parent)) {
						intermediateNodes.add(parent);
						toProcess.add(parent);
					}
				}
			}
		}
		
		List<String> compensations = new ArrayList<>();
		for (FaultTreeNode intermediateNode : intermediateNodes) {
			if (intermediateNode instanceof SPARE) {
				for (FaultTreeNode spare : ftHolder.getNodes(intermediateNode, EdgeType.SPARE)) {
					String recoveryAction = "Switch to " + (spare.getParent() != null ? spare.getParent().getName() + "." : "") + spare.getName();
					if (!compensations.contains(recoveryAction)) {
						compensations.add(recoveryAction);
					}
				}
			} else if (intermediateNode instanceof AND 
					|| (intermediateNode instanceof VOTE && ((VOTE) intermediateNode).getVotingThreshold() > 1)) {
				List<Fault> childFaults = ftHolder.getChildFaults(intermediateNode);
				for (Fault fault : childFaults) {
					if (!fault.equals(this)) {
						String compensation = (fault.getParent() != null ? fault.getParent().getName() + "." : "") + fault.getName();
						if (!compensations.contains(compensation)) {
							compensations.add(compensation);
						}
					}
				}
			}
		}
		
		return compensations;
	}
}
