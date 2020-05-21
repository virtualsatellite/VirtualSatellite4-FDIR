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
 * Class that implements the FinalStateMinimizer 
 * @author mika_li
 *
 */
public class FinalStateMinimizerTest extends ATestCase {

	protected ARecoveryAutomatonMinimizer minimizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		minimizer = new FinalStateMinimizer();
	}
	
	@Test
	public void basicTest() {
		
		final int INITIAL_STATES = 2;
		final int FINAL_STATES = 1;
		final int FINAL_TRANSITIONS = 1; 
		
		Fault fault = new Fault(concept);
		fault.setName("Fault");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
	
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		
		raHelper.assignInputs(transition01, fault);
		raHelper.assignInputs(transition11, fault);
		
		FaultTreeNode spare = ftBuilder.createGate(fault, FaultTreeNodeType.SPARE);
		
		ClaimAction action = new ClaimAction(concept);
		action.setClaimSpare(spare);
		
		raHelper.assignAction(transition01, action.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(FINAL_STATES, ra.getStates().size());
		assertEquals(FINAL_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void multipleTransitionsTest() {
		
		final int INITIAL_STATES = 2;
		final int FINAL_STATES = 2;
		final int FINAL_TRANSITIONS = 4; 
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		FaultEventTransition transition01_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition11_2 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(1));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition11, fault1);
		raHelper.assignInputs(transition01_2, fault2);
		raHelper.assignInputs(transition11_2, fault2);
		
		FaultTreeNode spare1 = ftBuilder.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftBuilder.createGate(fault2, FaultTreeNodeType.SPARE);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		ClaimAction action2 = new ClaimAction(concept);
		action2.setClaimSpare(spare2);
		
		raHelper.assignAction(transition01, action1.copy());
		raHelper.assignAction(transition01_2, action2.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(FINAL_STATES, ra.getStates().size());
		assertEquals(FINAL_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test 
	public void multipleStatesTest() {
		
		final int INITIAL_STATES = 4;
		final int FINAL_STATES = 3;
		final int FINAL_TRANSITIONS = 4; 
		
		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
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
		raHelper.assignAction(transition13, action1.copy());
		raHelper.assignAction(transition23, action1.copy());
		
		minimizer.minimize(ra);
		
		assertEquals(FINAL_STATES, ra.getStates().size());
		assertEquals(FINAL_TRANSITIONS, ra.getTransitions().size());
	}
	
	
}
