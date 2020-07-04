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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

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
	public void testCopyFaultTreeCsp2() throws IOException {
		Fault root = createDFT("/resources/galileo/csp2.dft");
		Set<Module> modules = modularizer.getModules(root);
		final int NUM_NODES_IN_SHARED_SPARE_MODULE = 3;
		Module module = modules.stream().filter(m -> m.getNodes().size() >= NUM_NODES_IN_SHARED_SPARE_MODULE).findAny().get();
		module.constructFaultTreeCopy();
	
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
	}

	@Test
	public void testCopyFaultTreeCMSimple2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm_simple2.dft");
		Set<Module> modules = modularizer.getModules(root);
		Module module = modules.stream().filter(Module::isNondeterministic).findAny().get();
		module.constructFaultTreeCopy();
		
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
	}
	
	@Test
	public void testCopyFaultTreeCM1() throws IOException {
		Fault root = createDFT("/resources/galileo/cm1.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		final int NUM_NODES_IN_SHARED_SPARE_MODULE = 8;
		Module module = modules.stream().filter(m -> m.getNodes().size() > NUM_NODES_IN_SHARED_SPARE_MODULE).findAny().get();

		module.constructFaultTreeCopy();
		assertEquals("Fault", module.getRootNodeCopy().getName());

		final int NUM_NODES = 17;
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
		assertEquals(NUM_NODES, ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size());
	}
	
	@Test
	public void testCopyFaultTreeCM2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm2.dft");
		Set<Module> modules = modularizer.getModules(root);
		final int NUM_NODES_IN_MODULE_DESIRED = 8;
		Module module = modules.stream().filter(m -> m.getNodes().size() > NUM_NODES_IN_MODULE_DESIRED).findAny().get();
		module.constructFaultTreeCopy();
		
		assertEquals("Fault", module.getRootNodeCopy().getName());
		
		final int NUM_NODES = 23;
		final int NUM_SPARES = 3;
		assertTrue("Number of nodes in tree copy should be >= number of nodes in module", ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size() >= module.getNodes().size());
		assertEquals(NUM_NODES, ftHelper.getAllNodes(module.getRootNodeCopy().getFault()).size());
		assertEquals(NUM_SPARES, ftHelper.getAllEdges(module.getRootNodeCopy().getFault(), EdgeType.SPARE).size());
	}
	
	@Test
	public void testCopyFaultTreeObsCsp2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsCsp2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		FaultTreeNode csp = ftHolder.getNodeByName("tle", SPARE.class);
		Module module = Module.getModule(modules, csp);
		
		module.constructFaultTreeCopy();
		
		ftHolder = new FaultTreeHolder(module.getRootNodeCopy());
		
		csp = ftHolder.getNodeByName("tle", SPARE.class);
		List<FaultTreeNode> monitors = ftHolder.getNodes(csp, EdgeType.MONITOR);
		FaultTreeNode o = ftHolder.getNodeByName("O", MONITOR.class);
		
		assertThat(monitors, hasItems(o));
		assertThat(monitors, hasSize(1));
	}
	
	@Test
	public void testCopyFaultTreeObsOr2ObsBe2() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsOr2ObsBe2.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		FaultTreeNode a = ftHolder.getNodeByName("a", Fault.class);
		
		Module module = Module.getModule(modules, a);
		
		module.constructFaultTreeCopy();
		
		ftHolder = new FaultTreeHolder(module.getRootNodeCopy());
		
		a = ftHolder.getNodeByName("a", Fault.class);
		
		List<FaultTreeNode> monitors = ftHolder.getNodes(a, EdgeType.MONITOR);
		assertTrue(monitors.isEmpty());
	}
	
	@Test
	public void testCopyFaultTreObsCsp2Unreliable() throws IOException {
		Fault root = createDFT("/resources/galileoObs/obsCsp2Unreliable.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		FaultTreeHolder ftHolder = modularizer.getFtHolder();
		Module module = Module.getModule(modules, root);
		
		module.constructFaultTreeCopy();
		
		ftHolder = new FaultTreeHolder(module.getRootNodeCopy());
		
		FaultTreeNode a = ftHolder.getNodeByName("a", Fault.class);
		FaultTreeNode c = ftHolder.getNodeByName("c", Fault.class);
		FaultTreeNode o = ftHolder.getNodeByName("O", MONITOR.class);
		
		List<FaultTreeNode> monitors = ftHolder.getNodes(a, EdgeType.MONITOR);
		assertThat(monitors, hasItems(o));
		assertThat(monitors, hasSize(1));
		
		List<FaultTreeNode> children = ftHolder.getNodes(o, EdgeType.CHILD);
		assertThat(children, hasItems(c));
		assertThat(children, hasSize(1));
	}
}
