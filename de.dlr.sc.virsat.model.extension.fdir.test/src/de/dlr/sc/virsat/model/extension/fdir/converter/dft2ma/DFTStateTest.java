/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFTStateTest extends ATestCase {

	@Test
	public void testGetWorkingUnit() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileo/2csp2Shared.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		
		SPARE spareGate1 = ftHolder.getNodeByName("SG1", SPARE.class);
		FaultTreeNode primary = ftHolder.getNodeByName("Memory1_P", Fault.class);
		
		DFTState state = new DFTState(ftHolder);
		
		FaultTreeNode workingUnit = state.getWorkingUnit(spareGate1);
		assertEquals("Initially the working unit is the primary", primary, workingUnit);
		
		FaultTreeNode spare = ftHolder.getNodeByName("Memory1_R", Fault.class);
		
		state.setFaultTreeNodeFailed(primary, true);
		workingUnit = state.getWorkingUnit(spareGate1);
		assertNull("No claims and primary failed gives null for working unit", workingUnit);
		
		state.getMapSpareToClaimedSpares().put(spare, spareGate1);
		workingUnit = state.getWorkingUnit(spareGate1);
		assertEquals("After claiming, the claimed spare is the working unit", spare, workingUnit);
		
		state.setFaultTreeNodeFailed(spare, true);
		state.setFaultTreeNodeFailed(primary, false);
		workingUnit = state.getWorkingUnit(spareGate1);
		assertNull("Claim and failed spare gives null for working unit", workingUnit);
		
		SPARE spareGate2 = ftHolder.getNodeByName("SG2", SPARE.class);
		state.setFaultTreeNodeFailed(spare, false);
		state.setFaultTreeNodeFailed(primary, true);
		state.getMapSpareToClaimedSpares().put(spare, spareGate2);
		assertNull("Claimed by other spare gate and failed primary gives null for working unit", workingUnit);
	}

	@Test
	public void testExecuteBasicEvent() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileo/failureMode.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		
		BasicEvent be = ftHolder.getNodeByName("FailureMode", BasicEvent.class);
		
		DFTState state = new DFTState(ftHolder);
		
		assertTrue(state.getFailedBasicEvents().isEmpty());
		
		state.executeBasicEvent(be, false, false);
		
		assertTrue(state.getFailedBasicEvents().contains(be));
		assertTrue(state.hasFaultTreeNodeFailed(be));
		assertTrue(state.isFaultTreeNodePermanent(be));
		
		state.executeBasicEvent(be, true, false);
		
		assertTrue(state.getFailedBasicEvents().isEmpty());
		assertFalse(state.hasFaultTreeNodeFailed(be));
	}
}
