/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments.ftscs;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.CleanMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.FinalStateMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.OrthogonalPartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.PartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;

/**
 * This class produces the experimental data for the Science of Computer Programming (SCP) Journal paper
 * in 2019.
 * @author muel_s8
 *
 */
public class SCP2019Experiments extends ASynthesizerExperiment {
	
	private static final int BENCHMARK_RUNS = 100;
	
	/**
	 * Creates an array of RAs for a benchmark run
	 * @param ra the base RA to copy
	 * @return tge array of benchmark RAs
	 */
	private RecoveryAutomaton[] createBenchmarkRas(RecoveryAutomaton ra) {
		RecoveryAutomaton[] benchmarkRas = new RecoveryAutomaton[BENCHMARK_RUNS];
		for (int j = 0; j < BENCHMARK_RUNS; ++j) {
			benchmarkRas[j] = raHelper.copyRA(ra);
		}
		return benchmarkRas;
	}
	
	@Test
	public void experimentMultiProcessorSystemBenchmark() throws Exception {
		System.out.println("--------------------- Experiment: MCS Benchmark  ---------------------");
		Fault fault = createGalileoDFT("/resources/ftscs/cm1.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		ComposedMinimizer basicMinimizer = new ComposedMinimizer();
		basicMinimizer.addMinimizer(new FinalStateMinimizer());
		basicMinimizer.addMinimizer(new OrthogonalPartitionRefinementMinimizer());
		basicMinimizer.addMinimizer(new CleanMinimizer());
		
		RecoveryAutomaton[] benchmarkRas = createBenchmarkRas(ra);
		long timeStart = System.currentTimeMillis();
		for (int i = 0; i < BENCHMARK_RUNS; ++i) {
			basicMinimizer.minimize(benchmarkRas[i]);
			
		}
		long timeEnd = System.currentTimeMillis();
		long timeBasic = timeEnd - timeStart;
		timeBasic /= BENCHMARK_RUNS;
		System.out.println("Basic Workflow " + timeBasic);
		
		ComposedMinimizer traceMinimizer = new ComposedMinimizer();
		traceMinimizer.addMinimizer(new PartitionRefinementMinimizer());
		traceMinimizer.addMinimizer(new CleanMinimizer());
		
		benchmarkRas = createBenchmarkRas(ra);
		timeStart = System.currentTimeMillis();
		for (int i = 0; i < BENCHMARK_RUNS; ++i) {
			traceMinimizer.minimize(benchmarkRas[i]);
		}
		timeEnd = System.currentTimeMillis();
		long timeTrace = timeEnd - timeStart;
		timeTrace /= BENCHMARK_RUNS;
		System.out.println("Trace " + timeTrace);
		
		ComposedMinimizer improvedMinimizer = ComposedMinimizer.createDefaultMinimizer();
		
		benchmarkRas = createBenchmarkRas(ra);
		timeStart = System.currentTimeMillis();
		for (int i = 0; i < BENCHMARK_RUNS; ++i) {
			improvedMinimizer.minimize(benchmarkRas[i]);
		}
		timeEnd = System.currentTimeMillis();
		long timeImproved = timeEnd - timeStart;
		timeImproved /= BENCHMARK_RUNS;
		System.out.println("Improved Workflow " + timeImproved);
	}
	
	@Test
	public void experimentMultiProcessorSystemNested() throws Exception {
		System.out.println("--------------------- Experiment: MCS Nested  ---------------------");
		Fault fault = createGalileoDFT("/resources/scp/cm1Nested.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		System.out.println("Original #states: " + ra.getStates().size());
		System.out.println("Original #transitions: " + ra.getTransitions().size());
		saveRA(ra, "scp/2019/nestedMCS/original");
		
		RecoveryAutomaton partitionRefinementMinimized = raHelper.copyRA(ra);
		PartitionRefinementMinimizer prMinimizer = new PartitionRefinementMinimizer();
		prMinimizer.minimize(partitionRefinementMinimized);
		double partitionStateReduction = 1 - (double) partitionRefinementMinimized.getStates().size() / ra.getStates().size();
		double partitionTransitionReduction = 1 - (double) partitionRefinementMinimized.getTransitions().size() / ra.getTransitions().size();
		System.out.println("Partition Refinement #states: " + partitionRefinementMinimized.getStates().size());
		System.out.println("Partition Refinement #transitions: " + partitionRefinementMinimized.getTransitions().size());
		System.out.println("Partition Refinement %state reduction: " + partitionStateReduction);
		System.out.println("Partition Refinement %transition reduction: " + partitionTransitionReduction);
		saveRA(ra, "scp/2019/nestedMCS/traceEquivalenceRule");
		
		RecoveryAutomaton fsMinimized = raHelper.copyRA(ra);
		FinalStateMinimizer fsMinimizer = new FinalStateMinimizer();
		fsMinimizer.minimize(fsMinimized);
		System.out.println("Final States Minimizer #states: " + fsMinimized.getStates().size());
		System.out.println("Final States Minimizer #transitions: " + fsMinimized.getTransitions().size());
		saveRA(ra, "scp/2019/nestedMCS/finalStateRule");
		
		RecoveryAutomaton orthogonalMinimized = raHelper.copyRA(ra);
		OrthogonalPartitionRefinementMinimizer osMinimizer = new OrthogonalPartitionRefinementMinimizer();
		osMinimizer.minimize(orthogonalMinimized);
		System.out.println("Orthogonal States Minimizer #states: " + orthogonalMinimized.getStates().size());
		System.out.println("Orthogonal States Minimizer #transitions: " + orthogonalMinimized.getTransitions().size());
		saveRA(ra, "scp/2019/nestedMCS/orthogonalRule");
		
		RecoveryAutomaton composedMinimized = raHelper.copyRA(ra);
		ComposedMinimizer cMinimizer = ComposedMinimizer.createDefaultMinimizer();
		cMinimizer.minimize(composedMinimized);
		double composedStateReduction = 1 - (double) composedMinimized.getStates().size() / ra.getStates().size();
		double composedTransitionReduction = 1 -  (double) composedMinimized.getTransitions().size() / ra.getTransitions().size();
		System.out.println("All Minimizers Combined #states: " + composedMinimized.getStates().size());
		System.out.println("All Minimizers Combined #transitions: " + composedMinimized.getTransitions().size());
		System.out.println("All Minimizers Combined %state reduction: " + composedStateReduction);
		System.out.println("All Minimizers Combined %transition reduction: " + composedTransitionReduction);
		
		double stateReductionCombinedVsPartition =  1 - (double) composedMinimized.getStates().size() / partitionRefinementMinimized.getStates().size();
		double transitionReductionCombinedVsPartition = 1 - (double) composedMinimized.getTransitions().size() / partitionRefinementMinimized.getTransitions().size();
		System.out.println("Combined vs Partition Refinement %state reduction: " + stateReductionCombinedVsPartition);
		System.out.println("Combined vs Partition Refinement %transition reduction: " + transitionReductionCombinedVsPartition);
		saveRA(ra, "scp/2019/nestedMCS/composedRules");
		
		final float DELTA = 0.01f;
		FaultTreeEvaluator unminimizedEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		unminimizedEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		unminimizedEvaluator.evaluateFaultTree(fault);
		
		FaultTreeEvaluator minimizedEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		minimizedEvaluator.setRecoveryStrategy(new RecoveryStrategy(composedMinimized));
		minimizedEvaluator.evaluateFaultTree(fault);
	}
	
	@Test
	public void experimentMultiProcessorSystemNestedBenchmark() throws Exception {
		System.out.println("--------------------- Experiment: MCS Nested Benchmark  ---------------------");
		Fault fault = createGalileoDFT("/resources/scp/cm1Nested.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		ComposedMinimizer basicMinimizer = new ComposedMinimizer();
		basicMinimizer.addMinimizer(new FinalStateMinimizer());
		basicMinimizer.addMinimizer(new OrthogonalPartitionRefinementMinimizer());
		basicMinimizer.addMinimizer(new CleanMinimizer());
		
		RecoveryAutomaton[] benchmarkRas = createBenchmarkRas(ra);
		long timeStart = System.currentTimeMillis();
		for (int i = 0; i < BENCHMARK_RUNS; ++i) {
			basicMinimizer.minimize(benchmarkRas[i]);
			
		}
		long timeEnd = System.currentTimeMillis();
		long timeBasic = timeEnd - timeStart;
		timeBasic /= BENCHMARK_RUNS;
		System.out.println("Basic Workflow " + timeBasic);
		
		ComposedMinimizer traceMinimizer = new ComposedMinimizer();
		traceMinimizer.addMinimizer(new PartitionRefinementMinimizer());
		traceMinimizer.addMinimizer(new CleanMinimizer());
		
		benchmarkRas = createBenchmarkRas(ra);
		timeStart = System.currentTimeMillis();
		for (int i = 0; i < BENCHMARK_RUNS; ++i) {
			traceMinimizer.minimize(benchmarkRas[i]);
		}
		timeEnd = System.currentTimeMillis();
		long timeTrace = timeEnd - timeStart;
		timeTrace /= BENCHMARK_RUNS;
		System.out.println("Trace " + timeTrace);
		
		ComposedMinimizer improvedMinimizer = ComposedMinimizer.createDefaultMinimizer();
		
		benchmarkRas = createBenchmarkRas(ra);
		timeStart = System.currentTimeMillis();
		for (int i = 0; i < BENCHMARK_RUNS; ++i) {
			improvedMinimizer.minimize(benchmarkRas[i]);
		}
		timeEnd = System.currentTimeMillis();
		long timeImproved = timeEnd - timeStart;
		timeImproved /= BENCHMARK_RUNS;
		System.out.println("Improved Workflow " + timeImproved);
	}
	
	@Test
	public void experimentMemory2AndNBackupsBenchmark() {
		System.out.println("--------------------- Experiment: Memory2 and N Backups Benchmark  ---------------------");
		Fault tle = new Fault(concept);
		
		FaultTreeNode or = ftHelper.createGate(tle, FaultTreeNodeType.OR);
		FaultTreeNode spare1 = ftHelper.createGate(tle, FaultTreeNodeType.SPARE);
		spare1.setName("SPARE1");
		FaultTreeNode spare2 = ftHelper.createGate(tle, FaultTreeNodeType.SPARE);
		spare2.setName("SPARE2");
		final float FAILURE_RATE = 1f;
		Fault memory1 = ftHelper.createBasicFault("B1", FAILURE_RATE);
		Fault memory2 = ftHelper.createBasicFault("B2", FAILURE_RATE);
		
		ftHelper.connect(tle, or, tle);
		ftHelper.connect(tle, spare1, or);
		ftHelper.connect(tle, spare2, or);

		ftHelper.connect(tle, memory1, spare1);
		ftHelper.connect(tle, memory2, spare2);
		
		final int MAX_BACKUPS = 10;
		
		ComposedMinimizer basicMinimizer = new ComposedMinimizer();
		basicMinimizer.addMinimizer(new FinalStateMinimizer());
		basicMinimizer.addMinimizer(new OrthogonalPartitionRefinementMinimizer());
		basicMinimizer.addMinimizer(new CleanMinimizer());

		ComposedMinimizer traceMinimizer = new ComposedMinimizer();
		traceMinimizer.addMinimizer(new PartitionRefinementMinimizer());
		traceMinimizer.addMinimizer(new CleanMinimizer());
		
		ComposedMinimizer improvedMinimizer = ComposedMinimizer.createDefaultMinimizer();
		
		for (int i = 1; i <= MAX_BACKUPS; ++i) {
			Fault backup = ftHelper.createBasicFault("B" + (i + 2), FAILURE_RATE);
			ftHelper.connectSpare(tle, backup, spare1);
			ftHelper.connectSpare(tle, backup, spare2);
			
			BasicSynthesizer synthesizer = new BasicSynthesizer();
			synthesizer.setMinimizer(null);
			RecoveryAutomaton ra = synthesizer.synthesize(tle);
			
			RecoveryAutomaton[] benchmarkRas = createBenchmarkRas(ra);
			long timeStart = System.currentTimeMillis();
			for (int j = 0; j < BENCHMARK_RUNS; ++j) {
				basicMinimizer.minimize(benchmarkRas[j]);
			}
			long timeEnd = System.currentTimeMillis();
			long timeBasic = timeEnd - timeStart;
			timeBasic /= BENCHMARK_RUNS;
			
			benchmarkRas = createBenchmarkRas(ra);
			timeStart = System.currentTimeMillis();
			for (int j = 0; j < BENCHMARK_RUNS; ++j) {
				traceMinimizer.minimize(benchmarkRas[j]);
			}
			timeEnd = System.currentTimeMillis();
			long timeTrace = timeEnd - timeStart;
			timeTrace /= BENCHMARK_RUNS;
			
			benchmarkRas = createBenchmarkRas(ra);
			timeStart = System.currentTimeMillis();
			for (int j = 0; j < BENCHMARK_RUNS; ++j) {
				improvedMinimizer.minimize(benchmarkRas[j]);
			}
			timeEnd = System.currentTimeMillis();
			long timeImproved = timeEnd - timeStart;
			timeImproved /= BENCHMARK_RUNS;
			
			System.out.println(i  + " " + timeBasic + " " + timeTrace + " " + timeImproved);
		}
	}
}
