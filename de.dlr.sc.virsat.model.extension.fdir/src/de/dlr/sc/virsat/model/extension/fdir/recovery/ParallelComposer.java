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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
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
	
	private Map<State, String> mapStateToPos;
	private Map<String, State> mapPosToState;
	private Map<RecoveryAutomaton, Map<State, List<Transition>>> mapRAtoTransitionMap;
	private Map<State, Integer> mapStateToInt;
	protected Concept concept;
	
	/**
	 * Takes a set of Recovery Automata and returns the composed Recovery Automaton
	 * @param ras the set of Recovery Automata
	 * @return the composed recovery automaton
	 */
	public RecoveryAutomaton compose(Set<RecoveryAutomaton> ras) {
		if (ras == null || ras.isEmpty()) {
			return null;
		} else if (ras.size() == 1) {
			return ras.iterator().next();
		}
		
		this.mapStateToPos = new HashMap<State, String>();
		this.mapPosToState = new HashMap<String, State>();
		this.mapRAtoTransitionMap = new HashMap<RecoveryAutomaton, Map<State, List<Transition>>>();
		this.concept = ras.iterator().next().getConcept();
		
		indexStates(ras);
		
		int numRAs = ras.size();
		String initialPos = new String(new char[numRAs]).replace('\0', '0');
		
		RecoveryAutomaton result = new RecoveryAutomaton(concept);
		RecoveryAutomatonHelper rah = new RecoveryAutomatonHelper(concept);
		
		State startState = createNewState(result, initialPos);
		
		Stack<State> dfsStack = new Stack<State>();
		dfsStack.push(startState);

		while (!dfsStack.isEmpty()) {
			State fromState = dfsStack.pop();
			String fromPos = mapStateToPos.get(fromState);
			
			int currRA = 0;
			for (RecoveryAutomaton ra : ras) {
				String fromStateName = Character.toString(mapStateToPos.get(fromState).charAt(currRA));
				Integer fromStateIndex = Integer.parseInt(fromStateName);
				State originalFromState = ra.getStates().get(fromStateIndex);
				
				String toPos = fromPos;
				for (Transition t : mapRAtoTransitionMap.get(ra).get(originalFromState)) {
					int changedNum = mapStateToInt.get(t.getTo());
					toPos = toPos.substring(0, currRA) + changedNum + toPos.substring(currRA + 1);
					
					State stateInMap = mapPosToState.get(toPos);
					
					State toState;
					if (stateInMap == null) {
						toState = createNewState(result, toPos);
						dfsStack.push(toState);
					} else {
						toState =  stateInMap;
					}
					Transition transition = rah.createTransition(result, fromState, toState);
					transition.setIsRepair(t.getIsRepair());
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
	 * @return a new State
	 */
	private State createNewState(RecoveryAutomaton ra, String pos) {			
		State newState = new State(concept);
		newState.setName(pos);
		mapStateToPos.put(newState, pos);
		mapPosToState.put(pos, newState);
		ra.getStates().add(newState);
		return newState;
	}
	
	/**
	 * Add recovery automata to map and rename all the states for future lookup
	 * @param ras the set of recovery automata
	 */
	private void indexStates(Set<RecoveryAutomaton> ras) {
		mapStateToInt = new HashMap<State, Integer>();
		
		RecoveryAutomatonHelper rah = new RecoveryAutomatonHelper(concept);
		for (RecoveryAutomaton ra : ras) {
			mapRAtoTransitionMap.put(ra, rah.getCurrentTransitions(ra));
			
			for (int i = 0; i < ra.getStates().size(); ++i) {
				mapStateToInt.put(ra.getStates().get(i), i);
			}
		}
	}

}
