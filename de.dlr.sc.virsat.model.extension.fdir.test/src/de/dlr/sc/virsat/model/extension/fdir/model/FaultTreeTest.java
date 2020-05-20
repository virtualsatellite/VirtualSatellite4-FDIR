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
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;

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
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public class FaultTreeTest extends AFaultTreeTest {
	
	@Test
	public void testGetRoot() {
		Fault fault = new Fault(concept);
		assertEquals(fault, fault.getFaultTree().getRoot());
	}
	
	@Test
	public void testGetChildSpares() {
		Fault root = new Fault(concept);
		SPARE spareGate = new SPARE(concept);
		Fault child = new Fault(concept);
		Fault spare = new Fault(concept);
		
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);
		ftBuilder.connect(root, spareGate, root);
		ftBuilder.connect(root, child, spareGate);
		ftBuilder.connectSpare(root, spare, spareGate);
		
		Set<Fault> childSpares = root.getFaultTree().getChildSpares();
		
		assertThat(childSpares, hasItem(spare));
		assertThat(childSpares, not(hasItem(child)));
	}
	
	@Test
	public void testGetAffectedFaults() {
		ResourceSet rs = new ResourceSetImpl();
		Resource resource = new ResourceImpl();
		rs.getResources().add(resource);
		
		// Propagation flow: grandchild -> child -> intermediateNode -> root
		Fault root = new Fault(concept);
		AND intermediateNode = new AND(concept);
		Fault child = new Fault(concept);
		Fault grandchild = new Fault(concept);
		
		resource.getContents().add(root.getTypeInstance());
		root.getFaultTree().getGates().add(intermediateNode);
		resource.getContents().add(child.getTypeInstance());
		resource.getContents().add(grandchild.getTypeInstance());
		
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);
		ftBuilder.connect(root, intermediateNode, root);
		ftBuilder.connect(root, child, intermediateNode);
		ftBuilder.connect(child, grandchild, child);
		
		Set<Fault> affectedFaults = child.getFaultTree().getAffectedFaults();
		
		assertThat(affectedFaults, hasItem(root));
		assertThat(affectedFaults, not(hasItem(grandchild)));
	}
	
	@Test
	public void testGetPotentialRecoveryActions() {
		StructuralElement se = StructuralFactory.eINSTANCE.createStructuralElement();
		StructuralElementInstance sei = new StructuralInstantiator().generateInstance(se, "System");
		
		Fault root = new Fault(concept);
		SPARE spareGate = new SPARE(concept);
		root.getFaultTree().getGates().add(spareGate);
		Fault child = new Fault(concept);
		Fault spare = new Fault(concept);
		BasicEvent be = new BasicEvent(concept);
		
		sei.getCategoryAssignments().add(root.getTypeInstance());
		
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);
		ftBuilder.connect(root, spareGate, root);
		ftBuilder.connect(root, child, spareGate);
		ftBuilder.connectSpare(root, spare, spareGate);
		root.getBasicEvents().add(be);
		be.setRepairAction("Restart");
		
		List<String> potentialRecoveryActions = root.getFaultTree().getPotentialRecoveryActions();
		
		assertEquals(2, potentialRecoveryActions.size());
		assertThat(potentialRecoveryActions, hasItem(be.getRepairAction()));
	}
}
