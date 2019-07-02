/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.dlr.sc.virsat.fdir.storm.docker.DockerHelperTest;
import de.dlr.sc.virsat.fdir.storm.files.ExplicitDRNFileWriterTest;
import de.dlr.sc.virsat.fdir.storm.files.ExplicitPropFileWriterTest;
import de.dlr.sc.virsat.fdir.storm.files.InstanceFileGeneratorTest;
import de.dlr.sc.virsat.fdir.storm.runner.StormDFTTest;
import de.dlr.sc.virsat.fdir.storm.runner.StormModelCheckerTest;
import de.dlr.sc.virsat.fdir.storm.runner.StormTest;
import junit.framework.JUnit4TestAdapter;

/**
 * 
 * @author muel_s8
 *
 */
@RunWith(Suite.class)

@SuiteClasses({ 
		DockerHelperTest.class,
		StormDFTTest.class,
		InstanceFileGeneratorTest.class,
		ExplicitDRNFileWriterTest.class,
		ExplicitPropFileWriterTest.class,
		StormModelCheckerTest.class,
		StormTest.class
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