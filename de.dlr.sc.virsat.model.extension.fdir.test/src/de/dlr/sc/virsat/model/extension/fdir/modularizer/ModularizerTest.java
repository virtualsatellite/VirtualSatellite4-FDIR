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

import de.dlr.sc.virsat.model.extension.fdir.model.AND;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.OR;
import de.dlr.sc.virsat.model.extension.fdir.model.PAND;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;


/**
 * To test the Modularizer Class
 * @author jord_ad
 */

public class ModularizerTest extends ATestCase {
	
	protected Modularizer modularizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		modularizer = new Modularizer();
	}
	
	/* **********************************************
	 * TESTING TREE COUNT
	 * *********************************************/

	
	@Test
	public void testCountTreeCSP2() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		FaultTreeNode spare = ftHolder.getNodeByName("B", Fault.class);
		
		final int ROOT_FIRST_VISIT = 1;
		final int ROOT_LAST_VISIT = 10;
		final int SPARE_LAST_VISIT = 8;
		
		Map<FaultTreeNode, FaultTreeNodePlus> ans = modularizer.getTable();		
		assertEquals("Root, first visit", ROOT_FIRST_VISIT, ans.get(root).getFirstVisit());
		assertEquals("Root, last visit", ROOT_LAST_VISIT, ans.get(root).getLastVisit());
		assertEquals("Spare", SPARE_LAST_VISIT, ans.get(spare).getLastVisit());
	}
	
	@Test
	public void testCountTreeOr3AndCSPBasic() throws IOException {
		Fault root = createDFT("/resources/galileo/or3AndColdSpareBasic.dft");

		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		
		FaultTreeHolder fthold = modularizer.getFtHolder();
		FaultTreeNode spare = fthold.getNodeByName("SPARE", Fault.class);
		FaultTreeNode andGate = fthold.getNodeByName("AND", AND.class);
		FaultTreeNode spareGate = fthold.getNodeByName("SPAREGATE", SPARE.class);
		Fault c = fthold.getNodeByName("C", Fault.class);
		
		final int ROOT_LAST_VISIT = 23;
		final int SPARE_LAST_VISIT = 19;
		final int ANDGATE_LAST_VISIT = 21;
		final int ANDGATE_FIRST_VISIT = 9;
		final int SPAREGATE_FIRST_VISIT = 13;
		final int C_LAST_VISIT = 12;
		
		Map<FaultTreeNode, FaultTreeNodePlus> ans = modularizer.getTable();	
		assertEquals("Root, last visit", ROOT_LAST_VISIT, ans.get(root).getLastVisit());
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
		Fault root = createDFT("/resources/galileo/csp2.dft");
		int ans = modularizer.getTreeDepth(root.getFaultTree());
		final int EXPECTED_DEPTH = 3;		
		assertEquals(EXPECTED_DEPTH, ans);
	}

	@Test
	public void testTreeDepthOr3AndCSPBasic() throws IOException {
		Fault root = createDFT("/resources/galileo/or3AndColdSpareBasic.dft");
		int ans = modularizer.getTreeDepth(root.getFaultTree());
		final int EXPECTED_DEPTH = 5;
		assertEquals(EXPECTED_DEPTH, ans);
	}
	
	
	/* **********************************************
	 * TESTING MODULARIZATION
	 * *********************************************/
	
	@Test
	public void testHarvestModuleSimpleSpareRoot() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		Module module = modularizer.harvestModule(root);
		
		FaultTreeHolder fthold = modularizer.getFtHolder();
		FaultTreeNode spare = fthold.getNodeByName("B", Fault.class);
		FaultTreeNode spareGate = fthold.getNodeByName("tle", SPARE.class);
		FaultTreeNode child = fthold.getChildFaults(root).iterator().next();
		
		final int MODULESIZE_ROOT = 6;
		assertEquals(MODULESIZE_ROOT, module.getNodes().size());
		assertThat(module.getNodes(), hasItems(child, spareGate, spare, root));
	}
	
	@Test
	public void testHarvestModuleSimpleSpareSpareGate() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		
		FaultTreeHolder fthold = modularizer.getFtHolder();
		FaultTreeNode spare = fthold.getNodeByName("B", Fault.class);
		FaultTreeNode spareGate = fthold.getNodeByName("tle", SPARE.class);
		FaultTreeNode child = fthold.getChildFaults(root).iterator().next();
		
		Module module = modularizer.harvestModule(spareGate);
		final int MODULESIZE_SPAREGATE = 5;
		assertEquals(MODULESIZE_SPAREGATE, module.getNodes().size());
		assertThat(module.getNodes(), allOf(hasItems(child, spareGate, spare), not(hasItems(root))));
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
		Fault root = createDFT("/resources/galileo/or2And2SharedSpare.dft");
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		
		FaultTreeNode andGate1 = modularizer.getFtHolder().getNodeByName("AND1", AND.class);
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
		Fault root = createDFT("/resources/galileo/or2And2Basic.dft");
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		
		FaultTreeNode and1 = modularizer.getFtHolder().getNodeByName("AND1", AND.class);
		
		Module module = modularizer.harvestModule(and1);
		assertTrue(module.isNondeterministic());
	}
	
	@Test
	public void testModuleTypeOr2() throws IOException {
		Fault root = createDFT("/resources/galileo/or2.dft");
		modularizer.setFaultTree(root.getFaultTree());
		modularizer.countTree();
		
		FaultTreeNode or = modularizer.getFtHolder().getNodeByName("tle", OR.class);
		
		Module module = modularizer.harvestModule(or);
		assertFalse(module.isNondeterministic());
		assertEquals(or, module.getRootNode());
	}
	
	@Test
	public void testModuleTypeOr2WithoutBEOptimization() throws IOException {
		Fault root = createDFT("/resources/galileo/or2.dft");
		modularizer.setBEOptimization(false);
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		final int NUM_MODULES = 4;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	
	@Test
	public void testModularizeCSP2() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		FaultTreeNode spare = ftHolder.getNodeByName("B", Fault.class);
		FaultTreeNode spareBE = ftHolder.getNodeByName("B", BasicEvent.class);
		FaultTreeNode spareGate = ftHolder.getNodeByName("tle", SPARE.class);
		FaultTreeNode primary = ftHolder.getNodeByName("A", Fault.class);
		FaultTreeNode primaryBE = ftHolder.getNodeByName("A", BasicEvent.class);
		
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
		
		Module rootModule = Module.getModule(modules, root);
		Module cspModule = Module.getModule(modules, spareGate);
		
		assertThat(rootModule.getNodes(), allOf(not(hasItems(primary, spareGate, spare, primaryBE, spareBE)), hasItems(root)));
		assertThat(cspModule.getNodes(), allOf(hasItems(primary, spareGate, spare, primaryBE, spareBE), not(hasItems(root))));
	}
	
	@Test
	public void testModularizeOr2And2SharedSP() throws IOException {
		Fault root = createDFT("/resources/galileo/or2And2SharedSpare.dft");
		FaultTreeHolder ftholder = new FaultTreeHolder(root);
		FaultTreeNode and1 = ftholder.getNodeByName("AND1", AND.class);
		FaultTreeNode and2 = ftholder.getNodeByName("AND2", AND.class);
		FaultTreeNode or = ftholder.getNodeByName("OR", OR.class);
		FaultTreeNode spareGate = ftholder.getNodeByName("SPAREGATE", SPARE.class);
		FaultTreeNode c = ftholder.getNodeByName("C", Fault.class);
		FaultTreeNode a = ftholder.getNodeByName("A", Fault.class);
		FaultTreeNode b = ftholder.getNodeByName("B", Fault.class);
		FaultTreeNode d = ftholder.getNodeByName("D", Fault.class);
		
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
		
		Map<FaultTreeNode, FaultTreeNodePlus> map = modularizer.getTable();
		assertFalse(map.get(a).hasSpareBelow());
		assertFalse(map.get(and2).hasSpareBelow());
		assertTrue(map.get(and1).hasSpareBelow());
		assertTrue(map.get(b).hasSpareAbove());
		assertTrue(map.get(c).hasSpareAbove());
		assertFalse(map.get(d).hasSpareAbove());

		Module rootModule = Module.getModule(modules, root);
		Module orModule = Module.getModule(modules, or);
		
		assertThat(rootModule.getNodes(), allOf(not(hasItems(and1, and2, or, spareGate, a, b, c, d)), hasItems(root)));
		assertThat(orModule.getNodes(), allOf(hasItems(and1, and2, or, spareGate, a, b, c, d), not(hasItems(root))));
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH NESTED PRIORITY GATES
	 * ****************************************************/
	
	@Test
	public void testNestedPrioritySimple() throws IOException {
		Fault root = createDFT("/resources/galileo/nestedPand.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 4;
		assertEquals(NUM_MODULES, modules.size());
		
		FaultTreeHolder fthold = new FaultTreeHolder(root);
		FaultTreeNode and = fthold.getNodeByName("tle", AND.class);
		FaultTreeNode pand1 = fthold.getNodeByName("PAND1", PAND.class);
		FaultTreeNode pand2 = fthold.getNodeByName("PAND2", PAND.class);
		FaultTreeNode a = fthold.getNodeByName("A", Fault.class);
		FaultTreeNode b = fthold.getNodeByName("B", Fault.class);
		FaultTreeNode c = fthold.getNodeByName("C", Fault.class);
		FaultTreeNode d = fthold.getNodeByName("D", Fault.class);
		FaultTreeNode e = fthold.getNodeByName("E", Fault.class);
		
		Map<FaultTreeNode, FaultTreeNodePlus> map = modularizer.getTable();
		assertTrue(map.get(b).hasPriorityAbove());
		assertFalse(map.get(a).hasPriorityAbove());
		assertFalse(map.get(pand2).hasPriorityAbove());
		
		Module rootModule = Module.getModule(modules, root);
		Module andModule = Module.getModule(modules, and);
		Module pand1Module = Module.getModule(modules, pand1);
		Module pand2Module = Module.getModule(modules, pand2);
		
		assertThat(rootModule.getNodes(), allOf(not(hasItems(and, pand1, pand2, a, b, c, d, e)), hasItems(root)));
		assertThat(andModule.getNodes(), allOf(hasItems(and, a), not(hasItems(root, pand1, pand2, b, c, d, e))));
		assertThat(pand1Module.getNodes(), allOf(hasItems(pand1, b, c), not(hasItems(root, pand2, d, e, and, a))));
		assertThat(pand2Module.getNodes(), allOf(hasItems(pand2, d, e), not(hasItems(root, pand1, b, c, and, a))));
	}
	
	@Test
	public void testNestedPriorityComplex() throws IOException {
		Fault root = createDFT("/resources/galileo/nestedPand2.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());

		final int NUM_MODULES = 7;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH FDEPS
	 * ****************************************************/
	
	@Test
	public void testModularizeFDEP1() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep1.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		final int NUM_MODULES = 5;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testModularizeFDEP2Complex() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep2.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 6;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH PARTIAL OBSERVABILITY
	 * ****************************************************/
	
	@Test
	public void testModuleTypeObsCsp2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsCsp2.dft");
		
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		final int NUM_MODULES = 1;
		assertEquals(NUM_MODULES, modules.size());
		
		Module module = modules.iterator().next();
		assertTrue(module.isPartialObservable());
	}
	
	@Test
	public void testModuleTypeObsOr2ObsBe2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsOr2ObsBe2.dft");
		
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		final int NUM_MODULES = 1;
		assertEquals(NUM_MODULES, modules.size());
		
		Module module = modules.iterator().next();
		assertTrue(module.isPartialObservable());
	}
	
	@Test
	public void testModuleTypeObsOr2ObsCsp2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsOr2ObsCsp2.dft");
		
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		final int NUM_MODULES = 3;
		assertEquals(NUM_MODULES, modules.size());
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		
		SPARE s1 = ftHolder.getNodeByName("s1", SPARE.class);
		SPARE s2 = ftHolder.getNodeByName("s2", SPARE.class);
		
		Module rootModule = Module.getModule(modules, root);
		Module s1Module = Module.getModule(modules, s1);
		Module s2Module = Module.getModule(modules, s2);
		
		assertFalse(rootModule.isPartialObservable());
		assertTrue(s1Module.isPartialObservable());
		assertTrue(s2Module.isPartialObservable());
	}
	
	/* *****************************************************
	 *  TESTING MORE REALISTIC & COMPLETE FAULT TREES
	 * *****************************************************/

	@Test
	public void testComplexTree() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep3Spare2.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 10;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCMSimple2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm_simple2.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 5;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM1() throws IOException {
		Fault root = createDFT("/resources/galileo/cm1.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm2.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 10;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM3() throws IOException {
		Fault root = createDFT("/resources/galileo/cm3.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 16;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testFTPP4() throws IOException {
		Fault root = createDFT("/resources/galileo/ftpp4.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testAHRS2() throws IOException {
		Fault root = createDFT("/resources/galileo/ahrs2.dft");
		Set<Module> modules = modularizer.getModules(root.getFaultTree());
		
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
	}
}
