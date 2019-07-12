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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 *
 * Convert a dynamic/static fault tree with syntactic sugar into a tree without
 * syntactic sugar.
 */
public class DFT2BasicDFTConverter implements IDFT2DFTConverter {

	private Concept concept;
	private FaultTreeHelper ftHelper;
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
	public DFT2DFTConversionResult convert(FaultTreeNode root) {
		this.concept = root.getConcept();

		ftHelper = new FaultTreeHelper(concept);
		ftHolder = new FaultTreeHolder(root);
		mapNodes = new HashMap<>();

		for (FaultTreeNode node : ftHolder.getNodes()) {
			if (node instanceof Fault) {
				Fault fault = (Fault) node;
				List<FaultTreeNode> nodesList = new ArrayList<FaultTreeNode>();
				Fault copy = ftHelper.copyFault(fault);
				nodesList.add(FaultTreeHelper.NODE_INDEX, copy);
				mapNodes.put(node, nodesList);
				
				for (int i = 0; i < copy.getBasicEvents().size(); ++i) {
					BasicEvent oldBasicEvent = fault.getBasicEvents().get(i);
					BasicEvent newBasicEvent = copy.getBasicEvents().get(i);
					mapNodes.put(oldBasicEvent, Arrays.asList(newBasicEvent));
				}
			}
		}

		for (FaultTreeNode node : ftHolder.getNodes()) {
			if (node instanceof Gate) {
				convertNode(node);
			}
		}

		for (FaultTreeNode node : ftHolder.getNodes()) {
			List<FaultTreeNode> newNodeList = mapNodes.get(node);
			FaultTreeNode newNodeOutputNode = newNodeList.get(FaultTreeHelper.NODE_INDEX);
			Fault fault = newNodeOutputNode.getFault();

			List<FaultTreeNode> spares = ftHolder.getMapNodeToSpares().get(node);
			for (int i = 0; i < spares.size(); ++i) {
				FaultTreeNode spare = spares.get(i);
				List<FaultTreeNode> newChildNodeList = mapNodes.get(spare);
				FaultTreeNode newSpareOutputNode = newChildNodeList.get(FaultTreeHelper.NODE_INDEX);
				ftHelper.connectSpare(fault, newSpareOutputNode, newNodeOutputNode);
			}

			List<OBSERVER> observers = ftHolder.getMapNodeToObservers().get(node);
			for (int i = 0; i < observers.size(); ++i) {
				OBSERVER observer = observers.get(i);
				List<FaultTreeNode> newChildNodeList = mapNodes.get(observer);
				FaultTreeNode newSpareOutputNode = newChildNodeList.get(FaultTreeHelper.NODE_INDEX);
				ftHelper.connectObserver(fault, newSpareOutputNode, newNodeOutputNode);
			}
			
			List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);

			createEdges(fault, newNodeList, newNodeOutputNode, node.getFaultTreeNodeType(), children);
			
			if (node instanceof Fault) {
				for (BasicEvent be : node.getFault().getBasicEvents()) {
					List<FaultTreeNode> deps = ftHolder.getMapNodeToDEPTriggers().get(be);
					if (deps != null) {
						for (int i = 0; i < deps.size(); ++i) {
							FaultTreeNode dep = deps.get(i);
							List<FaultTreeNode> newChildNodeList = mapNodes.get(dep);
							FaultTreeNode newDepOutputNode = newChildNodeList.get(FaultTreeHelper.NODE_INDEX);
							ftHelper.connectDep(fault, newDepOutputNode, mapNodes.get(be).get(0));
						}
					}
				}
			}
		}

		Fault newFault = (Fault) mapNodes.get(root).get(0);
		
		Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerators = new HashMap<>();
		for (FaultTreeNode generator : mapNodes.keySet()) {
			List<FaultTreeNode> generatedNodes = mapNodes.get(generator);
			for (FaultTreeNode generated : generatedNodes) {
				mapGeneratedToGenerators.put(generated, generator);
			}
		} 
		
		DFT2DFTConversionResult conversionResult = new DFT2DFTConversionResult(newFault, mapGeneratedToGenerators);
		
		return conversionResult;
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
	 * @param type
	 *            The type of the node
	 * @param toNodes
	 *            The nodes to connect to
	 */
	private void createEdges(Fault fault, List<FaultTreeNode> newNodeList, FaultTreeNode newNodeOutputNode,
			FaultTreeNodeType type, List<FaultTreeNode> toNodes) {
		for (int i = 0; i < toNodes.size(); ++i) {
			FaultTreeNode toNode = toNodes.get(i);
			List<FaultTreeNode> newChildNodeList = mapNodes.get(toNode);
			FaultTreeNode newChildOutputNode = newChildNodeList.get(FaultTreeHelper.NODE_INDEX);

			if (type.equals(FaultTreeNodeType.SAND)) {
				ftHelper.connectToBasicSandGate(fault, newChildOutputNode, newNodeList);
			} else if (type.equals(FaultTreeNodeType.POR_I)) {
				if (i == 0) {
					FaultTreeNode por = newNodeList.get(FaultTreeHelper.INCLUSIVE_POR_POR_GATE);
					ftHelper.connect(fault, newChildOutputNode, por);
					ftHelper.connectToBasicSandGate(fault, newChildOutputNode, newNodeList,
							FaultTreeHelper.INCLUSIVE_POR_SAND_GATE);
				} else {
					FaultTreeNode por = newNodeList.get(FaultTreeHelper.INCLUSIVE_POR_POR_GATE);
					FaultTreeNode or = newNodeList.get(FaultTreeHelper.INCLUSIVE_POR_LOWER_OR_GATE);
					ftHelper.connect(fault, newChildOutputNode, por);
					ftHelper.connect(fault, newChildOutputNode, or);
				}
			} else if (type.equals(FaultTreeNodeType.PAND)) {
				if (i == 0) {
					FaultTreeNode por = newNodeList.get(FaultTreeHelper.PAND_POR_GATE_INDEX);
					ftHelper.connect(fault, newChildOutputNode, por);
				} else if (i == toNodes.size() - 1) {
					FaultTreeNode porLeft = newNodeList.get(FaultTreeHelper.PAND_POR_GATE_INDEX + i - 1);
					FaultTreeNode and = newNodeList.get(FaultTreeHelper.PAND_AND_GATE_INDEX);
					ftHelper.connect(fault, newChildOutputNode, porLeft);
					ftHelper.connect(fault, newChildOutputNode, and);
				} else {
					FaultTreeNode porLeft = newNodeList.get(FaultTreeHelper.PAND_POR_GATE_INDEX + i - 1);
					FaultTreeNode porRight = newNodeList.get(FaultTreeHelper.PAND_POR_GATE_INDEX + i);
					ftHelper.connect(fault, newChildOutputNode, porLeft);
					ftHelper.connect(fault, newChildOutputNode, porRight);
				}
			} else if (type.equals(FaultTreeNodeType.PAND_I)) {
				if (i == 0) {
					FaultTreeNode sandLowerOr = newNodeList
							.get(FaultTreeHelper.INCLUSIVE_PAND_SAND_LOWER_OR_GATE_INDEX);
					FaultTreeNode sandLowerAnd = newNodeList
							.get(FaultTreeHelper.INCLUSIVE_PAND_SAND_LOWER_AND_GATE_INDEX);
					FaultTreeNode pandPor = newNodeList.get(FaultTreeHelper.INCLUSIVE_PAND_EXCLUSIVE_PAND_POR_GATE);

					ftHelper.connect(fault, newChildOutputNode, pandPor);
					ftHelper.connect(fault, newChildOutputNode, sandLowerAnd);
					ftHelper.connect(fault, newChildOutputNode, sandLowerOr);

				} else {
					FaultTreeNode sandLowerOr = newNodeList
							.get(FaultTreeHelper.INCLUSIVE_PAND_SAND_LOWER_OR_GATE_INDEX);
					FaultTreeNode sandLowerAnd = newNodeList
							.get(FaultTreeHelper.INCLUSIVE_PAND_SAND_LOWER_AND_GATE_INDEX);
					FaultTreeNode pandPor = newNodeList
							.get(FaultTreeHelper.INCLUSIVE_PAND_EXCLUSIVE_PAND_POR_GATE + i - 1);
					FaultTreeNode pandAnd = newNodeList.get(FaultTreeHelper.INCLUSIVE_PAND_EXCLUSIVE_PAND_AND_GATE);

					ftHelper.connect(fault, newChildOutputNode, pandPor);
					ftHelper.connect(fault, newChildOutputNode, pandAnd);
					ftHelper.connect(fault, newChildOutputNode, sandLowerAnd);
					ftHelper.connect(fault, newChildOutputNode, sandLowerOr);
				}
			} else {
				ftHelper.connect(fault, newChildOutputNode, newNodeOutputNode);
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
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);

		FaultTreeNodeType type = node.getFaultTreeNodeType();
		List<FaultTreeNode> nodesList = new ArrayList<FaultTreeNode>();

		if (type.equals(FaultTreeNodeType.OR)) {
			Gate gate = ftHelper.createBasicOrGate(newFault);
			nodesList.add(FaultTreeHelper.NODE_INDEX, gate);
		} else if (type.equals(FaultTreeNodeType.AND)) {
			Gate gate = ftHelper.createBasicAndGate(newFault, children.size());
			nodesList.add(FaultTreeHelper.NODE_INDEX, gate);
		} else if (type.equals(FaultTreeNodeType.SAND)) {
			List<FaultTreeNode> sandGate = ftHelper.createBasicSAndGate(newFault, children.size());
			nodesList.addAll(sandGate);
		} else if (type.equals(FaultTreeNodeType.PAND)) {
			List<FaultTreeNode> pandGate = ftHelper.createBasicPAndGate(newFault, children.size());
			nodesList.addAll(pandGate);
		} else if (type.equals(FaultTreeNodeType.POR_I)) {
			List<FaultTreeNode> gates = ftHelper.createBasicInclusivePorGate(newFault);
			nodesList.addAll(gates);
		} else if (type.equals(FaultTreeNodeType.PAND_I)) {
			List<FaultTreeNode> gates = ftHelper.createBasicInclusivePandGate(newFault, children.size());
			nodesList.addAll(gates);
		} else {
			Gate oldGate = (Gate) node;
			Gate gate = ftHelper.createGate(newFault, type);
			if (gate instanceof VOTE) {
				((VOTE) gate).setVotingThreshold(((VOTE) oldGate).getVotingThreshold());
			} else if (gate instanceof OBSERVER) {
				((OBSERVER) gate).setObservationRate(((OBSERVER) oldGate).getObservationRate());
			} else if (gate instanceof RDEP) {
				((RDEP) gate).setRateChange(((RDEP) oldGate).getRateChange());
			} else if (gate instanceof DELAY) {
				((DELAY) gate).setTime(((DELAY) oldGate).getTime());
			}
			
			gate.setName(oldGate.getName());
			nodesList.add(FaultTreeHelper.NODE_INDEX, gate);
		}

		nodesList.get(FaultTreeHelper.NODE_INDEX).setName(node.getName());
		nodesList.get(FaultTreeHelper.NODE_INDEX).getATypeInstance().setUuid(node.getATypeInstance().getUuid());

		mapNodes.put(node, nodesList);
	}
}
