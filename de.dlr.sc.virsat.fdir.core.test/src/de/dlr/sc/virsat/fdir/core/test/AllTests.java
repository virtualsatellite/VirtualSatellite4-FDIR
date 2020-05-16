/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomatonBuilderTest;
import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomatonTest;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.MarkovModelCheckerTest;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResultTest;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.MarkovSchedulerTest;
import junit.framework.JUnit4TestAdapter;

/**
 * Test suite
 * @author muel_s8
 *
 */
@RunWith(Suite.class)

@SuiteClasses({ 
	MarkovAutomatonTest.class,
	MarkovModelCheckerTest.class,
	MarkovSchedulerTest.class,
	ModelCheckingResultTest.class,
	MarkovAutomatonBuilderTest.class
	})

public class AllTests {

	/**
	 * Constructor for Test Suite
	 */
	private AllTests() {
	}

	/**
	 * entry point for test suite
	 * 
	 * @return the test suite
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}
}
