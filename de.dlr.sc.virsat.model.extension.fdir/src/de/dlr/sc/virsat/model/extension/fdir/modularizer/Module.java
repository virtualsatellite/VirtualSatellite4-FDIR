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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

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
	private Map<FaultTreeNode, FaultTreeNode> mapOriginalToCopy;
	private Map<FaultTreeNode, FaultTreeNode> mapCopyToOriginal;
	private Map<FaultTreeNode, FaultTreeNodePlus> mapOriginalToNodePlus;
	
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
	 * Set the table used when creating this module by the modularizer
	 * @param mapOriginalToNodePlus the table
	 */
	void setTable(Map<FaultTreeNode, FaultTreeNodePlus> mapOriginalToNodePlus) {
		this.mapOriginalToNodePlus = mapOriginalToNodePlus;
	}
	
	
	/**
	 * Add a node to the module
	 * @param node the node
	 * @return true iff the node was not in the module nodes before
	 */
	boolean addNode(FaultTreeNodePlus node) {
		if (this.moduleNodes.contains(node)) {
			return false;
		}
		
		if (this.moduleNodes.isEmpty()) {
			this.moduleRoot = node;
		}
		
		this.moduleNodes.add(node);
		
		if (node.isNondeterministic()) {
			this.moduleType = ModuleType.NONDETERMINISTIC;
		}
		
		return true;
	}
	
	/**
	 * Get nodes of a module.
	 * @return list of FaultTreeNodes
	 */
	public List<FaultTreeNode> getNodes() {
		return moduleNodes.stream().map(FaultTreeNodePlus::getFaultTreeNode).collect(Collectors.toList());
	}
	
	/**
	 * Gets the nodes of the modules with the associated meta-information such as visit times
	 * @return the nodes of the modules with their meta-information
	 */
	public List<FaultTreeNodePlus> getModuleNodes() {
		return moduleNodes;
	}
	
	/**
	 * Get the root node of the module
	 * @return the root node
	 */
	public FaultTreeNode getRootNode() {
		return this.moduleRoot.getFaultTreeNode();
	}
	
	/**
	 * Gets the root node with its meta information
	 * @return the root node with its meta information
	 */
	public FaultTreeNodePlus getModuleRoot() {
		return moduleRoot;
	}
	
	/**
	 * Get the root node of the copied module
	 * @return the root node of the copied module
	 */
	public FaultTreeNode getRootNodeCopy() {
		return this.moduleRootCopy;
	}
	
	/**
	 * Get the map which maps original fault tree nodes to the copy fault tree nodes
	 * @return the map
	 */
	public Map<FaultTreeNode, FaultTreeNode> getMapOriginalToCopy() {
		return this.mapOriginalToCopy;
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
		FaultTreeHelper fthelp = new FaultTreeHelper(this.moduleRoot.getFaultTreeNode().getConcept());
		Stack<FaultTreeNode> dfsStack = new Stack<FaultTreeNode>();
		this.mapOriginalToCopy = new HashMap<FaultTreeNode, FaultTreeNode>();
		this.mapCopyToOriginal = new HashMap<FaultTreeNode, FaultTreeNode>();
		
		FaultTreeNode originalRoot = this.moduleRoot.getFaultTreeNode();
		Fault rootFault = new Fault(originalRoot.getConcept());
		FaultTreeNode rootCopy = fthelp.copyFaultTreeNode(originalRoot, rootFault.getFault());
		this.moduleRootCopy = rootFault;
		fthelp.connect(rootFault, rootCopy, rootFault);

		List<FaultTreeNode> sparesInOriginalFaultTree = fthelp.getAllSpareNodes(originalRoot.getFault());
		mapOriginalToCopy.put(originalRoot, rootCopy);
		mapCopyToOriginal.put(rootCopy, originalRoot);
		dfsStack.push(originalRoot);
		
		while (!dfsStack.isEmpty()) {
			FaultTreeNode curr = dfsStack.pop();
			FaultTreeNode currCopy = mapOriginalToCopy.get(curr);
			
			List<FaultTreeNodePlus> children = this.mapOriginalToNodePlus.get(curr).getChildren();
			for (FaultTreeNodePlus childPlus : children) {
				FaultTreeNode child = childPlus.getFaultTreeNode();
				FaultTreeNode childCopy;
				if (mapOriginalToCopy.get(child) == null) {
					childCopy = fthelp.copyFaultTreeNode(child, currCopy.getFault());
					mapOriginalToCopy.put(child, childCopy);
					mapCopyToOriginal.put(childCopy, child);
					
					dfsStack.push(child);
					
					if (childCopy instanceof Fault) {
						for (int i = 0; i < childCopy.getFault().getBasicEvents().size(); ++i) {
							BasicEvent oldBasicEvent = child.getFault().getBasicEvents().get(i);
							BasicEvent newBasicEvent = childCopy.getFault().getBasicEvents().get(i);
							mapOriginalToCopy.put(oldBasicEvent, newBasicEvent);
							mapCopyToOriginal.put(newBasicEvent, oldBasicEvent);
							
							dfsStack.push(oldBasicEvent);
						}
					}
				} else {
					childCopy = mapOriginalToCopy.get(child);
				}
				
				boolean moduleContainsCurrAndChild = this.containsFaultTreeNode(curr) && this.containsFaultTreeNode(child);
				if (moduleContainsCurrAndChild && !child.getFaultTreeNodeType().equals(FaultTreeNodeType.BASIC_EVENT)) {
					if (sparesInOriginalFaultTree.contains(child)) {
						fthelp.connectSpare(currCopy.getFault(), childCopy, currCopy);
					} else {
						fthelp.createFaultTreeEdge(currCopy.getFault(), childCopy, currCopy);
					}
				}
			}
		}
	}
	
	/**
	 * Return whether or not the node has a priority node above it
	 * @param ftnode the node
	 * @return true if priority above, false otherwise
	 */
	public boolean hasPriorityAbove(FaultTreeNode ftnode) {
		return mapOriginalToNodePlus.get(mapCopyToOriginal.get(ftnode)).hasPriorityAbove();
	}
	
	/**
	 * Return whether or not the node has a spare gate above it
	 * @param ftnode the node
	 * @return true if spare gate above, false otherwise
	 */
	public boolean hasSpareAbove(FaultTreeNode ftnode) {
		return mapOriginalToNodePlus.get(mapCopyToOriginal.get(ftnode)).hasSpareAbove();
	}
	
	/**
	 * Return whether or not the node has a spare gate below it
	 * @param ftnode the node
	 * @return true if spare gate below, false otherwise
	 */
	public boolean hasSpareBelow(FaultTreeNode ftnode) {
		return mapOriginalToNodePlus.get(mapCopyToOriginal.get(ftnode)).hasSpareBelow();
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
