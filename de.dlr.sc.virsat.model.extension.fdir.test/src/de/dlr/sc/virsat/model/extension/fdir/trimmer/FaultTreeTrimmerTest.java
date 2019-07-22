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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
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
	public void setUp() throws Exception {
		super.set();
		
		fttrim = new FaultTreeTrimmer();
	}
	
	@Test
	public void testNull() {
		FaultTree result = fttrim.trimFaultTree(null);
		assertNull(result);
		
		Set<Module> resultSet = fttrim.trimModules(null);
		assertNull(resultSet);
	}
	
	@Test
	public void testNone() {
		Set<Module> resultSet = fttrim.trimModules(new HashSet<Module>());
		assertTrue(resultSet.isEmpty());
	}
	
	@Test
	public void testTrimNondeterministicModules() throws IOException {
		Fault rootNestedComplex = createDFT("/resources/galileo/nestedPand2.dft");
		Modularizer modularizer = new Modularizer();
		Set<Module> modules = modularizer.getModules(rootNestedComplex.getFaultTree());
		modules = fttrim.trimModules(modules);
		
		final int NUM_NONDET_MODULES = 1;
		assertEquals(NUM_NONDET_MODULES, modules.size());
	}
	
	@Test
	public void testTrimCM2() throws IOException {
		Fault rootNestedComplex = createDFT("/resources/galileo/cm2.dft");
		Modularizer modularizer = new Modularizer();
		Set<Module> modules = modularizer.getModules(rootNestedComplex.getFaultTree());
		modules = fttrim.trimModules(modules);
		
		final int NUM_NONDET_MODULES = 4;
		assertEquals(NUM_NONDET_MODULES, modules.size());
	}

}
