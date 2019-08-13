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

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class test the clean minimizer for removing duplicate transitions and epsilon loops
 * @author muel_s8
 *
 */

public class CleanMinimizerTest extends ATestCase {

	protected ARecoveryAutomatonMinimizer minimizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		minimizer = new CleanMinimizer();
	}
	
	@Test
	public void testEpsilon() {
		final int INITIAL_STATES = 1;
		final int RESULTING_STATES = 1; 
		final int RESULTING_TRANSITIONS = 0; 
		
		Fault fault = new Fault(concept);
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition00 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(0));
		raHelper.assignInputs(transition00, fault);
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_STATES, ra.getStates().size());
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
	
	@Test
	public void testDuplicate() {
		final int INITIAL_STATES = 2;
		final int RESULTING_TRANSITIONS = 1; 
		
		Fault fault = new Fault(concept);
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, INITIAL_STATES); 
		
		FaultEventTransition transition010 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		raHelper.assignInputs(transition010, fault);
		FaultEventTransition transition011 = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		raHelper.assignInputs(transition011, fault);
		
		minimizer.minimize(ra);
		
		assertEquals(RESULTING_TRANSITIONS, ra.getTransitions().size());
	}
}
