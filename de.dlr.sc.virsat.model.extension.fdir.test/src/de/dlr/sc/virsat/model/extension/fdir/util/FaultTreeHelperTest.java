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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * Test class for FaultTreeHelper
 * @author jord_ad
 *
 */
public class FaultTreeHelperTest extends ATestCase {

	@Test
	public void testCopyNode() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		FaultTreeNode copy = ftHelper.copyFaultTreeNode(rootcsp2, null);
		
		assertNotEquals(rootcsp2, copy);
		assertEquals(rootcsp2.getName(), copy.getName());
		assertEquals(rootcsp2.getUuid(), copy.getUuid());
		assertTrue(ftHelper.getChildren(copy).isEmpty());
	}
	
	@Test
	public void testCopySpare() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		FaultTreeNode spareGate = ftHelper.getAllSpares(rootcsp2).get(0).getTo();
		assertEquals(FaultTreeNodeType.SPARE, spareGate.getFaultTreeNodeType());
		
		Fault rootCopy = new Fault(concept);
		FaultTreeNode copy = ftHelper.copyFaultTreeNode(spareGate, rootCopy);
		
		assertEquals(FaultTreeNodeType.SPARE, copy.getFaultTreeNodeType());
	}
	
	@Test
	public void testCreateEdge() throws IOException {
		Fault rootand2or = createDFT("/resources/galileo/and2or.dft");
		FaultTreeNode orGate = ftHelper.getAllNodes(rootand2or).get(2);
		
		FaultTreeNode rootCopy = ftHelper.copyFaultTreeNode(rootand2or, null);
		FaultTreeNode orGateCopy = ftHelper.copyFaultTreeNode(orGate, rootCopy.getFault());
		
		ftHelper.createFaultTreeEdge(rootCopy.getFault(), orGateCopy, rootCopy);
		
		assertEquals(1, ftHelper.getAllPropagations(rootCopy.getFault()).size());
	}
	
	@Test
	public void testCreateEdgesSpare() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		FaultTreeNode spareGate = ftHelper.getAllSpares(rootcsp2).get(0).getTo();
		FaultTreeNode spare = ftHelper.getAllSpares(rootcsp2).get(0).getFrom();
		
		FaultTreeNode spareGateCopy = ftHelper.copyFaultTreeNode(spareGate, null);
		FaultTreeNode primary = new Fault(rootcsp2.getConcept());
		primary.setName("PRIMARY");
		FaultTreeNode spareCopy = ftHelper.copyFaultTreeNode(spare, null);

		ftHelper.connectSpare(spareGateCopy.getFault(), spareCopy, spareGateCopy);
		ftHelper.createFaultTreeEdge(spareGateCopy.getFault(), primary, spareGateCopy);
		
		assertEquals(1, ftHelper.getAllPropagations(spareGateCopy.getFault()).size());
		assertEquals(1, ftHelper.getAllSpares(spareGateCopy.getFault()).size());
	}

	@Test
	public void testRemoveNullEdge() {
		FaultTreeNode root = ftHelper.createBasicFault("ROOT", 0, 0);
		FaultTreeNode child = ftHelper.createBasicFault("CHILD", 0, 0);
		ftHelper.createFaultTreeEdge(root.getFault(), child, root);
		
		boolean removeEdge = ftHelper.removeEdgeFromFaultTree(null, root.getFault().getFaultTree());
		assertFalse(removeEdge);
	}
	
	@Test
	public void testRemoveOneEdge() {
		FaultTreeNode root = ftHelper.createBasicFault("ROOT", 0, 0);
		FaultTreeNode child = ftHelper.createBasicFault("CHILD", 0, 0);
		ftHelper.createFaultTreeEdge(root.getFault(), child, root);
		
		boolean removeEdge = ftHelper.removeEdgeFromFaultTree(root.getFault().getFaultTree().getPropagations().get(0), root.getFault().getFaultTree());
		assertTrue(removeEdge);
	}

}
