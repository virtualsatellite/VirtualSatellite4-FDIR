/*******************************************************************************
 * Copyright (c) 2008-2021 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments.phd;



import java.io.File;
import java.util.function.Supplier;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.CleanMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.FinalStateMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.OrthogonalPartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.PartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ISynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;

/**
 * A test case for benchmarking experiments
 * @author muel_s8
 *
 */
public class PhDExperiments extends ASynthesizerExperiment {
	
	public static final String EXPERIMENTS_SET = "phd";
	public static final String EXPERIMENTS_PATH = "/resources/" + EXPERIMENTS_SET;
	private static final long BENCHMARK_TIME_SECONDS = 60 * 10;
	
	@Override
	public void setUp() {
		super.setUp();
		timeoutSeconds = BENCHMARK_TIME_SECONDS;
	}
	
	private Supplier<ISynthesizer> modularSynthesizerSupplier = () -> new ModularSynthesizer();
	private Supplier<ISynthesizer> nonmodularSynthesizerSupplier = () -> new ModularSynthesizer().setModularizer(null);
	
	@Override
	protected void configQuery(SynthesisQuery query) {
		query.getFTHolder().getStaticAnalysis().setSymmetryChecker(null);
	}
	
	@Test
	public void experimentWithModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithModularizer", modularSynthesizerSupplier);
	}
	
	@Test
	public void experimentWithoutFailReduction() throws Exception {
		Supplier<ISynthesizer> supplier = () -> {
			ModularSynthesizer synthesizer = new ModularSynthesizer();
			BasicSynthesizer basicSynthesizer = (BasicSynthesizer) synthesizer.getDelegateSynthesizer().getBasicSynthesizer();
			
			ComposedMinimizer composedMinimizer = new ComposedMinimizer();
			composedMinimizer.addMinimizer(new PartitionRefinementMinimizer());
			composedMinimizer.addMinimizer(new OrthogonalPartitionRefinementMinimizer());
			composedMinimizer.addMinimizer(new CleanMinimizer());
			
			basicSynthesizer.setMinimizer(composedMinimizer);
			return synthesizer;
		};
		
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithoutFailReduction", supplier);
	}
	
	@Test
	public void experimentPostCompositionReduction() throws Exception {
		Supplier<ISynthesizer> supplier = () -> {
			ModularSynthesizer synthesizer = new ModularSynthesizer();
			ComposedMinimizer composedMinimizer = ComposedMinimizer.createDefaultMinimizer();
			synthesizer.setMinimizer(composedMinimizer);
			return synthesizer;
		};
		
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsPostCompositionReduction", supplier);
	}
	
	@Test
	public void experimentRepairWithModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithModularizer-repair", modularSynthesizerSupplier);
	}
	
	@Test
	public void experimentWithoutModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithoutModularizer", nonmodularSynthesizerSupplier);
	}
	
	@Test
	public void experimentRepairWithoutModularizer() throws Exception {
		File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair");
		benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatisticsWithoutModularizer-repair", nonmodularSynthesizerSupplier);
	}

	@Test
	public void experimentPO() throws Exception {
		for (int i = POPhdExperimentsGenerator.OBSERVATION_LEVELS - 1; i >= 0; --i) {
			File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-po-" + i);
			benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatistics-po-" + i, modularSynthesizerSupplier);	
		}
	}
	
	@Test
	public void experimentPORepair() throws Exception {
		for (int i = POPhdExperimentsGenerator.OBSERVATION_LEVELS - 1; i >= 0; --i) {
			File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair-po-" + i);
			benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatistics-po-repair-" + i, modularSynthesizerSupplier);
		}
	}
	
	@Test
	public void experimentPODelay() throws Exception {
		for (int i = POPhdExperimentsGenerator.OBSERVATION_LEVELS - 1; i >= 0; --i) {
			File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-po-delay-" + i);
			benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatistics-po-delay-" + i, modularSynthesizerSupplier);
		}
	}
	
	@Test
	public void experimentPORepairDelay() throws Exception {
		for (int i = POPhdExperimentsGenerator.OBSERVATION_LEVELS - 1; i >= 0; --i) {
			File experimentSet = new File("." + EXPERIMENTS_PATH + "/experimentSet-repair-po-delay-" + i);
			benchmark(experimentSet, EXPERIMENTS_PATH, EXPERIMENTS_SET + "/experimentStatistics-repair-po-delay-" + i, modularSynthesizerSupplier);
		}
	}
	
}
