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

import de.dlr.sc.virsat.fdir.core.markov.AStateSpaceGenerator;
import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovStateType;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.SymmetryReduction;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.IRepairableEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.ImmediateFaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.INodeSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.NDSPARESemantics;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FailableBasicEventsProvider;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFT2MAStateSpaceGenerator extends AStateSpaceGenerator<DFTState> {

	public static final FailLabelProvider DEFAULT_FAIL_LABEL_PROVIDER = new FailLabelProvider(FailLabel.FAILED);
	
	private DFTSemantics semantics = DFTSemantics.createNDDFTSemantics();
	private DFTStaticAnalysis staticAnalysis = new DFTStaticAnalysis();
	
	private FailLabelProvider failLabelProvider;
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
	public void configure(FaultTreeHolder ftHolder, FailLabelProvider failLabelProvider, FailableBasicEventsProvider failableBasicEventsProvider) {
		this.ftHolder = ftHolder;
		this.failLabelProvider = failLabelProvider != null ? failLabelProvider : DEFAULT_FAIL_LABEL_PROVIDER;
		this.failableBasicEventsProvider = failableBasicEventsProvider;
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
		
		return initialState;
	}

	@Override
	public List<DFTState> generateSuccs(DFTState state) {
		List<DFTState> newSuccs = new ArrayList<>();
		List<IDFTEvent> occurableEvents = getOccurableEvents(state);
		List<StateUpdate> stateUpdates = getStateUpdates(state, occurableEvents);
		
		for (StateUpdate stateUpdate : stateUpdates) {
			StateUpdateResult stateUpdateResult = semantics.performUpdate(stateUpdate);
			List<DFTState> newSuccsStateUpdate = handleStateUpdate(stateUpdate, stateUpdateResult);
			newSuccs.addAll(newSuccsStateUpdate);
		}
		
		return newSuccs;
	}
	
	@Override
	public void init(MarkovAutomaton<DFTState> targetMa) {
		super.init(targetMa);
		
		stateEquivalence = new DFTStateEquivalence();
		staticAnalysis.perform(ftHolder);
		
		events = createEvents();
		targetMa.getEvents().addAll(events);
		
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
		List<IDFTEvent> events = semantics.createEvents(ftHolder, staticAnalysis);
		Set<IDFTEvent> unoccurableEvents = new HashSet<>();
		for (IDFTEvent event : events) {
			if (event.getNode() instanceof BasicEvent && failableBasicEventsProvider != null) {
				BasicEvent be = (BasicEvent) event.getNode();
				if (!failableBasicEventsProvider.getBasicEvents().contains(be)) {
					unoccurableEvents.add(event);
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
			targetMa.addMarkovianTransition(stateUpdate.getEvent(), stateUpdate.getState(), markovSucc, stateUpdate.getRate());
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
				succ.failDontCares(stateUpdateResult.getChangedNodes(), staticAnalysis);
			}
			
			succ.setType(hasImmediateEvents(succ) ? MarkovStateType.PROBABILISTIC : MarkovStateType.MARKOVIAN);
			
			checkFailState(succ);
			DFTState equivalentState = stateEquivalence.getEquivalentState(succ);
			
			if (equivalentState == succ) {
				SymmetryReduction symmetryReduction = staticAnalysis.getSymmetryReduction();
				if (symmetryReduction != null) {
					if (stateUpdate.getEvent() instanceof FaultEvent) {
						symmetryReduction.createSymmetryRequirements(succ, stateUpdate.getState(), 
								(BasicEvent) stateUpdate.getEvent().getNode(), stateUpdateResult.getChangedNodes());
					}
				}
				
				targetMa.addState(succ, succ.getFailState() ? 1 : 0);
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
		if (state.getFailState() && state.isFaultTreeNodePermanent(ftHolder.getRoot())) {
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
			SymmetryReduction symmetryReduction = staticAnalysis.getSymmetryReduction();
			int symmetryMultiplier = symmetryReduction != null ? symmetryReduction.getSymmetryMultiplier(event, state) : 1;
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
		for (FailLabel failLabel : failLabelProvider.getFailLabels()) {
			FaultTreeNode root = ftHolder.getRoot();
			switch (failLabel) {
				case FAILED:
					if (!state.hasFaultTreeNodeFailed(root)) {
						return;
					}
					break;
				case OBSERVED:
					if (state instanceof PODFTState && !((PODFTState) state).isNodeFailObserved(root)) {
						return;
					}
					break;
				case UNOBSERVED:
					if (state instanceof PODFTState && ((PODFTState) state).isNodeFailObserved(root)) {
						return;
					}
					break;
				default:
					break;
			}
		}
		
		state.setFailState(true);
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
	 * Gets the static analysis setup
	 * @return the static analysis setup
	 */
	public DFTStaticAnalysis getStaticAnalysis() {
		return staticAnalysis;
	}

	/**
	 * Configugres the propertey whether dont care failing is allowed
	 * @param allowsDontCareFailing set to true to enable dont care failing of states
	 */
	public void setAllowsDontCareFailing(boolean allowsDontCareFailing) {
		this.allowsDontCareFailing = allowsDontCareFailing;
	}
	
	/**
	 * Gets the internal semantics obeject
	 * @return the internal semantics object
	 */
	public DFTSemantics getDftSemantics() {
		return semantics;
	}
}
