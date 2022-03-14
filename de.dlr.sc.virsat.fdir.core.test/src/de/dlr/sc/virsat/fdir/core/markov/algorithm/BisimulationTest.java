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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import org.eclipse.core.runtime.SubMonitor;


/**
 * Test Class for Bisimulation of state type S that extends MarkovState
 * 
 * @author rama_vi
 *
 */
public class BisimulationTest {
	SubMonitor subMonitor;

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

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor))));

		ma.addNondeterministicTransition("b", bisimilar1, successor);

		equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

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

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1, successor2))));

		ma.addNondeterministicTransition("b", successor1, bisimilar1);

		equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

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

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1, successor2))));

		ma.addNondeterministicTransition("b", bisimilar1, successor1);

		equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1, successor2))));

	}

	@Test
	/**
	 * This method also tests the bisimilarity with 2 predecessors and 2 successors
	 * but with different transitions
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

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

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

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor3))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor4))));
	}
	
	/**
	 * This method tests the duplicacy of the same labels
	 */
	@Test
	public void testlabelduplicacy() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState b1 = new MarkovState();
		MarkovState b2 = new MarkovState();
		MarkovState s1 = new MarkovState();
		MarkovState s2 = new MarkovState();
		MarkovState s3 = new MarkovState();

		ma.addState(b1);
		ma.addState(s1);
		ma.addState(s2);
		ma.addState(b2);
		ma.addState(s3);

		ma.addNondeterministicTransition("a", b1, s1);
		ma.addNondeterministicTransition("a", b1, s2);
		ma.addNondeterministicTransition("a", b2, s3);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceclasses = bisimulation.computeEquivalenceClasses(subMonitor);
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(b1, b2))));
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(s1, s2, s3))));
	}
	
	@Test
	/**
	 * This method test the ComputeQuotient method which contracts all
	 * equivalenceClasses States to a single representative State
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
		ma.addNondeterministicTransition("b", s1, s3);
		ma.addNondeterministicTransition("b", s2, s6);
		ma.addNondeterministicTransition("c", s1, s4);
		ma.addNondeterministicTransition("c", s2, s5);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		bisimulation.computeQuotient(subMonitor);

		List<MarkovState> states = ma.getStates();
		final int expectedStates = 3;
		List<Object> stateLabelsS0 = ma.getSuccEvents(s0);
		MarkovState s12 = states.contains(s1) ? s1 : s2;
		List<Object> stateLabelsS12 = ma.getSuccEvents(s12);
		MarkovState s3456 = states.contains(s3) ? s3 : states.contains(s4) ? s4 : states.contains(s5) ? s5 : s6;
		List<Object> stateIncomingLabelsS3456 = ma.getPredEvents(s3456);

		assertTrue(stateLabelsS0.containsAll(Arrays.asList("a")));
		assertTrue(stateLabelsS12.containsAll(Arrays.asList("a", "b", "c")));
		assertTrue(stateIncomingLabelsS3456.containsAll(Arrays.asList("b", "c")));
		assertEquals(expectedStates, states.size());
	}

	@Test
	/**
	 * This method also tests the ComputeQuotient method
	 */
	public void testComputeQuotient2() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState b1 = new MarkovState();
        MarkovState b2 = new MarkovState();
		MarkovState s1 = new MarkovState();
		MarkovState s2 = new MarkovState();
		MarkovState s3 = new MarkovState();
		MarkovState s4 = new MarkovState();
		MarkovState t1 = new MarkovState();
		MarkovState t2 = new MarkovState();

		ma.addState(b1);
		ma.addState(s1);
		ma.addState(s2);
		ma.addState(t1);
		ma.addState(t2);
		ma.addState(b2);
		ma.addState(s3);
		ma.addState(s4);

		ma.addNondeterministicTransition("a", b1, s1);
		ma.addNondeterministicTransition("a", b1, s2);
		ma.addNondeterministicTransition("a", b2, s3);
		ma.addNondeterministicTransition("a", b2, s4);
		ma.addNondeterministicTransition("b", s1, b1);
		ma.addNondeterministicTransition("b", s2, b1);
		ma.addNondeterministicTransition("b", s3, b2);
		ma.addNondeterministicTransition("c", s1, t1);
		ma.addNondeterministicTransition("c", s2, t2);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		bisimulation.computeQuotient(subMonitor);

		List<MarkovState> states = ma.getStates();
		int statesCount = states.size();
		final int requiredFinalStates = 5;
		List<Object> stateLabelsB1 = ma.getSuccEvents(b1);
		List<Object> stateLabelsB2 = ma.getSuccEvents(b2);
		MarkovState stateS12 = states.contains(s1) ? s1 : s2;
		List<Object> stateLabelsS12 = ma.getSuccEvents(stateS12);
		List<Object> stateLabelsS3 = ma.getSuccEvents(s3);
		MarkovState stateT12S4 = states.contains(t1) ? t1 : states.contains(t2) ? t2 : s4;
		List<Object> stateLabelsT12S4 = ma.getPredEvents(stateT12S4);

		assertTrue(stateLabelsB1.containsAll(Arrays.asList("a")));
		assertTrue(stateLabelsB2.containsAll(Arrays.asList("a", "a")));
		assertTrue(stateLabelsS12.containsAll(Arrays.asList("b", "c")));
		assertTrue(stateLabelsS3.containsAll(Arrays.asList("b")));
		assertTrue(stateLabelsT12S4.containsAll(Arrays.asList("c", "a")));
		assertEquals(requiredFinalStates, statesCount);
	}

	@Test
	/**
	 * This method also tests the ComputeQuotient method with 1 predecessor
	 * and 2 successor states
	 */
	public void testComputeQuotient3() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<MarkovState>();
		
		MarkovState b = new MarkovState();
		MarkovState s1 = new MarkovState();
		MarkovState s2 = new MarkovState();

		ma.addState(b);
		ma.addState(s1);
		ma.addState(s2);

		ma.addNondeterministicTransition("a", b, s1);
		ma.addNondeterministicTransition("b", b, s2);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		bisimulation.computeQuotient(subMonitor);

		List<MarkovState> states = ma.getStates();
		int statesCount = states.size();
		int finalStatesCount = 2;
		List<Object> stateLabelsB = ma.getSuccEvents(b);
		List<Object> stateLabelsS12 = ma.getPredEvents(states.contains(s1) ? s1 : s2);

		assertTrue(stateLabelsB.containsAll(Arrays.asList("a", "b")));
		assertTrue(stateLabelsS12.containsAll(Arrays.asList("a", "b")));
		assertEquals(statesCount, finalStatesCount);
	}
	
	@Test
	public void testBisimulationForProbabilisticTransition1() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<MarkovState>();
		
		MarkovState b1 = new MarkovState();
		MarkovState b2 = new MarkovState();
		MarkovState s1 = new MarkovState();
		
		ma.addState(b1);
		ma.addState(b2);
		ma.addState(s1);
		
		ma.addProbabilisticTransition("a", b1, s1, 1);
		ma.addProbabilisticTransition("a", b2, s1, 0);
		
		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceclasses = bisimulation.computeEquivalenceClasses(subMonitor);
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(b1))));
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(b2))));
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(s1))));	
	}
	@Test
	public void testBisimulationForProbabilisticTransition2() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState successor1 = new MarkovState();
		MarkovState successor2 = new MarkovState();
		MarkovState successor3 = new MarkovState();

		ma.addState(bisimilar1);
		ma.addState(successor1);
		ma.addState(successor2);
		ma.addState(successor3);

		ma.addProbabilisticTransition("a", bisimilar1, successor1, 0);
		ma.addProbabilisticTransition("a", bisimilar1, successor2, 0);
		ma.addProbabilisticTransition("a", successor1, successor3, 0);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2, successor3))));
		
	}
	@Test
	public void testComputeQuotientForPropabilisticTransition() {
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

		ma.addProbabilisticTransition("a", s0, s1, 0);
		ma.addProbabilisticTransition("a", s0, s2, 0);
		ma.addProbabilisticTransition("a", s1, s0, 0);
		ma.addProbabilisticTransition("a", s2, s0, 1);
		ma.addProbabilisticTransition("b", s1, s3, 0);
		ma.addProbabilisticTransition("b", s2, s6, 1);
		ma.addProbabilisticTransition("c", s1, s4, 0);
		ma.addProbabilisticTransition("c", s2, s5, 0);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);
		
		
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s0))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s3, s4, s5, s6))));
		
		bisimulation.computeQuotient(subMonitor);

		List<MarkovState> states = ma.getStates();
		final int expectedStates = 4;
		List<Object> stateLabelsS0 = ma.getSuccEvents(s0);
		List<Object> stateLabelsS1 = ma.getSuccEvents(s1);
		List<Object> stateLabelsS2 = ma.getSuccEvents(s2);
		MarkovState s3456 = states.contains(s3) ? s3 : states.contains(s4) ? s4 : states.contains(s5) ? s5 : s6;
		List<Object> stateIncomingLabelsS3456 = ma.getPredEvents(s3456);

		assertTrue(stateLabelsS0.containsAll(Arrays.asList("a", "a")));
		assertTrue(stateLabelsS1.containsAll(Arrays.asList("a", "b", "c")));
		assertTrue(stateLabelsS2.containsAll(Arrays.asList("a", "b", "c")));
		assertTrue(stateIncomingLabelsS3456.containsAll(Arrays.asList("b", "c")));
		assertEquals(expectedStates, states.size());
	}
	
	@Test
	public void testComputeQuotientForPropabilisticTransition2() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState s0 = new MarkovState();
		MarkovState s1 = new MarkovState();
		MarkovState s2 = new MarkovState();
		MarkovState s3 = new MarkovState();
		MarkovState s4 = new MarkovState();
		MarkovState s5 = new MarkovState();

		ma.addState(s0);
		ma.addState(s1);
		ma.addState(s2);
		ma.addState(s3);
		ma.addState(s4);
		ma.addState(s5);

		ma.addProbabilisticTransition("f(a)", s0, s1, 1);
		ma.addProbabilisticTransition("f(p)", s1, s2, 0);
		ma.addProbabilisticTransition("not-f(p)", s1, s3, 1);
		ma.addProbabilisticTransition("r(a)", s2, s4, 0);
		ma.addProbabilisticTransition("r(a)", s3, s5, 0);
		ma.addProbabilisticTransition("f(a)", s5, s1, 1);
		ma.addProbabilisticTransition("f(a)", s4, s2, 1);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);
		
		
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s0, s5))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s3))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s4))));
		
		bisimulation.computeQuotient(subMonitor);

		List<MarkovState> states = ma.getStates();
		final int expectedStates = 5;
		List<Object> stateLabelsS1 = ma.getSuccEvents(s1);
		List<Object> stateLabelsS2 = ma.getSuccEvents(s2);
		List<Object> stateLabelsS3 = ma.getSuccEvents(s3);
		List<Object> stateLabelsS4 = ma.getSuccEvents(s4);
		MarkovState s05 = states.contains(s0) ? s0 : s5;
		List<Object> stateIncomingLabelsS05 = ma.getPredEvents(s05);

		assertTrue(stateLabelsS1.containsAll(Arrays.asList("f(p)", "not-f(p)")));
		assertTrue(stateLabelsS2.containsAll(Arrays.asList("r(a)")));
		assertTrue(stateLabelsS3.containsAll(Arrays.asList("r(a)")));
		assertTrue(stateLabelsS4.containsAll(Arrays.asList("f(a)")));
		assertTrue(stateIncomingLabelsS05.containsAll(Arrays.asList("r(a)")));
		assertEquals(expectedStates, states.size());
	}
	
	
	@Test
	public void testBisimulationForMarkovianTransition1() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<MarkovState>();
		
		MarkovState b1 = new MarkovState();
		MarkovState b2 = new MarkovState();
		MarkovState s1 = new MarkovState();
		
		ma.addState(b1);
		ma.addState(b2);
		ma.addState(s1);
		
		ma.addMarkovianTransition("a", b1, s1, 1);
		ma.addMarkovianTransition("a", b2, s1, 2);
		
		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceclasses = bisimulation.computeEquivalenceClasses(subMonitor);
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(b1))));
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(b2))));
		assertTrue(equivalenceclasses.contains(new HashSet<>(Arrays.asList(s1))));
		
	}
	
	@Test
	public void testComputeQuotientForMarkovianTransition() {
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

		ma.addMarkovianTransition("a", s0, s1, 0);
		ma.addMarkovianTransition("a", s0, s2, 0);
		ma.addMarkovianTransition("a", s1, s0, 1);
		ma.addMarkovianTransition("a", s2, s0, 2);
		ma.addMarkovianTransition("b", s1, s3, 2);
		ma.addMarkovianTransition("b", s2, s6, 1);
		ma.addMarkovianTransition("c", s1, s4, 0);
		ma.addMarkovianTransition("c", s2, s5, 0);

		Bisimulation<MarkovState> bisimulation = new Bisimulation<>(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses(subMonitor);
		
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s0))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(s3, s4, s5, s6))));
		
		bisimulation.computeQuotient(subMonitor);

		List<MarkovState> states = ma.getStates();
		final int expectedStates = 4;
		List<Object> stateLabelsS0 = ma.getSuccEvents(s0);
		List<Object> stateLabelsS1 = ma.getSuccEvents(s1);
		List<Object> stateLabelsS2 = ma.getSuccEvents(s2);
		MarkovState s3456 = states.contains(s3) ? s3 : states.contains(s4) ? s4 : states.contains(s5) ? s5 : s6;
		List<Object> stateIncomingLabelsS3456 = ma.getPredEvents(s3456);

		assertTrue(stateLabelsS0.containsAll(Arrays.asList("a", "a")));
		assertTrue(stateLabelsS1.containsAll(Arrays.asList("a", "b", "c")));
		assertTrue(stateLabelsS2.containsAll(Arrays.asList("a", "b", "c")));
		assertTrue(stateIncomingLabelsS3456.containsAll(Arrays.asList("b", "c")));
		assertEquals(expectedStates, states.size());
	}

}