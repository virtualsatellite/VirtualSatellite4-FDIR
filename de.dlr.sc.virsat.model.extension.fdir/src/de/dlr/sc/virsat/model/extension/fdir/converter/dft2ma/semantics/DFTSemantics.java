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
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.DelayEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
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
	 * Configures whether the semantics should create repair events
	 * @param allowsRepairEvents whether the semantics considers repair events
	 */
	public void setAllowsRepairEvents(boolean allowsRepairEvents) {
		this.allowsRepairEvents = allowsRepairEvents;
	}
	
	/**
	 * Creates the list of occurable events
	 * @param ftHolder the fault tree holder
	 * @param staticAnalysis the static analysis of the fault tree
	 * @return the list of all occurable events
	 */
	public List<IDFTEvent> createEvents(FaultTreeHolder ftHolder, DFTStaticAnalysis staticAnalysis) {
		List<IDFTEvent> faultEvents = new ArrayList<>();
		
		for (BasicEvent be : ftHolder.getBasicEvents()) {
			if (allowsRepairEvents && be.isSetRepairRate() && be.getRepairRate() != 0) {
				faultEvents.add(new FaultEvent(be, true, ftHolder, staticAnalysis));					
			}
			
			if (be.isSetHotFailureRate() && be.getHotFailureRate() != 0) {
				faultEvents.add(new FaultEvent(be, false, ftHolder, staticAnalysis));
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
	 * Executes a state update
	 * @param stateUpdate the state update to execute
	 * @return the result of the state update
	 */
	public StateUpdateResult performUpdate(StateUpdate stateUpdate) {
		StateUpdateResult stateUpdateResult = stateUpdate.createResultContainer();
		stateUpdate.getEvent().execute(stateUpdateResult.getBaseSucc());
		propgateStateUpdate(stateUpdate, stateUpdateResult);
		
		return stateUpdateResult;
	}
	
	/**
	 * Propagates the changes from the state update
	 * @param stateUpdate the state update
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @return the list of updated nodes
	 */
	public void propgateStateUpdate(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult) {
		if (stateUpdate.getEvent().getNode() != null) {
			Queue<FaultTreeNode> worklist = createWorklist(stateUpdate.getEvent(), stateUpdateResult.getBaseSucc());
			propagateStateUpdate(stateUpdate, stateUpdateResult, worklist);
		}
	}
	
	/**
	 * Gets the minimum nodes that need to be checked for an event in a given state
	 * @param event the last event to occur
	 * @param baseSucc the base state
	 * @return an initial worklist of nodes that need to be checked
	 */
	private Queue<FaultTreeNode> createWorklist(IDFTEvent event, DFTState baseSucc) {
		FaultTreeHolder ftHolder = baseSucc.getFTHolder();
		Queue<FaultTreeNode> worklist = new LinkedList<FaultTreeNode>();
		if (event.getNode() instanceof BasicEvent) {
			worklist.add(ftHolder.getFault((BasicEvent) event.getNode()));
		} else {
			worklist.addAll(ftHolder.getNodes(event.getNode(), EdgeType.PARENT));
		}
		
		if (baseSucc.getRecoveryStrategy() != null) {
			RecoveryStrategy strategy = baseSucc.getRecoveryStrategy();
			for (RecoveryAction recoveryAction : strategy.getRecoveryActions()) {
				for (FaultTreeNode affectedNode : recoveryAction.getAffectedNodes(baseSucc)) {
					worklist.addAll(ftHolder.getNodes(affectedNode, EdgeType.PARENT));
				}
			}
		}
		
		return worklist;
	}
	
	/**
	 * Propagates the changes from the state update
	 * @param stateUpdate the state update
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @return the list of updated nodes
	 */
	protected void propagateStateUpdate(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult, Queue<FaultTreeNode> worklist) {
		List<FaultTreeNode> changedNodes = new ArrayList<>();
		FaultTreeHolder ftHolder = stateUpdate.getState().getFTHolder();
		
		while (!worklist.isEmpty()) {
			FaultTreeNode ftn = worklist.poll();
			boolean hasChanged = propagateStateUpdateToNode(stateUpdate, stateUpdateResult, ftn);
			
			if (hasChanged) {
				changedNodes.add(ftn);
				List<FaultTreeNode> parents = ftHolder.getNodes(ftn, EdgeType.PARENT);
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
	 * Propagates the changes from the state update to a single node
	 * @param stateUpdate the state update
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @param node the node we want to check
	 * @return true iff a change occurred in the update
	 */
	private boolean propagateStateUpdateToNode(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult, FaultTreeNode node) {
		if (stateUpdate.getState().isFaultTreeNodePermanent(node)) {
			return false;
		}
		
		GenerationResult generationResult = new GenerationResult(stateUpdateResult.getBaseSucc(), stateUpdateResult.getMapStateToRecoveryActions());
		boolean hasChanged = false;
		FaultTreeHolder ftHolder = stateUpdate.getState().getFTHolder();
		
		if (node instanceof BasicEvent) {
			List<FaultTreeNode> depTriggers = ftHolder.getNodes(node, EdgeType.DEP);
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
		
		List<FaultTreeNode> depTriggers = ftHolder.getNodes(node, EdgeType.DEP);
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
	 * @param stateUpdate the state update
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
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
}
