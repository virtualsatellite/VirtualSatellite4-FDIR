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
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;

/**
 * Test class for FaultTreeHelper
 * @author jord_ad
 *
 */
public class FaultTreeHelperTest extends ATestCase {
	
	protected Concept concept;
	
	@Before
	public void setUp() throws Exception {
		String conceptXmiPluginPath = "de.dlr.sc.virsat.model.extension.fdir/concept/concept.xmi";
		concept = de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader.loadConceptFromPlugin(conceptXmiPluginPath);
	}

	@Test
	public void testCopyNode() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootCMSimple.getConcept());
		FaultTreeNode copy = fthelp.copyFaultTreeNode(rootCMSimple, null);
		
		System.out.println(copy.getFault().getFaultTree().toDot());
		
		assertEquals(0, fthelp.getAllChildren(copy, copy.getFault().getFaultTree()).size());
	}
	
	@Test
	public void testCopySpare() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootCMSimple.getConcept());
		FaultTreeNode spareGate = fthelp.getAllSpares(rootCMSimple).get(0).getTo();
		assertEquals(FaultTreeNodeType.SPARE, spareGate.getFaultTreeNodeType());
		
		FaultTreeNode copy = fthelp.copyFaultTreeNode(spareGate, null);
		System.out.println(copy.getFault().getFaultTree().toDot());
		
		assertEquals(FaultTreeNodeType.SPARE, copy.getFaultTreeNodeType());
	}
	
	@Test
	public void testCreateEdge() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootCMSimple.getConcept());
		FaultTreeNode andGate = fthelp.getAllNodes(rootCMSimple).get(2);
		
		FaultTreeNode rootCopy = fthelp.copyFaultTreeNode(rootCMSimple, null);
		FaultTreeNode andGateCopy = fthelp.copyFaultTreeNode(andGate, rootCopy.getFault());
		
		fthelp.createFaultTreeEdge(rootCopy.getFault(), andGateCopy, rootCopy);
		
		System.out.println(rootCopy.getFault().getFaultTree().toDot());
		
		assertEquals(1, fthelp.getAllPropagations(rootCopy.getFault()).size());
	}
	
	@Test
	public void testCreateEdgesSpare() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		FaultTreeHelper fthelp = new FaultTreeHelper(rootCMSimple.getConcept());
		FaultTreeNode spareGate = fthelp.getAllSpares(rootCMSimple).get(0).getTo();
		FaultTreeNode spare = fthelp.getAllSpares(rootCMSimple).get(0).getFrom();
		
		FaultTreeNode spareGateCopy = fthelp.copyFaultTreeNode(spareGate, null);
		FaultTreeNode primary = new Fault(rootCMSimple.getConcept());
		primary.setName("PRIMARY");
		FaultTreeNode spareCopy = fthelp.copyFaultTreeNode(spare, null);

		fthelp.connectSpare(spareGateCopy.getFault(), spareCopy, spareGateCopy);
		fthelp.createFaultTreeEdge(spareGateCopy.getFault(), primary, spareGateCopy);
		
		System.out.println(spareGateCopy.getFault().getFaultTree().toDot());
		
		assertEquals(1, fthelp.getAllPropagations(spareGateCopy.getFault()).size());
		assertEquals(1, fthelp.getAllSpares(spareGateCopy.getFault()).size());
	}

}
