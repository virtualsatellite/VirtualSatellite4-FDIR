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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFTStaticAnalysis {
	private FaultTreeHolder ftHolder;
	
	private DFTSymmetryChecker symmetryChecker = new DFTSymmetryChecker();
	private SymmetryReduction symmetryReduction;
	
	private Set<BasicEvent> orderDependentBasicEvents;
	private Set<FaultTreeNode> transientNodes;
	
	/**
	 * Perform a static analysis to obtain useful information for improving runtime performance
	 * @param ftHolder the fault tree
	 */
	public void perform(FaultTreeHolder ftHolder) {
		this.ftHolder = ftHolder;
		
		orderDependentBasicEvents = new HashSet<>();
		transientNodes = new HashSet<>();
		
		for (BasicEvent be : ftHolder.getBasicEvents()) {
			if (isOrderDependent(be)) {
				orderDependentBasicEvents.add(be);
			}
			
			if (be.isSetRepairRate() && be.getRepairRate() > 0) {
				transientNodes.add(be);			
			}
		}
		
		if (symmetryChecker != null) {
			symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
		}
	}
	
	/**
	 * Checks if a basic event does not have a semantically different effect
	 * if it appears in a different order. E.g. it may not be used in a PAND gate.
	 * @param event the fault event to check
	 * @return true iff the failure mode is order dependent
	 */
	private boolean isOrderDependent(BasicEvent be) {
		Fault fault = be.getFault();
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.add(fault);
		Set<FaultTreeNode> processed = new HashSet<>();
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			
			if (!processed.add(node)) {
				continue;
			}
			
			FaultTreeNodeType type = node.getFaultTreeNodeType();
			if (type == FaultTreeNodeType.POR) {
				return true;
			}
			
			List<FaultTreeNode> parents = ftHolder.getNodes(node, EdgeType.PARENT);
			toProcess.addAll(parents);
		}
		
		return false;
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
	
	public Set<BasicEvent> getOrderDependentBasicEvents() {
		return orderDependentBasicEvents;
	}

	public SymmetryReduction getSymmetryReduction() {
		return symmetryReduction;
	}
}
