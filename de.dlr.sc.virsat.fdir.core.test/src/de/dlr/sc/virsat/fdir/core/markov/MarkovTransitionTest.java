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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarkovTransitionTest {

	//CHECKSTYLE:OFF
	private static class MockState extends MarkovState {
		public MockState(int index) {
			this.index = index;
		}
	}
	//CHECKSTYLE:ON
	
	@Test
	public void testToString() {
		MarkovState from = new MockState(0);
		MarkovState to = new MockState(1);
		
		MarkovTransition<MarkovState> transition = new MarkovTransition<MarkovState>(from, to, 1, "a", true);
		String stringRepresentation = transition.toString();
		
		assertEquals("Correct string representation", "0 --- a, 1.0 ---> 1", stringRepresentation);
	}
}
