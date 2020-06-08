/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class MarkovAutomatonValueIteratorTest {

	private class MockIterator implements IMatrixIterator {
		double[] values = { 0, 0, 1};
		
		@Override
		public void iterate() { }

		@Override
		public double[] getValues() {
			return values;
		}
	}
	
	@Test
	public void testIterate() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<MarkovState>();
		
		// Build the following ma:
		// init --- 0.5 ---> nondet --- a ---> fail
		//      <-- b ------ nondet
		//      --- 0.5 ---> fail
		
		MarkovState init = new MarkovState();
		MarkovState nondet = new MarkovState();
		MarkovState fail = new MarkovState();
		
		ma.addState(init);
		ma.addState(nondet);
		ma.addState(fail);
		
		// CHECKSTYLE:OFF
		ma.addProbabilisticTransition("not-p", init, nondet, 0.5);
		ma.addProbabilisticTransition("p", init, fail, 0.5);
		ma.addNondeterministicTransition("a", nondet, fail);
		ma.addNondeterministicTransition("b", nondet, init);
		// CHECKSTYLE:ON
		
		MarkovAutomatonValueIterator<MarkovState> maIteratorMax = new MarkovAutomatonValueIterator<MarkovState>(new MockIterator(), ma);
		maIteratorMax.iterate();
		
		final double EPS = 0.000001;
		final double[] EXPECTED_VALUES_MAX = { 1, 1, 1 }; 
		assertArrayEquals(EXPECTED_VALUES_MAX, maIteratorMax.getValues(), EPS);
		
		MarkovAutomatonValueIterator<MarkovState> maIteratorMin = new MarkovAutomatonValueIterator<MarkovState>(new MockIterator(), ma);
		maIteratorMin.setMaximize(false);
		maIteratorMin.iterate();
		
		final double[] EXPECTED_VALUES_MIN = { 0.5, 0, 1 }; 
		assertArrayEquals(EXPECTED_VALUES_MIN, maIteratorMin.getValues(), EPS);
	}

}
