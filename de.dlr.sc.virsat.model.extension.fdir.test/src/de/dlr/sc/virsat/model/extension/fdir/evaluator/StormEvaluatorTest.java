/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.storm.runner.IStormProgram;
import de.dlr.sc.virsat.fdir.storm.runner.IStormRunner;
import de.dlr.sc.virsat.fdir.storm.runner.StormExecutionEnvironment;
import de.dlr.sc.virsat.fdir.storm.runner.StormRunnerFactory;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the StormEvaluator implementation
 * @author muel_s8
 *
 */
public class StormEvaluatorTest extends ATestCase {

	@Test
	public void testEvaluateFaultTree() {
		StormDFTEvaluator stormEvaluator = new StormDFTEvaluator(1);
		stormEvaluator.setStormRunnerFactory(new StormRunnerFactory<Double>() {			
			@Override
			public IStormRunner<Double> create(IStormProgram<Double> storm, StormExecutionEnvironment executionEnvironment) {
				return new IStormRunner<Double>() {
					@Override
					public List<Double> run() throws IOException, URISyntaxException {
						return Arrays.asList(1d, 0d);
					}
				};
			}
		});
		
		Fault fault = new Fault(concept);
		ModelCheckingResult result = stormEvaluator.evaluateFaultTree(fault, MeanTimeToFailure.MTTF, new Reliability(1));
		
		final double EPS = 0.001;
		assertEquals(1, result.getMeanTimeToFailure(), EPS);
		assertEquals(1, result.getFailRates().size());
		assertEquals(0, result.getFailRates().get(0), EPS);
	}
	
}
