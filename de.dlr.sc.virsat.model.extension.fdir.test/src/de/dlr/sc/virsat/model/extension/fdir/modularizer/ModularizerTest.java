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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.AND;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
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
		
		modularizer.setFaultTree(root);
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

		modularizer.setFaultTree(root);
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
	 * TESTING MODULARIZATION
	 * *********************************************/
	
	@Test
	public void testHarvestModuleSimpleSpareRoot() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		modularizer.setFaultTree(root);
		modularizer.countTree();
		Module module = modularizer.harvestModule(root, Collections.emptySet());
		
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
		modularizer.setFaultTree(root);
		modularizer.countTree();
		
		FaultTreeHolder fthold = modularizer.getFtHolder();
		FaultTreeNode spare = fthold.getNodeByName("B", Fault.class);
		FaultTreeNode spareGate = fthold.getNodeByName("tle", SPARE.class);
		FaultTreeNode child = fthold.getChildFaults(root).iterator().next();
		
		Module module = modularizer.harvestModule(spareGate, Collections.emptySet());
		final int MODULESIZE_SPAREGATE = 5;
		assertEquals(MODULESIZE_SPAREGATE, module.getNodes().size());
		assertThat(module.getNodes(), allOf(hasItems(child, spareGate, spare), not(hasItems(root))));
	}
	
	@Test
	public void testHarvestModuleRootOnly() {
		Fault root = new Fault(concept);
		root.setName("ROOT");
		
		modularizer.setFaultTree(root);
		modularizer.countTree();
		Module module = modularizer.harvestModule(root, Collections.emptySet());
		
		final int MODULESIZE = 1;
		assertEquals(MODULESIZE, module.getNodes().size());
		assertThat(module.getNodes(), hasItems(root));
	}
	
	@Test
	public void testHarvestModuleSharedSpare() throws IOException {
		Fault root = createDFT("/resources/galileo/or2And2SharedSpare.dft");
		modularizer.setFaultTree(root);
		modularizer.countTree();
		
		FaultTreeNode andGate1 = modularizer.getFtHolder().getNodeByName("AND1", AND.class);
		Module module = modularizer.harvestModule(andGate1, Collections.emptySet());
		assertNull(module);
	}
	
	@Test
	public void testModuleTypeOr2And2Basic() throws IOException {
		Fault root = createDFT("/resources/galileo/or2And2Basic.dft");
		modularizer.setFaultTree(root);
		modularizer.countTree();
		
		FaultTreeNode and1 = modularizer.getFtHolder().getNodeByName("AND1", AND.class);
		
		Module module = modularizer.harvestModule(and1, Collections.emptySet());
		assertTrue(module.isNondeterministic());
	}
	
	@Test
	public void testModuleTypeOr2() throws IOException {
		Fault root = createDFT("/resources/galileo/or2.dft");
		modularizer.setFaultTree(root);
		modularizer.countTree();
		
		FaultTreeNode or = modularizer.getFtHolder().getNodeByName("tle", OR.class);
		
		Module module = modularizer.harvestModule(or, Collections.emptySet());
		assertFalse(module.isNondeterministic());
		assertEquals(or, module.getRootNode());
	}
	
	@Test
	public void testModularizeCSP2() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		
		Set<Module> modules = modularizer.getModules(root);
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		FaultTreeNode spare = ftHolder.getNodeByName("B", Fault.class);
		FaultTreeNode spareBE = ftHolder.getNodeByName("B", BasicEvent.class);
		FaultTreeNode spareGate = ftHolder.getNodeByName("tle", SPARE.class);
		FaultTreeNode primary = ftHolder.getNodeByName("A", Fault.class);
		FaultTreeNode primaryBE = ftHolder.getNodeByName("A", BasicEvent.class);
		
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
		
		Set<Module> modules = modularizer.getModules(root);
		
		FaultTreeHolder ftholder = modularizer.getFtHolder();
		FaultTreeNode and1 = ftholder.getNodeByName("AND1", AND.class);
		FaultTreeNode and2 = ftholder.getNodeByName("AND2", AND.class);
		FaultTreeNode or = ftholder.getNodeByName("OR", OR.class);
		FaultTreeNode spareGate = ftholder.getNodeByName("SPAREGATE", SPARE.class);
		FaultTreeNode c = ftholder.getNodeByName("C", Fault.class);
		FaultTreeNode a = ftholder.getNodeByName("A", Fault.class);
		FaultTreeNode b = ftholder.getNodeByName("B", Fault.class);
		FaultTreeNode d = ftholder.getNodeByName("D", Fault.class);
		
		final int NUM_MODULES = 4;
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
		Module aModule = Module.getModule(modules, a);
		Module dModule = Module.getModule(modules, d);
		
		assertThat(rootModule.getNodes(), allOf(not(hasItems(and1, and2, or, spareGate, a, b, c, d)), hasItems(root)));
		assertThat(orModule.getNodes(), allOf(hasItems(and1, and2, or, spareGate, b, c), not(hasItems(a, d, root))));
		assertThat(aModule.getNodes(), allOf(hasItems(a), not(hasItems(and1, and2, or, spareGate, b, c, d, root))));
		assertThat(dModule.getNodes(), allOf(hasItems(d), not(hasItems(and1, and2, or, spareGate, a, b, c, root))));
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH NESTED PRIORITY GATES
	 * ****************************************************/
	
	@Test
	public void testNestedPrioritySimple() throws IOException {
		Fault root = createDFT("/resources/galileo/nestedPand.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 5;
		assertEquals(NUM_MODULES, modules.size());
		
		FaultTreeHolder ftholder = modularizer.getFtHolder();
		FaultTreeNode and = ftholder.getNodeByName("tle", AND.class);
		FaultTreeNode pand1 = ftholder.getNodeByName("PAND1", PAND.class);
		FaultTreeNode pand2 = ftholder.getNodeByName("PAND2", PAND.class);
		FaultTreeNode a = ftholder.getNodeByName("A", Fault.class);
		FaultTreeNode b = ftholder.getNodeByName("B", Fault.class);
		FaultTreeNode c = ftholder.getNodeByName("C", Fault.class);
		FaultTreeNode d = ftholder.getNodeByName("D", Fault.class);
		FaultTreeNode e = ftholder.getNodeByName("E", Fault.class);
		FaultTreeNode beA = ftholder.getNodeByName("A", BasicEvent.class);
		FaultTreeNode beB = ftholder.getNodeByName("B", BasicEvent.class);
		FaultTreeNode beC = ftholder.getNodeByName("C", BasicEvent.class);
		FaultTreeNode beD = ftholder.getNodeByName("D", BasicEvent.class);
		FaultTreeNode beE = ftholder.getNodeByName("E", BasicEvent.class);
		
		Map<FaultTreeNode, FaultTreeNodePlus> map = modularizer.getTable();
		assertTrue(map.get(b).hasPriorityAbove());
		assertFalse(map.get(a).hasPriorityAbove());
		assertFalse(map.get(pand2).hasPriorityAbove());
		
		Module rootModule = Module.getModule(modules, root);
		Module andModule = Module.getModule(modules, and);
		Module pand1Module = Module.getModule(modules, pand1);
		Module pand2Module = Module.getModule(modules, pand2);
		Module aModule = Module.getModule(modules, a);
		
		//CHECKSTYLE:OFF
		assertThat(rootModule.getNodes(), allOf(hasSize(1), hasItems(root)));
		assertThat(andModule.getNodes(), allOf(hasSize(1), hasItems(and)));
		assertThat(pand1Module.getNodes(), allOf(hasSize(5), hasItems(pand1, c, beC, b, beB)));
		assertThat(pand2Module.getNodes(), allOf(hasSize(5), hasItems(pand2, d, beD, e, beE)));
		assertThat(aModule.getNodes(), allOf(hasSize(2), hasItems(a, beA)));
		//CHECKSTYLE:ON
	}
	
	@Test
	public void testNestedPriorityComplex() throws IOException {
		Fault root = createDFT("/resources/galileo/nestedPand2.dft");
		Set<Module> modules = modularizer.getModules(root);

		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH FDEPS
	 * ****************************************************/
	
	@Test
	public void testModularizeFDEP1() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep1.dft");
		Set<Module> modules = modularizer.getModules(root);
		final int NUM_MODULES = 6;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testModularizeFDEP2Complex() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 7;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	/* *****************************************************
	 *  TESTING MODULARIZER WITH PARTIAL OBSERVABILITY
	 * ****************************************************/
	
	@Test
	public void testModuleTypeObsCsp2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsCsp2.dft");
		
		Set<Module> modules = modularizer.getModules(root);
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		
		FaultTreeNode csp = ftHolder.getNodeByName("tle", SPARE.class);
		FaultTreeNode o = ftHolder.getNodeByName("O", MONITOR.class);
		
		Module rootModule = Module.getModule(modules, root);
		Module cspModule = Module.getModule(modules, csp);
		
		assertFalse(rootModule.isPartialObservable());
		assertTrue(cspModule.isPartialObservable());
		
		assertThat(cspModule.getNodes(), hasItems(o));
	}
	
	@Test
	public void testModuleTypeObsOr2ObsBe2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsOr2ObsBe2.dft");
		
		Set<Module> modules = modularizer.getModules(root);
		final int NUM_MODULES = 4;
		assertEquals(NUM_MODULES, modules.size());
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		
		FaultTreeNode or = ftHolder.getNodeByName("tle", OR.class);
		FaultTreeNode a = ftHolder.getNodeByName("a", Fault.class);
		FaultTreeNode b = ftHolder.getNodeByName("b", Fault.class);
		
		FaultTreeNode o1 = ftHolder.getNodeByName("O1", MONITOR.class);
		FaultTreeNode o2 = ftHolder.getNodeByName("O2", MONITOR.class);
		
		Module rootModule = Module.getModule(modules, root);
		Module orModule = Module.getModule(modules, or);
		Module aModule = Module.getModule(modules, a);
		Module bModule = Module.getModule(modules, b);
		
		assertFalse(rootModule.isPartialObservable());
		assertFalse(orModule.isPartialObservable());
		assertFalse(aModule.isPartialObservable());
		assertFalse(bModule.isPartialObservable());
		
		assertThat(aModule.getNodes(), hasItems(o1));
		assertThat(bModule.getNodes(), hasItems(o2));
	}
	
	@Test
	public void testModuleTypeObsOr2ObsCsp2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsOr2ObsCsp2.dft");
		
		Set<Module> modules = modularizer.getModules(root);
		final int NUM_MODULES = 4;
		assertEquals(NUM_MODULES, modules.size());
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		
		FaultTreeNode or = ftHolder.getNodeByName("tle", OR.class);
		FaultTreeNode s1 = ftHolder.getNodeByName("s1", SPARE.class);
		FaultTreeNode s2 = ftHolder.getNodeByName("s2", SPARE.class);
		
		FaultTreeNode o = ftHolder.getNodeByName("O", MONITOR.class);
		
		Module rootModule = Module.getModule(modules, root);
		Module orModule = Module.getModule(modules, or);
		Module s1Module = Module.getModule(modules, s1);
		Module s2Module = Module.getModule(modules, s2);
		
		assertFalse(rootModule.isPartialObservable());
		assertFalse(orModule.isPartialObservable());
		assertTrue(s1Module.isPartialObservable());
		assertTrue(s2Module.isPartialObservable());
		
		assertThat(s1Module.getNodes(), hasItems(o));
		assertThat(s2Module.getNodes(), hasItems(o));
	}
	
	/* *****************************************************
	 *  TESTING MORE REALISTIC & COMPLETE FAULT TREES
	 * *****************************************************/
	
	@Test
	public void testComplexTree() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep3Spare2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 12;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCMSimple2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm_simple2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 13;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM1() throws IOException {
		Fault root = createDFT("/resources/galileo/cm1.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 13;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 16;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testCM3() throws IOException {
		Fault root = createDFT("/resources/galileo/cm3.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 26;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testFTPP4() throws IOException {
		Fault root = createDFT("/resources/galileo/ftpp4.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
	}
	
	@Test
	public void testAHRS2() throws IOException {
		Fault root = createDFT("/resources/galileo/ahrs2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_MODULES = 2;
		assertEquals(NUM_MODULES, modules.size());
	}
}
