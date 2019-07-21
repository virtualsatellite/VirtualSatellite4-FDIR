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
	 * Computes a symmetry reduction between two FTs
	 * @param ftHolder1 the first ft
	 * @param ftHolder2 the second ft
	 * @return a symmetry reduction
	 */
	public Map<FaultTreeNode, Set<FaultTreeNode>> computeSymmetryReduction(FaultTreeHolder ftHolder1, FaultTreeHolder ftHolder2) {
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = new HashMap<>();	
		Map<Entry<FaultTreeNode, FaultTreeNode>, Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>>> mapPairToChildToChildPairs = new HashMap<>();
		Map<Set<Entry<FaultTreeNode, FaultTreeNode>>, Entry<FaultTreeNode, FaultTreeNode>> mapChildPairsToParentPair = new HashMap<>();
		Map<Entry<FaultTreeNode, FaultTreeNode>, Set<Entry<FaultTreeNode, FaultTreeNode>>> mapPairToPairSet = new HashMap<>();
		Queue<Set<Entry<FaultTreeNode, FaultTreeNode>>> toProcess = new LinkedList<>();
		Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> generated = new HashSet<>();
		
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
		// * the nodes have the same amount of child nodes
		// * for any child node, there exists at least one pair of child nodes in both trees that are isomomorphic
		while (!toProcess.isEmpty()) {
			Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = toProcess.poll();
			Set<Entry<FaultTreeNode, FaultTreeNode>> incorrectPairs = new HashSet<>();
			
			for (Entry<FaultTreeNode, FaultTreeNode> pair : candidatePairs) {
				FaultTreeNode node1 = pair.getKey();
				FaultTreeNode node2 = pair.getValue();
				if (!node1.hasSameProperties(node2)) {
					incorrectPairs.add(pair);
					continue;
				}
				
				Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> allSubCandidatePairs = new HashMap<>();
				Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> allChildCandidatePairs = createMapNodeToNodePairs(
						ftHolder1.getMapNodeToChildren().getOrDefault(node1, Collections.emptyList()), 
						ftHolder2.getMapNodeToChildren().getOrDefault(node2, Collections.emptyList()));
				if (allChildCandidatePairs == null) {
					incorrectPairs.add(pair);
					continue;
				}
				allSubCandidatePairs.putAll(allChildCandidatePairs);
				
				Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> allBeCandidatePairs = createMapNodeToNodePairs(
						ftHolder1.getMapFaultToBasicEvents().getOrDefault(node1, Collections.emptyList()), 
						ftHolder2.getMapFaultToBasicEvents().getOrDefault(node2, Collections.emptyList()));
				if (allBeCandidatePairs == null) {
					incorrectPairs.add(pair);
					continue;
				}
				allSubCandidatePairs.putAll(allBeCandidatePairs);
				
				Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> allSpareCandidatePairs = createMapNodeToNodePairs(
						ftHolder1.getMapNodeToSpares().getOrDefault(node1, Collections.emptyList()), 
						ftHolder2.getMapNodeToSpares().getOrDefault(node2, Collections.emptyList()));
				if (allSpareCandidatePairs == null) {
					incorrectPairs.add(pair);
					continue;
				}
				allSubCandidatePairs.putAll(allSpareCandidatePairs);
				
				Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> allDEPCandidatePairs = createMapNodeToNodePairs(
						ftHolder1.getMapNodeToDEPTriggers().getOrDefault(node1, Collections.emptyList()), 
						ftHolder2.getMapNodeToDEPTriggers().getOrDefault(node2, Collections.emptyList()));
				if (allDEPCandidatePairs == null) {
					incorrectPairs.add(pair);
					continue;
				}
				allSubCandidatePairs.putAll(allDEPCandidatePairs);
				
				Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> allParentCandidatePairs = createMapNodeToNodePairs(
						ftHolder1.getMapNodeToParents().getOrDefault(node1, Collections.emptyList()), 
						ftHolder2.getMapNodeToParents().getOrDefault(node2, Collections.emptyList()));
				if (allParentCandidatePairs == null) {
					incorrectPairs.add(pair);
					continue;
				}
				allSubCandidatePairs.putAll(allParentCandidatePairs);
				
				mapPairToChildToChildPairs.put(pair, allSubCandidatePairs);
				for (Set<Entry<FaultTreeNode, FaultTreeNode>> childCandidatePairs : allSubCandidatePairs.values()) {
					for (Entry<FaultTreeNode, FaultTreeNode> childPair : childCandidatePairs) {
						mapPairToPairSet.put(childPair, childCandidatePairs);
					}
					
					mapChildPairsToParentPair.put(childCandidatePairs, pair);
					if (!generated.contains(childCandidatePairs)) {
						toProcess.add(childCandidatePairs);
						generated.add(new HashSet<>(childCandidatePairs));
					}
				}
			} 
			
			candidatePairs.removeAll(incorrectPairs);
			
			// If the candidate set is empty, then we need to clean up the possible isomorphisms
			if (candidatePairs.isEmpty()) {
				// The parent pair of this pair is confirmed incorrect
				Entry<FaultTreeNode, FaultTreeNode> parentPair = mapChildPairsToParentPair.get(candidatePairs);
				Queue<Entry<FaultTreeNode, FaultTreeNode>> worklist = new LinkedList<>();
				worklist.add(parentPair);
				
				while (!worklist.isEmpty()) {
					parentPair = worklist.poll();
					
					if (parentPair == null) {
						continue;
					}
					
					mapPairToChildToChildPairs.remove(parentPair);
					Set<Entry<FaultTreeNode, FaultTreeNode>> parentPairSet = mapPairToPairSet.get(parentPair);
					parentPairSet.remove(parentPair);
					
					// Check further upwards in the tree if new parent pairs became incorrect
					if (parentPairSet.isEmpty()) {
						parentPair = mapChildPairsToParentPair.get(parentPairSet);
						worklist.add(parentPair);
					}
				}
			}
		}
		
		// Add the remaining valid isomorphisms into the symmetry relation and order them
		for (Entry<Entry<FaultTreeNode, FaultTreeNode>, Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>>> entry : mapPairToChildToChildPairs.entrySet()) {
			Entry<FaultTreeNode, FaultTreeNode> pair = entry.getKey();
			boolean isSmaller = symmetryRelation.getOrDefault(pair.getValue(), Collections.emptySet()).contains(pair.getKey());
			if (!isSmaller) {
				symmetryRelation.computeIfAbsent(pair.getKey(), v -> new HashSet<>()).add(pair.getValue());
			}
		}
		
		return symmetryRelation;
	}
	
	/**
	 * Helper method to create permutations between nodes of two given lists
	 * @param nodes1 the first node list
	 * @param nodes2 the second node list
	 * @return null if the node lists dont have the same size, otherwise all permutations between the lists
	 */
	private Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> createMapNodeToNodePairs(List<? extends FaultTreeNode> nodes1, List<? extends FaultTreeNode> nodes2) {
		if (nodes1.size() != nodes2.size()) {
			return null;
		}
		
		Map<FaultTreeNode, Set<Entry<FaultTreeNode, FaultTreeNode>>> mapNodeToNodePairs = new HashMap<>();
		for (FaultTreeNode child1 : nodes1) {
			Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = new HashSet<>();
			mapNodeToNodePairs.put(child1, candidatePairs);
			
			for (FaultTreeNode child2 : nodes2) {
				candidatePairs.add(new SimpleEntry<>(child1, child2));
			}
		}
		
		return mapNodeToNodePairs;
	}
}
