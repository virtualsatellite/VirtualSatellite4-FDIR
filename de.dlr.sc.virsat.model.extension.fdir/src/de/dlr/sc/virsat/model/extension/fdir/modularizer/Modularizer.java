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

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;



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
	private FaultTreeHolder ftHolder;
	
	private int maxDepth = 0;
	private boolean beOptimizationOn = true;
	
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
		this.modules = new HashSet<Module>();
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
	
	/**
	 * Allows the user to choose if they would like to start searching for modules at the lowest level (basic events)
	 * or past basic events.
	 * @param beOptimizationOn true for optimization turned on, false for optimization turned off.
	 */
	public void setBEOptimization(boolean beOptimizationOn) {
		this.beOptimizationOn = beOptimizationOn;
	}
	
	
	/* ***********************************************************************
	 * *********** PRIVATE METHODS ********************************************
	 * **********************************************************************/
	
	/**
	 * Number the fault tree and save a copy of the tree as our internal TreeSet.
	 */
	void countTree() {
		table.clear();
		nodePlusTree.clear();
		
		if (this.faultTree == null) {
			this.maxDepth = -1;
			return;
		}
		
		this.ftHolder = new FaultTreeHolder(faultTree.getRoot());
		
		FaultTreeNodePlus root = new FaultTreeNodePlus(this.faultTree.getRoot(), 0);
		
		/* dfs traverse the tree, numbering off nodes */
		Stack<FaultTreeNodePlus> dfsStack = new Stack<FaultTreeNodePlus>();
		dfsStack.push(root);
		
		int count = 0;
		
		while (!dfsStack.isEmpty()) {
			FaultTreeNodePlus curr = dfsStack.peek();
			
			curr.visit(++count);
			
			this.addNodeToInternalTree(curr);

			List<FaultTreeNodePlus> children = curr.getChildren().size() == 0 ? this.getChildrenInReverseOrder(curr) : curr.getChildren();
			
			int numChildrenAddedToStack = 0;
			
			for (FaultTreeNodePlus child : children) {
				if (!child.isHarvested() && !child.visitedBeforeFromNode(curr)) {
					curr.addChild(child);
					child.addVisitedFrom(curr);
					dfsStack.push(child);
					numChildrenAddedToStack++;
					
					if (curr.isPriority() || curr.hasPriorityAbove()) {
						child.setHasPriorityAbove();
					}
					
					if (curr.isNondeterministic() || curr.hasSpareAbove()) {
						child.setHasSpareAbove();
					}
				}
			}
			
			if (numChildrenAddedToStack == 0) {
				FaultTreeNodePlus nodePopped = dfsStack.pop();
				
				if (nodePopped.hasSpareBelow() || nodePopped.isNondeterministic()) {
					nodePopped.getSetFrom().stream().forEach(n -> n.setHasSpareBelow());
				}
			}
		}
	}
	
	/**
	 * Begin the modularization process.
	 */
	private void modularize() {
		this.countTree();
		/*	DEFAULT optimization
		 *  start looking for modules at maxDepth - 2 because deepest 2 levels are just faults and their basic events */
		int startingDepth = this.beOptimizationOn ? this.maxDepth - 2 : this.maxDepth - 1;
		
		for (int i = startingDepth; i >= 0; i--) {
			int currDepth = i;
			
			for (FaultTreeNodePlus currNode : this.nodePlusTree) {				
				/* detected a potential module */
				boolean moduleDetected = (currNode.getDepth() == currDepth) && !currNode.isHarvested();
				moduleDetected = moduleDetected 
						&& (this.beOptimizationOn ? (currNode.getFirstVisit() + 2 < currNode.getLastVisit())
												: (currNode.getFirstVisit() + 1 < currNode.getLastVisit()));
				if (moduleDetected) {
					Module module = this.harvestModule(currNode.getFaultTreeNode());
					
					if (module != null) {
						this.modules.add(module);
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
		List<FaultTreeNode> children = new ArrayList<>(ftHolder.getMapNodeToSubNodes().getOrDefault(node.getFaultTreeNode(), Collections.emptyList()));
		children.addAll(ftHolder.getMapFaultToBasicEvents().getOrDefault(node.getFaultTreeNode(), Collections.emptyList()));
		children.addAll(ftHolder.getMapNodeToDEPTriggers().getOrDefault(node.getFaultTreeNode(), Collections.emptyList()));
		
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
	 * Collect a suspected module
	 * @param root the root node of suspected module
	 * @return module containing modules nodes. Null if not a module.
	 */
	private Module collectModule(FaultTreeNodePlus root) {
		Module module = new Module();

		if (root == null
				|| (root.isNondeterministic() && root.hasPriorityAbove())
				|| root.hasSpareAbove()
				|| (root.hasSpareBelow() && root.hasPriorityAbove())) {
			return null;
		}
		
		Stack<FaultTreeNodePlus> stack = new Stack<FaultTreeNodePlus>();
		stack.push(root);
		
		while (!stack.isEmpty()) {
			FaultTreeNodePlus curr = stack.pop();
			if (module.addNode(curr)) {
				List<FaultTreeNodePlus> children = curr.getChildren();
				
				for (FaultTreeNodePlus child : children) {
					if (!child.isHarvested()) {
						if (!child.isWithinBoundsOf(root)) {
							return null;
						}
						stack.push(child);
					}
				}
			}
		}
		return module;
	}

	/**
	 * Collect the module. Returns null if not a module.
	 * @param root the root of the module
	 * @return the module
	 */
	Module harvestModule(FaultTreeNode root) {
		FaultTreeNodePlus rootPlus = this.table.get(root);
		
		Module mod = collectModule(rootPlus);
		if (mod != null) {
			mod.harvestFromFaultTree();
			mod.setTable(table);
		}
		return mod;
	}
}
