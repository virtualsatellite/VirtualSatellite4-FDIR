/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DelayEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class bundles a set of DFT node semantics and handles updating a set of states
 * for a given node.
 * @author muel_s8
 *
 */

public class DFTSemantics {
	
	protected Map<FaultTreeNodeType, INodeSemantics> mapTypeToSemantics = new EnumMap<>(FaultTreeNodeType.class);
	protected boolean allowsRepairEvents = true;
	
	/**
	 * Configures whether the semantics should create repair events
	 * @param allowsRepairEvents whether the semantics considers repair events
	 */
	public void setAllowsRepairEvents(boolean allowsRepairEvents) {
		this.allowsRepairEvents = allowsRepairEvents;
	}
	
	/**
	 * Creates the list of occurable events
	 * @param ftHolder the fault tree holder
	 * @return the list of all occurable events
	 */
	public List<IDFTEvent> createEvents(FaultTreeHolder ftHolder) {
		List<IDFTEvent> faultEvents = new ArrayList<>();
		
		for (BasicEvent be : ftHolder.getMapBasicEventToFault().keySet()) {
			if (allowsRepairEvents && be.isSetRepairRate() && be.getRepairRate() != 0) {
				faultEvents.add(new FaultEvent(be, true, ftHolder));					
			}
			
			if (be.isSetHotFailureRate() && be.getHotFailureRate() != 0) {
				faultEvents.add(new FaultEvent(be, false, ftHolder));
			}
		}
		
		for (FaultTreeNode node : ftHolder.getNodes()) {
			if (node instanceof DELAY) {
				DELAY delayNode = (DELAY) node;
				faultEvents.add(new DelayEvent(delayNode));
			}
		}
		
		return faultEvents;
	}
	
	/**
	 * Updates the changes in the fault tree due to the change of a basic event state
	 * @param pred the predecessor state
	 * @param succs a set of successor states
	 * @param recoveryActions list of recovery actions
	 * @param event the occured event
	 * @return the list of updated nodes
	 */
	public void updateFaultTreeNodeToFailedMap(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult) {
		if (stateUpdate.getEvent().getNode() != null) {
			Queue<FaultTreeNode> worklist = createWorklist(stateUpdate.getEvent(), stateUpdateResult.getBaseSucc());
			updateFaultTreeNodeToFailedMap(stateUpdate, stateUpdateResult, worklist);
		}
	}
	
	/**
	 * Gets the minimum nodes that need to be checked for an event in a given state
	 * @param event the last event to occur
	 * @param baseSucc the base state
	 * @return an initial worklist of nodes that need to be checked
	 */
	public Queue<FaultTreeNode> createWorklist(IDFTEvent event, DFTState baseSucc) {
		FaultTreeHolder ftHolder = baseSucc.getFTHolder();
		Queue<FaultTreeNode> worklist = new LinkedList<FaultTreeNode>();
		if (event.getNode() instanceof BasicEvent) {
			worklist.add(ftHolder.getMapBasicEventToFault().get(event.getNode()));
		} else {
			worklist.addAll(ftHolder.getMapNodeToParents().get(event.getNode()));
		}
		
		if (baseSucc.getRecoveryStrategy() != null) {
			RecoveryStrategy strategy = baseSucc.getRecoveryStrategy();
			for (RecoveryAction recoveryAction : strategy.getRecoveryActions()) {
				for (FaultTreeNode affectedNode : recoveryAction.getAffectedNodes(baseSucc)) {
					worklist.addAll(ftHolder.getMapNodeToParents().get(affectedNode));
				}
			}
		}
		
		return worklist;
	}
	
	/**
	 * Updates the changes in the fault tree due to the change of a basic event state
	 * @param pred the predecessor state
	 * @param succs a set of successor states
	 * @param recoveryActions list of recovery actions
	 * @param worklist the initial worklist
	 * @return the list of updated nodes
	 */
	public void updateFaultTreeNodeToFailedMap(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult, Queue<FaultTreeNode> worklist) {
		List<FaultTreeNode> changedNodes = new ArrayList<>();
		FaultTreeHolder ftHolder = stateUpdate.getState().getFTHolder();
		
		while (!worklist.isEmpty()) {
			FaultTreeNode ftn = worklist.poll();
			boolean hasChanged = updateFaultTreeNodeToFailedMap(stateUpdate, stateUpdateResult, ftn);
			
			if (hasChanged) {
				changedNodes.add(ftn);
				List<FaultTreeNode> parents = ftHolder.getMapNodeToParents().get(ftn);
				for (FaultTreeNode parent : parents) {
					if (!worklist.contains(parent)) {
						worklist.add(parent);
					}
				}
			}
		}
		
		stateUpdateResult.getChangedNodes().addAll(changedNodes);
	}
	
	/**
	 * Evaluates the fault tree node for every state in a set of given states
	 * @param pred the predecessor state
	 * @param states a set of states, may increase due to nondeterminism
	 * @param node the node we want to check
	 * @param mapStateToRecoveryActions map from state to recovery actions needed to go to the state from the predecessor
	 * @return true iff a change occurred in the update
	 */
	public boolean updateFaultTreeNodeToFailedMap(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult, FaultTreeNode node) {
		if (stateUpdate.getState().isFaultTreeNodePermanent(node)) {
			return false;
		}
		
		GenerationResult generationResult = new GenerationResult(stateUpdateResult.getBaseSucc(), stateUpdateResult.getMapStateToRecoveryActions());
		boolean hasChanged = false;
		FaultTreeHolder ftHolder = stateUpdate.getState().getFTHolder();
		
		if (node instanceof BasicEvent) {
			List<FaultTreeNode> depTriggers = ftHolder.getMapNodeToDEPTriggers().get(node);
			for (DFTState state : stateUpdateResult.getSuccs()) {
				if (state.handleUpdateTriggers(node, depTriggers)) {
					hasChanged = true;
				}
			}
			
			return hasChanged;
		}
		
		INodeSemantics nodeSemantics = mapTypeToSemantics.get(node.getFaultTreeNodeType());
		if (nodeSemantics == null) {
			throw new RuntimeException("The current semantics configuration doesnt support " +  node.getFaultTreeNodeType() + " as basic node type!");
		}
		
		List<FaultTreeNode> depTriggers = ftHolder.getMapNodeToDEPTriggers().getOrDefault(node, Collections.emptyList());
		for (DFTState succ : stateUpdateResult.getSuccs()) {
			if (succ.handleUpdateTriggers(node, depTriggers)) {
				hasChanged = true;
			}
			
			hasChanged  |= nodeSemantics.handleUpdate(node, succ, stateUpdate.getState(), generationResult);
		}
		
		if (!generationResult.getGeneratedStates().isEmpty()) {
			hasChanged = true;
			stateUpdateResult.getSuccs().addAll(generationResult.getGeneratedStates());
		}
		
		return hasChanged;
	}
	
	/**
	 * Extracts the occured event set for recovery strategies
	 * @param pred the predecessor state
	 * @param event the event that actually occured
	 * @param changedNodes the nodes that were affected due to the event
	 * @return the set of nodes that affect the recovery actions
	 */
	public Set<FaultTreeNode> extractRecoveryActionInput(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult) {
		Set<FaultTreeNode> occuredBasicEvents = new HashSet<>();
		if (stateUpdate.getEvent().getNode() instanceof BasicEvent) {
			occuredBasicEvents.add(stateUpdate.getEvent().getNode());
		}
		for (FaultTreeNode node : stateUpdateResult.getChangedNodes()) {
			if (node instanceof BasicEvent) {
				occuredBasicEvents.add(node);
			}
		}
		return occuredBasicEvents;
	}
	
	/**
	 * Generate a new dft state
	 * @param ftHolder the fault tree holder
	 * @return the generated state
	 */
	public DFTState generateState(FaultTreeHolder ftHolder) {
		return new DFTState(ftHolder);
	}
	
	/**
	 * Gets the registered gate semantics
	 * @return the mapping from node type to semantics
	 */
	public Map<FaultTreeNodeType, INodeSemantics> getMapTypeToSemantics() {
		return mapTypeToSemantics;
	}
	
	/**
	 * Creates a standard DFT semantics
	 * @return standard DFT semantics
	 */
	public static DFTSemantics createStandardDFTSemantics() {
		DFTSemantics semantics = new DFTSemantics();
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FAULT, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.FDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.RDEP, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.VOTE, new VOTESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.POR, new PORSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SPARE, new StandardSPARESemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.MONITOR, new FaultSemantics());
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.DELAY, new DelaySemantics());
		return semantics;
	}
	
	/**
	 * Creates a nondeterministic DFT semantics
	 * @return creates a nondeterministic DFT semantics
	 */
	public static DFTSemantics createNDDFTSemantics() {
		DFTSemantics semantics = createStandardDFTSemantics();
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SPARE, new NDSPARESemantics());
		return semantics;
	}

	/**
	 * Creates the list of initial events that should be applied to the initial state
	 * @param ftHolder the fault tree holder
	 * @return the intitial events for the initial state
	 */
	public List<IDFTEvent> getInitialEvents(FaultTreeHolder ftHolder) {
		return Collections.emptyList();
	}
}
