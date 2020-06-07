package de.dlr.sc.virsat.fdir.core.markov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class StronglyConnectedComponent<S extends MarkovState> {
	private Set<S> states = new HashSet<>();
	private MarkovAutomaton<S> ma;
	
	public Set<S> getStates() {
		return states;
	}
	
	@Override
	public boolean equals(Object obj) {
		return states.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return states.hashCode();
	}
	
	public boolean isEndComponent() {
		for (MarkovState state : states) {
			List<MarkovTransition<S>> succs = ma.getSuccTransitions(state);
			for (MarkovTransition<S> succTransition : succs) {
				if (!states.contains(succTransition.getTo())) {
					return false;
				}
			}
		}
		
		return true;
	}
}
