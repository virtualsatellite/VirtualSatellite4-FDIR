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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;



/**
 * Modularizer class which implements IModularizer
 * @author jord_ad
 */

public class Modularizer implements IModularizer {

	private TreeSet<FaultTreeNodePlus> nodePlusTree;
	private Map<FaultTreeNode, FaultTreeNodePlus> table;
	private Set<Module> modules;
	
	/* a reference to the entire fault tree */
	private FaultTree faultTree;
	
	private int maxDepth = 0;
	
	// CONSTRUCTORS
	
	/**
	 * Default constructor
	 */
	public Modularizer() {
		/* create comparator for sorting FaultTreeNodePlus nodes */
		Comparator<FaultTreeNodePlus> comparator = new Comparator<FaultTreeNodePlus>() {
			
			@Override
			public int compare(final FaultTreeNodePlus o1, final FaultTreeNodePlus o2) {
				return o1.getFirstVisit() - o2.getFirstVisit();
			}
		};
		
		this.nodePlusTree = new TreeSet<FaultTreeNodePlus>(comparator);
		this.table = new HashMap<FaultTreeNode, FaultTreeNodePlus>();
		this.modules = new HashSet<Module>();
	}
	
	
	
	/* ***********************************************************************
	 * *********** PUBLIC METHODS ********************************************
	 * **********************************************************************/
	
	/**
	 * A method which modularizes a Fault Tree and returns the modules in a set.
	 * @param ft the root node to the Fault Tree which is to be modularized
	 * @return a set of modules
	 */
	public Set<Module> getModules(FaultTree ft) {
		this.faultTree = ft;
		this.modularize();
		return this.modules;
	}
	
	
	/**
	 * Get the depth of the tree, where root has depth 0
	 * @param ft the fault tree
	 * @return the depth
	 */
	public int getTreeDepth(FaultTree ft) {
		this.faultTree = ft;
		this.countTree();
		return this.maxDepth;
	}
	
	
	/* ***********************************************************************
	 * *********** PRIVATE METHODS ********************************************
	 * **********************************************************************/
	
	/**
	 * Number the fault tree and save a copy of the tree as our internal TreeSet.
	 */
	void countTree() {
		
		if (this.faultTree == null) {
			this.maxDepth = -1;
			return;
		}
		
		FaultTreeNodePlus root = new FaultTreeNodePlus(this.faultTree.getRoot(), 0);
		
		/* dfs traverse the tree, numbering off nodes */
		Stack<FaultTreeNodePlus> dfsStack = new Stack<FaultTreeNodePlus>();
		dfsStack.push(root);
		
		int count = 0;
		
		while (!dfsStack.isEmpty()) {
			FaultTreeNodePlus curr = dfsStack.peek();
		
			curr.visit(++count);
			
			this.addNodeToInternalTree(curr);

			List<FaultTreeNodePlus> children = this.getChildrenInReverseOrder(curr);
			int numChildrenAddedToStack = 0;
			
			for (FaultTreeNodePlus child : children) {
				if (!child.visitedBeforeFromNode(curr) && !child.isHarvested()) {
					
					child.addVisitedFrom(curr);
					dfsStack.push(child);
					numChildrenAddedToStack++;
				}
			}
			
			if (numChildrenAddedToStack == 0) {
				dfsStack.pop();
			}
		}
	}
	
	/**
	 * Begin the modularization process.
	 */
	private void modularize() {
		this.countTree();
		/* start looking for modules at maxDepth - 2 because deepest 2 levels are just faults and their basic events */
		for (int i = this.maxDepth - 2; i >= 0; i--) {
			int currDepth = i;
			
			for (FaultTreeNodePlus currNode : this.nodePlusTree) {				
				/* detected a potential module */
				if ((currNode.getFirstVisit() + 2 < currNode.getLastVisit())
						&& (currNode.getDepth() == currDepth)
						&& !currNode.isHarvested()) {
					Module module = this.harvestModule(currNode);
					
					if (module != null) {
						this.modules.add(module);
						module.harvestFromFaultTree();
					}
				}
			} 
		} 
	}
	
	
	/* *******************************************
	 * SMALLER HELPER METHODS
	 * *******************************************/
	
	/**
	 * Add a fault tree node to the internal FaultTreeNodePlus tree
	 * @param node the node to be added to the internal tree
	 */
	private void addNodeToInternalTree(FaultTreeNodePlus node) {
		if (!this.table.containsValue(node)) {
			this.nodePlusTree.add(node);
			this.table.put(node.getFaultTreeNode(), node);
		} 
	}
	
	/** Set the fault tree of the modularizer. For testing only.
	 * @param ft the fault tree
	 */
	void setFaultTree(FaultTree ft) {
		this.faultTree = ft;
	}
	
	/**
	 * Get ALL children of a node (events, spares)
	 * @param node the node you want to find the children of
	 * @return the list of children
	 */
	private List<FaultTreeNodePlus> getChildren(FaultTreeNodePlus node) {
		FaultTreeHelper fthelper = new FaultTreeHelper(node.getFaultTreeNode().getConcept());
		
		/* get children, spares, and dependencies and compile one big list */
		List<FaultTreeNode> children = fthelper.getChildren(node.getFaultTreeNode(), getTreeAsSet(this.faultTree));
		children.addAll(fthelper.getSpares(node.getFaultTreeNode(), getTreeAsSet(this.faultTree)));
		children.addAll(fthelper.getDeps(node.getFaultTreeNode(), getTreeAsSet(this.faultTree)));
		
		if (node.getFaultTreeNode() instanceof Fault) {
			children.addAll(((Fault) node.getFaultTreeNode()).getBasicEvents()); 
		}
		
		return children.stream()
					.map(ftNode -> getOrCreateFaultTreeNodePlus(ftNode, node.getDepth() + 1))
					.collect(Collectors.toList());
	}
	
	/**
	 * Checks if an internal node exists and if not creates a new one
	 * @param ftNode the fault tree node
	 * @param depth the depth of the internal node
	 * @return the internal node
	 */
	private FaultTreeNodePlus getOrCreateFaultTreeNodePlus(FaultTreeNode ftNode, int depth) {
		FaultTreeNodePlus nodeInInternalTree = this.table.get(ftNode);
		
		if (nodeInInternalTree == null) {
			nodeInInternalTree = new FaultTreeNodePlus(ftNode, depth);
			this.maxDepth = Math.max(depth, this.maxDepth);
		}
		
		return nodeInInternalTree;
	}
	

	/**
	 * Get ALL children of a node (events, spares) in reverse order
	 * @param node the node you want to find the children of
	 * @return the list of children in reverse order
	 */
	private List<FaultTreeNodePlus> getChildrenInReverseOrder(FaultTreeNodePlus node) {
		List<FaultTreeNodePlus> children = this.getChildren(node);
		Collections.reverse(children);
		return children;
	}


	/**
	 * Small getter for testing purposes only.
	 * @return the table
	 */
	Map<FaultTreeNode, FaultTreeNodePlus> getTable() {
		return this.table;
	}
	
	/**
	 * Get a fault tree as a Set<FaultTree>. Needed for ftHelper.getChildren and .getSpares
	 * @param ft the fault tree
	 * @return a set containing the fault tree
	 */
	private static Set<FaultTree> getTreeAsSet(FaultTree ft) {
		return Collections.singleton(ft);
	}
	
	/** 
	 * Determine if a node is the root of a module
	 * @param root the root node of suspected module
	 * @return false if not a module, true otherwise
	 */
	private boolean isModule(FaultTreeNodePlus root) {
		if (root.determineIfHasPriorityAbove()) {
			return false;
		} else if (root.determineIfIsDEPDescendant()) {
			return false;
		} else if (root.isDependency() || root.determineIfIsDEPDescendant()) {
			return false;
		}
		
		List<FaultTreeNodePlus> children = this.getChildren(root);
		
		for (FaultTreeNodePlus child : children) {
			if (!child.isHarvested()) {
				if (!child.isWithinBoundsOf(root)) {
					return false;
				} else if (!this.allChildrenWithinBounds(child, root)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Determine if all child nodes are within bounds of the root
	 * @param child a child node in the original suspected module
	 * @param root the original module root
	 * @return false if not within bounds, true otherwise
	 */
	private boolean allChildrenWithinBounds(FaultTreeNodePlus child, FaultTreeNodePlus root) {
		List<FaultTreeNodePlus> children = this.getChildren(child);
		
		for (FaultTreeNodePlus c : children) {
			if (!c.isHarvested()) {
				if (!c.isWithinBoundsOf(root)) {
					return false;
				} else if (!this.allChildrenWithinBounds(c, root)) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Collect the module ONLY AFTER it has been determined that it is a module
	 * @param root the root of the module
	 * @return the module
	 */
	private Module collectModule(FaultTreeNodePlus root) {
		Module module = new Module();
		
		for (FaultTreeNodePlus curr : this.nodePlusTree) {
			if ((curr.getFirstVisit() >= root.getFirstVisit())
					&& (curr.getLastVisit() <= root.getLastVisit())
					&& !curr.isHarvested()) {
				module.addNode(curr);
			}
		}
		return module;
	}

	/**
	 * Collect the module. Automatically checks if the module is a module. Returns null if not a module.
	 * @param root the root of the module
	 * @return the module
	 */
	private Module harvestModule(FaultTreeNodePlus root) {
		if ((root != null) && isModule(root)) {
			Module mod = collectModule(root);
			mod.harvestFromFaultTree();
			return mod;
		}
		return null;
	}
	
	
	/**
	 * Collect the module. Automatically checks if the module is a module. Returns null if not a module.
	 * Uses FaultTreeNode (for testing only)
	 * @param root the root of the module
	 * @return the module
	 */
	Module harvestModule(FaultTreeNode root) {
		FaultTreeNodePlus rootPlus = this.table.get(root);
		return harvestModule(rootPlus);
	}
}
