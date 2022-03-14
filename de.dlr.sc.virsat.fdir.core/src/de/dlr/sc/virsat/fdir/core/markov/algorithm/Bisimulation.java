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


import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;




/**
 * Class for performing Bisimulation taking a state of 
 * base type MarkovState 
 * @author rama_vi
 *
 * @param <S>
 */

public class Bisimulation<S extends MarkovState> {
	
	protected MarkovAutomaton<S> ma;
	protected Map<S, Set<S>> mapStateToBlock;

	/**
	 * Constructor class which takes Markov Automaton as a parameter
	 * 
	 * @param ma
	 */
	public Bisimulation(MarkovAutomaton<S> ma) {
		this.ma = ma;
	}

	/**
	 * Computes Equivalence Classes and quotient
	 */
	public void minimize(SubMonitor subMonitor) {
		subMonitor = SubMonitor.convert(subMonitor);
		computeEquivalenceClasses(subMonitor);
		computeQuotient(subMonitor);
	}

	/**
	 * Assigns the states to respective blocks when they have similar labels
	 * 
	 * @return blocks
	 */
	protected Set<Set<S>> createInitialBlocks(SubMonitor subMonitor) {
		Set<Set<S>> blocks = new HashSet<>();
		mapStateToBlock = new HashMap<>();
		for (S state : ma.getStates()) {
			final int PROGRESS_COUNT = 100;
			subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
			Set<S> block = getBlock(state, blocks);
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
	private Set<S> getBlock(S state, Set<Set<S>> blocks) {
		
		for (Set<S> block : blocks) {
			
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
	protected boolean belongsToBlock(Set<S> block, S state) {
		List<Object> stateLabels = new ArrayList<Object>();		
		List<Object> stateRates = new ArrayList<Object>();
		List<Object> blockLabels = new ArrayList<Object>();
		List<Object> blockRates = new ArrayList<Object>();
		
		boolean isequal;
        stateRates = ma.getSuccRates(state);
	    stateLabels = ma.getSuccEvents(state);
        blockRates = ma.getSuccRates(block.iterator().next());
		blockLabels = ma.getSuccEvents(block.iterator().next());
		if (stateRates.isEmpty() && blockRates.isEmpty()) {
			isequal = blockLabels.equals(stateLabels);
		}
		isequal = blockLabels.equals(stateLabels)  && stateRates.equals(blockRates)   && state.getFailLabels().equals(block.iterator().next().getFailLabels());
		return isequal;
	}

	/**
	 * @param blocks takes as input the blocks created from the
	 * createInitialBlocks() method and further refines them
	 */
	public void refineBlocks(Set<Set<S>> blocks, SubMonitor subMonitor) {
		long startTime1 = System.currentTimeMillis();
		Queue<Set<S>> blocksToProcess = new LinkedList<>(blocks);
		while (!blocksToProcess.isEmpty()) {
			final int PROGRESS_COUNT = 100;
			subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
			Set<S> block = blocksToProcess.poll();
			if (block.size() <= 1) {
				continue;
			}
			Set<Set<S>> refinedBlocks = refineBlock(block);
			if (refinedBlocks.size() > 1) {
				blocks.remove(block);
				applyRefinement(blocks, refinedBlocks);
				Set<Set<S>> outdatedBlocks = getOutDatedBlocks(refinedBlocks);
				for (Set<S> outdatedBlock : outdatedBlocks) {
					if (!blocksToProcess.contains(outdatedBlock)) {
						blocksToProcess.offer(outdatedBlock);
					}
				}
			}
		}
		long endTime1 = System.currentTimeMillis();
		System.out.println("The total time for refinement of blocks is : " + (endTime1 - startTime1) + "milliseconds");
	}

	/**
	 * The Set of States will be further refined based on their respective outgoing
	 * blocks
	 * 
	 * @param block takes the individual block from queue
	 * @return blocks refines the block into blocks further if necessary
	 */
	public Set<Set<S>> refineBlock(Set<S> block) {
		Map<Map<Object, Set<S>>, Set<S>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		Set<Set<S>> refinedBlocks = new HashSet<>();
		for (S state : block) {
			Map<Object, Set<S>> mapLabelsOfAStateToOutgoingBlock = createBlockReachabilityMapForAState(block,
					state);
			Set<S> refinedBlock = getEquivalentBlock(mapBlockReachabilityMapToRefinedBlock,
					mapLabelsOfAStateToOutgoingBlock);
			if (refinedBlock == null) {
				refinedBlock = new HashSet<S>();
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
	private Set<S> getEquivalentBlock(
			Map<Map<Object, Set<S>>, Set<S>> mapBlockReachabilityMapToRefinedBlock,
			Map<Object, Set<S>> mapLabelsOfAStateToOutgoingBlock) {
		Set<S> refinedblock = mapBlockReachabilityMapToRefinedBlock.get(mapLabelsOfAStateToOutgoingBlock);
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
	private Map<Object, Set<S>> createBlockReachabilityMapForAState(Set<S> block,
			S state) {
		Map<Object, Set<S>> mapLabelsOfAStateToOutgoingBlock = new HashMap<>();
		for (MarkovTransition<S> markovtransition : ma.getSuccTransitions(state)) {
			Set<S> toBlock = mapStateToBlock.get(markovtransition.getTo());
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
	private void applyRefinement(Set<Set<S>> blocks, Set<Set<S>> refinedBlocks) {
		for (Set<S> refinedBlock : refinedBlocks) {
			blocks.add(refinedBlock);
			for (S state : refinedBlock) {
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
	private Set<Set<S>> getOutDatedBlocks(Set<Set<S>> refinedBlocks) {
		Set<Set<S>> outdatedBlocks = new HashSet<>();
		for (Set<S> refinedBlock : refinedBlocks) {
			for (S state : refinedBlock) {
				for (MarkovTransition<S> markovtransition : ma.getPredTransitions(state)) {
					Set<S> fromBlock = mapStateToBlock.get(markovtransition.getFrom());
					outdatedBlocks.add(fromBlock);
				}
			}
		}
		return outdatedBlocks;
	}

	/**
	 * This method merges each equivalence class states into a single representative
	 * state
	 * 
	 * @param blocks the equivalence class blocks that are to be merged are given as
	 *               input
	 */
	public void mergeBlocks(Set<Set<S>> blocks, SubMonitor subMonitor) {
		double rate = 0;
		
		final int PROGRESS_COUNT = 100;
		subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
		long startTime = System.currentTimeMillis();
		for (Set<S> block : blocks) {
			S state = block.iterator().next();
			List<MarkovTransition<S>> stateOutgoingTransitions = ma.getSuccTransitions(state);
			for (int i = stateOutgoingTransitions.size() - 1; i >= 0; i--) {
				MarkovTransition<S> stateOutgoingTransition = stateOutgoingTransitions.get(i);
				S toState = stateOutgoingTransition.getTo();
				S blockRepresentative = mapStateToBlock.get(toState).iterator().next();
				Object stateEvent = stateOutgoingTransition.getEvent();
				if (state.isMarkovian() || state.isProbabilisic()) {
					rate = stateOutgoingTransition.getRate();
				}
				if (toState != blockRepresentative) {
					if (blockRepresentative != state) {
						if (state.isNondet()) {
							ma.addNondeterministicTransition(stateEvent, state, blockRepresentative);
						} else if (state.isMarkovian()) {
							ma.addMarkovianTransition(stateEvent, state, blockRepresentative, rate);
						} else {
							ma.addProbabilisticTransition(stateEvent, state, blockRepresentative, rate);
						}
					}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("The time taken for adding new transitions is : " + (endTime - startTime) + "milliseconds");
		Queue<Set<S>> blocksToProcess = new LinkedList<>(blocks);
		
		long startTime2 = System.currentTimeMillis();
		while (!blocksToProcess.isEmpty()) {
			Set<S> block = blocksToProcess.poll();
			S state = block.iterator().next();
			block.remove(state);
			for (S removedState : block) {
				ma.removeState(removedState);
			}
			block.add(state);
		}
//		for (Set<S> block : blocks) {
//			S state = block.iterator().next();
//			ma.removeInvalidTransitions(state);
//			ma.removeDuplicateTransitions(state);
//		}
		for (int i = 0; i < ma.getStates().size(); i++) {
			S state = ma.getStates().get(i);
     		ma.removeInvalidTransitions(state);
			ma.removeDuplicateTransitions(state);
			state.setIndex(i);
		}
		long endTime2 = System.currentTimeMillis();
		System.out.println("The time taken for deleting states and transitions is : " + (endTime2 - startTime2)  + "milliseconds");
		


	}
	

	/**
	 * compute the equivalence classes on the Markov automaton. Each equivalence
	 * class is represented as a "block" list of states.
	 * 
	 * @return the computed block partitions
	 */
	public Set<Set<S>> computeEquivalenceClasses(SubMonitor subMonitor) {
		subMonitor = SubMonitor.convert(subMonitor);
		Set<Set<S>> blocks = createInitialBlocks(subMonitor);
		refineBlocks(blocks, subMonitor);
		return blocks;
	}

	/**
	 * Computes Quotient of the equivalence Classes. Basically all the States in an
	 * equivalence Class are contracted into Single State
	 */
	public void computeQuotient(SubMonitor subMonitor) {
		subMonitor = SubMonitor.convert(subMonitor);
		long startTime1 = System.currentTimeMillis();
		Set<Set<S>> equivalenceClasses = computeEquivalenceClasses(subMonitor);
		long endTime1 = System.currentTimeMillis();
		System.out.println("The time taken for equivalence classes is : " + (endTime1 - startTime1) + "millisecs");
		long startTime2 = System.currentTimeMillis();
		mergeBlocks(equivalenceClasses, subMonitor);
		long endTime2 = System.currentTimeMillis();
		System.out.println("The time taken for merging is : " + (endTime2 - startTime2) + "millisecs");
	}
	
}