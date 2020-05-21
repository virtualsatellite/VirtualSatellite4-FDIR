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
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
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
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();
		
		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B",  BasicEvent.class);
		Fault faultA = ftHolder.getNodeByName("A",  Fault.class);
		Fault faultB = ftHolder.getNodeByName("B",  Fault.class);
		
		assertTrue(relation.get(a).isEmpty());
		assertTrue(relation.get(b).isEmpty());
		assertTrue(relation.get(faultA).isEmpty());
		assertTrue(relation.get(faultB).isEmpty());
	}
	
	@Test
	public void testPAnd4() throws IOException {
		Fault fault = createDFT("/resources/galileo/pand4.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();
		
		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B",  BasicEvent.class);
		BasicEvent c = ftHolder.getNodeByName("C",  BasicEvent.class);
		BasicEvent d = ftHolder.getNodeByName("D",  BasicEvent.class);
		
		assertTrue(relation.get(a).isEmpty());
		assertTrue(relation.get(b).isEmpty());
		assertTrue(relation.get(c).isEmpty());
		assertTrue(relation.get(d).isEmpty());
	}
	
	@Test
	public void testAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();

		FaultTreeNode min = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 1).findFirst().get();
		FaultTreeNode max = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 0).findFirst().get();
		
		assertThat(relation.get(min), hasItems(max));
		assertTrue(relation.get(max).isEmpty());
	}
	
	@Test
	public void testAnd3Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and3Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();
		
		//CHECKSTYLE:OFF
		FaultTreeNode min = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 2).findFirst().get();
		FaultTreeNode mid = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 1).findFirst().get();
		FaultTreeNode max = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 0).findFirst().get();
		//CHECKSTYLE:ON
		
		assertThat(relation.get(min), hasItems(mid, max));
		assertThat(relation.get(mid), hasItems(max));
		assertTrue(relation.get(max).isEmpty());
	}
	
	@Test
	public void testAnd2OrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();

		//CHECKSTYLE:OFF
		FaultTreeNode min1 = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 1).findFirst().get();
		FaultTreeNode min2 = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 1).skip(1).findFirst().get();
		FaultTreeNode max1 = relation.keySet().stream().filter(k -> relation.get(min1).contains(k) && relation.get(k).size() == 0).findFirst().get();
		FaultTreeNode max2 = relation.keySet().stream().filter(k -> relation.get(min2).contains(k) && relation.get(k).size() == 0).findFirst().get();
		FaultTreeNode minParent = ftHolder.getNodes(min1, EdgeType.PARENT).get(0);
		FaultTreeNode maxParent = ftHolder.getNodes(max1, EdgeType.PARENT).get(0);
		//CHECKSTYLE:ON
		
		assertThat(relation.get(min1), hasItems(max1));
		assertThat(relation.get(min2), hasItems(max2));
		assertThat(relation.get(minParent), hasItems(maxParent));
		assertTrue(relation.get(max1).isEmpty());
		assertTrue(relation.get(max2).isEmpty());
		assertTrue(relation.get(maxParent).isEmpty());
	}
	
	@Test
	public void testBeOrAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/beOrAnd2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();

		BasicEvent a = ftHolder.getNodeByName("A",  BasicEvent.class);
		FaultTreeNode min = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 1).findFirst().get();
		FaultTreeNode max = relation.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 0 && !k.equals(a)).findFirst().get();
		
		assertTrue(relation.get(a).isEmpty());
		assertThat(relation.get(min), hasItems(max));
		assertTrue(relation.get(max).isEmpty());
	}
	
	@Test
	public void testAnd2OrAnd2SharedSymmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2OrAnd2SharedSymmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		Map<FaultTreeNode, List<FaultTreeNode>> relation = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder).getBiggerRelation();
		
		//CHECKSTYLE:OFF
		FaultTreeNode allSymmetric = relation
				.keySet().stream().filter(k -> k instanceof BasicEvent && relation.get(k).size() == 3)
				.findFirst().orElse(null);
		//CHECKSTYLE:ON
		
		assertNull("Since 1 of the BEs is shared, at most the 2 non shared BEs may be symmetric!", allSymmetric);
	}
}
