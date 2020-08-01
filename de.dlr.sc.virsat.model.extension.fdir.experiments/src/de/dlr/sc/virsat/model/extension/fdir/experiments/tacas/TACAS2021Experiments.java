/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments.tacas;



import java.io.File;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
public class TACAS2021Experiments extends ASynthesizerExperiment {
	
	private static final String EXPERIMENTS_SET = "tacas/2021";
	private static final String EXPERIMENTS_PATH = "/resources/tacas/2021";
	private static final long BENCHMARK_TIME_SECONDS = 1 * 10;
	
	@Override
	public void setUp() {
		super.setUp();
		timeoutSeconds = BENCHMARK_TIME_SECONDS;
	}
	
	@Test
	public void experimentWithModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithModularizer", synthesizer);
	}
	
	@Test
	public void experimentRepairWithModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithModularizer-repair", synthesizer);
	}
	
	@Test
	public void experimentWithoutModulariter() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		synthesizer.setModularizer(null);
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithoutModularizer", synthesizer);
	}

}
