/*******************************************************************************
- * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
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

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Abstract base class for performing partition refinement based minimization
 * @author muel_s8
 *
 */
public abstract class APartitionRefinementMinimizer extends ARecoveryAutomatonMinimizer {
	
	protected RecoveryAutomatonHolder raHolder;
	protected Map<State, List<State>> mapStateToBlock;
	
	/**
	 * Checks if a state fits into a give partition.
	 * A state fits into a given partition if the actions of the transitions agree with all the transitions
	 * of all the states in the partition
	 * @param block the partition to check if the state belongs into
	 * @param state the state
	 * @return true iff the state belongs into the block
	 */
	protected abstract boolean belongsToBlock(List<State> block, State state);
	
	/**
	 * Refines the given partitions until no more refinement is possible.
	 * A partition needs to be split into refined partitions if at least two states
	 * have a different transition profile.
	 * @param blocks the partitions to be refined-
	 */
	protected abstract List<List<State>> refineBlock(List<State> block);
	
	/**
	 * Merge all the states in the given partitions
	 * @param blocks the partitions in which the states should be merged
	 */
	protected abstract void mergeBlocks(Set<List<State>> blocks);
	
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder, FaultTreeHolder ftHolder, SubMonitor subMonitor) {
		this.raHolder = raHolder;
		subMonitor = SubMonitor.convert(subMonitor);
		
		Set<List<State>> blocks = computeBlocks(subMonitor);
		mergeBlocks(blocks);
	}
	
	/**
	 * Performs the actual partition refinement algorithm
	 * to compute the equivalence classes on the recovery automaton.
	 * Each equivalence class is represented as a "block" list of states.
	 * @param subMonitor a monitor
	 * @return the computed block partitions
	 */
	protected Set<List<State>> computeBlocks(SubMonitor subMonitor) {
		Set<List<State>> blocks = createInitialBlocks();
		refineBlocks(blocks, subMonitor);
		return blocks;
	}
	
	/**
	 * Creates the initial partitions. Each partition contains the states
	 * that are potentially equivalent. States in different partitions cannot
	 * be equivalent.
	 * @return the initial partitions.
	 */
	protected Set<List<State>> createInitialBlocks() {
		Set<List<State>> blocks = new HashSet<>();
		mapStateToBlock = new HashMap<>();
		for (State state : raHolder.getRa().getStates()) {
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
	 * Refines the given partitions until no more refinement is possible.
	 * A partition needs to be split into refined partitions if at least two states
	 * have a different transition profile.
	 * @param blocks the partitions to be refined
	 * @param subMonitor a monitor
	 */
	protected void refineBlocks(Set<List<State>> blocks, SubMonitor subMonitor) {
		Queue<List<State>> blocksToProcess = new LinkedList<>(blocks); 
		while (!blocksToProcess.isEmpty()) {
			final int PROGRESS_COUNT = 100;
			subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
			
			List<State> block = blocksToProcess.poll();
		
			if (block.size() <= 1) {
				continue;
			}
			
			List<List<State>> refinedBlocks = refineBlock(block);
			if (refinedBlocks.size() > 1) {
				blocks.remove(block);
				
				applyRefinement(blocks, refinedBlocks);
				List<List<State>> outdatedBlocks = getOutDatedBlocks(refinedBlocks);
				for (List<State> outdatedBlock  : outdatedBlocks) {
					if (!blocksToProcess.contains(outdatedBlock)) {
						blocksToProcess.offer(outdatedBlock);
					}
				}
			}
		}
	}

	/**
	 * Gets the blocks that now have to be rechecked after the given refinement has been applied
	 * @param refinedBlocks a refinement of the partitions
	 * @return all outdated blocks that need to be rechecked for refinement
	 */
	private List<List<State>> getOutDatedBlocks(List<List<State>> refinedBlocks) {
		List<List<State>> outdatedBlocks = new ArrayList<>();
		for (List<State> refinedBlock : refinedBlocks) {
			for (State state : refinedBlock) {
				List<Transition> incomingTransitions = raHolder.getStateHolder(state).getIncomingTransitions();
				for (Transition transition : incomingTransitions) {
					List<State> fromBlock = mapStateToBlock.get(transition.getFrom());
					outdatedBlocks.add(fromBlock);
				}
			}
		}
		
		return outdatedBlocks;
	}

	/**
	 * Updates the the current partitions with the new refined blocks 
	 * @param blocks the current partitions
	 * @param refinedBlocks a list of refined blocks
	 */
	private void applyRefinement(Set<List<State>> blocks, List<List<State>> refinedBlocks) {
		for (List<State> refinedBlock : refinedBlocks) {
			blocks.add(refinedBlock);
			for (State state : refinedBlock) {
				mapStateToBlock.put(state, refinedBlock);
			}
		}
	}
}
