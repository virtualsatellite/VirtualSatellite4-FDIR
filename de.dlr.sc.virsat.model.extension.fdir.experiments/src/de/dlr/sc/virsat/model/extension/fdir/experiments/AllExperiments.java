/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.dlr.sc.virsat.model.extension.fdir.experiments.aiaa.AIAA2017Experiments;
import de.dlr.sc.virsat.model.extension.fdir.experiments.aiaa.AIAA2018Experiments;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ftscs.FTSCS2018Experiments;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ftscs.SCP2019Experiments;
import junit.framework.JUnit4TestAdapter;

/**
 * 
 * @author muel_s8
 *
 */
@RunWith(Suite.class)

@SuiteClasses({ 
		AIAA2017Experiments.class,
		AIAA2018Experiments.class,
		FTSCS2018Experiments.class,
		SCP2019Experiments.class
		})

public class AllExperiments {

	/**
	 * Constructor for Test Suite
	 */
	private AllExperiments() {
	}

	/**
	 * entry point for test suite
	 * 
	 * @return the test suite
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllExperiments.class);
	}
}