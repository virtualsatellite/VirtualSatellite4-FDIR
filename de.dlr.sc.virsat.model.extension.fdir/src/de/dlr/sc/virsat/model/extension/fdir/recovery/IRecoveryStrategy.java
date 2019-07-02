/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery;

import java.util.Collection;
import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;

/**
 * Interface for recovery strategies.
 * @author muel_s8
 *
 */

public interface IRecoveryStrategy {
	
	/**
	 * React to the occurrence of a set of faults
	 * @param faults the occurred faults
	 * @return the recovery strategy after reading the fault
	 */
	IRecoveryStrategy onFaultsOccured(Collection<FaultTreeNode> faults);
	
	/**
	 * Get the currently recommended recovery actions.
	 * @return A list of recommened recovery actions.
	 */
	List<RecoveryAction> getRecoveryActions();
}
