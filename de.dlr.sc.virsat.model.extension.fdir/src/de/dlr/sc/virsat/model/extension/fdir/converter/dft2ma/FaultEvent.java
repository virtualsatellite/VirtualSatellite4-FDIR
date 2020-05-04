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

import java.util.Collections;
import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class represents a single fault event (e.g. failure or repair)
 * @author muel_s8
 *
 */

public class FaultEvent implements IDFTEvent {

	private BasicEvent be;
	private boolean isRepair;
	
	private double repairRate;
	private double hotFailRate;
	private double coldFailRate;
	
	/**
	 * The default constructor
	 * @param be the failure mode
	 * @param isRepair true iff this event is a repair event. If false, then this event is a fail event.
	 * @param ftHolder the fault tree
	 */
	public FaultEvent(BasicEvent be, boolean isRepair, FaultTreeHolder ftHolder) {
		this.be = be;
		this.isRepair = isRepair;
		
		this.repairRate = ftHolder.getRepairRate(be);
		this.hotFailRate = ftHolder.getHotFailRate(be);
		this.coldFailRate = ftHolder.getColdFailRate(be);
	}
	
	@Override
	public String toString() {
		String eventType = isRepair ? "R" : "F";
		return eventType + "(" + be.toString() + ")";
	}

	@Override
	public double getRate(DFTState state) {
		boolean isParentNodeActive = state.isNodeActive(be.getFault());
		
		if (isRepair) {
			return repairRate;
		} else {
			return state.getExtraRateFactor(be) * (isParentNodeActive ? hotFailRate : coldFailRate);
		}
	}
	
	@Override
	public void execute(DFTState state, DFTStaticAnalysis staticAnalysis) {
		if (isRepair) {
			if (staticAnalysis.getOrderDependentBasicEvents().contains(be)) {
				state.orderedBes.remove(be);
			} else {
				state.unorderedBes.remove(be);
			}
			state.setFaultTreeNodeFailed(be, false);
		} else {
			if (staticAnalysis.getOrderDependentBasicEvents().contains(be)) {
				state.orderedBes.add(be);
			} else {
				state.unorderedBes.add(be);
			}
			state.setFaultTreeNodeFailed(be, true);
			if (!staticAnalysis.getTransientNodes().contains(be)) {
				state.setFaultTreeNodePermanent(be, true);
			}
		}
	}
	
	@Override
	public boolean canOccur(DFTState state) {
		if (state.isFaultTreeNodePermanent(be)) {
			return false;
		}
		
		boolean hasAlreadyFailed = state.hasFaultTreeNodeFailed(be);
		
		if (isRepair) {
			// Disable repair events while there is a failed FDEP trigger
			List<FaultTreeNode> depTriggers = state.ftHolder.getMapNodeToDEPTriggers().getOrDefault(be, Collections.emptyList());
			for (FaultTreeNode depTrigger : depTriggers) {
				if (state.hasFaultTreeNodeFailed(depTrigger)) {
					return false;
				}
			}
			
			boolean isFailedDueToFDEP = state.getAffectors(be).stream().filter(affector -> affector instanceof FDEP).findAny().isPresent();
			return hasAlreadyFailed && !isFailedDueToFDEP;
		} else {
			boolean isParentNodeActive = state.isNodeActive(state.getFTHolder().getMapBasicEventToFault().get(be));
			return !hasAlreadyFailed && (isParentNodeActive || coldFailRate != 0);
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
