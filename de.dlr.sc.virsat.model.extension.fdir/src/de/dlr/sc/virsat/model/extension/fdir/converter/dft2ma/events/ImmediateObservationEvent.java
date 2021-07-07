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

import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.util.EdgeType;

/**
 * This class represents an immediate single observation event (e.g. observation of failure or repair)
 * @author khan_ax
 *
 */

public class ImmediateObservationEvent extends ObservationEvent {
	
	public ImmediateObservationEvent(FaultTreeNode node, boolean isRepair) {
		super(node, isRepair);
	}

	@Override
	public double getRate(DFTState state) {
		double rate = 0;
		List<FaultTreeNode> observers = state.getFTHolder().getNodes(super.getNode(), EdgeType.MONITOR);
		for (FaultTreeNode observer : observers) {
			if (!state.hasFaultTreeNodeFailed(observer) && ((MONITOR) observer).getObservationRateBean().getValueToBaseUnit() == 0) {
				return 1;
			}
		}
		return rate;
	}
	
	@Override
	public boolean isImmediate() {
		return true;
	}
}
