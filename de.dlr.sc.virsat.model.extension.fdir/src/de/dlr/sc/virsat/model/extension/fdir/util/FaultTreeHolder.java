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
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;

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
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToDEPTriggers;
	private Map<FaultTreeNode, List<OBSERVER>> mapNodeToObservers;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToSubNodes;
	private Map<FaultTreeNode, Set<BasicEvent>> mapFaultToBasicEvents;
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
		
		mapNodeToChildren = new HashMap<>();
		mapNodeToSpares = new HashMap<>();
		mapNodeToParents = new HashMap<>();
		mapNodeToDEPTriggers = new HashMap<>();
		mapFaultToBasicEvents = new HashMap<>();
		mapNodeToObservers = new HashMap<>();
		mapNodeToIndex = new HashMap<>();
		mapBasicEventToFault = new HashMap<>();
		mapBasicEventToHotFailRate = new HashMap<>();
		mapBasicEventToColdFailRate = new HashMap<>();
		mapBasicEventToRepairRate = new HashMap<>();
		mapNodeToSubNodes = new HashMap<>();
		nodes = new HashSet<>();
		faultTrees = new HashSet<>();
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.offer(root);
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			
			if (nodes.contains(node)) {
				continue;
			} else {
				nodes.add(node);
			}
			
			nodes.add(node);
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
			
			if (mapNodeToDEPTriggers.get(node) == null) {
				mapNodeToDEPTriggers.put(node, new ArrayList<>());
			}
			
			if (mapNodeToObservers.get(node) == null) {
				mapNodeToObservers.put(node, new ArrayList<>());
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
					OBSERVER observer = (OBSERVER) obs.getFrom();
					FaultTreeNode observable = obs.getTo();
					
					List<OBSERVER> nodeObservers = mapNodeToObservers.get(observable);
					if (nodeObservers == null) {
						nodeObservers = new ArrayList<>();
						mapNodeToObservers.put(observable, nodeObservers);
					}
					
					toProcess.add(observer);
					nodeObservers.add(observer);

					if (mapNodeToParents.get(observer) == null) {
						mapNodeToParents.put(observer, new ArrayList<>());
					}
					mapNodeToParents.get(observer).add(observable);
				}

				mapFaultToBasicEvents.put(node, new HashSet<>(node.getFault().getBasicEvents()));
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
		
		List<FaultTreeNode> indexedNodes = new ArrayList<>(nodes);
		for (Entry<FaultTreeNode, Set<BasicEvent>> entry : mapFaultToBasicEvents.entrySet()) {
			indexedNodes.addAll(entry.getValue());
		}
		
		for (int i = 0; i < indexedNodes.size(); ++i) {
			mapNodeToIndex.put(indexedNodes.get(i), i);
		}
		
		for (FaultTreeNode node : getNodes()) {
			List<FaultTreeNode> children = getMapNodeToChildren().get(node);
			List<FaultTreeNode> spares = getMapNodeToSpares().get(node);
			List<FaultTreeNode> fdepTriggers = getMapNodeToDEPTriggers().get(node);
			
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
	public Map<FaultTreeNode, List<OBSERVER>> getMapNodeToObservers() {
		return mapNodeToObservers;
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
	 * Gets a mapping from any fault to the basic events
	 * @return map fault to basic events
	 */
	public Map<FaultTreeNode, Set<BasicEvent>> getMapFaultToBasicEvents() {
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
}
