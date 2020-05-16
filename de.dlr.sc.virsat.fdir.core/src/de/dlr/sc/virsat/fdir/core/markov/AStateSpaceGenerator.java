package de.dlr.sc.virsat.fdir.core.markov;

import java.util.List;

public abstract class AStateSpaceGenerator<S extends MarkovState> {
	
	protected MarkovAutomaton<S> targetMa;
	
	/**
	 * Initializes the state space generator with the given markov automaton
	 * @param ma the markov automaton where the generated states should be put
	 */
	public void init(MarkovAutomaton<S> targetMa) {
		this.targetMa = targetMa;
	}
	
	/**
	 * Creates the initial state
	 * @param ma the markov automaton where the intial state should be created in
	 * @return the new intial belief state
	 */
	public abstract S createInitialState();
	
	/**
	 * Takes a belief state and generates the successor states for it
	 * @param ma the markov automaton where the generated states will be inserted
	 * @param beliefState the belief state to consider
	 * @return the newly generated states
	 */
	public abstract List<S> generateSuccs(S state);
}
