/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * @author muel_s8
 *
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
		CoreAllTest.class,
		de.dlr.sc.virsat.fdir.galileo.tests.AllTests.class,
		de.dlr.sc.virsat.fdir.core.test.AllTests.class,
		de.dlr.sc.virsat.fdir.storm.test.AllTests.class,
		de.dlr.sc.virsat.model.extension.fdir.test.AllTests.class,
		de.dlr.sc.virsat.model.extension.fdir.test.AllTestsGen.class,
	})

/**
 * Test Class
 */
public class ProjectAllTests {   
}