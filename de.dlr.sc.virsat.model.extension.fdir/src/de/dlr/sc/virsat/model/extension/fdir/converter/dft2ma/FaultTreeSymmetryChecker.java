/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.AbstractMap.SimpleEntry;
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

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Checks a Fault Tree for symmetry
 * @author muel_s8
 *
 */

public class FaultTreeSymmetryChecker {
	
	/**
	 * Enum for different type of edges
	 * @author muel_s8
	 *
	 */
	private enum EdgeType {
		CHILD, BE, SPARE, DEP, PARENT
	}
	
	/**
	 * Computes a symmetry reduction between two FTs
	 * @param ftHolder1 the first ft
	 * @param ftHolder2 the second ft
	 * @return a symmetry reduction
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> computeSymmetryReduction(FaultTreeHolder ftHolder1, FaultTreeHolder ftHolder2) {
		Set<Entry<FaultTreeNode, FaultTreeNode>> pairs = new HashSet<>();
		Map<Entry<FaultTreeNode, FaultTreeNode>, Set<Entry<FaultTreeNode, FaultTreeNode>>> mapChildPairToParentPairs = new HashMap<>();
		Map<Entry<FaultTreeNode, FaultTreeNode>, Set<Entry<FaultTreeNode, FaultTreeNode>>> mapPairToPairSet = new HashMap<>();
		Queue<Set<Entry<FaultTreeNode, FaultTreeNode>>> toProcess = new LinkedList<>();
		Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> generated = new HashSet<>();
		
		Map<EdgeType, Entry<Map<FaultTreeNode, List<FaultTreeNode>>, Map<FaultTreeNode, List<FaultTreeNode>>>> mapEdgeTypeToLookUp = createMapEdgeTypeToLookUp(ftHolder1, ftHolder2);

		// For the trees to be symmetric, at least the root nodes have to be isomorphic
		Set<Entry<FaultTreeNode, FaultTreeNode>> initialCandidates = new HashSet<>();
		Entry<FaultTreeNode, FaultTreeNode> initialEntry = new SimpleEntry<>(ftHolder1.getRoot(), ftHolder2.getRoot());
		initialCandidates.add(initialEntry);
		mapPairToPairSet.put(initialEntry, initialCandidates);
		
		toProcess.add(initialCandidates);
		generated.add(new HashSet<>(initialCandidates));
		
		// Process all isomomorphism candidates as follows:
		// A pair is correct iff 
		// * the nodes have the same properties (node type, failure rate if it is a BE, etc)
		// * the nodes have the degree
		// * for any connected node, there exists at least one pair of connected nodes in both trees that are isomomorphic
		while (!toProcess.isEmpty()) {
			Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = toProcess.poll();
			Set<Entry<FaultTreeNode, FaultTreeNode>> incorrectPairs = new HashSet<>();
			
			nextPair: for (Entry<FaultTreeNode, FaultTreeNode> pair : candidatePairs) {
				FaultTreeNode node1 = pair.getKey();
				FaultTreeNode node2 = pair.getValue();
				if (!node1.hasSameProperties(node2)) {
					incorrectPairs.add(pair);
					continue;
				}
				
				// For order dependent nodes we must have actual equality
				Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> allSubCandidatePairs = new HashSet<>();
				if (node1.getFaultTreeNodeType().isOrderDependent()) {
					for (EdgeType edgeType : EdgeType.values()) {
						Entry<Map<FaultTreeNode, List<FaultTreeNode>>, Map<FaultTreeNode, List<FaultTreeNode>>> lookup = mapEdgeTypeToLookUp.get(edgeType);
						Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> allCandidatePairs = createMapNodeToPairsOrderDependent(
								lookup.getKey().getOrDefault(node1, Collections.emptyList()), 
								lookup.getValue().getOrDefault(node2, Collections.emptyList()));
						if (allCandidatePairs == null) {
							incorrectPairs.add(pair);
							continue nextPair;
						}
						allSubCandidatePairs.addAll(allCandidatePairs);
					}
				} else {
					for (EdgeType edgeType : EdgeType.values()) {
						Entry<Map<FaultTreeNode, List<FaultTreeNode>>, Map<FaultTreeNode, List<FaultTreeNode>>> lookup = mapEdgeTypeToLookUp.get(edgeType);
						Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> allCandidatePairs = createMapNodeToNodePairs(
								lookup.getKey().getOrDefault(node1, Collections.emptyList()), 
								lookup.getValue().getOrDefault(node2, Collections.emptyList()));
						if (allCandidatePairs == null) {
							incorrectPairs.add(pair);
							continue nextPair;
						}
						allSubCandidatePairs.addAll(allCandidatePairs);
					}
				}
				
				pairs.add(pair);
				for (Set<Entry<FaultTreeNode, FaultTreeNode>> childCandidatePairs : allSubCandidatePairs) {
					boolean hasBeenGenerated = false;
					for (Set<Entry<FaultTreeNode, FaultTreeNode>> generatedPair : generated) {
						if (generatedPair.containsAll(childCandidatePairs)) {
							hasBeenGenerated = true;	
						}
					}
					
					if (!hasBeenGenerated) {
						for (Entry<FaultTreeNode, FaultTreeNode> childPair : childCandidatePairs) {
							mapPairToPairSet.put(childPair, childCandidatePairs);
							mapChildPairToParentPairs.computeIfAbsent(childPair, v -> new HashSet<>()).add(pair);
						}
					
						toProcess.add(childCandidatePairs);
						generated.add(new HashSet<>(childCandidatePairs));
					}
				}
			} 
			
			candidatePairs.removeAll(incorrectPairs);
			
			// If the candidate set is empty, then we need to clean up the possible isomorphisms
			if (candidatePairs.isEmpty()) {
				// The parent pair of this pair is confirmed incorrect
				Set<Entry<FaultTreeNode, FaultTreeNode>> parentPairs = mapChildPairToParentPairs.getOrDefault(incorrectPairs.iterator().next(), Collections.emptySet());
				Queue<Entry<FaultTreeNode, FaultTreeNode>> worklist = new LinkedList<>();
				worklist.addAll(parentPairs);
				
				while (!worklist.isEmpty()) {
					Entry<FaultTreeNode, FaultTreeNode> parentPair = worklist.poll();
					
					Set<Entry<FaultTreeNode, FaultTreeNode>> parentPairSet = mapPairToPairSet.get(parentPair);
					if (!parentPairSet.isEmpty()) {
						parentPairSet.remove(parentPair);
						pairs.remove(parentPair);
						
						// Check further upwards in the tree if new parent pairs became incorrect
						if (parentPairSet.isEmpty()) {
							parentPairs = mapChildPairToParentPairs.getOrDefault(parentPair, Collections.emptySet());
							worklist.addAll(parentPairs);
						}
					}
				}
			}
		}
		
		return computeSymmetryReduction(pairs);
	}
	
	/**
	 * Batches the edges of the two fault tree holders according to their edge types
	 * @param ftHolder1 the holder of the first fault tree
	 * @param ftHolder2 the holder of the second fault tree
	 * @return the batched edges according to type
	 */
	private Map<EdgeType, Entry<Map<FaultTreeNode, List<FaultTreeNode>>, Map<FaultTreeNode, List<FaultTreeNode>>>> createMapEdgeTypeToLookUp(FaultTreeHolder ftHolder1, FaultTreeHolder ftHolder2) {
		Map<EdgeType, Entry<Map<FaultTreeNode, List<FaultTreeNode>>, Map<FaultTreeNode, List<FaultTreeNode>>>> mapEdgeTypeToLookUp = new HashMap<>();
		mapEdgeTypeToLookUp.put(EdgeType.CHILD, new SimpleEntry<>(ftHolder1.getMapNodeToChildren(), ftHolder2.getMapNodeToChildren()));
		mapEdgeTypeToLookUp.put(EdgeType.BE, new SimpleEntry<>(ftHolder1.getMapFaultToBasicEvents(), ftHolder2.getMapFaultToBasicEvents()));
		mapEdgeTypeToLookUp.put(EdgeType.SPARE, new SimpleEntry<>(ftHolder1.getMapNodeToSpares(), ftHolder2.getMapNodeToSpares()));
		mapEdgeTypeToLookUp.put(EdgeType.DEP, new SimpleEntry<>(ftHolder1.getMapNodeToDEPTriggers(), ftHolder2.getMapNodeToDEPTriggers()));
		mapEdgeTypeToLookUp.put(EdgeType.PARENT, new SimpleEntry<>(ftHolder1.getMapNodeToParents(), ftHolder2.getMapNodeToParents()));
		return mapEdgeTypeToLookUp;
	}
	
	/**
	 * Compute a symmetry reduction from a set of isomorphisms
	 * @param pairs ismorphic node pairs
	 * @return the symmetry reduction
	 */
	private Map<FaultTreeNode, List<FaultTreeNode>> computeSymmetryReduction(Set<Entry<FaultTreeNode, FaultTreeNode>> pairs) {
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = new HashMap<>();	
		for (Entry<FaultTreeNode, FaultTreeNode> pair : pairs) {
			boolean isBigger = !symmetryReduction.getOrDefault(pair.getValue(), Collections.emptyList()).contains(pair.getKey());
			if (isBigger) {
				if (!pair.getValue().equals(pair.getKey())) {
					symmetryReduction.computeIfAbsent(pair.getKey(), v -> new ArrayList<>()).add(pair.getValue());
				} else {
					symmetryReduction.computeIfAbsent(pair.getKey(), v -> new ArrayList<>());
				}
			}
		}
		
		return symmetryReduction;
	}
	
	/**
	 * Helper method to create permutations between nodes of two given lists
	 * @param nodes1 the first node list
	 * @param nodes2 the second node list
	 * @return null if the node lists dont have the same size, otherwise all permutations between the lists
	 */
	private Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> createMapNodeToNodePairs(List<? extends FaultTreeNode> nodes1, List<? extends FaultTreeNode> nodes2) {
		if (nodes1.size() != nodes2.size()) {
			return null;
		}
		
		Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> mapNodeToNodePairs = new HashSet<>();
		for (FaultTreeNode child1 : nodes1) {
			Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = new HashSet<>();
			mapNodeToNodePairs.add(candidatePairs);
			
			for (FaultTreeNode child2 : nodes2) {
				candidatePairs.add(new SimpleEntry<>(child1, child2));
			}
		}
		
		return mapNodeToNodePairs;
	}
	
	/**
	 * Helper method to create the ordered permutations between nodes of two given lists.
	 * Since the lists are ordered, only the permutations of matching indexes are created.
	 * @param nodes1 the first node list
	 * @param nodes2 the second node list
	 * @return null if the node lists dont have the same size, otherwise the permuntation of the matching indexes
	 */
	private Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> createMapNodeToPairsOrderDependent(List<? extends FaultTreeNode> nodes1, List<? extends FaultTreeNode> nodes2) {
		if (nodes1.size() != nodes2.size()) {
			return null;
		}
		
		Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> mapNodeToNodePairs = new HashSet<>();
		for (int i = 0; i < nodes1.size(); ++i) {
			FaultTreeNode child1 = nodes1.get(i);
			FaultTreeNode child2 = nodes2.get(i);
			Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = new HashSet<>();
			mapNodeToNodePairs.add(candidatePairs);
	
			if (child1.equals(child2)) {
				candidatePairs.add(new SimpleEntry<>(child1, child2));
			}
		}
		
		return mapNodeToNodePairs;
	}
}
