/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the ModelCheckingResult implementation
 * @author muel_s8
 *
 */

public class ModelCheckingResultTest {

	@Test
	public void testLimitPointMetrics() {
		ModelCheckingResult result = new ModelCheckingResult();
		
		result.limitPointMetrics(1);
		
		assertTrue(result.getFailRates().isEmpty());
		assertTrue(result.getAvailability().isEmpty());
		
		result.getFailRates().add(1d);
		result.getAvailability().add(1d);
		
		final int EXPECTED_POINTS = 2;
		result.limitPointMetrics(EXPECTED_POINTS);
		
		assertEquals(EXPECTED_POINTS, result.getFailRates().size());
		assertEquals(EXPECTED_POINTS, result.getAvailability().size());
		
		result.limitPointMetrics(0);
		
		assertTrue(result.getFailRates().isEmpty());
		assertTrue(result.getAvailability().isEmpty());
	}

}
