/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the OrthogonalPartitionRefinementMinimizer
 * @author muel_s8
 *
 */

public class OrthogonalPartitionRefinementMinimizerTest extends ATestCase {

	protected ARecoveryAutomatonMinimizer minimizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		minimizer = new OrthogonalPartitionRefinementMinimizer();
	}
	
	@Test 
	public void testMinimzeStatesWithEmptyActions() {
		
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		
		final int RESULTING_TRANSITIONS = 1;
		
		Fault fault = new Fault(concept);
		
		// initial recovery automaton with empty recovery action list
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		
		raHelper.assignInputs(transition01, fault);
		raHelper.assignInputs(transition11, fault);
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
		
	}
	
	@Test 
	public void testMinimzeDirectlyEquivalentStates() {
		
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		
		final int RESULTING_TRANSITIONS = 1; 
		
		Fault fault1 = new Fault(concept);
		Fault fault2 = new Fault(concept);
		
		// initial recovery automaton with non-empty recovery action list
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));

		raHelper.assignInputs(transition01, fault1, fault2);
		raHelper.assignInputs(transition11, fault2, fault1);
		
		FaultTreeNode spare = ftHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		ClaimAction action = new ClaimAction(concept);
		action.setClaimSpare(spare);
		
		raHelper.assignAction(transition01, raHelper.copyClaimAction(action));
		raHelper.assignAction(transition11, raHelper.copyClaimAction(action));

		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testMinimizeIndirectlyEquivalentStates() {
		
		final int INITIAL_STATES = 3;
		final int RESULTING_STATES = 2; 
		
		final int RESULTING_TRANSITIONS = 4;
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition00 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(0));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		FaultEventTransition newTransition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		FaultEventTransition transition20 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(0));
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition21 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(1));
		
		raHelper.assignInputs(transition00, fault1);
		raHelper.assignInputs(transition01, fault2);
		raHelper.assignInputs(transition11, fault1);
		raHelper.assignInputs(newTransition11, fault2);
		raHelper.assignInputs(transition20, fault1);
		raHelper.assignInputs(transition21, fault2);
		
		FaultTreeNode spare1 = ftHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare1);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare2);
		
		raHelper.assignAction(transition00, raHelper.copyClaimAction(action0));
		raHelper.assignAction(transition01, raHelper.copyClaimAction(action0));
		raHelper.assignAction(transition11, raHelper.copyClaimAction(action1));
		raHelper.assignAction(newTransition11, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition20, raHelper.copyClaimAction(action0));
		raHelper.assignAction(transition21, raHelper.copyClaimAction(action0));
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testEpsilon() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		final int RESULTING_TRANSITIONS = 1; 
		
		Fault fault = new Fault(concept);
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		raHelper.assignInputs(transition01, fault);
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void completeTest() {
		
		final int INITIAL_STATES = 5;
		final int RESULTING_STATES = 3; 
		final int RESULTING_TRANSITIONS = 13; 
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		Fault fault3 = new Fault(concept);
		fault3.setName("Fault3");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition00 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(0));
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition13_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition14 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(4));
		FaultEventTransition transition24 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition24_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));	
		FaultEventTransition transition33 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition33_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition33_3 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition44 = raHelper.createFaultEventTransition(ra, ra.getStates().get(4), ra.getStates().get(4));
		FaultEventTransition transition44_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(4), ra.getStates().get(4));
		FaultEventTransition transition44_3 = raHelper.createFaultEventTransition(ra, ra.getStates().get(4), ra.getStates().get(4));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition00, fault3);
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition13, fault1);
		raHelper.assignInputs(transition13_2, fault2);
		raHelper.assignInputs(transition14, fault3);
		raHelper.assignInputs(transition24, fault1);
		raHelper.assignInputs(transition24_2, fault2);
		raHelper.assignInputs(transition23, fault3);
		raHelper.assignInputs(transition33, fault1);
		raHelper.assignInputs(transition33_2, fault2);
		raHelper.assignInputs(transition33_3, fault3);
		raHelper.assignInputs(transition44, fault1);
		raHelper.assignInputs(transition44_2, fault2);
		raHelper.assignInputs(transition44_3, fault3);
		
		FaultTreeNode spare1 = ftHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		FaultTreeNode spare3 = ftHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action2.setClaimSpare(spare2);
		
		ClaimAction action3 = new ClaimAction(concept);
		action3.setClaimSpare(spare3);
		
		raHelper.assignAction(transition01, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition02, raHelper.copyClaimAction(action2));
		raHelper.assignAction(transition13, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition13_2, raHelper.copyClaimAction(action2));
		raHelper.assignAction(transition14, raHelper.copyClaimAction(action3));
		raHelper.assignAction(transition24, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition24_2, raHelper.copyClaimAction(action2));
		raHelper.assignAction(transition23, raHelper.copyClaimAction(action3));
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}

	@Test 
	public void testMergeTwoOrthogonal() {
		
		final int INITIAL_STATES = 4;
		final int RESULTING_STATES = 1; 
		final int RESULTING_TRANSITIONS = 4; 
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		ra.setInitial(ra.getStates().get(0));
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault2);
		raHelper.assignInputs(transition02, fault1);
		raHelper.assignInputs(transition13, fault1);
		raHelper.assignInputs(transition23, fault2);
		                                                                                                                                                                                                    
		FaultTreeNode spare1 = ftHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action2.setClaimSpare(spare2);
		
		raHelper.assignAction(transition01, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition02, raHelper.copyClaimAction(action2));
		raHelper.assignAction(transition13, raHelper.copyClaimAction(action2));
		raHelper.assignAction(transition23, raHelper.copyClaimAction(action1));
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());

	}
	
	@Test
	public void testMergeThreeOrthogonal() {
		final int INITIAL_STATES = 5;
		final int RESULTING_STATES = 3; 
		final int RESULTING_TRANSITIONS = 7;
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		Fault fault3 = new Fault(concept);
		fault3.setName("Fault3");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES);
		ra.setInitial(ra.getStates().get(0));
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition03 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(3));
		FaultEventTransition transition14 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(4));
		FaultEventTransition transition24 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition34 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(4));
		
		FaultEventTransition transition22 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(2));
		FaultEventTransition transition33 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition03, fault3);
		raHelper.assignInputs(transition14, fault2);
		raHelper.assignInputs(transition24, fault1);
		raHelper.assignInputs(transition34, fault1);
		
		raHelper.assignInputs(transition11, fault1);
		raHelper.assignInputs(transition22, fault2);
		raHelper.assignInputs(transition33, fault2);
		
		FaultTreeNode spare1 = ftHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		raHelper.assignAction(transition01, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition02, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition03, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition14, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition24, raHelper.copyClaimAction(action1));
		raHelper.assignAction(transition34, raHelper.copyClaimAction(action1));
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
}
