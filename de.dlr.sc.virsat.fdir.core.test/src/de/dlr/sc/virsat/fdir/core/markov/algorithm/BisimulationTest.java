package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class BisimulationTest {

	@Test
	public void testComputeEquivalenceClassesDirectlyByPredecessor() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();

		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor  = new MarkovState();

        ma.addState(bisimilar1);
        ma.addState(bisimilar2);
        ma.addState(successor);
        
		ma.addNondeterministicTransition("a", bisimilar1, successor);
		ma.addNondeterministicTransition("a", bisimilar2, successor);

		Bisimulation bisimulation = new Bisimulation(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet <>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor))));

		ma.addNondeterministicTransition("b", bisimilar1, successor);

		equivalenceClasses = bisimulation.computeEquivalenceClasses();

		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor))));

	}
	
	@Test

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
	
	
	
	public void testComputeEquivalenceClassesDirectlyByPredecessorandSuccessor() {
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
	public void testComputeEquivalenceClassesDirectlyByPredecessorandSuccessorRefinement() {
		
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
		ma.addNondeterministicTransition("b", successor1,bisimilar1);
		ma.addNondeterministicTransition("b", successor2, bisimilar2);
		
		Bisimulation bisimulation = new Bisimulation(ma);
		
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();
		
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor2))));
		
		
	}
	
	
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
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor1,successor2))));

	}


}