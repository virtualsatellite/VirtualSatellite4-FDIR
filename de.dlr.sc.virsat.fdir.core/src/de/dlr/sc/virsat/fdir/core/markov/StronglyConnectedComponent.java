package de.dlr.sc.virsat.fdir.core.markov;

import java.util.ArrayList;
import java.util.List;

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
}