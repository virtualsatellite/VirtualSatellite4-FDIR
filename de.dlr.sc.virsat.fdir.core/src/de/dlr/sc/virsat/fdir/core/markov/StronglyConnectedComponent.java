/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class manages a strongly connected component (SCC) of a markov automaton.
 * A strongly connected component is a set of states where each state can
 * be reached from any other state. 
 * 
 * @author muel_s8
 *
 */

public class StronglyConnectedComponent {
	private List<MarkovState> states = new ArrayList<>();
	private MarkovAutomaton<? extends MarkovState> ma;
	
	public StronglyConnectedComponent(MarkovAutomaton<? extends MarkovState> ma) {
		this.ma = ma;
	}
	
	public List<MarkovState> getStates() {
		return states;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StronglyConnectedComponent) {
			StronglyConnectedComponent sccOther = (StronglyConnectedComponent) obj;
			return states.equals(sccOther.getStates()); 
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return states.hashCode();
	}
	
	/**
	 * Checks if this SCC is an end component.
	 * An SCC is a end component if no state has any outgoing transition to state that is
	 * not in this SCC.
	 * 
	 * @return true iff the SCC is an end component
	 */
	public boolean isEndComponent() {
		for (MarkovState state : states) {
			List<?> succs = ma.getSuccTransitions(state);
			for (Object succTransitionObject : succs) {
				MarkovTransition<?> succTransition = (MarkovTransition<?>) succTransitionObject;
				if (!states.contains(succTransition.getTo())) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Computes the state union for a given collection of strongly connected components.
	 * @param sccs the strongly connected components
	 * @return the state union
	 */
	public static Set<MarkovState> union(Collection<StronglyConnectedComponent> sccs) {
		Set<MarkovState> sccStates = new HashSet<>();
		for (StronglyConnectedComponent scc : sccs) {
			sccStates.addAll(scc.getStates());
		}
		
		return sccStates;
	}
}