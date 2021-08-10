/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovStateType;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.AStateSpaceGenerator;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTSymmetryChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.SymmetryReduction;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IRepairableEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateFaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ObservationEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.INodeSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FailableBasicEventsProvider;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFT2MAStateSpaceGenerator extends AStateSpaceGenerator<DFTState> {
	
	private static final long MEMORY_THRESHOLD = 1024 * 1024 * 512;
	
	private DFTSemantics semantics = DFTSemantics.createNDDFTSemantics();
	
	private FailableBasicEventsProvider failableBasicEventsProvider;
	
	private boolean allowsDontCareFailing = true;

	private Collection<IDFTEvent> events;
	private FaultTreeHolder ftHolder;
	private RecoveryStrategy recoveryStrategy;
	private DFTStateEquivalence stateEquivalence;
	
	/**
	 * Configures the state space generator
	 * @param ftHolder the fault tree holder
	 * @param failLabelProvider the fail label criterion
	 * @param failableBasicEventsProvider the nodes that need to fail
	 */
	public void configure(FaultTreeHolder ftHolder, FailableBasicEventsProvider failableBasicEventsProvider) {
		this.ftHolder = ftHolder;
		this.failableBasicEventsProvider = failableBasicEventsProvider;
		
		if (semantics instanceof PONDDFTSemantics) {
			ftHolder.getStaticAnalysis().setSymmetryChecker(null);
			allowsDontCareFailing = false;
		} else {
			ftHolder.getStaticAnalysis().setSymmetryChecker(new DFTSymmetryChecker());
			allowsDontCareFailing = true;
		}
		
		if (failableBasicEventsProvider != null) {
			ftHolder.getStaticAnalysis().setSymmetryChecker(null);
		}
		
	}
	
	@Override
	public DFTState createInitialState() {
		DFTState initialState = semantics.generateState(ftHolder);
		stateEquivalence.getEquivalentState(initialState);
		FaultTreeNode root = ftHolder.getRoot();
		initialState.setNodeActivation(root.getFault(), true);
		if (!root.equals(root.getFault())) {
			initialState.setNodeActivation(root, true);
		}
		initialState.setRecoveryStrategy(recoveryStrategy);
		initialState.setType(hasImmediateEvents(initialState) ? MarkovStateType.PROBABILISTIC : MarkovStateType.MARKOVIAN);
		
		for (FaultTreeNode seqNode : ftHolder.getNodes(FaultTreeNodeType.SEQ)) {
			initialState.setFaultTreeNodePermanent(seqNode, true);
		}
		
		return initialState;
	}
	
	private long maxMemory = Runtime.getRuntime().maxMemory();

	@Override
	public List<DFTState> generateSuccs(DFTState state, SubMonitor monitor) {
		List<DFTState> newSuccs = new ArrayList<>();
		List<IDFTEvent> occurableEvents = getOccurableEvents(state);
		if (state.isProbabilisic() && occurableEvents.size() > 1) {
			IRepairableEvent representantEvent = (IRepairableEvent) occurableEvents.iterator().next();
			boolean isRepair = representantEvent.isRepair();
			List<FaultTreeNode> eventList = new ArrayList<>();
			for (IDFTEvent occurableEvent : occurableEvents) {
				if (((IRepairableEvent) occurableEvent).isRepair() != isRepair) {
					if (!isRepair) {
						eventList.clear();
						isRepair = true;
					} else {
						continue;
					}
				}
				eventList.addAll(occurableEvent.getNodes());
			}
			ImmediateObservationEvent immediateObservationEvent = new ImmediateObservationEvent(eventList, isRepair);
			occurableEvents.clear();
			occurableEvents.add(immediateObservationEvent);
		}
		List<StateUpdate> stateUpdates = getStateUpdates(state, occurableEvents);
		
		for (StateUpdate stateUpdate : stateUpdates) {
			checkCancellation(monitor);
			StateUpdateResult stateUpdateResult = semantics.performUpdate(stateUpdate);
			checkCancellation(monitor);
			List<DFTState> newSuccsStateUpdate = handleStateUpdate(stateUpdate, stateUpdateResult);
			newSuccs.addAll(newSuccsStateUpdate);
		}
		
		return newSuccs;
	}
	
	/**
	 * Checks if this long running operation should cancelled / has been cancelled
	 * and gives user feedback that the operation is still running.
	 * @param monitor a monitor
	 */
	private void checkCancellation(SubMonitor monitor) {
		// Eclipse trick for doing progress updates with unknown ending time
		final int PROGRESS_COUNT = 100;
		monitor.setWorkRemaining(PROGRESS_COUNT).split(1);
		
		long freeMemory = maxMemory - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		if (freeMemory < MEMORY_THRESHOLD) {
			throw new RuntimeException("Close to out of memory. Aborting so we can still maintain an operational state.");
		}
	}
	
	@Override
	public void init(MarkovAutomaton<DFTState> targetMa) {
		super.init(targetMa);
		
		stateEquivalence = new DFTStateEquivalence();
		ftHolder.getStaticAnalysis().perform(ftHolder);
		
		events = createEvents();
		
		if (recoveryStrategy != null) {
			events.addAll(recoveryStrategy.createEventSet());
			INodeSemantics spareSemantics = semantics.getMapTypeToSemantics().get(FaultTreeNodeType.SPARE);
			if (spareSemantics instanceof NDSPARESemantics) {
				((NDSPARESemantics) spareSemantics).setPropagateWithoutActions(true);
			}
		}
	}
	
	/**
	 * Creates the event list using the semantics class
	 * Filters events out that cannot occur due to additional settings such as the failable basic events provider
	 * @return
	 */
	private List<IDFTEvent> createEvents() {
		List<IDFTEvent> events = semantics.createEvents(ftHolder);
		Set<IDFTEvent> unoccurableEvents = new HashSet<>();
		for (IDFTEvent event : events) {
			for (FaultTreeNode node : event.getNodes()) {
				if (node instanceof BasicEvent && failableBasicEventsProvider != null) {
					BasicEvent be = (BasicEvent) node;
					if (!failableBasicEventsProvider.getBasicEvents().contains(be)) {
						unoccurableEvents.add(event);
					}
				}
			}
		}
		events.removeAll(unoccurableEvents);
		
		// Sort the list so that we always have the same ordering of events
		// during all executions. This is to ensure that repeated executions
		// of the state space generator give the same state space.
		Collections.sort(events, IDFTEvent.IDFTEVENT_COMPARATOR);
		
		return events;
	}

	/**
	 * Handles the result of a state update and inserts the successors into the markov automaton
	 * @param stateUpdate the state update
	 * @param stateUpdateResult the result of the state update
	 * @return all newly generated successor states
	 */
	private List<DFTState> handleStateUpdate(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult) {
		DFTState markovSucc = null;
		if (recoveryStrategy != null) {			
			synchronizeWithRecoveryStrategy(stateUpdateResult);
		} else if (stateUpdateResult.getSuccs().size() > 1) { 
			// If we do not have a recovery strategy and either multiple successors
			// or an obsertvation event for which we generally must provide the ability
			// to react, then we need an intermediate non-deterministic state
			
			DFTState interimState = stateUpdateResult.getBaseSucc().copy();
			interimState.setType(MarkovStateType.NONDET);
			
			markovSucc = stateEquivalence.getEquivalentState(interimState);
			if (markovSucc == interimState) {
				targetMa.addState(markovSucc);	
			} else {
				// In the event that the intermediate state already exists
				// then we know that we do not need to handle the generated successors anymore
				// because have already done so in the past
				
				stateUpdateResult.getSuccs().clear();
			}
			if (stateUpdate.getState().isProbabilisic()) {
				targetMa.addProbabilisticTransition(stateUpdate.getEvent(), stateUpdate.getState(), markovSucc, stateUpdate.getRate());
			} else {
				targetMa.addMarkovianTransition(stateUpdate.getEvent(), stateUpdate.getState(), markovSucc, stateUpdate.getRate());
			}
		}
		
		List<DFTState> newSuccs = handleGeneratedSuccs(stateUpdateResult, markovSucc);
		return newSuccs;
	}

	/**
	 * If we have a recovery strategy, resolve the update such that we only have one deterministic successor
	 * for this we extract the input from the previous update and then repeat the update with the
	 * determined recovery actions
	 * @param stateUpdate the state update
	 * @param stateUpdateResult the state update result
	 */
	private void synchronizeWithRecoveryStrategy(StateUpdateResult stateUpdateResult) {
		StateUpdate stateUpdate = stateUpdateResult.getStateUpdate();
		DFTState state = stateUpdate.getState();
		IDFTEvent event = stateUpdate.getEvent();
		DFTState baseSucc = stateUpdateResult.getBaseSucc();
		
		if (event instanceof ImmediateFaultEvent) {
			if (((ImmediateFaultEvent) event).isNegative()) {
				return;
			}
		}
		
		boolean isRepair = stateUpdate.getEvent() instanceof IRepairableEvent 
				? ((IRepairableEvent) stateUpdate.getEvent()).isRepair() : false;
		Set<FaultTreeNode> occuredEvents = semantics.extractRecoveryActionInput(stateUpdateResult);
		RecoveryStrategy recoveryStrategy = occuredEvents.isEmpty() ? baseSucc.getRecoveryStrategy() : state.getRecoveryStrategy().onFaultsOccured(occuredEvents, isRepair);
		
		if (!recoveryStrategy.getRecoveryActions().isEmpty()) {
			DFTState newBaseSucc = stateUpdateResult.init(state);
			event.execute(newBaseSucc);
			List<FaultTreeNode> affectedNodes = recoveryStrategy.execute(newBaseSucc);
			
			Queue<FaultTreeNode> worklist = semantics.createWorklist(event, newBaseSucc);
			worklist.addAll(affectedNodes);
			semantics.propagateStateUpdate(stateUpdateResult, worklist);
		} else {
			baseSucc.setRecoveryStrategy(recoveryStrategy);
		}
	}
	
	/**
	 * Handles the actual insertion of a generated successor into the markov automaton
	 * @param stateUpdateResult the results of a state update
	 * @param markovSucc the markovian intermediate state if available
	 * @return the generated states that are really new
	 */
	private List<DFTState> handleGeneratedSuccs(StateUpdateResult stateUpdateResult, DFTState markovSucc) {
		StateUpdate stateUpdate = stateUpdateResult.getStateUpdate();
		List<DFTState> newSuccs = new ArrayList<>();
		
		for (DFTState succ : stateUpdateResult.getSuccs()) {
			if (allowsDontCareFailing) {
				succ.failDontCares(stateUpdateResult.getChangedNodes());
			}
			
			succ.setType(hasImmediateEvents(succ) ? MarkovStateType.PROBABILISTIC : MarkovStateType.MARKOVIAN);
			
			if (/*(markovSucc != null || recoveryStrategy != null) && */hasImmediateEvents(succ)) {
				boolean lockFaults = false;
				boolean lockTLE = false;
				for (IDFTEvent event : events) {
					if (event.canOccur(succ)) {
						if (event instanceof ImmediateObservationEvent) {
							if (!lockTLE && ((ImmediateObservationEvent) event).getNodes().iterator().next().getName().equals("tle")) {
								succ.setType(MarkovStateType.PROBABILISTIC);
								lockFaults = true;
							}
							if (((ImmediateObservationEvent) event).isRepair()) {
								succ.setType(MarkovStateType.PROBABILISTIC);
								break;
							}
							if (markovSucc == null && !((PODFTState) stateUpdate.getState()).getObservedFailedNodes().containsAll(((ImmediateObservationEvent) event).getNodes())) {
								succ.setType(MarkovStateType.PROBABILISTIC);
								break;
							}
							if (markovSucc != null && !((PODFTState) markovSucc).getObservedFailedNodes().containsAll(((ImmediateObservationEvent) event).getNodes())) {
								succ.setType(MarkovStateType.PROBABILISTIC);
								break;
							}
						} else if (event instanceof ObservationEvent && ((ObservationEvent) event).getNodes().iterator().next().getName().equals("tle")) {
							succ.setType(MarkovStateType.MARKOVIAN);
						} else if (event instanceof FaultEvent && ((FaultEvent) event).isRepair()) {
							Set<FaultTreeNode> parents = ftHolder.getMapNodeToAllParents().get(event.getNodes().iterator().next());
							boolean monitorExists = false;
							for (FaultTreeNode node : parents) {
								if (node instanceof MONITOR) {
									monitorExists = true;
									break;
								}
							}
							if (!monitorExists) {
								succ.setType(MarkovStateType.MARKOVIAN);
								lockTLE = true;
							} else {
								continue;
							}					
						} else if (!lockFaults) {
							succ.setType(MarkovStateType.MARKOVIAN);
						}
					}
				}
			}
			
			checkFailState(succ);
			DFTState equivalentState = stateEquivalence.getEquivalentState(succ);
			
			if (equivalentState == succ) {
				SymmetryReduction symmetryReduction = ftHolder.getStaticAnalysis().getSymmetryReduction();
				if (symmetryReduction != null) {
					if (stateUpdate.getEvent() instanceof FaultEvent) {
						symmetryReduction.createSymmetryRequirements(succ, stateUpdate.getState(), 
								(BasicEvent) stateUpdate.getEvent().getNodes().iterator().next(), stateUpdateResult.getChangedNodes());
					}
				}
				
				targetMa.addState(succ);
				newSuccs.add(succ);
			}
			
			if (markovSucc != null) {
				List<RecoveryAction> actions = stateUpdateResult.getMapStateToRecoveryActions().get(succ);
				targetMa.addNondeterministicTransition(actions, markovSucc, equivalentState);	
			} else if (stateUpdate.getState() != equivalentState) {
				if (stateUpdate.getState().isMarkovian()) {
					targetMa.addMarkovianTransition(stateUpdate.getEvent(), stateUpdate.getState(), equivalentState, stateUpdate.getRate());
				} else {
					targetMa.addProbabilisticTransition(stateUpdate.getEvent(), stateUpdate.getState(), equivalentState, stateUpdate.getRate());
				}
			}
		}
		
		return newSuccs;
	}
	
	/**
	 * Gets a list of all fault events that can occur in a given state
	 * @param state the current state
	 * @return the list of all events that can occur
	 */
	private List<IDFTEvent> getOccurableEvents(DFTState state) {
		/*if (state.getFailLabels().contains(FailLabel.FAILED) 
				&& (!(state instanceof PODFTState) || state.getFailLabels().contains(FailLabel.OBSERVED))
				&& state.isFaultTreeNodePermanent(ftHolder.getRoot())) {
			return Collections.emptyList();
		}*/
		
		List<IDFTEvent> occurableEvents = new ArrayList<>();		
		if (occurableEvents.isEmpty()) {
			for (IDFTEvent event : events) {
				if (event.isImmediate() == state.isProbabilisic() && event.canOccur(state)) {
					occurableEvents.add(event);
				}
			}
		} 
		
		return occurableEvents;
	}
	
	/**
	 * Checks if a state has immediate events enabled
	 * @param state the state to check
	 * @return true iff at least one immediate event can occur
	 */
	private boolean hasImmediateEvents(DFTState state) {
		for (IDFTEvent event : events) {
			if (event.isImmediate() && event.canOccur(state)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Creates the state updates from the list of occurable events
	 * @param state the current state
	 * @param occurableEvents the currently occurable events
	 * @return returns the state updates that should be performed due to the events
	 */
	private List<StateUpdate> getStateUpdates(DFTState state, List<IDFTEvent> occurableEvents) {
		List<StateUpdate> stateUpdates = new ArrayList<>();
		for (IDFTEvent event : occurableEvents) {
			SymmetryReduction symmetryReduction = ftHolder.getStaticAnalysis().getSymmetryReduction();
			int symmetryMultiplier = symmetryReduction != null ? symmetryReduction.getSymmetryMultiplier(event.getNodes().iterator().next(), state) : 1;
			if (symmetryMultiplier != SymmetryReduction.SKIP_EVENT) {
				StateUpdate stateUpdate = new StateUpdate(state, event, symmetryMultiplier);
				stateUpdates.add(stateUpdate);
			}
		}
		
		return stateUpdates;
	}
	
	/**
	 * Checks if the state is a fail state and sets the associated flags
	 * @param state the state to check
	 */
	private void checkFailState(DFTState state) {
		if (state.hasFaultTreeNodeFailed(ftHolder.getRoot())) {
			state.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		}
		
		if (state instanceof PODFTState && ((PODFTState) state).isNodeFailObserved(ftHolder.getRoot())) {
			state.getMapFailLabelToProb().put(FailLabel.OBSERVED, 1d);
		}
	}
	
	/**
	 * Sets the node semantics for the converter
	 * @param dftSemantics the node semantics of the dft nodes
	 */
	public void setSemantics(DFTSemantics dftSemantics) {
		this.semantics = dftSemantics;
	}
	
	/**
	 * Sets the recovery strategy
	 * @param recoveryStrategy the recovery strategy
	 */
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}
	
	/**
	 * Gets the internal semantics obeject
	 * @return the internal semantics object
	 */
	public DFTSemantics getDftSemantics() {
		return semantics;
	}
}
