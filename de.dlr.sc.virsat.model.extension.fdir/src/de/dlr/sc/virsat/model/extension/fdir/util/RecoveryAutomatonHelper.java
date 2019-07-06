/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;


/**
 * Class with helper functions for recovery automata. 
 * @author mika_li
 *
 */

public class RecoveryAutomatonHelper {

	private Concept concept;

	/**
	 * Basic constructor for RecoveryAutomatonHelper 
	 * @param concept
	 * the concept to be used for construction
	 */
	public RecoveryAutomatonHelper(Concept concept) {
		this.concept = concept;
	}	
	
	/**
	 * Creates a state
	 * @param ra Recovery Automaton to add a state to 
	 * @param num number to name the state  
	 * @return created state
	 */
	public State createSingleState(RecoveryAutomaton ra, int num) {
		State state = new State(concept);
		state.setName("state" + num);
		ra.getStates().add(state);
		return state;
	}
	
	/**
	 * Creates multiple number of states
	 * @param ra recovery automaton that to add the states to
	 * @param num the number of states to be created 
	 */
	public void createStates(RecoveryAutomaton ra, int num) {
		for (int i = 0; i < num; i++) {
			createSingleState(ra, i);
		}
	}

	/**
	 * Creates a transition from a state to another state
	 * @param ra Recovery Automaton to add the transition to  
	 * @param presentState the present state from which the transition is created
	 * @param successorState the next state to which the transition is created
	 * @return transition between the present and the successor states
	 */
	
	public Transition createTransition(RecoveryAutomaton ra, State presentState, State successorState) {
		Transition transition = new Transition(concept);
		
		transition.setFrom(presentState);
		transition.setTo(successorState);
		ra.getTransitions().add(transition);
		
		return transition; 
	}
	
	/**
	 * Assigns an input to a transition
	 * @param transition the transition to which the input is assigned
	 * @param faults the faults (input) that are assigned to the transition 
	 */
	public void assignInputs(Transition transition, FaultTreeNode... faults) {
		for (FaultTreeNode fault : faults) {
			transition.getGuards().add(fault);
		}
	}

	/**
	 * Adds an action to a transition  
	 * @param transition the transition to which the action is added
	 * @param claimAction the action that will be assigned to the transition 
	 * @return assignedActions present state recovery actions 
	 */
	public IBeanList<RecoveryAction> assignAction(Transition transition, ClaimAction claimAction) {
		
		transition.getRecoveryActions().add(claimAction); 
		
		return transition.getRecoveryActions();
	}
	
	/**
	 * Determine if two states in a recovery automaton are connected by a transition
	 * @param ra the recovery automaton
	 * @param from the state where the transition is from
	 * @param to the state where the transition is to
	 * @return true if there exists such a transition, false otherwise
	 */
	public boolean isConnected(RecoveryAutomaton ra, State from, State to) {
		for (Transition transition : ra.getTransitions()) {
			if (transition.getFrom().getName().equals(from.getName()) && transition.getTo().getName().equals(to.getName())) {
				return true;
			}
		}
		return false;
	}
	
	
	/** 
	 * Get state by its name
	 * @param ra the recovery automaton
	 * @param name name of the state
	 * @return the state
	 */
	public State getState(RecoveryAutomaton ra, String name) {
		for (State state : ra.getStates()) {
			if (state.getName().equals(name)) {
				return state;
			}
		}
		return null;
	}
	
	/**
	 * Creates a map of current transitions to states
	 * @param ra recovery automaton
	 * @return a map of current transitions to states
	 */
	public Map<State, List<Transition>> getCurrentTransitions(RecoveryAutomaton ra) {
		Map<State, List<Transition>> mapStateToSuccessors = new HashMap<>();
		
		List<Transition> transitions = ra.getTransitions(); 
		
		for (State state : ra.getStates()) {
			mapStateToSuccessors.put(state, new ArrayList<>());
		}
		
		for (Transition transition : transitions) {
			State toState = transition.getFrom();
			List<Transition> currentTransitions = mapStateToSuccessors.get(toState);
			currentTransitions.add(transition);
		}
		
		return mapStateToSuccessors;
	}
	
	/**
	 * Creates a map of previous transitions to states
	 * @param ra recovery automaton
	 * @return a map of previous transitions to states
	 */
	public Map<State, List<Transition>> getPreviousTransitions(RecoveryAutomaton ra) {
		Map<State, List<Transition>> mapStateToPredecessors = new HashMap<>();
		
		List<Transition> transitions = ra.getTransitions(); 
		
		for (State state : ra.getStates()) {
			mapStateToPredecessors.put(state, new ArrayList<>());
		}
		
		for (Transition transition : transitions) {
			State toState = transition.getTo();
			List<Transition> previousTransitions = mapStateToPredecessors.get(toState);
			previousTransitions.add(transition);
		}
		
		return mapStateToPredecessors;
	}

	/**
	 * Creates a copy of the given recovery action
	 * @param recoveryAction the recovery action to copy
	 * @return the copied recovery action
	 */
	public RecoveryAction copyRecoveryAction(RecoveryAction recoveryAction) {
		if (recoveryAction instanceof ClaimAction) {
			return copyClaimAction((ClaimAction) recoveryAction);
		}
		
		throw new RuntimeException("Unknown Recovery Action type of recovery action " + recoveryAction.toString());
	}
	
	/**
	 * Creates a copy of a claimAction
	 * @param claimAction clamAction whose copy gets created
	 * @return copy of the claimAction
	 */
	public ClaimAction copyClaimAction(ClaimAction claimAction) {
		
		ClaimAction copyClaimAction = new ClaimAction(concept);
		copyClaimAction.setClaimSpare(claimAction.getClaimSpare());
		copyClaimAction.setSpareGate(claimAction.getSpareGate());
		
		return copyClaimAction;
	}
	
	/**
	 * Computes a map of inputs for every transition in the path to a state 
	 * @param ra recovery automaton to compute the inputs for
	 * @param mapStateToPredecessors mapping of state to precessor transitions
	 * @param mapStateToSuccessors mapping of state to successor transitions 
	 * @param mapTransitionToTo mapping of a transition to its succesor state
	 * @return map of inputs 
	 */
	public Map<State, Set<FaultTreeNode>> computeInputs(RecoveryAutomaton ra, Map<State, List<Transition>> mapStateToPredecessors, Map<State, List<Transition>> mapStateToSuccessors, Map<Transition, State> mapTransitionToTo) {
		Map<State, Set<FaultTreeNode>> mapStateToInputs = new HashMap<>();
		
		for (State s : ra.getStates()) {
			mapStateToInputs.put(s, new HashSet<>());
		}
		
		Queue<State> queue = new LinkedList<>(ra.getStates());
		while (!queue.isEmpty()) {
			State s = queue.poll();
			
			// Compute the updated set of guaranteed inputs
			Set<FaultTreeNode> newInputs = new HashSet<>();
			boolean initialInput = true;
			for (Transition predecessor : mapStateToPredecessors.get(s)) {
				State predecessorState = predecessor.getFrom();
				if (!s.equals(predecessorState)) {
					Set<FaultTreeNode> incomingInputs = new HashSet<>(mapStateToInputs.get(predecessorState));
					incomingInputs.addAll(predecessor.getGuards());
					if (initialInput) {
						initialInput = false;
						newInputs.addAll(incomingInputs);
					} else {
						newInputs.retainAll(incomingInputs);
					}
				}
			}
			
			Set<FaultTreeNode> oldInputs = mapStateToInputs.get(s); 
			if (!newInputs.equals(oldInputs)) {
				// If no fix point has been reached yet, update the set
				// and recompute all successors
				
				mapStateToInputs.put(s, newInputs);
				for (Transition successor : mapStateToSuccessors.get(s)) {
					State successorState = mapTransitionToTo.get(successor);
					if (!queue.contains(successorState)) {
						queue.offer(successorState);
					}
				}
			}
		}
		
		return mapStateToInputs;
	}
	
	/**
	 * Determines whether a state is final  
	 * @param ra recovery automaton 
	 * @param state state to determine if it is final 
	 * @param outgoingTransitions list of outgoing transitions
	 * @return true if state is final, false otherwise 
	 */
	public boolean isFinalState(RecoveryAutomaton ra, State state, List<Transition> outgoingTransitions) { 
		
		boolean finalState = true; 
		
		for (Transition transition : outgoingTransitions) {
			if (!state.equals(transition.getTo()) || !transition.getRecoveryActions().isEmpty()) {
				return false;
			} 
		}
		
		return finalState;  
	}
	
	/**
	 * Determines whether all outgoing transitions lead to the same next state
	 * @param ra recovery automaton
	 * @param outgoingTransitions a list of outgoing transitions from a state
	 * @return true if transitions lead to the same next state, false otherwise 
	 */
	public boolean isFinalStateEquivalent(RecoveryAutomaton ra, List<Transition> outgoingTransitions) {
		if (outgoingTransitions.size() > 1) {
			boolean nonEmptyActions = false;
			for (int i = 0; i < outgoingTransitions.size(); i++) {
				
				if (!outgoingTransitions.get(i).getRecoveryActions().isEmpty()) {
					if (!nonEmptyActions) {
						nonEmptyActions = true;
					} else {
						return false;
					}
				}
				
				for (int j = i + 1; j < outgoingTransitions.size(); j++) {
					if (!outgoingTransitions.get(i).getTo().equals(outgoingTransitions.get(j).getTo())) {
						return false; 
					} 
				}
			}
		} 
		
		return true; 
	}
	
	
	/**
	 * Creates a copy of a given recovery automaton 
	 * @param ra recovery automaton to create a copy of 
	 * @return newRA 
	 */
	public RecoveryAutomaton copyRA(RecoveryAutomaton ra) {
		RecoveryAutomaton newRA = new RecoveryAutomaton(concept);
		Map<State, State> mapOldStateToNewState = new HashMap<>();
		
		for (State state : ra.getStates()) {
			State newState = new State(concept);
			newState.setName(state.getName());
			newRA.getStates().add(newState);
			mapOldStateToNewState.put(state, newState);
		}
		
		for (Transition transition : ra.getTransitions()) {
			Transition newTransition = new Transition(concept);
			newRA.getTransitions().add(newTransition);
			newTransition.setFrom(mapOldStateToNewState.get(transition.getFrom()));
			newTransition.setTo(mapOldStateToNewState.get(transition.getTo()));
			for (FaultTreeNode fault : transition.getGuards()) {
				newTransition.getGuards().add(fault);
			}
			for (RecoveryAction recoveryAction : transition.getRecoveryActions())  {
				newTransition.getRecoveryActions().add(copyRecoveryAction(recoveryAction));
			}
		}
		
		newRA.setInitial(mapOldStateToNewState.get(ra.getInitial()));
		
		return newRA;
	}
}
