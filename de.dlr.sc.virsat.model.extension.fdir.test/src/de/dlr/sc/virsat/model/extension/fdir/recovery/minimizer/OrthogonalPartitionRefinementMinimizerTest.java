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
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;

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
	public void testMinimizeStatesWithEmptyActions() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		
		final int RESULTING_TRANSITIONS = 1;
		
		Fault fault = new Fault(concept);
		
		// initial recovery automaton with empty recovery action list
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		
		raHelper.assignInputs(transition01, fault);
		
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
		
		raHelper.assignInputs(transition01, fault1, fault2);
		
		FaultTreeNode spare = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		ClaimAction action = new ClaimAction(concept);
		action.setClaimSpare(spare);
		
		raHelper.assignAction(transition01, action.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testMinimizeIndirectlyEquivalentStates() {
		
		final int INITIAL_STATES = 3;
		final int RESULTING_STATES = 2; 
		
		final int RESULTING_TRANSITIONS = 3;
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition20 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(0));
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition21 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(1));
		
		raHelper.assignInputs(transition01, fault2);
		raHelper.assignInputs(transition20, fault1);
		raHelper.assignInputs(transition21, fault2);
		
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare1);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare2);
		
		raHelper.assignAction(transition01, action0.copy());
		raHelper.assignAction(transition20, action0.copy());
		raHelper.assignAction(transition21, action0.copy());
		
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
	public void testRepair() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1;
		final int RESULTING_TRANSITIONS = 2;
		
		Fault fault = new Fault(concept);
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition10 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(0));
		
		raHelper.assignInputs(transition01, fault);
		raHelper.assignInputs(transition10, fault);
		
		transition10.setIsRepair(true);
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}

	@Test 
	public void testRepeatedEventSubset() {
		final int INITIAL_STATES = 3;
		final int RESULTING_STATES = 1; 
		final int RESULTING_TRANSITIONS = 1; 
		
		Fault fault = new Fault(concept);
		Fault fault1 = new Fault(concept);
		Fault fault2 = new Fault(concept);
		
		// initial recovery automaton with non-empty recovery action list
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		raHelper.assignInputs(transition01, fault1, fault2);
		
		FaultEventTransition transition12 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(2));
		raHelper.assignInputs(transition12, fault1);
		
		FaultTreeNode spare = ftBuilder.createGate(fault, FaultTreeNodeType.SPARE);
		ClaimAction action = new ClaimAction(concept);
		action.setClaimSpare(spare);
		
		raHelper.assignAction(transition01, action.copy());
		raHelper.assignAction(transition12, action.copy());
		
		RecoveryAutomaton raCopy = raHelper.copyRA(ra);
		minimizer.minimize(raCopy, fault);
		
		assertEquals(RESULTING_STATES, raCopy.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, raCopy.getTransitions().size());
		
		// If the event is partially observable, then we cannot disable it
		final int RESULTING_STATES_PO = 3; 
		final int RESULTING_TRANSITIONS_PO = 2; 
		
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);
		FaultTreeNode monitor = ftBuilder.createGate(fault, FaultTreeNodeType.MONITOR);
		ftBuilder.connectObserver(fault, fault1, monitor);
		
		raCopy = raHelper.copyRA(ra);
		minimizer.minimize(raCopy, fault);
		
		assertEquals(RESULTING_STATES_PO, raCopy.getStates().size());
		assertEquals(RESULTING_TRANSITIONS_PO, raCopy.getTransitions().size());
		
		// If the monitor is failed, then we know that the event cannot be repeated
		final int RESULTING_STATES_FAILED_MONITOR = 1; 
		final int RESULTING_TRANSITIONS_FAILED_MONITOR = 1; 
		
		raHelper.assignInputs(transition01, monitor);
		
		raCopy = raHelper.copyRA(ra);
		minimizer.minimize(raCopy, fault);
		
		assertEquals(RESULTING_STATES_FAILED_MONITOR, raCopy.getStates().size());
		assertEquals(RESULTING_TRANSITIONS_FAILED_MONITOR, raCopy.getTransitions().size());
	}
	
	@Test
	public void testComplete() {
		final int INITIAL_STATES = 5;
		final int RESULTING_STATES = 3; 
		final int RESULTING_TRANSITIONS = 6;
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		Fault fault3 = new Fault(concept);
		fault3.setName("Fault3");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition13_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition14 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(4));
		FaultEventTransition transition24 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition24_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));	
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition13, fault1);
		raHelper.assignInputs(transition13_2, fault2);
		raHelper.assignInputs(transition14, fault3);
		raHelper.assignInputs(transition24, fault1);
		raHelper.assignInputs(transition24_2, fault2);
		raHelper.assignInputs(transition23, fault3);
		
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		FaultTreeNode spare3 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action2.setClaimSpare(spare2);
		
		ClaimAction action3 = new ClaimAction(concept);
		action3.setClaimSpare(spare3);
		
		raHelper.assignAction(transition01, action1.copy());
		raHelper.assignAction(transition02, action2.copy());
		raHelper.assignAction(transition13, action1.copy());
		raHelper.assignAction(transition13_2, action2.copy());
		raHelper.assignAction(transition14, action3.copy());
		raHelper.assignAction(transition24, action1.copy());
		raHelper.assignAction(transition24_2, action2.copy());
		raHelper.assignAction(transition23, action3.copy());
		
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
		                                                                                                                                                                                                    
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action2.setClaimSpare(spare2);
		
		raHelper.assignAction(transition01, action1.copy());
		raHelper.assignAction(transition02, action2.copy());
		raHelper.assignAction(transition13, action2.copy());
		raHelper.assignAction(transition23, action1.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());

	}
	
	@Test
	public void testMergeThreeOrthogonal() {
		final int INITIAL_STATES = 5;
		final int RESULTING_STATES = 3; 
		final int RESULTING_TRANSITIONS = 6;
		
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
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition03, fault3);
		raHelper.assignInputs(transition14, fault2);
		raHelper.assignInputs(transition24, fault1);
		raHelper.assignInputs(transition34, fault1);
		
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		raHelper.assignAction(transition01, action1.copy());
		raHelper.assignAction(transition02, action1.copy());
		raHelper.assignAction(transition03, action1.copy());
		raHelper.assignAction(transition14, action1.copy());
		raHelper.assignAction(transition24, action1.copy());
		raHelper.assignAction(transition34, action1.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
}
