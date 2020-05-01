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

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DelaySemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.FaultSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.PORSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.VOTESemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
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
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FAULT, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.RDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.VOTE, new VOTESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.POR, new PORSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.MONITOR, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SPARE, new PONDSPARESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.DELAY, new DelaySemantics());
		return semantics;
	}
	
	@Override
	public List<IDFTEvent> createEvents(FaultTreeHolder ftHolder) {
		List<IDFTEvent> events = super.createEvents(ftHolder);
		
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
	public void updateFaultTreeNodeToFailedMap(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult) {
		IDFTEvent event = stateUpdate.getEvent();
		
		if (event.getNode() == null) {
			// Only update if the event actually affected a node in the tree
			// e.g. dont update when processing TimeEvents
			return;
		}
		
		DFTState pred = stateUpdate.getState();
		
		FaultTreeHolder ftHolder = pred.getFTHolder();
		boolean hasRecoveryStrategy = hasRecoveryStrategy(pred);
		boolean anyObservation = checkObservationEvent(event, stateUpdateResult.getSuccs());

		if (!anyObservation || hasRecoveryStrategy) {
			((NDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutActions(true);
			super.updateFaultTreeNodeToFailedMap(stateUpdate, stateUpdateResult);
			((NDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutActions(false);
		}
		
		anyObservation |= propagateObservations(stateUpdateResult);
		
		if (anyObservation && !hasRecoveryStrategy) {
			Set<FaultTreeNode> nondetGates = getNondeterministicGates(ftHolder);
			
			Queue<FaultTreeNode> worklist = new LinkedList<>(nondetGates);
			stateUpdateResult.getChangedNodes().clear();
			super.updateFaultTreeNodeToFailedMap(stateUpdate, stateUpdateResult, worklist);
			propagateObservations(stateUpdateResult);
		}
	}
	
	/**
	 * Gets the gates capable of nondeterminism from the fault tree
	 * @param ftHolder the fault tree holder
	 * @return the set of nondeterministic nodes
	 */
	private Set<FaultTreeNode> getNondeterministicGates(FaultTreeHolder ftHolder) {
		Set<FaultTreeNode> spareGatesToCheck = new HashSet<>();
		
		for (FaultTreeNode child : ftHolder.getNodes()) {
			if (child instanceof SPARE) {
				spareGatesToCheck.add(child);
			}
		}
		
		return spareGatesToCheck;
	}
	
	/**
	 * Handles an observation event
	 * @param event the event to handle
	 * @param succs the current successor list
	 * @return true iff the event was an observation event
	 */
	private boolean checkObservationEvent(IDFTEvent event, List<DFTState> succs) {
		if (event instanceof ObservationEvent) {
			FaultTreeNode observedNode = event.getNode();
			
			for (DFTState state : succs) {
				PODFTState poState = (PODFTState) state;
				boolean observedNodeFail = state.hasFaultTreeNodeFailed(observedNode);
				boolean failNodeObserved = poState.isNodeFailObserved(observedNode);
				if (failNodeObserved == observedNodeFail) {
					Set<FaultTreeNode> allParents = state.getFTHolder().getMapNodeToAllParents().get(observedNode);
					for (FaultTreeNode parent : allParents) {
						poState.setNodeFailObserved(parent, state.hasFaultTreeNodeFailed(parent));
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Handles a propagation step for the observability information
	 * @param succs the successor states
	 * @param changedNodes the set of nodes from which we want to propagate
	 * @return true if an observation was made during the propagation
	 */
	private boolean propagateObservations(StateUpdateResult stateUpdateResult) {
		boolean anyObservation = false;
		for (DFTState state : stateUpdateResult.getSuccs()) {
			for (FaultTreeNode node : stateUpdateResult.getChangedNodes()) {
				PODFTState poState = (PODFTState) state;
				boolean isObserved = poState.existsNonFailedObserver(node, false);
				if (isObserved) {
					anyObservation = true;
					poState.setNodeFailObserved(node, state.hasFaultTreeNodeFailed(node));
					
					Set<FaultTreeNode> allParents = state.getFTHolder().getMapNodeToAllParents().get(node);
					for (FaultTreeNode parent : allParents) {
						poState.setNodeFailObserved(parent, state.hasFaultTreeNodeFailed(parent));
					}
				}
			}
		}
		
		return anyObservation;
	}
	
	@Override
	public Set<FaultTreeNode> extractRecoveryActionInput(DFTState pred, IDFTEvent event, List<FaultTreeNode> changedNodes) {
		if (!(pred instanceof PODFTState)) {
			throw new IllegalArgumentException("Expected state of type PODFTState but got state " + pred);
		}
		
		Set<FaultTreeNode> observedNodes = new HashSet<>();
		
		if (event instanceof ObservationEvent) {
			observedNodes.add(event.getNode());
		} else {
			PODFTState poPred = (PODFTState) pred;
			
			if (poPred.existsNonFailedObserver(event.getNode(), false)) {
				observedNodes.add(event.getNode());
			}
			
			for (FaultTreeNode node : changedNodes) {
				if (poPred.existsNonFailedObserver(node, false)) {
					observedNodes.add(node);
				}
			}
		}
		
		return observedNodes;
	}
	
	/**
	 * Checks if the state is governed by a recovery strategy
	 * @param pred the current dft state
	 * @return true iff the dft state has a recovery strategy
	 */
	private boolean hasRecoveryStrategy(DFTState pred) {
		return pred.getRecoveryStrategy() != null;
	}
	
	@Override
	public DFTState generateState(FaultTreeHolder ftHolder) {
		return new PODFTState(ftHolder);
	}
	
	@Override
	public List<IDFTEvent> getInitialEvents(FaultTreeHolder ftHolder) {
		//return Collections.singletonList(new ObservationEvent(ftHolder.getRoot(), true));
		return super.getInitialEvents(ftHolder);
	}
}
