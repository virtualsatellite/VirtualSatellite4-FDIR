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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Abstract base class for performing partition refinement based minimization
 * @author muel_s8
 *
 */
public abstract class APartitionRefinementMinimizer extends ARecoveryAutomatonMinimizer {
	
	protected RecoveryAutomatonHolder raHolder;
	protected Map<State, List<State>> mapStateToBlock;
	
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder) {
		this.raHolder = raHolder;
	}
	
	/**
	 * Refines the given partitions until no more refinement is possible.
	 * A partition needs to be split into refined partitions if at least two states
	 * have a different transition profile.
	 * @param blocks the partitions to be refined
	 */
	protected void refineBlocks(Set<List<State>> blocks) {
		Queue<List<State>> blocksToProcess = new LinkedList<>(blocks); 
		while (!blocksToProcess.isEmpty()) {
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
				List<Transition> incomingTransitions = raHolder.getMapStateToIncomingTransitions().get(state);
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
	
	/**
	 * Refines the given partitions until no more refinement is possible.
	 * A partition needs to be split into refined partitions if at least two states
	 * have a different transition profile.
	 * @param blocks the partitions to be refined-
	 */
	protected abstract List<List<State>> refineBlock(List<State> block);
}
