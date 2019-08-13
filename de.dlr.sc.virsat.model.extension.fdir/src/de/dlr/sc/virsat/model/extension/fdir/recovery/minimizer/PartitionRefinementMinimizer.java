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
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Minimizes a given recovery automaton using the partition refinement algorithm.
 * @author muel_s8
 *
 */

public class PartitionRefinementMinimizer extends ARecoveryAutomatonMinimizer {
	private RecoveryAutomaton ra;
	
	private Map<State, List<Transition>> mapStateToOutgoingTransitions;
	private Map<State, List<Transition>> mapStateToIncomingTransitions;
	private Map<Transition, Set<FaultTreeNode>> mapTransitionToGuards;
	private Map<State, List<State>> mapStateToBlock;
	private Map<Transition, String> mapTransitionToActionLabels;
	private Map<Transition, State> mapTransitionToTo;
	private Map<State, Map<Set<FaultTreeNode>, String>> mapStateToGuardProfile;
	
	@Override
	public void minimize(RecoveryAutomatonHolder raHolder) {
		ra = raHolder.getRa();
		
		statistics = new MinimizationStatistics();
		statistics.time = System.currentTimeMillis();
		statistics.removedStates = ra.getStates().size();
		statistics.removedTransitions = ra.getTransitions().size();
		
		mapStateToIncomingTransitions = raHolder.getMapStateToIncomingTransitions();
		mapStateToOutgoingTransitions = raHolder.getMapStateToOutgoingTransitions();
		mapTransitionToActionLabels = raHolder.getMapTransitionToActionLabels();
		mapTransitionToGuards = raHolder.getMapTransitionToGuards();
		mapTransitionToTo = raHolder.getMapTransitionToTo();
		mapStateToGuardProfile = raHolder.getMapStateToGuardProfile();

		Set<List<State>> blocks = createInitialBlocks();
		refineBlocks(blocks);
		mergeBlocks(blocks);
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		statistics.removedStates = statistics.removedStates - ra.getStates().size();
		statistics.removedTransitions = statistics.removedTransitions - ra.getTransitions().size();
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
		Map<Map<Set<FaultTreeNode>, String>, List<State>> mapGuardProfileToBlock = new HashMap<>();
		for (State state : ra.getStates()) {
			List<State> block = getBlock(state, mapGuardProfileToBlock);
			if (!blocks.remove(block)) {
				block = new ArrayList<>();
				mapGuardProfileToBlock.put(mapStateToGuardProfile.get(state), block);
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
			
			List<State> refinedBlock = mapBlockReachabilityMapToRefinedBlock.get(mapGuardsToBlock);
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
			
			List<Transition> outgoingTransitions = mapStateToOutgoingTransitions.get(state);
			for (Transition transition : outgoingTransitions) {
				State stateTo = mapTransitionToTo.get(transition);
				State blockRepresentative = mapStateToBlock.get(stateTo).get(0);
				if (blockRepresentative != stateTo) {
					if (blockRepresentative != state || !mapTransitionToActionLabels.get(transition).isEmpty()) {
						transition.setTo(blockRepresentative);
						mapTransitionToTo.put(transition, blockRepresentative);
						
						List<Transition> otherIncomingTransitions = mapStateToIncomingTransitions.get(stateTo);
						otherIncomingTransitions.remove(transition);
						
						otherIncomingTransitions = mapStateToIncomingTransitions.get(blockRepresentative);
						otherIncomingTransitions.add(transition);
					}
				}
			}
			
			if (block.contains(ra.getInitial())) {
				ra.setInitial(state);
			}
		}
		
		for (List<State> block : blocks) {
			State state = block.get(0);
			block.remove(state);
			
			List<Transition> transitionsToRemove = new ArrayList<>();
			
			for (State removedState : block) {
				List<Transition> outgoingTransitions = mapStateToOutgoingTransitions.get(removedState);
				List<Transition> incomingTransitions = mapStateToIncomingTransitions.get(removedState);
				
				for (Transition transition : outgoingTransitions) {
					List<Transition> otherIncomingTransitions = mapStateToIncomingTransitions.get(mapTransitionToTo.get(transition));
					if (otherIncomingTransitions != null) {
						otherIncomingTransitions.remove(transition);
					}
					transitionsToRemove.add(transition);
				}
				
				for (Transition transition : incomingTransitions) {
					List<Transition> otherOutgoingTransitions = mapStateToOutgoingTransitions.get(transition.getFrom());
					if (otherOutgoingTransitions != null) {
						otherOutgoingTransitions.remove(transition);
					}
					transitionsToRemove.add(transition);
				}
			}
			
			mapTransitionToActionLabels.keySet().removeAll(transitionsToRemove);
			mapTransitionToTo.keySet().removeAll(transitionsToRemove);
			mapTransitionToGuards.keySet().removeAll(transitionsToRemove);
			
			mapStateToOutgoingTransitions.keySet().removeAll(block);
			mapStateToIncomingTransitions.keySet().removeAll(block);
			mapStateToGuardProfile.keySet().removeAll(block);
			
			ra.getTransitions().removeAll(transitionsToRemove);
			ra.getStates().removeAll(block);
		}
	}
	
	/**
	 * Checks if a state fits into one of the given partitions
	 * @param state the state to insert into the partition list
	 * @param mapGuardProfileToBlock mapping from guard profile to block
	 * @return the partition to which the state belongs or null if it belongs no existing partition
	 */
	private List<State> getBlock(State state, Map<Map<Set<FaultTreeNode>, String>, List<State>> mapGuardProfileToBlock) {
		return mapGuardProfileToBlock.get(mapStateToGuardProfile.get(state));
	}
}
