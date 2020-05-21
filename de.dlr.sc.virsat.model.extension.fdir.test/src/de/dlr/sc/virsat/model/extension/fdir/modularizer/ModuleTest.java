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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;

/**
 * Class to test Modules
 * @author jord_ad
 *
 */
public class ModuleTest extends ATestCase {
	
	protected Modularizer modularizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		modularizer = new Modularizer();
	}
	
	@Test
	public void testEvaluateCsp2() throws IOException {
		Fault rootcsp2 = createDFT("/resources/galileo/csp2.dft");
		Set<Module> modules = modularizer.getModules(rootcsp2.getFaultTree());
		final int NUM_NODES_IN_SHARED_SPARE_MODULE = 3;
		Module module = modules.stream().filter(m -> m.getNodes().size() >= NUM_NODES_IN_SHARED_SPARE_MODULE)
				.findAny().get();
		module.constructFaultTreeCopy();
	
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
	}

	@Test
	public void testCopyFaultTreeCMSimple() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		Set<Module> modules = modularizer.getModules(rootCMSimple.getFaultTree());
		Module module = modules.stream().filter(m -> m.isNondeterministic()).findAny().get();
		module.constructFaultTreeCopy();
		
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
	}
	
	@Test
	public void testCopyFaultTreeCM1() throws IOException {
		Fault rootCM1 = createDFT("/resources/galileo/cm1.dft");
		Set<Module> modules = modularizer.getModules(rootCM1.getFaultTree());
		
		final int NUM_NODES_IN_SHARED_SPARE_MODULE = 8;
		Module module = modules.stream().filter(m -> m.getNodes().size() > NUM_NODES_IN_SHARED_SPARE_MODULE)
				.findAny().get();

		module.constructFaultTreeCopy();
		assertEquals("Fault", module.getRootNodeCopy().getName());

		final int NUM_NODES = 17;
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
		assertEquals(NUM_NODES, ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size());
	}
	
	@Test
	public void testCopyFaultTreeCM2() throws IOException {
		Fault rootCM2 = createDFT("/resources/galileo/cm2.dft");
		Set<Module> modules = modularizer.getModules(rootCM2.getFaultTree());
		final int NUM_NODES_IN_MODULE_DESIRED = 8;
		Module module = modules.stream().filter(m -> m.getNodes().size() > NUM_NODES_IN_MODULE_DESIRED)
				.findAny().get();
		module.constructFaultTreeCopy();
		
		assertEquals("Fault", module.getRootNodeCopy().getName());
		
		final int NUM_NODES = 23;
		final int NUM_SPARES = 3;
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
		assertEquals(NUM_NODES, ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size());
		assertEquals(NUM_SPARES, ftHelper.getAllEdges(module.getRootNodeCopy().getFault(), EdgeType.SPARE).size());
	}
}
