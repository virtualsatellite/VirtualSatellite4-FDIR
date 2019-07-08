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

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

/**
 * Abstract class for representing the state of a DFT
 * @author muel_s8
 *
 */

public abstract class DFTState extends MarkovState {
	protected boolean isFailState;
	protected RecoveryStrategy recoveryStrategy;
	
	/**
	 * Is this state representing a fail state?
	 * @return true iff this state is a fail state
	 */
	
	public boolean isFailState() {
		return isFailState;
	}
	
	/**
	 * Sets the recovery strategy state
	 * @param recoveryStrategy the recovery strategy state
	 */
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}
	
	/**
	 * Gets the recovery strategy state
	 * @return the recovery strategy state
	 */
	public RecoveryStrategy getRecoveryStrategy() {
		return recoveryStrategy;
	}
	
	/**
	 * Sets whether this DFT state should be marked as a fail state
	 * @param isFailState true iff the dft state is a fail state
	 */
	public void setFailState(boolean isFailState) {
		this.isFailState = isFailState;
	}
	
	@Override
	public String toString() {
		String res = index + " [label=\"";
		
		res += getLabel();
		
		res += "\"";
		
		if (isFailState) {
			res += ", color=\"red\"";
		}
		
		res += "]";
		
		return res;
	}
	
	/**
	 * Gets a label string for this DFTState for the purpose of printing
	 * @return a label for this state
	 */
	
	public abstract String getLabel();
}
