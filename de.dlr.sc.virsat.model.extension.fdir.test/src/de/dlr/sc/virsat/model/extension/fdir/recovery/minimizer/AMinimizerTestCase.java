/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * Defines test cases for recovery automaton minimizers
 * @author muel_s8
 *
 */

public abstract class AMinimizerTestCase extends ATestCase {
	protected RecoveryAutomatonHelper recoveryAutomatonHelper; 
	protected FaultTreeHelper faultTreeHelper;


	@Override
	public void set() throws Exception {
		super.set();
		recoveryAutomatonHelper = new RecoveryAutomatonHelper(concept);
		faultTreeHelper = new FaultTreeHelper(concept);
	}
	
	/**
	 * Creates the recovery automaton minimizer to test
	 * @return the concrete instance under test
	 */
	public abstract ARecoveryAutomatonMinimizer createMinimizer();

}
