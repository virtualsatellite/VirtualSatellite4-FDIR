/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;


/**
 * Test Class for Bisimulation
 * @author rama_vi
 *
 */
public class BisimulationTest {

	@Test
	/**
	 * This method tests the bisimilarity of 2 predecessor states with a successor
	 * state
	 */
	public void testComputeEquivalenceClassesDirectlyByPredecessor() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(bisimilar2);
		ma.addState(successor);

		ma.addNondeterministicTransition("a", bisimilar1, successor);
		ma.addNondeterministicTransition("a", bisimilar2, successor);

		Bisimulation bisimulation = new Bisimulation(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor))));

		ma.addNondeterministicTransition("b", bisimilar1, successor);

		equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor))));

	}

	@Test
	/**
	 * This method tests the bisimilarity of a predecessor state with 2 successor
	 * states
	 */
	public void testComputeEquivalenceClassesDirectlyBySuccessor() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState successor1 = new MarkovState();
		MarkovState successor2 = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(successor1);
		ma.addState(successor2);

		ma.addNondeterministicTransition("a", bisimilar1, successor1);
		ma.addNondeterministicTransition("a", bisimilar1, successor2);

		Bisimulation bisimulation = new Bisimulation(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1, successor2))));

		ma.addNondeterministicTransition("b", successor1, bisimilar1);

		equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2))));

	}

	@Test
	/**
	 * This method tests the bisimilarity with 2 predecessors and 2 successors
	 * before and after refinement
	 */
	public void testComputeEquivalenceClassesDirectlyByPredecessorAndSuccessor() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor1 = new MarkovState();
		MarkovState successor2 = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(bisimilar2);
		ma.addState(successor1);
		ma.addState(successor2);

		ma.addNondeterministicTransition("a", bisimilar1, successor1);
		ma.addNondeterministicTransition("a", bisimilar2, successor2);

		Bisimulation bisimulation = new Bisimulation(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1, successor2))));

		ma.addNondeterministicTransition("b", bisimilar1, successor1);

		equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2))));

	}

	@Test
	/**
	 * This method also tests the bisimilarity with 2 predecessors and 2 successors but with different transitions
	 */
	public void testComputeEquivalenceClassesDirectlyByPredecessorAndSuccessorRefinementTest1() {

		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<MarkovState>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor1 = new MarkovState();
		MarkovState successor2 = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(bisimilar2);
		ma.addState(successor1);
		ma.addState(successor2);

		ma.addNondeterministicTransition("a", bisimilar1, successor1);
		ma.addNondeterministicTransition("a", bisimilar2, successor2);
		ma.addNondeterministicTransition("b", successor1, bisimilar1);
		ma.addNondeterministicTransition("b", successor2, successor2);

		Bisimulation bisimulation = new Bisimulation(ma);

		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2))));

	}

	@Test
	/**
	 * This method tests the bisimilarity with 2 predecessor and 4 successor states
	 */
	public void testComputeEquivalenceClassesDirectlyByPredecessorandSuccessorRefinementTest2() {

		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<MarkovState>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor1 = new MarkovState();
		MarkovState successor2 = new MarkovState();
		MarkovState successor3 = new MarkovState();
		MarkovState successor4 = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(bisimilar2);
		ma.addState(successor1);
		ma.addState(successor2);
		ma.addState(successor3);
		ma.addState(successor4);

		ma.addNondeterministicTransition("a", bisimilar1, successor1);
		ma.addNondeterministicTransition("b", bisimilar1, successor2);
		ma.addNondeterministicTransition("a", bisimilar2, successor3);
		ma.addNondeterministicTransition("b", bisimilar2, successor4);
		ma.addNondeterministicTransition("c", successor1, successor1);
		ma.addNondeterministicTransition("d", successor2, bisimilar1);
		ma.addNondeterministicTransition("c", successor3, bisimilar2);
		ma.addNondeterministicTransition("d", successor4, successor4);

		Bisimulation bisimulation = new Bisimulation(ma);

		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor3))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor4))));

	}
	
	@Test
	/**
	 * This method test the ComputeQuotient method which contracts all equivalenceClasses 
	 * States to a single representative State
	 */
	public void testComputeQuotient1() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState s0 = new MarkovState();
		MarkovState s1 = new MarkovState();
		MarkovState s2 = new MarkovState();
		MarkovState s3 = new MarkovState();
		MarkovState s4 = new MarkovState();
		MarkovState s5 = new MarkovState();
		MarkovState s6 = new MarkovState();
		
		ma.addState(s0);
		ma.addState(s1);
		ma.addState(s2);
		ma.addState(s3);
		ma.addState(s4);
		ma.addState(s5);
		ma.addState(s6);
		
		ma.addNondeterministicTransition("a", s0, s1);
		ma.addNondeterministicTransition("a", s0, s2);
		ma.addNondeterministicTransition("a", s1, s0);
		ma.addNondeterministicTransition("a", s2, s0);
		ma.addNondeterministicTransition("b", s0, s0);
		ma.addNondeterministicTransition("b", s1, s3);
		ma.addNondeterministicTransition("b", s2, s6);
		ma.addNondeterministicTransition("c", s1, s4);
		ma.addNondeterministicTransition("c", s2, s5);
		
		Bisimulation bisimulation = new Bisimulation(ma);
		bisimulation.computeQuotient();
		List<MarkovState> states = ma.getStates();
		
		assertTrue(states.contains(s0));
		assertTrue(states.contains(s1) || states.contains(s2));
		assertTrue(states.contains(s3) || states.contains(s6));
		assertTrue(states.contains(s4) || states.contains(s5));
		
		
		
		
		
	}

	@Test
	/**
	 * This method also tests the ComputeQuotient method
	 */
	public void testComputeQuotient2() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState b1 = new MarkovState();
		MarkovState s1 = new MarkovState();
		MarkovState s2 = new MarkovState();
		MarkovState t1 = new MarkovState();
		MarkovState t2 = new MarkovState();
		
		ma.addState(b1);
		ma.addState(s1);
		ma.addState(s2);
		ma.addState(t1);
		ma.addState(t2);
		
		ma.addNondeterministicTransition("a", b1, s1);
		ma.addNondeterministicTransition("a", b1, s2);
		ma.addNondeterministicTransition("b", s1, b1);
		ma.addNondeterministicTransition("b", s2, b1);
		ma.addNondeterministicTransition("c", s1, t1);
		ma.addNondeterministicTransition("c", s2, t2);
		
		Bisimulation bisimulation = new Bisimulation(ma);
		bisimulation.computeQuotient();
		List<MarkovState> states = ma.getStates();
//		List<Object> stateLabels = new ArrayList<Object>();
//		
//		for (MarkovState state : states) {
//			
//			List<MarkovTransition<MarkovState>> stateTransitions = ma.getSuccTransitions(state);
//			
//			for (MarkovTransition<MarkovState> statetransition : stateTransitions) {
//
//				stateLabels.add(statetransition.getEvent());
//			}
//			
//			if (state==s1) {
//				a = ma.get
//				assertTrue(a.equals(stateLabels));
//			}
//			
//		}
			
		
		
		assertTrue(states.contains(b1));
		assertTrue(states.contains(s1) || states.contains(s2));
		assertFalse(states.contains(s1) && states.contains(s2));
		assertTrue(states.contains(t2) || states.contains(t1));
		assertFalse(states.contains(t2) && states.contains(t1));
		
		
	}
//	@Test

	/**
	 * This method tests the bisimilarity with Propabilistic transitions
	 */
	public void testComputeEquivalenceClassesDirectlyByPredecessorPT() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor1 = new MarkovState();
		MarkovState successor2 = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(bisimilar2);
		ma.addState(successor1);
		ma.addState(successor2);

		ma.addProbabilisticTransition("1", bisimilar1, successor1, 1);
		ma.addProbabilisticTransition("2", bisimilar2, successor1, 1);

		Bisimulation bisimulation = new Bisimulation(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));

		ma.addProbabilisticTransition("3", bisimilar1, successor2, 1);

		equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1, successor2))));

	}

}