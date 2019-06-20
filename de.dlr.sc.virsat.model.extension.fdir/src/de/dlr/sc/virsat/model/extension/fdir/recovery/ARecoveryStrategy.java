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

import java.util.List;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;

/**
 * Abstract base class for recovery strategies.
 * @author muel_s8
 *
 */

public abstract class ARecoveryStrategy implements IRecoveryStrategy {

	protected List<RecoveryAction> recoveryAction;
	
	@Override
	public List<RecoveryAction> getRecoveryActions() {
		return recoveryAction;
	}

}
