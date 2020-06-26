/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.AND;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the FaultTreeHolder
 * @author muel_s8
 *
 */

public class FaultTreeHolderTest extends ATestCase {

	@Test
	public void testGetNodeByName() throws IOException {
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		assertEquals(ftHolder.getNodeByName("tle", Fault.class), fault);
		assertTrue(ftHolder.getNodeByName("A", BasicEvent.class) instanceof BasicEvent);
	}
	
	@Test
	public void testGetChildFaults() {
		// Propagation flow: grandchild -> child -> intermediateNode -> root
		Fault root = new Fault(concept);
		AND intermediateNode = new AND(concept);
		root.getFaultTree().getGates().add(intermediateNode);
		Fault child = new Fault(concept);
		Fault grandchild = new Fault(concept);
		Fault notAChild = new Fault(concept);
		
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);
		ftBuilder.connect(root, intermediateNode, root);
		ftBuilder.connect(root, child, intermediateNode);
		ftBuilder.connect(child, grandchild, child);
		ftBuilder.connect(root, grandchild, notAChild);
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		Set<Fault> childFaults = ftHolder.getChildFaults(root);
		
		assertThat(childFaults, hasItems(child));
		assertThat(childFaults, not(hasItem(grandchild)));
		assertThat(childFaults, not(hasItem(notAChild)));
	}
}
