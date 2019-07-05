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

import java.util.ArrayList;
import java.util.List;
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
	
	/**
	 * Default constructor.
	 */
	public Module() {
		this.moduleNodes = new ArrayList<FaultTreeNodePlus>();
		this.moduleType = ModuleType.STATIC;
	}
	
	
	/**
	 * Add a node to the module
	 * @param node the node
	 */
	void addNode(FaultTreeNodePlus node) {
		if (this.moduleNodes.isEmpty()) {
			this.moduleRoot = node;
		}
		
		this.moduleNodes.add(node);
		if (node.isDynamic()) {
			this.moduleType = ModuleType.DYNAMIC;
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
	 * Get nodes of a module. For testing only.
	 * @return list of FaultTreeNodePlus
	 */
	List<FaultTreeNodePlus> getNodesPlus() {
		return this.moduleNodes;
	}
	
	/**
	 * Get the root node of the module
	 * @return the root node
	 */
	public FaultTreeNode getRootNode() {
		return this.moduleRoot.getFaultTreeNode();
	}
	
	/**
	 * Returns true if module is dynamic, false otherwise
	 * @return is dynamic
	 */
	public boolean isDynamic() {
		return this.moduleType == ModuleType.DYNAMIC;
	}
	
	/**
	 * Trim the module from the tree.
	 */
	public void harvestFromFaultTree() {
		for (FaultTreeNodePlus node : this.moduleNodes) {
			node.harvestFromFaultTree();
		}
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
