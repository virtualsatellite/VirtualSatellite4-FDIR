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
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
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
	public List<FaultTreeNode> updateFaultTreeNodeToFailedMap(DFTState pred, List<DFTState> succs, Map<DFTState, List<RecoveryAction>> recoveryActions, IDFTEvent event) {
		if (event.getNode() == null) {
			return Collections.emptyList();
		}
		
		FaultTreeHolder ftHolder = pred.getFTHolder();
		Queue<FaultTreeNode> worklist = new LinkedList<FaultTreeNode>();
		if (event.getNode() instanceof BasicEvent) {
			worklist.add(ftHolder.getMapBasicEventToFault().get(event.getNode()));
		} else {
			worklist.addAll(ftHolder.getMapNodeToParents().get(event.getNode()));
		}

		List<FaultTreeNode> changedNodes = updateFaultTreeNodeToFailedMap(pred, succs, recoveryActions, worklist);
		boolean existsNonTLESucc = existsNonTLE(succs);
		
		if (succs.size() > 1) {
			if (!existsNonTLESucc) {
				DFTState baseSucc = succs.get(0);
				succs.clear();
				succs.add(baseSucc);
				recoveryActions.clear();
				recoveryActions.put(baseSucc, new ArrayList<RecoveryAction>());
			} 
		}
		
		return changedNodes;
	}
	
	/**
	 * Updates the changes in the fault tree due to the change of a basic event state
	 * @param pred the predecessor state
	 * @param succs a set of successor states
	 * @param recoveryActions list of recovery actions
	 * @param worklist the initial worklist
	 * @return the list of updated nodes
	 */
	public List<FaultTreeNode> updateFaultTreeNodeToFailedMap(DFTState pred, List<DFTState> succs, Map<DFTState, List<RecoveryAction>> recoveryActions, Queue<FaultTreeNode> worklist) {
		List<FaultTreeNode> changedNodes = new ArrayList<>();
		FaultTreeHolder ftHolder = pred.getFTHolder();
		
		while (!worklist.isEmpty()) {
			FaultTreeNode ftn = worklist.poll();
			boolean hasChanged = updateFaultTreeNodeToFailedMap(pred, succs, recoveryActions, ftn);
			
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
		
		return changedNodes;
	}
	
	/**
	 * Evaluates the fault tree node for every state in a set of given states
	 * @param pred the predecessor state
	 * @param states a set of states, may increase due to nondeterminism
	 * @param node the node we want to check
	 * @param mapStateToRecoveryActions map from state to recovery actions needed to go to the state from the predecessor
	 * @return true iff a change occurred in the update
	 */
	public boolean updateFaultTreeNodeToFailedMap(DFTState pred, List<DFTState> states, Map<DFTState, List<RecoveryAction>> mapStateToRecoveryActions, FaultTreeNode node) {
		if (pred.isFaultTreeNodePermanent(node)) {
			return false;
		}
		
		GenerationResult generationResult = new GenerationResult(mapStateToRecoveryActions);
		boolean hasChanged = false;
		FaultTreeHolder ftHolder = pred.getFTHolder();
		
		if (node instanceof BasicEvent) {
			List<FaultTreeNode> depTriggers = ftHolder.getMapNodeToDEPTriggers().get(node);
			for (DFTState state : states) {
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
		for (DFTState state : states) {
			if (state.handleUpdateTriggers(node, depTriggers)) {
				hasChanged = true;
			}
			
			hasChanged  |= nodeSemantics.handleUpdate(node, state, pred, generationResult);
		}
		
		if (!generationResult.getGeneratedStates().isEmpty()) {
			hasChanged = true;
			states.addAll(generationResult.getGeneratedStates());
		}
		
		return hasChanged;
	}
	
	/**
	 * Checks if in the set of states contains a state without the TLE triggered
	 * @param states set of states
	 * @return true iff there exists a node without a failed TLE
	 */
	private boolean existsNonTLE(List<DFTState> states) {
		for (DFTState state : states) {
			FaultTreeHolder ftHolder = state.getFTHolder();
			if (!state.hasFaultTreeNodeFailed(ftHolder.getRoot())) {
				return true;
			}
		}
		
		return false;
	}

	
	/**
	 * Extracts the occured event set for recovery strategies
	 * @param pred the predecessor state
	 * @param event the event that actually occured
	 * @param changedNodes the nodes that were affected due to the event
	 * @return the set of nodes that affect the recovery actions
	 */
	public Set<FaultTreeNode> extractRecoveryActionInput(DFTState pred, IDFTEvent event, List<FaultTreeNode> changedNodes) {
		Set<FaultTreeNode> occuredBasicEvents = new HashSet<>();
		if (event.getNode() instanceof BasicEvent) {
			occuredBasicEvents.add(event.getNode());
		}
		for (FaultTreeNode node : changedNodes) {
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
