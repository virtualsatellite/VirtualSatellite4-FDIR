/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DelaySemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.FaultSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.PORSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.VOTESemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class implements the semantics for a partial observable nondeterministic DFT
 * @author muel_s8
 *
 */

public class PONDDFTSemantics extends DFTSemantics {
	/**
	 * Creates a partial observable nondeterministic DFT semantics
	 * @return creates a partial observable nondeterministic DFT semantics
	 */
	public static DFTSemantics createPONDDFTSemantics() {
		PONDDFTSemantics semantics = new PONDDFTSemantics();
		semantics.stateGenerator = new PODFTStateGenerator();
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FAULT, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.RDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.VOTE, new VOTESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.POR, new PORSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.MONITOR, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SPARE, new PONDSPARESemantics(semantics.stateGenerator));
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.DELAY, new DelaySemantics());
		return semantics;
	}
	
	@Override
	public Set<IDFTEvent> createEventSet(FaultTreeHolder ftHolder) {
		Set<IDFTEvent> events = super.createEventSet(ftHolder);
		
		for (FaultTreeNode node : ftHolder.getNodes()) {
			List<MONITOR> monitors = ftHolder.getMapNodeToMonitors().getOrDefault(node, Collections.emptyList());
			double observationRate = 0;
			for (MONITOR observer : monitors) {
				observationRate += observer.getObservationRateBean().getValueToBaseUnit();
			}
			
			if (observationRate > 0) {
				events.add(new ObservationEvent(node, true));	
				events.add(new ObservationEvent(node, false));	
			}
		}
		
		return events;
	}
	
	@Override
	public List<FaultTreeNode> updateFaultTreeNodeToFailedMap(FaultTreeHolder ftHolder, DFTState pred,
			List<DFTState> succs, Map<DFTState, List<RecoveryAction>> recoveryActions, IDFTEvent event) {
		
		boolean anyObservation = false;
		List<FaultTreeNode> changedNodes = null;
		Set<FaultTreeNode> possiblyFailedSpareGates = new HashSet<>();
		FaultTreeHelper ftHelper = new FaultTreeHelper(event.getNode().getConcept());
		
		if (event instanceof ObservationEvent) {
			FaultTreeNode observedNode = event.getNode();
			anyObservation = true;
			changedNodes = new ArrayList<>();
			
			for (DFTState state : succs) {
				PODFTState poState = (PODFTState) state;
				poState.setNodeFailObserved(observedNode, state.hasFaultTreeNodeFailed(observedNode));
				for (FaultTreeNode parent : ftHolder.getMapNodeToAllParents().get(observedNode)) {
					poState.setNodeFailObserved(parent, state.hasFaultTreeNodeFailed(observedNode));
				}
				
				for (FaultTreeNode child : ftHelper.getAllNodes(observedNode.getFault())) {
					if (child instanceof SPARE) {
						possiblyFailedSpareGates.add(child);
					}
				}
			}
		} else {
			((NDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutActions(true);
			changedNodes = super.updateFaultTreeNodeToFailedMap(ftHolder, pred, succs, recoveryActions, event);
			for (DFTState state : succs) {
				for (FaultTreeNode node : changedNodes) {
					boolean isObserved = existsNonFailedImmediateObserver(state, ftHolder, node);
					if (isObserved) {
						anyObservation = true;
						PODFTState poState = (PODFTState) state;
						poState.setNodeFailObserved(node, state.hasFaultTreeNodeFailed(node));
						
						for (FaultTreeNode parent : ftHolder.getMapNodeToAllParents().get(node)) {
							poState.setNodeFailObserved(parent, state.hasFaultTreeNodeFailed(parent));
						}
						
						for (FaultTreeNode child : ftHelper.getAllNodes(node.getFault())) {
							if (child instanceof SPARE) {
								possiblyFailedSpareGates.add(child);
							}
						}
					}
				}
			}
		}
		
		if (anyObservation) {
			((NDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutActions(false);
			Queue<FaultTreeNode> spareGates = new LinkedList<>(possiblyFailedSpareGates);
			List<FaultTreeNode> repairedNodes = super.updateFaultTreeNodeToFailedMap(ftHolder, pred, succs, recoveryActions, spareGates);
			
			for (DFTState state : succs) {
				for (FaultTreeNode node : repairedNodes) {
					boolean isObserved = existsNonFailedImmediateObserver(state, ftHolder, node);
					if (isObserved) {
						PODFTState poState = (PODFTState) state;
						poState.setNodeFailObserved(node, state.hasFaultTreeNodeFailed(node));
					}
				}
			}
		}
		

		DFTState baseSucc = succs.remove(0);
		succs.add(baseSucc);
		
		return changedNodes;
	}
	
	/**
	 * Checks if a node is being observed
	 * @param state the current state
	 * @param ftHolder the fault tree data holder
	 * @param node the node to check for observation
	 * @return true iff the node is being observed
	 */
	private boolean existsNonFailedImmediateObserver(DFTState state, FaultTreeHolder ftHolder, FaultTreeNode node) {
		if (node instanceof MONITOR) {
			return true;
		}
		
		List<MONITOR> observers = ftHolder.getMapNodeToMonitors().get(node);
		if (observers != null) {
			for (MONITOR observer : observers) {
				if (!state.hasFaultTreeNodeFailed(observer) && observer.getObservationRate() == 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Set<FaultTreeNode> extractRecoveryActionInput(FaultTreeHolder ftHolder, DFTState pred, IDFTEvent event, List<FaultTreeNode> changedNodes) {
		Set<FaultTreeNode> observedNodes = new HashSet<>();
		if (existsNonFailedImmediateObserver(pred, ftHolder, event.getNode())) {
			observedNodes.add(event.getNode());
		}
		
		for (FaultTreeNode node : changedNodes) {
			if (existsNonFailedImmediateObserver(pred, ftHolder, node)) {
				observedNodes.add(node);
			}
		}
		return observedNodes;
	}
}
