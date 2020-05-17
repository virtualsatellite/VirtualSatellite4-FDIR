/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
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
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Checks a Fault Tree for symmetry
 * @author muel_s8
 *
 */

public class DFTSymmetryChecker {
	
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
				
				Set<Entry<FaultTreeNode, FaultTreeNode>> parentPairs = mapChildPairToParentPairs.get(pair);
				
				// For order dependent nodes we must have actual equality
				Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> allSubCandidatePairs = new HashSet<>();
				if (node1.getFaultTreeNodeType().isOrderDependent()) {
					for (EdgeType edgeType : EdgeType.values()) {
						Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> allCandidatePairs = createMapNodeToPairsOrderDependent(
								parentPairs,
								ftHolder1.getNodes(node1, edgeType),
								ftHolder2.getNodes(node2, edgeType));
						if (allCandidatePairs == null) {
							incorrectPairs.add(pair);
							continue nextPair;
						}
						allSubCandidatePairs.addAll(allCandidatePairs);
					}
				} else {
					for (EdgeType edgeType : EdgeType.values()) {
						Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> allCandidatePairs = createMapNodeToNodePairs(
								parentPairs,
								ftHolder1.getNodes(node1, edgeType),
								ftHolder2.getNodes(node2, edgeType));
						if (allCandidatePairs == null) {
							incorrectPairs.add(pair);
							continue nextPair;
						}
						allSubCandidatePairs.addAll(allCandidatePairs);
					}
				}
				
				pairs.add(pair);
				
				for (Set<Entry<FaultTreeNode, FaultTreeNode>> childCandidatePairs : allSubCandidatePairs) {
					boolean hasBeenGenerated = hasBeenGenerated(generated, childCandidatePairs);
					
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
	 * Checks if some pair set has already been generated
	 * @param generated the set of all generated pair sets
	 * @param pairs a set of pair sets
	 * @return true iff the pairs have already been generated
	 */
	private boolean hasBeenGenerated(Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> generated, Collection<?> pairs) {
		for (Set<Entry<FaultTreeNode, FaultTreeNode>> generatedPair : generated) {
			if (generatedPair.containsAll(pairs)) {
				return true;	
			}
		}
		
		return false;
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
	 * @param parentPairs the parent pairs
	 * @param nodes1 the first node list
	 * @param nodes2 the second node list
	 * @return null if the node lists dont have the same size, otherwise all permutations between the lists
	 */
	private Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> createMapNodeToNodePairs(Set<Entry<FaultTreeNode, FaultTreeNode>> parentPairs, List<? extends FaultTreeNode> nodes1, List<? extends FaultTreeNode> nodes2) {
		if (nodes1.size() != nodes2.size()) {
			return null;
		}
		
		Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> mapNodeToNodePairs = new HashSet<>();
		for (FaultTreeNode child1 : nodes1) {
			boolean isParentPair = false;
			if (parentPairs != null) {
				for (Entry<FaultTreeNode, FaultTreeNode> parentPair : parentPairs) {
					if (parentPair.getKey().equals(child1)) {
						isParentPair = true;
						break;
					}
				}
			}
			
			if (!isParentPair) {
				Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = new HashSet<>();
				mapNodeToNodePairs.add(candidatePairs);
				
				for (FaultTreeNode child2 : nodes2) {
					isParentPair = false;
					if (parentPairs != null) {
						for (Entry<FaultTreeNode, FaultTreeNode> parentPair : parentPairs) {
							if (parentPair.getValue().equals(child2)) {
								isParentPair = true;
								break;
							}
						}
					}
					
					if (!isParentPair) {
						candidatePairs.add(new SimpleEntry<>(child1, child2));
					}
				}
			}
		}
		
		return mapNodeToNodePairs;
	}
	
	/**
	 * Helper method to create the ordered permutations between nodes of two given lists.
	 * Since the lists are ordered, only the permutations of matching indexes are created.
	 * @param parentPairs the parent pairs
	 * @param nodes1 the first node list
	 * @param nodes2 the second node list
	 * @return null if the node lists dont have the same size, otherwise the permuntation of the matching indexes
	 */
	private Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> createMapNodeToPairsOrderDependent(Set<Entry<FaultTreeNode, FaultTreeNode>> parentPairs, List<? extends FaultTreeNode> nodes1, List<? extends FaultTreeNode> nodes2) {
		if (nodes1.size() != nodes2.size()) {
			return null;
		}
		
		Set<Set<Entry<FaultTreeNode, FaultTreeNode>>> mapNodeToNodePairs = new HashSet<>();
		for (int i = 0; i < nodes1.size(); ++i) {
			FaultTreeNode child1 = nodes1.get(i);
			FaultTreeNode child2 = nodes2.get(i);
	
			boolean isParentPair = false;
			if (parentPairs != null) {
				for (Entry<FaultTreeNode, FaultTreeNode> parentPair : parentPairs) {
					if (parentPair.getKey().equals(child1) || parentPair.getValue().equals(child2)) {
						isParentPair = true;
						break;
					}
				}
			}
			
			Set<Entry<FaultTreeNode, FaultTreeNode>> candidatePairs = new HashSet<>();
			mapNodeToNodePairs.add(candidatePairs);
			
			if (!isParentPair && child1.equals(child2)) {
				candidatePairs.add(new SimpleEntry<>(child1, child2));
			}
		}
		
		return mapNodeToNodePairs;
	}
	
	/**
	 * Computes the inverse mapping for a given symmetry reduction
	 * @param symmetryReduction the symmetry reduction
	 * @return the inverted symmetry reduction
	 */
	public Map<FaultTreeNode, Set<FaultTreeNode>> invertSymmetryReduction(Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction) {
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryReductionInverted = new HashMap<>();
		for (FaultTreeNode node : symmetryReduction.keySet()) {
			symmetryReductionInverted.put(node, new HashSet<>());
		}
		
		for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : symmetryReduction.entrySet()) {
			for (FaultTreeNode node : entry.getValue()) {
				symmetryReductionInverted.get(node).add(entry.getKey());
			}
		}
		
		return symmetryReductionInverted;
	}
}
