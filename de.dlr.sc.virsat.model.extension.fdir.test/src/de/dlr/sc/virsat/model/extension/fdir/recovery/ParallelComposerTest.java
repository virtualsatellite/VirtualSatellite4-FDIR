/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Class to test the ParallelComposer
 * @author jord_ad
 *
 */
public class ParallelComposerTest {
	
	protected Concept concept;
	protected ParallelComposer pc;
	
	@Before
	public void setUp() throws Exception {
		String conceptXmiPluginPath = "de.dlr.sc.virsat.model.extension.fdir/concept/concept.xmi";
		concept = de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader.loadConceptFromPlugin(conceptXmiPluginPath);
		
		pc = new ParallelComposer();
	}
	
	@Test
	public void testNull() {
		RecoveryAutomaton result = pc.compose(null, null);
		assertNull(result);
	}
	
	@Test
	public void testNone() {
		RecoveryAutomaton result = pc.compose(new HashSet<RecoveryAutomaton>(), concept);
		
		final int NUM_STATES = 0;
		final int NUM_TRANSITIONS = 0;
		assertEquals(NUM_STATES, result.getStates().size());
		assertEquals(NUM_TRANSITIONS, result.getTransitions().size());
	}
	
	@Test
	public void testOne() {
		RecoveryAutomaton ra1 = new RecoveryAutomaton(concept);
		RecoveryAutomatonHolder rahold1 = new RecoveryAutomatonHolder(ra1);
		RecoveryAutomatonHelper rahelp = rahold1.getRaHelper();
		State s10 = rahelp.createSingleState(ra1, 0);
		State s11 = rahelp.createSingleState(ra1, 1);
		rahelp.createFaultEventTransition(ra1, s10, s11);
		
		Set<RecoveryAutomaton> ras = new HashSet<RecoveryAutomaton>();
		ras.add(ra1);
		RecoveryAutomaton result = pc.compose(ras, concept);
		
		assertEquals(ra1, result);
	}
	
	@Test
	public void testParallelConvert2Basic() {
		RecoveryAutomaton ra1 = new RecoveryAutomaton(concept);
		ra1.setName("ra1");
		RecoveryAutomatonHolder rahold1 = new RecoveryAutomatonHolder(ra1);
		RecoveryAutomatonHelper rahelp = rahold1.getRaHelper();
		State s10 = rahelp.createSingleState(ra1, 0);
		State s11 = rahelp.createSingleState(ra1, 1);
		rahelp.createFaultEventTransition(ra1, s10, s11);
		
		RecoveryAutomaton ra2 = new RecoveryAutomaton(concept);
		ra2.setName("ra2");
		State s20 = rahelp.createSingleState(ra2, 0);
		State s21 = rahelp.createSingleState(ra2, 1);
		rahelp.createFaultEventTransition(ra2, s20, s21);
		
		Set<RecoveryAutomaton> ras = new TreeSet<RecoveryAutomaton>(new Comparator<RecoveryAutomaton>() {
			@Override
			public int compare(RecoveryAutomaton ra1, RecoveryAutomaton ra2) {
				return ra1.getName().compareTo(ra2.getName());
			}
		});
		ras.add(ra1);
		ras.add(ra2);
		
		RecoveryAutomaton result = pc.compose(ras, concept);
		
		final int NUM_STATES = 4;
		final int NUM_TRANSITIONS = 4;
		assertEquals(NUM_STATES, result.getStates().size());
		assertEquals(NUM_TRANSITIONS, result.getTransitions().size());

		State r0 = rahelp.getState(result, "[0, 0]");
		State r1 = rahelp.getState(result, "[0, 1]");
		State r2 = rahelp.getState(result, "[1, 0]");
		State r3 = rahelp.getState(result, "[1, 1]");
		
		assertTrue(rahelp.isConnected(result, r0, r1));
		assertTrue(rahelp.isConnected(result, r0, r2));
		assertTrue(rahelp.isConnected(result, r1, r3));
		assertTrue(rahelp.isConnected(result, r2, r3));
	}
	
	@Test
	public void testParallelConvert2Medium() {
		RecoveryAutomaton ra1 = new RecoveryAutomaton(concept);
		RecoveryAutomatonHolder rahold1 = new RecoveryAutomatonHolder(ra1);
		RecoveryAutomatonHelper rahelp = rahold1.getRaHelper();
		ra1.setName("ra1");
		State s10 = rahelp.createSingleState(ra1, 0);
		State s11 = rahelp.createSingleState(ra1, 1);
		rahelp.createFaultEventTransition(ra1, s10, s11);
		rahelp.createFaultEventTransition(ra1, s11, s10);
		
		RecoveryAutomaton ra2 = new RecoveryAutomaton(concept);
		ra2.setName("ra2");
		State s20 = rahelp.createSingleState(ra2, 0);
		State s21 = rahelp.createSingleState(ra2, 1);
		State s22 = rahelp.createSingleState(ra2, 2);
		rahelp.createFaultEventTransition(ra2, s20, s21);
		rahelp.createFaultEventTransition(ra2, s21, s22);
		
		Set<RecoveryAutomaton> ras = new TreeSet<RecoveryAutomaton>(new Comparator<RecoveryAutomaton>() {
			@Override
			public int compare(RecoveryAutomaton ra1, RecoveryAutomaton ra2) {
				return ra1.getName().compareTo(ra2.getName());
			}
		});
		ras.add(ra1);
		ras.add(ra2);
		
		RecoveryAutomaton result = pc.compose(ras, concept);
		
		final int NUM_STATES = 6;
		final int NUM_TRANSITIONS = 10;
		assertEquals(NUM_STATES, result.getStates().size());
		assertEquals(NUM_TRANSITIONS, result.getTransitions().size());
		
		State r0 = rahelp.getState(result, "[0, 0]");
		State r1 = rahelp.getState(result, "[0, 1]");
		State r2 = rahelp.getState(result, "[0, 2]");
		State r3 = rahelp.getState(result, "[1, 0]");
		State r4 = rahelp.getState(result, "[1, 1]");
		State r5 = rahelp.getState(result, "[1, 2]");
		
		assertTrue(rahelp.isConnected(result, r0, r1));
		assertTrue(rahelp.isConnected(result, r1, r2));
		assertTrue(rahelp.isConnected(result, r3, r4));
		assertTrue(rahelp.isConnected(result, r4, r5));
		assertFalse(rahelp.isConnected(result, r1, r0));
		
		assertTrue(rahelp.isConnected(result, r0, r3));
		assertTrue(rahelp.isConnected(result, r3, r0));
		assertTrue(rahelp.isConnected(result, r1, r4));
		assertTrue(rahelp.isConnected(result, r4, r1));
		assertTrue(rahelp.isConnected(result, r2, r5));
		assertTrue(rahelp.isConnected(result, r5, r2));
	}
	
	@Test
	public void testParallelConvert3Basic() {
		RecoveryAutomaton ra1 = new RecoveryAutomaton(concept);
		RecoveryAutomatonHolder rahold1 = new RecoveryAutomatonHolder(ra1);
		RecoveryAutomatonHelper rahelp = rahold1.getRaHelper();
		State s10 = rahelp.createSingleState(ra1, 0);
		State s11 = rahelp.createSingleState(ra1, 1);
		rahelp.createFaultEventTransition(ra1, s10, s11);
		
		RecoveryAutomaton ra2 = new RecoveryAutomaton(concept);
		State s20 = rahelp.createSingleState(ra2, 0);
		State s21 = rahelp.createSingleState(ra2, 1);
		rahelp.createFaultEventTransition(ra2, s20, s21);
		
		RecoveryAutomaton ra3 = new RecoveryAutomaton(concept);
		State s30 = rahelp.createSingleState(ra3, 0);
		State s31 = rahelp.createSingleState(ra3, 1);
		rahelp.createFaultEventTransition(ra3, s30, s31);
		
		Set<RecoveryAutomaton> ras = new HashSet<RecoveryAutomaton>();
		ras.add(ra1);
		ras.add(ra2);
		ras.add(ra3);
		
		RecoveryAutomaton result = pc.compose(ras, concept);
		
		final int NUM_STATES = 8;
		final int NUM_TRANSITIONS = 12;
		assertEquals(NUM_STATES, result.getStates().size());
		assertEquals(NUM_TRANSITIONS, result.getTransitions().size());
		
		State r0 = rahelp.getState(result, "[0, 0, 0]");
		State r1 = rahelp.getState(result, "[0, 0, 1]");
		State r2 = rahelp.getState(result, "[0, 1, 0]");
		State r3 = rahelp.getState(result, "[0, 1, 1]");
		State r4 = rahelp.getState(result, "[1, 0, 0]");
		State r5 = rahelp.getState(result, "[1, 0, 1]");
		State r6 = rahelp.getState(result, "[1, 1, 0]");
		State r7 = rahelp.getState(result, "[1, 1, 1]");
		
		assertTrue(rahelp.isConnected(result, r0, r1));
		assertTrue(rahelp.isConnected(result, r0, r2));
		assertTrue(rahelp.isConnected(result, r0, r4));
		assertTrue(rahelp.isConnected(result, r1, r3));
		assertTrue(rahelp.isConnected(result, r1, r5));
		assertTrue(rahelp.isConnected(result, r2, r3));
		assertTrue(rahelp.isConnected(result, r2, r6));
		assertTrue(rahelp.isConnected(result, r3, r7));
		assertTrue(rahelp.isConnected(result, r4, r5));
		assertTrue(rahelp.isConnected(result, r4, r6));
		assertTrue(rahelp.isConnected(result, r5, r7));
		assertTrue(rahelp.isConnected(result, r6, r7));
	}

}
