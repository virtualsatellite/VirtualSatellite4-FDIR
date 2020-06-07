package de.dlr.sc.virsat.fdir.core.markov;

import org.junit.Test;

public class StronglyConnectedComponentTest {

	@Test
	public void testIsEndComponent() {
		// Construct the following MA:
		// init --- 3 ---> good1 --- 2 ---> fail1 --- 4 ---> good1
		//          2 ---> good2 --- 5 ---> fail2 --- 0.5 -> good2
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		ma.getEvents().add("m");
		MarkovState init = new MarkovState();
		MarkovState good1 = new MarkovState();
		MarkovState fail1 = new MarkovState();
		MarkovState good2 = new MarkovState();
		MarkovState fail2 = new MarkovState();
		
		ma.addState(init);
		ma.addState(good1);
		ma.addState(fail1);
		ma.addState(good2);
		ma.addState(fail2);
		ma.getFinalStateProbs().put(fail1, 1d);
		ma.getFinalStateProbs().put(fail2, 1d);
		
		final double RATE_INIT_TO_GOOD_1 = 3;
		final double RATE_GOOD_1_TO_FAIL_1 = 2;
		final double RATE_FAIL_1_TO_GOOD_1 = 4;
		
		ma.addMarkovianTransition("m", init, good1, RATE_INIT_TO_GOOD_1);
		ma.addMarkovianTransition("m", good1, fail1, RATE_GOOD_1_TO_FAIL_1);
		ma.addMarkovianTransition("m", fail1, good1, RATE_FAIL_1_TO_GOOD_1);
		
		final double RATE_INIT_TO_GOOD_2 = 2;
		final double RATE_GOOD_2_TO_FAIL_2 = 5;
		final double RATE_FAIL_2_TO_GOOD_2 = 0.5;
		
		ma.addMarkovianTransition("m", init, good2, RATE_INIT_TO_GOOD_2);
		ma.addMarkovianTransition("m", good2, fail2, RATE_GOOD_2_TO_FAIL_2);
		ma.addMarkovianTransition("m", fail2, good2, RATE_FAIL_2_TO_GOOD_2);
		
		StronglyConnectedComponent<MarkovState> sccInit = new StronglyConnectedComponent<>(ma);
		sccInit.getStates().add(init);
		
		StronglyConnectedComponent<MarkovState> scc1 = new StronglyConnectedComponent<>(ma);
		scc1.getStates().add(good1);
		scc1.getStates().add(fail1);
		
		StronglyConnectedComponent<MarkovState> scc2 = new StronglyConnectedComponent<>(ma);
		scc2.getStates().add(good2);
		scc2.getStates().add(fail2);
	}
}
