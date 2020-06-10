/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;

public class MetricsDeriverTest {

	private static final double DELTA = 10;
	private static final double EPS = 0.00001;
	private MetricsDeriver deriver;
	
	@Before
	public void setUp() {
		this.deriver = new MetricsDeriver();
	}

	@Test
	public void testDeriveMTTF() {
		Map<FailLabelProvider, ModelCheckingResult> baseResults = new HashMap<>();
		ModelCheckingResult reliabilityResult = new ModelCheckingResult();
		// CHECKSTYLE:OFF
		reliabilityResult.getFailRates().addAll(Arrays.asList(0d, 0.25, 0.5, 1d));
		// CHECKSTYLE:ON
		baseResults.put(FailLabelProvider.SINGLETON_FAILED, reliabilityResult);
		
		ModelCheckingResult derivedResult = deriver.derive(baseResults, DELTA, MeanTimeToFailure.MTTF);
		
		final double EXPECTED_MTTF = 17.75;
		assertEquals(EXPECTED_MTTF, derivedResult.getMeanTimeToFailure(), EPS);
	}
	
	@Test
	public void testDeriveDetectability() {
		Map<FailLabelProvider, ModelCheckingResult> baseResults = new HashMap<>();
		ModelCheckingResult availabilityResult = new ModelCheckingResult();
		ModelCheckingResult observedAvailabilityResult = new ModelCheckingResult();
		// CHECKSTYLE:OFF
		availabilityResult.getAvailability().addAll(Arrays.asList(1d, 0.5, 0.4, 0.75, 0d));
		observedAvailabilityResult.getAvailability().addAll(Arrays.asList(1d, 1d, 0.6, 0.8125, 0d));
		// CHECKSTYLE:ON
		baseResults.put(FailLabelProvider.SINGLETON_FAILED, availabilityResult);
		baseResults.put(FailLabelProvider.SINGLETON_OBSERVED, observedAvailabilityResult);
		
		ModelCheckingResult derivedResult = deriver.derive(baseResults, DELTA, Detectability.UNIT_DETECTABILITY);
		
		final List<Double> EXPECTED_DETECTABILITIES = Arrays.asList(1d, 0d, 0.6666666666666667, 0.75, 1d);
		assertEquals(EXPECTED_DETECTABILITIES, derivedResult.getDetectabiity());
	}
	
	@Test
	public void testDeriveMeanTimeToDetection() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDeriveSteadyStateDetectability() {
		fail("Not yet implemented");
	}
}
