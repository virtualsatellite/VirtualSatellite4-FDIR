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

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.FinalStateMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.OrthogonalPartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.PartitionRefinementMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;

/**
 * This class produces the experimental data for the Formal Techniques for Safety Critical Systems
 * workshop 2018.
 * @author muel_s8
 *
 */

public class FTSCS2018Experiments extends ASynthesizerExperiment {
	
	@Test
	public void experimentMultiProcessorSystem() throws Exception {
		Fault fault = createDFT("/resources/ftscs/cm1.dft");
		DFT2BasicDFTConverter dft2BasicDFTConverter = new DFT2BasicDFTConverter();
		FaultTreeNode system = dft2BasicDFTConverter.convert(fault).getRoot();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(system), null);
		System.out.println("Original #states: " + ra.getStates().size());
		System.out.println("Original #transitions: " + ra.getTransitions().size());
		saveRA(ra, "ftscs/mcs/original");
		
		RecoveryAutomaton partitionRefinementMinimized = raHelper.copyRA(ra);
		PartitionRefinementMinimizer prMinimizer = new PartitionRefinementMinimizer();
		prMinimizer.minimize(partitionRefinementMinimized);
		double partitionStateReduction = 1 - (double) partitionRefinementMinimized.getStates().size() / ra.getStates().size();
		double partitionTransitionReduction = 1 - (double) partitionRefinementMinimized.getTransitions().size() / ra.getTransitions().size();
		System.out.println("Partition Refinement #states: " + partitionRefinementMinimized.getStates().size());
		System.out.println("Partition Refinement #transitions: " + partitionRefinementMinimized.getTransitions().size());
		System.out.println("Partition Refinement %state reduction: " + partitionStateReduction);
		System.out.println("Partition Refinement %transition reduction: " + partitionTransitionReduction);
		saveRA(partitionRefinementMinimized, "ftscs/mcs/traceEquivalenceRule");
		
		RecoveryAutomaton fsMinimized = raHelper.copyRA(ra);
		FinalStateMinimizer fsMinimizer = new FinalStateMinimizer();
		fsMinimizer.minimize(fsMinimized);
		System.out.println("Final States Minimizer #states: " + fsMinimized.getStates().size());
		System.out.println("Final States Minimizer #transitions: " + fsMinimized.getTransitions().size());
		saveRA(fsMinimized, "ftscs/mcs/finalStateRule");
		
		RecoveryAutomaton orthogonalMinimized = raHelper.copyRA(ra);
		OrthogonalPartitionRefinementMinimizer osMinimizer = new OrthogonalPartitionRefinementMinimizer();
		osMinimizer.minimize(orthogonalMinimized);
		System.out.println("Orthogonal States Minimizer #states: " + orthogonalMinimized.getStates().size());
		System.out.println("Orthogonal States Minimizer #transitions: " + orthogonalMinimized.getTransitions().size());
		saveRA(fsMinimized, "ftscs/mcs/orthogonalRule");
		
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
		saveRA(fsMinimized, "ftscs/mcs/composedRules");
		
		final float DELTA = 0.01f;
		FaultTreeEvaluator unminimizedEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		unminimizedEvaluator.evaluateFaultTree(system);
		
		FaultTreeEvaluator minimizedEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(composedMinimized, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		minimizedEvaluator.evaluateFaultTree(system);
	}
	
	@Test
	public void experimentMemory2AndNBackups() throws IOException {
		System.out.println("--------------------- Experiment: Memory2 and N Backups  ---------------------");
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
		
		final int MAX_BACKUPS = 20;
		
		for (int i = 1; i <= MAX_BACKUPS; ++i) {
			Fault backup = ftBuilder.createBasicFault("B" + (i + 2), FAILURE_RATE, 0);
			ftBuilder.connectSpare(tle, backup, spare1);
			ftBuilder.connectSpare(tle, backup, spare2);
			
			BasicSynthesizer synthesizer = new BasicSynthesizer();
			RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(tle), null);

			int statesMa = synthesizer.getStatistics().maBuildStatistics.maxStates;
			int statesMinimizedRa = ra.getStates().size();
			int statesUnminimizedRa = statesMinimizedRa + synthesizer.getStatistics().minimizationStatistics.removedStates;
			
			System.out.println(i  + " " + statesMa + " " + statesMinimizedRa + " " + statesUnminimizedRa);
			
			saveRA(ra, "ftscs/memory2WithNBackups/" + i + "_backups");
		}
	}
	
}
