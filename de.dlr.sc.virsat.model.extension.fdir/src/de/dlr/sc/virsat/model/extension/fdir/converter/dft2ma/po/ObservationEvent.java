/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po;

import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;

/**
 * This class represents a single observation event (e.g. observation of failure or repair)
 * @author muel_s8
 *
 */

public class ObservationEvent implements IDFTEvent {
	
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
		List<OBSERVER> observers = state.getFTHolder().getMapNodeToObservers().get(node);
		for (OBSERVER observer : observers) {
			if (!state.hasFaultTreeNodeFailed(observer)) {
				rate += observer.getObservationRate();
			}
		}
		return rate;
	}

	@Override
	public void execute(DFTState state, Set<BasicEvent> orderDependentBasicEvents,
			Set<FaultTreeNode> transientNodes) {
		PODFTState poState = (PODFTState) state;
		poState.setNodeFailObserved(node, !isRepair);
	}
	
	@Override
	public FaultTreeNode getNode() {
		return node;
	}

	@Override
	public boolean canOccur(DFTState state) {
		PODFTState poState = (PODFTState) state;
		if (isRepair) {
			if (state.hasFaultTreeNodeFailed(node) || !poState.isNodeFailObserved(node))  {
				return false;
			}
		} else {
			if (!state.hasFaultTreeNodeFailed(node) || poState.isNodeFailObserved(node))  {
				return false;
			}
		}
		
		List<OBSERVER> observers = state.getFTHolder().getMapNodeToObservers().get(node);
		for (OBSERVER observer : observers) {
			if (!state.hasFaultTreeNodeFailed(observer)) {
				return true;
			}
		}
		return false;
	}
}