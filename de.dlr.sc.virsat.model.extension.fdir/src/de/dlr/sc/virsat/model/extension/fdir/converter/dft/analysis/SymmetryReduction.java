/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis;

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

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class manages services for a symmetry reduction on DFTs.
 * A node is bigger (>=) than another node if it is ranked higher in the symmetry relation.
 * Likewise, a node is smaller (<=) if it is ranked lowed in the symmetry relation.
 * @author muel_s8
 *
 */

public class SymmetryReduction {
	
	public static final int SKIP_EVENT = -1;
	
	private Map<FaultTreeNode, List<FaultTreeNode>> biggerRelation = new HashMap<>();
	private Map<FaultTreeNode, Set<FaultTreeNode>> smallerRelation;
	
	/**
	 * Compute a symmetry reduction from a set of isomorphisms
	 * @param pairs ismorphic node pairs
	 */
	public SymmetryReduction(Set<Entry<FaultTreeNode, FaultTreeNode>> pairs) {
		biggerRelation = new HashMap<>();	
		for (Entry<FaultTreeNode, FaultTreeNode> pair : pairs) {
			boolean isBigger = !biggerRelation.getOrDefault(pair.getValue(), Collections.emptyList()).contains(pair.getKey());
			if (isBigger) {
				if (!pair.getValue().equals(pair.getKey())) {
					biggerRelation.computeIfAbsent(pair.getKey(), v -> new ArrayList<>()).add(pair.getValue());
				} else {
					biggerRelation.computeIfAbsent(pair.getKey(), v -> new ArrayList<>());
				}
			}
		}
	}
	
	/**
	 * Gets the bigger relation
	 * @return the bigger relation
	 */
	public Map<FaultTreeNode, List<FaultTreeNode>> getBiggerRelation() {
		return biggerRelation;
	}
	
	/**
	 * Gets all nodes that are bigger than the node
	 * @param node the node
	 * @return all bigger nodes
	 */
	public List<FaultTreeNode> getBiggerNodes(FaultTreeNode node) {
		return biggerRelation.get(node);
	}
	
	/**
	 * Gets all nodes that are smaller than this node
	 * @param node the node
	 * @return all smaller nodes
	 */
	public Set<FaultTreeNode> getSmallerNodes(FaultTreeNode node) {
		if (smallerRelation == null) {
			smallerRelation = invertSymmetryReduction();
		}
		
		return smallerRelation.getOrDefault(node, Collections.emptySet());
	}
	
	/**
	 * Gets the symmetry rate multiplier for a given event in a given state
	 * @param event the event
	 * @param state the state
	 * @return the symmetry rate multiplier or -1 if there is a symmetric event covering this one
	 */
	public int getSymmetryMultiplier(IDFTEvent event, DFTState state) {
		int multiplier = 1;
		int countBiggerSymmetricEvents = countBiggerSymmetricEvents(event, state);
		if (countBiggerSymmetricEvents == -1) {
			return SKIP_EVENT;
		} else {
			multiplier += countBiggerSymmetricEvents;
		}
		
		return multiplier;
	}
	
	/**
	 * Computes the number of events symmetric to the passed one
	 * @param event the event
	 * @param state the current state
	 * @return -1 if there exists a smaller symmetric event, otherwise the number of bigger symmetric events
	 */
	private int countBiggerSymmetricEvents(IDFTEvent event, DFTState state) {
		int symmetryMultiplier = 0;
		
		if (event instanceof FaultEvent) {
			Set<BasicEvent> failedBasicEvents = state.getFailedBasicEvents();
			
			boolean haveNecessaryEventsFailed = failedBasicEvents.containsAll(getSmallerNodes(event.getNode()));
			if (!haveNecessaryEventsFailed && isSymmetryReductionApplicable(state, event.getNode())) {
				return -1;
			}
			
			List<FaultTreeNode> symmetricNodes = biggerRelation.getOrDefault(event.getNode(), Collections.emptyList());
			for (FaultTreeNode node : symmetricNodes) {
				if (!failedBasicEvents.contains(node)) {
					if (isSymmetryReductionApplicable(state, node)) {
						symmetryMultiplier++;
					}
				}
			}
		}
		
		return symmetryMultiplier;
	}
	
	/**
	 * Creates the symmetry requirements for a state
	 * @param state the state
	 * @param predecessor the predecessor state
	 * @param basicEvent the basic event that has failed
	 */
	public void createSymmetryRequirements(DFTState state, DFTState predecessor, BasicEvent basicEvent) {
		state.getMapParentToSymmetryRequirements().putAll(predecessor.getMapParentToSymmetryRequirements());

		FaultTreeHolder ftHolder = state.getFTHolder();
		Set<FaultTreeNode> checkedNodes = new HashSet<>();
		Queue<FaultTreeNode> queue = new LinkedList<>();
		Set<FaultTreeNode> allParents = ftHolder.getMapNodeToAllParents().get(basicEvent);
		queue.add(basicEvent);
		checkedNodes.add(basicEvent);
		
		while (!queue.isEmpty()) {
			FaultTreeNode node = queue.poll();
			List<FaultTreeNode> biggerNodes = biggerRelation.getOrDefault(node, Collections.emptyList());
			if (!biggerNodes.isEmpty()) {
				List<FaultTreeNode> parents = ftHolder.getNodes(node, EdgeType.PARENT);
				for (FaultTreeNode parent : parents) {
					boolean continueToParent = updateSymmetryRequirements(state, parent, biggerNodes, allParents);
					
					if (continueToParent && checkedNodes.add(parent)) {
						queue.add(parent);
					}
				}
			}
		}
	}
	
	/**
	 * Updates the symmetry requirements of a parent node
	 * @param state the state
	 * @param parent the parent node
	 * @param biggerNodes the symmetrically bigger nodes according to the symmetry reduction (smallerNode <= biggerNode)
	 * @param allParents all parents of a basic event
	 * @return true iff the parent nodes parents should also update their summetry requirements, either
	 * because the node is failed or because new symmetry requirements were added to this node
	 */
	private boolean updateSymmetryRequirements(DFTState state, FaultTreeNode parent, List<FaultTreeNode> biggerNodes, Set<FaultTreeNode> allParents) {
		if (state.hasFaultTreeNodeFailed(parent)) {
			return true;
		}
		
		boolean continueToParent = false;
		Set<FaultTreeNode> processedBiggerParents = new HashSet<>();
		FaultTreeHolder ftHolder = state.getFTHolder();
		
		for (FaultTreeNode biggerNode : biggerNodes) {
			List<FaultTreeNode> biggerParents = ftHolder.getNodes(biggerNode, EdgeType.PARENT);
			for (FaultTreeNode biggerParent : biggerParents) {
				if (processedBiggerParents.add(biggerParent) && !allParents.contains(biggerParent)) {
					Set<FaultTreeNode> symmetryRequirements = state.getMapParentToSymmetryRequirements().computeIfAbsent(biggerParent, key -> new HashSet<>());
					continueToParent |= symmetryRequirements.add(biggerNode);
				}
			}
		}
		
		return continueToParent;
	}
	
	/**
	 * Checks if symmetry reduction is applicable for a given node
	 * @param state the current state
	 * @param node the node
	 * @return true iff symmetry reduction is applicable
	 */
	private boolean isSymmetryReductionApplicable(DFTState state, FaultTreeNode node) {
		Map<FaultTreeNode, Set<FaultTreeNode>> mapParentToSymmetryRequirements = state.getMapParentToSymmetryRequirements();
		
		FaultTreeHolder ftHolder = state.getFTHolder();
		Set<FaultTreeNode> allParents = ftHolder.getMapNodeToAllParents().get(node);
		for (FaultTreeNode parent : allParents) {
			Set<FaultTreeNode> symmetryRequirements = mapParentToSymmetryRequirements.getOrDefault(parent, Collections.emptySet());
			for (FaultTreeNode symmetryRequirement : symmetryRequirements) {
				if (!state.hasFaultTreeNodeFailed(symmetryRequirement)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Computes the inverse mapping for a given symmetry reduction
	 * @param biggerRelation the symmetry reduction
	 * @return the inverted symmetry reduction
	 */
	public Map<FaultTreeNode, Set<FaultTreeNode>> invertSymmetryReduction() {
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryReductionInverted = new HashMap<>();
		for (FaultTreeNode node : biggerRelation.keySet()) {
			symmetryReductionInverted.put(node, new HashSet<>());
		}
		
		for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : biggerRelation.entrySet()) {
			for (FaultTreeNode node : entry.getValue()) {
				symmetryReductionInverted.get(node).add(entry.getKey());
			}
		}
		
		return symmetryReductionInverted;
	}
}
