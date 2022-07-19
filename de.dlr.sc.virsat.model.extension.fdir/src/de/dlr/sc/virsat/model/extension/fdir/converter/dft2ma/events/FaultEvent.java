/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.util.BasicEventHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class represents a single fault event (e.g. failure or repair)
 * @author muel_s8
 *
 */

public class FaultEvent implements IDFTEvent, IRepairableEvent {

	private BasicEvent be;
	private BasicEventHolder beHolder;
	
	private boolean isRepair;
	private boolean isTransient;
	
	/**
	 * The default constructor
	 * @param be the failure mode
	 * @param isRepair true iff this event is a repair event. If false, then this event is a fail event.
	 * @param ftHolder the fault tree
	 */
	public FaultEvent(BasicEvent be, boolean isRepair, FaultTreeHolder ftHolder) {
		this.be = be;
		this.isRepair = isRepair;
		this.beHolder = ftHolder.getBasicEventHolder(be);
		
		isTransient = ftHolder.getStaticAnalysis().getTransientNodes().contains(be);
	}
	
	@Override
	public String toString() {
		String eventType = isRepair ? "R" : "F";
		return eventType + "(" + be.toString() + ")";
	}

	@Override
	public double getRate(DFTState state) {
		if (isRepair) {
			return getRepairRate(state);
		} else {
			return getFailRate(state);
		}
	}

	/**
	 * Gets the occurrence rate for repair
	 * @param state the current state
	 * @return the rate for repairing the basic event
	 */
	private double getRepairRate(DFTState state) {
		double maxRepairRate = 0;
		for (Entry<List<FaultTreeNode>, Double> repairAction : beHolder.getRepairRates().entrySet()) {
			if (canRepairActionOccur(state, repairAction)) {
				maxRepairRate = Math.max(maxRepairRate, repairAction.getValue());
			}
		}
		return maxRepairRate;
	}

	/**
	 * Gets the occurrence rate for failing
	 * @param state the current state
	 * @return the rate for failing the basic event
	 */
	private double getFailRate(DFTState state) {
		boolean isParentNodeActive = state.isNodeActive(beHolder.getFault());
		double baseRate = isParentNodeActive ? beHolder.getHotFailureRate() : beHolder.getColdFailureRate();
		double rate = getExtraRateFactor(state) * baseRate;
		return rate;
	}
	
	/**
	 * Gets the extra fail rate factor for a given basic event
	 * @param state the state
	 * @param be the basic event
	 * @return the extra fail rate factor
	 */
	private double getExtraRateFactor(DFTState state) {
		Set<FaultTreeNode> affectors = state.getAffectors(be);
		double extraRateFactor = 1;
		for (FaultTreeNode affector : affectors) {
			if (affector instanceof RDEP) {
				RDEP rdep = (RDEP) affector;
				extraRateFactor += rdep.getRateChangeBean().getValueToBaseUnit() - 1;
			}
		}
		return extraRateFactor;
	}
	
	@Override
	public void execute(DFTState state) {
		state.executeBasicEvent(be, isRepair, isTransient);
	}
	
	@Override
	public boolean canOccur(DFTState state) {
		if (state.isFaultTreeNodePermanent(be)) {
			return false;
		}
		
		boolean hasAlreadyFailed = state.hasFaultTreeNodeFailed(be);
		
		if (isRepair) {
			return hasAlreadyFailed && canRepairOccur(state);
		} else {
			return !hasAlreadyFailed && canFailureOccur(state);
		} 
	}

	/**
	 * Verifies if further conditions, besides the basic event needing to be in a opertaional state, that are required for
	 * the fault event to occur, are met.
	 * @param state the current state
	 * @return true iff the basic event can fail
	 */
	private boolean canFailureOccur(DFTState state) {
		boolean isParentNodeActive = state.isNodeActive(beHolder.getFault());
		return isParentNodeActive || beHolder.getColdFailureRate() != 0;
	}

	/**
	 * Verifies if further conditions, besides the basic event needing to be in a failed state, that are required for
	 * the repair event to occur, are met.
	 * @param state the current state
	 * @return true iff the repair event can occur
	 */
	private boolean canRepairOccur(DFTState state) {
		// Disable repair events while there is a failed FDEP trigger
		List<FaultTreeNode> depTriggers = state.getFTHolder().getNodes(be, EdgeType.DEP);
		for (FaultTreeNode depTrigger : depTriggers) {
			if (depTrigger instanceof FDEP && state.hasFaultTreeNodeFailed(depTrigger)) {
				return false;
			}
		}
		
		for (Entry<List<FaultTreeNode>, Double> repairAction : beHolder.getRepairRates().entrySet()) {
			if (canRepairActionOccur(state, repairAction)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Checks if a given repair action can be executed in the current state
	 * @param state the current state
	 * @param repairAction the repair action to check
	 */
	private boolean canRepairActionOccur(DFTState state, Entry<List<FaultTreeNode>, Double> repairAction) {
		double repairRate = repairAction.getValue();
		if (!MarkovAutomaton.isRateDefined(repairRate)) {
			return false;
		}
		
		List<FaultTreeNode> requiredObservations = repairAction.getKey();
		return areAllNodesObserved(state, requiredObservations);
	}

	/**
	 * Checks if all passed nodes have been observed in the current state
	 * @param state the current states
	 * @param nodes the nodes
	 * @return true iff all the passed nodes are being observed
	 */
	private boolean areAllNodesObserved(DFTState state, List<FaultTreeNode> nodes) {
		if (state instanceof PODFTState) {
			PODFTState poState = (PODFTState) state;
			for (FaultTreeNode node : nodes) {
				if (!poState.isNodeFailObserved(node)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Collection<FaultTreeNode> getNodes() {
		return Collections.singleton(be);
	}
	

	@Override
	public boolean isRepair() {
		return isRepair;
	}

	@Override
	public boolean isImmediate() {
		return false;
	}
}
