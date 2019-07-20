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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Checks a Fault Tree for symmetry
 * @author muel_s8
 *
 */

public class FaultTreeSymmetryChecker {
	
	public Map<FaultTreeNode, Set<FaultTreeNode>> computeSymmetryRelation(FaultTreeHolder ftHolder) {
		Map<FaultTreeNode, Set<FaultTreeNode>> symmetryRelation = new HashMap<>();
		
		for (BasicEvent be : ftHolder.getMapBasicEventToFault().keySet()) {
			
		}
		
		
		return symmetryRelation;
	}
}
