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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Constructs a markov automaton from a DFT.
 * @author muel_s8
 *
 */

public class DFT2MAConverter {
	private DFTSemantics dftSemantics = DFTSemantics.createNDDFTSemantics();
	
	private FaultTreeNode root;
	
	private MarkovAutomaton<DFTState> ma;
	private DFTState initial;

	private Set<IDFTEvent> events;
	private Set<FaultTreeNode> transientNodes;
	private Set<BasicEvent> orderDependentBasicEvents;
	private Map<Set<BasicEvent>, List<DFTState>> mapUnorderedBesToMarkovianDFTStates;
	private FaultTreeHolder ftHolder;
	private RecoveryStrategy recoveryStrategy;
	private Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction;
	private Map<FaultTreeNode, Set<FaultTreeNode>> symmetryReductionInverted;
	
	private boolean enableSymmetryReduction = false;
	
	/**
	 * Converts a fault tree with the passed node as a root to a
	 * Markov automaton.
	 * @param root a fault tree node used as a root node for the conversion
	 * @return the generated Markov automaton resulting from the conversion
	 */
	public MarkovAutomaton<DFTState> convert(FaultTreeNode root) {
		this.root = root;
		
		init();
		staticAnalysis();
		buildMA();
		
		System.out.println("STATES: " + ma.getStates().size());
		
		return ma;
	}
	
	/**
	 * Initializes the data structures
	 */
	private void init() {
		orderDependentBasicEvents = new HashSet<>();
		mapUnorderedBesToMarkovianDFTStates = new HashMap<>();
		transientNodes = new HashSet<>();
		
		ftHolder = new FaultTreeHolder(root);
		
		events = dftSemantics.createEventSet(ftHolder);
		for (BasicEvent be : ftHolder.getMapBasicEventToFault().keySet()) {
			if (be.isSetRepairRate() && be.getRepairRate() > 0) {
				transientNodes.add(be);			
			}
		}
		
		if (recoveryStrategy != null) {
			events.addAll(recoveryStrategy.createEventSet());
		}
		
		if (enableSymmetryReduction) {
			FaultTreeSymmetryChecker symmetryChecker = new FaultTreeSymmetryChecker();
			symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
			symmetryReductionInverted = new HashMap<>();
			for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : symmetryReduction.entrySet()) {
				for (FaultTreeNode node : entry.getValue()) {
					if (!node.equals(entry.getKey())) {
						symmetryReductionInverted.computeIfAbsent(node, v -> new HashSet<>()).add(entry.getKey());
					} else {
						symmetryReductionInverted.computeIfAbsent(node, v -> new HashSet<>());
					}
				}
			}
		}
	}
	
	/**
	 * Perform a static analysis to obtain useful information for improving runtime performance
	 */
	private void staticAnalysis() {
		for (IDFTEvent event : events) {
			if (event instanceof FaultEvent) {
				FaultEvent faultEvent = (FaultEvent) event;
				if (!faultEvent.isRepair()) {
					if (isOrderDependent(event)) {
						orderDependentBasicEvents.add((BasicEvent) faultEvent.getNode());
					}
				}
			}
		}
	}
	
	/**
	 * Checks if a failure mode event does not have a semantically different effect
	 * if it appears in a different order. E.g. it may not be used in a PAND gate.
	 * @param event the fault event to check
	 * @return true iff the failure mode is order dependent
	 */
	private boolean isOrderDependent(IDFTEvent event) {
		Fault fault = event.getNode().getFault();
		
		Queue<FaultTreeNode> toProcess = new LinkedList<>();
		toProcess.add(fault);
		Set<FaultTreeNode> processed = new HashSet<>();
		
		while (!toProcess.isEmpty()) {
			FaultTreeNode node = toProcess.poll();
			
			if (!processed.add(node)) {
				continue;
			}
			
			FaultTreeNodeType type = node.getFaultTreeNodeType();
			if (type == FaultTreeNodeType.POR) {
				return true;
			}
			
			List<FaultTreeNode> parents = ftHolder.getMapNodeToParents().get(node);
			toProcess.addAll(parents);
		}
		
		return false;
	}
	
	/**
	 * Constructs the actual automaton
	 */
	private void buildMA() {
		FaultTreeHelper ftHelper = new FaultTreeHelper(root.getConcept());
		
		ma = new MarkovAutomaton<DFTState>();
		ma.getEvents().addAll(events);
		
		createInitialState();
		
		Queue<DFTState> toProcess = new LinkedList<>();
		toProcess.offer(initial);
		
		while (!toProcess.isEmpty()) {
			DFTState state = toProcess.poll();
			List<IDFTEvent> occurableEvents = getOccurableEvents(state);
			
			for (IDFTEvent event : occurableEvents) {
				Double rate = event.getRate(state);
				
				// Very simple symmetry reduction to get started
				// Doesnt yet work with deps so disable symmetry reduction if we have deps
				if (enableSymmetryReduction) {
					if (event instanceof FaultEvent) {
						Set<BasicEvent> failedBasicEvents = state.getFailedBasicEvents();
						Queue<FaultTreeNode> queue = new LinkedList<>();
						Set<FaultTreeNode> markedParents = state.getMarkedParents();
						queue.add(ftHolder.getMapBasicEventToFault().get((BasicEvent) event.getNode()));
						boolean hasMarkedParent = false;
						while (!queue.isEmpty()) {
							FaultTreeNode parent = queue.poll();
							if (markedParents.contains(parent)) {
								hasMarkedParent = true;
								break;
							}
							queue.addAll(ftHolder.getMapNodeToParents().get(parent));
						}
						
						if (!hasMarkedParent && !failedBasicEvents.containsAll(symmetryReductionInverted.get(event.getNode()))) {
							continue;
						}
						
						int multiplier = 1;
						for (FaultTreeNode node : symmetryReduction.get(event.getNode())) {
							if (!node.equals(event.getNode())) {
								queue.add(ftHolder.getMapBasicEventToFault().get((BasicEvent) node));
								hasMarkedParent = false;
								while (!queue.isEmpty()) {
									FaultTreeNode parent = queue.poll();
									if (markedParents.contains(parent)) {
										hasMarkedParent = true;
										break;
									}
									queue.addAll(ftHolder.getMapNodeToParents().get(parent));
								}
								if (!hasMarkedParent) {
									multiplier++;
								}
							}
						}
						rate *= multiplier;
					}
				}
				
				DFTState baseSucc = dftSemantics.getStateGenerator().generateState(state);
				baseSucc.setRecoveryStrategy(state.getRecoveryStrategy());
				event.execute(baseSucc, orderDependentBasicEvents, transientNodes);
				
				List<DFTState> succs = new ArrayList<>();
				succs.add(baseSucc);
				
				Map<DFTState, List<RecoveryAction>> mapStateToRecoveryActions = new HashMap<>();
				mapStateToRecoveryActions.put(baseSucc, new ArrayList<RecoveryAction>());
				
				List<FaultTreeNode> changedNodes = dftSemantics.updateFaultTreeNodeToFailedMap(ftHolder, state, 
						succs, mapStateToRecoveryActions, event);
				
				DFTState markovSucc = null;
				if (succs.size() > 1) { 
					if (recoveryStrategy != null) {
						Set<FaultTreeNode> occuredEvents = dftSemantics.extractRecoveryActionInput(ftHolder, baseSucc, event, changedNodes);
						RecoveryStrategy recoveryStrategy = state.getRecoveryStrategy().onFaultsOccured(occuredEvents);
						dftSemantics.determinizeSuccs(ftHelper, recoveryStrategy, succs, mapStateToRecoveryActions);
					} else {
						markovSucc = dftSemantics.getStateGenerator().generateState(baseSucc);
						ma.addState(markovSucc);
						ma.addMarkovianTransition(event, state, markovSucc, rate);
					}
				
				}
				
				for (DFTState succ : succs) {
					succ.failDontCares(changedNodes, orderDependentBasicEvents);
					
					DFTState equivalentState = getEquivalentState(succ);
					
					if (equivalentState == succ) {
						if (enableSymmetryReduction) {
							if (event instanceof FaultEvent) {
								succ.createMarkings(state, (BasicEvent) event.getNode(), symmetryReduction, symmetryReductionInverted);
							}
						}
						
						ma.addState(succ);
						toProcess.offer(succ);
						
						if (succ.hasFaultTreeNodeFailed(root)) {
							ma.getFinalStates().add(succ);
							succ.setFailState(true);
						}
					}
					
					if (markovSucc != null) {
						List<RecoveryAction> actions = mapStateToRecoveryActions.get(succ);
						ma.addNondeterministicTransition(actions, markovSucc, equivalentState);	
					} else {
						ma.addMarkovianTransition(event, state, equivalentState, rate);
					}
				}
			}
		}
	}

	/**
	 * Creates and initializes the initial state of the Markov Automaton
	 */
	private void createInitialState() {
		initial = dftSemantics.getStateGenerator().generateState(ftHolder);
		mapUnorderedBesToMarkovianDFTStates.put(initial.unorderedBes, new ArrayList<>(Collections.singletonList(initial)));
		initial.activateNode(root);
		initial.setRecoveryStrategy(recoveryStrategy);
		ma.addState(initial);
	}
	
	/**
	 * Gets a list of all fault events that can occur in a given state
	 * @param state the current state
	 * @return the list of all events that can occur
	 */
	private List<IDFTEvent> getOccurableEvents(DFTState state) {
		List<IDFTEvent> occurableEvents = new ArrayList<>();
		for (IDFTEvent event : events) {
			if (event.canOccur(state)) {
				occurableEvents.add(event);
			}
		}
		return occurableEvents;
	}
	
	/**
	 * Check if there is an equivalent state of the passed state and return it.
	 * @param state the state for which we want to check if an equivalent one exists
	 * @return an equivalent state to the passed one or the state itself if not equivalent state exists
	 */
	private DFTState getEquivalentState(DFTState state) {
		List<DFTState> states = mapUnorderedBesToMarkovianDFTStates.get(state.unorderedBes);
		if (states == null) {
			List<DFTState> dftStates = new ArrayList<>();
			dftStates.add(state);
			mapUnorderedBesToMarkovianDFTStates.put(state.unorderedBes, dftStates);
			return state;
		}
		
		for (DFTState other : states) {
			if (state.isEquivalent(other)) {
				return other;
			}
		}
		
		states.add(state);
		
		return state;
	}

	/**
	 * Get the initial state of the markov automaton
	 * @return the initial state of the markov automaton
	 */
	public DFTState getInitial() {
		return initial;
	}
	
	/**
	 * Sets the node semantics for the converter
	 * @param dftSemantics the node semantics of the dft nodes
	 */
	public void setSemantics(DFTSemantics dftSemantics) {
		this.dftSemantics = dftSemantics;
	}
	
	/**
	 * Sets the recovery strategy
	 * @param recoveryStrategy the recovery strategy
	 */
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}
	
	/**
	 * Sets whether symmetry reduction should be enabled
	 * @param enableSymmetryReduction set to true for symmetry reduction
	 */
	public void setEnableSymmetryReduction(boolean enableSymmetryReduction) {
		this.enableSymmetryReduction = enableSymmetryReduction;
	}
}
