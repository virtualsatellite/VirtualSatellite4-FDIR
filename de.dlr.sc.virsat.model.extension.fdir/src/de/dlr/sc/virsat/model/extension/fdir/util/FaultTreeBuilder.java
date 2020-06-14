/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.Activator;
import de.dlr.sc.virsat.model.extension.fdir.model.AND;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.OR;
import de.dlr.sc.virsat.model.extension.fdir.model.PAND;
import de.dlr.sc.virsat.model.extension.fdir.model.PANDI;
import de.dlr.sc.virsat.model.extension.fdir.model.PDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.POR;
import de.dlr.sc.virsat.model.extension.fdir.model.PORI;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.SAND;
import de.dlr.sc.virsat.model.extension.fdir.model.SEQ;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;

/**
 * This class provides utlity functionality for modifying and creating fault trees.
 * @author muel_s8
 *
 */
public class FaultTreeBuilder {
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
	
	private FaultTreeHelper ftHelper = new FaultTreeHelper();
	private Concept concept;
	
	public FaultTreeBuilder(Concept concept) {
		this.concept = concept;
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
			case MONITOR:
				return new MONITOR(concept);
			case DELAY:
				return new DELAY(concept);
			case SEQ:
				return new SEQ(concept);
			default:
				throw new RuntimeException("Cannot create FaultTree Gate: Unknown type " + type); 
		}
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
	
	public FaultTreeEdge connect(Fault fault, EdgeType edgeType, FaultTreeNode from, FaultTreeNode to) {
		FaultTreeEdge fte = new FaultTreeEdge(concept);
		fte.setFrom(from);
		fte.setTo(to);
		ftHelper.getEdges(edgeType, fault.getFaultTree()).add(fte);
		return fte;
	}

	/**
	 * Connects the source node to the target node
	 * @param fault where the edge should be stored
	 * @param from the source node
	 * @param to the target node
	 * @return the created edge
	 */
	public FaultTreeEdge connect(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		return connect(fault, EdgeType.CHILD, from, to);
	}
	
	/**
	 * Connects the source node as a functional dependency trigger to the target node
	 * @param fault where the edge should be stored
	 * @param from the source node
	 * @param to the target node
	 * @return the created edge
	 */
	public FaultTreeEdge connectDep(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		return connect(fault, EdgeType.DEP, from, to);
	}
	
	/**
	 * Connects the source node as observation input the target node
	 * @param fault where the edge should be stored
	 * @param from the source node
	 * @param to the target node
	 * @return the created edge
	 */
	public FaultTreeEdge connectObserver(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		return connect(fault, EdgeType.MONITOR, from, to);
	}
	
	/**
	 * Connects the source node as a spare to the target node
	 * @param fault where the edge should be stored
	 * @param from the source node
	 * @param to the target node
	 * @return the created edge
	 */
	public FaultTreeEdge connectSpare(Fault fault, FaultTreeNode from, FaultTreeNode to) {
		return connect(fault, EdgeType.SPARE, from, to);
	}
	
	/**
	 * @param fault
	 *            The fault to associate with the gate;
	 * @param inputs
	 *            Number of inputs into the gate;
	 * @return The new gate basic AND gate;
	 */
	public VOTE createBasicAndGate(Fault fault, int inputs) {
		VOTE gate = (VOTE) createGate(fault, FaultTreeNodeType.VOTE);
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
		if (fault == null || ftnode instanceof Fault) {
			return copyFault(ftnode.getFault());
		} else {
			try {
				CategoryAssignment copyCa = EcoreUtil.copy(ftnode.getTypeInstance());
				FaultTreeNode copyBean = ftnode.getClass().newInstance();
				copyBean.setTypeInstance(copyCa);
				
				if (copyBean instanceof BasicEvent) {
					fault.getBasicEvents().add((BasicEvent) copyBean);
				} else {
					fault.getFaultTree().getGates().add((Gate) copyBean);
				}
				
				return copyBean;
			} catch (InstantiationException | IllegalAccessException e) {
				Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.getPluginId(), "Failed to copy a fault tree node!", e));
				return null;
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
		FaultTreeNodeType typeFrom = from.getFaultTreeNodeType();
		switch (typeFrom) {
			case FDEP:
			case RDEP:
			case PDEP:
				return connectDep(fault, from, to);
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

		for (BasicEvent be : fault.getBasicEvents()) {
			copyBasicEvent(be, copy);
		}

		return copy;
	}
	
	/**
	 * Copies a basic event
	 * @param basicEvent the basic event to copy
	 * @param fault the fault that will contain it
	 * @return the newly created basic event
	 */
	public BasicEvent copyBasicEvent(BasicEvent basicEvent, Fault fault) {
		CategoryAssignment copyCa = EcoreUtil.copy(basicEvent.getTypeInstance());
		BasicEvent copyBe = new BasicEvent(copyCa);
		fault.getBasicEvents().add(copyBe);
		return copyBe;
	}
	
	/**
	 * Remove an edge from the fault tree
	 * @param edge the edge
	 * @param ft the fault tree
	 * @return true if edge removed, false otherwise
	 */
	public boolean removeEdgeFromFaultTree(FaultTreeEdge edge, FaultTree ft) {
		return ft.getPropagations().remove(edge);
	}
	
	/**
	 * Gets the encapsulated fault tree helper
	 * @return the encapulsated fault tree helper
	 */
	public FaultTreeHelper getFtHelper() {
		return ftHelper;
	}

	/**
	 * Gets the encapsulated concept
	 * @return the encapsulated concept
	 */
	public Concept getConcept() {
		return concept;
	}
}