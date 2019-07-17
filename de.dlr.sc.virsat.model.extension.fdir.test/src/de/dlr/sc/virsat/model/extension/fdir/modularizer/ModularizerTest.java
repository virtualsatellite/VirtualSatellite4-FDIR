/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.modularizer;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;


/**
 * To test the Modularizer Class
 * @author jord_ad
 */

public class ModularizerTest extends ATestCase {
	
	protected Concept concept;
	protected Modularizer modularizer;
	
	@Before
	public void setUp() throws Exception {
		String conceptXmiPluginPath = "de.dlr.sc.virsat.model.extension.fdir/concept/concept.xmi";
		concept = de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader.loadConceptFromPlugin(conceptXmiPluginPath);
		
		modularizer = new Modularizer();
	}
	
	/* **********************************************
	 * TESTING TREE COUNT
	 * *********************************************/

	
	@Test
	public void testCountTreeCSP2() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		final int SPARE_IND = 0;
		FaultTreeNode spare = rootcsp2.getFaultTree().getSpares().get(SPARE_IND).getFrom();
		
		modularizer.setFaultTree(rootcsp2.getFaultTree());
		modularizer.countTree();
		
		final int ROOT_FIRST_VISIT = 1;
		final int ROOT_LAST_VISIT = 10;
		final int SPARE_LAST_VISIT = 8;
		
		Map<FaultTreeNode, FaultTreeNodePlus> ans = modularizer.getTable();		
		assertEquals("Root, first visit", ROOT_FIRST_VISIT, ans.get(rootcsp2).getFirstVisit());
		assertEquals("Root, last visit", ROOT_LAST_VISIT, ans.get(rootcsp2).getLastVisit());
		assertEquals("Spare", SPARE_LAST_VISIT, ans.get(spare).getLastVisit());
	}
	
	
	@Test
	public void testCountTreeOr3AndCSPBasic() throws IOException {
		final int ANDGATE_INDEX = 1;
		final int SPARE_INDEX = 0;
		final int SPAREGATE_INDEX = 0;
		
		Fault rootOr3Andcsp = createDFT("/resources/galileo/or3AndColdSpareBasic.dft");
		FaultTreeNode spare = rootOr3Andcsp.getFaultTree().getSpares().get(SPARE_INDEX).getFrom();
		FaultTreeNode andGate = rootOr3Andcsp.getFaultTree().getGates().get(ANDGATE_INDEX);
		FaultTreeNode spareGate = rootOr3Andcsp.getFaultTree().getSpares().get(SPAREGATE_INDEX).getTo();
		Fault c = rootOr3Andcsp.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("C"))
				.findAny().get();

		modularizer.setFaultTree(rootOr3Andcsp.getFaultTree());
		modularizer.countTree();
		
		final int ROOT_LAST_VISIT = 23;
		final int SPARE_LAST_VISIT = 19;
		final int ANDGATE_LAST_VISIT = 21;
		final int ANDGATE_FIRST_VISIT = 9;
		final int SPAREGATE_FIRST_VISIT = 13;
		final int C_LAST_VISIT = 12;
		
		Map<FaultTreeNode, FaultTreeNodePlus> ans = modularizer.getTable();	
		assertEquals("Root, last visit", ROOT_LAST_VISIT, ans.get(rootOr3Andcsp).getLastVisit());
		assertEquals("Spare", SPARE_LAST_VISIT, ans.get(spare).getLastVisit());
		assertEquals("andGate, last visit", ANDGATE_LAST_VISIT, ans.get(andGate).getLastVisit());
		assertEquals("spareGate, first visit", SPAREGATE_FIRST_VISIT, ans.get(spareGate).getFirstVisit());
		assertEquals("c", C_LAST_VISIT, ans.get(c).getLastVisit());
		assertEquals("andGate, first visit", ANDGATE_FIRST_VISIT, ans.get(andGate).getFirstVisit());
	}
	
	
	/* **********************************************
	 * TESTING TREE DEPTH
	 * *********************************************/
	@Test
	public void testNullTree() {
		final int TREE_DEPTH = -1;
		assertEquals(TREE_DEPTH, modularizer.getTreeDepth(null));
	}
	
	@Test
	public void testTreeDepthCSP2() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		int ans = modularizer.getTreeDepth(rootcsp2.getFaultTree());
		final int EXPECTED_DEPTH = 3;		
		assertEquals(EXPECTED_DEPTH, ans);
	}

	@Test
	public void testTreeDepthOr3AndCSPBasic() throws IOException {
		Fault rootOr3Andcsp = createDFT("/resources/galileo/or3AndColdSpareBasic.dft");
		int ans = modularizer.getTreeDepth(rootOr3Andcsp.getFaultTree());
		final int EXPECTED_DEPTH = 5;
		assertEquals(EXPECTED_DEPTH, ans);
	}
	
	
	/* **********************************************
	 * TESTING MODULARIZATION
	 * *********************************************/
	
	@Test
	public void testHarvestModuleSimpleSpareRoot() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		modularizer.setFaultTree(rootcsp2.getFaultTree());
		modularizer.countTree();
		Module module = modularizer.harvestModule(rootcsp2);
		
		final int SPARE_INDEX = 0;
		final int SPAREGATE_INDEX = 0;
		
		FaultTreeNode spare = rootcsp2.getFaultTree().getSpares().get(SPARE_INDEX).getFrom();
		FaultTreeNode spareGate = rootcsp2.getFaultTree().getSpares().get(SPAREGATE_INDEX).getTo();
		FaultTreeNode child = rootcsp2.getFaultTree().getChildFaults().iterator().next();
		
		final int MODULESIZE_ROOT = 6;
		assertEquals(MODULESIZE_ROOT, module.getNodes().size());
		assertThat(module.getNodes(), hasItems(child, spareGate, spare, rootcsp2));
	}
	
	@Test
	public void testHarvestModuleSimpleSpareSpareGate() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		modularizer.setFaultTree(rootcsp2.getFaultTree());
		modularizer.countTree();
		
		final int SPARE_INDEX = 0;
		final int SPAREGATE_INDEX = 0;
		
		FaultTreeNode spare = rootcsp2.getFaultTree().getSpares().get(SPARE_INDEX).getFrom();
		FaultTreeNode spareGate = rootcsp2.getFaultTree().getSpares().get(SPAREGATE_INDEX).getTo();
		FaultTreeNode child = rootcsp2.getFaultTree().getChildFaults().iterator().next();
		
		Module module = modularizer.harvestModule(spareGate);
		final int MODULESIZE_SPAREGATE = 5;
		assertEquals(MODULESIZE_SPAREGATE, module.getNodes().size());
		assertThat(module.getNodes(), allOf(hasItems(child, spareGate, spare), not(hasItems(rootcsp2))));
	}
	
	
	@Test
	public void testHarvestModuleRootOnly() {
		Fault root = new Fault(concept);
		root.setName("ROOT");
		
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		Module module = modularizer.harvestModule(root);
		
		final int MODULESIZE = 1;
		assertEquals(MODULESIZE, module.getNodes().size());
		assertThat(module.getNodes(), hasItems(root));
	}
	
	@Test
	public void testHarvestModuleSharedSpare() throws IOException {
		Fault rootOr2And2SharedSP = createDFT("/resources/galileo/or2And2SharedSpare.dft");
		modularizer.setFaultTree(rootOr2And2SharedSP.getFaultTree());
		modularizer.countTree();
		
		final int AND1_INDEX = 2;
		
		FaultTreeNode andGate1 = rootOr2And2SharedSP.getFaultTree().getGates().get(AND1_INDEX);
		Module module = modularizer.harvestModule(andGate1);
		assertNull(module);
	}
	
	@Test
	public void testHarvestModuleNull() {
		Module module = modularizer.harvestModule(null);
		assertNull(module);
	}
	
	@Test
	public void testModuleTypeOr2And2Basic() throws IOException {
		Fault rootOr2And2Basic = createDFT("/resources/galileo/or2And2Basic.dft");
		modularizer.setFaultTree(rootOr2And2Basic.getFaultTree());
		modularizer.countTree();
		
		final int AND1_INDEX = 1;
		FaultTreeNode andGate1 = rootOr2And2Basic.getFaultTree().getGates().get(AND1_INDEX);
		Module module = modularizer.harvestModule(andGate1);
		assertTrue(module.isNondeterministic());
	}
	
	@Test
	public void testModuleTypeOr2() throws IOException {
		Fault rootOr2 = createDFT("/resources/galileo/or2.dft");
		modularizer.setFaultTree(rootOr2.getFaultTree());
		modularizer.countTree();
		
		final int OR_INDEX = 0;
		FaultTreeNode orGate = rootOr2.getFaultTree().getGates().get(OR_INDEX);
		Module module = modularizer.harvestModule(orGate);
		assertFalse(module.isNondeterministic());
		assertEquals(orGate, module.getRootNode());
	}
	
	
	@Test
	public void testModularizeCSP2() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		
		final int SPAREGATE_INDEX = 0;
		final int SPARE_INDEX = 0;
		
		FaultTreeNode spareGate = rootcsp2.getFaultTree().getSpares().get(SPAREGATE_INDEX).getTo();
		FaultTreeNode child = rootcsp2.getFaultTree().getChildFaults().iterator().next();
		FaultTreeNode spare = rootcsp2.getFaultTree().getSpares().get(SPARE_INDEX).getFrom();
		FaultTreeNode childBE = ((Fault) child).getBasicEvents().get(0);
		FaultTreeNode spareBE = ((Fault) spare).getBasicEvents().get(0);
		
		Set<Module> modules = modularizer.getModules(rootcsp2.getFaultTree());

		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());

		for (Module module : modules) {
			assertThat(module.getNodes(), anyOf(
					allOf(hasItems(child, spareGate, spare, childBE, spareBE), not(hasItems(rootcsp2))),
					allOf(not(hasItems(child, spareGate, spare, childBE, spareBE)), hasItems(rootcsp2))
			));
		}
	}
	
	@Test
	public void testModularizeOr2And2SharedSP() throws IOException {
		Fault rootOr2And2SharedSP = createDFT("/resources/galileo/or2And2SharedSpare.dft");
		
		final int AND1_INDEX = 1;
		final int AND2_INDEX = 2;
		final int OR_INDEX = 0;
		final int SPAREGATE_INDEX = 0;
		final int C_INDEX = 0;
		
		FaultTreeNode and1 = rootOr2And2SharedSP.getFaultTree().getGates().get(AND1_INDEX);
		FaultTreeNode and2 = rootOr2And2SharedSP.getFaultTree().getGates().get(AND2_INDEX);
		FaultTreeNode or = rootOr2And2SharedSP.getFaultTree().getGates().get(OR_INDEX);
		FaultTreeNode spareGate = rootOr2And2SharedSP.getFaultTree().getSpares().get(SPAREGATE_INDEX).getTo();
		FaultTreeNode c = rootOr2And2SharedSP.getFaultTree().getSpares().get(C_INDEX).getFrom();
		
		FaultTreeNode a = rootOr2And2SharedSP.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("A"))
				.findAny().get();
		FaultTreeNode b = rootOr2And2SharedSP.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("B"))
				.findAny().get();
		FaultTreeNode d = rootOr2And2SharedSP.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("D"))
				.findAny().get();
		
		Set<Module> modules = modularizer.getModules(rootOr2And2SharedSP.getFaultTree());
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
		
		Map<FaultTreeNode, FaultTreeNodePlus> map = modularizer.getTable();
		assertFalse(map.get(a).hasSpareBelow());
		assertFalse(map.get(and2).hasSpareBelow());
		assertTrue(map.get(and1).hasSpareBelow());
		assertTrue(map.get(b).hasSpareAbove());
		assertTrue(map.get(c).hasSpareAbove());
		assertFalse(map.get(d).hasSpareAbove());

		for (Module module : modules) {
			assertThat(module.getNodes(), anyOf(
					allOf(hasItems(and1, and2, or, spareGate, a, b, c, d), not(hasItems(rootOr2And2SharedSP))),
					allOf(not(hasItems(and1, and2, or, spareGate, a, b, c, d)), hasItems(rootOr2And2SharedSP))
			));
		}
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH NESTED PRIORITY GATES
	 * ****************************************************/
	
	@Test
	public void testNestedPrioritySimple() throws IOException {
		Fault rootNestedPand = createDFT("/resources/galileo/nestedPand.dft");
		Set<Module> modules = modularizer.getModules(rootNestedPand.getFaultTree());
		
		final int NUM_MODULES = 4;
		assertEquals(NUM_MODULES, modules.size());
		
		final int AND1_IND = 0;
		final int PAND1_IND = 1;
		final int PAND2_IND = 2;
		FaultTreeNode and1 = rootNestedPand.getFaultTree().getGates().get(AND1_IND);
		FaultTreeNode pand1 = rootNestedPand.getFaultTree().getGates().get(PAND1_IND);
		FaultTreeNode pand2 = rootNestedPand.getFaultTree().getGates().get(PAND2_IND);
		
		FaultTreeNode a = rootNestedPand.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("A"))
				.findAny().get();
		FaultTreeNode b = rootNestedPand.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("B"))
				.findAny().get();
		FaultTreeNode c = rootNestedPand.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("C"))
				.findAny().get();
		FaultTreeNode d = rootNestedPand.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("D"))
				.findAny().get();
		FaultTreeNode e = rootNestedPand.getFaultTree().getChildFaults().stream()
				.filter(node -> node.getName().equals("E"))
				.findAny().get();
		
		Map<FaultTreeNode, FaultTreeNodePlus> map = modularizer.getTable();
		assertTrue(map.get(b).hasPriorityAbove());
		assertFalse(map.get(a).hasPriorityAbove());
		assertFalse(map.get(pand2).hasPriorityAbove());
		
		for (Module module : modules) {
			assertThat(module.getNodes(), anyOf(
					allOf(hasItems(and1, a), not(hasItems(rootNestedPand, pand1, pand2, b, c, d, e))),
					allOf(hasItems(pand1, b, c), not(hasItems(rootNestedPand, pand2, d, e, and1, a))),
					allOf(hasItems(pand2, d, e), not(hasItems(rootNestedPand, pand1, b, c, and1, a))),
					allOf(not(hasItems(and1, pand1, pand2, a, b, c, d, e)), hasItems(rootNestedPand))
			));
		}
	}
	
	@Test
	public void testNestedPriorityComplex() throws IOException {
		Fault rootNestedComplex = createDFT("/resources/galileo/nestedPand2.dft");
		Set<Module> modules = modularizer.getModules(rootNestedComplex.getFaultTree());

		final int NUM_MODULES = 7;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH FDEPS
	 * ****************************************************/
	
	@Test
	public void testModularizeFDEP1() throws IOException {
		Fault rootFDEP1 = createDFT("/resources/galileo/fdep1.dft");
		Set<Module> modules = modularizer.getModules(rootFDEP1.getFaultTree());
		final int NUM_MODULES = 5;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testModularizeFDEP2Complex() throws IOException {
		Fault rootFDEP2 = createDFT("/resources/galileo/fdep2.dft");
		Set<Module> modules = modularizer.getModules(rootFDEP2.getFaultTree());
		
		final int NUM_MODULES = 6;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	/* *****************************************************
	 *  TESTING MORE REALISTIC & COMPLETE FAULT TREES
	 * *****************************************************/

	@Test
	public void testComplexTree() throws IOException {
		Fault rootFDEP3Spare2 = createDFT("/resources/galileo/fdep3Spare2.dft");
		Set<Module> modules = modularizer.getModules(rootFDEP3Spare2.getFaultTree());
		
		final int NUM_MODULES = 10;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCMSimple() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		Set<Module> modules = modularizer.getModules(rootCMSimple.getFaultTree());
		
		final int NUM_MODULES = 5;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM1() throws IOException {
		Fault rootCM1 = createDFT("/resources/galileo/cm1.dft");
		Set<Module> modules = modularizer.getModules(rootCM1.getFaultTree());
		
		modules.stream().forEach(m -> System.out.println(m));
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM2() throws IOException {
		Fault rootCM2 = createDFT("/resources/galileo/cm2.dft");
		Set<Module> modules = modularizer.getModules(rootCM2.getFaultTree());
		
		final int NUM_MODULES = 10;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM3() throws IOException {
		Fault rootCM3 = createDFT("/resources/galileo/cm3.dft");
		Set<Module> modules = modularizer.getModules(rootCM3.getFaultTree());
		
		final int NUM_MODULES = 16;
		assertEquals(NUM_MODULES, modules.size());
	}
}
