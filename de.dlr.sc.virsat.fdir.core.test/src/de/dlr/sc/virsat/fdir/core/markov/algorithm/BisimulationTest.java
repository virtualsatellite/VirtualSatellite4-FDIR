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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

/**
 * Test Class for Bisimulation
 * 
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

}