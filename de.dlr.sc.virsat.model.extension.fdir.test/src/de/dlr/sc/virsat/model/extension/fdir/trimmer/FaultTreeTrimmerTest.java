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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;


/**
 * Class to test FaultTreeTrimmer
 * @author jord_ad
 *
 */
public class FaultTreeTrimmerTest {
	
	protected Concept concept;
	protected FaultTreeTrimmer fttrim;
	
	@Before
	public void setUp() throws Exception {
		String conceptXmiPluginPath = "de.dlr.sc.virsat.model.extension.fdir/concept/concept.xmi";
		concept = de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader.loadConceptFromPlugin(conceptXmiPluginPath);
		
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
		FaultTree result = fttrim.trimFaultTree(new FaultTree(concept));
		final int NUM_STATES = 0;
		final int NUM_TRANSITIONS = 0;
		assertEquals(NUM_STATES, result.getChildFaults().size());
		assertEquals(NUM_STATES, result.getChildSpares().size());
		assertEquals(NUM_STATES, result.getDeps().size());
		assertEquals(NUM_STATES, result.getGates().size());
		assertEquals(NUM_TRANSITIONS, result.getPropagations().size());
		
		Set<Module> resultSet = fttrim.trimModules(new HashSet<Module>());
		assertTrue(resultSet.isEmpty());
	}

}
