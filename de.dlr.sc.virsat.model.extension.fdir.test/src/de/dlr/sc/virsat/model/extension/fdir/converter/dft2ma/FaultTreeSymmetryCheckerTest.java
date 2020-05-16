/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTSymmetryChecker;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class tests the FaultTreeSymmetriyReduction
 * @author muel_s8
 *
 */

public class FaultTreeSymmetryCheckerTest extends ATestCase {

	private DFTSymmetryChecker symmetryChecker = new DFTSymmetryChecker();
	
	@Test
	public void testAnd2NonSymmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B",  BasicEvent.class);
		Fault faultA = ftHolder.getNodeByName("A",  Fault.class);
		Fault faultB = ftHolder.getNodeByName("B",  Fault.class);
		
		assertTrue(symmetryReduction.get(a).isEmpty());
		assertTrue(symmetryReduction.get(b).isEmpty());
		assertTrue(symmetryReduction.get(faultA).isEmpty());
		assertTrue(symmetryReduction.get(faultB).isEmpty());
	}
	
	@Test
	public void testPAnd4() throws IOException {
		Fault fault = createDFT("/resources/galileo/pand4.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B",  BasicEvent.class);
		BasicEvent c = ftHolder.getNodeByName("C",  BasicEvent.class);
		BasicEvent d = ftHolder.getNodeByName("D",  BasicEvent.class);
		
		assertTrue(symmetryReduction.get(a).isEmpty());
		assertTrue(symmetryReduction.get(b).isEmpty());
		assertTrue(symmetryReduction.get(c).isEmpty());
		assertTrue(symmetryReduction.get(d).isEmpty());
	}
	
	@Test
	public void testAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		FaultTreeNode min = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).findFirst().get();
		FaultTreeNode max = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 0).findFirst().get();
		
		assertThat(symmetryReduction.get(min), hasItems(max));
		assertTrue(symmetryReduction.get(max).isEmpty());
	}
	
	@Test
	public void testAnd3Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and3Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		//CHECKSTYLE:OFF
		FaultTreeNode min = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 2).findFirst().get();
		FaultTreeNode mid = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).findFirst().get();
		FaultTreeNode max = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 0).findFirst().get();
		//CHECKSTYLE:ON
		
		assertThat(symmetryReduction.get(min), hasItems(mid, max));
		assertThat(symmetryReduction.get(mid), hasItems(max));
		assertTrue(symmetryReduction.get(max).isEmpty());
	}
	
	@Test
	public void testAnd2OrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		//CHECKSTYLE:OFF
		FaultTreeNode min1 = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).findFirst().get();
		FaultTreeNode min2 = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).skip(1).findFirst().get();
		FaultTreeNode max1 = symmetryReduction.keySet().stream().filter(k -> symmetryReduction.get(min1).contains(k) && symmetryReduction.get(k).size() == 0).findFirst().get();
		FaultTreeNode max2 = symmetryReduction.keySet().stream().filter(k -> symmetryReduction.get(min2).contains(k) && symmetryReduction.get(k).size() == 0).findFirst().get();
		FaultTreeNode minParent = ftHolder.getMapNodeToParents().get(min1).get(0);
		FaultTreeNode maxParent = ftHolder.getMapNodeToParents().get(max1).get(0);
		//CHECKSTYLE:ON
		
		assertThat(symmetryReduction.get(min1), hasItems(max1));
		assertThat(symmetryReduction.get(min2), hasItems(max2));
		assertThat(symmetryReduction.get(minParent), hasItems(maxParent));
		assertTrue(symmetryReduction.get(max1).isEmpty());
		assertTrue(symmetryReduction.get(max2).isEmpty());
		assertTrue(symmetryReduction.get(maxParent).isEmpty());
	}
	
	@Test
	public void testBeOrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/beOrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		FaultTreeNode min = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).findFirst().get();
		FaultTreeNode max = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 0 && !k.equals(a)).findFirst().get();
		
		assertTrue(symmetryReduction.get(a).isEmpty());
		assertThat(symmetryReduction.get(min), hasItems(max));
		assertTrue(symmetryReduction.get(max).isEmpty());
	}
	
	@Test
	public void testAnd2OrAnd2SharedSymmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2SharedSymmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		//CHECKSTYLE:OFF
		FaultTreeNode allSymmetric = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 3).findFirst().orElse(null);
		//CHECKSTYLE:ON
		
		assertNull("Since 1 of the BEs is shared, at most the 2 non shared BEs may be symmetric!", allSymmetric);
	}
}
