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

import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;

/**
 * Helper functions for work with static/dynamic fault trees;
 * @author lapi_an
 * 
 */
public class FaultTreeHelper {
	
	public static final EdgeType[] EDGE_TYPES = { EdgeType.CHILD, EdgeType.MONITOR, EdgeType.DEP, EdgeType.SPARE }; 
	
	/**
	 * Gets the edge list corresponding to the edge type from the fault tree
	 * @param edgeType an edge type
	 * @param faultTree the fault tree
	 * @return the edge list of the fault tree containing the edges of the desired type
	 */
	public List<FaultTreeEdge> getEdges(EdgeType edgeType, FaultTree faultTree) {
		switch (edgeType) {
			case CHILD:
			case PARENT:
				return faultTree.getPropagations();
			case MONITOR:
				return faultTree.getObservations();
			case DEP:
				return faultTree.getDeps();
			case SPARE:
				return faultTree.getSpares();
			default:
				return Collections.emptyList();
		}
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
	 * Gets all nodes that have an edge of the given type to the desired node
	 * @param edgeType the edge type
	 * @param node the node
	 * @param faultTrees the fault trees
	 * @return a list of nodes at the end of the desired edges
	 */
	public List<FaultTreeNode> getNodes(EdgeType edgeType, FaultTreeNode node, Set<FaultTree> faultTrees) {
		List<FaultTreeNode> nodes = new ArrayList<FaultTreeNode>();

		for (FaultTree faultTree : faultTrees) {
			for (FaultTreeEdge edge : getEdges(edgeType, faultTree)) {
				if (edge.getTo().equals(node)) {
					nodes.add(edge.getFrom());
				}
			}
		}
		
		return nodes;
	}
	
	/**
	 * Gets all nodes that have an edge of the given type to the desired node
	 * @param edgeType the edge type
	 * @param node the node
	 * @return a list of nodes at the end of the desired edges
	 */
	public List<FaultTreeNode> getNodes(EdgeType edgeType, FaultTreeNode node) {
		return getNodes(edgeType, node, Collections.singleton(node.getFault().getFaultTree()));
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
			nodes.addAll(getNodes(EdgeType.CHILD, node));
			nodes.addAll(getNodes(EdgeType.SPARE, node));
			nodes.addAll(getNodes(EdgeType.MONITOR, node));
			nodes.addAll(getNodes(EdgeType.DEP, node));
			
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
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		allLocalSubNodes.addAll(ftHolder.getChildFaults(fault));
		allLocalSubNodes.addAll(faultTree.getChildSpares());
		return allLocalSubNodes;
	}
	
	/**
	 * Gets all the edges in the fault tree of the passed fault
	 * including all the edges in the sub trees
	 * @param fault the root fault of a fault tree
	 * @return all edges in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllEdges(Fault fault, EdgeType... edgeTypes) {
		List<FaultTreeNode> allNodes = getAllNodes(fault);
		List<FaultTreeEdge> allEdges = new ArrayList<>();
		
		allNodes.forEach(node -> {
			if (node instanceof Fault) {
				Fault child = (Fault) node;
				for (EdgeType edgeType : edgeTypes) {
					allEdges.addAll(getEdges(edgeType, child.getFaultTree()));
				}
			}
		});
		
		return allEdges;
	}
	
	/**
	 * Gets all the edges in the fault tree of the passed fault
	 * including all the edges in the sub trees
	 * @param fault the root fault of a fault tree
	 * @return all edges in the entire fault tree
	 */
	public List<FaultTreeEdge> getAllEdges(Fault fault) {
		return getAllEdges(fault, EDGE_TYPES);
	}
	
	/**
	 * Gets the edges in the fault tree of the passed fault
	 * @param fault the root fault of a fault tree
	 * @return all edges in the fault
	 */
	public List<FaultTreeEdge> getEdges(Fault fault) {
		List<FaultTreeEdge> edges = new ArrayList<>();
		for (EdgeType edgeType : EDGE_TYPES) {
			edges.addAll(getEdges(edgeType, fault.getFaultTree()));
		}
		return edges;
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
