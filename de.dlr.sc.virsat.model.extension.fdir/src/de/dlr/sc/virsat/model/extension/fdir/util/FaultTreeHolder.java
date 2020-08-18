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

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.SEQ;

/**
 * This helper class holds fault tree data and provides interferable data
 * in a convenient way.
 * @author muel_s8
 *
 */

public class FaultTreeHolder {
	
	private FaultTreeNode root;
	private Map<FaultTreeNodeType, Set<FaultTreeNode>> mapTypeToNodes;
	private Set<FaultTreeNode> nodes;
	private Set<FaultTree> faultTrees;
	
	private Map<FaultTreeNode, NodeHolder> mapNodeToNodeHolders;
	private Map<BasicEvent, BasicEventHolder> mapBEToBEHolders;
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapNodeToAllParents;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodeToSubNodes;
	private Map<Fault, List<ADEP>> mapFaultToContainedDeps;
	
	/**
	 * Standard constructor
	 * @param root the root of the fault tree
	 */
	public FaultTreeHolder(FaultTreeNode root) {
		this.root = root;
		
		initDataStructures();
		collectFaultTrees();
		processFaultTree();
		indexNodes();
	}
	
	/**
	 * Builds the actual data
	 */
	private void processFaultTree() {
		FaultTreeHelper ftHelper = new FaultTreeHelper();
		Map<EdgeType, Map<FaultTreeNode, List<FaultTreeNode>>> mapEdgeTypesToNodes = ftHelper.getMapEdgeTypeToNodes(faultTrees);
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.offer(root);
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();

			mapTypeToNodes.get(node.getFaultTreeNodeType()).add(node);
			if (!nodes.add(node)) {
				continue;
			}
			
			if (faultTrees.add(node.getFault().getFaultTree())) {
				mapEdgeTypesToNodes = ftHelper.getMapEdgeTypeToNodes(faultTrees);
			}
			toProcess.addAll(processNode(node, mapEdgeTypesToNodes));
		}
		
		for (FaultTreeNode node : ftHelper.getAllNodes(root.getFault())) {
			if (node instanceof SEQ) {
				mapTypeToNodes.get(node.getFaultTreeNodeType()).add(node);
				if (!nodes.add(node)) {
					continue;
				}
				
				if (faultTrees.add(node.getFault().getFaultTree())) {
					mapEdgeTypesToNodes = ftHelper.getMapEdgeTypeToNodes(faultTrees);
				}
				processNode(node, mapEdgeTypesToNodes);
			}
		}
		
		for (FaultTree ft : faultTrees) {
			Fault fault = ft.getRoot();
			for (FaultTreeNode gate : ft.getGates()) {
				if (gate instanceof ADEP) {
					mapFaultToContainedDeps.computeIfAbsent(fault, key -> new ArrayList<>()).add((ADEP) gate);
				}
			}
		}
	}
	
	/**
	 * Processes a single fault tree node
	 * @param node the fault tree node to process
	 * @param mapEdgeTypesToNodes a fault tree helper
	 * @return a list of nodes that need to be queued
	 */
	private List<FaultTreeNode> processNode(FaultTreeNode node, Map<EdgeType, Map<FaultTreeNode, List<FaultTreeNode>>> mapEdgeTypesToNodes) {
		List<FaultTreeNode> toProcess = new ArrayList<>();
		
		List<FaultTreeNode> children = mapEdgeTypesToNodes.get(EdgeType.CHILD).getOrDefault(node, Collections.emptyList());
		List<FaultTreeNode> spares =  mapEdgeTypesToNodes.get(EdgeType.SPARE).getOrDefault(node, Collections.emptyList());
		
		NodeHolder nodeHolder = getNodeHolder(node);
		nodeHolder.mapEdgeTypeToNodes.put(EdgeType.CHILD, children);
		nodeHolder.mapEdgeTypeToNodes.put(EdgeType.SPARE, spares);
		
		for (FaultTreeNode spare : spares) {
			NodeHolder spareHolder = getNodeHolder(spare);
			spareHolder.getNodes(EdgeType.PARENT).add(node);
		}
		
		for (FaultTreeNode child : children) {
			NodeHolder childHolder = getNodeHolder(child);
			childHolder.getNodes(EdgeType.PARENT).add(node);
		}
		
		if (node instanceof Fault) {
			Fault fault = (Fault) node;
			toProcess.addAll(processDeps(fault));
			toProcess.addAll(processMonitors(fault));
			
			nodeHolder.mapEdgeTypeToNodes.put(EdgeType.BE, new ArrayList<>(fault.getBasicEvents()));
			processBasicEvents(fault);
		}
		
		toProcess.addAll(children);
		toProcess.addAll(spares);
		
		return toProcess;
	}
	
	/**
	 * Processes the dep edges in a fault
	 * @param fault the fault
	 * @return a list of dep gates that need to be queued
	 */
	private List<FaultTreeNode> processDeps(Fault fault) {
		List<FaultTreeNode> depGates = new ArrayList<>();
		for (FaultTreeEdge dep : fault.getFaultTree().getDeps()) {
			FaultTreeNode depGate = dep.getFrom();
			FaultTreeNode dependentEvent = dep.getTo();
			
			NodeHolder dependentHolder = getNodeHolder(dependentEvent);
			dependentHolder.getNodes(EdgeType.DEP).add(depGate);
			
			NodeHolder parentHolder = getNodeHolder(depGate);
			parentHolder.getNodes(EdgeType.PARENT).add(dependentEvent);
			
			depGates.add(depGate);
		}
		
		return depGates;
	}
	
	/**
	 * Processes the observation edges in a fault
	 * @param fault the fault
	 * @return a list of monitor gates that need to be queued
	 */
	private List<FaultTreeNode> processMonitors(Fault fault) {
		List<FaultTreeNode> monitorGates = new ArrayList<>();
		for (FaultTreeEdge obs : fault.getFaultTree().getObservations()) {
			FaultTreeNode monitor = obs.getTo();
			FaultTreeNode monitored = obs.getFrom();
			
			NodeHolder monitorHolder = getNodeHolder(monitored);
			monitorHolder.getNodes(EdgeType.MONITOR).add(monitor);
			
			monitorGates.add(monitor);
		}
		
		return monitorGates;
	}
	
	/**
	 * Processes the basic events attached to a fault
	 * @param fault the fault
	 */
	private void processBasicEvents(Fault fault) {
		for (BasicEvent basicEvent : fault.getBasicEvents()) {
			nodes.add(basicEvent);
			mapBEToBEHolders.put(basicEvent, new BasicEventHolder(basicEvent));
					
			NodeHolder parentHolder = getNodeHolder(basicEvent);
			parentHolder.getNodes(EdgeType.PARENT).add(fault);
		}
	}
	
	/**
	 * Collects fault trees with related edges to this tree
	 */
	private void collectFaultTrees() {
		IBeanStructuralElementInstance rootParent = root.getParent();
		if (rootParent != null) {
			StructuralElementInstance rootSei = (StructuralElementInstance) VirSatEcoreUtil.getRootContainer(root.getParent().getStructuralElementInstance(), true);
			List<StructuralElementInstance> deepChildren = rootSei.getDeepChildren();
			deepChildren.add(rootSei);
			for (StructuralElementInstance child : deepChildren) {
				BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance();
				beanSei.setStructuralElementInstance(child);
				for (Fault fault : beanSei.getAll(Fault.class)) {
					if (!fault.getFaultTree().getPropagations().isEmpty()) {
						faultTrees.add(fault.getFaultTree());
					}
				}
			}
		}
	}
	
	/**
	 * Initializes all data structures
	 */
	private void initDataStructures() {
		mapNodeToNodeHolders = new HashMap<>();
		mapBEToBEHolders = new HashMap<>();
		mapTypeToNodes = new HashMap<>();
		nodes = new HashSet<>();
		faultTrees = new HashSet<>();
		mapFaultToContainedDeps = new HashMap<>(); 
		
		for (FaultTreeNodeType type : FaultTreeNodeType.values()) {
			mapTypeToNodes.put(type, new HashSet<>());
		}
	}
	
	/**
	 * Creates an index of all nodes
	 */
	private void indexNodes() {
		List<FaultTreeNode> indexedNodes = new ArrayList<>(nodes);
		for (Entry<FaultTreeNode, NodeHolder> entry : mapNodeToNodeHolders.entrySet()) {
			indexedNodes.addAll(entry.getValue().getNodes(EdgeType.BE));
		}
		
		for (int i = 0; i < indexedNodes.size(); ++i) {
			getNodeHolder(indexedNodes.get(i)).index = i;
		}
	}
	
	/**
	 * Gets the node holder for a given node or creates a new one if it doesnt exist
	 * @param node the node holder
	 * @return a node holder for the node
	 */
	public NodeHolder getNodeHolder(FaultTreeNode node) {
		return mapNodeToNodeHolders.computeIfAbsent(node, key -> new NodeHolder());
	}
	
	/**
	 * Gets a mapping from any node to the children
	 * @return map from node to children
	 */
	public List<FaultTreeNode> getNodes(FaultTreeNode node, EdgeType... edgeTypes) {
		NodeHolder nodeHolder = getNodeHolder(node);
		return nodeHolder.getNodes(edgeTypes);
	}
	
	/**
	 * Checks if the passed tree is partially observable
	 * @return true iff the tree has an observer node
	 */
	public boolean isPartialObservable() {
		return !getNodes(FaultTreeNodeType.MONITOR).isEmpty();
	}
	
	/**
	 * Gets a mapping from a node to all parents
	 * @return a mapping from a node to all parents
	 */
	public Map<FaultTreeNode, Set<FaultTreeNode>> getMapNodeToAllParents() {
		if (mapNodeToAllParents == null) {
			mapNodeToAllParents = new HashMap<>();
			for (Entry<FaultTreeNode, NodeHolder> entry : mapNodeToNodeHolders.entrySet()) {
				FaultTreeNode node = entry.getKey();
				List<FaultTreeNode> parents = entry.getValue().getNodes(EdgeType.PARENT);
				
				Set<FaultTreeNode> allParents = new HashSet<>();
				mapNodeToAllParents.put(node, allParents);
				Queue<FaultTreeNode> queue = new LinkedList<>();
				queue.addAll(parents);
				while (!queue.isEmpty()) {
					FaultTreeNode parent = queue.poll();
					if (allParents.add(parent)) {
						queue.addAll(mapNodeToNodeHolders.get(parent).getNodes(EdgeType.PARENT));
					}
				}
			}
		}
		return mapNodeToAllParents;
	}
	
	/**
	 * Gets the root element of this fault tree
	 * @return the root node
	 */
	public FaultTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Gets all nodes in the fault tree
	 * @return set containing all nodes
	 */
	public Set<FaultTreeNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Gets all the nodes in the fault tree of the specified type
	 * @param type the node type
	 * @return all nodes of the specfied type
	 */
	public Set<FaultTreeNode> getNodes(FaultTreeNodeType type) {
		return mapTypeToNodes.get(type);
	}
	
	/**
	 * Gets the mapping from a fault tre node to its index
	 * @param node the fault tree node
	 * @return the index of the fault tree node
	 */
	public int getNodeIndex(FaultTreeNode node) {
		return getNodeHolder(node).index;
	}
	
	/**
	 * Gets the basic events in the fault tree
	 * @return the basic events
	 */
	public Set<BasicEvent> getBasicEvents() {
		return mapBEToBEHolders.keySet();
	}
	
	/**
	 * Gets the basic event holder for a basic event
	 * @param be the basic event
	 * @return the basic event holder
	 */
	public BasicEventHolder getBasicEventHolder(BasicEvent be) {
		return mapBEToBEHolders.get(be);
	}
	
	/**
	 * Gets a mapping from any basic event to its owning fault
	 * @return map basic event to fault
	 */
	public Fault getFault(BasicEvent be) {
		return getBasicEventHolder(be).getFault();
	}
	
	/**
	 * Gets all sub nodes for a given node (children, spares, fdeptriggers, etc.)
	 * @return a list of all subnodes of the given node
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getMapNodeToSubNodes() {
		if (mapNodeToSubNodes == null) {
			mapNodeToSubNodes = new HashMap<>();
			for (FaultTreeNode node : getNodes()) {
				List<FaultTreeNode> subNodes = getNodes(node, EdgeType.CHILD, EdgeType.SPARE, EdgeType.DEP, EdgeType.BE);
				mapNodeToSubNodes.put(node, subNodes);
			}
		}
		return mapNodeToSubNodes;
	}
	
	/**
	 * Gets all subnodes for a fault.
	 * @return all sub nodes of the fault
	 */
	public Set<FaultTreeNode> getAllSubNodes(Fault fault) {
		Set<FaultTreeNode> allSubNodes = new HashSet<>();
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.offer(fault);
		allSubNodes.addAll(toProcess);
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			getMapNodeToSubNodes().get(node).forEach(child -> {
				if (!allSubNodes.contains(child)) {
					allSubNodes.add(child);
					toProcess.add(child);
				}
			});
		}
		
		return allSubNodes;
	}
	
	/**
	 * Gets all local sub nodes of a fault.
	 * I.e. all sub nodes until the next level of faults.
	 * @param fault the fault
	 * @return a set of all local sub nodes
	 */
	public Set<FaultTreeNode> getAllLocalSubNodes(Fault fault) {
		Set<FaultTreeNode> allSubNodes = getAllSubNodes(fault);
		Set<FaultTreeNode> allLocalSubNodes = new HashSet<>(allSubNodes);
		for (FaultTreeNode node : allSubNodes) {
			if (node instanceof Fault && !node.equals(fault)) {
				Fault subFault = (Fault) node;
				Set<FaultTreeNode> subSubNodes = getAllSubNodes(subFault);
				subSubNodes.remove(subFault);
				allLocalSubNodes.removeAll(subSubNodes);

			}
		}
		
		return allLocalSubNodes;
	}
	
	/**
	 * Gets all incomfing edges for a give fault tree node
	 * @param node the node
	 * @return the incoming edges for a fault tree node
	 */
	public Set<FaultTreeEdge> getIncomingEdges(FaultTreeNode node) {
		Set<FaultTreeEdge> incomingEdges = new HashSet<>();
		FaultTreeHelper ftHelper = new FaultTreeHelper();
		for (FaultTree ft : faultTrees) {
			List<FaultTreeEdge> edges = ftHelper.getEdges(ft.getRoot());
			for (FaultTreeEdge edge : edges) {
				if (edge.getTo().equals(node)) {
					incomingEdges.add(edge);
				}
			}
		}
		
		return incomingEdges;
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
		return (T) getNodes().stream()
				.filter(node -> ftnClazz.isAssignableFrom(node.getClass()) && node.getName().equals(name)).findFirst().get();
	}
	
	/**
	 * Gets all direct child faults of this fault
	 * @param node the fault
	 * @return the set of direct child faults
	 */
	public List<Fault> getChildFaults(FaultTreeNode root) {
		List<Fault> childFaults = new ArrayList<>();
		Queue<FaultTreeNode> nodes = new LinkedList<FaultTreeNode>();
		nodes.add(root);
		
		while (!nodes.isEmpty()) {
			FaultTreeNode node = nodes.poll();
			List<FaultTreeNode> children = getNodes(node, EdgeType.CHILD);
			for (FaultTreeNode child : children) {
				if (child instanceof Fault) {
					Fault childFault = (Fault) child;
					if (!childFaults.contains(childFault)) {
						childFaults.add(childFault);
					}
				} else {
					nodes.add(child);
				}
			}
		}
		
		return childFaults;
	}
	
	/**
	 * Gets all failure modes for the fault event.
	 * An event is a failure mode iff 
	 * - its a direct child fault
	 * - its a basic event attached to the root event
	 * @param fault the fault
	 * @return all failure modes for the root event
	 */
	public List<FaultEvent> getFailureModes(Fault fault) {
		List<FaultEvent> failureModes = new ArrayList<>(getChildFaults(fault));
		failureModes.addAll(fault.getBasicEvents());
		return failureModes;
	}
	
	/**
	 * Gets the root nodes of the monitoring gates.
	 * @return the set root nodes of any monitoring gate
	 */
	public Set<FaultTreeNode> getMonitorRoots() {
		Set<FaultTreeNode> monitorRoots = new HashSet<>();
		Set<FaultTreeNode> monitors = getNodes(FaultTreeNodeType.MONITOR);
		for (FaultTreeNode monitor : monitors) {
			monitorRoots.addAll(getRoots(monitor));
		}
		return monitorRoots;
	}
	
	/**
	 * Gets all roots for a given node
	 * @param node the node
	 * @return all roots that have the given node as a sub node
	 */
	public Set<FaultTreeNode> getRoots(FaultTreeNode node) {
		Set<FaultTreeNode> roots = new HashSet<>();
		Set<FaultTreeNode> allParents = getMapNodeToAllParents().get(node);
		for (FaultTreeNode allParent : allParents) {
			if (getNodes(allParent, EdgeType.PARENT).isEmpty()) {
				roots.add(allParent);
			}
		}
		
		if (allParents.isEmpty()) {
			roots.add(node);
		}
		
		return roots;
	}
	
	public Map<Fault, List<ADEP>> getMapFaultToContainedDeps() {
		return mapFaultToContainedDeps;
	}
	
	/**
	 * Get a statistics object for the fault tree
	 * @return the main fault tree statistics
	 */
	public FaultTreeStatistics getStatistics() {
		FaultTreeStatistics statistics = new FaultTreeStatistics();
		
		statistics.countNodes = nodes.size();
		for (FaultTreeNode node : nodes) {
			if (!node.getFaultTreeNodeType().isStatic()) {
				statistics.dynamic = true;
			}
		}
		
		for (BasicEventHolder beHolder : mapBEToBEHolders.values()) {
			if (beHolder.isRepairDefined()) {
				statistics.repair = true;
			}
		}
		
		statistics.partialObservable = isPartialObservable();
		
		for (FaultTreeNodeType nodeType : FaultTreeNodeType.values()) {
			statistics.countNodeType[nodeType.ordinal()] = mapTypeToNodes.getOrDefault(nodeType, Collections.emptySet()).size();
		}
		statistics.countNodeType[FaultTreeNodeType.BASIC_EVENT.ordinal()] = mapBEToBEHolders.size();
		
		return statistics;
	}
}
