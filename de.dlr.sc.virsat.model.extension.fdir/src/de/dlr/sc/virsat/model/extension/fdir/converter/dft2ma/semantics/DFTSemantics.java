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
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.GenerationResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.DelayEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateFaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.BasicEventHolder;
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
	protected boolean permanence = true;
	
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
		semantics.mapTypeToSemantics.put(FaultTreeNodeType.SEQ, new SEQSemantics());
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
	 * Configures whether the semantics should check permanence
	 * @param permanence whether the semantics considers permanence
	 */
	public void setPermanence(boolean permanence) {
		this.permanence = permanence;
	}
	
	/**
	 * Creates the list of occurable events
	 * @param ftHolder the fault tree holder
	 * @return the list of all occurable events
	 */
	public List<IDFTEvent> createEvents(FaultTreeHolder ftHolder) {
		List<IDFTEvent> faultEvents = new ArrayList<>();
		
		for (BasicEvent be : ftHolder.getBasicEvents()) {
			BasicEventHolder beHolder = ftHolder.getBasicEventHolder(be);
			if (allowsRepairEvents && beHolder.isRepairDefined()) {
				faultEvents.add(new FaultEvent(be, true, ftHolder));
			}
			
			if (beHolder.isFailureDefined()) {
				if (beHolder.isImmediateDistribution()) {
					faultEvents.add(new ImmediateFaultEvent(be, false, ftHolder, true));
					faultEvents.add(new ImmediateFaultEvent(be, false, ftHolder, false));
				} else {
					faultEvents.add(new FaultEvent(be, false, ftHolder));	
				}
			}
		}
		
		for (FaultTreeNode node : ftHolder.getNodes(FaultTreeNodeType.DELAY)) {
			DELAY delayNode = (DELAY) node;
			faultEvents.add(new DelayEvent(delayNode));
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
		propagateStateUpdate(stateUpdateResult);
		
		return stateUpdateResult;
	}
	
	/**
	 * Propagates the changes from the state update
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @return the list of updated nodes
	 */
	public void propagateStateUpdate(StateUpdateResult stateUpdateResult) {
		StateUpdate stateUpdate = stateUpdateResult.getStateUpdate();
		if (stateUpdate.getEvent().getNodes() != null) {
			Queue<FaultTreeNode> worklist = createWorklist(stateUpdate.getEvent(), stateUpdateResult.getBaseSucc());
			propagateStateUpdate(stateUpdateResult, worklist);
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
		for (FaultTreeNode node : event.getNodes()) {
			if (node instanceof BasicEvent) {
				worklist.add(ftHolder.getFault((BasicEvent) node));
			} else {
				worklist.addAll(ftHolder.getNodes(node, EdgeType.PARENT));
			}
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
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @return the list of updated nodes
	 */
	public void propagateStateUpdate(StateUpdateResult stateUpdateResult, Queue<FaultTreeNode> worklist) {
		List<FaultTreeNode> changedNodes = new ArrayList<>();
		FaultTreeHolder ftHolder = stateUpdateResult.getStateUpdate().getState().getFTHolder();
		
		while (!worklist.isEmpty()) {
			FaultTreeNode ftn = worklist.poll();
			boolean hasChanged = propagateStateUpdateToNode(stateUpdateResult, ftn);
			
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
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @param node the node we want to check
	 * @return true iff a change occurred in the update
	 */
	private boolean propagateStateUpdateToNode(StateUpdateResult stateUpdateResult, FaultTreeNode node) {
		StateUpdate stateUpdate = stateUpdateResult.getStateUpdate();
		if (permanence && stateUpdate.getState().isFaultTreeNodePermanent(node) && !node.getFaultTreeNodeType().equals(FaultTreeNodeType.SEQ)) {
			return false;
		}
		
		GenerationResult generationResult = new GenerationResult(stateUpdateResult.getBaseSucc(), stateUpdateResult.getMapStateToRecoveryActions());
		FaultTreeHolder ftHolder = stateUpdate.getState().getFTHolder();
		
		boolean hasChanged = false;
		
		if (node instanceof BasicEvent) {
			List<FaultTreeNode> depTriggers = ftHolder.getNodes(node, EdgeType.DEP);
			for (DFTState state : stateUpdateResult.getSuccs()) {
				hasChanged |= state.handleUpdateTriggers(node, depTriggers);
			}
			
			return hasChanged;
		}
		
		INodeSemantics nodeSemantics = mapTypeToSemantics.get(node.getFaultTreeNodeType());
		if (nodeSemantics == null) {
			throw new RuntimeException("The current semantics configuration doesnt support " +  node.getFaultTreeNodeType() + " as basic node type!");
		}
		
		for (DFTState succ : stateUpdateResult.getSuccs()) {
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
	 * @param stateUpdateResult accumulator for saving results from the update, including the propagation
	 * @return the set of nodes that affect the recovery actions
	 */
	public Set<FaultTreeNode> extractRecoveryActionInput(StateUpdateResult stateUpdateResult) {
		Collection<FaultTreeNode> eventNodes = stateUpdateResult.getStateUpdate().getEvent().getNodes();
		
		Set<FaultTreeNode> occuredBasicEvents = new HashSet<>();
		for (FaultTreeNode eventNode : eventNodes) {
			if (eventNode instanceof BasicEvent) {
				occuredBasicEvents.add(eventNode);
			}
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
