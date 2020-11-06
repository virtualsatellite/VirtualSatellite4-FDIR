package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class Bisimulation {
	public Bisimulation(MarkovAutomaton<?> ma) {
		
	}
	
	public void minimize() {
		computeEquivalenceClasses();
		computeQuotient();
	}
	
	public Set<Set<MarkovState>> computeEquivalenceClasses() {
		return null;
	}
	
	public void computeQuotient() {
		
	}
}
