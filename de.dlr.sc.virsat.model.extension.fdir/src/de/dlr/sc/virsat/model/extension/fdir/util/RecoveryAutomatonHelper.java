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

import org.eclipse.emf.ecore.util.EcoreUtil;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
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
	 * Creates a fault event guarded transition from a state to another state
	 * @param ra Recovery Automaton to add the transition to  
	 * @param presentState the present state from which the transition is created
	 * @param successorState the next state to which the transition is created
	 * @return transition between the present and the successor states
	 */
	public FaultEventTransition createFaultEventTransition(RecoveryAutomaton ra, State presentState, State successorState) {
		FaultEventTransition transition = new FaultEventTransition(concept);
		
		transition.setFrom(presentState);
		transition.setTo(successorState);
		ra.getTransitions().add(transition);
		
		return transition; 
	}
	
	/**
	 * Creates a timed transition from a state to another state
	 * @param ra Recovery Automaton to add the transition to  
	 * @param presentState the present state from which the transition is created
	 * @param successorState the next state to which the transition is created
	 * @param time the time guard
	 * @return transition between the present and the successor states
	 */
	public TimeoutTransition createTimeoutTransition(RecoveryAutomaton ra, State presentState, State successorState, double time) {
		TimeoutTransition transition = new TimeoutTransition(concept);
		
		transition.setFrom(presentState);
		transition.setTo(successorState);
		ra.getTransitions().add(transition);
		transition.setTime(time);
		
		return transition; 
	}
	
	/**
	 * Assigns an input to a transition
	 * @param transition the transition to which the input is assigned
	 * @param faults the faults (input) that are assigned to the transition 
	 */
	public void assignInputs(FaultEventTransition transition, FaultTreeNode... faults) {
		for (FaultTreeNode fault : faults) {
			transition.getGuards().add(fault);
		}
	}

	/**
	 * Adds an action to a transition  
	 * @param transition the transition to which the action is added
	 * @param recoveryAction the action that will be assigned to the transition
	 */
	public void assignAction(Transition transition, RecoveryAction recoveryAction) {
		transition.getRecoveryActions().add(recoveryAction); 
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
			if (transition.getFrom().equals(from) && transition.getTo().equals(to)) {
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
	 * Gets the timeout transition of a state if it has one.
	 * @param ra the recovery automaton
	 * @param state the state
	 * @return the timeout transition or null if there is none
	 */
	public TimeoutTransition getTimeoutTransition(RecoveryAutomaton ra, State state) {
		List<Transition> transitions = getCurrentTransitions(ra).get(state);
		TimeoutTransition timeoutTransition = null;
		for (Transition transition : transitions) {
			if (transition instanceof TimeoutTransition) {
				timeoutTransition = (TimeoutTransition) transition;
			}
		}
		return timeoutTransition;
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
	 * Computes a map of inputs for every transition in the path to a state 
	 * @param raHolder holde rof the ra data
	 * @return map of inputs 
	 */
	public Map<State, Map<FaultTreeNode, Boolean>> computeDisabledInputs(RecoveryAutomatonHolder raHolder) {
		Map<State, Map<FaultTreeNode, Boolean>> mapStateToInputs = new HashMap<>();
		RecoveryAutomaton ra = raHolder.getRa();
		
		for (State s : ra.getStates()) {
			mapStateToInputs.put(s, new HashMap<>());
		}
		
		// For the initial state all repair events are disabled
		State initialState = ra.getInitial();
		if (initialState != null) {
			Map<FaultTreeNode, Boolean> repairEvents = new HashMap<>();
			for (Transition transition : raHolder.getRa().getTransitions()) {
				if (transition instanceof FaultEventTransition) {
					FaultEventTransition fte = (FaultEventTransition) transition;
					for (FaultTreeNode guard : fte.getGuards()) {
						repairEvents.put(guard, true);
					}
				}
			}
			mapStateToInputs.get(ra.getInitial()).putAll(repairEvents);
		}
		
		Queue<State> queue = new LinkedList<>(ra.getStates());
		while (!queue.isEmpty()) {
			State s = queue.poll();
			
			// Compute the updated set of guaranteed inputs
			Map<FaultTreeNode, Boolean> newInputs = new HashMap<>();
			boolean initialInput = true;
			StateHolder stateHolder = raHolder.getStateHolder(s);
			for (Transition predecessorTransition : stateHolder.getIncomingTransitions()) {
				State predecessorState = predecessorTransition.getFrom();
				if (!s.equals(predecessorState)) {
					Map<FaultTreeNode, Boolean> incomingInputs = new HashMap<>(mapStateToInputs.get(predecessorState));
					if (predecessorTransition instanceof FaultEventTransition) {
						FaultEventTransition fte = (FaultEventTransition) predecessorTransition;
						List<FaultTreeNode> guards = fte.getGuards();

						incomingInputs.keySet().removeAll(guards);
						for (FaultTreeNode guard : guards) {
							incomingInputs.put(guard, fte.getIsRepair());
						}
					}
					if (initialInput) {
						initialInput = false;
						newInputs.putAll(incomingInputs);
					} else {
						newInputs.keySet().retainAll(incomingInputs.keySet());
					}
				}
			}
			
			Map<FaultTreeNode, Boolean> oldInputs = mapStateToInputs.get(s); 
			if (!newInputs.equals(oldInputs)) {
				// If no fix point has been reached yet, update the set
				// and recompute all successors
				
				mapStateToInputs.put(s, newInputs);
				for (Transition successor : stateHolder.getOutgoingTransitions()) {
					State successorState = raHolder.getTransitionHolder(successor).getTo();
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
			Transition newTransition = null;
			
			if (transition instanceof FaultEventTransition) {
				newTransition = copyFaultEventTransition((FaultEventTransition) transition);
			} else if (transition instanceof TimeoutTransition) {
				newTransition = copyTimeoutTransition((TimeoutTransition) transition);
			} else {
				throw new RuntimeException("Unknown transition type " +  transition);
			}
			
			newTransition.setFrom(mapOldStateToNewState.get(transition.getFrom()));
			newTransition.setTo(mapOldStateToNewState.get(transition.getTo()));
			newRA.getTransitions().add(newTransition);
		}
		
		newRA.setInitial(mapOldStateToNewState.get(ra.getInitial()));
		
		return newRA;
	}
	
	/**
	 * Copies a fault event transition
	 * @param transition the transition to copy
	 * @return a copy
	 */
	public FaultEventTransition copyFaultEventTransition(FaultEventTransition transition) {
		CategoryAssignment ca = EcoreUtil.copy(transition.getTypeInstance());
		FaultEventTransition newTransition = new FaultEventTransition(ca);
		return newTransition;
	}
	
	/**
	 * Copies a timed transition
	 * @param transition the transition to copy
	 * @return a copy
	 */
	public TimeoutTransition copyTimeoutTransition(TimeoutTransition transition) {
		CategoryAssignment ca = EcoreUtil.copy(transition.getTypeInstance());
		TimeoutTransition newTransition = new TimeoutTransition(ca);
		return newTransition;
	}

	/**
	 * Creates a set of all repairable events
	 * @param raHolder the recovery automaton data holder
	 * @return a set of all repairable events
	 */
	public Set<FaultTreeNode> computeRepairableEvents(RecoveryAutomatonHolder raHolder) {
		Set<FaultTreeNode> repairableEvents = new HashSet<>();
		for (Transition transition : raHolder.getMapTransitionToTransitionHolder().keySet()) {
			if (transition instanceof FaultEventTransition) {
				FaultEventTransition fte = (FaultEventTransition) transition;
				if (fte.getIsRepair()) {
					repairableEvents.addAll(fte.getGuards());
				}
			}
		}
		
		return repairableEvents;
	}
}
