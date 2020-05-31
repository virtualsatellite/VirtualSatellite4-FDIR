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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;


/**
 * Class to test FaultTreeTrimmer
 * @author jord_ad
 *
 */
public class FaultTreeTrimmerTest extends ATestCase {
	
	private FaultTreeTrimmer fttrim;
	private Modularizer modularizer;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		fttrim = new FaultTreeTrimmer();
		modularizer = new Modularizer();
	}
	
	@Test
	public void testNone() {
		Set<Module> resultSet = fttrim.trimModulesAll(new HashSet<Module>());
		assertTrue(resultSet.isEmpty());
	}
	
	@Test
	public void testDeterministicTree() throws IOException {
		Fault root = createDFT("/resources/galileo/and2or.dft");
		Set<Module> modules = modularizer.getModules(root);
		modules = fttrim.trimModulesAll(modules);
		
		assertTrue(modules.isEmpty());
	}
	
	@Test
	public void testTrimNondeterministicModules() throws IOException {
		Fault root = createDFT("/resources/galileo/nestedPand2.dft");
		Set<Module> modules = modularizer.getModules(root);
		modules = fttrim.trimModulesAll(modules);
		
		final int NUM_NONDET_MODULES = 1;
		assertEquals(NUM_NONDET_MODULES, modules.size());
	}
	
	@Test
	public void testTrimCM2() throws IOException {
		Fault root = createDFT("/resources/galileo/cm2.dft");
		Set<Module> modules = modularizer.getModules(root);
		modules = fttrim.trimModulesAll(modules);
		
		final int NUM_NONDET_MODULES = 4;
		assertEquals(NUM_NONDET_MODULES, modules.size());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testTrimSideNode() throws IOException {
		Fault root = createDFT("/resources/galileo/sharedSpareWithSideNode.dft");
		Set<Module> modules = modularizer.getModules(root);
		
		assertFalse(modules.isEmpty());
		
		modules = fttrim.trimModulesAll(modules);
		final int NUM_NODES_IN_UNWANTED_MODULE = 2;
		Module module = modules.stream().filter(m -> m.getNodes().size() > NUM_NODES_IN_UNWANTED_MODULE)
				.findAny().get();
		
		FaultTreeHolder ftholder = new FaultTreeHolder(module.getRootNodeCopy().getFault());
		ftholder.getNodeByName("side node", Fault.class);
	}

}
