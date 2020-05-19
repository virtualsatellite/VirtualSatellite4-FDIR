/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.concept.types.structural.ABeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.model.extension.fdir.converter.galileo.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * Tests the conversion from a galileo DFT to a VirSat DFT.
 * @author muel_s8
 *
 */

public class GalileoDFT2DFTTest extends ATestCase {
	
	ABeanStructuralElementInstance parent;
	
	@Before
	public void setup() {
		StructuralElement se = StructuralFactory.eINSTANCE.createStructuralElement();
		StructuralElementInstance sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		
		sei.setType(se);
		parent = new BeanStructuralElementInstance(sei);
	}
	
	@Test
	public void testConvertToplevel() throws IOException {
		InputStream is = resourceGetter.getResourceContentAsStream("/resources/galileo/failureMode.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is, parent);
		
		converter.convert();
		
		assertFalse("Contains at least one entry", parent.getAll(Fault.class).isEmpty());
		assertEquals("Name of fault is set", "FailureMode", parent.getAll(Fault.class).get(0).getName());
	}
	
	@Test
	public void testConvert2of3() throws IOException {
		InputStream is = resourceGetter.getResourceContentAsStream("/resources/galileo/2of3.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is, parent);
		
		converter.convert();
		
		List<Fault> faults = parent.getAll(Fault.class);
		
		final int EXPECTED_FMECA_ENTRY_COUNT = 4;
		assertEquals("FMECA contains correct number of entry", EXPECTED_FMECA_ENTRY_COUNT, faults.size());
		
		final int FAULT_A_ID = 0;
		Fault faultA = faults.get(FAULT_A_ID);
		assertEquals("Name of fault is set", "A", faultA.getName());
		assertEquals("FaultTree contains correct number of nodes", 1, faultA.getFaultTree().getGates().size());
		
		VOTE vote = (VOTE) faultA.getFaultTree().getGates().get(0);
		assertEquals("Voting gate has correct type", FaultTreeNodeType.VOTE, vote.getFaultTreeNodeType());
		assertEquals("Name of 2of3 voting gate set", "A", vote.getName());
		assertEquals("Voting treshold is set correctly", 2, vote.getVotingThreshold());
		
		final int FAULT_B_ID = 1;
		Fault faultB = faults.get(FAULT_B_ID);
		assertEquals("Name of fault is set", "B", faultB.getName());
		assertFalse("Fault B has failure mode", faultB.getBasicEvents().isEmpty());
		final double EXPECTED_FAILURE_RATE_B = 0.1;
		assertEquals("Failure rate is correct", EXPECTED_FAILURE_RATE_B, faultB.getBasicEvents().get(0).getHotFailureRate(), TEST_EPSILON);
		
		final int FAULT_C_ID = 2;
		Fault faultC = faults.get(FAULT_C_ID);
		assertEquals("Name of fault is set", "C", faultC.getName());
		assertFalse("Fault B has failure mode", faultC.getBasicEvents().isEmpty());
		final double EXPECTED_FAILURE_RATE_C = 0.2;
		assertEquals("Failure rate is correct", EXPECTED_FAILURE_RATE_C, faultC.getBasicEvents().get(0).getHotFailureRate(), TEST_EPSILON);
		
		final int FAULT_D_ID = 3;
		Fault faultD = faults.get(FAULT_D_ID);
		assertEquals("Name of fault is set", "D", faultD.getName());
		assertFalse("Fault B has failure mode", faultD.getBasicEvents().isEmpty());
		final double EXPECTED_FAILURE_RATE_D = 0.3;
		assertEquals("Failure rate is correct", EXPECTED_FAILURE_RATE_D, faultD.getBasicEvents().get(0).getHotFailureRate(), TEST_EPSILON);
	}
	
	@Test
	public void testSF() throws IOException {
		InputStream is = resourceGetter.getResourceContentAsStream("/resources/galileo/sf.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is, parent);
		
		converter.convert();
		final int EXPECTED_FMECA_ENTRY_COUNT = 6;
		assertEquals("FMECA contains correct number of entry", EXPECTED_FMECA_ENTRY_COUNT, parent.getAll(Fault.class).size());
		
		Fault faultA = parent.getFirst(Fault.class);
		assertEquals("Name of fault is set", "Filter_1", faultA.getName());
		final int EXPECTED_FAULTY_NODE_COUNT = 4;
		assertEquals("FaultTree contains correct number of nodes", EXPECTED_FAULTY_NODE_COUNT, faultA.getFaultTree().getGates().size());
	}
	
	@Test
	public void testFDEP1() throws IOException {
		InputStream is = resourceGetter.getResourceContentAsStream("/resources/galileo/fdep1.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is, parent);
		
		converter.convert();
		
		List<Fault> faults = parent.getAll(Fault.class);
		
		final int EXPECTED_FMECA_ENTRY_COUNT = 3;
		assertEquals("FMECA contains correct number of entry", EXPECTED_FMECA_ENTRY_COUNT, faults.size());
		
		Fault faultA = faults.get(0);
		assertEquals("Name of fault is set", "TLE", faultA.getName());
		final int EXPECTED_FAULTY_NODE_COUNT = 2;
		assertEquals("FaultTree contains correct number of nodes", EXPECTED_FAULTY_NODE_COUNT, faultA.getFaultTree().getGates().size());
		
		final int EXPECTED_DEPS_COUNT = 1;
		assertEquals("FaultTree contains correct number of dependencies", EXPECTED_DEPS_COUNT, faultA.getFaultTree().getDeps().size());
	}

	@Test
	public void testObsCsp() throws IOException {
		InputStream is = resourceGetter.getResourceContentAsStream("/resources/galileoObs/obsCsp2.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is, parent);
		
		converter.convert();
		
		List<Fault> faults = parent.getAll(Fault.class);
		
		final int EXPECTED_FMECA_ENTRY_COUNT = 3;
		assertEquals("FMECA contains correct number of entry", EXPECTED_FMECA_ENTRY_COUNT, faults.size());
		
		List<FaultTreeEdge> observations = faults.get(0).getFaultTree().getObservations();
		final int EXPECTED_OBSERVATION_RELATIONS = 1;
		assertEquals("FaultTree contains correct number of observation relations", EXPECTED_OBSERVATION_RELATIONS, observations.size());
	}
	
	@Test
	public void testCsp2Repair2() throws IOException {
		InputStream is = resourceGetter.getResourceContentAsStream("/resources/galileoRepair/csp2Repair2BadPrimary.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is, parent);
		
		converter.convert();
		
		List<Fault> faults = parent.getAll(Fault.class);
		
		final int EXPECTED_FMECA_ENTRY_COUNT = 3;
		assertEquals("FMECA contains correct number of entry", EXPECTED_FMECA_ENTRY_COUNT, faults.size());
		
		final double EXPECTED_REPAIR_RATE_1 = 1;
		assertEquals("Repair rate is correct", EXPECTED_REPAIR_RATE_1, faults.get(1).getBasicEvents().get(0).getRepairRate(), TEST_EPSILON);
		
		final double EXPECTED_REPAIR_RATE_2 = 0.5;
		assertEquals("Repair rate is correct", EXPECTED_REPAIR_RATE_2, faults.get(2).getBasicEvents().get(0).getRepairRate(), TEST_EPSILON);
	}

}
