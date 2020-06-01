/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.modularizer;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Links each FaultTreeNode in a FaultTree with the first and last date visited.
 * For use with Modularizer
 * @author jord_ad
 *
 */

public class FaultTreeNodePlus {
	
	private FaultTreeNode node;
	private Set<FaultTreeNodePlus> visitedFrom = new HashSet<FaultTreeNodePlus>();
	private int firstVisit;
	private int lastVisit;
	private int depth;
	
	private boolean harvested = false;
	private boolean hasPriorityAbove = false;
	private boolean hasSpareAbove = false;
	private boolean hasSpareBelow = false;
	private boolean isDEPDescendant = false;
	
	private List<FaultTreeNodePlus> children = new ArrayList<FaultTreeNodePlus>();
	
	/* CONSTRUCTORS */
	
	/**
	 * Constructor
	 * @param reference the FaultTreeNode reference
	 * @param depth the depth
	 */
	public FaultTreeNodePlus(FaultTreeNode reference, int depth) {
		this.node = reference;
		this.depth = depth;
	}

	/** 
	 * Visit the node
	 * @param visitDate the date on which the node is visited
	 */
	public void visit(int visitDate) {
		if (this.firstVisit == 0) {
			this.firstVisit = visitDate;
		} 
		
		this.lastVisit = visitDate;
	}
	
	/* ***********************
	 * GETTERS
	 *********************** */
	/**
	 * Get the first visit date
	 * @return first visit date
	 */
	public int getFirstVisit() {
		return this.firstVisit;
	}
	
	/**
	 * Get the last visit date
	 * @return last visit date
	 */
	public int getLastVisit() {
		return this.lastVisit;
	}
	
	/**
	 * Get the reference FaultTreeNode
	 * @return the reference FaultTreeNode
	 */
	public FaultTreeNode getFaultTreeNode() {
		return this.node;
	}
	
	/** 
	 * Get depth of the node in the fault tree
	 * @return the depth, where root has depth 0
	 */
	public int getDepth() {
		return this.depth;
	}
	
	/**
	 * Has the node been harvested from the fault tree
	 * @return true if harvested, false otherwise.
	 */
	boolean isHarvested() {
		return this.harvested;
	}
	
	/**
	 * Get the nodes this node has been visited from
	 * @return the set of nodes this node has been visited from
	 */
	Set<FaultTreeNodePlus> getSetFrom() {
		return this.visitedFrom;
	}
	
	/**
	 * Determine if this node has a priority gate above
	 * @return true if has priority above, false otherwise
	 */
	boolean hasPriorityAbove() {
		return this.hasPriorityAbove;
	}
	
	/**
	 * Determine if this node has a spare gate above
	 * @return true if has spare above, false otherwise
	 */
	boolean hasSpareAbove() {
		return this.hasSpareAbove;
	}
	
	/**
	 * Determine if this node is a DEP descendant
	 * @return true if DEP descendant, false otherwise
	 */
	boolean isDEPDescendant() {
		return this.isDEPDescendant;
	}
	
	/**
	 * Does this node have a spare below
	 * @return true if has spare below, false otherwise
	 */
	boolean hasSpareBelow() {
		return this.hasSpareBelow;
	}
	
	/**
	 * Get the children of a node
	 * @return list of children
	 */
	public List<FaultTreeNodePlus> getChildren() {
		return this.children;
	}
	
	/* ***********************
	 * OTHER METHODS
	 *********************** */
	
	/**
	 * Add a child to this node
	 * @param child the child to be added
	 */
	void addChild(FaultTreeNodePlus child) {
		// Since the child lists are created with a FIFO (Stack) processing, we are inverting the order of the children
		// This inversion needs to be remedied by adding the child at the beginning of the list
		this.children.add(0, child);
	}
	
	/**
	 * Set node has is a dep descendant as true
	 */
	void setDEPDescendant() {
		this.isDEPDescendant = true;
	}
	
	/**
	 * Set that there is a priority gate above
	 */
	void setHasPriorityAbove() {
		this.hasPriorityAbove = true;
	}
	
	/**
	 * Set that there is a spare gate below
	 */
	void setHasSpareBelow() {
		this.hasSpareBelow = true;
	}
	
	/**
	 * Set that there is a spare gate above
	 */
	void setHasSpareAbove() {
		this.hasSpareAbove = true;
	}
	
	/**
	 * Trim node from fault tree
	 */
	void harvestFromFaultTree() {
		this.harvested = true;
	}
	
	/**
	 * Add a path to this node
	 * @param node the node we are visiting from
	 */
	void addVisitedFrom(FaultTreeNodePlus node) {
		this.visitedFrom.add(node);
	}
	
	/**
	 * Determine if we have visited this node before from the same path.
	 * @param node the node we are visiting
	 * @return true if we have, false if we are visiting from a new path.
	 */
	boolean visitedBeforeFromNode(FaultTreeNodePlus node) {
		return this.visitedFrom.contains(node);
	}
	
	/**
	 * Get whether this node is within visiting dates of a root node
	 * @param root the root of suspected module
	 * @return false if node is out of bounds, true otherwise
	 */
	boolean isWithinBoundsOf(FaultTreeNodePlus root) {
		if ((this.firstVisit < root.getFirstVisit()) || (this.lastVisit > root.getLastVisit())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Override of toString()
	 * @return the string
	 */
	public String toString() {
		return "Name: " + this.node.getName();
	}
}
