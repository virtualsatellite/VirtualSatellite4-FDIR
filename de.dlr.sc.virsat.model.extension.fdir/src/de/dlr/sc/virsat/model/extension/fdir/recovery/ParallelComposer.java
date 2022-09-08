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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ComposedPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.PropertyinstancesFactory;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Takes a collection of Recovery Automata and performs parallel construction on them
 * @author jord_ad
 *
 */
public class ParallelComposer {
	
	private static final long MEMORY_THRESHOLD = 1024 * 1024 * 512;
	
	private Map<State, List<Integer>> mapStateToPos;
	private Map<List<Integer>, State> mapPosToState;
	private Map<RecoveryAutomaton, RecoveryAutomatonHolder> mapRAtoRaHolder;
	private Map<State, Integer> mapStateToInt;
	protected Concept concept;
	
	private long maxMemory = Runtime.getRuntime().maxMemory();
	
	/**
	 * Takes a set of Recovery Automata and returns the composed Recovery Automaton
	 * @param ras the set of Recovery Automata
	 * @param concept the concept
	 * @param monitor a monitor
	 * @return the composed recovery automaton
	 */
	public RecoveryAutomaton compose(Set<RecoveryAutomaton> ras, Concept concept, IProgressMonitor monitor) {
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
		this.mapRAtoRaHolder = new HashMap<>();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor);
		
		ras.stream().forEach(ra -> mapRAtoRaHolder.put(ra, new RecoveryAutomatonHolder(ra)));
		
		Map<RecoveryAutomaton, IBeanList<State>> mapRAtoStates = this.createMapRAtoStates(ras);
		indexStates(ras, mapRAtoStates);
		List<Integer> initialPos = ras.stream().map(ra -> 0).collect(Collectors.toList());
		
		RecoveryAutomaton result = new RecoveryAutomaton(concept);
		mapRAtoStates.put(result, result.getStates());
		RecoveryAutomatonHelper rah = new RecoveryAutomatonHelper(concept);
		
		State startState = createNewState(result, initialPos, mapRAtoStates);
		result.setInitial(startState);
		
		Stack<State> dfsStack = new Stack<State>();
		dfsStack.push(startState);
		
		while (!dfsStack.isEmpty()) {
			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			
			long freeMemory = maxMemory - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
			if (freeMemory < MEMORY_THRESHOLD) {
				throw new RuntimeException("Close to out of memory. Aborting so we can still maintain an operational state.");
			}
			
			State fromState = dfsStack.pop();
			List<Integer> fromPos = mapStateToPos.get(fromState);
			
			int currRA = 0;
			for (RecoveryAutomaton ra : ras) {
				Integer fromStateIndex = mapStateToPos.get(fromState).get(currRA);
				State originalFromState = new State(((ComposedPropertyInstance) mapRAtoStates.get(ra)
						.getArrayInstance().getArrayInstances().get(fromStateIndex)).getTypeInstance());
				RecoveryAutomatonHolder originalRah = mapRAtoRaHolder.get(ra);
				
				for (Transition transition : originalRah.getStateHolder(originalFromState).getOutgoingTransitions()) {
					State originalToState = originalRah.getMapTransitionToTransitionHolder().get(transition).getTo();
					int changedNum = mapStateToInt.get(originalToState);
					List<Integer> toPos = new ArrayList<>(fromPos);
					toPos.set(currRA, changedNum);
					
					State toState = mapPosToState.get(toPos);
					if (toState == null) {
						toState = createNewState(result, toPos, mapRAtoStates);
						dfsStack.push(toState);
					}
					
					Transition copiedTransition = null;
					if (transition instanceof FaultEventTransition) {
						copiedTransition = rah.copyFaultEventTransition((FaultEventTransition) transition);
					} else if (transition instanceof TimeoutTransition) {
						copiedTransition = rah.copyTimeoutTransition((TimeoutTransition) transition);
					} else {
						throw new RuntimeException("Unknown transition type " +  transition);
					}
					
					copiedTransition.getFromBean().setValue(fromState);
					copiedTransition.getToBean().setValue(toState);
					
					// Low level add to the transitions to avoid bean overhead
					ComposedPropertyInstance cpi = PropertyinstancesFactory.eINSTANCE.createComposedPropertyInstance();
					ArrayInstance ai = result.getTransitionsBean().getArrayInstance();
					cpi.setTypeInstance(copiedTransition.getTypeInstance());
					cpi.setType(ai.getType());
					ai.getArrayInstances().add(cpi);
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
	private State createNewState(RecoveryAutomaton ra, List<Integer> pos, Map<RecoveryAutomaton, IBeanList<State>> mapRAtoState) {			
		State newState = new State(concept);
		newState.setName("q_" + pos.stream().map(Object::toString).collect(Collectors.joining()));
		mapStateToPos.put(newState, pos);
		mapPosToState.put(pos, newState);
		
		// Low level add to the state to avoid bean overhead
		ArrayInstance ai = mapRAtoState.get(ra).getArrayInstance();
		ComposedPropertyInstance cpi = PropertyinstancesFactory.eINSTANCE.createComposedPropertyInstance();
		cpi.setTypeInstance(newState.getTypeInstance());
		cpi.setType(ai.getType());
		ai.getArrayInstances().add(cpi);
		return newState;
	}
	
	/**
	 * Add recovery automata to map and rename all the states for future lookup
	 * @param ras the set of recovery automata
	 * @param mapRAtoState map of RA to its states
	 */
	private void indexStates(Set<RecoveryAutomaton> ras, Map<RecoveryAutomaton, IBeanList<State>> mapRAtoState) {
		mapStateToInt = new HashMap<State, Integer>();
		
		for (RecoveryAutomaton ra : ras) {
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
	private Map<RecoveryAutomaton, IBeanList<State>> createMapRAtoStates(Set<RecoveryAutomaton> ras) {
		Map<RecoveryAutomaton, IBeanList<State>> mapRAtoStates = new HashMap<RecoveryAutomaton, IBeanList<State>>();
		ras.forEach(ra -> mapRAtoStates.put(ra, ra.getStates()));
		return mapRAtoStates;
	}

}
