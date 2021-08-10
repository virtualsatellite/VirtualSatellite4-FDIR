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
import java.util.function.Supplier;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ISynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizer;

/**
 * A test case for benchmarking experiments with partial observability
 * @author khan_ax
 *
 */
public class TACAS2021POExperiments extends ASynthesizerExperiment {
	
	private static final String EXPERIMENTS_SET = "tacas/2021";
	private static final String EXPERIMENTS_PATH = "/resources/tacas/2021";
	private static final long BENCHMARK_TIME_SECONDS = 60 * 10;
	
	@Override
	public void setUp() {
		super.setUp();
		timeoutSeconds = BENCHMARK_TIME_SECONDS;
	}
	
	private Supplier<ISynthesizer> modularSynthesizerSupplier = () -> new ModularSynthesizer();
	private Supplier<ISynthesizer> nonmodularSynthesizerSupplier = () -> new ModularSynthesizer().setModularizer(null);
	
	@Test
	public void experimentWithModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithModularizer", modularSynthesizerSupplier);
	}
	
	@Test
	public void experimentRepairWithModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithModularizer-repair", modularSynthesizerSupplier);
	}
	
	@Test
	public void experimentWithoutModulariter() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithoutModularizer", nonmodularSynthesizerSupplier);
	}
	
	@Test
	public void experimentRepairWithoutModulariter() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithoutModularizer-repair", nonmodularSynthesizerSupplier);
	}

}
