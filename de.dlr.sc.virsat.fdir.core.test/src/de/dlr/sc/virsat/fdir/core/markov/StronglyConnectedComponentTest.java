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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StronglyConnectedComponentTest {

	@Test
	public void testIsEndComponent() {
		// Construct the following MA:
		// init --- 3 ---> good1 --- 2 ---> fail1 --- 4 ---> good1
		
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
		
		StronglyConnectedComponent sccInit = new StronglyConnectedComponent(ma);
		sccInit.getStates().add(init);
		
		StronglyConnectedComponent scc1 = new StronglyConnectedComponent(ma);
		scc1.getStates().add(good1);
		scc1.getStates().add(fail1);
		
		assertFalse(sccInit.isEndComponent());
		assertTrue(scc1.isEndComponent());
	}
}
