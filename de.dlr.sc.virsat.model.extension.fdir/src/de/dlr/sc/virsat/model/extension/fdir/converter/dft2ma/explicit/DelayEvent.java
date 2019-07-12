/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit;

import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Delay event by a DELAY node
 * @author muel_s8
 *
 */
public class DelayEvent implements IDFTEvent {

	private double delay;
	private DELAY delayNode;
	
	/**
	 * Default constructor
	 * @param delayNode the delay node
	 */
	public DelayEvent(DELAY delayNode) {
		this.delayNode = delayNode;
		delay = delayNode.getTimeBean().getValueToBaseUnit();
	}
	
	@Override
	public double getRate(ExplicitDFTState state) {
		return 1 / delay;
	}

	@Override
	public boolean canOccur(ExplicitDFTState state) {
		return state.isFaultTreeNodeFailing(delayNode) && !state.hasFaultTreeNodeFailed(delayNode);
	}

	@Override
	public void execute(ExplicitDFTState state, Set<BasicEvent> orderDependentBasicEvents,
			Set<FaultTreeNode> transientNodes) {
		state.setFaultTreeNodeFailed(delayNode, true);
		state.setFaultTreeNodeFailing(delayNode, false);
	}

	@Override
	public FaultTreeNode getNode() {
		return delayNode;
	}
	
	@Override
	public String toString() {
		return "F(" + delayNode.getName() + ")";
	}
}
