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
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.StateHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.TransitionHolder;

/**
 * Minimizes a given recovery automaton using the partition refinement algorithm.
 * @author muel_s8
 *
 */

public class PartitionRefinementMinimizer extends APartitionRefinementMinimizer {
	
	@Override
	protected boolean belongsToBlock(List<State> block, State state) {
		return raHolder.getStateHolder(state).getGuardProfile().equals(raHolder.getStateHolder(block.get(0)).getGuardProfile());
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
		for (Transition transition : raHolder.getStateHolder(state).getOutgoingTransitions()) {
			TransitionHolder transitionHolder = raHolder.getTransitionHolder(transition);
			List<State> toBlock = mapStateToBlock.get(transitionHolder.getTo());
			if (transition instanceof TimeoutTransition) {
				toBlock = getTimeoutBlock(block, state);
			}
			
			if (toBlock != block || !transitionHolder.isEpsilonTransition()) {
				mapGuardsToBlock.put(transitionHolder.getGuards(), toBlock);
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
			StateHolder stateHolder = raHolder.getStateHolder(internalState);
					
			TimeoutTransition timeoutTransition = stateHolder.getTimeoutTransition();
			if (timeoutTransition != null) {
				internalState = raHolder.getTransitionHolder(timeoutTransition).getTo();
				toBlock = mapStateToBlock.get(internalState);
			}
			
			if (timeoutTransition == null || !visitedStates.add(internalState)) {
				break;
			}
		}
		return toBlock;
	}
	
	@Override
	protected void mergeBlocks(Set<List<State>> blocks) {
		State initial = raHolder.getRa().getInitial();
		
		// Redirect all transitions between blocks so that they are between the block represenatatives
		for (List<State> block : blocks) {
			State state = block.get(0);
			StateHolder stateHolder = raHolder.getStateHolder(state);
			List<Transition> outgoingTransitions = stateHolder.getOutgoingTransitions();
			for (Transition transition : outgoingTransitions) {
				TransitionHolder transitionHolder = raHolder.getTransitionHolder(transition);
				State stateTo = transitionHolder.getTo();
				State blockRepresentative = mapStateToBlock.get(stateTo).get(0);
				if (blockRepresentative != stateTo) {
					if (blockRepresentative != state || !transitionHolder.isEpsilonTransition()) {
						transitionHolder.setTo(blockRepresentative);
					}
				}
			}
			
			BlockTimeoutTransition blockTimeoutTransition = new BlockTimeoutTransition(block);
			if (blockTimeoutTransition.toState != null) {
				TimeoutTransition timeoutTransition = stateHolder.getTimeoutTransition();
				timeoutTransition.setTime(blockTimeoutTransition.timeout);
				raHolder.getTransitionHolder(timeoutTransition).setTo(blockTimeoutTransition.toState);
			}
			
			if (block.contains(initial)) {
				raHolder.getRa().setInitial(state);
			}
		}
		
		List<Transition> transitionsToRemove = new ArrayList<>();
		
		// Now remove all the non-representative states and all their transitions
		for (List<State> block : blocks) {
			State state = block.get(0);
			block.remove(state);
			
			for (State removedState : block) {
				transitionsToRemove.addAll(raHolder.getStateHolder(removedState).getOutgoingTransitions());
				transitionsToRemove.addAll(raHolder.getStateHolder(removedState).getIncomingTransitions());
			}
			
			raHolder.removeStates(block);
		}
		
		raHolder.removeTransitions(transitionsToRemove);
	}	

	
	/**
	 * The target of the timeout transitions and the total timeout time.
	 *
	 */
	private class BlockTimeoutTransition {
		State toState;
		double timeout;
		
		/**
		 * Standard constructor
		 * @param block the block to consider
		 */
		BlockTimeoutTransition(List<State> block) {
			for (State state : block) {
				TimeoutTransition timeoutTransition = raHolder.getStateHolder(state).getTimeoutTransition();
				if (timeoutTransition != null) {
					State stateTo = raHolder.getTransitionHolder(timeoutTransition).getTo();
					List<State> blockTarget = mapStateToBlock.get(stateTo);
					if (block != blockTarget) {
						if (toState == null) {
							toState = blockTarget.get(0);
							timeout += timeoutTransition.getTime();
						}
					} else {
						timeout += timeoutTransition.getTime();
					}
				}
			}
		}
	}
}
