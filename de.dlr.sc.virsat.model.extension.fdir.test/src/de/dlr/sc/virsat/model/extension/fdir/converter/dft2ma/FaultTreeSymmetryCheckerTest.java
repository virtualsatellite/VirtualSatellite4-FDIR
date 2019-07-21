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
import java.util.Map;
import java.util.Set;

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
	public void testAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		FaultTreeNode min = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 2).findFirst().get();
		FaultTreeNode max = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 1).findFirst().get();
		
		assertThat(symmetryRelation.get(min), hasItems(min, max));
		assertThat(symmetryRelation.get(max), hasItems(max));
	}
	
	@Test
	public void testAnd3Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and3Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		//CHECKSTYLE:OFF
		FaultTreeNode min = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 3).findFirst().get();
		FaultTreeNode mid = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 2).findFirst().get();
		FaultTreeNode max = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 1).findFirst().get();
		//CHECKSTYLE:ON
		
		assertThat(symmetryRelation.get(min), hasItems(min, mid, max));
		assertThat(symmetryRelation.get(mid), hasItems(mid, max));
		assertThat(symmetryRelation.get(max), hasItems(max));
	}
	
	@Test
	public void testAnd2OrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		//CHECKSTYLE:OFF
		FaultTreeNode min1 = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 2).findFirst().get();
		FaultTreeNode min2 = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 2).skip(1).findFirst().get();
		FaultTreeNode max1 = symmetryRelation.keySet().stream().filter(k -> symmetryRelation.get(min1).contains(k) && symmetryRelation.get(k).size() == 1).findFirst().get();
		FaultTreeNode max2 = symmetryRelation.keySet().stream().filter(k -> symmetryRelation.get(min2).contains(k) && symmetryRelation.get(k).size() == 1).findFirst().get();
		//CHECKSTYLE:ON
		
		assertThat(symmetryRelation.get(min1), hasItems(min1, max1));
		assertThat(symmetryRelation.get(min2), hasItems(min2, max2));
		assertThat(symmetryRelation.get(max1), hasItems(max1));
		assertThat(symmetryRelation.get(max2), hasItems(max2));
	}
	
	@Test
	public void testBeOrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/beOrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);

		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		FaultTreeNode min = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 2).findFirst().get();
		FaultTreeNode max = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 1 && !k.equals(a)).findFirst().get();
		
		assertEquals(1, symmetryRelation.get(a).size());
		assertThat(symmetryRelation.get(a), hasItems(a));
		assertThat(symmetryRelation.get(min), hasItems(min, max));
		assertThat(symmetryRelation.get(max), hasItems(max));
	}
	
	@Test
	public void testAnd2OrAnd2SharedSymmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2SharedSymmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		
		//CHECKSTYLE:OFF
		FaultTreeNode allSymmetric = symmetryRelation.keySet().stream().filter(k -> k instanceof BasicEvent && symmetryRelation.get(k).size() == 3).findFirst().orElse(null);
		//CHECKSTYLE:ON
		
		assertNull("Since 1 of the BEs is shared, at most the 2 non shared BEs may be symmetric!", allSymmetric);
	}
}
