/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

// *****************************************************************
// * Import Statements
// *****************************************************************



// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public class TransitionTest extends ATransitionTest {
	
	protected RecoveryAutomatonHelper recoveryAutomatonHelper; 
	protected FaultTreeHelper faultTreeHelper;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		recoveryAutomatonHelper = new RecoveryAutomatonHelper(concept);
		faultTreeHelper = new FaultTreeHelper(concept);
	}
	
	@Test
	public void testIsEquivalentTransition() {
		Fault fault = new Fault(concept);
		FaultTreeNode spare0 = faultTreeHelper.createGate(fault, FaultTreeNodeType.SPARE);
		FaultTreeNode spare1 = faultTreeHelper.createGate(fault, FaultTreeNodeType.SPARE);
		
		ClaimAction action0 = new ClaimAction(concept);
		action0.setClaimSpare(spare0);
		
		ClaimAction action1 = new ClaimAction(concept);
		action1.setClaimSpare(spare1);
		
		Transition transition0 = new Transition(concept);
		Transition transition1 = new Transition(concept);
		
		recoveryAutomatonHelper.assignAction(transition0, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition1, recoveryAutomatonHelper.copyClaimAction(action0));
		recoveryAutomatonHelper.assignAction(transition1, recoveryAutomatonHelper.copyClaimAction(action1));
		
		assertFalse(transition0.isEquivalentTransition(transition1));
		assertFalse(transition1.isEquivalentTransition(transition0));
		
		recoveryAutomatonHelper.assignAction(transition0, recoveryAutomatonHelper.copyClaimAction(action1));
		
		assertTrue(transition0.isEquivalentTransition(transition1));
		assertTrue(transition1.isEquivalentTransition(transition0));
	}
}
