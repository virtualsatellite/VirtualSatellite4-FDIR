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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;



/**
 * Modularizer class which implements IModularizer
 * @author jord_ad
 */

public class Modularizer implements IModularizer {

	private static final Comparator<FaultTreeNodePlus> FTN_PLUS_COMPARATOR = 
			(ftnPlus1, ftnPlus2) -> ftnPlus1.getFirstVisit() - ftnPlus2.getFirstVisit();
	
	private TreeSet<FaultTreeNodePlus> nodePlusTree;
	private Map<FaultTreeNode, FaultTreeNodePlus> table;
	private Set<Module> modules;
	
	/* a reference to the entire fault tree */
	private FaultTreeHolder ftHolder;
	
	private int maxDepth = 0;
	
	/* ***********************************************************************
	 * *********** PUBLIC METHODS ********************************************
	 * **********************************************************************/
	
	@Override
	public Set<Module> getModules(Fault root) {
		setFaultTree(root);
		this.modularize();
		return modules;
	}
	
	/**
	 * Gets the underlying fault tree holder data structure
 	 * @return the fault tree holder
	 */
	public FaultTreeHolder getFtHolder() {
		return ftHolder;
	}
	
	/* ***********************************************************************
	 * *********** PRIVATE METHODS ********************************************
	 * **********************************************************************/
	
	/**
	 * Number the fault tree and save a copy of the tree as our internal TreeSet.
	 */
	void countTree() {
		this.nodePlusTree = new TreeSet<FaultTreeNodePlus>(FTN_PLUS_COMPARATOR);
		this.table = new HashMap<FaultTreeNode, FaultTreeNodePlus>();
		
		/* dfs traverse the tree, numbering off nodes */
		Stack<FaultTreeNodePlus> dfsStack = new Stack<FaultTreeNodePlus>();
		FaultTreeNodePlus rootPlus = getOrCreateFaultTreeNodePlus(ftHolder.getRoot(), 0);
		dfsStack.push(rootPlus);
		
		int count = 0;
		
		while (!dfsStack.isEmpty()) {
			FaultTreeNodePlus curr = dfsStack.peek();
			curr.visit(++count);
			
			this.addNodeToInternalTree(curr);
			
			List<FaultTreeNodePlus> children = curr.getChildren().isEmpty() ? this.getChildrenInReverseOrder(curr) : curr.getChildren();
			
			boolean addedToStack = false;
			for (FaultTreeNodePlus child : children) {
				if (!child.isHarvested() && !child.visitedBeforeFromNode(curr)) {
					curr.addChild(child);
					child.addVisitedFrom(curr);
					dfsStack.push(child);
					addedToStack = true;
					
					FaultTreeNodeType nodeType = curr.getFaultTreeNode().getFaultTreeNodeType();
					if (nodeType.isOrderDependent() || curr.hasPriorityAbove()) {
						child.setHasPriorityAbove();
					}
					
					if (nodeType.isNondeterministic() || curr.hasSpareAbove()) {
						child.setHasSpareAbove();
					}
				}
			}
			
			if (!addedToStack) {
				FaultTreeNodePlus nodePopped = dfsStack.pop();
				
				if (nodePopped.hasSpareBelow() || nodePopped.getFaultTreeNode().getFaultTreeNodeType().isNondeterministic()) {
					nodePopped.getSetFrom().stream().forEach(FaultTreeNodePlus::setHasSpareBelow);
				}
			}
		}
	}
	
	/**
	 * Begin the modularization process.
	 */
	private void modularize() {
		this.modules = new HashSet<Module>();
		this.countTree();
		
		int startingDepth = this.maxDepth - 1;
		
		Set<FaultTreeNodePlus> nodes = new HashSet<>(nodePlusTree);
		for (int i = startingDepth; i >= 0; i--) {
			for (FaultTreeNodePlus currNode : nodes) {	
				boolean potentialModuleDetected = isPotentialModule(i, currNode);
				if (potentialModuleDetected) {
					Module module = this.harvestModule(currNode.getFaultTreeNode(), modules);
					if (module != null) {
						this.modules.add(module);
					}
				}
			}
		}
	}

	/**
	 * Checks if a given node defines a module.
	 * @param currentDepth the current depth
	 * @param currNode the current node
	 * @return true iff the current node defines a module root
	 */
	private boolean isPotentialModule(int currentDepth, FaultTreeNodePlus currNode) {
		if (currNode.getDepth() != currentDepth) {
			return false;
		}
		
		if (currNode.isHarvested()) {
			return false;
		}
		
		if (currNode.getFirstVisit() + 1 >= currNode.getLastVisit()) {
			return false;
		}
		
		if (currNode.hasPriorityAbove()) {
			return false;
		}
		
		if (currNode.hasSpareAbove()) {
			return false;
		}
		
		return true;
	}

	/**
	 * Checks if there exists a guaranteed monitor that can immediately
	 * observe the given node.
	 * @param node the node to check
	 * @return true iff there exists an immediate monitor node
	 */
	private boolean existsImmediateMonitor(FaultTreeNode node) {
		if (node instanceof BasicEvent) {
			node = ftHolder.getBasicEventHolder((BasicEvent) node).getFault();
		}
		
		List<FaultTreeNode> monitors = ftHolder.getNodes(node, EdgeType.MONITOR);
		for (FaultTreeNode monitor : monitors) {
			if (((MONITOR) monitor).getObservationRate() == 0 
					&& ftHolder.getNodes(monitor, EdgeType.CHILD).isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/* *******************************************
	 * SMALLER HELPER METHODS
	 * *******************************************/
	
	/**
	 * Add a fault tree node to the internal FaultTreeNodePlus tree
	 * @param node the node to be added to the internal tree
	 */
	private void addNodeToInternalTree(FaultTreeNodePlus node) {
		if (this.nodePlusTree.add(node)) {
			this.table.put(node.getFaultTreeNode(), node);
		} 
	}
	
	/** Set the fault tree of the modularizer. For testing only.
	 * @param root the root of the tree
	 */
	void setFaultTree(Fault root) {
		ftHolder = new FaultTreeHolder(root);
	}
	
	/**
	 * Get ALL children of a node (events, spares)
	 * @param node the node you want to find the children of
	 * @return the list of children
	 */
	private List<FaultTreeNodePlus> getChildren(FaultTreeNodePlus node) {
		List<FaultTreeNode> children = ftHolder.getMapNodeToSubNodes().getOrDefault(node.getFaultTreeNode(), Collections.emptyList());
		
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
	 * Collect the module. Returns null if not a module.
	 * @param root the root of the module
	 * @param modules the current modules
	 * @return the module
	 */
	Module harvestModule(FaultTreeNode root, Set<Module> modules) {
		FaultTreeNodePlus rootPlus = this.table.get(root);
		Module module = new Module(ftHolder, rootPlus);
		
		Stack<FaultTreeNodePlus> stack = new Stack<FaultTreeNodePlus>();
		stack.push(rootPlus);
		while (!stack.isEmpty()) {
			FaultTreeNodePlus curr = stack.pop();
			if (!module.addNode(curr)) {
				continue;
			}
			
			List<FaultTreeNodePlus> children = curr.getChildren();
			for (FaultTreeNodePlus child : children) {
				if (!child.isHarvested()) {
					if (!child.isWithinBoundsOf(rootPlus)) {
						return null;
					}
					stack.push(child);
				} else if (ftHolder.isPartialObservable()) {
					Module subModule = Module.getModule(modules, child.getFaultTreeNode());
					if (subModule.isPartialObservable() && !existsImmediateMonitor(child.getFaultTreeNode())) {
						module.getModuleProperties().add(ModuleProperty.PARTIALOBSERVABLE);
					}
				}
			}
			
			if (children.isEmpty()) {
				// This is a leaf node within the module
				if (ftHolder.isPartialObservable() && !existsImmediateMonitor(curr.getFaultTreeNode())) {
					module.getModuleProperties().add(ModuleProperty.PARTIALOBSERVABLE);
				}
			}
			
			List<FaultTreeNode> monitors = ftHolder.getNodes(curr.getFaultTreeNode(), EdgeType.MONITOR);
			for (FaultTreeNode monitor : monitors) {
				Set<FaultTreeNode> monitorTree = getMonitorTree(monitor);
				
				for (FaultTreeNode monitorTreeNode : monitorTree) {
					FaultTreeNodePlus ftnPlus = getOrCreateFaultTreeNodePlus(monitorTreeNode, 0);
					module.addNode(ftnPlus);
					
					if (!this.table.containsKey(monitorTreeNode)) {
						this.table.put(monitorTreeNode, ftnPlus);
						for (FaultTreeNodePlus child : getChildrenInReverseOrder(ftnPlus)) {
							ftnPlus.addChild(child);
						}
					}
				}
			}
		}
		
		if (ftHolder.isPartialObservable() && module.isPartialObservable()) {
			if (!existsImmediateMonitor(root) && !ftHolder.getNodes(root, EdgeType.PARENT).isEmpty()) {
				return null;
			}
		}
		
		module.harvestFromFaultTree();
		module.setTable(table);
		
		return module;
	}

	/**
	 * Gets all nodes in a tree starting from the root of the given monitor node
	 * @param monitor the given monitor node
	 * @return all nodes in the tree starting from the roots of the given monitor node
	 */
	private Set<FaultTreeNode> getMonitorTree(FaultTreeNode monitor) {
		Set<FaultTreeNode> monitorTree = new HashSet<>();
		Queue<FaultTreeNode> toProcess = new LinkedList<>(ftHolder.getRoots(monitor));
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			if (monitorTree.add(node)) {
				List<FaultTreeNode> subNodes = ftHolder.getMapNodeToSubNodes().get(node);
				toProcess.addAll(subNodes);
			}
		}
		
		return monitorTree;
	}
}
