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
		Map<FailLabelProvider, ModelCheckingResult> baseResults = new HashMap<>();
		ModelCheckingResult observedResult = new ModelCheckingResult();
		ModelCheckingResult unobservedResult = new ModelCheckingResult();
		// CHECKSTYLE:OFF
		observedResult.setMeanTimeToFailure(10);
		unobservedResult.setMeanTimeToFailure(5);
		// CHECKSTYLE:ON
		baseResults.put(FailLabelProvider.SINGLETON_FAILED, unobservedResult);
		baseResults.put(FailLabelProvider.SINGLETON_OBSERVED, observedResult);
		
		ModelCheckingResult derivedResult = deriver.derive(baseResults, DELTA, MeanTimeToDetection.MTTD);
		
		final double EXPECTED_MTTD = 5;
		assertEquals(EXPECTED_MTTD, derivedResult.getMeanTimeToDetection(), EPS);
		
		// Check the case that the failure is never observed
		observedResult.setMeanTimeToFailure(Double.POSITIVE_INFINITY);
		
		derivedResult = deriver.derive(baseResults, DELTA, MeanTimeToDetection.MTTD);
		
		final double EXPECTED_MTTD_NO_OBSERVATION = Double.POSITIVE_INFINITY;
		assertEquals(EXPECTED_MTTD_NO_OBSERVATION, derivedResult.getMeanTimeToDetection(), EPS);
		
		// CHeck the case that the failure never occurs
		unobservedResult.setMeanTimeToFailure(Double.POSITIVE_INFINITY);
		
		derivedResult = deriver.derive(baseResults, DELTA, MeanTimeToDetection.MTTD);
		
		final double EXPECTED_MTTD_NO_FAILURE = 0;
		assertEquals(EXPECTED_MTTD_NO_FAILURE, derivedResult.getMeanTimeToDetection(), EPS);
	}
	
	@Test
	public void testDeriveSteadyStateDetectability() {
		Map<FailLabelProvider, ModelCheckingResult> baseResults = new HashMap<>();
		ModelCheckingResult observedResult = new ModelCheckingResult();
		ModelCheckingResult unobservedResult = new ModelCheckingResult();
		// CHECKSTYLE:OFF
		observedResult.setSteadyStateAvailability(0.8);
		unobservedResult.setSteadyStateAvailability(0.4);
		// CHECKSTYLE:ON
		baseResults.put(FailLabelProvider.SINGLETON_FAILED, unobservedResult);
		baseResults.put(FailLabelProvider.SINGLETON_OBSERVED, observedResult);
		
		ModelCheckingResult derivedResult = deriver.derive(baseResults, DELTA, SteadyStateDetectability.SSD);
		
		final double EXPECTED_SSD = 0.33333333333333326;
		assertEquals(EXPECTED_SSD, derivedResult.getSteadyStateDetectability(), EPS);
		
		// Check the case that the failure is never observed
		observedResult.setSteadyStateAvailability(1);
		
		derivedResult = deriver.derive(baseResults, DELTA, SteadyStateDetectability.SSD);
		
		final double EXPECTED_SSD_NO_OBSERVATION = 0;
		assertEquals(EXPECTED_SSD_NO_OBSERVATION, derivedResult.getSteadyStateDetectability(), EPS);
		
		// Check the case that the failure never occurs
		unobservedResult.setSteadyStateAvailability(1);
		
		derivedResult = deriver.derive(baseResults, DELTA, SteadyStateDetectability.SSD);
		
		final double EXPECTED_SSD_NO_FAILURE = 1;
		assertEquals(EXPECTED_SSD_NO_FAILURE, derivedResult.getSteadyStateDetectability(), EPS);
	}
}
