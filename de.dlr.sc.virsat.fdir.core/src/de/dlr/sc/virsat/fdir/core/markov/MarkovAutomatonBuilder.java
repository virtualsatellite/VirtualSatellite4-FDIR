package de.dlr.sc.virsat.fdir.core.markov;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MarkovAutomatonBuilder<S extends MarkovState> {
	
	private AStateSpaceGenerator<S> stateSpaceGenerator;
	private S initialState;
	
	public MarkovAutomatonBuilder(AStateSpaceGenerator<S> stateSpaceGenerator) {
		this.stateSpaceGenerator = stateSpaceGenerator;
	}
	
	/**
	 * Builds a markov automaton using the provided state space generator
	 * @return the new markov automaton
	 */
	public MarkovAutomaton<S> build() {
		MarkovAutomaton<S> ma = new MarkovAutomaton<>();
		stateSpaceGenerator.init(ma);
		
		initialState = stateSpaceGenerator.createInitialState();
		
		Queue<S> toProcess = new LinkedList<>();
		toProcess.offer(initialState);
		
		while (!toProcess.isEmpty()) {
			S state = toProcess.poll();
			List<S> generatedNewSuccs = stateSpaceGenerator.generateSuccs(state);
			toProcess.addAll(generatedNewSuccs);
		}
		
		return ma;
	}
	
	/**
	 * Gets the initial belief state corresponding to the initial Ma state
	 * @return the initial belief state
	 */
	public S getInitialState() {
		return initialState;
	}
}
