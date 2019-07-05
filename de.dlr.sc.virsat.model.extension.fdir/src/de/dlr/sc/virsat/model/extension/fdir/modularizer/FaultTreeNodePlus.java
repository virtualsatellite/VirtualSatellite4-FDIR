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


import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

import java.util.Set;
import java.util.HashSet;

/**
 * Links each FaultTreeNode in a FaultTree with the first and last date visited.
 * For use with Modularizer
 * @author jord_ad
 *
 */

public class FaultTreeNodePlus {

	private FaultTreeNode node;
	private Set<FaultTreeNodePlus> visitedFrom;
	private int firstVisit = 0;
	private int lastVisit = 0;
	private int depth;
	
	private boolean harvested = false;
	private boolean hasPriorityAbove = false;
	private boolean isDEPDescendant = false;

	/* CONSTRUCTORS */
	
	/**
	 * Constructor
	 * @param reference the FaultTreeNode reference
	 * @param visitedFrom the set of nodes we have visited from
	 * @param firstVisit date of first visit
	 * @param lastVisit date of last visit
	 * @param depth the depth of the node
	 * @param harvested whether or not the fault tree node has been harvested
	 */
	public FaultTreeNodePlus(FaultTreeNode reference, Set<FaultTreeNodePlus> visitedFrom, int firstVisit, int lastVisit, int depth, boolean harvested) {
		this.node = reference;
		this.firstVisit = firstVisit;
		this.lastVisit = lastVisit;
		
		if (visitedFrom == null) {
			this.visitedFrom = new HashSet<FaultTreeNodePlus>();
		} else {
			this.visitedFrom = visitedFrom;
			this.determineIfHasPriorityAbove();
			this.determineIfIsDEPDescendant();
		}
		
		this.depth = depth;
		this.harvested = harvested;
	}
	
	/**
	 * Constructor
	 * @param reference the FaultTreeNode reference
	 * @param depth the depth
	 */
	public FaultTreeNodePlus(FaultTreeNode reference, int depth) {
		this(reference, null, 0, 0, depth, false);
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
	int getFirstVisit() {
		return this.firstVisit;
	}
	
	/**
	 * Get the last visit date
	 * @return last visit date
	 */
	int getLastVisit() {
		return this.lastVisit;
	}
	
	/**
	 * Get the reference FaultTreeNode
	 * @return the reference FaultTreeNode
	 */
	FaultTreeNode getFaultTreeNode() {
		return this.node;
	}
	
	/**
	 * Get set of nodes we have visited this node from
	 * @return the set of neighbouring nodes
	 */
	Set<FaultTreeNodePlus> getSetFrom() {
		return this.visitedFrom;
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
	 * Determine if this node has a priority gate above
	 * @return true is has priority above, false otherwise
	 */
	boolean determineIfHasPriorityAbove() {
		if (!this.hasPriorityAbove) {
			for (FaultTreeNodePlus node : this.visitedFrom) {
				if (node.isPriority() || node.determineIfHasPriorityAbove()) {
					this.hasPriorityAbove = true;
					break;
				}
			}
		}
		return this.hasPriorityAbove;
	}
	
	/**
	 * Determine if this node is a DEP descendant
	 * @return true if DEP descendant, false otherwise
	 */
	boolean determineIfIsDEPDescendant() {
		if (!this.isDEPDescendant) {
			for (FaultTreeNodePlus node : this.visitedFrom) {
				if (node.isDependency() || node.determineIfIsDEPDescendant()) {
					this.isDEPDescendant = true;
					break;
				}
			}
		}
		return this.isDEPDescendant;
	}

	
	/* ***********************
	 * OTHER METHODS
	 *********************** */
	
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
		this.determineIfHasPriorityAbove();
		this.determineIfIsDEPDescendant();
	}
	
	/**
	 * Determine if we have visited this node before from the same path.
	 * @param node the node we are visiting
	 * @return true if we have, false if we are visiting from a new path.
	 */
	boolean visitedBeforeFromNode(FaultTreeNodePlus node) {
		if (this.visitedFrom.isEmpty()) {
			return false;
		}
		
		if (this.visitedFrom.contains(node)) {
			return true;
		}
		return false;
	}

	
	/**
	 * Returns whether a FaultTreeNodePlus is dynamic or not
	 * @return true if dynamic, false otherwise
	 */
	public boolean isDynamic() {
		switch (this.getFaultTreeNode().getFaultTreeNodeType()) {
			case FDEP:
				return true;
			case SPARE:
				return true;
			case PAND:
				return true;
			case POR:
				return true;
			default: return false;
		}
	}
	
	
	/**
	 * Returns whether a FaultTreeNodePlus is a priority gate or not
	 * @return true if priority gate, false otherwise
	 */
	public boolean isPriority() {
		switch (this.getFaultTreeNode().getFaultTreeNodeType()) {
			case PAND:
				return true;
			case POR:
				return true;
			default: return false;
		}
	}
	
	/**
	 * Returns whether a FaultTreeNodePlus is a dependency gate or not
	 * @return true if dependency gate, false otherwise
	 */
	public boolean isDependency() {
		switch (this.getFaultTreeNode().getFaultTreeNodeType()) {
			case FDEP:
				return true;
			default: return false;
		}
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
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + this.node.getName());
		return sb.toString();
	}
}
