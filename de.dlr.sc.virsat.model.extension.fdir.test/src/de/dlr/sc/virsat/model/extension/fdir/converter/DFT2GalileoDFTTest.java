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

import org.junit.Test;

import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.model.dvlm.structural.util.StructuralInstantiator;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the conversion from a fault model in the FDIR concept to 
 * a GalileoDFT
 * @author muel_s8
 *
 */

public class DFT2GalileoDFTTest extends ATestCase {
	
	@Test
	public void testConvert() {
		DFT2GalileoDFT converter = new DFT2GalileoDFT();
		
		StructuralElement se = StructuralFactory.eINSTANCE.createStructuralElement();
		StructuralElementInstance sei = new StructuralInstantiator().generateInstance(se, "FUNCTION");
		BeanStructuralElementInstance function = new BeanStructuralElementInstance(sei);
		
		Fault tle = new Fault(concept);
		tle.setName("TLE");
		function.add(tle);
		
		BasicEvent be1 = new BasicEvent(concept);
		be1.setName("BE1");
		be1.setHotFailureRate(1);
		be1.setRepairRate(1);
		tle.getBasicEvents().add(be1);
		
		Gate gate = ftHelper.createGate(tle, FaultTreeNodeType.AND);
		
		Fault fault = new Fault(concept);
		fault.setName("FAULT");
		function.add(fault);
		
		BasicEvent be2 = new BasicEvent(concept);
		be2.setName("BE2");
		be2.setHotFailureRate(2);
		fault.getBasicEvents().add(be2);
		
		ftHelper.connect(tle, fault, gate);
		ftHelper.connect(tle, gate, tle);
		
		final int TOTAL_NUMBER_OF_BASIC_EVENTS = 2;
		final int TOTAL_NUMBER_OF_GATES = 3;
		
		GalileoDft galileoDft = converter.convert(sei);
		
		GalileoFaultTreeNode galileoRoot = galileoDft.getRoot();
		assertEquals("Root has correct name", tle.getTypeInstance().getFullQualifiedInstanceName(), galileoRoot.getName());
		assertEquals("GalileoDFT has correct number of nodes", TOTAL_NUMBER_OF_BASIC_EVENTS, galileoDft.getBasicEvents().size());
		assertEquals("GalileoDFT has correct number of nodes", TOTAL_NUMBER_OF_GATES, galileoDft.getGates().size());
	}

}
