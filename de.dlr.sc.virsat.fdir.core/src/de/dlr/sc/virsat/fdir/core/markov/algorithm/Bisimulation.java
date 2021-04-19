/*******************************************************************************
 * Copyright (c) 2021 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

/**
 * Class for performing Bisimulation
 * 
 * @author rama_vi
 *
 */
public class Bisimulation {

	protected MarkovAutomaton<MarkovState> ma;
	protected Map<MarkovState, Set<MarkovState>> mapStateToBlock;

	public Bisimulation(MarkovAutomaton<MarkovState> ma) {
		this.ma = ma;

	}

	/**
	 * Computes Equivalence Classes and quotient
	 */
	public void minimize() {
		computeEquivalenceClasses();
		computeQuotient();
	}

	/**
	 * Assigns the states to respective blocks when they have similar labels
	 * 
	 * @return blocks
	 */
	protected Set<Set<MarkovState>> createInitialBlocks() {
		Set<Set<MarkovState>> blocks = new HashSet<>();
		mapStateToBlock = new HashMap<>();
		for (MarkovState state : ma.getStates()) {
			Set<MarkovState> block = getBlock(state, blocks);
			if (!blocks.remove(block)) {
				block = new HashSet<>();
			}

			block.add(state);
			blocks.add(block);
			mapStateToBlock.put(state, block);
		}

		return blocks;
	}

	/**
	 * @param state
	 * @param blocks
	 * @return block The block to which a state belongs to
	 */
	private Set<MarkovState> getBlock(MarkovState state, Set<Set<MarkovState>> blocks) {
		for (Set<MarkovState> block : blocks) {
			if (belongsToBlock(block, state)) {
				return block;
			}
		}

		return null;
	}

	/**
	 * checks whether a state belongs to block or not
	 * 
	 * @param block
	 * @param state
	 * @return boolean outputs true or false
	 */
	protected boolean belongsToBlock(Set<MarkovState> block, MarkovState state) {
		List<Object> StateLabels = new ArrayList<Object>();
		List<Object> BlockLabels = new ArrayList<Object>();

		List<MarkovTransition<MarkovState>> StateTransitions = ma.getSuccTransitions(state);

		for (MarkovTransition<MarkovState> Statetransition : StateTransitions) {

			StateLabels.add(Statetransition.getEvent());
		}

		List<MarkovTransition<MarkovState>> BlockTransitions = ma.getSuccTransitions(block.iterator().next());

		for (MarkovTransition<MarkovState> Blocktransition : BlockTransitions) {

			BlockLabels.add(Blocktransition.getEvent());
		}

		boolean isequal = BlockLabels.equals(StateLabels);

		return isequal;

	}

	/**
	 * @param blocks takes as input the blocks created from the
	 *               createInitialBlocks() method and further refines them
	 */
	public void refineBlocks(Set<Set<MarkovState>> blocks) {

		Queue<Set<MarkovState>> blocksToProcess = new LinkedList<>(blocks);
		while (!blocksToProcess.isEmpty()) {

			Set<MarkovState> block = blocksToProcess.poll();

			if (block.size() <= 1) {
				continue;
			}

			Set<Set<MarkovState>> refinedBlocks = refineBlock(block);
			if (refinedBlocks.size() > 1) {
				blocks.remove(block);

				applyRefinement(blocks, refinedBlocks);
				Set<Set<MarkovState>> outdatedBlocks = getOutDatedBlocks(refinedBlocks);
				for (Set<MarkovState> outdatedBlock : outdatedBlocks) {
					if (!blocksToProcess.contains(outdatedBlock)) {
						blocksToProcess.offer(outdatedBlock);
					}
				}
			}

		}

	}

	/**
	 * @param block takes the individual block from queue
	 * @return blocks refines the block into blocks further if necessary
	 */
	public Set<Set<MarkovState>> refineBlock(Set<MarkovState> block) {
		Map<Map<Object, Set<MarkovState>>, Set<MarkovState>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		Set<Set<MarkovState>> refinedblocks = new HashSet<>();
		for (MarkovState state : block) {
			Map<Object, Set<MarkovState>> mapLabelsOFaStateToOutgoingBlock = createBlockReachabilityMapForAState(block,
					state);
			Set<MarkovState> refinedBlock = getEquivalentBlock(mapBlockReachabilityMapToRefinedBlock,
					mapLabelsOFaStateToOutgoingBlock);
			if (refinedBlock == null) {
				refinedBlock = new HashSet<MarkovState>();
				refinedblocks.add(refinedBlock);
				mapBlockReachabilityMapToRefinedBlock.put(mapLabelsOFaStateToOutgoingBlock, refinedBlock);
			}

			refinedBlock.add(state);
		}

		return refinedblocks;

	}

	/**
	 * @param mapBlockReachabilityMapToRefinedBlock
	 * @param mapLabelsToBlock
	 * @return refinedblock outputs a block if we already a
	 *         mapblockreachabilityofState
	 */
	private Set<MarkovState> getEquivalentBlock(
			Map<Map<Object, Set<MarkovState>>, Set<MarkovState>> mapBlockReachabilityMapToRefinedBlock,
			Map<Object, Set<MarkovState>> mapLabelsToBlock) {
		Set<MarkovState> refinedblock = mapBlockReachabilityMapToRefinedBlock.get(mapLabelsToBlock);

		return refinedblock;

	}

	/**
	 * We create a map for a state to which block it goes with the event
	 * 
	 * @param block
	 * @param state
	 * @return createBlockReachabilityMapForAState map between event(label of a
	 *         transition) and a block
	 */
	private Map<Object, Set<MarkovState>> createBlockReachabilityMapForAState(Set<MarkovState> block,
			MarkovState state) {
		Map<Object, Set<MarkovState>> mapLabelsOFaStateToOutgoingBlock = new HashMap<>();
		for (MarkovTransition<MarkovState> markovtransition : ma.getSuccTransitions(state)) {

			Set<MarkovState> toBlock = mapStateToBlock.get(markovtransition.getTo());

			if (toBlock != block) {
				mapLabelsOFaStateToOutgoingBlock.put(markovtransition.getEvent(), toBlock);
			}
		}
		return mapLabelsOFaStateToOutgoingBlock;
	}

	/**
	 * Updates the the current blocks with the new refined blocks
	 * 
	 * @param blocks        the current blocks
	 * @param refinedBlocks a list of refined blocks
	 */
	private void applyRefinement(Set<Set<MarkovState>> blocks, Set<Set<MarkovState>> refinedBlocks) {
		for (Set<MarkovState> refinedBlock : refinedBlocks) {
			blocks.add(refinedBlock);
			for (MarkovState state : refinedBlock) {
				mapStateToBlock.put(state, refinedBlock);
			}
		}

	}

	/**
	 * Gets the blocks that now have to be rechecked after the given refinement has
	 * been applied
	 * 
	 * @param refinedBlocks a refinement of the blocks
	 * @return all outdated blocks that need to be rechecked for refinement
	 */
	private Set<Set<MarkovState>> getOutDatedBlocks(Set<Set<MarkovState>> refinedBlocks) {
		Set<Set<MarkovState>> outdatedBlocks = new HashSet<>();

		for (Set<MarkovState> refinedBlock : refinedBlocks) {
			for (MarkovState state : refinedBlock) {

				for (MarkovTransition<MarkovState> markovtransition : ma.getPredTransitions(state)) {
					Set<MarkovState> fromBlock = mapStateToBlock.get(markovtransition.getFrom());
					outdatedBlocks.add(fromBlock);
				}
			}
		}

		return outdatedBlocks;

	}

	/**
	 * compute the equivalence classes on the Markov automaton. Each equivalence
	 * class is represented as a "block" list of states.
	 * 
	 * @return the computed block partitions
	 */

	public Set<Set<MarkovState>> computeEquivalenceClasses() {

		Set<Set<MarkovState>> blocks = createInitialBlocks();
		refineBlocks(blocks);

		return blocks;
	}

	public void computeQuotient() {

	}
}
