/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * A class to test RecoveryAutomatonHelper 
 * @author mika_li
 *
 */
public class RecoveryAutomatonHelperTest extends ATestCase {

	@Test 
	public void testComputeDisabledInputs() {
		final int STATES = 5;

		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, STATES);
		ra.setInitial(ra.getStates().get(0));
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = raHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition23 = raHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));
		FaultEventTransition transition34 = raHelper.createFaultEventTransition(ra, ra.getStates().get(3), ra.getStates().get(4));
		//CHECKSTYLE:ON
		
		raHelper.assignInputs(transition01, fault1);
		raHelper.assignInputs(transition02, fault2);
		raHelper.assignInputs(transition13, fault2);
		raHelper.assignInputs(transition23, fault1);
		raHelper.assignInputs(transition34, fault1);
		
		transition34.setIsRepair(true);
		
		Map<Transition, State> mapTransitionToTo = new HashMap<>();
		for (Transition transition : ra.getTransitions()) {
			mapTransitionToTo.put(transition, transition.getTo());
		}
		
		RecoveryAutomatonHolder raHolder = new RecoveryAutomatonHolder(ra);
		
		Map<State, Map<FaultTreeNode, Boolean>> disabledInputs = raHelper.computeDisabledInputs(raHolder);
		//CHECKSTYLE:OFF
		assertTrue(disabledInputs.get(ra.getStates().get(0)).isEmpty());
		assertFalse(disabledInputs.get(ra.getStates().get(1)).get(fault1));
		assertFalse(disabledInputs.get(ra.getStates().get(2)).get(fault2));
		assertFalse(disabledInputs.get(ra.getStates().get(3)).get(fault1));
		assertFalse(disabledInputs.get(ra.getStates().get(3)).get(fault2));
		assertTrue(disabledInputs.get(ra.getStates().get(4)).get(fault1));
		assertFalse(disabledInputs.get(ra.getStates().get(4)).get(fault2));
		assertEquals(1, disabledInputs.get(ra.getStates().get(1)).size());
		assertEquals(1, disabledInputs.get(ra.getStates().get(2)).size());
		assertEquals(2, disabledInputs.get(ra.getStates().get(3)).size());
		assertEquals(2, disabledInputs.get(ra.getStates().get(4)).size());
		//CHECKSTYLE:ON
	}
	
	@Test
	public void testCopyRA() {
		final int NUMBER_STATES = 2;
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, NUMBER_STATES);
		
		raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		raHelper.createTimeoutTransition(ra, ra.getStates().get(0), ra.getStates().get(1), 1);
		
		RecoveryAutomaton raCopy = raHelper.copyRA(ra);
		
		assertEquals(ra.getStates().size(), raCopy.getStates().size());
		assertEquals(ra.getTransitions().size(), raCopy.getTransitions().size());
		assertTrue(ra.getTransitions().get(0) instanceof FaultEventTransition);
		assertTrue(ra.getTransitions().get(1) instanceof TimeoutTransition);
	}

}
