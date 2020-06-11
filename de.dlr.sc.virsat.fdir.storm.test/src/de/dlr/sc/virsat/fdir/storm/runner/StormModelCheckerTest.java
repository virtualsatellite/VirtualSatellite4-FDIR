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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingQuery;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;


/**
 * This class tests the StormModelChecker class
 * @author yoge_re
 *
 */
public class StormModelCheckerTest {
	
	/**
	 * Creates a StormModelChecker with a mocked runner, producing always the passed results
	 * @param mockResults the mock results
	 * @return a storm model checker not depending on a storm installation
	 */
	private StormModelChecker createMockStormModelChecker(List<Double> mockResults) {
		return new StormModelChecker(1, StormExecutionEnvironment.Docker) {
			protected StormRunner<Double> createStormRunner(Storm storm) {
				return new StormRunner<Double>(storm, StormExecutionEnvironment.Docker) {
					public List<Double> run() throws IOException, URISyntaxException {
						return mockResults;
					};
				};
			}
		};
	}
	

	@Test
	public void testCheckModel() {
		final List<Double> EXPECTED_FAIL_RATES = new ArrayList<>();
		final double FAIL_RATE = 0.9975212478;
		EXPECTED_FAIL_RATES.add((double) 0);
		EXPECTED_FAIL_RATES.add(FAIL_RATE);
		final double EXPECTED_MTTF = 0.166666666;
		
		final List<Double> MOCK_RESULTS = new ArrayList<>();
		MOCK_RESULTS.add(FAIL_RATE);
		MOCK_RESULTS.add(EXPECTED_MTTF);
		StormModelChecker modelChecker = createMockStormModelChecker(MOCK_RESULTS);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		final double RATE = 6;
		ma.addState(state1);
		ma.addState(state2);
		state2.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		ma.addMarkovianTransition("a", state1, state2, RATE);
		ma.addNondeterministicTransition("b", state2, state1); 
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, null, Reliability.UNIT_RELIABILITY, MeanTimeToFailure.MTTF), null);
		
		final double DELTA = 0.000000001;
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), DELTA);
		assertEquals(EXPECTED_FAIL_RATES, result.getFailRates());
	}
	
	@Test
	public void testCheckModel2() {
		final List<Double> EXPECTED_FAIL_RATES = new ArrayList<>();
		final double FAIL_RATE = 0.9975212478;
		EXPECTED_FAIL_RATES.add((double) 0);
		EXPECTED_FAIL_RATES.add(FAIL_RATE);
		final double EXPECTED_MTTF = 0.166666666;
		
		final List<Double> MOCK_RESULTS = new ArrayList<>();
		MOCK_RESULTS.add(FAIL_RATE);
		MOCK_RESULTS.add(EXPECTED_MTTF);
		StormModelChecker modelChecker = createMockStormModelChecker(MOCK_RESULTS);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		final double RATE = 6;
		ma.addState(state1);
		ma.addState(state2);
		state2.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		ma.addMarkovianTransition("a", state1, state2, RATE);
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, null, Reliability.UNIT_RELIABILITY, MeanTimeToFailure.MTTF), null);
		
		final double DELTA = 0.00000001;
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), DELTA);
		assertEquals(EXPECTED_FAIL_RATES, result.getFailRates());
	}
	
	@Test
	public void testCheckModelNonDet() {
		final List<Double> EXPECTED_FAIL_RATES = new ArrayList<>();
		final double FAIL_RATE = 0.8646647168;
		EXPECTED_FAIL_RATES.add((double) 0);
		EXPECTED_FAIL_RATES.add(FAIL_RATE);
		final double EXPECTED_MTTF = 0.5;
		
		final List<Double> MOCK_RESULTS = new ArrayList<>();
		MOCK_RESULTS.add(FAIL_RATE);
		MOCK_RESULTS.add(EXPECTED_MTTF);
		StormModelChecker modelChecker = createMockStormModelChecker(MOCK_RESULTS);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		MarkovState state3 = new MarkovState();
		MarkovState state4 = new MarkovState();
		ma.addState(state1);
		ma.addState(state2);
		ma.addState(state3);
		ma.addState(state4);
		state4.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		final double RATE1 = 0.6;
		final double RATE2 = 1.4;
		ma.addMarkovianTransition("a", state1, state2, RATE1);
		ma.addMarkovianTransition("b", state1, state3, RATE2);
		ma.addNondeterministicTransition("c", state2, state3);
		ma.addNondeterministicTransition("d", state2, state4);
		ma.addNondeterministicTransition("e", state3, state4);
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, null, Reliability.UNIT_RELIABILITY, MeanTimeToFailure.MTTF), null);
		
		final double DELTA = 0.00000001;
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), DELTA);
		assertEquals(EXPECTED_FAIL_RATES, result.getFailRates());
	}
	
	@Test
	public void testSteadyStateAvailability() {
		final double EXPECTED_STEADY_STATE_AVAILABILITY = 0.5;
		
		final List<Double> MOCK_RESULTS = new ArrayList<>();
		MOCK_RESULTS.add(EXPECTED_STEADY_STATE_AVAILABILITY);
		StormModelChecker modelChecker = createMockStormModelChecker(MOCK_RESULTS);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		ma.addState(state1);
		ma.addState(state2);
		state2.getMapFailLabelToProb().put(FailLabel.FAILED, 1d);
		
		final double RATE1 = 1.2;
		final double RATE2 = 1.2;
		ma.addMarkovianTransition("a", state1, state2, RATE1);
		ma.addMarkovianTransition("b", state2, state1, RATE2);
		
		ModelCheckingResult result = modelChecker.checkModel(new ModelCheckingQuery<>(ma, null, SteadyStateAvailability.SSA), null);
		
		final double DELTA = 0.01;
		assertEquals(EXPECTED_STEADY_STATE_AVAILABILITY, result.getSteadyStateAvailability(), DELTA);
	}
}