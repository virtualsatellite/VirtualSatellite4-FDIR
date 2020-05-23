/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Class for representing the state of a DFT
 * @author muel_s8
 *
 */

public class DFTState extends MarkovState {
	private boolean isFailState;
	private RecoveryStrategy recoveryStrategy;
	
	protected FaultTreeHolder ftHolder;
	
	private Map<FaultTreeNode, FaultTreeNode> mapSpareToClaimedSpares;
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapNodeToAffectors;
	
	private Set<Fault> activeFaults;
	
	private BitSet failedNodes;
	private BitSet permanentNodes;
	private BitSet failingNodes;
	
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapParentToSymmetryRequirements;
	
	private List<BasicEvent> orderedBes;
	private Set<BasicEvent> unorderedBes;
	
	/**
	 * Default Constructor
	 * @param ftHolder the fault tree
	 */
	public DFTState(FaultTreeHolder ftHolder) {
		this.ftHolder = ftHolder;
		int countNodes = ftHolder.getNodes().size();
		failedNodes = new BitSet();
		mapSpareToClaimedSpares = new HashMap<>(countNodes);
		mapNodeToAffectors = new HashMap<>();
		activeFaults = new HashSet<>();
		permanentNodes = new BitSet(countNodes);
		failingNodes = new BitSet(countNodes);
		orderedBes = new ArrayList<>();
		unorderedBes = new HashSet<>();
	}
	
	/**
	 * Copy constructor
	 * @param other the markov state that will be copied
	 */
	public DFTState(DFTState other) {
		orderedBes = new ArrayList<>(other.orderedBes);
		unorderedBes = new HashSet<>(other.unorderedBes);
		activeFaults = new HashSet<>(other.activeFaults);
		mapNodeToAffectors = new HashMap<>();
		for (Entry<FaultTreeNode, Set<FaultTreeNode>> entry : other.mapNodeToAffectors.entrySet()) {
			mapNodeToAffectors.put(entry.getKey(), new HashSet<>(entry.getValue()));
		}
		mapSpareToClaimedSpares = new HashMap<>(other.mapSpareToClaimedSpares);
		failedNodes = (BitSet) other.failedNodes.clone();
		permanentNodes = (BitSet) other.permanentNodes.clone();
		failingNodes = (BitSet) other.failingNodes.clone();
		ftHolder = other.ftHolder;
		recoveryStrategy = other.recoveryStrategy;
		index = other.index + 1;
	}
	
	/**
	 * Sets the recovery strategy state
	 * @param recoveryStrategy the recovery strategy state
	 */
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}
	
	/**
	 * Gets the recovery strategy state
	 * @return the recovery strategy state
	 */
	public RecoveryStrategy getRecoveryStrategy() {
		return recoveryStrategy;
	}
	
	/**
	 * Gets the ordered bes that have occured in this state
	 * @return a list of ordered bes
	 */
	public List<BasicEvent> getOrderedBes() {
		return orderedBes;
	}
	
	/**
	 * Gets the unordered bes that have occured in this state
	 * @return a set of unordered bes
	 */
	public Set<BasicEvent> getUnorderedBes() {
		return unorderedBes;
	}
	
	/**
	 * Sets whether this DFT state should be marked as a fail state
	 * @param isFailState true iff the dft state is a fail state
	 */
	public void setFailState(boolean isFailState) {
		this.isFailState = isFailState;
	}
	
	/**
	 * Whether this state is a fail state
	 * @return true iff this state is a fail state
	 */
	public boolean getFailState() {
		return isFailState;
	}
	
	@Override
	public String toString() {
		String res = index + " [label=\"";
		
		res += getLabel();
		
		res += "\"";
		
		if (isFailState) {
			res += ", color=\"red\"";
		} else if (!isMarkovian()) {
			res += ", color=\"blue\"";
		}
		
		res += "]";
		
		return res;
	}
	
	/**
	 * Gets a label string for this DFTState for the purpose of printing
	 * @return a label for this state
	 */
	
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
	 * Gets the fault tree
	 * @return the fault tree
	 */
	public FaultTreeHolder getFTHolder() {
		return ftHolder;
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
	 * @return true iff the permanence was changed
	 */
	public boolean setFaultTreeNodePermanent(FaultTreeNode node, boolean permanent) {
		int nodeID = ftHolder.getNodeIndex(node);
		boolean hasChanged = permanentNodes.get(nodeID) != permanent;
		permanentNodes.set(nodeID, permanent);
		return hasChanged;
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
	 * Changes the activation state of the passed node in this state
	 * @param node the node to activate/deactivate
	 * @param activation true to activate, false to deactivate
	 */
	public void setNodeActivation(FaultTreeNode node, boolean activation) {
		if (node instanceof Fault) {
			Fault fault = (Fault) node;
			if (activation) {
				activeFaults.add(fault);
			} else {
				activeFaults.remove(fault);
			}
			
			// All depenency gates in a fault are considered activated as soon as the parent fault is activated
			for (FaultTreeNode gate : fault.getFaultTree().getGates()) {
				if (gate instanceof ADEP) {
					if (activeFaults.contains(gate) ^ activation) {
						setNodeActivation(gate, activation);
					}
				}
			}
			
			for (FaultTreeNode be : ftHolder.getNodes(node, EdgeType.BE)) {
				for (FaultTreeNode trigger : ftHolder.getNodes(be, EdgeType.DEP)) {
					if (activeFaults.contains(trigger) ^ activation) {
						setNodeActivation(trigger, activation);
					}
				}
			}
		}
		
		List<FaultTreeNode> children = ftHolder.getNodes(node, EdgeType.CHILD, EdgeType.MONITOR);
		for (FaultTreeNode child : children) {
			if (node instanceof ADEP && ftHolder.getNodes(child, EdgeType.PARENT).size() > 1) {
				continue;
			}
			if (!activeFaults.contains(child)) {
				setNodeActivation(child, activation);
			}
		}
	}
	
	/**
	 * Makes a DFT state uniform by failing all basic events and nodes about we
	 * dont care what exact state we have
	 * @param changedNodes the nodes that have changed in this dft state
	 * @param staticAnalysis static anaylsis data
	 */
	public void failDontCares(List<FaultTreeNode> changedNodes, DFTStaticAnalysis staticAnalysis) {
		Stack<FaultTreeNode> toProcess = new Stack<>();
		toProcess.addAll(changedNodes);
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode ftn = toProcess.pop();
			List<FaultTreeNode> parents = ftHolder.getNodes(ftn, EdgeType.PARENT);

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
				
				removeClaimedSparesOnPermanentFailureIfPossible(ftn);
			}
			
			if (permanentNodes.get(nodeID)) {
				List<FaultTreeNode> subNodes = ftHolder.getMapNodeToSubNodes().getOrDefault(ftn, Collections.emptyList());
				for (FaultTreeNode subNode : subNodes) {
					if (!toProcess.contains(subNode)) {
						int subNodeID = ftHolder.getNodeIndex(subNode);
						if (!permanentNodes.get(subNodeID)) {
							toProcess.push(subNode);
						}
					}
				}
				
				List<FaultTreeNode> basicEvents = ftHolder.getNodes(ftn, EdgeType.BE);
				for (FaultTreeNode be : basicEvents) {
					executeBasicEvent((BasicEvent) be, false, staticAnalysis.getOrderDependentBasicEvents().contains(be), false);
				}
				
				removeClaimedSparesOnPermanentFailureIfPossible(ftn);
			}
		}
	}
	
	/**
	 * Executes a single basic event in the current state
	 * @param be the basic event to execute
	 * @param isRepair whether its the repair or a failure event
	 * @param isOrderDependent whether the event has order depencies
	 * @param isTransient whether the event occurss transiently or permanently
	 * @return true iff the basic event successfully caused some change
	 */
	public boolean executeBasicEvent(BasicEvent be, boolean isRepair, boolean isOrderDependent, boolean isTransient) {
		Collection<BasicEvent> beCollection = isOrderDependent ? getOrderedBes() : getUnorderedBes();
		setFaultTreeNodeFailed(be, !isRepair);
		setFaultTreeNodePermanent(be, !isTransient);
		
		return isRepair ? beCollection.remove(be) : beCollection.add(be);
	}
	
	/**
	 * Whether or not claimed spares should be removed upon failure of a node.
	 * Overwrite to customize.
	 * @param node the node to check
	 * @return per default true for all permanent nodes
	 */
	protected boolean removeClaimedSparesOnPermanentFailureIfPossible(FaultTreeNode node) {
		for (FaultTreeNode parent : ftHolder.getNodes(node, EdgeType.PARENT)) {
			if (!isFaultTreeNodePermanent(parent)) {
				return false;
			}
		}
		
		FaultTreeNode oldClaimingSpareGate = mapSpareToClaimedSpares.remove(node);
		if (oldClaimingSpareGate != null) {
			List<FaultTreeNode> spares = ftHolder.getNodes(oldClaimingSpareGate, EdgeType.SPARE);
			boolean hasClaim = false;
			for (FaultTreeNode spare : spares) {
				FaultTreeNode claimingSpareGateOther = getMapSpareToClaimedSpares().get(spare);
				if (claimingSpareGateOther != null && oldClaimingSpareGate.equals(claimingSpareGateOther)) {
					hasClaim = true;
					break;
				}
			}
			
			if (!hasClaim) {
				for (FaultTreeNode primary : ftHolder.getNodes(oldClaimingSpareGate, EdgeType.CHILD)) {
					setNodeActivation(primary, true);
				}
			}
		}
		
		return true;
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
				boolean addAffector = false;
				
				if (depTrigger instanceof FDEP) {
					hasChangedNodeState |= setFaultTreeNodeFailed(node, true);
					
					int nodeIDTrigger = ftHolder.getNodeIndex(depTrigger);
					if (permanentNodes.get(nodeIDTrigger)) {
						int nodeID = ftHolder.getNodeIndex(node);
						permanentNodes.set(nodeID, true);
					} else if (hasChangedNodeState) {
						addAffector = true;
					}
				} else {
					addAffector = true;
				}
				
				if (addAffector) {
					Set<FaultTreeNode> affectors = mapNodeToAffectors.computeIfAbsent(node, v -> new HashSet<>());
					affectors.add(depTrigger);
				}
				
			} else if (getAffectors(node).contains(depTrigger)) {
				if (depTrigger instanceof FDEP) {
					hasChangedNodeState |= setFaultTreeNodeFailed(node, false);
				} 
				
				getAffectors(node).remove(depTrigger);
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
	public boolean isEquivalent(DFTState other) {
		if (isMarkovian() != other.isMarkovian()) {
			return false;
		}
		
		if (recoveryStrategy != null && !recoveryStrategy.getCurrentState().equals(other.getRecoveryStrategy().getCurrentState())) {				
			return false;
		}
		
		boolean sameClaims = other.getMapSpareToClaimedSpares().equals(getMapSpareToClaimedSpares());	
		if (!sameClaims) {
			return false;
		}
		
		boolean sameFms = orderedBes.size() == other.orderedBes.size() && orderedBes.equals(other.orderedBes);
		if (!sameFms) {
			return false;
		}
		
		if (isFailState != other.isFailState) {
			return false;
		}
				
		boolean sameFailingNodes = failingNodes.equals(other.failingNodes);
		if (!sameFailingNodes) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the symmetry requirements from this state
	 * @return the symmetry requirements
	 */
	public Map<FaultTreeNode, Set<FaultTreeNode>> getMapParentToSymmetryRequirements() {
		if (mapParentToSymmetryRequirements == null) {
			mapParentToSymmetryRequirements = new HashMap<>();
		}
		return mapParentToSymmetryRequirements;
	}

	public DFTState copy() {
		return new DFTState(this);
	}
}
