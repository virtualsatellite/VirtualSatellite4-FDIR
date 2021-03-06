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
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * Class that tests the partition refinement minimizer 
 * @author mika_li
 *
 */

public class PartitionRefinementMinimizerTest extends ATestCase {

	protected ARecoveryAutomatonMinimizer minimizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		minimizer = new PartitionRefinementMinimizer();
	}
	
	@Test 
	public void testMinimzeStatesWithEmptyActions() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		
		final int RESULTING_TRANSITIONS = 0;
		
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
		
		FaultTreeNode spare = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		ClaimAction action = new ClaimAction(concept);
		action.setClaimSpare(spare);
		
		raHelper.assignAction(transition01, action.copy());
		raHelper.assignAction(transition11, action.copy());

		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testMinimizeComplete() {
		
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
		
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare1);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare2);
		
		raHelper.assignAction(transition00, action0.copy());
		raHelper.assignAction(transition01, action0.copy());
		raHelper.assignAction(transition11, action1.copy());
		raHelper.assignAction(newTransition11, action1.copy());
		raHelper.assignAction(transition20, action0.copy());
		raHelper.assignAction(transition21, action0.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test 
	public void testMinimizeBig() {
		final int INITIAL_STATES = 4;
		final int RESULTING_STATES = 2; 
		
		final int RESULTING_TRANSITIONS = 4; 
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition03 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(3));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition12 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(2));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));
		FaultEventTransition transition20 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(0));
		FaultEventTransition transition33 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition32 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(2));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition03, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition13, fault1);
		raHelper.assignInputs(transition12, fault2);
		raHelper.assignInputs(transition23, fault1);
		raHelper.assignInputs(transition20, fault2);
		raHelper.assignInputs(transition33, fault1);
		raHelper.assignInputs(transition32, fault2);
		
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare1);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare2);
		
		raHelper.assignAction(transition03, action0.copy());
		raHelper.assignAction(transition02, action0.copy());
		raHelper.assignAction(transition13, action0.copy());
		raHelper.assignAction(transition12, action0.copy());
		raHelper.assignAction(transition23, action0.copy());
		raHelper.assignAction(transition20, action0.copy());
		raHelper.assignAction(transition33, action1.copy());
		raHelper.assignAction(transition32, action1.copy());
	
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testEpsilon() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		final int RESULTING_TRANSITIONS = 0; 
		
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
		final int RESULTING_TRANSITIONS = 9; 
		
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
	public void testTimedEquivalent() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		final int RESULTING_TRANSITIONS = 0; 
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		raHelper.createTimeoutTransition(ra, ra.getStates().get(0), ra.getStates().get(1), 1);
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testTimedNotEquivalent() {
		final int INITIAL_STATES = 6;
		final int RESULTING_STATES = 6; 
		final int RESULTING_TRANSITIONS = 6; 
		
		Fault fault1 = new Fault(concept);
		Fault fault2 = new Fault(concept);
		Fault fault3 = new Fault(concept);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(fault1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action1.setClaimSpare(fault2);
		
		//CHECKSTYLE:OFF
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignAction(transition01, action1.copy());
		
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignAction(transition02, action2.copy());
		
		raHelper.createTimeoutTransition(ra, ra.getStates().get(1), ra.getStates().get(3), 1);
		raHelper.createTimeoutTransition(ra, ra.getStates().get(2), ra.getStates().get(4), 2);
		
		FaultEventTransition transition35 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(5));
		raHelper.assignInputs(transition35, fault3);
		raHelper.assignAction(transition35, action1.copy());
		
		FaultEventTransition transition45 = raHelper.createFaultEventTransition(ra, ra.getStates().get(4), ra.getStates().get(5));
		raHelper.assignInputs(transition45, fault3);
		raHelper.assignAction(transition45, action2.copy());
		//CHECKSTYLE:ON
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testTimedSuccessiveEquivalent() {
		final int INITIAL_STATES = 3;
		final int RESULTING_STATES = 2; 
		final int RESULTING_TRANSITIONS = 2; 
		
		Fault fault = new Fault(concept);
		
		//CHECKSTYLE:OFF
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		raHelper.createTimeoutTransition(ra, ra.getStates().get(0), ra.getStates().get(1), 1);
		raHelper.createTimeoutTransition(ra, ra.getStates().get(1), ra.getStates().get(2), 2);
		
		ClaimAction action = new ClaimAction(concept);
		FaultEventTransition transition22 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(2));
		raHelper.assignInputs(transition22, fault);
		raHelper.assignAction(transition22, action);
		//CHECKSTYLE:ON
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
		
		TimeoutTransition timeoutTransition = (TimeoutTransition) ra.getTransitions()
				.stream()
				.filter(transition -> transition.getFrom().equals(ra.getStates().get(0)) && transition.getTo().equals(ra.getStates().get(1)))
				.findFirst().get();
		
		final double EXPECTED_TIMEOUT = 3;
		assertEquals(EXPECTED_TIMEOUT, timeoutTransition.getTime(), 0);
	}
}
