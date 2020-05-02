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
				
				for (List<State> refinedBlock : refinedBlocks) {
					blocks.add(refinedBlock);
					for (State state : refinedBlock) {
						mapStateToBlock.put(state, refinedBlock);
					}
				}
				
				// Get the predecessor blocks and check if we now have to refine them
				for (List<State> refinedBlock : refinedBlocks) {
					for (State state : refinedBlock) {
						List<Transition> incomingTransitions = raHolder.getMapStateToIncomingTransitions().get(state);
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
	 * Refines the given partitions until no more refinement is possible.
	 * A partition needs to be split into refined partitions if at least two states
	 * have a different transition profile.
	 * @param blocks the partitions to be refined-
	 */
	protected abstract List<List<State>> refineBlock(List<State> block);
}
