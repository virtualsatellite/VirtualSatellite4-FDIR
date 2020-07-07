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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.model.dvlm.structural.util.StructuralInstantiator;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;

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
	private FaultTreeBuilder ftBuilder;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		ftBuilder = new FaultTreeBuilder(concept);
		
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
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		beanSei.add(fdirParameters);
		beanSei.add(fault);
	
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fault.getSeverity(), entry.getSeverity());
		assertNull(entry.getFailureMode());
		assertNull(entry.getFailureCause());
		assertTrue(entry.getFailureEffects().isEmpty());
		assertTrue(entry.getCompensationBean().isEmpty());
	}
	
	@Test
	public void testGenerateEntriesEffect() {
		ResourceSet rs = new ResourceSetImpl();
		Resource resource = new ResourceImpl();
		rs.getResources().add(resource);
		
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		Fault effect1 = new Fault(concept);
		Fault effect2 = new Fault(concept);
		ftBuilder.connect(effect1, fault, effect1);
		ftBuilder.connect(effect2, fault, effect2);
		
		resource.getContents().add(fault.getATypeInstance());
		resource.getContents().add(effect1.getATypeInstance());
		resource.getContents().add(effect2.getATypeInstance());
		
		FMECAEntry entry = fmeca.generateEntries(new NullProgressMonitor()).get(0);
		assertThat(entry.getFailureEffects(), hasItem(effect1));
		assertThat(entry.getFailureEffects(), hasItem(effect2));
	}
	
	@Test
	public void testGenerateEntriesFailureMode1() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		BasicEvent fm = new BasicEvent(concept);
		fm.setHotFailureRate(1);
		fault.getBasicEvents().add(fm);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fm, entry.getFailureMode());
		assertEquals(1, entry.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testGenerateEntriesFailureMode2() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		Fault fm = new Fault(concept);
		ftBuilder.connect(fault, fm, fault);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fm, entry.getFailureMode());
		assertEquals(Double.POSITIVE_INFINITY, entry.getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testGenerateEntriesFailureMode3() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		Fault fm1 = new Fault(concept);
		fm1.setName("B");
		ftBuilder.connect(fault, fm1, fault);
		Fault fm2 = new Fault(concept);
		fm2.setName("A");
		ftBuilder.connect(fault, fm2, fault);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(2, entries.size());
		
		FMECAEntry entry1 = entries.get(0);
		assertEquals(fm2, entry1.getFailureMode());
		
		FMECAEntry entry2 = entries.get(1);
		assertEquals(fm1, entry2.getFailureMode());
	}
	
	@Test
	public void testGenerateEntriesCause1() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		
		Fault fm = new Fault(concept);
		
		BasicEvent cause = new BasicEvent(concept);
		cause.setHotFailureRate(1);
		
		ftBuilder.connect(fault, fm, fault);
		fm.getBasicEvents().add(cause);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fm, entry.getFailureMode());
		assertEquals(cause, entry.getFailureCause());
		assertEquals(1, entry.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testGenerateEntriesCause2() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		
		Fault fm = new Fault(concept);
		Fault cause = new Fault(concept);
		BasicEvent be = new BasicEvent(concept);
		be.setHotFailureRate(1);
		
		ftBuilder.connect(fault, fm, fault);
		ftBuilder.connect(fm, cause, fm);
		cause.getBasicEvents().add(be);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(fault, entry.getFailure());
		assertEquals(fm, entry.getFailureMode());
		assertEquals(cause, entry.getFailureCause());
		assertEquals(1, entry.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testGenerateEntriesMitigation() {
		Fault fault = new Fault(concept);
		beanSei.add(fault);
		
		Fault fm = new Fault(concept);
		Fault red = new Fault(concept);
		red.setName("Redundancy");
		BasicEvent cause = new BasicEvent(concept);
		
		RepairAction repairAction = new RepairAction(concept);
		repairAction.setName("Reset");
		cause.getRepairActions().add(repairAction);
		
		fm.getBasicEvents().add(cause);
		cause.setHotFailureRate(1);
		
		SPARE spareGate = (SPARE) ftBuilder.createGate(fault, FaultTreeNodeType.SPARE);
		ftBuilder.connect(fault, spareGate, fault);
		ftBuilder.connect(fault, fm, spareGate);
		ftBuilder.connectSpare(fault, red, spareGate);
		
		List<FMECAEntry> entries = fmeca.generateEntries(new NullProgressMonitor());
		assertEquals(1, entries.size());
		
		FMECAEntry entry = entries.get(0);
		assertEquals(2, entry.getCompensationBean().size());
		assertEquals(repairAction.getName(), entry.getCompensationBean().get(1).getValue());
		assertEquals("Switch to Redundancy", entry.getCompensationBean().get(0).getValue());
	}
}
