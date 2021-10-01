/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
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
 * This class represents an immediate observation event (e.g. observation of failure or repair)
 * @author khan_ax
 *
 */

public class ImmediateObservationEvent extends ObservationEvent {
	
	private Collection<FaultTreeNode> nodes;
	
	public ImmediateObservationEvent(FaultTreeNode node, boolean isRepair) {
		super(node, isRepair);
		nodes = Collections.singleton(node);
	}
	
	public ImmediateObservationEvent(Collection<FaultTreeNode> nodes, boolean isRepair) {
		super(null, isRepair);
		this.nodes = nodes;
	}

	@Override
	public double getRate(DFTState state) {
		double rate = 0;
		for (FaultTreeNode node : getNodes()) {
			if (node instanceof MONITOR) {
				return 1;
			}
			List<FaultTreeNode> observers = state.getFTHolder().getNodes(node, EdgeType.MONITOR);
			for (FaultTreeNode observer : observers) {
				if (!state.hasFaultTreeNodeFailed(observer) && ((MONITOR) observer).getObservationRateBean().getValueToBaseUnit() == 0) {
					return 1;
				}
			}
		}
		return rate;
	}
	
	@Override
	public boolean isImmediate() {
		return true;
	}
	
	@Override
	public Collection<FaultTreeNode> getNodes() {
		return nodes;
	}
	
	@Override
	public String toString() {
		String eventType = isRepair() ? "R" : "F";
		return "O(" + eventType + "(" + nodes.toString() + "))";
	}
	
	@Override
	public void execute(DFTState state) {
		if (state instanceof PODFTState) {
			PODFTState poState = (PODFTState) state;
			for (FaultTreeNode node : nodes) {
				poState.setNodeFailObserved(node, !isRepair());
			}
		}
	}
}
