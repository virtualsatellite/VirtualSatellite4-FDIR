/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;

/**
 * This helper class holds fault tree data and provides interferable data
 * in a convenient way.
 * @author muel_s8
 *
 */

public class FaultTreeHolder {
	private FaultTreeNode root;
	
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToChildren;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToSpares;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToParents;
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapNodeToAllParents;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToDEPTriggers;
	private Map<FaultTreeNode, List<MONITOR>> mapNodeToMonitors;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToSubNodes;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapFaultToBasicEvents;
	private Map<BasicEvent, Fault> mapBasicEventToFault;
	private Map<BasicEvent, Double> mapBasicEventToHotFailRate;
	private Map<BasicEvent, Double> mapBasicEventToColdFailRate;
	private Map<BasicEvent, Double> mapBasicEventToRepairRate;
	private Map<FaultTreeNode, Integer> mapNodeToIndex;
	private Set<FaultTreeNode> nodes;
	private Set<FaultTree> faultTrees;
	
	/**
	 * Standard constructor
	 * @param root the root of the fault tree
	 */
	public FaultTreeHolder(FaultTreeNode root) {
		this.root = root;
		init();
	}
	
	/**
	 * Initializes the inferred data
	 */
	private void init() {
		FaultTreeHelper ftHelper = new FaultTreeHelper(root.getConcept());
		
		initDataStructures();
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.offer(root);
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			
			if (!nodes.add(node)) {
				continue;
			}
			
			faultTrees.add(node.getFault().getFaultTree());
			List<FaultTreeNode> children = ftHelper.getChildren(node, faultTrees);
			List<FaultTreeNode> spares = ftHelper.getSpares(node, faultTrees);
			mapNodeToChildren.put(node, children);
			mapNodeToSpares.put(node, spares);
			
			for (FaultTreeNode spare : spares) {
				List<FaultTreeNode> parentsSpare = mapNodeToParents.get(spare);
				if (parentsSpare == null) {
					parentsSpare = new ArrayList<>();
					mapNodeToParents.put(spare, parentsSpare);
				}
				parentsSpare.add(node);
			}
			
			if (mapNodeToParents.get(node) == null) {
				mapNodeToParents.put(node, new ArrayList<>());
			}
			
			for (FaultTreeNode child : children) {
				List<FaultTreeNode> parentsChild = mapNodeToParents.get(child);
				if (parentsChild == null) {
					parentsChild = new ArrayList<>();
					mapNodeToParents.put(child, parentsChild);
				}
				parentsChild.add(node);
			}
			
			if (mapNodeToMonitors.get(node) == null) {
				mapNodeToMonitors.put(node, new ArrayList<>());
			}
			
			if (node instanceof Fault) {
				for (FaultTreeEdge dep : node.getFault().getFaultTree().getDeps()) {
					FaultTreeNode depGate = dep.getFrom();
					FaultTreeNode dependentEvent = dep.getTo();
					
					List<FaultTreeNode> nodeFDEPTriggers = mapNodeToDEPTriggers.get(dependentEvent);
					if (nodeFDEPTriggers == null) {
						nodeFDEPTriggers = new ArrayList<>();
						mapNodeToDEPTriggers.put(dependentEvent, nodeFDEPTriggers);
					}
					
					toProcess.add(depGate);
					nodeFDEPTriggers.add(depGate);

					if (mapNodeToParents.get(depGate) == null) {
						mapNodeToParents.put(depGate, new ArrayList<>());
					}
					mapNodeToParents.get(depGate).add(dependentEvent);
				}
				
				for (FaultTreeEdge obs : node.getFault().getFaultTree().getObservations()) {
					MONITOR monitor = (MONITOR) obs.getTo();
					FaultTreeNode monitored = obs.getFrom();
					
					List<MONITOR> nodeMonitors = mapNodeToMonitors.get(monitored);
					if (nodeMonitors == null) {
						nodeMonitors = new ArrayList<>();
						mapNodeToMonitors.put(monitored, nodeMonitors);
					}
					
					toProcess.add(monitor);
					nodeMonitors.add(monitor);

					if (mapNodeToParents.get(monitor) == null) {
						mapNodeToParents.put(monitor, new ArrayList<>());
					}
					mapNodeToParents.get(monitor).add(monitored);
				}

				mapFaultToBasicEvents.put(node, new ArrayList<>(node.getFault().getBasicEvents()));
				for (BasicEvent basicEvent : node.getFault().getBasicEvents()) {
					mapBasicEventToFault.put(basicEvent, node.getFault());
					mapBasicEventToHotFailRate.put(basicEvent, basicEvent.getHotFailureRateBean().getValueToBaseUnit());
					mapBasicEventToColdFailRate.put(basicEvent, basicEvent.getColdFailureRateBean().getValueToBaseUnit());
					mapBasicEventToRepairRate.put(basicEvent, basicEvent.getRepairRateBean().getValueToBaseUnit());
					mapNodeToParents.put(basicEvent, Collections.singletonList(node.getFault()));
				}
			}
			
			toProcess.addAll(children);
			toProcess.addAll(spares);
		}
		
		indexNodes();
		mapNodeToSubNodes();
	}
	
	/**
	 * Initializes all data structures
	 */
	private void initDataStructures() {
		mapNodeToChildren = new HashMap<>();
		mapNodeToSpares = new HashMap<>();
		mapNodeToParents = new HashMap<>();
		mapNodeToDEPTriggers = new HashMap<>();
		mapFaultToBasicEvents = new HashMap<>();
		mapNodeToMonitors = new HashMap<>();
		mapNodeToIndex = new HashMap<>();
		mapBasicEventToFault = new HashMap<>();
		mapBasicEventToHotFailRate = new HashMap<>();
		mapBasicEventToColdFailRate = new HashMap<>();
		mapBasicEventToRepairRate = new HashMap<>();
		mapNodeToSubNodes = new HashMap<>();
		nodes = new HashSet<>();
		faultTrees = new HashSet<>();
	}
	
	/**
	 * Creates an index of all nodes
	 */
	private void indexNodes() {
		List<FaultTreeNode> indexedNodes = new ArrayList<>(nodes);
		for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : mapFaultToBasicEvents.entrySet()) {
			indexedNodes.addAll(entry.getValue());
		}
		
		for (int i = 0; i < indexedNodes.size(); ++i) {
			mapNodeToIndex.put(indexedNodes.get(i), i);
		}
		
	}
	
	/**
	 * Maps all nodes to the nodes of the sub tree
	 */
	private void mapNodeToSubNodes() {
		for (FaultTreeNode node : getNodes()) {
			List<FaultTreeNode> children = getMapNodeToChildren().get(node);
			List<FaultTreeNode> spares = getMapNodeToSpares().get(node);
			List<FaultTreeNode> fdepTriggers = getMapNodeToDEPTriggers().getOrDefault(node, Collections.emptyList());
			
			List<FaultTreeNode> subNodes = new ArrayList<>(children);
			subNodes.addAll(spares);
			subNodes.addAll(fdepTriggers);
			
			mapNodeToSubNodes.put(node, subNodes);
		}
	}
	
	/**
	 * Gets a mapping from any node to the children
	 * @return map from node to children
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapNodeToChildren() {
		return mapNodeToChildren;
	}
	
	/**
	 * Gets a mapping from any node to the FDEP Triggers
	 * @return map from node to FDEP triggers
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapNodeToDEPTriggers() {
		return mapNodeToDEPTriggers;
	}
	
	/**
	 * Gets a mapping from any node to the Observers
	 * @return map from node to Observer
	 */
	public Map<FaultTreeNode, List<MONITOR>> getMapNodeToMonitors() {
		return mapNodeToMonitors;
	}
	
	/**
	 * Checks if the passed tree is partially observable
	 * @return true iff the tree has an observer node
	 */
	public boolean isPartialObservable() {
		return getNodes().stream().filter(node -> node instanceof MONITOR).findAny().isPresent();
	}
	
	/**
	 * Gets a mapping from any node to the spares
	 * @return map from node to spares
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapNodeToSpares() {
		return mapNodeToSpares;
	}
	
	/**
	 * Gets a mapping from any node to the parents
	 * @return map from node to parents
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapNodeToParents() {
		return mapNodeToParents;
	}
	
	/**
	 * Gets a mapping from a node to all parents
	 * @return a mapping from a node to all parents
	 */
	public Map<FaultTreeNode, Set<FaultTreeNode>> getMapNodeToAllParents() {
		if (mapNodeToAllParents == null) {
			mapNodeToAllParents = new HashMap<>();
			for (FaultTreeNode node : mapNodeToParents.keySet()) {
				Set<FaultTreeNode> allParents = new HashSet<>();
				mapNodeToAllParents.put(node, allParents);
				Queue<FaultTreeNode> queue = new LinkedList<>();
				queue.addAll(mapNodeToParents.get(node));
				while (!queue.isEmpty()) {
					FaultTreeNode parent = queue.poll();
					if (allParents.add(parent)) {
						queue.addAll(mapNodeToParents.get(parent));
					}
				}
			}
		}
		return mapNodeToAllParents;
	}
	
	/**
	 * Gets a mapping from any fault to the basic events
	 * @return map fault to basic events
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapFaultToBasicEvents() {
		return mapFaultToBasicEvents;
	}
	
	/**
	 * Gets a mapping from any basic event to its owning fault
	 * @return map basic event to fault
	 */
	public Map<BasicEvent, Fault> getMapBasicEventToFault() {
		return mapBasicEventToFault;
	}
	
	/**
	 * Gets the root element of this fault tree
	 * @return the root node
	 */
	public FaultTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Gets all fault trees and sub fault trees contained in it
	 * @return set of a all fault trees making up the overall fault tree
	 */
	public Set<FaultTree> getFaultTrees() {
		return faultTrees;
	}
	
	/**
	 * Gets all nodes in the fault tree
	 * @return set containing all nodes
	 */
	public Set<FaultTreeNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Gets the mapping from a fault tre node to its index
	 * @param node the fault tree node
	 * @return the index of the fault tree node
	 */
	public int getNodeIndex(FaultTreeNode node) {
		return mapNodeToIndex.get(node);
	}
	
	/**
	 * Gets the cached cold failure rate of a basic event in the basic unit
	 * @param be the basic event
	 * @return the cold fail rate
	 */
	public double getColdFailRate(BasicEvent be) {
		return mapBasicEventToColdFailRate.get(be);
	}
	
	/**
	 * Gets the cached hot failure rate of a basic event in the basic unit
	 * @param be the basic event
	 * @return the hot fail rate
	 */
	public double getHotFailRate(BasicEvent be) {
		return mapBasicEventToHotFailRate.get(be);
	}
	
	/**
	 * Gets the cached repair rate of a basic event in the basic unit
	 * @param be the basic event
	 * @return the repair rate
	 */
	public double getRepairRate(BasicEvent be) {
		return mapBasicEventToRepairRate.get(be);
	}
	
	/**
	 * Gets all sub nodes for a given node (children, spares, fdeptriggers, etc.)
	 * @return a list of all subnodes of the given node
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapNodeToSubNodes() {
		return mapNodeToSubNodes;
	}
	
	/**
	 * Gets a fault tree node by name
	 * @param name the name of the node
	 * @param ftnClazz the fault tree node type
	 * @param <T> the fault tree node type
	 * @return the node if there is 
	 */
	@SuppressWarnings("unchecked")
	public <T extends FaultTreeNode> T getNodeByName(String name, Class<T> ftnClazz) {
		if (ftnClazz == BasicEvent.class) {
			return (T) getMapBasicEventToFault().keySet().stream()
					.filter(be -> be.getName().equals(name)).findFirst().get();
		} else {
			return (T) getNodes().stream()
					.filter(node -> ftnClazz.isAssignableFrom(node.getClass()) && node.getName().equals(name)).findFirst().get();
		}
	}
}
