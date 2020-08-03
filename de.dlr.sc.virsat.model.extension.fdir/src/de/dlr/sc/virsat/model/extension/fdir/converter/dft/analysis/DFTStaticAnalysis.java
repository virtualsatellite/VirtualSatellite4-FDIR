/*******************************************************************************
 * Copyright (c) 2008-2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis;

import java.util.HashSet;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.BasicEventHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFTStaticAnalysis {
	private DFTSymmetryChecker symmetryChecker = new DFTSymmetryChecker();
	private SymmetryReduction symmetryReduction;
	private Set<FaultTreeNode> transientNodes;
	
	/**
	 * Perform a static analysis to obtain useful information for improving runtime performance
	 * @param ftHolder the fault tree
	 */
	public void perform(FaultTreeHolder ftHolder) {
		transientNodes = new HashSet<>();
		
		for (BasicEvent be : ftHolder.getBasicEvents()) {			
			BasicEventHolder beHolder = ftHolder.getBasicEventHolder(be);
			if (beHolder.isRepairDefined()) {
				transientNodes.add(be);			
			}
		}
		
		if (symmetryChecker != null) {
			symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		}
	}
	
	public DFTSymmetryChecker getSymmetryChecker() {
		return symmetryChecker;
	}
	
	public void setSymmetryChecker(DFTSymmetryChecker symmetryChecker) {
		this.symmetryChecker = symmetryChecker;
	}
	
	public Set<FaultTreeNode> getTransientNodes() {
		return transientNodes;
	}

	public SymmetryReduction getSymmetryReduction() {
		return symmetryReduction;
	}
}
