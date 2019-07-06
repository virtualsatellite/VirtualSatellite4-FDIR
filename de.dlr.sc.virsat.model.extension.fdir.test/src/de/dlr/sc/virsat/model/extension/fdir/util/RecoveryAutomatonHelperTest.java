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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * A class to test RecoveryAutomatonHelper 
 * @author mika_li
 *
 */
public class RecoveryAutomatonHelperTest extends ATestCase {

	protected RecoveryAutomatonHelper recoveryAutomatonHelper; 
	protected FaultTreeHelper faultTreeHelper;
	
	@Before
	@Override
	public void set() throws Exception {
		super.set();
		recoveryAutomatonHelper = new RecoveryAutomatonHelper(concept);
		faultTreeHelper = new FaultTreeHelper(concept);
	}
	
	@Test 
	public void inputsTest() {
		
		final int STATES = 4;

		Fault fault1 = new Fault(concept);
		fault1.setName("Fault1");
		Fault fault2 = new Fault(concept);
		fault2.setName("Fault2");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		recoveryAutomatonHelper.createStates(ra, STATES);
		ra.setInitial(ra.getStates().get(0));
		
		//CHECKSTYLE:OFF
		FaultEventTransition transition01 = recoveryAutomatonHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		FaultEventTransition transition02 = recoveryAutomatonHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(2));
		FaultEventTransition transition13 = recoveryAutomatonHelper.createFaultEventTransition(ra, ra.getStates().get(1), ra.getStates().get(3));
		FaultEventTransition transition23 = recoveryAutomatonHelper.createFaultEventTransition(ra, ra.getStates().get(2), ra.getStates().get(3));
		//CHECKSTYLE:ON
		
		recoveryAutomatonHelper.assignInputs(transition01, fault1);
		recoveryAutomatonHelper.assignInputs(transition02, fault2);
		recoveryAutomatonHelper.assignInputs(transition13, fault2);
		recoveryAutomatonHelper.assignInputs(transition23, fault1);
		
		Map<Transition, State> mapTransitionToTo = new HashMap<>();
		for (Transition transition : ra.getTransitions()) {
			mapTransitionToTo.put(transition, transition.getTo());
		}
		
		Map<State, Set<FaultTreeNode>> inputs = recoveryAutomatonHelper
				.computeInputs(ra, recoveryAutomatonHelper.getPreviousTransitions(ra), recoveryAutomatonHelper.getCurrentTransitions(ra), mapTransitionToTo);
		//CHECKSTYLE:OFF
		assertTrue(inputs.get(ra.getStates().get(0)).isEmpty());
		assertThat(inputs.get(ra.getStates().get(1)), hasItems(fault1));
		assertThat(inputs.get(ra.getStates().get(2)), hasItems(fault2));
		assertThat(inputs.get(ra.getStates().get(3)), hasItems(fault1, fault2));
		assertEquals(inputs.get(ra.getStates().get(1)).size(), 1);
		assertEquals(inputs.get(ra.getStates().get(2)).size(), 1);
		assertEquals(inputs.get(ra.getStates().get(3)).size(), 2);
		//CHECKSTYLE:ON
	}

}
