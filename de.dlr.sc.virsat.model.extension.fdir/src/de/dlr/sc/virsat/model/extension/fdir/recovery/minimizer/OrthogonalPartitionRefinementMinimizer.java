/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Minimizes a given recovery automaton using the partition refinement algorithm.
 * @author muel_s8
 *
 */

public class OrthogonalPartitionRefinementMinimizer extends ARecoveryAutomatonMinimizer {
	private RecoveryAutomatonHelper raHelper;
	private RecoveryAutomaton ra;
	private Map<State, List<Transition>> mapStateToOutgoingTransitions;
	private Map<State, List<Transition>> mapStateToIncomingTransitions;
	private Map<Transition, Set<FaultTreeNode>> mapTransitionToGuards;
	private Map<Transition, String> mapTransitionToActionLabels;
	private Map<State, Map<Set<FaultTreeNode>, String>> mapStateToGuardProfile;
	private Map<Transition, State> mapTransitionToTo;
	
	private Map<State, List<State>> mapStateToBlock;
	private Map<State, Set<FaultTreeNode>> mapStateToGuaranteedInputs;
	
	@Override
	public void minimize(RecoveryAutomatonHolder raHolder) {
		ra = raHolder.getRa();
		raHelper = raHolder.getRaHelper();
		
		mapStateToIncomingTransitions = raHolder.getMapStateToIncomingTransitions();
		mapStateToOutgoingTransitions = raHolder.getMapStateToOutgoingTransitions();
		mapTransitionToActionLabels = raHolder.getMapTransitionToActionLabels();
		mapTransitionToGuards = raHolder.getMapTransitionToGuards();
		mapTransitionToTo = raHolder.getMapTransitionToTo();
		mapStateToGuardProfile = raHolder.getMapStateToGuardProfile();
		mapStateToGuaranteedInputs = raHelper.computeInputs(ra, mapStateToIncomingTransitions, mapStateToOutgoingTransitions, mapTransitionToTo);
		
		removeUntakeableTransitions();
		
		Set<List<State>> blocks = createInitialBlocks();
		refineBlocks(blocks);
		mergeBlocks(blocks);
	}
	
	/**
	 * Removes all transitions that cannot be taken due to their inputs
	 * being guaranteed to have already occured.
	 */
	private void removeUntakeableTransitions() {
		List<Transition> toRemove = new ArrayList<>();
		for (Transition transition : ra.getTransitions()) {
			State state = transition.getFrom();
			Set<FaultTreeNode> guaranteedInputs = mapStateToGuaranteedInputs.get(state);
			if (mapTransitionToGuards.containsKey(transition) && guaranteedInputs.containsAll(mapTransitionToGuards.get(transition))) {
				toRemove.add(transition);
				mapStateToIncomingTransitions.get(transition.getTo()).remove(transition);
				mapStateToOutgoingTransitions.get(state).remove(transition);
			}
		}
		ra.getTransitions().removeAll(toRemove);
	}
	
	/**
	 * Creates the initial partitions. Each partition contains the states
	 * that are potentially equivalent. States in different partitions cannot
	 * be equivalent.
	 * @return the initial partitions.
	 */
	private Set<List<State>> createInitialBlocks() {
		Set<List<State>> blocks = new HashSet<>();
		mapStateToBlock = new HashMap<>();
		for (State state : ra.getStates()) {
			List<State> block = getBlock(state, blocks);
			if (!blocks.remove(block)) {
				block = new ArrayList<>();
			}
	
			block.add(state);	
			blocks.add(block);
			mapStateToBlock.put(state, block);
		}
		
		return blocks;
	}
	
	/**
	 * Refines the given partitions until no more refinement is possible.
	 * A partition needs to be split into refined partitions if at least two states
	 * have a different transition profile.
	 * @param blocks the partitions to be refined
	 */
	private void refineBlocks(Set<List<State>> blocks) {
		Queue<List<State>> blocksToProcess = new LinkedList<>(blocks); 
		while (!blocksToProcess.isEmpty()) {
			List<State> block = blocksToProcess.poll();
		
			if (block.size() <= 1) {
				continue;
			}
			
			List<List<State>> refinedBlocks = refineBlock(block);
			if (refinedBlocks.size() > 1) {
				blocks.remove(block);
				
				for (List<State> refinedBlock : refinedBlocks) {
					blocks.add(refinedBlock);
					for (State state : refinedBlock) {
						mapStateToBlock.put(state, refinedBlock);
					}
				}
				
				// Get the predecessor blocks and check if we now have to refine them
				for (List<State> refinedBlock : refinedBlocks) {
					for (State state : refinedBlock) {
						List<Transition> incomingTransitions = mapStateToIncomingTransitions.get(state);
						for (Transition transition : incomingTransitions) {
							List<State> fromBlock = mapStateToBlock.get(transition.getFrom());
							if (!blocksToProcess.contains(fromBlock)) {
								blocksToProcess.offer(fromBlock);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Performs one refinement step on a given partition.
	 * The contained states in the partition are refined into partitions with different transition profile.
	 * A transition profile is a mapping input -> partition
	 * @param block the partition to refine
	 * @return a list of refined partitions
	 */
	private List<List<State>> refineBlock(List<State> block) {
		Map<Map<Set<FaultTreeNode>, List<State>>, List<State>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		List<List<State>> refinedBlocks = new ArrayList<>();
		
		for (State state : block) {
			List<Transition> outgoingTransitions = mapStateToOutgoingTransitions.get(state);
			Map<Set<FaultTreeNode>, List<State>> mapGuardsToBlock = new HashMap<>();
			
			for (Transition transition : outgoingTransitions) {
				List<State> toBlock = mapStateToBlock.get(mapTransitionToTo.get(transition));
				if (toBlock != block || !mapTransitionToActionLabels.get(transition).isEmpty()) {
					mapGuardsToBlock.put(mapTransitionToGuards.get(transition), toBlock);
				}
			}
			
			List<State> refinedBlock = null;
			for (Map<Set<FaultTreeNode>, List<State>> mapGuardsToBlockOther : mapBlockReachabilityMapToRefinedBlock.keySet()) {
				boolean isEqual = true;
				Set<Set<FaultTreeNode>> allGuards = new HashSet<>(mapGuardsToBlock.keySet());
				allGuards.addAll(mapGuardsToBlockOther.keySet());
				
				for (Set<FaultTreeNode> guards : allGuards) {
					if (mapGuardsToBlock.get(guards) != mapGuardsToBlockOther.get(guards)) {
						State stateOther = mapBlockReachabilityMapToRefinedBlock.get(mapGuardsToBlockOther).get(0);
						if (!isOrthogonalWithRespectToGuards(state, stateOther, guards)) {
							isEqual = false;
							break;
						}
					}
				}
				
				if (isEqual) {
					refinedBlock = mapBlockReachabilityMapToRefinedBlock.get(mapGuardsToBlockOther);
					break;
				}
			}
			
			if (refinedBlock == null) {
				refinedBlock = new ArrayList<State>();
				refinedBlocks.add(refinedBlock);
			}
			
			refinedBlock.add(state);
			mapBlockReachabilityMapToRefinedBlock.put(mapGuardsToBlock, refinedBlock);
		}
		
		return refinedBlocks;
	}
	
	/**
	 * Merge all the states in the given partitions
	 * @param blocks the partitions in which the states should be merged
	 */
	private void mergeBlocks(Set<List<State>> blocks) {
		for (List<State> block : blocks) {
			State state = block.get(0);
			
			if (block.contains(ra.getInitial())) {
				ra.setInitial(state);
			}
			
			block.remove(state);
		
			List<Transition> outgoingTransitionsState = mapStateToOutgoingTransitions.get(state);
			List<Transition> incomingTransitionsState = mapStateToIncomingTransitions.get(state);
			
			List<Transition> outgoingTransitionsToAdd = new ArrayList<>();
			List<Transition> incomingTransitionsToAdd = new ArrayList<>();
			
			for (State removedState : block) {
				List<Transition> outgoingTransitions = mapStateToOutgoingTransitions.get(removedState);
				List<Transition> incomingTransitions = mapStateToIncomingTransitions.get(removedState);
				
				for (Transition transition : outgoingTransitions) {
					outgoingTransitionsToAdd.add(transition);
					transition.setFrom(state);
				}
				
				for (Transition transition : incomingTransitions) {
					incomingTransitionsToAdd.add(transition);
					transition.setTo(state);
					mapTransitionToTo.put(transition, state);
				}
			}
			
			outgoingTransitionsState.addAll(outgoingTransitionsToAdd);
			incomingTransitionsState.addAll(incomingTransitionsToAdd);
			
			mapStateToOutgoingTransitions.keySet().removeAll(block);
			mapStateToIncomingTransitions.keySet().removeAll(block);
			mapStateToGuardProfile.keySet().removeAll(block);
			
			ra.getStates().removeAll(block);
		}
	}
	
	/**
	 * Checks if a state fits into one of the given partitions
	 * @param state the state to insert into the partition list
	 * @param blocks a list of partitions
	 * @return the partition to which the state belongs or null if it belongs no none of the given partitions
	 */
	private List<State> getBlock(State state, Set<List<State>> blocks) {
		for (List<State> block : blocks) {
			if (belongsToBlock(block, state)) {
				return block;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if a state fits into a give partition.
	 * A state fits into a given partition if the actions of the transitions agree with all the transitions
	 * of all the states in the partition
	 * @param block the partition to check if the state belongs into
	 * @param state the state
	 * @return true iff the state belongs into the block
	 */
	private boolean belongsToBlock(List<State> block, State state) {
		for (State other : block) {
			if (!isActionEquivalent(other, state)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Determines if states are equivalent considering their transitions
	 * @param state0 first state
	 * @param state1 second state
	 * @return isEqual 
	 */
	private boolean isActionEquivalent(State state0, State state1) {
		List<Transition> outgoingTransitions0 = mapStateToOutgoingTransitions.get(state0);
		List<Transition> outgoingTransitions1 = mapStateToOutgoingTransitions.get(state1);
		
		for (Transition transition : outgoingTransitions0) {
			// Check for orthogonality
			Set<FaultTreeNode> guards0 = mapTransitionToGuards.get(transition);
			if (guards0 == null) {
				return false;
			}
			
			if (isOrthogonalWithRespectToGuards(state0, state1, guards0)) {
				continue;
			}
			
			if (!Objects.equals(mapStateToGuardProfile.get(state0).get(guards0), mapStateToGuardProfile.get(state1).get(guards0))) {
				return false;
			}
		}
		
		for (Transition transition : outgoingTransitions1) {
			// Check for orthogonality
			Set<FaultTreeNode> guards0 = mapTransitionToGuards.get(transition);
			
			if (guards0 == null) {
				return false;
			}
			
			if (isOrthogonalWithRespectToGuards(state0, state1, guards0)) {
				continue;
			}
			
			if (!Objects.equals(mapStateToGuardProfile.get(state0).get(guards0), mapStateToGuardProfile.get(state1).get(guards0))) {
				return false;
			}
		}
		
		return true; 
	}

	/**
	 * Checks if two states are orthogonal with respect to a set of guards.
	 * @param state0 the first state
	 * @param state1 the second state
	 * @param guards the guards
	 * @return true iff state0 and state1 are orthogonal with respect to the set of guards transition
	 */
	private boolean isOrthogonalWithRespectToGuards(State state0, State state1, Set<FaultTreeNode> guards) {
		Set<FaultTreeNode> guaranteedInputs0 = mapStateToGuaranteedInputs.get(state0); 
		if (guaranteedInputs0.containsAll(guards)) {
			return true;
		}
		
		Set<FaultTreeNode> guaranteedInputs1 = mapStateToGuaranteedInputs.get(state1); 
		if (guaranteedInputs1.containsAll(guards)) {
			return true;
		}
		
		return false;
	}
	
}
