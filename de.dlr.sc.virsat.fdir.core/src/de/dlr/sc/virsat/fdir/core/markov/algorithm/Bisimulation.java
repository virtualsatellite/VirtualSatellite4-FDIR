/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
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
	 * It either returns a null value or set of States (Block) to which a state
	 * belongs to
	 * 
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
		List<Object> stateLabels = new ArrayList<Object>();
		List<Object> blockLabels = new ArrayList<Object>();

		List<MarkovTransition<MarkovState>> stateTransitions = ma.getSuccTransitions(state);

		List<MarkovTransition<MarkovState>> blockTransitions = ma.getSuccTransitions(block.iterator().next());

		
		for (MarkovTransition<MarkovState> statetransition : stateTransitions) {
			
			if (!stateLabels.contains(statetransition.getEvent())) {
				
				stateLabels.add(statetransition.getEvent());
				
			}

			
		}

		
		for (MarkovTransition<MarkovState> blocktransition : blockTransitions) {
			
			if (!blockLabels.contains(blocktransition.getEvent())) {
				blockLabels.add(blocktransition.getEvent());
			}
			
		}

		boolean isequal = blockLabels.equals(stateLabels);

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
	 * The Set of States will be further refined based on their respective outgoing
	 * blocks
	 * 
	 * @param block takes the individual block from queue
	 * @return blocks refines the block into blocks further if necessary
	 */
	public Set<Set<MarkovState>> refineBlock(Set<MarkovState> block) {
		Map<Map<Object, Set<MarkovState>>, Set<MarkovState>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		Set<Set<MarkovState>> refinedBlocks = new HashSet<>();
		for (MarkovState state : block) {
			Map<Object, Set<MarkovState>> mapLabelsOfAStateToOutgoingBlock = createBlockReachabilityMapForAState(block,
					state);
			Set<MarkovState> refinedBlock = getEquivalentBlock(mapBlockReachabilityMapToRefinedBlock,
					mapLabelsOfAStateToOutgoingBlock);
			if (refinedBlock == null) {
				refinedBlock = new HashSet<MarkovState>();
				refinedBlocks.add(refinedBlock);
				mapBlockReachabilityMapToRefinedBlock.put(mapLabelsOfAStateToOutgoingBlock, refinedBlock);
			}

			refinedBlock.add(state);
		}

		return refinedBlocks;

	}

	/**
	 * A set of States are outputted if we have a map from a state to its outgoing
	 * blocks
	 * 
	 * @param mapBlockReachabilityMapToRefinedBlock
	 * @param mapLabelsToBlock
	 * @return refinedblock outputs a block if we already a
	 *         mapblockreachabilityofState
	 */
	private Set<MarkovState> getEquivalentBlock(
			Map<Map<Object, Set<MarkovState>>, Set<MarkovState>> mapBlockReachabilityMapToRefinedBlock,
			Map<Object, Set<MarkovState>> mapLabelsOfAStateToOutgoingBlock) {
		Set<MarkovState> refinedblock = mapBlockReachabilityMapToRefinedBlock.get(mapLabelsOfAStateToOutgoingBlock);

		return refinedblock;

	}

	/**
	 * We create a map for a state to which block it goes with its labelled
	 * transitions
	 * 
	 * @param block
	 * @param state
	 * @return createBlockReachabilityMapForAState map between event(label of a
	 *         transition) and a block
	 */
	private Map<Object, Set<MarkovState>> createBlockReachabilityMapForAState(Set<MarkovState> block,
			MarkovState state) {
		Map<Object, Set<MarkovState>> mapLabelsOfAStateToOutgoingBlock = new HashMap<>();
		for (MarkovTransition<MarkovState> markovtransition : ma.getSuccTransitions(state)) {

			Set<MarkovState> toBlock = mapStateToBlock.get(markovtransition.getTo());

			if (toBlock != block) {
				mapLabelsOfAStateToOutgoingBlock.put(markovtransition.getEvent(), toBlock);
			}
		}
		return mapLabelsOfAStateToOutgoingBlock;
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
	 * @param blocks
	 */
	public void mergeBlocks(Set<Set<MarkovState>> blocks) {



		for (Set<MarkovState> block : blocks) {
			MarkovState state = block.iterator().next();
			block.remove(state);
			for (MarkovState removedState : block) {
				ma.removeState(removedState);
			}
			block.add(state);
			List<MarkovTransition<MarkovState>> stateOutgoingTransitions = ma.getSuccTransitions(state);
			for (MarkovTransition<MarkovState> stateOutgoingTransition : stateOutgoingTransitions) {
				MarkovState toState = stateOutgoingTransition.getTo();
				MarkovState blockRepresentative = mapStateToBlock.get(toState).iterator().next();
				if (toState != blockRepresentative) {
					stateOutgoingTransition.setTo(blockRepresentative);
				}
			}

		}


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

	/**
	 * Computes Quotient of the equivalence Classes. Basically all the States in an equivalence Class
	 *  are contracted into Single State
	 */
	public void computeQuotient() {
		
		Set<Set<MarkovState>> equivalenceClasses = computeEquivalenceClasses();
		mergeBlocks(equivalenceClasses);
		
		
		
		
		
		

	}
}
