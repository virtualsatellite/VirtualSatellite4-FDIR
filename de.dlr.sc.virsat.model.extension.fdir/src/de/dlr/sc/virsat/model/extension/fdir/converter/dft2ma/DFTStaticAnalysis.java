/*******************************************************************************
 * Copyright (c) 2008-2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFTStaticAnalysis {
	private FaultTreeHolder ftHolder;
	
	private DFTSymmetryChecker symmetryChecker = new DFTSymmetryChecker();
	private Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction;
	private Map<FaultTreeNode, Set<FaultTreeNode>> symmetryReductionInverted;
	
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
		
		for (BasicEvent be : ftHolder.getMapBasicEventToFault().keySet()) {
			if (isOrderDependent(be)) {
				orderDependentBasicEvents.add(be);
			}
			
			if (be.isSetRepairRate() && be.getRepairRate() > 0) {
				transientNodes.add(be);			
			}
		}
		
		if (symmetryChecker != null) {
			symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
			symmetryReductionInverted = symmetryChecker.invertSymmetryReduction(symmetryReduction);
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
			
			List<FaultTreeNode> parents = ftHolder.getMapNodeToParents().getOrDefault(node, Collections.emptyList());
			toProcess.addAll(parents);
		}
		
		return false;
	}
	
	public static final int SKIP_EVENT = -1;
	
	/**
	 * Gets the symmetry rate multiplier for a given event in a given state
	 * @param event the event
	 * @param state the state
	 * @return the symmetry rate multiplier or -1 if there is a symmetric event covering this one
	 */
	public int getSymmetryMultiplier(IDFTEvent event, DFTState state) {
		int multiplier = 1;
		if (symmetryChecker != null) {
			int countBiggerSymmetricEvents = countBiggerSymmetricEvents(event, state);
			if (countBiggerSymmetricEvents == -1) {
				return SKIP_EVENT;
			} else {
				multiplier += countBiggerSymmetricEvents;
			}
		}
		
		return multiplier;
	}
	
	/**
	 * Computes the number of events symmetric to the passed one
	 * @param event the event
	 * @param state the current state
	 * @return -1 if there exists a smaller symmetric event, otherwise the number of bigger symmetric events
	 */
	private int countBiggerSymmetricEvents(IDFTEvent event, DFTState state) {
		int symmetryMultiplier = 0;
		
		if (event instanceof FaultEvent) {
			Set<BasicEvent> failedBasicEvents = state.getFailedBasicEvents();
			
			boolean haveNecessaryEventsFailed = failedBasicEvents.containsAll(symmetryReductionInverted.getOrDefault(event.getNode(), Collections.emptySet()));
			if (!haveNecessaryEventsFailed && isSymmetryReductionApplicable(state, event.getNode())) {
				return -1;
			}
			
			List<FaultTreeNode> symmetricNodes = symmetryReduction.getOrDefault(event.getNode(), Collections.emptyList());
			for (FaultTreeNode node : symmetricNodes) {
				if (!failedBasicEvents.contains(node)) {
					if (isSymmetryReductionApplicable(state, node)) {
						symmetryMultiplier++;
					}
				}
			}
		}
		
		return symmetryMultiplier;
	}
	
	/**
	 * Checks if symmetry reduction is applicable for a given node
	 * @param state the current state
	 * @param node the node
	 * @return true iff symmetry reduction is applicable
	 */
	private boolean isSymmetryReductionApplicable(DFTState state, FaultTreeNode node) {
		Map<FaultTreeNode, Set<FaultTreeNode>> mapParentToSymmetryRequirements = state.getMapParentToSymmetryRequirements();
		
		Set<FaultTreeNode> allParents = ftHolder.getMapNodeToAllParents().get(node);
		for (FaultTreeNode parent : allParents) {
			Set<FaultTreeNode> symmetryRequirements = mapParentToSymmetryRequirements.getOrDefault(parent, Collections.emptySet());
			for (FaultTreeNode symmetryRequirement : symmetryRequirements) {
				if (!state.hasFaultTreeNodeFailed(symmetryRequirement)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public DFTSymmetryChecker getSymmetryChecker() {
		return symmetryChecker;
	}
	
	public void setSymmetryChecker(DFTSymmetryChecker symmetryChecker) {
		this.symmetryChecker = symmetryChecker;
	}
	
	public Map<FaultTreeNode, List<FaultTreeNode>> getSymmetryReduction() {
		return symmetryReduction;
	}
	
	public Set<FaultTreeNode> getTransientNodes() {
		return transientNodes;
	}
	
	public Set<BasicEvent> getOrderDependentBasicEvents() {
		return orderDependentBasicEvents;
	}
}
