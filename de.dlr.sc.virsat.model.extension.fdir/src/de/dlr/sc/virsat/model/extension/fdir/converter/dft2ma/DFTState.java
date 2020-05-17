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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Class for representing the state of a DFT
 * @author muel_s8
 *
 */

public class DFTState extends MarkovState {
	protected boolean isFailState;
	protected RecoveryStrategy recoveryStrategy;
	
	protected FaultTreeHolder ftHolder;
	
	private Map<FaultTreeNode, FaultTreeNode> mapSpareToClaimedSpares;
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapNodeToAffectors;
	
	private Set<Fault> activeFaults;
	
	protected BitSet failedNodes;
	protected BitSet permanentNodes;
	protected BitSet failingNodes;
	
	private Map<FaultTreeNode, Set<FaultTreeNode>> mapParentToSymmetryRequirements;
	
	List<BasicEvent> orderedBes;
	Set<BasicEvent> unorderedBes;
	
	/**
	 * Default Constructor
	 * @param ftHolder the fault tree
	 */
	public DFTState(FaultTreeHolder ftHolder) {
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
	public DFTState(DFTState other) {
		orderedBes = new ArrayList<>(other.orderedBes);
		unorderedBes = new HashSet<>(other.unorderedBes);
		activeFaults = new HashSet<>(other.activeFaults);
		mapNodeToAffectors = new HashMap<>(other.mapNodeToAffectors);
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
	
	public void setMapSpareToClaimedSpares(Map<FaultTreeNode, FaultTreeNode> mapSpareToClaimedSpares) {
		this.mapSpareToClaimedSpares = mapSpareToClaimedSpares;
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
		if (node instanceof BasicEvent) {
			if (activation) {
				activeFaults.add(node.getFault());
			} else {
				activeFaults.remove(node.getFault());
			}
			return;
		}
		
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
				
				if (removeClaimedSparesOnFailureIfPossible(ftn)) {
					mapSpareToClaimedSpares.remove(ftn);
				}
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
					int beID = ftHolder.getNodeIndex(be);
					failedNodes.set(beID);
					permanentNodes.set(beID);
					if (staticAnalysis.getOrderDependentBasicEvents().contains(be)) {
						if (!orderedBes.contains(be)) {
							orderedBes.add((BasicEvent) be);
						}
					} else {
						unorderedBes.add((BasicEvent) be);
					}
					
					List<FaultTreeNode> deps = ftHolder.getNodes(be, EdgeType.DEP);
					for (FaultTreeNode dep : deps) {
						if (!toProcess.contains(dep)) {
							int depID = ftHolder.getNodeIndex(dep);
							if (!permanentNodes.get(depID)) {
								toProcess.push(dep);
							}
						}
					}
				}
				
				removeClaimedSparesOnFailureIfPossible(ftn);
			}
		}
	}
	
	/**
	 * Whether or not claimed spares should be removed upon failure of a node.
	 * Overwrite to customize.
	 * @param node the node to check
	 * @return per default true for all permanent nodes
	 */
	protected boolean removeClaimedSparesOnFailureIfPossible(FaultTreeNode node) {
		if (!isFaultTreeNodePermanent(node)) {
			return false;
		}
		
		for (FaultTreeNode parent : ftHolder.getNodes(node, EdgeType.PARENT)) {
			if (!isFaultTreeNodePermanent(parent)) {
				return false;
			}
		}
		
		FaultTreeNode oldCLaimingSpareGate = mapSpareToClaimedSpares.remove(node);
		if (oldCLaimingSpareGate != null) {
			List<FaultTreeNode> spares = ftHolder.getNodes(oldCLaimingSpareGate, EdgeType.SPARE);
			boolean hasClaim = false;
			for (FaultTreeNode spare : spares) {
				FaultTreeNode claimingSpareGateOther = getMapSpareToClaimedSpares().get(spare);
				if (claimingSpareGateOther != null && oldCLaimingSpareGate.equals(claimingSpareGateOther)) {
					hasClaim = true;
					break;
				}
			}
			
			if (!hasClaim) {
				for (FaultTreeNode primary : ftHolder.getNodes(oldCLaimingSpareGate, EdgeType.CHILD)) {
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
	public boolean hasFailStateChanged(DFTState stateOther, List<FaultTreeNode> nodes) {
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
	public boolean isEquivalent(DFTState other) {
		if (isMarkovian() != other.isMarkovian()) {
			return false;
		}
		
		if (recoveryStrategy != null) {
			if (!recoveryStrategy.getCurrentState().equals(other.getRecoveryStrategy().getCurrentState())) {				
				return false;
			}
		}
		
		boolean sameClaims = other.getMapSpareToClaimedSpares().equals(getMapSpareToClaimedSpares());	
		if (sameClaims) {
			boolean sameFms = orderedBes.size() == other.orderedBes.size() && orderedBes.equals(other.orderedBes);
			if (sameFms) {
				if (isFailState != other.isFailState) {
					return false;
				}
				
				boolean sameFailingNodes = failingNodes.equals(other.failingNodes);
				return sameFailingNodes;
			}
		}
		
		return false;
	}
	
	/**
	 * Creates the symmetry requirements for this state
	 * @param predecessor the predecessor state
	 * @param basicEvent the basic event that has failed
	 * @param symmetryReduction the symmetry reduction
	 */
	public void createSymmetryRequirements(DFTState predecessor, BasicEvent basicEvent, Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction) {
		if (mapParentToSymmetryRequirements == null) {
			mapParentToSymmetryRequirements = new HashMap<>(predecessor.getMapParentToSymmetryRequirements());
		} else {
			mapParentToSymmetryRequirements.putAll(predecessor.getMapParentToSymmetryRequirements());
		}
		
		Set<FaultTreeNode> checkedNodes = new HashSet<>();
		Queue<FaultTreeNode> queue = new LinkedList<>();
		Set<FaultTreeNode> allParents = ftHolder.getMapNodeToAllParents().get(basicEvent);
		queue.add(basicEvent);
		checkedNodes.add(basicEvent);
		
		while (!queue.isEmpty()) {
			FaultTreeNode node = queue.poll();
			List<FaultTreeNode> biggerNodes = symmetryReduction.get(node);
			if (biggerNodes != null && !biggerNodes.isEmpty()) {
				List<FaultTreeNode> parents = ftHolder.getNodes(node, EdgeType.PARENT);
				for (FaultTreeNode parent : parents) {
					boolean continueToParent = hasFaultTreeNodeFailed(parent);
					
					if (!continueToParent) {
						Set<FaultTreeNode> processedBiggerParents = new HashSet<>();
						for (FaultTreeNode biggerNode : biggerNodes) {
							List<FaultTreeNode> biggerParents = ftHolder.getNodes(biggerNode, EdgeType.PARENT);
							for (FaultTreeNode biggerParent : biggerParents) {
								if (processedBiggerParents.add(biggerParent)) {
									if (!allParents.contains(biggerParent)) {
										Set<FaultTreeNode> symmetryRequirements = mapParentToSymmetryRequirements.computeIfAbsent(biggerParent, key -> new HashSet<>());
										continueToParent |= symmetryRequirements.add(biggerNode);
									}
								}
							}
						}
					}
					
					if (continueToParent && checkedNodes.add(parent)) {
						queue.add(parent);
					}
				}
			}
		}
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
