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
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Contains information for modules of the fault tree
 * @author jord_ad
 *
 */
public class Module {

	private FaultTreeNodePlus moduleRoot;
	private ModuleType moduleType;
	private List<FaultTreeNodePlus> moduleNodes;
	private FaultTreeNode moduleRootCopy;
	
	/**
	 * Default constructor.
	 */
	public Module() {
		this.moduleNodes = new ArrayList<FaultTreeNodePlus>();
		this.moduleType = ModuleType.DETERMINISTIC;
	}
	
	/**
	 * Constructor which takes in the known module nodes
	 * @param moduleNodes the module nodes
	 */
	public Module(List<FaultTreeNode> moduleNodes) {
		for (FaultTreeNode ftn : moduleNodes) {
			this.moduleNodes.add(new FaultTreeNodePlus(ftn, null, 0, 0, 0, false));
		}
	}
	
	
	/**
	 * Add a node to the module
	 * @param node the node
	 */
	void addNode(FaultTreeNodePlus node) {
		if (this.moduleNodes.isEmpty()) {
			this.moduleRoot = node;
		}
		
		if (!this.moduleNodes.contains(node)) {
			this.moduleNodes.add(node);
		}
		
		if (node.isNondeterministic()) {
			this.moduleType = ModuleType.NONDETERMINISTIC;
		}
	}
	
	/**
	 * Get nodes of a module.
	 * @return list of FaultTreeNodes
	 */
	public List<FaultTreeNode> getNodes() {
		return moduleNodes.stream().map(FaultTreeNodePlus::getFaultTreeNode).collect(Collectors.toList());
	}
	
	/**
	 * Get the root node of the module
	 * @return the root node
	 */
	public FaultTreeNode getRootNode() {
		return this.moduleRoot.getFaultTreeNode();
	}
	
	/**
	 * Get the root node of the copied module
	 * @return the root node of the copied module
	 */
	public FaultTreeNode getRootNodeCopy() {
		return this.moduleRootCopy;
	}
	
	
	/**
	 * Returns true if module is nondeterministic, false otherwise
	 * @return is nondeterministic
	 */
	public boolean isNondeterministic() {
		return this.moduleType == ModuleType.NONDETERMINISTIC;
	}
	
	/**
	 * Trim the module from the tree.
	 */
	public void harvestFromFaultTree() {
		this.moduleNodes.stream().forEach(node -> node.harvestFromFaultTree());
	}
	
	/**
	 * Create the fault tree copy that is required for conversion to markov automata, with only the edges in the module
	 */
	public void constructFaultTreeCopy() {
		FaultTreeHelper fthelp = new FaultTreeHelper(this.moduleNodes.get(0).getFaultTreeNode().getConcept());
		Stack<FaultTreeNode> dfsStack = new Stack<FaultTreeNode>();
		Map<FaultTreeNode, FaultTreeNode> mapOriginalToCopy = new HashMap<FaultTreeNode, FaultTreeNode>();
		
		FaultTreeNode originalRoot = this.moduleNodes.get(0).getFaultTreeNode().getFault().getFaultTree().getRoot();
		FaultTreeNode rootCopy = fthelp.copyFaultTreeNode(originalRoot, null);
		rootCopy.setName(originalRoot.getName());

		List<FaultTreeNode> sparesInOriginalFaultTree = fthelp.getAllSpareNodes(originalRoot.getFault());
		mapOriginalToCopy.put(originalRoot, rootCopy);
		dfsStack.push(originalRoot);
		
		while (!dfsStack.isEmpty()) {
			FaultTreeNode curr = dfsStack.pop();
			FaultTreeNode currCopy = mapOriginalToCopy.get(curr);
			
			if (curr.equals(moduleRoot.getFaultTreeNode())) {
				this.moduleRootCopy = currCopy;
			}
			
			List<FaultTreeNode> children = fthelp.getAllChildren(curr, curr.getFault().getFaultTree());
			for (FaultTreeNode child : children) {
				FaultTreeNode childCopy;
				if (mapOriginalToCopy.get(child) == null) {
					childCopy = fthelp.copyFaultTreeNode(child, currCopy.getFault());
					mapOriginalToCopy.put(child, childCopy);
				} else {
					childCopy = mapOriginalToCopy.get(child);
				}
				
				boolean moduleContainsCurrAndChild = this.containsFaultTreeNode(curr) && this.containsFaultTreeNode(child);
				if (moduleContainsCurrAndChild
						&& child.getFaultTreeNodeType() != FaultTreeNodeType.BASIC_EVENT) {
					if (sparesInOriginalFaultTree.contains(child)) {
						fthelp.connectSpare(currCopy.getFault(), childCopy, currCopy);
					} else {
						fthelp.createFaultTreeEdge(currCopy.getFault(), childCopy, currCopy);
					}
				}
				dfsStack.push(child);
			}
		}
	}
	
	/**
	 * If the module nodes contain a specific FaultTreeNode
	 * @param ftnode the FaultTreeNode
	 * @return true if this node is part of the module, false otherwise
	 */
	private boolean containsFaultTreeNode(FaultTreeNode ftnode) {
		for (FaultTreeNodePlus ftPlus : moduleNodes) {
			if (ftPlus.getFaultTreeNode().equals(ftnode)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Override of toString()
	 * @return the string
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (FaultTreeNodePlus node : this.moduleNodes) {
			sb.append(node.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append("}");
		
		return sb.toString();
	}
}
