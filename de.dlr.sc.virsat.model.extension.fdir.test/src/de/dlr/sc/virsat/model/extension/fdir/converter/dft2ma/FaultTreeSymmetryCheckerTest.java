/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class tests the FaultTreeSymmetriyReduction
 * @author muel_s8
 *
 */

public class FaultTreeSymmetryCheckerTest extends ATestCase {

	private FaultTreeSymmetryChecker ftSymmetryChecker = new FaultTreeSymmetryChecker();
	
	@Test
	public void testAnd2Symmetric() throws IOException {
		Fault fault = createDFT("/resources/galileo/and2Symmetric.dft");
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		
		BasicEvent a = ftHolder.getMapBasicEventToFault().keySet().stream().filter(be -> be.getName().equals("a")).findFirst().get();
		BasicEvent b = ftHolder.getMapBasicEventToFault().keySet().stream().filter(be -> be.getName().equals("b")).findFirst().get();
		
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = ftSymmetryChecker.computeSymmetryRelation(ftHolder);
		
	}

}
