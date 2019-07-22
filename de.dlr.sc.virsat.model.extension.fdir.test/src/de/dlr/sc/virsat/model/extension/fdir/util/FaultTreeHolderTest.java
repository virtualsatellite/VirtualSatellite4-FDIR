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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

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
}
