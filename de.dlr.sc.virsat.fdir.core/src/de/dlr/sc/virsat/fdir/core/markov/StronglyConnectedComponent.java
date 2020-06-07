package de.dlr.sc.virsat.fdir.core.markov;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StronglyConnectedComponent<S extends MarkovState> {
	private Set<S> states = new HashSet<>();
	private MarkovAutomaton<S> ma;
	
	public StronglyConnectedComponent(MarkovAutomaton<S> ma) {
		this.ma = ma;
	}
	
	public Set<S> getStates() {
		return states;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StronglyConnectedComponent) {
			StronglyConnectedComponent<?> sccOther = (StronglyConnectedComponent<?>) obj;
			return states.equals(sccOther.getStates()); 
		}
		
		return super.equals(obj);
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
