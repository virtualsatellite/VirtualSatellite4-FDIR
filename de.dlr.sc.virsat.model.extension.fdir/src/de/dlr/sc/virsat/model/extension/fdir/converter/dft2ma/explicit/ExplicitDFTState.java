/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Internal class used to represent a markov state of a DFT
 * @author muel_s8
 *
 */
public class ExplicitDFTState extends DFTState {
	protected FaultTreeHolder ftHolder;
	
	private Map<FaultTreeNode, FaultTreeNode> mapSpareToClaimedSpares;
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapNodeToAffectors;
	
	private Set<Fault> activeFaults;
	
	private BitSet failedNodes;
	private BitSet permanentNodes;
	private BitSet failingNodes;
	
	List<BasicEvent> orderedBes;
	Set<BasicEvent> unorderedBes;
	
	/**
	 * Default Constructor
	 * @param ftHolder the fault tree
	 */
	public ExplicitDFTState(FaultTreeHolder ftHolder) {
		this.ftHolder = ftHolder;
		failedNodes = new BitSet(ftHolder.getNodes().size());
		mapSpareToClaimedSpares = new HashMap<>();
		mapNodeToAffectors = new HashMap<>();
		activeFaults = new HashSet<>();
		permanentNodes = new BitSet(ftHolder.getNodes().size());
		failingNodes = new BitSet(ftHolder.getNodes().size());
		orderedBes = new ArrayList<>();
		unorderedBes = new HashSet<>();
	}
	
	/**
	 * Copy constructor
	 * @param other the markov state that will be copied
	 */
	public ExplicitDFTState(ExplicitDFTState other) {
		orderedBes = new ArrayList<>(other.orderedBes);
		unorderedBes = new HashSet<>(other.unorderedBes);
		activeFaults = new HashSet<>(other.activeFaults);
		mapNodeToAffectors = new HashMap<>(other.mapNodeToAffectors);
		mapSpareToClaimedSpares = new HashMap<>(other.mapSpareToClaimedSpares);
		failedNodes = (BitSet) other.failedNodes.clone();
		permanentNodes = (BitSet) other.permanentNodes.clone();
		failingNodes = (BitSet) other.failingNodes.clone();
		ftHolder = other.ftHolder;
	}
	
	/**
	 * Gets the fault tree
	 * @return the fault tree
	 */
	public FaultTreeHolder getFTHolder() {
		return ftHolder;
	}
	
	/**
	 * Get the map from spare gate to claimed spare
	 * @return a mapping from spare gates to claimed spares
	 */
	public Map<FaultTreeNode, FaultTreeNode> getSpareClaims() {
		return mapSpareToClaimedSpares;
	}
	
	/**
	 * Get the set of nodes that have failed
	 * @return set of failed nodes
	 */
	public BitSet getFailedNodes() {
		return failedNodes;
	}
	
	/**
	 * Get the map from fault node tree to their claimed spares
	 * @return a mapping from nodes to their claimed spares
	 */
	public Map<FaultTreeNode, FaultTreeNode> getMapSpareToClaimedSpares() {
		return mapSpareToClaimedSpares;
	}
	
	/**
	 * Updates the failed state of the given fault tree node and
	 * returns if there was a state change
	 * @param node the node to modify
	 * @param failed the new node state
	 * @return true iff the state of the fault tree node changed
	 */
	public boolean setFaultTreeNodeFailed(FaultTreeNode node, boolean failed) {
		int nodeID = ftHolder.getNodeIndex(node);
		boolean hasChanged = failedNodes.get(nodeID) != failed;
		failedNodes.set(nodeID, failed);
		return hasChanged;
	}
	
	/**
	 * Checks if the given fault tree node is failed in this state
	 * @param node the fault tree node
	 * @return true iff the fault tree node is in a failed state
	 */
	public boolean hasFaultTreeNodeFailed(FaultTreeNode node) {
		int nodeID = ftHolder.getNodeIndex(node);
		return failedNodes.get(nodeID);
	}
	
	/**
	 * Sets whether a node is curently in the process of failing
	 * @param node the node to modify
	 * @param failing the new node state
	 * @return true iff the state of the fault tree node changed
	 */
	public boolean setFaultTreeNodeFailing(FaultTreeNode node, boolean failing) {
		int nodeID = ftHolder.getNodeIndex(node);
		boolean hasChanged = failingNodes.get(nodeID) != failing;
		failingNodes.set(nodeID, failing);
		return hasChanged;
	}
	
	/**
	 * Checks if the given fault tree node is failed in this state
	 * @param node the fault tree node
	 * @return true iff the fault tree node is in a failed state
	 */
	public boolean isFaultTreeNodeFailing(FaultTreeNode node) {
		int nodeID = ftHolder.getNodeIndex(node);
		return failingNodes.get(nodeID);
	}
	
	/**
	 * Checks if the given fault tree node is permanently stuck at its state
	 * @param node the fault tree node
	 * @return true iff the fault tree node is permanently stuck at its state
	 */
	public boolean isFaultTreeNodePermanent(FaultTreeNode node) {
		int nodeID = ftHolder.getNodeIndex(node);
		return permanentNodes.get(nodeID);
	}
	
	/**
	 * Checks if the given set of fault tree nodes is permanently stuck at its state
	 * @param nodes the set of fault tree nodes
	 * @return true iff the fault tree nodes are all permanently stuck at their state
	 */
	public boolean areFaultTreeNodesPermanent(Collection<FaultTreeNode> nodes) {
		for (FaultTreeNode node : nodes) {
			if (!isFaultTreeNodePermanent(node)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Updates the permanent state of the given fault tree
	 * @param node the node to modify
	 * @param permanent the permanence of the node state
	 */
	public void setFaultTreeNodePermanent(FaultTreeNode node, boolean permanent) {
		int nodeID = ftHolder.getNodeIndex(node);
		permanentNodes.set(nodeID, permanent);
	}
	
	@Override
	public String getLabel() {
		String raSuffix = "";
		if (recoveryStrategy != null) {
			raSuffix = ", " +  recoveryStrategy.getCurrentState();
		}
		
		String ftInfix = "";
		if (failedNodes.isEmpty() && mapSpareToClaimedSpares.isEmpty()) {
			ftInfix = " initial";
		} else {
			ftInfix = " " + unorderedBes.toString() + orderedBes.toString() + " | C" +  mapSpareToClaimedSpares.toString();
		}
		
		return index + ftInfix + raSuffix;
	}
	
	/**
	 * Checks if a given node is active
	 * @param node the node to check
	 * @return true iff the node is active
	 */
	public boolean isNodeActive(FaultTreeNode node) {
		return activeFaults.contains(node);
	}
	
	/**
	 * Activates the passed node in a given state
	 * @param node the node to activate
	 */
	public void activateNode(FaultTreeNode node) {
		if (node instanceof Fault) {
			activeFaults.add((Fault) node);
			
			// All depenency gates in a fault are considered activated as soon as the parent fault is activated 
			for (FaultTreeNode gate : node.getFault().getFaultTree().getGates()) {
				if (gate instanceof ADEP) {
					if (!activeFaults.contains(gate)) {
						activateNode(gate);
					}
				}
			}
		}
		
		List<FaultTreeNode> children = ftHolder.getMapNodeToChildren().get(node);
		for (FaultTreeNode child : children) {
			if (node instanceof ADEP && ftHolder.getMapNodeToParents().get(child).size() > 1) {
				continue;
			}
			activateNode(child);
		}
		
		List<OBSERVER> observers = ftHolder.getMapNodeToObservers().get(node);
		for (OBSERVER observer : observers) {
			activateNode(observer);
		}
	}
	
	/**
	 * Makes a DFT state uniform by failing all basic events and nodes about we
	 * dont care what exact state we have
	 * @param changedNodes the nodes that have changed in this dft state
	 * @param orderDependentBasicEvents set of basic events that are dependent
	 */
	public void failDontCares(List<FaultTreeNode> changedNodes, Set<BasicEvent> orderDependentBasicEvents) {
		Stack<FaultTreeNode> toProcess = new Stack<>();
		toProcess.addAll(changedNodes);
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode ftn = toProcess.pop();
			List<FaultTreeNode> parents = ftHolder.getMapNodeToParents().get(ftn);

			boolean allParentsPermanent = !parents.isEmpty();
			for (FaultTreeNode parent : parents) {
				int parentNodeID = ftHolder.getNodeIndex(parent);
				if (!permanentNodes.get(parentNodeID)) {
					allParentsPermanent = false;
					break;
				}
			}
			
			int nodeID = ftHolder.getNodeIndex(ftn);
			if (allParentsPermanent) {
				failedNodes.set(nodeID);
				permanentNodes.set(nodeID);
				activeFaults.remove(ftn);
				
				if (removeClaimedSparesOnFailure(ftn)) {
					mapSpareToClaimedSpares.remove(ftn);
				}
			}
			
			if (permanentNodes.get(nodeID)) {
				List<FaultTreeNode> subNodes = ftHolder.getMapNodeToSubNodes().get(ftn);
				if (subNodes != null) {
					for (FaultTreeNode subNode : subNodes) {
						if (!toProcess.contains(subNode)) {
							int subNodeID = ftHolder.getNodeIndex(subNode);
							if (!permanentNodes.get(subNodeID)) {
								toProcess.push(subNode);
							}
						}
					}
				}
				
				Set<BasicEvent> basicEvents = ftHolder.getMapFaultToBasicEvents().get(ftn);
				if (basicEvents != null) {
					for (BasicEvent be : basicEvents) {
						int beID = ftHolder.getNodeIndex(be);
						failedNodes.set(beID);
						permanentNodes.set(beID);
						if (orderDependentBasicEvents.contains(be)) {
							if (!orderedBes.contains(be)) {
								orderedBes.add(be);
							}
						} else {
							unorderedBes.add(be);
						}
					}
				}
				
				if (removeClaimedSparesOnFailure(ftn)) {
					mapSpareToClaimedSpares.remove(ftn);
				}
			}
		}
	}
	
	/**
	 * Whether or not claimed spares should be removed upon failure of a node.
	 * Overwrite to customize.
	 * @param node the node to check
	 * @return per default true for all permanent nodes
	 */
	protected boolean removeClaimedSparesOnFailure(FaultTreeNode node) {
		return isFaultTreeNodePermanent(node);
	}
	
	/**
	 * Handles the state update of a fault tree nodes that can be triggered
	 * @param node the node which may have changed
	 * @param depTriggers the triggers of the node
	 * @return true iff the state of the node was changed
	 */
	public boolean handleUpdateTriggers(FaultTreeNode node, List<FaultTreeNode> depTriggers) {
		boolean hasChangedNodeState = false;
		
		for (FaultTreeNode depTrigger : depTriggers) {
			if (hasFaultTreeNodeFailed(depTrigger)) {
				hasChangedNodeState = true;
				if (depTrigger instanceof FDEP) {
					hasChangedNodeState |= setFaultTreeNodeFailed(node, true);
					
					int nodeIDTrigger = ftHolder.getNodeIndex(depTrigger);
					if (permanentNodes.get(nodeIDTrigger)) {
						int nodeID = ftHolder.getNodeIndex(node);
						permanentNodes.set(nodeID, true);
					}
				} else if (depTrigger instanceof RDEP) {
					Set<FaultTreeNode> affectors = mapNodeToAffectors.get(node);
					if (affectors == null) {
						affectors = new HashSet<>();
						mapNodeToAffectors.put(node, affectors);
					}
					affectors.add(depTrigger);
				}
			}
		}
	
		return hasChangedNodeState;
	}
	
	/**
	 * Gets all affectors for a given node
	 * @param node the node
	 * @return the affectors
	 */
	public Set<FaultTreeNode> getAffectors(FaultTreeNode node) {
		return mapNodeToAffectors.getOrDefault(node, Collections.emptySet());
	}
	
	/**
	 * Gets the extra fail rate factor for a given basic event
	 * @param be the basic event
	 * @return the extra fail rate factor
	 */
	public double getExtraRateFactor(BasicEvent be) {
		Set<FaultTreeNode> affectors = getAffectors(be);
		double extraRateFactor = 1;
		for (FaultTreeNode affector : affectors) {
			if (affector instanceof RDEP) {
				RDEP rdep = (RDEP) affector;
				extraRateFactor += rdep.getRateChangeBean().getValueToBaseUnit() - 1;
			}
		}
		return extraRateFactor;
	}
	
	/**
	 * Checks if the fail state of any node in the given list is different in the given two states
	 * @param stateOther another state
	 * @param nodes list of nodes to be checked
	 * @return true iff there exists a fault tree node such that state1 and state2 do not agree on the fail state of that fault tree node
	 */
	public boolean hasFailStateChanged(ExplicitDFTState stateOther, List<FaultTreeNode> nodes) {
		for (FaultTreeNode node : nodes) {
			if (hasFaultTreeNodeFailed(node) != stateOther.hasFaultTreeNodeFailed(node)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Gets the set of all failed basic events
	 * @return the set of all failed basic events
	 */
	public Set<BasicEvent> getFailedBasicEvents() {
		Set<BasicEvent> failedBasicEvents = new HashSet<>();
		failedBasicEvents.addAll(orderedBes);
		failedBasicEvents.addAll(unorderedBes);
		return failedBasicEvents;
	}
	
	/**
	 * Checks if this state is equivalent to another state
	 * that already has been confirmed to have the same failed basic events
	 * @param other the other state, must have the same failed basic events
	 * @return true iff also the claims and the order of the ordered failed basic events match
	 */
	public boolean isEquivalent(ExplicitDFTState other) {
		if (recoveryStrategy != null) {
			if (!recoveryStrategy.getCurrentState().equals(other.getRecoveryStrategy().getCurrentState())) {
				return false;
			}
		}
			
		boolean sameClaims = other.getMapSpareToClaimedSpares().equals(getMapSpareToClaimedSpares());	
		if (sameClaims) {
			boolean sameFms = orderedBes.equals(other.orderedBes);
			if (sameFms) {
				boolean sameFailingNodes = failingNodes.equals(other.failingNodes);
				return sameFailingNodes;
			}
		}
		
		return false;
	}
}