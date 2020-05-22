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
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Minimizes a given recovery automaton using the partition refinement algorithm.
 * @author muel_s8
 *
 */

public class PartitionRefinementMinimizer extends APartitionRefinementMinimizer {
	private RecoveryAutomaton ra;
	
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder) {
		super.minimize(raHolder);
		this.ra = raHolder.getRa();
		
		Set<List<State>> blocks = createInitialBlocks();
		refineBlocks(blocks);
		mergeBlocks(blocks);
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
				mapGuardProfileToBlock.put(raHolder.getMapStateToGuardProfile().get(state), block);
			}
			
			block.add(state);	
			blocks.add(block);
			mapStateToBlock.put(state, block);
		}
		
		return blocks;
	}
	
	@Override
	protected List<List<State>> refineBlock(List<State> block) {
		Map<Map<Set<FaultTreeNode>, List<State>>, List<State>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		List<List<State>> refinedBlocks = new ArrayList<>();
		
		for (State state : block) {
			Map<Set<FaultTreeNode>, List<State>> mapGuardsToBlock = createBlockReachabiliyMap(block, state);
			
			List<State> refinedBlock = getEquivalentBlock(mapBlockReachabilityMapToRefinedBlock, mapGuardsToBlock);
			if (refinedBlock == null) {
				refinedBlock = new ArrayList<State>();
				refinedBlocks.add(refinedBlock);
				mapBlockReachabilityMapToRefinedBlock.put(mapGuardsToBlock, refinedBlock);
			}
			
			refinedBlock.add(state);
		}
		
		return refinedBlocks;
	}

	/**
	 * Checks if in the current set of reachability maps there exists one, that is equivalent, 
	 * to the reachability map of a a given state
	 * @param mapBlockReachabilityMapToRefinedBlock the reachability maps computed until now
	 * @param state the current state 
	 * @param mapGuardsToBlock the reachability map of the state
	 * @return a refined block with an orthogoally equivalent reachability map or null if none exists
	 */
	private List<State> getEquivalentBlock(Map<Map<Set<FaultTreeNode>, List<State>>, List<State>> mapBlockReachabilityMapToRefinedBlock, Map<Set<FaultTreeNode>, List<State>> mapGuardsToBlock) {
		List<State> refinedBlock = mapBlockReachabilityMapToRefinedBlock.get(mapGuardsToBlock);
		return refinedBlock;
	}

	/**
	 * Creates a transition profile profile for a given state, mapping guards to the reached blocks
	 * @param block the current block
	 * @param state the current state
	 * @return a block level transition profile for the current state
	 */
	private Map<Set<FaultTreeNode>, List<State>> createBlockReachabiliyMap(List<State> block, State state) {
		Map<Set<FaultTreeNode>, List<State>> mapGuardsToBlock = new HashMap<>();
		List<Transition> outgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(state);
		for (Transition transition : outgoingTransitions) {
			List<State> toBlock = mapStateToBlock.get(raHolder.getMapTransitionToTo().get(transition));
			if (transition instanceof TimeoutTransition) {
				toBlock = getTimeoutBlock(block, state);
			}
			
			if (toBlock != block || !raHolder.getMapTransitionToActionLabels().get(transition).isEmpty()) {
				mapGuardsToBlock.put(raHolder.getMapTransitionToGuards().get(transition), toBlock);
			}
		}
		return mapGuardsToBlock;
	}

	/**
	 * Gets the block that will be entered if a all successive timeouts occurs starting with the given state in the block.
	 * @param block the current block
	 * @param state the current state
	 * @return the block that will be eventually reached when all successive timeouts have occurred
	 */
	private List<State> getTimeoutBlock(List<State> block, State state) {
		Set<State> visitedStates = new HashSet<>();
		State internalState = state;
		List<State> toBlock = block;
		
		while (toBlock == block) {
			List<Transition> internalTransitions = raHolder.getMapStateToOutgoingTransitions().get(internalState);
			boolean hasTimeoutTransition = false;
			for (Transition internalTransition : internalTransitions) {
				if (internalTransition instanceof TimeoutTransition) {
					internalState = raHolder.getMapTransitionToTo().get(internalTransition);
					toBlock = mapStateToBlock.get(raHolder.getMapTransitionToTo().get(internalTransition));
					hasTimeoutTransition = true;
					break;
				}
			}
			
			if (!hasTimeoutTransition || !visitedStates.add(internalState)) {
				break;
			}
		}
		return toBlock;
	}
	
	/**
	 * Merge all the states in the given partitions
	 * @param blocks the partitions in which the states should be merged
	 */
	private void mergeBlocks(Set<List<State>> blocks) {
		// Redirect all transitions between blocks so that they are between the block represenatatives
		for (List<State> block : blocks) {
			State state = block.get(0);
			
			// Get the target of the timout transitions and the total timeout time
			double blockTimeout = 0;
			State blockTimeoutTarget = null;
			for (State other : block) {
				List<Transition> outgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(other);
				for (Transition transition : outgoingTransitions) {
					if (transition instanceof TimeoutTransition) {
						TimeoutTransition timeoutTransition = (TimeoutTransition) transition;
						blockTimeout += timeoutTransition.getTime();
						
						State stateTo = raHolder.getMapTransitionToTo().get(transition);
						List<State> blockTarget = mapStateToBlock.get(stateTo);
						if (block != blockTarget && other != state) {
							blockTimeoutTarget = blockTarget.get(0);
						}
					}
				}
			}
			
			List<Transition> outgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(state);
			for (Transition transition : outgoingTransitions) {
				State stateTo = raHolder.getMapTransitionToTo().get(transition);
				State blockRepresentative = mapStateToBlock.get(stateTo).get(0);
				if (blockRepresentative != stateTo) {
					if (blockRepresentative != state || !raHolder.getMapTransitionToActionLabels().get(transition).isEmpty()) {
						transition.setTo(blockRepresentative);
						raHolder.getMapTransitionToTo().put(transition, blockRepresentative);
						
						List<Transition> otherIncomingTransitions = raHolder.getMapStateToIncomingTransitions().get(stateTo);
						otherIncomingTransitions.remove(transition);
						
						otherIncomingTransitions = raHolder.getMapStateToIncomingTransitions().get(blockRepresentative);
						otherIncomingTransitions.add(transition);
					}
					
					if (transition instanceof TimeoutTransition) {
						TimeoutTransition timeoutTransition = (TimeoutTransition) transition;
						timeoutTransition.setTime(blockTimeout);
						
						if (blockTimeoutTarget != null) {
							timeoutTransition.setTo(blockTimeoutTarget);
							
							raHolder.getMapTransitionToTo().put(timeoutTransition, blockTimeoutTarget);
							raHolder.getMapStateToIncomingTransitions().get(stateTo).remove(timeoutTransition);
							raHolder.getMapStateToIncomingTransitions().get(blockTimeoutTarget).add(timeoutTransition);
						}
					}
				}
			}
			
			if (block.contains(ra.getInitial())) {
				ra.setInitial(state);
			}
		}
		
		List<Transition> transitionsToRemove = new ArrayList<>();
		
		// Now remove all the non-representative states and all their transitions
		for (List<State> block : blocks) {
			State state = block.get(0);
			block.remove(state);
			
			for (State removedState : block) {
				List<Transition> outgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(removedState);
				List<Transition> incomingTransitions = raHolder.getMapStateToIncomingTransitions().get(removedState);
				
				for (Transition transition : outgoingTransitions) {
					List<Transition> otherIncomingTransitions = raHolder.getMapStateToIncomingTransitions().get(raHolder.getMapTransitionToTo().get(transition));
					if (otherIncomingTransitions != null) {
						otherIncomingTransitions.remove(transition);
					}
					
					transitionsToRemove.add(transition);
				}
				
				for (Transition transition : incomingTransitions) {
					List<Transition> otherOutgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(transition.getFrom());
					if (otherOutgoingTransitions != null) {
						otherOutgoingTransitions.remove(transition);
					}
					transitionsToRemove.add(transition);
				}
			}
			
			raHolder.getMapStateToOutgoingTransitions().keySet().removeAll(block);
			raHolder.getMapStateToIncomingTransitions().keySet().removeAll(block);
			raHolder.getMapStateToGuardProfile().keySet().removeAll(block);
			ra.getStates().removeAll(block);
		}
		
		raHolder.getMapTransitionToActionLabels().keySet().removeAll(transitionsToRemove);
		raHolder.getMapTransitionToTo().keySet().removeAll(transitionsToRemove);
		raHolder.getMapTransitionToGuards().keySet().removeAll(transitionsToRemove);
		raHolder.getTransitions().removeAll(transitionsToRemove);
		ra.getTransitions().removeAll(transitionsToRemove);
	}
	
	/**
	 * Checks if a state fits into one of the given partitions
	 * @param state the state to insert into the partition list
	 * @param mapGuardProfileToBlock mapping from guard profile to block
	 * @return the partition to which the state belongs or null if it belongs no existing partition
	 */
	private List<State> getBlock(State state, Map<Map<Set<FaultTreeNode>, String>, List<State>> mapGuardProfileToBlock) {
		return mapGuardProfileToBlock.get(raHolder.getMapStateToGuardProfile().get(state));
	}
}
