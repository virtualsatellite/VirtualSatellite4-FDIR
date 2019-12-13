/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.runner;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the StormRunnerFactory class.
 * @author muel_s8
 *
 */

public class StormRunnerFactoryTest {

	@Test
	public void testCreate() {
		StormRunnerFactory<Double> stormRunnerFactory = new StormRunnerFactory<>();
		IStormRunner<Double> stormRunner = stormRunnerFactory.create(new StormDFT(), StormExecutionEnvironment.Local);
		assertTrue(stormRunner instanceof StormRunner);
	}

}
