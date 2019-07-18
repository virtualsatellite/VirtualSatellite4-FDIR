/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.hasItem;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.model.dvlm.structural.util.StructuralInstantiator;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

// *****************************************************************
// * Import Statements
// *****************************************************************



// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class FMECATest extends AFMECATest {
	
	private static final double TEST_EPSILON = 0.000001;
	
	private BeanStructuralElementInstance beanSei;
	private FMECA fmeca;
	private FaultTreeHelper ftHelper;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		ftHelper = new FaultTreeHelper(concept);
		
		StructuralElement se = StructuralFactory.eINSTANCE.createStructuralElement();
		StructuralElementInstance sei = new StructuralInstantiator().generateInstance(se, "parent");
		beanSei = new BeanStructuralElementInstance(sei);
		
		fmeca = new FMECA(concept);
		beanSei.add(fmeca);
	}
	
	@Test
	public void testGenerateEntries() {
		Fault fault = new Fault(concept);
		fault.setSeverity("Critical");
		beanSei.add(fault);
	
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fault.getSeverity(), entry.getSeverity());
		assertNull(entry.getFailureMode());
		assertNull(entry.getFailureCause());
		assertTrue(entry.getFailureEffects().isEmpty());
		assertTrue(entry.getProposedRecovery().isEmpty());
	}
	
	@Test
	public void testGenerateEntriesEffect() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		Fault effect1 = new Fault(concept);
		beanSei.add(effect1);
		Fault effect2 = new Fault(concept);
		beanSei.add(effect2);
		ftHelper.connect(effect1, fault, effect1);
		ftHelper.connect(effect2, fault, effect2);
		
		FMECAEntry entry = fmeca.generateEntries(new NullProgressMonitor()).get(0);
		assertThat(entry.getFailureEffects(), hasItem(effect1));
		assertThat(entry.getFailureEffects(), hasItem(effect2));
	}
	
	@Test
	public void testGenerateEntriesFailureMode() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		BasicEvent fm1 = new BasicEvent(concept);
		fm1.setHotFailureRate(1);
		Fault fm2 = new Fault(concept);
		fault.getBasicEvents().add(fm1);
		ftHelper.connect(fault, fm2, fault);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(2, entries.size());
		
		FMECAEntry entry1 = entries.get(0);
		assertEquals(fault, entry1.getFailure());
		assertEquals(fm1, entry1.getFailureMode());
		assertEquals(1, entry1.getMeanTimeToFailure(), TEST_EPSILON);
		
		FMECAEntry entry2 = entries.get(1);
		assertEquals(fault, entry2.getFailure());
		assertEquals(fm2, entry2.getFailureMode());
		assertFalse(entry2.isSetMeanTimeToFailure());
	}
	
	@Test
	public void testGenerateEntriesCause() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		
		Fault fm = new Fault(concept);
		
		BasicEvent cause1 = new BasicEvent(concept);
		cause1.setHotFailureRate(1);
		Fault cause2 = new Fault(concept);
		BasicEvent be = new BasicEvent(concept);
		be.setHotFailureRate(1);
		
		ftHelper.connect(fault, fm, fault);
		fm.getBasicEvents().add(cause1);
		ftHelper.connect(fm, cause2, fm);
		cause2.getBasicEvents().add(be);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(2, entries.size());
		
		FMECAEntry entry1 = entries.get(0);
		assertEquals(fault, entry1.getFailure());
		assertEquals(fm, entry1.getFailureMode());
		assertEquals(cause1, entry1.getFailureCause());
		assertEquals(1, entry1.getMeanTimeToFailure(), TEST_EPSILON);
		
		FMECAEntry entry2 = entries.get(0);
		assertEquals(fault, entry2.getFailure());
		assertEquals(fm, entry2.getFailureMode());
		assertEquals(cause2, entry2.getFailureCause());
		assertEquals(1, entry1.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testGenerateEntriesMitigation() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		
		Fault fm = new Fault(concept);
		Fault red = new Fault(concept);
		red.setName("Redundancy");
		BasicEvent cause = new BasicEvent(concept);
		cause.setRepairAction("Reset");
		
		fm.getBasicEvents().add(cause);
		cause.setHotFailureRate(1);
		
		SPARE spareGate = (SPARE) ftHelper.createGate(fault, FaultTreeNodeType.SPARE);
		ftHelper.connect(fault, spareGate, fault);
		ftHelper.connect(fault, fm, spareGate);
		ftHelper.connectSpare(fault, red, spareGate);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fm, entry.getFailureMode());
		assertEquals(cause, entry.getFailureCause());
		assertEquals(1, entry.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(cause.getRepairAction(), entry.getProposedRecovery().get(0));
		assertEquals("Switch to Redundancy", entry.getProposedRecovery().get(1));
	}
}
