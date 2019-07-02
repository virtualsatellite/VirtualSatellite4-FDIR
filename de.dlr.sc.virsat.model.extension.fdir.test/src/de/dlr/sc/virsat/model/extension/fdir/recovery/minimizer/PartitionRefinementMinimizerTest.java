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

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Class that tests the partition refinement minimizer 
 * @author mika_li
 *
 */

public class PartitionRefinementMinimizerTest extends AMinimizerTestCase {

	@Override
	public ARecoveryAutomatonMinimizer createMinimizer() {
		return new PartitionRefinementMinimizer();
	}
	
	@Test 
	public void testMinimzeStatesWithEmptyActions() {
		final int INITIAL_STATES = 2;
		final int RESULTING_STATES = 1; 
		
		final int RESULTING_TRANSITIONS = 0;
		
		Fault fault = new Fault(concept);
		
		// initial recovery automaton with empty recovery action list
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		
		recoveryAutomatonHelper.createStates(ra, INITIAL_STATES); 
		
		Transition transition01 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		Transition transition11 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		
		recoveryAutomatonHelper.assignInputs(transition01, fault);
		recoveryAutomatonHelper.assignInputs(transition11, fault);
		
		//System.out.println(ra.toDot());
		
		// get a minimized recovery automaton
		ARecoveryAutomatonMinimizer minimizer = createMinimizer();
		minimizer.minimize(ra);
		
		//System.out.println(minimizedRA.toDot());
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
		
		recoveryAutomatonHelper.createStates(ra, INITIAL_STATES); 
		
		Transition transition01 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		Transition transition11 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(1));

		recoveryAutomatonHelper.assignInputs(transition01, fault1, fault2);
		recoveryAutomatonHelper.assignInputs(transition11, fault2, fault1);
		
		FaultTreeNode spare = faultTreeHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		ClaimAction action = new ClaimAction(concept);
		action.setClaimSpare(spare);
		
		recoveryAutomatonHelper.assignAction(transition01, recoveryAutomatonHelper.copyClaimAction(action));
		recoveryAutomatonHelper.assignAction(transition11, recoveryAutomatonHelper.copyClaimAction(action));

		//System.out.println(ra.toDot());
		
		// get a minimized recovery automaton
		ARecoveryAutomatonMinimizer minimizer = createMinimizer();
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
		//System.out.println(minimizedRA.toDot());
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
		recoveryAutomatonHelper.createStates(ra, INITIAL_STATES); 
		
		Transition transition00 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(0));
		Transition transition11 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		Transition newTransition11 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		Transition transition20 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(0));
		Transition transition01 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		Transition transition21 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(1));
		
		recoveryAutomatonHelper.assignInputs(transition00, fault1);
		recoveryAutomatonHelper.assignInputs(transition01, fault2);
		recoveryAutomatonHelper.assignInputs(transition11, fault1);
		recoveryAutomatonHelper.assignInputs(newTransition11, fault2);
		recoveryAutomatonHelper.assignInputs(transition20, fault1);
		recoveryAutomatonHelper.assignInputs(transition21, fault2);
		
		FaultTreeNode spare1 = faultTreeHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = faultTreeHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare1);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare2);
		
		recoveryAutomatonHelper.assignAction(transition00, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition01, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition11, recoveryAutomatonHelper.copyClaimAction(action1));
		recoveryAutomatonHelper.assignAction(newTransition11, recoveryAutomatonHelper.copyClaimAction(action1));
		recoveryAutomatonHelper.assignAction(transition20, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition21, recoveryAutomatonHelper.copyClaimAction(action0));
		
		//System.out.println(ra.toDot());
		
		ARecoveryAutomatonMinimizer minimizer = createMinimizer();
		minimizer.minimize(ra);
		
		//System.out.println(minimizedRA.toDot());
		
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
		recoveryAutomatonHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		Transition transition03 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(3));
		Transition transition02 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		Transition transition13 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		Transition transition12 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(2));
		Transition transition23 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(3));
		Transition transition20 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(0));
		Transition transition33 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		Transition transition32 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(3), ra.getStates().get(2));
		//CHECKSTYLE:ON
		
		recoveryAutomatonHelper.assignInputs(transition03, fault1);
		recoveryAutomatonHelper.assignInputs(transition02, fault2);
		recoveryAutomatonHelper.assignInputs(transition13, fault1);
		recoveryAutomatonHelper.assignInputs(transition12, fault2);
		recoveryAutomatonHelper.assignInputs(transition23, fault1);
		recoveryAutomatonHelper.assignInputs(transition20, fault2);
		recoveryAutomatonHelper.assignInputs(transition33, fault1);
		recoveryAutomatonHelper.assignInputs(transition32, fault2);
		
		FaultTreeNode spare1 = faultTreeHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = faultTreeHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare1);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare2);
		
		recoveryAutomatonHelper.assignAction(transition03, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition02, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition13, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition12, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition23, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition20, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition33, recoveryAutomatonHelper.copyClaimAction(action1));
		recoveryAutomatonHelper.assignAction(transition32, recoveryAutomatonHelper.copyClaimAction(action1));
	
		System.out.println(ra.toDot());
		
		ARecoveryAutomatonMinimizer minimizer = createMinimizer();
		minimizer.minimize(ra);
		
		System.out.println(ra.toDot());
		
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
		recoveryAutomatonHelper.createStates(ra, INITIAL_STATES); 
		
		Transition transition01 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		recoveryAutomatonHelper.assignInputs(transition01, fault);
		
		//System.out.println(ra.toDot());
		
		ARecoveryAutomatonMinimizer minimizer = createMinimizer();
		minimizer.minimize(ra);
		
		//System.out.println(ra.toDot());
		
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
		recoveryAutomatonHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		Transition transition00 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(0));
		Transition transition01 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		Transition transition02 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		Transition transition13 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		Transition transition13_2 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		Transition transition14 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(1), ra.getStates().get(4));
		Transition transition24 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		Transition transition24_2 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(4));
		Transition transition23 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(2), ra.getStates().get(3));	
		Transition transition33 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		Transition transition33_2 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		Transition transition33_3 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(3), ra.getStates().get(3));
		Transition transition44 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(4), ra.getStates().get(4));
		Transition transition44_2 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(4), ra.getStates().get(4));
		Transition transition44_3 = recoveryAutomatonHelper.createTransition(ra, ra.getStates().get(4), ra.getStates().get(4));
		//CHECKSTYLE:ON
		
		recoveryAutomatonHelper.assignInputs(transition00, fault3);
		recoveryAutomatonHelper.assignInputs(transition01, fault1);
		recoveryAutomatonHelper.assignInputs(transition02, fault2);
		recoveryAutomatonHelper.assignInputs(transition13, fault1);
		recoveryAutomatonHelper.assignInputs(transition13_2, fault2);
		recoveryAutomatonHelper.assignInputs(transition14, fault3);
		recoveryAutomatonHelper.assignInputs(transition24, fault1);
		recoveryAutomatonHelper.assignInputs(transition24_2, fault2);
		recoveryAutomatonHelper.assignInputs(transition23, fault3);
		recoveryAutomatonHelper.assignInputs(transition33, fault1);
		recoveryAutomatonHelper.assignInputs(transition33_2, fault2);
		recoveryAutomatonHelper.assignInputs(transition33_3, fault3);
		recoveryAutomatonHelper.assignInputs(transition44, fault1);
		recoveryAutomatonHelper.assignInputs(transition44_2, fault2);
		recoveryAutomatonHelper.assignInputs(transition44_3, fault3);
		
		FaultTreeNode spare1 = faultTreeHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = faultTreeHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		FaultTreeNode spare3 = faultTreeHelper.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action2.setClaimSpare(spare2);
		
		ClaimAction action3 = new ClaimAction(concept);
		action3.setClaimSpare(spare3);
		
		recoveryAutomatonHelper.assignAction(transition01, recoveryAutomatonHelper.copyClaimAction(action1));
		recoveryAutomatonHelper.assignAction(transition02, recoveryAutomatonHelper.copyClaimAction(action2));
		recoveryAutomatonHelper.assignAction(transition13, recoveryAutomatonHelper.copyClaimAction(action1));
		recoveryAutomatonHelper.assignAction(transition13_2, recoveryAutomatonHelper.copyClaimAction(action2));
		recoveryAutomatonHelper.assignAction(transition14, recoveryAutomatonHelper.copyClaimAction(action3));
		recoveryAutomatonHelper.assignAction(transition24, recoveryAutomatonHelper.copyClaimAction(action1));
		recoveryAutomatonHelper.assignAction(transition24_2, recoveryAutomatonHelper.copyClaimAction(action2));
		recoveryAutomatonHelper.assignAction(transition23, recoveryAutomatonHelper.copyClaimAction(action3));
		
		System.out.println(ra.toDot());
		
		ARecoveryAutomatonMinimizer minimizer = createMinimizer();
		minimizer.minimize(ra);
		
		System.out.println(ra.toDot());
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
}
