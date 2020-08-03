/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.RepairAction;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 *
 * Convert a dynamic/static fault tree with syntactic sugar into a tree without
 * syntactic sugar.
 */
public class DFT2BasicDFTConverter implements IDFT2DFTConverter {

	private Concept concept;
	private FaultTreeBuilder ftBuilder;
	private FaultTreeHolder ftHolder;
	private Map<FaultTreeNode, List<FaultTreeNode>> mapNodes;

	/**
	 * Convert a tree containing syntactic sugar into a tree without syntactic
	 * sugar.
	 * 
	 * @param root
	 *            the root of the fault tree that possibly contains syntactic
	 *            sugar
	 * 
	 * @return a tree that does not contain syntactic sugar
	 */
	public DFT2DFTConversionResult convert(FaultTreeHolder ftHolder) {
		FaultTreeNode root = ftHolder.getRoot();
		this.ftHolder = ftHolder;
		this.concept = root.getConcept();

		ftBuilder = new FaultTreeBuilder(concept);
		mapNodes = new HashMap<>();

		for (FaultTreeNode node : ftHolder.getNodes(FaultTreeNodeType.FAULT)) {
			Fault fault = (Fault) node;
			List<FaultTreeNode> nodesList = new ArrayList<FaultTreeNode>();
			Fault copy = ftBuilder.copyFault(fault);
			nodesList.add(FaultTreeBuilder.NODE_INDEX, copy);
			mapNodes.put(node, nodesList);
			
			for (int i = 0; i < copy.getBasicEvents().size(); ++i) {
				BasicEvent oldBasicEvent = fault.getBasicEvents().get(i);
				BasicEvent newBasicEvent = copy.getBasicEvents().get(i);
				mapNodes.put(oldBasicEvent, Arrays.asList(newBasicEvent));
			}
		}

		FaultTreeNode newRoot = mapNodes.get(root).get(0);
		
		for (FaultTreeNode node : ftHolder.getNodes()) {
			if (node instanceof Gate) {
				if (!mapNodes.containsKey(node.getFault())) {
					mapNodes.put(node.getFault(), Collections.singletonList(newRoot));
				}
				convertNode(node);
			}
		}

		for (FaultTreeNode node : ftHolder.getNodes()) {
			List<FaultTreeNode> newNodeList = mapNodes.get(node);
			FaultTreeNode newNodeOutputNode = newNodeList.get(FaultTreeBuilder.NODE_INDEX);
			Fault fault = newNodeOutputNode.getFault();

			createEdges(EdgeType.SPARE, node, newNodeOutputNode, fault);
			createEdges(EdgeType.MONITOR, node, newNodeOutputNode, fault);
			createChildEdges(fault, newNodeList, newNodeOutputNode, node);
			
			if (node instanceof Fault) {
				for (BasicEvent be : node.getFault().getBasicEvents()) {
					BasicEvent newBe = (BasicEvent) mapNodes.get(be).get(0);
					createEdges(EdgeType.DEP, be, newBe, fault);
					remapObservations(newBe);
				}
			}
		}

		Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerators = createTracing();
		DFT2DFTConversionResult conversionResult = new DFT2DFTConversionResult(newRoot, mapGeneratedToGenerators);

		return conversionResult;
	}

	/**
	 * Remaps the observation lists of repair actions of a basic event to the observations in the new tree
	 * @param newBe the basic event for remapping.
	 */
	private void remapObservations(BasicEvent newBe) {
		for (RepairAction repairAction : newBe.getRepairActions()) {
			for (int i = 0; i < repairAction.getObservations().size(); ++i) {
				FaultTreeNode observation = repairAction.getObservations().get(i);
				FaultTreeNode newObservation = mapNodes.get(observation).get(FaultTreeBuilder.NODE_INDEX);
				repairAction.getObservations().set(i, newObservation);
			}
		}
	}

	/**
	 * Creates a mapping between the generator fault tree nodes and the generated fault tree nodes
	 * for backwards tracing.
	 * @return a map to identify which fault tree node was responsible for generating a given fault tree node
	 */
	private Map<FaultTreeNode, FaultTreeNode> createTracing() {
		Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerators = new HashMap<>();
		for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : mapNodes.entrySet()) {
			FaultTreeNode generator = entry.getKey();
			List<FaultTreeNode> generatedNodes = entry.getValue();
			for (FaultTreeNode generated : generatedNodes) {
				mapGeneratedToGenerators.put(generated, generator);
			}
		}
		return mapGeneratedToGenerators;
	}

	/**
	 * Creates the edges of the specified node type in the given fault
	 * @param node the old node to which edges connect
	 * @param newNodeOutputNode the new node to which the edges will be connected
	 * @param fault the fault that will contain the edges
	 */
	private void createEdges(EdgeType edgeType, FaultTreeNode node, FaultTreeNode newNodeOutputNode, Fault fault) {
		List<FaultTreeNode> edges = ftHolder.getNodes(node, edgeType);
		for (int i = 0; i < edges.size(); ++i) {
			FaultTreeNode fromNode = edges.get(i);
			List<FaultTreeNode> newFromNodeList = mapNodes.get(fromNode);
			FaultTreeNode newFromOutputNode = newFromNodeList.get(FaultTreeBuilder.NODE_INDEX);
			if (edgeType.equals(EdgeType.MONITOR)) {
				ftBuilder.connect(fault, edgeType, newNodeOutputNode, newFromOutputNode);
			} else {
				ftBuilder.connect(fault, edgeType, newFromOutputNode, newNodeOutputNode);
			}
		}
	}

	/**
	 * Creates connections between components in the Fault Tree.
	 * 
	 * @param fault
	 *            The Fault Tree with components to be connected
	 * @param newNodeList
	 *            The list of nodes for connection
	 * @param newNodeOutputNode
	 *            The new output node
	 */
	private void createChildEdges(Fault fault, List<FaultTreeNode> newNodeList, FaultTreeNode newNodeOutputNode, FaultTreeNode node) {
		FaultTreeNodeType type = node.getFaultTreeNodeType();
		List<FaultTreeNode> toNodes = ftHolder.getNodes(node, EdgeType.CHILD);
		
		for (int i = 0; i < toNodes.size(); ++i) {
			FaultTreeNode toNode = toNodes.get(i);
			List<FaultTreeNode> newChildNodeList = mapNodes.get(toNode);
			FaultTreeNode newChildOutputNode = newChildNodeList.get(FaultTreeBuilder.NODE_INDEX);

			if (type.equals(FaultTreeNodeType.SAND)) {
				ftBuilder.connectToBasicSandGate(fault, newChildOutputNode, newNodeList);
			} else if (type.equals(FaultTreeNodeType.POR_I)) {
				if (i == 0) {
					FaultTreeNode por = newNodeList.get(FaultTreeBuilder.INCLUSIVE_POR_POR_GATE);
					ftBuilder.connect(fault, newChildOutputNode, por);
					ftBuilder.connectToBasicSandGate(fault, newChildOutputNode, newNodeList,
							FaultTreeBuilder.INCLUSIVE_POR_SAND_GATE);
				} else {
					FaultTreeNode por = newNodeList.get(FaultTreeBuilder.INCLUSIVE_POR_POR_GATE);
					FaultTreeNode or = newNodeList.get(FaultTreeBuilder.INCLUSIVE_POR_LOWER_OR_GATE);
					ftBuilder.connect(fault, newChildOutputNode, por);
					ftBuilder.connect(fault, newChildOutputNode, or);
				}
			} else if (type.equals(FaultTreeNodeType.PAND)) {
				if (i == 0) {
					FaultTreeNode por = newNodeList.get(FaultTreeBuilder.PAND_POR_GATE_INDEX);
					ftBuilder.connect(fault, newChildOutputNode, por);
				} else if (i == toNodes.size() - 1) {
					FaultTreeNode porLeft = newNodeList.get(FaultTreeBuilder.PAND_POR_GATE_INDEX + i - 1);
					FaultTreeNode and = newNodeList.get(FaultTreeBuilder.PAND_AND_GATE_INDEX);
					ftBuilder.connect(fault, newChildOutputNode, porLeft);
					ftBuilder.connect(fault, newChildOutputNode, and);
				} else {
					FaultTreeNode porLeft = newNodeList.get(FaultTreeBuilder.PAND_POR_GATE_INDEX + i - 1);
					FaultTreeNode porRight = newNodeList.get(FaultTreeBuilder.PAND_POR_GATE_INDEX + i);
					ftBuilder.connect(fault, newChildOutputNode, porLeft);
					ftBuilder.connect(fault, newChildOutputNode, porRight);
				}
			} else if (type.equals(FaultTreeNodeType.PAND_I)) {
				if (i == 0) {
					FaultTreeNode sandLowerOr = newNodeList
							.get(FaultTreeBuilder.INCLUSIVE_PAND_SAND_LOWER_OR_GATE_INDEX);
					FaultTreeNode sandLowerAnd = newNodeList
							.get(FaultTreeBuilder.INCLUSIVE_PAND_SAND_LOWER_AND_GATE_INDEX);
					FaultTreeNode pandPor = newNodeList.get(FaultTreeBuilder.INCLUSIVE_PAND_EXCLUSIVE_PAND_POR_GATE);

					ftBuilder.connect(fault, newChildOutputNode, pandPor);
					ftBuilder.connect(fault, newChildOutputNode, sandLowerAnd);
					ftBuilder.connect(fault, newChildOutputNode, sandLowerOr);

				} else {
					FaultTreeNode sandLowerOr = newNodeList
							.get(FaultTreeBuilder.INCLUSIVE_PAND_SAND_LOWER_OR_GATE_INDEX);
					FaultTreeNode sandLowerAnd = newNodeList
							.get(FaultTreeBuilder.INCLUSIVE_PAND_SAND_LOWER_AND_GATE_INDEX);
					FaultTreeNode pandPor = newNodeList
							.get(FaultTreeBuilder.INCLUSIVE_PAND_EXCLUSIVE_PAND_POR_GATE + i - 1);
					FaultTreeNode pandAnd = newNodeList.get(FaultTreeBuilder.INCLUSIVE_PAND_EXCLUSIVE_PAND_AND_GATE);

					ftBuilder.connect(fault, newChildOutputNode, pandPor);
					ftBuilder.connect(fault, newChildOutputNode, pandAnd);
					ftBuilder.connect(fault, newChildOutputNode, sandLowerAnd);
					ftBuilder.connect(fault, newChildOutputNode, sandLowerOr);
				}
			} else {
				ftBuilder.connect(fault, newChildOutputNode, newNodeOutputNode);
			}
		}
	}

	/**
	 * Convert the syntactic sugar into a node without a syntactic sugar
	 * 
	 * @param node
	 *            the node that needs to be converted
	 */
	private void convertNode(FaultTreeNode node) {
		Fault oldFault = node.getFault();
		Fault newFault = (Fault) mapNodes.get(oldFault).get(0);
		List<FaultTreeNode> children = ftHolder.getNodes(node, EdgeType.CHILD);

		FaultTreeNodeType type = node.getFaultTreeNodeType();
		List<FaultTreeNode> nodesList = new ArrayList<FaultTreeNode>();

		if (type.equals(FaultTreeNodeType.OR)) {
			Gate gate = ftBuilder.createBasicOrGate(newFault);
			nodesList.add(FaultTreeBuilder.NODE_INDEX, gate);
		} else if (type.equals(FaultTreeNodeType.AND)) {
			Gate gate = ftBuilder.createBasicAndGate(newFault, children.size());
			nodesList.add(FaultTreeBuilder.NODE_INDEX, gate);
		} else if (type.equals(FaultTreeNodeType.SAND)) {
			List<FaultTreeNode> sandGate = ftBuilder.createBasicSAndGate(newFault, children.size());
			nodesList.addAll(sandGate);
		} else if (type.equals(FaultTreeNodeType.PAND)) {
			List<FaultTreeNode> pandGate = ftBuilder.createBasicPAndGate(newFault, children.size());
			nodesList.addAll(pandGate);
		} else if (type.equals(FaultTreeNodeType.POR_I)) {
			List<FaultTreeNode> gates = ftBuilder.createBasicInclusivePorGate(newFault);
			nodesList.addAll(gates);
		} else if (type.equals(FaultTreeNodeType.PAND_I)) {
			List<FaultTreeNode> gates = ftBuilder.createBasicInclusivePandGate(newFault, children.size());
			nodesList.addAll(gates);
		} else {
			Gate oldGate = (Gate) node;
			Gate gate = ftBuilder.createGate(newFault, type);
			if (gate instanceof VOTE) {
				((VOTE) gate).setVotingThreshold(((VOTE) oldGate).getVotingThreshold());
			} else if (gate instanceof MONITOR) {
				((MONITOR) gate).setObservationRate(((MONITOR) oldGate).getObservationRateBean().getValueToBaseUnit());
			} else if (gate instanceof RDEP) {
				((RDEP) gate).setRateChange(((RDEP) oldGate).getRateChangeBean().getValueToBaseUnit());
			} else if (gate instanceof DELAY) {
				((DELAY) gate).setTime(((DELAY) oldGate).getTimeBean().getValueToBaseUnit());
			}
			
			gate.setName(oldGate.getName());
			nodesList.add(FaultTreeBuilder.NODE_INDEX, gate);
		}

		nodesList.get(FaultTreeBuilder.NODE_INDEX).setName(node.getName());
		nodesList.get(FaultTreeBuilder.NODE_INDEX).getATypeInstance().setUuid(node.getATypeInstance().getUuid());

		mapNodes.put(node, nodesList);
	}
}
