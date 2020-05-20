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

import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
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

public class FaultEvent implements IDFTEvent {

	private BasicEvent be;
	private BasicEventHolder beHolder;
	
	private boolean isRepair;
	private boolean isOrderDependent;
	private boolean isTransient;
	
	/**
	 * The default constructor
	 * @param be the failure mode
	 * @param isRepair true iff this event is a repair event. If false, then this event is a fail event.
	 * @param ftHolder the fault tree
	 */
	public FaultEvent(BasicEvent be, boolean isRepair, FaultTreeHolder ftHolder, DFTStaticAnalysis staticAnalysis) {
		this.be = be;
		this.isRepair = isRepair;
		this.beHolder = ftHolder.getBasicEventHolder(be);
		
		isOrderDependent = staticAnalysis.getOrderDependentBasicEvents().contains(be);
		isTransient = staticAnalysis.getTransientNodes().contains(be);
	}
	
	@Override
	public String toString() {
		String eventType = isRepair ? "R" : "F";
		return eventType + "(" + be.toString() + ")";
	}

	@Override
	public double getRate(DFTState state) {
		if (isRepair) {
			return beHolder.getRepairRate();
		} else {
			boolean isParentNodeActive = state.isNodeActive(beHolder.getFault());
			double rate = isParentNodeActive ? beHolder.getHotFailureRate() : beHolder.getColdFailureRate();
			return getExtraRateFactor(state) * rate;
		}
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
		state.executeBasicEvent(be, isRepair, isOrderDependent, isTransient);
	}
	
	@Override
	public boolean canOccur(DFTState state) {
		if (state.isFaultTreeNodePermanent(be)) {
			return false;
		}
		
		boolean hasAlreadyFailed = state.hasFaultTreeNodeFailed(be);
		
		if (isRepair) {
			// Disable repair events while there is a failed FDEP trigger
			List<FaultTreeNode> depTriggers = state.getFTHolder().getNodes(be, EdgeType.DEP);
			for (FaultTreeNode depTrigger : depTriggers) {
				if (state.hasFaultTreeNodeFailed(depTrigger)) {
					return false;
				}
			}
			
			boolean isFailedDueToFDEP = state.getAffectors(be).stream().filter(affector -> affector instanceof FDEP).findAny().isPresent();
			return hasAlreadyFailed && !isFailedDueToFDEP;
		} else {
			boolean isParentNodeActive = state.isNodeActive(beHolder.getFault());
			return !hasAlreadyFailed && (isParentNodeActive || beHolder.getColdFailureRate() != 0);
		} 
	}

	@Override
	public FaultTreeNode getNode() {
		return be;
	}
	
	/**
	 * Returns true iff this is a repair event
	 * @return true iff this is a repair event
	 */
	public boolean isRepair() {
		return isRepair;
	}
}
