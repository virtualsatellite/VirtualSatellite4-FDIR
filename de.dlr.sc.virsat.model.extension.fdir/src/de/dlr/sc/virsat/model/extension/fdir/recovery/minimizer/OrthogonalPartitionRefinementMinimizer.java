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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
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
	private RecoveryAutomaton ra;
	private RecoveryAutomatonHolder raHolder;
	
	private Map<State, List<State>> mapStateToBlock;
	private Map<State, Map<FaultTreeNode, Boolean>> mapStateToDisabledInputs;
	private Set<FaultTreeNode> repairableEvents;
	private Set<FaultTreeNode> repeatedEvents;
	
	@Override
	public void minimize(RecoveryAutomatonHolder raHolder) {
		this.ra = raHolder.getRa();
		this.raHolder = raHolder;
		
		statistics = new MinimizationStatistics();
		statistics.time = System.currentTimeMillis();
		statistics.removedStates = ra.getStates().size();
		statistics.removedTransitions = ra.getTransitions().size();
		
		RecoveryAutomatonHelper raHelper = raHolder.getRaHelper();
		repairableEvents = raHelper.computeRepairableEvents(raHolder);
		mapStateToDisabledInputs = raHelper.computeDisabledInputs(raHolder);
		repeatedEvents = computeRepeatedEvents();
		
		Set<List<State>> blocks = createInitialBlocks();
		refineBlocks(blocks);
		mergeBlocks(blocks);
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		statistics.removedStates = statistics.removedStates - ra.getStates().size();
		statistics.removedTransitions = statistics.removedTransitions - ra.getTransitions().size();
	}
	
	/**
	 * Identify all repeated inputs and mark them to not be considered for orthogonal minimization
	 * This is to ensure that observable events which can occur more than once are correctly handled
	 */
	private Set<FaultTreeNode> computeRepeatedEvents() {
		Set<FaultTreeNode> repeatedEvents = new HashSet<>();
		for (Transition transition : ra.getTransitions()) {
			if (transition instanceof FaultEventTransition) {
				FaultEventTransition fte = (FaultEventTransition) transition;
				
				State state = transition.getFrom();
				Map<FaultTreeNode, Boolean> guaranteedInputs = mapStateToDisabledInputs.get(state);
				
				boolean isRepeated = raHolder.getMapTransitionToGuards().containsKey(transition);
				
				if (isRepeated 
						&& transition.getRecoveryActions().isEmpty() && transition.getFrom().equals(transition.getTo())) {
					isRepeated = false;
				}
				
				if (isRepeated) {
					for (FaultTreeNode guard : raHolder.getMapTransitionToGuards().get(transition)) {
						Boolean repairLabel = guaranteedInputs.get(guard);
						if (repairableEvents.contains(guard)) {
							if (!Objects.equals(repairLabel, fte.getIsRepair())) {
								isRepeated = false;
								break;
							}
						} else {
							if (!guaranteedInputs.containsKey(guard)) {
								isRepeated = false;
								break;
							}
						}
					}
				}
				
				if (isRepeated) {
					repeatedEvents.addAll(((FaultEventTransition) transition).getGuards());
				}
			}
		}
		
		return repeatedEvents;
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
	 * Performs one refinement step on a given partition.
	 * The contained states in the partition are refined into partitions with different transition profile.
	 * A transition profile is a mapping input -> partition
	 * @param block the partition to refine
	 * @return a list of refined partitions
	 */
	private List<List<State>> refineBlock(List<State> block) {
		Map<Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>>, List<State>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		List<List<State>> refinedBlocks = new ArrayList<>();
		
		for (State state : block) {
			List<Transition> outgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(state);
			Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlock = new HashMap<>();
			
			for (Transition transition : outgoingTransitions) {
				List<State> toBlock = mapStateToBlock.get(raHolder.getMapTransitionToTo().get(transition));
				if (toBlock != block || !raHolder.getMapTransitionToActionLabels().get(transition).isEmpty()) {
					Entry<Set<FaultTreeNode>, Boolean> guards = new SimpleEntry<>(raHolder.getMapTransitionToGuards().get(transition), false);
					
					if (transition instanceof FaultEventTransition) {
						FaultEventTransition fte = (FaultEventTransition) transition;
						guards.setValue(fte.getIsRepair());
					}
					
					mapGuardsToBlock.put(guards, toBlock);
				}
			}
			
			List<State> refinedBlock = null;
			for (Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlockOther : mapBlockReachabilityMapToRefinedBlock.keySet()) {
				boolean isEqual = true;
				Set<Entry<Set<FaultTreeNode>, Boolean>> allGuards = new HashSet<>(mapGuardsToBlock.keySet());
				allGuards.addAll(mapGuardsToBlockOther.keySet());
				
				equalityCheck: for (Entry<Set<FaultTreeNode>, Boolean> guards : allGuards) {
					if (mapGuardsToBlock.get(guards) != mapGuardsToBlockOther.get(guards)) {
						for (State stateOther : mapBlockReachabilityMapToRefinedBlock.get(mapGuardsToBlockOther)) {
							if (!isOrthogonalWithRespectToGuards(state, stateOther, guards.getKey(), guards.getValue())) {
								isEqual = false;
								break equalityCheck;
							}
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
		
			List<Transition> outgoingTransitionsState = raHolder.getMapStateToOutgoingTransitions().get(state);
			List<Transition> incomingTransitionsState = raHolder.getMapStateToIncomingTransitions().get(state);
			
			List<Transition> outgoingTransitionsToAdd = new ArrayList<>();
			List<Transition> incomingTransitionsToAdd = new ArrayList<>();
			
			for (State removedState : block) {
				List<Transition> outgoingTransitions = raHolder.getMapStateToOutgoingTransitions().get(removedState);
				List<Transition> incomingTransitions = raHolder.getMapStateToIncomingTransitions().get(removedState);
				
				for (Transition transition : outgoingTransitions) {
					outgoingTransitionsToAdd.add(transition);
					transition.setFrom(state);
				}
				
				for (Transition transition : incomingTransitions) {
					incomingTransitionsToAdd.add(transition);
					transition.setTo(state);
					raHolder.getMapTransitionToTo().put(transition, state);
				}
			}
			
			outgoingTransitionsState.addAll(outgoingTransitionsToAdd);
			incomingTransitionsState.addAll(incomingTransitionsToAdd);
			
			raHolder.getMapStateToOutgoingTransitions().keySet().removeAll(block);
			raHolder.getMapStateToIncomingTransitions().keySet().removeAll(block);
			raHolder.getMapStateToGuardProfile().keySet().removeAll(block);
			
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
		List<Transition> outgoingTransitions0 = raHolder.getMapStateToOutgoingTransitions().get(state0);
		List<Transition> outgoingTransitions1 = raHolder.getMapStateToOutgoingTransitions().get(state1);
		return isRecoveryEquivalent(outgoingTransitions0, state0, state1) && isRecoveryEquivalent(outgoingTransitions1, state0, state1);
	}
	
	/**
	 * Checks if a list of transition is recovery equivalent, that is either:
	 * - The guards are orthogonal in the two passed states
	 * - The guards have the same transition profile in the two passed states 
	 * @param transitions the transitions to be checked
	 * @param state0 one state
	 * @param state1 other state
	 * @return true iff the transition is recovery equivalent
	 */
	private boolean isRecoveryEquivalent(List<Transition> transitions, State state0, State state1) {
		for (Transition transition : transitions) {
			Set<FaultTreeNode> guards0 = raHolder.getMapTransitionToGuards().get(transition);
			if (guards0 == null) {
				return false;
			}
			
			if (transition instanceof FaultEventTransition) {
				// Check for orthogonality				
				FaultEventTransition fte = (FaultEventTransition) transition;
				if (isOrthogonalWithRespectToGuards(state0, state1, guards0, fte.getIsRepair())) {
					continue;
				}
				
				if (!Objects.equals(raHolder.getMapStateToGuardProfile().get(state0).get(guards0), 
						raHolder.getMapStateToGuardProfile().get(state1).get(guards0))) {
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	 * Checks if two states are orthogonal with respect to a set of guards.
	 * @param state0 the first state
	 * @param state1 the second state
	 * @param guards the guards
	 * @param 
	 * @return true iff state0 and state1 are orthogonal with respect to the set of guards transition
	 */
	private boolean isOrthogonalWithRespectToGuards(State state0, State state1, Set<FaultTreeNode> guards, boolean isRepair) {
		for (FaultTreeNode guard : guards) {
			if (repeatedEvents.contains(guard)) {
				return false;
			}
		}
		
		
		Map<FaultTreeNode, Boolean> disabledInputs0 = mapStateToDisabledInputs.get(state0);
		Map<FaultTreeNode, Boolean> disabledInputs1 = mapStateToDisabledInputs.get(state1);
		
		if (Collections.disjoint(guards, repairableEvents)) {
			if (disabledInputs0.keySet().containsAll(guards)) {
				return true;
			}
			
			if (disabledInputs1.keySet().containsAll(guards)) {
				return true;
			}
			
			return false;
		} else {
			for (FaultTreeNode guard : guards) {
				Boolean repairLabel0 = disabledInputs0.get(guard);
				Boolean repairLabel1 = disabledInputs1.get(guard);
				
				if (!Objects.equals(repairLabel0, isRepair) || !Objects.equals(repairLabel1, isRepair)) {
					return false;
				}
			}
			return true;
		}
	}
}