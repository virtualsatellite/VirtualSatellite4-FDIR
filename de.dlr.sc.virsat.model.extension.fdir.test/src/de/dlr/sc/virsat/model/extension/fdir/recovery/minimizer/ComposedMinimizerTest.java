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
 * Class that implements the ComposedMinimizer
 * 
 * @author mika_li
 *
 */
public class ComposedMinimizerTest extends ATestCase {

	protected ARecoveryAutomatonMinimizer minimizer;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		minimizer = ComposedMinimizer.createDefaultMinimizer();
	}
	
	@Test
	public void composedTestForTwoParallelSpareGates() {

		final int INITIAL_STATES = 4;
		final int RESULTING_STATES = 1;
		final int RESULTING_TRANSITIONS = 2;

		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");

		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES);
		ra.setInitial(ra.getStates().get(0));

		// CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(2), ra.getStates().get(3));
		FaultEventTransition transition11 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(1), ra.getStates().get(1));
		FaultEventTransition transition22 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(2), ra.getStates().get(2));
		FaultEventTransition transition33 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition33_2 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(3), ra.getStates().get(3));
		// CHECKSTYLE:ON

		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition13, fault2);
		raHelper.assignInputs(transition23, fault1);
		raHelper.assignInputs(transition11, fault1);
		raHelper.assignInputs(transition22, fault2);
		raHelper.assignInputs(transition33, fault1);
		raHelper.assignInputs(transition33_2, fault2);

		FaultTreeNode spare1 = ftHelper.createGate(fault1, FaultTreeNodeType.SPARE);
		FaultTreeNode spare2 = ftHelper.createGate(fault2, FaultTreeNodeType.SPARE);

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
	public void completeTest() {

		final int INITIAL_STATES = 5;
		final int RESULTING_STATES = 3;
		final int RESULTING_TRANSITIONS = 5;

		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		Fault fault3 = new Fault(concept);
		fault3.setName("Fault3");

		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES);

		// CHECKSTYLE:OFF
		FaultEventTransition transition00 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(0), ra.getStates().get(0));
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition13_2 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition14 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(1), ra.getStates().get(4));
		FaultEventTransition transition24 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition24_2 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(2), ra.getStates().get(4));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(2), ra.getStates().get(3));
		FaultEventTransition transition33 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition33_2 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition33_3 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(3), ra.getStates().get(3));
		FaultEventTransition transition44 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(4), ra.getStates().get(4));
		FaultEventTransition transition44_2 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(4), ra.getStates().get(4));
		FaultEventTransition transition44_3 = raHelper.createFaultEventTransition(ra,
				ra.getStates().get(4), ra.getStates().get(4));
		// CHECKSTYLE:ON

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

}
