/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.po;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.FaultSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.PORSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.VOTESemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
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
		semantics.stateGenerator = new ExplicitPODFTStateGenerator();
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FAULT, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.RDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.VOTE, new VOTESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.POR, new PORSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.OBSERVER, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SPARE, new PONDSPARESemantics(semantics.stateGenerator));
		return semantics;
	}
	
	@Override
	public Set<IDFTEvent> createEventSet(FaultTreeHolder ftHolder) {
		Set<IDFTEvent> events = super.createEventSet(ftHolder);
		
		for (FaultTreeNode node : ftHolder.getNodes()) {
			List<OBSERVER> observers = ftHolder.getMapNodeToObservers().get(node);
			if (observers != null) {
				double observationRate = 0;
				for (OBSERVER observer : observers) {
					observationRate += observer.getObservationRate();
				}
				
				if (observationRate > 0) {
					events.add(new ObservationEvent(node, true));	
					events.add(new ObservationEvent(node, false));	
				}
			}
		}
		
		return events;
	}
	
	@Override
	public List<FaultTreeNode> updateFaultTreeNodeToFailedMap(FaultTreeHolder ftHolder, ExplicitDFTState pred,
			List<ExplicitDFTState> succs, Map<ExplicitDFTState, List<RecoveryAction>> recoveryActions, IDFTEvent event) {
		
		boolean anyObservation = false;
		List<FaultTreeNode> changedNodes = null;
		Set<FaultTreeNode> possiblyFailedSpareGates = new HashSet<>();
		FaultTreeHelper ftHelper = new FaultTreeHelper(event.getNode().getConcept());
		
		if (event instanceof ObservationEvent) {
			FaultTreeNode observedNode = event.getNode();
			anyObservation = true;
			changedNodes = new ArrayList<>();
			
			for (ExplicitDFTState state : succs) {
				ExplicitPODFTState poState = (ExplicitPODFTState) state;
				poState.setNodeFailObserved(observedNode, state.hasFaultTreeNodeFailed(observedNode));
				for (FaultTreeNode child : ftHelper.getAllNodes(observedNode.getFault())) {
					if (child instanceof SPARE) {
						possiblyFailedSpareGates.add(child);
					}
				}
			}
		} else {
			((PONDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutClaiming(true);
			changedNodes = super.updateFaultTreeNodeToFailedMap(ftHolder, pred, succs, recoveryActions, event);
			for (ExplicitDFTState state : succs) {
				for (FaultTreeNode node : changedNodes) {
					boolean isObserved = existsNonFailedImmediateObserver(state, ftHolder, node);
					if (isObserved) {
						anyObservation = true;
						ExplicitPODFTState poState = (ExplicitPODFTState) state;
						poState.setNodeFailObserved(node, state.hasFaultTreeNodeFailed(node));
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
			((PONDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutClaiming(false);
			Queue<FaultTreeNode> spareGates = new LinkedList<>(possiblyFailedSpareGates);
			List<FaultTreeNode> repairedNodes = super.updateFaultTreeNodeToFailedMap(ftHolder, pred, succs, recoveryActions, spareGates);
			
			for (ExplicitDFTState state : succs) {
				for (FaultTreeNode node : repairedNodes) {
					boolean isObserved = existsNonFailedImmediateObserver(state, ftHolder, node);
					if (isObserved) {
						ExplicitPODFTState poState = (ExplicitPODFTState) state;
						poState.setNodeFailObserved(node, state.hasFaultTreeNodeFailed(node));
					}
				}
			}
		}
		
		return changedNodes;
	}
	
	/**
	 * Checks if a node is being observed
	 * @param state the current state
	 * @param ftHolder the fault tree data holder
	 * @param node the node to check for observation
	 * @return true iff the node is being observed
	 */
	private boolean existsNonFailedImmediateObserver(ExplicitDFTState state, FaultTreeHolder ftHolder, FaultTreeNode node) {
		if (node instanceof OBSERVER) {
			return true;
		}
		
		List<OBSERVER> observers = ftHolder.getMapNodeToObservers().get(node);
		if (observers != null) {
			for (OBSERVER observer : observers) {
				if (!state.hasFaultTreeNodeFailed(observer) && observer.getObservationRate() == 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Set<FaultTreeNode> extractRecoveryActionInput(FaultTreeHolder ftHolder, ExplicitDFTState pred, IDFTEvent event, List<FaultTreeNode> changedNodes) {
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
