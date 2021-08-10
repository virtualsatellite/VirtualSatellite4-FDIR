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

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.CleanMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.FinalStateMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.OrthogonalPartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.PartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.POSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;

/**
 * This class uses the experimental data for the Science of Computer Programming (SCP) Journal paper
 * in 2019 with partial observability.
 * @author khan_ax
 *
 */
public class SCP2019POExperiments extends ASynthesizerExperiment {
	
	private static final int BENCHMARK_RUNS = 100;
	
	/**
	 * Creates an array of RAs for a benchmark run
	 * @param ra the base RA to copy
	 * @return the array of benchmark RAs
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
		Fault fault = createDFT("/resources/ftscs/obsCm1.dft");
		DFT2BasicDFTConverter dft2BasicDFTConverter = new DFT2BasicDFTConverter();
		FaultTreeNode system = dft2BasicDFTConverter.convert(fault).getRoot();
		
		POSynthesizer synthesizer = new POSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(system), null);

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
		Fault fault = createDFT("/resources/scp/cm1Nested.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(fault), null);
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
		FaultTreeEvaluator unminimizedEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		unminimizedEvaluator.evaluateFaultTree(fault);
		
		FaultTreeEvaluator minimizedEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(composedMinimized, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		minimizedEvaluator.evaluateFaultTree(fault);
	}
	
	@Test
	public void experimentMultiProcessorSystemNestedBenchmark() throws Exception {
		System.out.println("--------------------- Experiment: MCS Nested Benchmark  ---------------------");
		Fault fault = createDFT("/resources/scp/cm1Nested.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(fault), null);

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
		
		FaultTreeNode or = ftBuilder.createGate(tle, FaultTreeNodeType.OR);
		FaultTreeNode spare1 = ftBuilder.createGate(tle, FaultTreeNodeType.SPARE);
		spare1.setName("SPARE1");
		FaultTreeNode spare2 = ftBuilder.createGate(tle, FaultTreeNodeType.SPARE);
		spare2.setName("SPARE2");
		final float FAILURE_RATE = 1f;
		Fault memory1 = ftBuilder.createBasicFault("B1", FAILURE_RATE, 0);
		Fault memory2 = ftBuilder.createBasicFault("B2", FAILURE_RATE, 0);
		
		ftBuilder.connect(tle, or, tle);
		ftBuilder.connect(tle, spare1, or);
		ftBuilder.connect(tle, spare2, or);

		ftBuilder.connect(tle, memory1, spare1);
		ftBuilder.connect(tle, memory2, spare2);
		
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
			Fault backup = ftBuilder.createBasicFault("B" + (i + 2), FAILURE_RATE, 0);
			ftBuilder.connectSpare(tle, backup, spare1);
			ftBuilder.connectSpare(tle, backup, spare2);
			
			BasicSynthesizer synthesizer = new BasicSynthesizer();
			synthesizer.setMinimizer(null);
			RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(tle), null);
			
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
