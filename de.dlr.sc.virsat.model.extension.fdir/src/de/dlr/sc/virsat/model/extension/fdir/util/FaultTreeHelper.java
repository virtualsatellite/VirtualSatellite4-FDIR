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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.AND;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
import de.dlr.sc.virsat.model.extension.fdir.model.OR;
import de.dlr.sc.virsat.model.extension.fdir.model.PAND;
import de.dlr.sc.virsat.model.extension.fdir.model.PANDI;
import de.dlr.sc.virsat.model.extension.fdir.model.PDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.POR;
import de.dlr.sc.virsat.model.extension.fdir.model.PORI;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.SAND;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;

/**
 * 
 * @author lapi_an
 * 
 *         Helper functions for work with static/dynamic fault trees;
 *
 */
public class FaultTreeHelper {
	private final Concept concept;

	public static final int NODE_INDEX = 0;

	// SAND gate
	public static final int SAND_LEFTMOST_POR_GATE_INDEX = 0;
	public static final int SAND_LOWER_AND_GATE_INDEX = 1;
	public static final int SAND_LOWER_OR_GATE_INDEX = 2;
	public static final int SAND_RIGHTMOST_POR_GATE_INDEX = 3;

	public static final int PAND_AND_GATE_INDEX = 0;
	public static final int PAND_POR_GATE_INDEX = 1;

	// Inclusive POR gate
	public static final int INCLUSIVE_POR_UPPER_OR_GATE = 0;
	public static final int INCLUSIVE_POR_POR_GATE = 1;
	public static final int INCLUSIVE_POR_LOWER_OR_GATE = 2;
	public static final int INCLUSIVE_POR_SAND_GATE = 3;

	// Inclusive PAND gate
	public static final int INCLUSIVE_PAND_UPPER_OR_GATE = 0;

	// Inclusive PAND SAND part of the gate
	public static final int INCLUSIVE_PAND_SAND_LEFTMOST_POR = 1;
	public static final int INCLUSIVE_PAND_SAND_LOWER_AND_GATE_INDEX = 2;
	public static final int INCLUSIVE_PAND_SAND_LOWER_OR_GATE_INDEX = 3;
	public static final int INCLUSIVE_PAND_SAND_RIGHTMOST_POR_GATE_INDEX = 4;

	// Inclusive PAND exclusive PAND part of the gate
	public static final int INCLUSIVE_PAND_EXCLUSIVE_PAND_AND_GATE = 5;
	public static final int INCLUSIVE_PAND_EXCLUSIVE_PAND_POR_GATE = 6;
	
	//RDEP
	public static final int RDEP_FDEP_GATE = 0;
	public static final int RDEP_SPARE_GATE = 1;
	public static final int RDEP_RATE_CHANGE_FAULT = 2;

	/**
	 * A basic constructor of FaultTreeHelper
	 * 
	 * @param concept
	 *            the concept to be used for construction
	 */
	public FaultTreeHelper(Concept concept) {
		this.concept = concept;
	}

	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @param inputs
	 *            Number of inputs into the gate;
	 * @return The new gate basic AND gate;
	 */
	public VOTE createBasicAndGate(Fault fault, int inputs) {
		FaultTreeHelper helper = new FaultTreeHelper(concept);
		VOTE gate = (VOTE) helper.createGate(fault, FaultTreeNodeType.VOTE);
		gate.setVotingThreshold(inputs);
		return gate;
	}

	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @return The new gate basic OR gate;
	 */
	public VOTE createBasicOrGate(Fault fault) {
		VOTE gate = (VOTE) createGate(fault, FaultTreeNodeType.VOTE);
		gate.setVotingThreshold(1);
		return gate;
	}

	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @param inputs
	 *            Number of inputs into the gate;
	 * @return The new gate basic SAND gate;
	 */
	public List<FaultTreeNode> createBasicSAndGate(Fault fault, int inputs) {
		List<FaultTreeNode> nodesList = new ArrayList<>();

		Gate leftmostPor = createGate(fault, FaultTreeNodeType.POR);
		nodesList.add(SAND_LEFTMOST_POR_GATE_INDEX, leftmostPor);

		Gate lowerAnd = createBasicAndGate(fault, inputs);
		nodesList.add(SAND_LOWER_AND_GATE_INDEX, lowerAnd);

		Gate lowerOr = createBasicOrGate(fault);
		nodesList.add(SAND_LOWER_OR_GATE_INDEX, lowerOr);

		Gate rightmostPor = createGate(fault, FaultTreeNodeType.POR);
		nodesList.add(SAND_RIGHTMOST_POR_GATE_INDEX, rightmostPor);

		connect(fault, lowerAnd, leftmostPor);
		connect(fault, lowerOr, rightmostPor);
		connect(fault, lowerAnd, rightmostPor);
		connect(fault, rightmostPor, leftmostPor);

		return nodesList;
	}

	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @param inputs
	 *            Number of inputs into the gate;
	 * @return The new gate basic PAND gate;
	 */
	public List<FaultTreeNode> createBasicPAndGate(Fault fault, int inputs) {
		List<FaultTreeNode> nodesList = new ArrayList<>();

		Gate and = createBasicAndGate(fault, inputs);
		nodesList.add(PAND_AND_GATE_INDEX, and);

		for (int i = 0; i < inputs - 1; ++i) {
			Gate por = createGate(fault, FaultTreeNodeType.POR);
			nodesList.add(por);
			connect(fault, por, and);
		}

		return nodesList;
	}

	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @return The new gate basic inclusive POR gate;
	 */
	public List<FaultTreeNode> createBasicInclusivePorGate(Fault fault) {
		List<FaultTreeNode> nodesList = new ArrayList<>();

		Gate or = createBasicOrGate(fault);
		nodesList.add(INCLUSIVE_POR_UPPER_OR_GATE, or);

		Gate por = createGate(fault, FaultTreeNodeType.POR);
		nodesList.add(INCLUSIVE_POR_POR_GATE, por);

		Gate lowerOr = createBasicOrGate(fault);
		nodesList.add(INCLUSIVE_POR_LOWER_OR_GATE, lowerOr);

		List<FaultTreeNode> sand = createBasicSAndGate(fault, 2);
		nodesList.addAll(sand);

		connect(fault, por, or);
		connect(fault, sand.get(NODE_INDEX), or);
		connectToBasicSandGate(fault, lowerOr, sand);

		return nodesList;
	}

	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @param inputs
	 *            Number of children of the gate
	 * @return The new gate basic inclusive PAND gate;
	 */
	public List<FaultTreeNode> createBasicInclusivePandGate(Fault fault, int inputs) {
		List<FaultTreeNode> nodesList = new ArrayList<>();

		Gate or = createBasicOrGate(fault);
		nodesList.add(INCLUSIVE_PAND_UPPER_OR_GATE, or);

		List<FaultTreeNode> sand = createBasicSAndGate(fault, inputs);
		nodesList.add(INCLUSIVE_PAND_SAND_LEFTMOST_POR, sand.get(SAND_LEFTMOST_POR_GATE_INDEX));
		nodesList.add(INCLUSIVE_PAND_SAND_LOWER_AND_GATE_INDEX, sand.get(SAND_LOWER_AND_GATE_INDEX));
		nodesList.add(INCLUSIVE_PAND_SAND_LOWER_OR_GATE_INDEX, sand.get(SAND_LOWER_OR_GATE_INDEX));
		nodesList.add(INCLUSIVE_PAND_SAND_RIGHTMOST_POR_GATE_INDEX, sand.get(SAND_RIGHTMOST_POR_GATE_INDEX));

		List<FaultTreeNode> pand = createBasicPAndGate(fault, inputs);
		nodesList.addAll(pand);

		connect(fault, pand.get(PAND_AND_GATE_INDEX), or);
		connect(fault, sand.get(SAND_LEFTMOST_POR_GATE_INDEX), or);

		return nodesList;
	}

	/**
	 * Sets a fault propagation link from a source fault tree node to a target
	 * fault tree node
	 * 
	 * @param fault
	 *            The fault containing the fault propagation edge
	 * @param from
	 *            The source fault tree node
	 * @param to
	 *            The target fault tree node
	 * @return the created fault tree edge
	 */
	public FaultTreeEdge connect(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		FaultTreeEdge fte = new FaultTreeEdge(concept);
		fte.setFrom(from);
		fte.setTo(to);
		fault.getFaultTree().getPropagations().add(fte);
		return fte;
	}
	
	/**
	 * Connects dependencies
	 * 
	 * @param fault
	 *            The fault containing the fault propagation edge
	 * @param from
	 *            The source fault tree node
	 * @param to
	 *            The target fault tree node
	 * @return the created dep
	 */
	public FaultTreeEdge connectDep(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		FaultTreeEdge dep = new FaultTreeEdge(concept);
		dep.setFrom(from);
		dep.setTo(to);
		fault.getFaultTree().getDeps().add(dep);
		return dep;
	}
	
	/**
	 * Sets an observation link from an observation node to a fault tree node
	 * @param fault the fault into which this link will be inserted
	 * @param from the observer node
	 * @param to the observable node
	 * @return the created edge
	 */
	public FaultTreeEdge connectObserver(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		FaultTreeEdge obs = new FaultTreeEdge(concept);
		obs.setFrom(from);
		obs.setTo(to);
		fault.getFaultTree().getObservations().add(obs);
		return obs;
	}
	
	/**
	 * Sets a spare to a spare gate
	 * 
	 * @param fault
	 *            The fault containing the spare link
	 * @param from
	 *            The spare fault tree node
	 * @param to
	 *            The spare gate fault tree node
	 * @return the created fault tree edge
	 */
	public FaultTreeEdge connectSpare(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		FaultTreeEdge fte = new FaultTreeEdge(concept);
		fte.setFrom(from);
		fte.setTo(to);
		fault.getFaultTree().getSpares().add(fte);
		return fte;
	}

	/**
	 * @param fault
	 *            The fault associated with the connection;
	 * @param fromNode
	 *            The source node;
	 * @param toSandGate
	 *            The sand gate that the source node connects to;
	 */
	public void connectToBasicSandGate(Fault fault, FaultTreeNode fromNode, List<FaultTreeNode> toSandGate) {
		connectToBasicSandGate(fault, fromNode, toSandGate, 0);
	}

	/**
	 * @param fault
	 *            The fault associated with the connection;
	 * @param fromNode
	 *            The source node;
	 * @param toNodeList
	 *            The list of nodes to connect to;
	 * @param sandGateIndex
	 *            Index of the SAND gate to connect to;
	 */
	public void connectToBasicSandGate(Fault fault, FaultTreeNode fromNode, List<FaultTreeNode> toNodeList,
			int sandGateIndex) {
		FaultTreeNode andGate = toNodeList.get(sandGateIndex + SAND_LOWER_AND_GATE_INDEX);
		FaultTreeNode orGate = toNodeList.get(sandGateIndex + SAND_LOWER_OR_GATE_INDEX);

		connect(fault, fromNode, andGate);
		connect(fault, fromNode, orGate);
	}

	/**
	 * Creates a fault tree node for a basic fault event
	 * 
	 * @param name
	 *            The name of the basic fault event
	 * @param failureRate
	 *            The failure rate of the basic fault event
	 * @return The fault tree node for the basic fault event
	 */
	public Fault createBasicFault(String name, double failureRate) {
		return createBasicFault(name, failureRate, 0);
	}

	/**
	 * Creates a fault tree node for a basic fault event
	 * 
	 * @param name
	 *            The name of the basic fault event
	 * @param failureRateHot
	 *            The hot failure rate of the basic fault event
	 * @param failureRateCold
	 *            The cold failure rate of the basic fault event
	 * @return The fault tree node for the basic fault event
	 */
	public Fault createBasicFault(String name, double failureRateHot, double failureRateCold) {
		Fault fault = new Fault(concept);
		fault.setName(name);
		BasicEvent fm = new BasicEvent(concept);
		fm.setName("BasicEvent:" + name);
		fault.getBasicEvents().add(fm);
		fm.setHotFailureRate(failureRateHot);
		fm.setColdFailureRate(failureRateCold);

		return fault;
	}
	
	/**
	 * Copy a fault tree node no matter what type it is
	 * @param ftnode the original fault tree node
	 * @param fault the fault that is associated with the fault tree you want to add the node to
	 * @return the copy
	 */
	public FaultTreeNode copyFaultTreeNode(FaultTreeNode ftnode, Fault fault) {
		if (fault == null) {
			return copyFault(ftnode.getFault());
		} else {
			FaultTreeNodeType type = ftnode.getFaultTreeNodeType();
			switch (type) {
				case FAULT:
				case BASIC_EVENT:
					return copyFault(ftnode.getFault());
				default:
					FaultTreeNode gateCopy = createGate(fault, type);
					gateCopy.setName(ftnode.getName());
					return gateCopy;
			}
		}
	}
	
	
	/**
	 * Copy a fault tree node no matter what type it is
	 * @param fault the connection to the fault tree
	 * @param from node which we are connecting from
	 * @param to the node which we are connecting to
	 * @return the created edge
	 */
	public FaultTreeEdge createFaultTreeEdge(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		FaultTreeNodeType typeTo = to.getFaultTreeNodeType();
		switch (typeTo) {
			case FDEP:
			case RDEP:
			case PDEP:
				return connectDep(fault, from, to);
			case OBSERVER:
				return connectObserver(fault, from, to);
			default:
				return connect(fault, from, to);
		}
	}


	/**
	 * Copy the fault
	 * 
	 * @param fault
	 *            The fault to be copied
	 * 
	 * @return a copy of the fault tree
	 */
	public Fault copyFault(Fault fault) {
		Fault copy = new Fault(concept);
		copy.setName(fault.getName());
		copy.getTypeInstance().setUuid(fault.getTypeInstance().getUuid());

		for (BasicEvent fm : fault.getBasicEvents()) {
			BasicEvent newFm = new BasicEvent(concept);
			newFm.getHotFailureRateBean().setValueAsBaseUnit(fm.getHotFailureRateBean().getValueToBaseUnit());
			newFm.getColdFailureRateBean().setValueAsBaseUnit(fm.getColdFailureRateBean().getValueToBaseUnit());
			newFm.getRepairRateBean().setValueAsBaseUnit(fm.getRepairRateBean().getValueToBaseUnit());
			newFm.setName(fm.getName());
			newFm.getTypeInstance().setUuid(fm.getTypeInstance().getUuid());
			copy.getBasicEvents().add(newFm);
		}

		return copy;
	}

	/**
	 * Creates a fault tree node for a gate
	 * 
	 * @param fault
	 *            the fault containing the gate
	 * @param type
	 *            the type of the gate
	 * @return a fault tree node
	 */
	public Gate createGate(Fault fault, FaultTreeNodeType type) {
		Gate gate = createGate(type);
		fault.getFaultTree().getGates().add(gate);
		return gate;
	}
	
	/**
	 * Creates a gate
	 * 
	 * @param type
	 *            the type of the gate
	 * @return a fault tree node
	 */
	public Gate createGate(FaultTreeNodeType type) {
		switch (type) {
			case AND:
				return new AND(concept);
			case OR:
				return new OR(concept);
			case PAND:
				return new PAND(concept);
			case PAND_I:
				return new PANDI(concept);
			case POR:
				return new POR(concept);
			case POR_I:
				return new PORI(concept);
			case SAND:
				return new SAND(concept);
			case SPARE:
				return new SPARE(concept);
			case VOTE:
				return new VOTE(concept);
			case FDEP:
				return new FDEP(concept);
			case RDEP:
				return new RDEP(concept);
			case PDEP:
				return new PDEP(concept);
			case OBSERVER:
				return new OBSERVER(concept);
			default:
				throw new RuntimeException("Cannot create FaultTree Gate: Unknown type " + type); 
		}
	}
	
	
	/**
	 * Get ALL children of a node (events, spares, dependencies, observations, faults)
	 * @param node the node you want to find the children of
	 * @param ft the fault tree
	 * @return the list of children
	 */
	public List<FaultTreeNode> getAllChildren(FaultTreeNode node, FaultTree ft) {
		List<FaultTreeNode> children = new ArrayList<FaultTreeNode>();
		/* get children, spares, and dependencies and compile one big list */
			
		List<FaultTreeEdge> allEdges = new ArrayList<FaultTreeEdge>(ft.getPropagations());
		allEdges.addAll(ft.getDeps());
		allEdges.addAll(ft.getSpares());
		allEdges.addAll(ft.getObservations());
		
		for (FaultTreeEdge edge : allEdges) {
			if (edge.getTo().equals(node)) {
				children.add(edge.getFrom());
			}
		}
		
		if (node instanceof Fault) {
			children.addAll(((Fault) node).getBasicEvents());
		}
		
		return children;
	}
	
	

	/**
	 * Get the children of a node
	 * 
	 * @param node
	 *            the node want to know the children of
	 * @return a list of children
	 */
	public List<FaultTreeNode> getSpares(FaultTreeNode node) {
		return getSpares(node, Collections.singleton(node.getFault().getFaultTree()));
	}
	
	/**
	 * Get the children of a node
	 * 
	 * @param node
	 *            the node want to know the children of
	 * @param faultTrees the fault trees containing the edges
	 * @return a list of children
	 */
	public List<FaultTreeNode> getSpares(FaultTreeNode node, Set<FaultTree> faultTrees) {
		List<FaultTreeNode> spares = new ArrayList<FaultTreeNode>();

		for (FaultTree faultTree : faultTrees) {
			for (FaultTreeEdge edge : faultTree.getSpares()) {
				if (edge.getTo().equals(node)) {
					spares.add(edge.getFrom());
				}
			}
		}

		return spares;
	}

	/**
	 * @param newFault
	 *            the fault tree that potentially has syntactic sugar
	 * 
	 * @return true if the fault tree contains syntactic sugar and false
	 *         otherwise
	 */

	public boolean checkSyntacticSugar(FaultTreeNode newFault) {
		IBeanList<Gate> faultTree = newFault.getFault().getFaultTree().getGates();

		for (FaultTreeNode node : faultTree) {
			if (!(node.getFaultTreeNodeType().equals(FaultTreeNodeType.SPARE)
					|| node.getFaultTreeNodeType().equals(FaultTreeNodeType.VOTE)
					|| node.getFaultTreeNodeType().equals(FaultTreeNodeType.POR)
					|| node.getFaultTreeNodeType().equals(FaultTreeNodeType.FDEP))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Get the children of a node
	 * 
	 * @param node
	 *            the node want to know the children of
	 * @return a list of children
	 */
	public List<FaultTreeNode> getChildren(FaultTreeNode node) {
		return getChildren(node, Collections.singleton(node.getFault().getFaultTree()));
	}
	
	/**
	 * Get the children of a node
	 * 
	 * @param node
	 *            the node want to know the children of
	 * @param faultTrees the fault trees containing the edges
	 * @return a list of children
	 */
	public List<FaultTreeNode> getChildren(FaultTreeNode node, Set<FaultTree> faultTrees) {
		List<FaultTreeNode> children = new ArrayList<FaultTreeNode>();

		for (FaultTree faultTree : faultTrees) {
			for (FaultTreeEdge edge : faultTree.getPropagations()) {
				if (edge.getTo().equals(node)) {
					children.add(edge.getFrom());
				}
			}
		}
		return children;
	}
	
	/**
	 * Get the dependencies of a node
	 * 
	 * @param node
	 *            the node want to know the dependencies of
	 * @return a list of dependencies
	 */
	public List<FaultTreeNode> getDeps(FaultTreeNode node) {
		return getDeps(node, Collections.singleton(node.getFault().getFaultTree()));
	}
	
	/**
	 * Get the dependencies of a node
	 * 
	 * @param node
	 *            the node want to know the dependencies of
	 * @param faultTrees the fault trees containing the edges
	 * @return a list of dependencies
	 */
	public List<FaultTreeNode> getDeps(FaultTreeNode node, Set<FaultTree> faultTrees) {
		List<FaultTreeNode> deps = new ArrayList<FaultTreeNode>();

		for (FaultTree faultTree : faultTrees) {
			for (FaultTreeEdge edge : faultTree.getDeps()) {
				if (edge.getTo().equals(node)) {
					deps.add(edge.getFrom());
				}
			}
		}

		return deps;
	}
	
	/**
	 * Gets the nodes observed by this node
	 * 
	 * @param node
	 *            the node want to know the observables of
	 * @return a list of observables
	 */
	public List<FaultTreeNode> getObservables(FaultTreeNode node) {
		return getObservables(node, Collections.singleton(node.getFault().getFaultTree()));
	}
	
	/**
	 * Gets the nodes observed by this node
	 * 
	 * @param node
	 *            the node want to know the observables of
	 * @param faultTrees the fault trees containing the edges
	 * @return a list of observables
	 */
	public List<FaultTreeNode> getObservables(FaultTreeNode node, Set<FaultTree> faultTrees) {
		List<FaultTreeNode> observables = new ArrayList<FaultTreeNode>();

		for (FaultTree faultTree : faultTrees) {
			for (FaultTreeEdge edge : faultTree.getObservations()) {
				if (edge.getFrom().equals(node)) {
					observables.add(edge.getFrom());
				}
			}
		}
		return observables;
	}
	
	/**
	 * Gets all the nodes in the fault tree of the passed fault
	 * including all nodes in sub trees
	 * @param fault the root fault of a fault tree
	 * @return all nodes in the entire fault tree
	 */
	public List<FaultTreeNode> getAllNodes(Fault fault) {
		List<FaultTreeNode> allNodes = new ArrayList<>();
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.offer(fault);
		for (Gate gate : fault.getFaultTree().getGates()) {
			toProcess.offer(gate);
		}
		allNodes.addAll(toProcess);
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			List<FaultTreeNode> nodes = new ArrayList<>();
			nodes.addAll(getChildren(node));
			nodes.addAll(getSpares(node));
			
			if (node instanceof Fault) {
				nodes.addAll(((Fault) node).getBasicEvents());
			}
			
			nodes.forEach(child -> {
				if (!allNodes.contains(child)) {
					allNodes.add(child);
					toProcess.add(child);
				}
			});
		}
		
		return allNodes;
	}
	
	/**
	 * Gets all local nodes in the fault tree of the passed fault
	 * excluding all nodes in sub trees
	 * @param fault the root fault of a tree
	 * @return all local nodes in the fault tree
	 */
	public List<FaultTreeNode> getAllLocalNodes(Fault fault) {
		FaultTree faultTree = fault.getFaultTree();
		List<FaultTreeNode> allLocalSubNodes = new ArrayList<>();
		for (Gate gate : faultTree.getGates()) {
			allLocalSubNodes.add(gate);
		}
		allLocalSubNodes.addAll(fault.getBasicEvents());
		allLocalSubNodes.addAll(faultTree.getChildFaults());
		allLocalSubNodes.addAll(faultTree.getChildSpares());
		return allLocalSubNodes;
	}
	
	/**
	 * Gets all the propagations in the fault tree of the passed fault
	 * including all propagations in sub trees
	 * @param fault the root fault of a fault tree
	 * @return all propagations in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllPropagations(Fault fault) {
		List<FaultTreeNode> allNodes = getAllNodes(fault);
		List<FaultTreeEdge> allPropagations = new ArrayList<>();
		
		allNodes.forEach(node -> {
			if (node instanceof Fault) {
				Fault child = (Fault) node;
				allPropagations.addAll(child.getFaultTree().getPropagations());
			}
		});
		
		return allPropagations;
	}
	
	/**
	 * Gets all the spares in the fault tree of the passed fault
	 * including all spares in the sub trees
	 * @param fault the root fault of a fault tree
	 * @return all spares in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllSpares(Fault fault) {
		List<FaultTreeNode> allNodes = getAllNodes(fault);
		List<FaultTreeEdge> allSpares = new ArrayList<>();
		
		allNodes.forEach(node -> {
			if (node instanceof Fault) {
				Fault child = (Fault) node;
				allSpares.addAll(child.getFaultTree().getSpares());
			}
		});
		
		return allSpares;
	}
	
	/**
	 * get all spare NODES in the fault tree
	 * @param fault the fault containing the fault tree
	 * @return a list of all the spare nodes in the tree
	 */
	public List<FaultTreeNode> getAllSpareNodes(Fault fault) {
		List<FaultTreeNode> spareNodes = new ArrayList<FaultTreeNode>();
		this.getAllSpares(fault).stream().forEach(edge -> spareNodes.add(edge.getFrom()));
		return spareNodes;
	}
	
	/**
	 * Gets all the dependencies in the fault tree of the passed fault
	 * including all dependencies in the sub trees
	 * @param fault the root fault of a fault tree
	 * @return all dependencies in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllDeps(Fault fault) {
		List<FaultTreeNode> allNodes = getAllNodes(fault);
		List<FaultTreeEdge> allDependencies = new ArrayList<>();
		
		allNodes.forEach(node -> {
			if (node instanceof Fault) {
				Fault child = (Fault) node;
				allDependencies.addAll(child.getFaultTree().getDeps());
			}
		});
		
		return allDependencies;
	}
	
	/**
	 * Gets all the observations in the fault tree of the passed fault
	 * including all observations in the sub trees
	 * @param fault the root fault of a fault tree
	 * @return all observations in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllObservations(Fault fault) {
		List<FaultTreeNode> allNodes = getAllNodes(fault);
		List<FaultTreeEdge> allObservations = new ArrayList<>();
		
		allNodes.forEach(node -> {
			if (node instanceof Fault) {
				Fault child = (Fault) node;
				allObservations.addAll(child.getFaultTree().getObservations());
			}
		});
		
		return allObservations;
	}
	
	/**
	 * Gets all the edges in the fault tree of the passed fault
	 * including all the edges in the sub trees
	 * @param fault the root fault of a fault tree
	 * @return all edges in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllEdges(Fault fault) {
		List<FaultTreeEdge> allEdges = new ArrayList<>();
		allEdges.addAll(getAllPropagations(fault));
		allEdges.addAll(getAllSpares(fault));
		allEdges.addAll(getAllDeps(fault));
		allEdges.addAll(getAllObservations(fault));
		return allEdges;
	}

	/**
	 * Checks whether transition contains equivalent recovery actions with the given transition
	 * @param recoveryActions1 to check the recovery actions
	 * @param recoveryActions2 to check the recovery actions
	 * @return true if contains, false otherwise 
	 */
	public boolean hasEquivalentRecoveryActions(List<RecoveryAction> recoveryActions1, List<RecoveryAction> recoveryActions2) {
		return recoveryActions1.stream().map(RecoveryAction::getActionLabel).collect(Collectors.joining())
				.equals(recoveryActions2.stream().map(RecoveryAction::getActionLabel).collect(Collectors.joining()));
	}
	
	/**
	 * Computes a string in the dot format representing
	 * the complete fault tree under the passed fault
	 * @param faultTree the fault tree
	 * @return a string representation of the faultTree in the dot format
	 */
	public String toDot(FaultTree faultTree) {
		Fault root = faultTree.getRoot();
		List<FaultTreeNode> allNodes = getAllNodes(root);
		List<FaultTreeEdge> allEdges = getAllEdges(root);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("digraph " + root.getName() + " {\n");
		
		for (int i = 0; i < allNodes.size(); ++i) {
			FaultTreeNode ftn = allNodes.get(i);
			sb.append(i + " [label=\"" + ftn.getName() + "\"]\n");
		}
		
		for (int i = 0; i < allNodes.size(); ++i) {
			FaultTreeNode ftn = allNodes.get(i);
			if (ftn instanceof BasicEvent) {
				int indexFrom = allNodes.indexOf(ftn);
				int indexTo = allNodes.indexOf(ftn.getFault());
				
				sb.append(indexFrom + " -> " + indexTo + "\n");
			}
		}
		
		for (FaultTreeEdge propagation : allEdges) {
			FaultTreeNode ftnFrom = propagation.getFrom();
			FaultTreeNode ftnTo = propagation.getTo();
			
			int indexFrom = allNodes.indexOf(ftnFrom);
			int indexTo = allNodes.indexOf(ftnTo);
			
			sb.append(indexFrom + " -> " + indexTo + "\n");
		}
		
		sb.append("}");
		
		return sb.toString();
	}
}
