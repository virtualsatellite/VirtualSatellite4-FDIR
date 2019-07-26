/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * Takes a collection of Recovery Automata and performs parallel construction on them
 * @author jord_ad
 *
 */
public class ParallelComposer {
	
	private Map<State, List<Integer>> mapStateToPos;
	private Map<List<Integer>, State> mapPosToState;
	private Map<RecoveryAutomaton, Map<State, List<Transition>>> mapRAtoTransitionMap;
	private Map<State, Integer> mapStateToInt;
	protected Concept concept;
	
	/**
	 * Takes a set of Recovery Automata and returns the composed Recovery Automaton
	 * @param ras the set of Recovery Automata
	 * @param concept the concept
	 * @return the composed recovery automaton
	 */
	public RecoveryAutomaton compose(Set<RecoveryAutomaton> ras, Concept concept) {
		this.concept = concept;
		if (ras == null) {
			return null;
		} else if (ras.isEmpty()) {
			RecoveryAutomaton ra = new RecoveryAutomaton(concept);
			State state = new State(concept);
			ra.getStates().add(state);
			ra.setInitial(state);
			return ra;
		} else if (ras.size() == 1) {
			return ras.iterator().next();
		}
		
		this.mapStateToPos = new HashMap<>();
		this.mapPosToState = new HashMap<>();
		this.mapRAtoTransitionMap = new HashMap<>();
		
		Map<RecoveryAutomaton, List<State>> mapRAtoStates = this.createMapRAtoStates(ras);
		indexStates(ras, mapRAtoStates);
		List<Integer> initialPos = ras.stream().map(ra -> 0).collect(Collectors.toList());
		
		RecoveryAutomaton result = new RecoveryAutomaton(concept);
		mapRAtoStates.put(result, result.getStates());
		List<Transition> resultTransitions = result.getTransitions();
		RecoveryAutomatonHelper rah = new RecoveryAutomatonHelper(concept);
		
		State startState = createNewState(result, initialPos, mapRAtoStates);
		result.setInitial(startState);
		
		Stack<State> dfsStack = new Stack<State>();
		dfsStack.push(startState);

		while (!dfsStack.isEmpty()) {
			State fromState = dfsStack.pop();
			List<Integer> fromPos = mapStateToPos.get(fromState);
			
			int currRA = 0;
			for (RecoveryAutomaton ra : ras) {
				Integer fromStateIndex = mapStateToPos.get(fromState).get(currRA);
				State originalFromState = mapRAtoStates.get(ra).get(fromStateIndex);
				
				for (Transition t : mapRAtoTransitionMap.get(ra).get(originalFromState)) {
					int changedNum = mapStateToInt.get(t.getTo());
					List<Integer> toPos = new ArrayList<>(fromPos);
					toPos.set(currRA, changedNum);
					
					State toState = mapPosToState.get(toPos);
					if (toState == null) {
						toState = createNewState(result, toPos, mapRAtoStates);
						dfsStack.push(toState);
					}
					
					if (t instanceof FaultEventTransition) {
						FaultEventTransition copiedTransition = rah.copyFaultEventTransition((FaultEventTransition) t);
						copiedTransition.setFrom(fromState);
						copiedTransition.setTo(toState);
						resultTransitions.add(copiedTransition);
					}
					
				}
				currRA++;
			}	
		}
		return result;
	}
	
	/**
	 * create a new state
	 * @param ra the recovery automaton you wish to add the state to
	 * @param pos the coordinates of the state in the new recovery automaton
	 * @param mapRAtoState map of RA to states
	 * @return a new State
	 */
	private State createNewState(RecoveryAutomaton ra, List<Integer> pos, Map<RecoveryAutomaton, List<State>> mapRAtoState) {			
		State newState = new State(concept);
		newState.setName(pos.stream().map(Object::toString).collect(Collectors.joining()));
		mapStateToPos.put(newState, pos);
		mapPosToState.put(pos, newState);
		mapRAtoState.get(ra).add(newState);
		return newState;
	}
	
	/**
	 * Add recovery automata to map and rename all the states for future lookup
	 * @param ras the set of recovery automata
	 * @param mapRAtoState map of RA to its states
	 */
	private void indexStates(Set<RecoveryAutomaton> ras, Map<RecoveryAutomaton, List<State>> mapRAtoState) {
		mapStateToInt = new HashMap<State, Integer>();
		
		RecoveryAutomatonHelper rah = new RecoveryAutomatonHelper(concept);
		for (RecoveryAutomaton ra : ras) {
			mapRAtoTransitionMap.put(ra, rah.getCurrentTransitions(ra));
			
			for (int i = 0; i < mapRAtoState.get(ra).size(); ++i) {
				mapStateToInt.put(mapRAtoState.get(ra).get(i), i);
			}
		}
	}
	
	/**
	 * Get the states for each RA and save them in a map
	 * @param ras the set of RAs
	 * @return the map of each RA to its states
	 */
	private Map<RecoveryAutomaton, List<State>> createMapRAtoStates(Set<RecoveryAutomaton> ras) {
		Map<RecoveryAutomaton, List<State>> mapRAtoStates = new HashMap<RecoveryAutomaton, List<State>>();
		ras.forEach(ra -> mapRAtoStates.put(ra, ra.getStates()));
		return mapRAtoStates;
	}

}
