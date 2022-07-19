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

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class ImmediateFaultEvent extends FaultEvent {
	
	private boolean isNegative;
	
	public ImmediateFaultEvent(BasicEvent be, boolean isRepair, FaultTreeHolder ftHolder, boolean isNegative) {
		super(be, isRepair, ftHolder);
		this.isNegative = isNegative;
	}
	
	public boolean isNegative() {
		return isNegative;
	}
	
	@Override
	public double getRate(DFTState state) {
		double rate = super.getRate(state);
		if (isNegative) {
			rate = 1 - rate;
		}
		return rate;
	}
	
	@Override
	public String toString() {
		String occurenceType = isNegative ? "Not-" : "";
		return occurenceType + super.toString();
	}
	@Override
	public boolean isImmediate() {
		return true;
	}
	
	@Override
	public void execute(DFTState state) {
		if (isNegative) {
			// If this is a non-occurence we must mark the node as permanent
			// to ensure that the immediate event will not trigger again
			for (FaultTreeNode node : getNodes()) {
				state.setFaultTreeNodePermanent(node, true);
			}
		} else {
			super.execute(state);
		}
	}
}
