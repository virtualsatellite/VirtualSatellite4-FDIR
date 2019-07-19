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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;

/**
 * Test class for FaultTreeHelper
 * @author jord_ad
 *
 */
public class FaultTreeHelperTest extends ATestCase {
	
	@Before
	public void setUp() throws Exception {
		super.set();
	}

	@Test
	public void testCopyNode() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootcsp2.getConcept());
		FaultTreeNode copy = fthelp.copyFaultTreeNode(rootcsp2, null);
		
		assertEquals(0, fthelp.getAllChildren(copy, copy.getFault().getFaultTree()).size());
	}
	
	@Test
	public void testCopySpare() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootcsp2.getConcept());
		FaultTreeNode spareGate = fthelp.getAllSpares(rootcsp2).get(0).getTo();
		assertEquals(FaultTreeNodeType.SPARE, spareGate.getFaultTreeNodeType());
		
		Fault rootCopy = new Fault(concept);
		FaultTreeNode copy = fthelp.copyFaultTreeNode(spareGate, rootCopy);
		
		assertEquals(FaultTreeNodeType.SPARE, copy.getFaultTreeNodeType());
	}
	
	@Test
	public void testCreateEdge() throws IOException {
		Fault rootand2or = createDFT("/resources/galileo/and2or.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootand2or.getConcept());
		FaultTreeNode orGate = fthelp.getAllNodes(rootand2or).get(2);
		
		FaultTreeNode rootCopy = fthelp.copyFaultTreeNode(rootand2or, null);
		FaultTreeNode orGateCopy = fthelp.copyFaultTreeNode(orGate, rootCopy.getFault());
		
		fthelp.createFaultTreeEdge(rootCopy.getFault(), orGateCopy, rootCopy);
		
		assertEquals(1, fthelp.getAllPropagations(rootCopy.getFault()).size());
	}
	
	@Test
	public void testCreateEdgesSpare() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootcsp2.getConcept());
		FaultTreeNode spareGate = fthelp.getAllSpares(rootcsp2).get(0).getTo();
		FaultTreeNode spare = fthelp.getAllSpares(rootcsp2).get(0).getFrom();
		
		FaultTreeNode spareGateCopy = fthelp.copyFaultTreeNode(spareGate, null);
		FaultTreeNode primary = new Fault(rootcsp2.getConcept());
		primary.setName("PRIMARY");
		FaultTreeNode spareCopy = fthelp.copyFaultTreeNode(spare, null);

		fthelp.connectSpare(spareGateCopy.getFault(), spareCopy, spareGateCopy);
		fthelp.createFaultTreeEdge(spareGateCopy.getFault(), primary, spareGateCopy);
		
		assertEquals(1, fthelp.getAllPropagations(spareGateCopy.getFault()).size());
		assertEquals(1, fthelp.getAllSpares(spareGateCopy.getFault()).size());
	}

}
