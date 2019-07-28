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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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

	private FaultTreeSymmetryChecker ftSymmetryChecker = new FaultTreeSymmetryChecker();
	
	@Test
	public void testAnd2NonSymmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B",  BasicEvent.class);
		Fault faultA = ftHolder.getNodeByName("A",  Fault.class);
		Fault faultB = ftHolder.getNodeByName("B",  Fault.class);
		
		assertEquals(1, symmetryReduction.get(a).size());
		assertEquals(1, symmetryReduction.get(b).size());
		assertEquals(1, symmetryReduction.get(faultA).size());
		assertEquals(1, symmetryReduction.get(faultB).size());
	}
	
	@Test
	public void testPAnd4() throws IOException {
		Fault fault = createDFT("/resources/galileo/pand4.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B",  BasicEvent.class);
		BasicEvent c = ftHolder.getNodeByName("C",  BasicEvent.class);
		BasicEvent d = ftHolder.getNodeByName("D",  BasicEvent.class);
		
		assertEquals(1, symmetryReduction.get(a).size());
		assertEquals(1, symmetryReduction.get(b).size());
		assertEquals(1, symmetryReduction.get(c).size());
		assertEquals(1, symmetryReduction.get(d).size());
	}
	
	@Test
	public void testAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		FaultTreeNode min = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 2).findFirst().get();
		FaultTreeNode max = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).findFirst().get();
		
		assertThat(symmetryReduction.get(min), hasItems(min, max));
		assertThat(symmetryReduction.get(max), hasItems(max));
	}
	
	@Test
	public void testAnd3Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and3Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		//CHECKSTYLE:OFF
		FaultTreeNode min = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 3).findFirst().get();
		FaultTreeNode mid = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 2).findFirst().get();
		FaultTreeNode max = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1).findFirst().get();
		//CHECKSTYLE:ON
		
		assertThat(symmetryReduction.get(min), hasItems(min, mid, max));
		assertThat(symmetryReduction.get(mid), hasItems(mid, max));
		assertThat(symmetryReduction.get(max), hasItems(max));
	}
	
	@Test
	public void testAnd2OrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		//CHECKSTYLE:OFF
		FaultTreeNode min1 = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 2).findFirst().get();
		FaultTreeNode min2 = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 2).skip(1).findFirst().get();
		FaultTreeNode max1 = symmetryReduction.keySet().stream().filter(k -> symmetryReduction.get(min1).contains(k) && symmetryReduction.get(k).size() == 1).findFirst().get();
		FaultTreeNode max2 = symmetryReduction.keySet().stream().filter(k -> symmetryReduction.get(min2).contains(k) && symmetryReduction.get(k).size() == 1).findFirst().get();
		//CHECKSTYLE:ON
		
		assertThat(symmetryReduction.get(min1), hasItems(min1, max1));
		assertThat(symmetryReduction.get(min2), hasItems(min2, max2));
		assertThat(symmetryReduction.get(max1), hasItems(max1));
		assertThat(symmetryReduction.get(max2), hasItems(max2));
	}
	
	@Test
	public void testBeOrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/beOrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		FaultTreeNode min = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 2).findFirst().get();
		FaultTreeNode max = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 1 && !k.equals(a)).findFirst().get();
		
		assertEquals(1, symmetryReduction.get(a).size());
		assertThat(symmetryReduction.get(a), hasItems(a));
		assertThat(symmetryReduction.get(min), hasItems(min, max));
		assertThat(symmetryReduction.get(max), hasItems(max));
	}
	
	@Test
	public void testAnd2OrAnd2SharedSymmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2SharedSymmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		//CHECKSTYLE:OFF
		FaultTreeNode allSymmetric = symmetryReduction.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryReduction.get(k).size() == 3).findFirst().orElse(null);
		//CHECKSTYLE:ON
		
		assertNull("Since 1 of the BEs is shared, at most the 2 non shared BEs may be symmetric!", allSymmetric);
	}
}
