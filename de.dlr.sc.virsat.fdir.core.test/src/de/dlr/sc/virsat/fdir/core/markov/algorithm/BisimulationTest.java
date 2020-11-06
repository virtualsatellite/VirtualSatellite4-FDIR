package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class BisimulationTest {

	@Test
	public void testComputeEquiavlenceClassesDirectlyBisimilarActions() {
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState bisimilar1 = new MarkovState();
		MarkovState bisimilar2 = new MarkovState();
		MarkovState successor  = new MarkovState();
		
		ma.addNondeterministicTransition("a", bisimilar1, successor);
		ma.addNondeterministicTransition("a", bisimilar2, successor);
		
		Bisimulation bisimulation = new Bisimulation(ma);
		Set<Set<MarkovState>> equivalenceClasses = bisimulation.computeEquivalenceClasses();
		
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(bisimilar1, bisimilar2))));
		assertTrue(equivalenceClasses.contains(new HashSet<>(Arrays.asList(successor))));
	}

}
