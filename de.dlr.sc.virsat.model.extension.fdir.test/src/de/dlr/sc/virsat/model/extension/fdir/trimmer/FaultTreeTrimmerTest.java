/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.trimmer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Modularizer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;


/**
 * Class to test FaultTreeTrimmer
 * @author jord_ad
 *
 */
public class FaultTreeTrimmerTest extends ATestCase {
	
	protected FaultTreeTrimmer fttrim;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		fttrim = new FaultTreeTrimmer();
	}
	
	@Test
	public void testNull() {
		FaultTree result = fttrim.trimFaultTree(null);
		assertNull(result);
		
		Set<Module> resultSet = fttrim.trimModulesAll(null);
		assertNull(resultSet);
	}
	
	@Test
	public void testNone() {
		Set<Module> resultSet = fttrim.trimModulesAll(new HashSet<Module>());
		assertTrue(resultSet.isEmpty());
	}
	
	@Test
	public void testTrimNondeterministicModules() throws IOException {
		Fault rootNestedComplex = createDFT("/resources/galileo/nestedPand2.dft");
		Modularizer modularizer = new Modularizer();
		Set<Module> modules = modularizer.getModules(rootNestedComplex.getFaultTree());
		modules.forEach(module -> module.constructFaultTreeCopy());
		modules = fttrim.trimModulesAll(modules);
		
		final int NUM_NONDET_MODULES = 1;
		assertEquals(NUM_NONDET_MODULES, modules.size());
	}
	
	@Test
	public void testTrimCM2() throws IOException {
		Fault rootNestedComplex = createDFT("/resources/galileo/cm2.dft");
		Modularizer modularizer = new Modularizer();
		Set<Module> modules = modularizer.getModules(rootNestedComplex.getFaultTree());
		modules.forEach(module -> module.constructFaultTreeCopy());
		modules = fttrim.trimModulesAll(modules);
		
		final int NUM_NONDET_MODULES = 4;
		assertEquals(NUM_NONDET_MODULES, modules.size());
	}
	
	@Test
	public void testTrimSideNode() throws IOException {
		Fault rootSideNode = createDFT("/resources/galileo/sharedSpareWithSideNode.dft");
		Modularizer modularizer = new Modularizer();
		Set<Module> modules = modularizer.getModules(rootSideNode.getFaultTree());
		modules.forEach(m -> m.constructFaultTreeCopy());
		
		final int NUM_NODES_IN_UNWANTED_MODULE = 2;
		Module module = modules.stream().filter(m -> m.getNodes().size() > NUM_NODES_IN_UNWANTED_MODULE)
				.findAny().get();
		FaultTreeHolder fthold = new FaultTreeHolder(module.getRootNodeCopy().getFault());
		FaultTreeNode sideNode = fthold.getNodeByName("side node", Fault.class);
		
		modules = fttrim.trimModulesAll(modules);
		assertFalse(modules.isEmpty());
		assertFalse(ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).contains(sideNode));
	}

}
