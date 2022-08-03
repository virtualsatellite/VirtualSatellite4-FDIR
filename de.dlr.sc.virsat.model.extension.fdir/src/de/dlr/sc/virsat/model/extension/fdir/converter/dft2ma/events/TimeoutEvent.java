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

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

/**
 * Event modeling passage of time for timed recovery transitions
 * @author muel_s8
 *
 */
public class TimeoutEvent implements IDFTEvent {

	private double time;
	private State raState;
	
	/**
	 * Default constructor
	 * @param time the time
	 * @param raState the raState required for the event to be active
	 */
	public TimeoutEvent(double time, State raState) {
		this.time = time;
		this.raState = raState;
	}
	
	@Override
	public double getRate(DFTState state) {
		return 1 / time;
	}

	@Override
	public boolean canOccur(DFTState state) {
		RecoveryStrategy raStrategy = (RecoveryStrategy) state.getRecoveryStrategy();
		return raStrategy.getCurrentState().equals(raState);
	}

	@Override
	public void execute(DFTState state) {
		state.setRecoveryStrategy(state.getRecoveryStrategy().onTimeout());
	}

	@Override
	public Collection<FaultTreeNode> getNodes() {
		return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(time) + "s";
	}

	@Override
	public boolean isImmediate() {
		return false;
	}

}
