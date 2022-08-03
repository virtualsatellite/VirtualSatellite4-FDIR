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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class extends the ExplicitDFT state by belief information to support
 * partial observability
 * @author muel_s8
 *
 */

public class PODFTState extends DFTState {
	private BitSet observedFailed;
	
	/**
	 * Default constructor
	 * @param ftHolder the fault tree
	 */
	public PODFTState(FaultTreeHolder ftHolder) {
		super(ftHolder);
		observedFailed = new BitSet(ftHolder.getNodes().size());
	}
	
	/**
	 * Marks a given node failure as observed / unobserved
	 * @param node the node
	 * @param state the observation state
	 */
	public void setNodeFailObserved(FaultTreeNode node, boolean state) {
		int nodeID = ftHolder.getNodeIndex(node);
		observedFailed.set(nodeID, state);
	}
	
	/**
	 * Checks if a given node failure is observed
	 * @param node the node
	 * @return true iff a node is observed as failed
	 */
	public boolean isNodeFailObserved(FaultTreeNode node) {
		int nodeID = ftHolder.getNodeIndex(node);
		return observedFailed.get(nodeID);
	}
	
	/**
	 * Gets the set of all nodes that have been observed as failed
	 * @return the set of all nodes observed as failed
	 */
	public Set<FaultTreeNode> getObservedFailedNodes() {
		Set<FaultTreeNode> observedFailedNodes = new HashSet<>();
		for (FaultTreeNode node : ftHolder.getNodes()) {
			if (isNodeFailObserved(node) && existsObserver(node, true, true)) {
				observedFailedNodes.add(node);
			}
		}
		return observedFailedNodes;
	}
	
	/**
	 * Checks if a node is being observed
	 * @param node the node to check for observation
	 * @param allowDelay whether the observer is allowed to have a time delay
	 * @param allowFailed if a failed observer is allowed for consideration
	 * @return true iff the node is being observed
	 */
	public boolean existsObserver(FaultTreeNode node, boolean allowDelay, boolean allowFailed) {
		if (node instanceof MONITOR) {
			return true;
		}
		
		List<FaultTreeNode> observers = ftHolder.getNodes(node, EdgeType.MONITOR);
		for (FaultTreeNode observer : observers) {
			if ((allowFailed || !hasFaultTreeNodeFailed(observer)) 
					&& (allowDelay)) {
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 * Standard constructor
	 * @param other the other partial observable explicit dft state
	 */
	public PODFTState(PODFTState other) {
		super(other);
		observedFailed = (BitSet) other.observedFailed.clone();
	}
	
	@Override
	public String getLabel() {
		List<FaultTreeNode> observedFailedNodes = new ArrayList<>(getObservedFailedNodes());
		Collections.sort(observedFailedNodes, Comparator.comparing(Object::toString));
		return super.getLabel() + " | O" + observedFailedNodes.toString();
	}
	
	@Override
	public boolean isEquivalent(DFTState other) {
		PODFTState poState = (PODFTState) other;
		if (!getObservedFailedNodes().equals(poState.getObservedFailedNodes())) {
			return false;
		}
		
		return super.isEquivalent(other);
	}
	
	@Override
	protected boolean removeClaimedSparesOnPermanentFailureIfPossible(FaultTreeNode node) {
		return false;
	}
	
	@Override
	public DFTState copy() {
		return new PODFTState(this);
	}
	
	/**
	 * Extracts the set observation event containing the observed events and the information if this is a repair
	 * event or not
	 * @param succ 
	 * @param succTransitions the successor transitions from a belief state to the successor belief state
	 * @return the set of observation event
	 */
	public Entry<Set<Object>, Boolean> extractObservationEvent(PODFTState succ, List<MarkovTransition<DFTState>> succTransitions) {
		Set<Object> observationSet = new HashSet<>();
		
		MarkovTransition<DFTState> representantTransition = succTransitions.iterator().next();
		Object event = representantTransition.getEvent();
		if (event instanceof ObservationEvent) {
			ObservationEvent obsEvent = (ObservationEvent) event;
			observationSet.addAll(obsEvent.getNodes());
			Entry<Set<Object>, Boolean> observationEvent = new SimpleEntry<>(observationSet, obsEvent.isRepair());
			return observationEvent;
		}
		
		// obtain all newly observed failed nodes
		Set<FaultTreeNode> succObservedFailedNodes = succ.getObservedFailedNodes();
		Set<FaultTreeNode> currentObservedFailedNodes = getObservedFailedNodes();
		observationSet.addAll(succObservedFailedNodes);
		observationSet.removeAll(currentObservedFailedNodes);
		
		// obtain all newly observed repaired nodes in the event that no failures were observed
		boolean isRepair = false;
		if (observationSet.isEmpty()) {
			observationSet.addAll(currentObservedFailedNodes);
			observationSet.removeAll(succObservedFailedNodes);
			isRepair = !observationSet.isEmpty();
		}
		
		Entry<Set<Object>, Boolean> observationEvent = new SimpleEntry<>(observationSet, isRepair);
		return observationEvent;
	}
}
