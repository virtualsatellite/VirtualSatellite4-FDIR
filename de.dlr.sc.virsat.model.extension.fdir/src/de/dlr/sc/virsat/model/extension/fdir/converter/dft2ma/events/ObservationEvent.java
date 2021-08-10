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

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;

/**
 * This class represents a single observation event (e.g. observation of failure or repair)
 * @author muel_s8
 *
 */

public class ObservationEvent implements IDFTEvent, IRepairableEvent {
	
	private FaultTreeNode node;
	private boolean isRepair;
	
	/**
	 * The default constructor
	 * @param node the observed node
	 * @param isRepair true iff this event is a repair event. If false, then this event is a fail event.
	 */
	public ObservationEvent(FaultTreeNode node, boolean isRepair) {
		this.node = node;
		this.isRepair = isRepair;
	}
	
	@Override
	public String toString() {
		String eventType = isRepair ? "R" : "F";
		return "O(" + eventType + "(" + node.toString() + "))";
	}

	@Override
	public double getRate(DFTState state) {
		double rate = 0;
		List<FaultTreeNode> observers = state.getFTHolder().getNodes(node, EdgeType.MONITOR);
		for (FaultTreeNode observer : observers) {
			if (!state.hasFaultTreeNodeFailed(observer)) {
				rate += ((MONITOR) observer).getObservationRateBean().getValueToBaseUnit();
			}
		}
		return rate;
	}

	@Override
	public void execute(DFTState state) {
		if (state instanceof PODFTState) {
			PODFTState poState = (PODFTState) state;
			poState.setNodeFailObserved(node, !isRepair);
		}
	}
	
	@Override
	public Collection<FaultTreeNode> getNodes() {
		return Collections.singleton(node);
	}
	
	@Override
	public boolean isRepair() {
		return isRepair;
	}

	@Override
	public boolean canOccur(DFTState state) {
		if (state instanceof PODFTState) {
			PODFTState poState = (PODFTState) state;
			if (isRepair) {
				if (state.hasFaultTreeNodeFailed(node) || !poState.isNodeFailObserved(node))  {
					return false;
				}
			} else {
				if (!state.hasFaultTreeNodeFailed(node)) {
					return false;
				}
			}
			
			if (node instanceof MONITOR) {
				// An observer can always observe itself
				return true;
			}
			
			return poState.existsObserver(node, true, false);
		} 
		
		return false;
	}

	@Override
	public boolean isImmediate() {
		return false;
	}
}
