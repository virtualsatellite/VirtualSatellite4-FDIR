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
	
	private DFTSemantics semantics = DFTSemantics.createNDDFTSemantics();
	
	private FailableBasicEventsProvider failableBasicEventsProvider;
	
	private boolean allowsDontCareFailing = true;
	private boolean permanence = true;

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

	@Override
	public List<DFTState> generateSuccs(DFTState state, SubMonitor monitor) {
		List<DFTState> newSuccs = new ArrayList<>();
		List<IDFTEvent> occurableEvents = getOccurableEvents(state);
		if (state.isProbabilisic() && occurableEvents.size() > 1 && occurableEvents.stream().allMatch(event -> event instanceof ImmediateObservationEvent)) {
			// ImmediateObservationEvents need to be grouped but ObservationEvents can either be repair or not. Repairs take priority.
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
			StateUpdateResult stateUpdateResult = semantics.performUpdate(stateUpdate, monitor);
			checkCancellation(monitor);
			List<DFTState> newSuccsStateUpdate = handleStateUpdate(stateUpdate, stateUpdateResult, monitor);
			newSuccs.addAll(newSuccsStateUpdate);
		}
		
		return newSuccs;
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
	private List<DFTState> handleStateUpdate(StateUpdate stateUpdate, StateUpdateResult stateUpdateResult, SubMonitor monitor) {
		DFTState markovSucc = null;
		if (recoveryStrategy != null) {			
			synchronizeWithRecoveryStrategy(stateUpdateResult, monitor);
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
		
		List<DFTState> newSuccs = handleGeneratedSuccs(stateUpdateResult, markovSucc, monitor);
		return newSuccs;
	}

	/**
	 * If we have a recovery strategy, resolve the update such that we only have one deterministic successor
	 * for this we extract the input from the previous update and then repeat the update with the
	 * determined recovery actions
	 * @param stateUpdate the state update
	 * @param stateUpdateResult the state update result
	 */
	private void synchronizeWithRecoveryStrategy(StateUpdateResult stateUpdateResult, SubMonitor monitor) {
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
			semantics.propagateStateUpdate(stateUpdateResult, worklist, monitor);
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
	private List<DFTState> handleGeneratedSuccs(StateUpdateResult stateUpdateResult, DFTState markovSucc, SubMonitor monitor) {
		StateUpdate stateUpdate = stateUpdateResult.getStateUpdate();
		List<DFTState> newSuccs = new ArrayList<>();
		
		for (DFTState succ : stateUpdateResult.getSuccs()) {
			checkCancellation(monitor);
			if (allowsDontCareFailing) {
				succ.failDontCares(stateUpdateResult.getChangedNodes());
			}
			
			determineStateType(stateUpdateResult, markovSucc, stateUpdate, succ);
			
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
	 * Determines the state type of a successor state
	 * @param stateUpdateResult the update result that created this state
	 * @param markovState the state
	 * @param stateUpdate the state update
	 * @param succ the generated successor state
	 */
	private void determineStateType(StateUpdateResult stateUpdateResult, DFTState markovState, StateUpdate stateUpdate, DFTState succ) {
		succ.setType(hasImmediateEvents(succ) ? MarkovStateType.PROBABILISTIC : MarkovStateType.MARKOVIAN);
		if (hasImmediateEvents(succ)) {
			// Sets the state to the correct type if there are Immediate Observations since they require an order
			boolean lockNonRepairs = false;
			boolean lockImmediateTLE = false;
			boolean lockOldMarkovianObservations = false;
			boolean lockOldImmediateObservations = false;
			for (IDFTEvent event : events) {
				if (!event.canOccur(succ)) {
					continue;
				}
				
				if (event.getNodes().isEmpty() && stateUpdateResult.getMapStateToRecoveryActions().get(succ).isEmpty()) {
					continue;
				}
				
				if (event instanceof ImmediateObservationEvent) {
					ImmediateObservationEvent immediateObservationEvent = (ImmediateObservationEvent) event;
					if (!lockImmediateTLE && ftHolder.getRoot().equals(immediateObservationEvent.getNode())) {
						// If observing the Top Level Event has not been locked and the Top Level Event is observed
						succ.setType(MarkovStateType.PROBABILISTIC);
						lockNonRepairs = true;
					}
					if (succIsProbabilistic(immediateObservationEvent, stateUpdate, markovState)) {
						succ.setType(MarkovStateType.PROBABILISTIC);
						break;
					} else if (!lockOldImmediateObservations) {
						succ.setType(MarkovStateType.PROBABILISTIC);
						lockOldMarkovianObservations = true;
					}
				} else if (event instanceof ImmediateFaultEvent) {
					succ.setType(MarkovStateType.PROBABILISTIC);
					break;
				} else if (event instanceof FaultEvent && ((FaultEvent) event).isRepair()) {
					// Checks whether the repair only affects an observer
					if (nonMonitorParentExists(event)) {
						// Such a repair event takes priority over the Top Level Event
						succ.setType(MarkovStateType.MARKOVIAN);
						lockImmediateTLE = true;
					}
				} else if (!lockNonRepairs && lockOldImmediateObservations(event, stateUpdate, markovState)) {
					succ.setType(MarkovStateType.MARKOVIAN);
					lockOldImmediateObservations = true;
				} else if (!lockNonRepairs && !lockOldMarkovianObservations) {
					succ.setType(MarkovStateType.MARKOVIAN);
				}
			}
		}
	}
	
	/**
	 * Gets a list of all fault events that can occur in a given state
	 * @param state the current state
	 * @return the list of all events that can occur
	 */
	private List<IDFTEvent> getOccurableEvents(DFTState state) {
		if (permanence && state.getFailLabels().contains(FailLabel.FAILED) 
				&& (!(state instanceof PODFTState) || state.getFailLabels().contains(FailLabel.OBSERVED))
				&& state.isFaultTreeNodePermanent(ftHolder.getRoot())) {
			// We do not want permanence with partial observability
			return Collections.emptyList();
		}
		
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
			if (event.isImmediate() && !event.getNodes().isEmpty() && event.canOccur(state)) {
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
			int symmetryMultiplier = (symmetryReduction != null  && event.getNodes() != null) ? symmetryReduction.getSymmetryMultiplier(event.getNodes().iterator().next(), state) : 1;
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
	 * Sets the node semantics for the converter
	 * @param dftSemantics the node semantics of the dft nodes
	 */
	public void setPermanence(boolean permanence) {
		this.permanence = permanence;
	}
	
	/**
	 * Gets the internal semantics obeject
	 * @return the internal semantics object
	 */
	public DFTSemantics getDftSemantics() {
		return semantics;
	}
	
	/**
	 * Checks whether the successor is required to be probabilistic or not
	 * @param immediateObservationEvent the immediate observation event
	 * @param stateUpdate the state update
	 * @param markovSucc the intermediate non-deterministic state
	 * @return whether the successor has to be probabilistic or not
	 */
	private boolean succIsProbabilistic(ImmediateObservationEvent immediateObservationEvent, StateUpdate stateUpdate, DFTState markovSucc) {
		if (immediateObservationEvent.isRepair()) {
			return true;
		}
		
		if (markovSucc == null && !((PODFTState) stateUpdate.getState()).getObservedFailedNodes().containsAll(immediateObservationEvent.getNodes())) {
			// If the observations provide information that was previously not known
			return true;
		}
		
		if (markovSucc != null && !((PODFTState) markovSucc).getObservedFailedNodes().containsAll(immediateObservationEvent.getNodes())) {
			return true;
		} 
			
		return false;
	}
	
	/**
	 * Checks whether a non-MONITOR parent exists
	 * @param event the IDFT event
	 * @return whether the event's node has a non-MONITOR parent
	 */
	private boolean nonMonitorParentExists(IDFTEvent event) {
		Set<FaultTreeNode> parents = ftHolder.getMapNodeToAllParents().get(event.getNodes().iterator().next());
		for (FaultTreeNode node : parents) {
			if (!node.getName().equals(event.getNodes().iterator().next().getName()) && !(node instanceof MONITOR)) {
				// if there is a node other than the event's node that is not a monitor
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the old immediate observations should be locked
	 * @param immediateObservationEvent the immediate observation event
	 * @param stateUpdate the state update
	 * @param markovSucc the intermediate non-deterministic state
	 * @return whether the successor can be Markovian and old immediate observations be locked
	 */
	private boolean lockOldImmediateObservations(IDFTEvent event, StateUpdate stateUpdate, DFTState markovSucc) {
		if (event instanceof ObservationEvent && ((ObservationEvent) event).getNodes().iterator().next().equals(ftHolder.getRoot())) {
			// If the Top Level Event is observed
			return true;
		}
		
		if (event instanceof FaultEvent) {
			// If the event is just a Markovian Fault Event
			return true;
		}
		
		if (markovSucc == null && !((PODFTState) stateUpdate.getState()).getObservedFailedNodes().containsAll(event.getNodes())) {
			// If the observations provide information that was previously not known
			return true;
		} 
		
		if (markovSucc != null && !((PODFTState) markovSucc).getObservedFailedNodes().containsAll(event.getNodes())) {
			return true;
		} 
		
		return false;
	}
}
