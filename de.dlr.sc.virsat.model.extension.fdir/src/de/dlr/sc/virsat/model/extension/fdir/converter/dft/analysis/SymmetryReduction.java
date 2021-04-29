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
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

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
	 * Checks if there exists a symmetric smaller node than the given node
	 * in the given state that has not yet failed
	 * @param node the node
	 * @param state the state
	 * @return true iff the exists a smaller unfailed node in the given state
	 */
	public boolean isSymmetricSmallerNodeUnfailed(FaultTreeNode node, DFTState state) {
		Set<FaultTreeNode> smallerNodes = getSmallerNodes(node);
		boolean haveSmallerNodesFailed = true;
		for (FaultTreeNode smallerNode : smallerNodes) {
			haveSmallerNodesFailed &= state.hasFaultTreeNodeFailed(smallerNode);
		}
		
		if (!haveSmallerNodesFailed && isSymmetryReductionApplicable(state, node)) {
			return true;
		}
		
		return false;
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
	 * Gets the symmetry rate multiplier for a given node in a given state
	 * @param node the node
	 * @param state the state
	 * @return the symmetry rate multiplier or -1 if there is a symmetric node covering this one
	 */
	public int getSymmetryMultiplier(FaultTreeNode node, DFTState state) {
		int multiplier = 1;
		int countBiggerSymmetricEvents = countBiggerSymmetricNodes(node, state);
		if (countBiggerSymmetricEvents == SKIP_EVENT) {
			return SKIP_EVENT;
		} else {
			multiplier += countBiggerSymmetricEvents;
		}
		
		return multiplier;
	}
	
	/**
	 * Computes the number of events symmetric to the passed one
	 * @param node the node
	 * @param state the current state
	 * @return -1 if there exists a smaller symmetric event, otherwise the number of bigger symmetric events
	 */
	public int countBiggerSymmetricNodes(FaultTreeNode node, DFTState state) {
		int symmetryMultiplier = 0;
		
		if (node != null) {
			Set<BasicEvent> failedBasicEvents = state.getFailedBasicEvents();
			
			Set<FaultTreeNode> smallerNodes = getSmallerNodes(node);
			boolean haveNecessaryEventsFailed = failedBasicEvents.containsAll(smallerNodes);
			
			if (!haveNecessaryEventsFailed && isSymmetryReductionApplicable(state, node)) {
				
				// At least one smaller event must also be active to skip this event
				boolean smallerEventIsActive = false;
				for (FaultTreeNode smallerNode : smallerNodes) {
					if (canFail(smallerNode, state)) {
						smallerEventIsActive = true;
						break;
					}
				}
				
				if (smallerEventIsActive) {
					return SKIP_EVENT;
				}
			}
			
			List<FaultTreeNode> symmetricNodes = biggerRelation.getOrDefault(node, Collections.emptyList());
			for (FaultTreeNode symmetricNode : symmetricNodes) {
				if (!failedBasicEvents.contains(symmetricNode) 
						&& canFail(symmetricNode, state)
						&& isSymmetryReductionApplicable(state, symmetricNode)) {
					symmetryMultiplier++;
				}
			}
		}
		
		return symmetryMultiplier;
	}
	
	/** 
	 * Internal method to check if a node can fail in the given state,
	 * given its activation status / dormancy.
	 * @param node the node to check
	 * @param state the state
	 * @return true if the node is either activated or a BE with a cold failure rate
	 */
	private boolean canFail(FaultTreeNode node, DFTState state) {
		if (node instanceof BasicEvent) {
			BasicEvent be = (BasicEvent) node;
			boolean canFailDormant = state.getFTHolder().getBasicEventHolder(be).getColdFailureRate() > 0;
			if (canFailDormant) {
				return true;
			}
		}
		
		return state.isNodeActive(node.getFault());
	}
	
	/**
	 * Creates the symmetry requirements for a state
	 * @param state the state
	 * @param predecessor the predecessor state
	 * @param basicEvent the basic event that has failed
	 */
	public void createSymmetryRequirements(DFTState state, DFTState predecessor, BasicEvent basicEvent, List<FaultTreeNode> changedNodes) {
		for (Entry<FaultTreeNode, Set<FaultTreeNode>> entry : predecessor.getMapParentToSymmetryRequirements().entrySet()) {
			state.getMapParentToSymmetryRequirements().put(entry.getKey(), new HashSet<>(entry.getValue()));
		}
		
		Set<FaultTreeNode> checkedNodes = new HashSet<>();
		Queue<FaultTreeNode> queue = new LinkedList<>();
		Set<FaultTreeNode> allParents = state.getFTHolder().getMapNodeToAllParents().get(basicEvent);
		queue.addAll(changedNodes);
		checkedNodes.addAll(changedNodes);
		
		while (!queue.isEmpty()) {
			FaultTreeNode node = queue.poll();
			List<FaultTreeNode> nextNodes = updateSymmetryRequirements(state, allParents, node);
			
			for (FaultTreeNode nextNode : nextNodes) {
				if (checkedNodes.add(nextNode)) {
					queue.add(nextNode);
				}
			}
		}
	}

	/**
	 * Updates the symmetry requirements for a single node in a state
	 * @param state the current state
	 * @param allParents all parents of the node
	 * @param node the node
	 * @return a new list of nodes that should have their symmetry requirements updated
	 */
	private List<FaultTreeNode> updateSymmetryRequirements(DFTState state, Set<FaultTreeNode> allParents, FaultTreeNode node) {
		List<FaultTreeNode> nextNodes = new ArrayList<>();
		List<FaultTreeNode> biggerNodes = biggerRelation.getOrDefault(node, Collections.emptyList());
		
		if (state.getMapSpareToClaimedSpares().values().contains(node)) {
			for (FaultTreeNode biggerNode : biggerNodes) {
				Set<FaultTreeNode> symmetryRequirements = state.getMapParentToSymmetryRequirements().computeIfAbsent(biggerNode, key -> new HashSet<>());
				symmetryRequirements.add(node);
			}
		}
		
		if (!biggerNodes.isEmpty()) {
			List<FaultTreeNode> parents = state.getFTHolder().getNodes(node, EdgeType.PARENT);
			for (FaultTreeNode parent : parents) {
				boolean continueToParent = updateSymmetryRequirements(state, parent, biggerNodes, allParents);
				
				if (continueToParent) {
					nextNodes.add(parent);
				}
			}
		}
		return nextNodes;
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
	public boolean isSymmetryReductionApplicable(DFTState state, FaultTreeNode node) {
		Map<FaultTreeNode, Set<FaultTreeNode>> mapParentToSymmetryRequirements = state.getMapParentToSymmetryRequirements();
		
		FaultTreeHolder ftHolder = state.getFTHolder();
		Set<FaultTreeNode> allParents = ftHolder.getMapNodeToAllParents().get(node);
		for (FaultTreeNode parent : allParents) {
			Set<FaultTreeNode> symmetryRequirements = mapParentToSymmetryRequirements.getOrDefault(parent, Collections.emptySet());
			for (FaultTreeNode symmetryRequirement : symmetryRequirements) {
				if (!state.hasFaultTreeNodeFailed(symmetryRequirement) || state.getMapSpareToClaimedSpares().values().contains(symmetryRequirement)) {
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
	
	/**
	 * Takes a list of transitions and creates a list of symmetric transitions.
	 * That is, each transition has its trigger event and the nodes mentioned in the recovery actions
	 * replaced by the respective symmetric nodes
	 * @param transitions the original transitions
	 * @return a list of symmetric transitions
	 */
	public void createSymmetricTransitions(RecoveryAutomaton ra) {
		RecoveryAutomatonHelper raHelper = new RecoveryAutomatonHelper(ra.getConcept());
		List<Transition> symmetricTransitions = new ArrayList<>();
		for (Transition transition : ra.getTransitions()) {
			List<List<FaultTreeNode>> symmetricSubstitutionsGuards = new ArrayList<>();
			if (transition instanceof FaultEventTransition) {
				FaultEventTransition feTransition = (FaultEventTransition) transition;
				symmetricSubstitutionsGuards = createSymmetricSubstitutions(feTransition.getGuards());
			}
			
			List<FaultTreeNode> actionNodes = new ArrayList<>();
			for (RecoveryAction action : transition.getRecoveryActions()) {
				actionNodes.addAll(action.getNodeParameters());
			}
			List<List<FaultTreeNode>> symmetricSubstitutionsActions = createSymmetricSubstitutions(actionNodes);
			
			int maxSize = Math.max(symmetricSubstitutionsActions.size(), symmetricSubstitutionsGuards.size());
			if (maxSize > 0) {
				for (int i = 0; i < maxSize; ++i) {
					Transition copy = raHelper.copyTransition(transition);
					symmetricTransitions.add(copy);
					
					if (!symmetricSubstitutionsGuards.isEmpty()) {
						List<FaultTreeNode> symmetricSubstitutionGuards = symmetricSubstitutionsGuards.get(i);
						FaultEventTransition feTransition = (FaultEventTransition) copy;
						feTransition.getGuards().clear();
						feTransition.getGuards().addAll(symmetricSubstitutionGuards);
					}
					
					if (!symmetricSubstitutionsActions.isEmpty()) {
						List<FaultTreeNode> symmetricSubstitutionActions = symmetricSubstitutionsActions.get(i);
						copy.applyNodeSubstiutionOnActions(symmetricSubstitutionActions);
					}
				}
			}
		}
		
		ra.getTransitions().addAll(symmetricTransitions);
	}
	
	/**
	 * Takes a list of nodes a creates several substitution lists according to the symmetry reduction.
	 * Each substitution list is structured as follows:
	 * For every node in the original list there is either a symmetric node (smaller or bigger)
	 * or if there is no symmetric node, then the original node itself 
	 * @param originalNodes a list of nodes
	 * @return a list of symmetric node substitutions
	 */
	private List<List<FaultTreeNode>> createSymmetricSubstitutions(List<FaultTreeNode> originalNodes) {
		List<List<FaultTreeNode>> allSymmetricNodes = new ArrayList<>();
		for (FaultTreeNode originalNode : originalNodes) {
			List<FaultTreeNode> symmetricNodes = new ArrayList<>();
			symmetricNodes.addAll(getSmallerNodes(originalNode));
			symmetricNodes.addAll(getBiggerNodes(originalNode));
			allSymmetricNodes.add(symmetricNodes);
		}
		
		List<List<FaultTreeNode>> symmetricSubstitutions = new ArrayList<>();
		OptionalInt maxSize = allSymmetricNodes.stream().mapToInt(List::size).max();
		if (maxSize.isPresent() && maxSize.getAsInt() > 0) {
			// max size is not present only if there are no symmetric bigger nodes
			
			List<FaultTreeNode> symmetricSubstitution = new ArrayList<>();
			for (int i = 0; i < maxSize.getAsInt(); ++i) {
				for (List<FaultTreeNode> biggerNodes : allSymmetricNodes) {
					// Either all nodes have the same number of symmetric nodes
					// or one has 0 which means itself has to be used
					if (biggerNodes.isEmpty()) {
						symmetricSubstitution.add(originalNodes.get(i));
					} else {
						symmetricSubstitution.add(biggerNodes.get(i));
					}
				}
			}
			symmetricSubstitutions.add(symmetricSubstitution);
		}
		
		return symmetricSubstitutions;
	}
}
