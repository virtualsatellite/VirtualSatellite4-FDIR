/*******************************************************************************
- * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.StateHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.TransitionHolder;

/**
 * Minimizes a given recovery automaton using the partition refinement algorithm.
 * @author muel_s8
 *
 */

public class OrthogonalPartitionRefinementMinimizer extends APartitionRefinementMinimizer {
	private Map<State, Map<FaultTreeNode, Boolean>> mapStateToDisabledInputs;
	private Set<FaultTreeNode> repairableEvents;
	private Set<FaultTreeNode> repeatedEvents;
	private FaultTreeHolder ftHolder;
	
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder, FaultTreeHolder ftHolder, SubMonitor subMonitor) {
		this.ftHolder = ftHolder;
		super.minimize(raHolder, ftHolder, subMonitor);
	}
	
	@Override
	protected Set<List<State>> computeBlocks(SubMonitor subMonitor) {
		RecoveryAutomatonHelper raHelper = raHolder.getRaHelper();
		repairableEvents = raHelper.computeRepairableEvents(raHolder);
		mapStateToDisabledInputs = raHelper.computeDisabledInputs(raHolder);
		repeatedEvents = computeRepeatedEvents();
		
		return super.computeBlocks(subMonitor);
	}
	
	/**
	 * Identify all repeated inputs and mark them to not be considered for orthogonal minimization
	 * This is to ensure that observable events which can occur more than once are correctly handled
	 */
	private Set<FaultTreeNode> computeRepeatedEvents() {
		Concept concept = raHolder.getRa().getConcept();
		Category monitorCategory = ActiveConceptHelper.getCategory(concept, MONITOR.FULL_QUALIFIED_CATEGORY_NAME);
		
		Set<FaultTreeNode> repeatedEvents = new HashSet<>();
		for (Transition transition : raHolder.getRa().getTransitions()) {
			if (transition instanceof FaultEventTransition) {
				FaultEventTransition fte = (FaultEventTransition) transition;
				
				State state = transition.getFrom();
				Map<FaultTreeNode, Boolean> guaranteedInputs = mapStateToDisabledInputs.get(state);
				
				TransitionHolder transitionHolder = raHolder.getTransitionHolder(transition);
				boolean isRepeated = !transitionHolder.isEpsilonLoop();
				
				if (isRepeated) {
					for (FaultTreeNode guard : transitionHolder.getGuards()) {
						if (ftHolder != null) {
							List<FaultTreeNode> monitors = ftHolder.getNodes(guard, EdgeType.MONITOR);
							if (!guard.getTypeInstance().getType().equals(monitorCategory) && monitors.isEmpty()) {
								isRepeated = false;
								break;
							}
						}
						
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
	
	@Override
	protected boolean belongsToBlock(List<State> block, State state) {
		for (State other : block) {
			if (!isActionEquivalent(other, state)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected List<List<State>> refineBlock(List<State> block) {
		Map<Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>>, List<State>> mapBlockReachabilityMapToRefinedBlock = new HashMap<>();
		List<List<State>> refinedBlocks = new ArrayList<>();
		
		for (State state : block) {
			Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlock = createReachabilityProfile(block, state);
			List<State> refinedBlock = getEquivalentBlock(mapBlockReachabilityMapToRefinedBlock, state, mapGuardsToBlock);
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
	 * Creates the reachability profile for a state in the given block.
	 * @param block the block of the state
	 * @param state the current state
	 * @return the reachability profile of the state
	 */
	private Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> createReachabilityProfile(List<State> block, State state) {
		Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlock = new HashMap<>();
		List<Transition> outgoingTransitions = raHolder.getStateHolder(state).getOutgoingTransitions();
		for (Transition transition : outgoingTransitions) {
			TransitionHolder transitionHolder = raHolder.getTransitionHolder(transition);
			List<State> toBlock = mapStateToBlock.get(transitionHolder.getTo());
			if (toBlock != block || !transitionHolder.isEpsilonTransition()) {
				Entry<Set<FaultTreeNode>, Boolean> guards = new SimpleEntry<>(transitionHolder.getGuards(), false);
				
				if (transition instanceof FaultEventTransition) {
					FaultEventTransition fte = (FaultEventTransition) transition;
					guards.setValue(fte.getIsRepair());
				}
				
				mapGuardsToBlock.put(guards, toBlock);
			}
		}
		return mapGuardsToBlock;
	}

	/**
	 * Checks if in the current set of reachability maps there exists one, that is equivalent, 
	 * to the reachability map of a a given state
	 * @param mapBlockReachabilityMapToRefinedBlock the reachability maps computed until now
	 * @param state the current state 
	 * @param mapGuardsToBlock the reachability map of the state
	 * @return a refined block with an orthogoally equivalent reachability map or null if none exists
	 */
	private List<State> getEquivalentBlock(Map<Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>>, List<State>> mapBlockReachabilityMapToRefinedBlock,
			State state, Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlock) {
		
		for (Entry<Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>>, List<State>> entry : mapBlockReachabilityMapToRefinedBlock.entrySet()) {
			Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlockOther = entry.getKey();
			List<State> blockOther = entry.getValue();
			if (isOrthogonallyEqivalent(state, mapGuardsToBlock, mapGuardsToBlockOther, blockOther)) {
				return mapBlockReachabilityMapToRefinedBlock.get(mapGuardsToBlockOther);
			}
		}
		return null;
	}

	/**
	 * Checks if the reachability profile of a state is orthogonally equivalent to the
	 * reachability profile of another state.
	 * I.e. for all transitions with matching guards the same blocks must be reached
	 * or the guards must be orthogonal.
	 * 
	 * @param state the current state
	 * @param mapGuardsToBlock the reachability profile of the state
	 * @param mapGuardsToBlockOther the reachability profile of another block
	 * @param blockOther another block
	 * @return true iff the reachability profile of the other block is orthogonally equivalent.
	 */
	private boolean isOrthogonallyEqivalent(State state, Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlock,
			Map<Entry<Set<FaultTreeNode>, Boolean>, List<State>> mapGuardsToBlockOther, List<State> blockOther) {
		Set<Entry<Set<FaultTreeNode>, Boolean>> allGuards = new HashSet<>(mapGuardsToBlock.keySet());
		allGuards.addAll(mapGuardsToBlockOther.keySet());
		for (Entry<Set<FaultTreeNode>, Boolean> guards : allGuards) {
			if (mapGuardsToBlock.get(guards) != mapGuardsToBlockOther.get(guards)) {
				for (State stateOther : blockOther) {
					if (!isOrthogonalWithRespectToGuards(state, stateOther, guards.getKey(), guards.getValue())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	protected void mergeBlocks(Set<List<State>> blocks) {
		State initial = raHolder.getRa().getInitial();
		
		List<Transition> transitionsToRemove = new ArrayList<>();
		
		for (List<State> block : blocks) {
			State state = block.get(0);
			
			if (block.contains(initial)) {
				raHolder.getRa().setInitial(state);
			}
			
			List<Transition> repOutgoingTransitions = raHolder.getStateHolder(state).getOutgoingTransitions();
			for (Transition transition : repOutgoingTransitions) {
				if (isDisabled(state, transition)) {
					transitionsToRemove.add(transition);
				}
			}
			
			block.remove(state);
			
			for (State removedState : block) {
				StateHolder removedStateHolder = raHolder.getStateHolder(removedState);
				List<Transition> outgoingTransitions = new ArrayList<>(removedStateHolder.getOutgoingTransitions());
				List<Transition> incomingTransitions = new ArrayList<>(removedStateHolder.getIncomingTransitions());
				
				for (Transition transition : outgoingTransitions) {
					if (isDisabled(removedState, transition)) {
						transitionsToRemove.add(transition);
					} else {
						raHolder.getTransitionHolder(transition).setFrom(state);
					}
				}
				
				for (Transition transition : incomingTransitions) {
					raHolder.getTransitionHolder(transition).setTo(state);
				}
			}
			
			raHolder.removeStates(block);
		}
		
		raHolder.removeTransitions(transitionsToRemove);
	}
	
	/**
	 * Determines if states are equivalent considering their transitions
	 * @param state0 first state
	 * @param state1 second state
	 * @return isEqual 
	 */
	private boolean isActionEquivalent(State state0, State state1) {
		List<Transition> outgoingTransitions0 = raHolder.getStateHolder(state0).getOutgoingTransitions();
		List<Transition> outgoingTransitions1 = raHolder.getStateHolder(state1).getOutgoingTransitions();
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
			Set<FaultTreeNode> guards0 = raHolder.getTransitionHolder(transition).getGuards();
			if (guards0 == null) {
				return false;
			}
			
			if (transition instanceof FaultEventTransition) {
				// Check for orthogonality				
				FaultEventTransition fte = (FaultEventTransition) transition;
				if (isOrthogonalWithRespectToGuards(state0, state1, guards0, fte.getIsRepair())) {
					continue;
				}
				
				String actions0 = raHolder.getStateHolder(state0).getGuardProfile().get(guards0);
				String actions1 = raHolder.getStateHolder(state1).getGuardProfile().get(guards0);
				if (!Objects.equals(actions0, actions1)) {
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
	 * @param isRepair whether the guards are repair guards
	 * @return true iff state0 and state1 are orthogonal with respect to the set of guards transition
	 */
	private boolean isOrthogonalWithRespectToGuards(State state0, State state1, Set<FaultTreeNode> guards, boolean isRepair) {
		return isDisabled(state0, guards, isRepair) || isDisabled(state1, guards, isRepair);
	}
	
	/**
	 * Checks if a transition is disabled in a state
	 * @param state the state
	 * @param transition the transition
	 * @return true iff the transition is an event transition and the guards are disabled
	 */
	private boolean isDisabled(State state, Transition transition) {
		if (transition instanceof FaultEventTransition) {
			FaultEventTransition fet = (FaultEventTransition) transition;
			return isDisabled(state, fet.getGuards(), fet.getIsRepair());
		}
		
		return false;
	}
	
	/**
	 * Checks if a set of guards is disabled in a state
	 * @param state the the state
	 * @param guards the guards
	 * @param isRepair whether the guards are repair guards
	 * @return true iff at least one guard is disabled in the state
	 */
	private boolean isDisabled(State state, Collection<FaultTreeNode> guards, boolean isRepair) {
		Map<FaultTreeNode, Boolean> disabledInputs = mapStateToDisabledInputs.get(state);
		
		for (FaultTreeNode guard : guards) {
			if (ftHolder != null) {
				if (!hasEnabledMonitor(guard, disabledInputs)) {
					disabledInputs.put(guard, false);
				} else if (repeatedEvents.contains(guard)) {
					return false;
				}
			}
			
			Boolean repairLabel = disabledInputs.get(guard);
			
			if (!Objects.equals(repairLabel, isRepair)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if there exists an enabled monitor gate for a given guard.
	 * @param guard the guard to be checked
	 * @param disabledInputs a list of inputs that are disabled in a give state
	 * @return true iff there are no monitors or if at least one monitor gate is not disabled
	 */
	private boolean hasEnabledMonitor(FaultTreeNode guard, Map<FaultTreeNode, Boolean> disabledInputs) {
		List<FaultTreeNode> monitors = ftHolder.getNodes(guard, EdgeType.MONITOR);
		
		// If there are no monitor gates in the first place
		// we assume that the npde is always fully observable
		if (monitors.isEmpty()) {
			return true;
		}
		
		for (FaultTreeNode monitor : monitors) {
			Boolean repairLabel = disabledInputs.get(monitor);
			if (repairLabel == null || repairLabel) {
				return true;
			}
		}
		
		return false;
	}
}