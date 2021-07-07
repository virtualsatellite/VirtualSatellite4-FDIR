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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DelaySemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.FaultSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.PORSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.SEQSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.VOTESemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
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
	public static PONDDFTSemantics createPONDDFTSemantics() {
		PONDDFTSemantics semantics = new PONDDFTSemantics();
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FAULT, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.RDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.VOTE, new VOTESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.POR, new PORSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.MONITOR, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SPARE, new PONDSPARESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.DELAY, new DelaySemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SEQ, new SEQSemantics());
		return semantics;
	}
	
	@Override
	public List<IDFTEvent> createEvents(FaultTreeHolder ftHolder) {
		List<IDFTEvent> events = super.createEvents(ftHolder);
		
		for (FaultTreeNode node : ftHolder.getNodes()) {
			List<FaultTreeNode> monitors = ftHolder.getNodes(node, EdgeType.MONITOR);
			double observationRate = 0;
			for (FaultTreeNode observer : monitors) {
				observationRate += ((MONITOR) observer).getObservationRateBean().getValueToBaseUnit();
				for (FaultTreeNode childNode : ftHolder.getNodes(observer, EdgeType.CHILD)) {
					events.add(new ImmediateObservationEvent(childNode, true));	
					events.add(new ImmediateObservationEvent(childNode, false));
				}
			}
			
			if (observationRate > 0) {
				events.add(new ObservationEvent(node, true));	
				events.add(new ObservationEvent(node, false));
			} else if (!monitors.isEmpty()) {
				events.add(new ImmediateObservationEvent(node, true));	
				events.add(new ImmediateObservationEvent(node, false));	
			}
		}
		
		return events;
	}
	
	@Override
	public void propagateStateUpdate(StateUpdateResult stateUpdateResult, Queue<FaultTreeNode> worklist) {
		IDFTEvent event = stateUpdateResult.getStateUpdate().getEvent();
		DFTState pred = stateUpdateResult.getStateUpdate().getState();
		
		FaultTreeHolder ftHolder = pred.getFTHolder();
		boolean hasRecoveryStrategy = hasRecoveryStrategy(pred);
		boolean anyObservation = checkObservationEvent(event, stateUpdateResult.getSuccs());

		if (!anyObservation || hasRecoveryStrategy) {
			((NDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutActions(true);
			super.propagateStateUpdate(stateUpdateResult, worklist);
			((NDSPARESemantics) mapTypeToSemantics.get(FaultTreeNodeType.SPARE)).setPropagateWithoutActions(false);
		}
		
		anyObservation |= propagateObservations(stateUpdateResult);
		
		if (anyObservation && !hasRecoveryStrategy) {
			Set<FaultTreeNode> nondetGates = getNondeterministicGates(ftHolder);
			
			worklist = new LinkedList<>(nondetGates);
			stateUpdateResult.getChangedNodes().clear();
			super.propagateStateUpdate(stateUpdateResult, worklist);
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
		
		for (FaultTreeNode child : ftHolder.getNodes(FaultTreeNodeType.SPARE)) {
			spareGatesToCheck.add(child);
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
				Set<FaultTreeNode> allParents = state.getFTHolder().getMapNodeToAllParents().get(observedNode);
				for (FaultTreeNode parent : allParents) {
					poState.setNodeFailObserved(parent, state.hasFaultTreeNodeFailed(parent));
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Handles a propagation step for the observability information
	 * @param stateUpdateResult accumulator for saving results from the update
	 * @return true if an observation was made during the propagation
	 */
	private boolean propagateObservations(StateUpdateResult stateUpdateResult) {
		boolean anyObservation = false;
		for (DFTState state : stateUpdateResult.getSuccs()) {
			for (FaultTreeNode node : stateUpdateResult.getChangedNodes()) {
				PODFTState poState = (PODFTState) state;
				boolean isObserved = poState.existsObserver(node, false, false);
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
	public Set<FaultTreeNode> extractRecoveryActionInput(StateUpdateResult stateUpdateResult) {
		Set<FaultTreeNode> observedNodes = new HashSet<>();
		IDFTEvent event = stateUpdateResult.getStateUpdate().getEvent();
		DFTState state = stateUpdateResult.getStateUpdate().getState();
		
		if (event instanceof ObservationEvent) {
			observedNodes.add(event.getNode());
		} else {
			if (!(state instanceof PODFTState)) {
				throw new IllegalArgumentException("Expected state of type PODFTState but got state " + state);
			}
			
			PODFTState poPred = (PODFTState) state;
			for (FaultTreeNode node : stateUpdateResult.getChangedNodes()) {
				if (poPred.existsObserver(node, false, false)) {
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
}
