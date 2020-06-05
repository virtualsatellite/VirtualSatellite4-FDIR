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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RepairAction;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Contains information for modules of the fault tree
 * @author jord_ad
 *
 */
public class Module {

	private FaultTreeHolder ftHolder;
	private FaultTreeNodePlus moduleRoot;
	private Set<ModuleProperty> moduleProperties;
	private Set<FaultTreeNodePlus> moduleNodes;
	private FaultTreeNode moduleRootCopy;
	private Map<FaultTreeNode, FaultTreeNode> mapCopyToOriginal;
	private Map<FaultTreeNode, FaultTreeNodePlus> mapOriginalToNodePlus;
	
	/**
	 * Default constructor.
	 * @param ftHolder the fault tree holder
	 * @param moduleRoot the root of the module
	 */
	public Module(FaultTreeHolder ftHolder, FaultTreeNodePlus moduleRoot) {
		this.ftHolder = ftHolder;
		this.moduleRoot = moduleRoot;
		this.moduleNodes = new HashSet<FaultTreeNodePlus>();
		this.moduleProperties = new HashSet<>();
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
		if (node.getFaultTreeNode().getFaultTreeNodeType().isNondeterministic()) {
			this.moduleProperties.add(ModuleProperty.NONDETERMINISTIC);
		}
		
		return this.moduleNodes.add(node);
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
	public Set<FaultTreeNodePlus> getModuleNodes() {
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
	public Map<FaultTreeNode, FaultTreeNode> getMapCopyToOriginal() {
		return this.mapCopyToOriginal;
	}
	
	
	/**
	 * Returns true if module is nondeterministic, false otherwise
	 * @return is nondeterministic
	 */
	public boolean isNondeterministic() {
		return this.moduleProperties.contains(ModuleProperty.NONDETERMINISTIC);
	}
	
	/**
	 * Returns true if module is partial observable, false otherwise
	 * @return is nondeterministic
	 */
	public boolean isPartialObservable() {
		return this.moduleProperties.contains(ModuleProperty.PARTIALOBSERVABLE);
	}
	
	/**
	 * Gets the module classification properties
	 * @return the classification properties attached to this module
	 */
	public Set<ModuleProperty> getModuleProperties() {
		return moduleProperties;
	}
	
	/**
	 * Marks the nodes in the module as harvested.
	 */
	public void harvestFromFaultTree() {
		this.moduleNodes.stream().forEach(FaultTreeNodePlus::harvestFromFaultTree);
	}
	
	/**
	 * Create the fault tree copy that is required for conversion to markov automata, with only the edges in the module
	 */
	public void constructFaultTreeCopy() {
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(this.moduleRoot.getFaultTreeNode().getConcept());
		Stack<FaultTreeNode> dfsStack = new Stack<FaultTreeNode>();
		Map<FaultTreeNode, FaultTreeNode> mapOriginalToCopy = new HashMap<FaultTreeNode, FaultTreeNode>();
		this.mapCopyToOriginal = new HashMap<FaultTreeNode, FaultTreeNode>();
		
		FaultTreeNode originalRoot = this.moduleRoot.getFaultTreeNode();
		Fault rootFault = new Fault(originalRoot.getConcept());
		FaultTreeNode rootCopy = ftBuilder.copyFaultTreeNode(originalRoot, rootFault.getFault());
		this.moduleRootCopy = rootFault;
		ftBuilder.connect(rootFault, rootCopy, rootFault);

		mapOriginalToCopy.put(originalRoot, rootCopy);
		mapCopyToOriginal.put(rootCopy, originalRoot);
		dfsStack.push(originalRoot);
		
		if (isPartialObservable()) {
			Set<FaultTreeNode> monitorRoots = ftHolder.getMonitorRoots();
			for (FaultTreeNode monitorRoot : monitorRoots) {
				FaultTreeNode monitorRootCopy = ftBuilder.copyFaultTreeNode(monitorRoot, rootFault);
				mapOriginalToCopy.put(monitorRoot, monitorRootCopy);
				mapCopyToOriginal.put(monitorRootCopy, monitorRoot);
				dfsStack.push(monitorRoot);
			}
		}
		
		while (!dfsStack.isEmpty()) {
			FaultTreeNode curr = dfsStack.pop();
			FaultTreeNode currCopy = mapOriginalToCopy.get(curr);
			
			if (isPartialObservable()) {
				for (FaultTreeNode monitor : ftHolder.getNodes(curr, EdgeType.MONITOR)) {
					ftBuilder.connectObserver(rootFault, currCopy, mapOriginalToCopy.get(monitor));
				}
			}
			
			List<FaultTreeNodePlus> children = mapOriginalToNodePlus.get(curr).getChildren();
			for (FaultTreeNodePlus childPlus : children) {
				FaultTreeNode child = childPlus.getFaultTreeNode();
				
				if (child instanceof BasicEvent) {
					// Basic event children are already copied when copying a fault
					continue;
				}
				
				FaultTreeNode childCopy;
				if (mapOriginalToCopy.get(child) == null) {
					childCopy = ftBuilder.copyFaultTreeNode(child, rootFault);
					mapOriginalToCopy.put(child, childCopy);
					mapCopyToOriginal.put(childCopy, child);
					
					dfsStack.push(child);
					
					if (childCopy instanceof Fault) {
						for (int i = 0; i < childCopy.getFault().getBasicEvents().size(); ++i) {
							BasicEvent oldBasicEvent = child.getFault().getBasicEvents().get(i);
							BasicEvent newBasicEvent = childCopy.getFault().getBasicEvents().get(i);
							mapOriginalToCopy.put(oldBasicEvent, newBasicEvent);
							mapCopyToOriginal.put(newBasicEvent, oldBasicEvent);
							
							for (RepairAction repairAction : newBasicEvent.getRepairActions()) {
								for (int j = 0; j < repairAction.getObservations().size(); ++j) {
									FaultTreeNode observation = repairAction.getObservations().get(i);
									FaultTreeNode newObservation = mapOriginalToCopy.get(observation);
									repairAction.getObservations().set(i, newObservation);
								}
							}
							
							dfsStack.push(oldBasicEvent);
						}
					}
				} else {
					childCopy = mapOriginalToCopy.get(child);
				}
				
				boolean moduleContainsCurrAndChild = this.containsFaultTreeNode(curr) && this.containsFaultTreeNode(child);
				if (moduleContainsCurrAndChild) {
					if (ftHolder.getNodes(curr, EdgeType.SPARE).contains(child)) {
						ftBuilder.connectSpare(rootFault, childCopy, currCopy);
					} else {
						ftBuilder.createFaultTreeEdge(rootFault, childCopy, currCopy);
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
		return moduleNodes.stream().anyMatch(ftPlus -> ftPlus.getFaultTreeNode().equals(ftnode));
	}
	
	@Override
	public String toString() {
		String res = moduleNodes.stream().map(FaultTreeNodePlus::toString).collect(Collectors.joining(","));
		return "{" + res + "}";
	}
	
	/**
	 * Gets the module for a given fault tree node
	 * @param modules a set of modules
	 * @param node a fault tree node
	 * @return the module for the fault tree node, or null of no such module exists
	 */
	public static Module getModule(Set<Module> modules, FaultTreeNode node) {
		return modules.stream().filter(module -> module.getRootNode().equals(node)).findAny().orElse(null);
	}
}
