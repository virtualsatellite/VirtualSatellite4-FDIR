/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments.aiaa;

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.DFTEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.POSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;

/**
 * This class uses the experimental data for the Journal of Aerospace Information Systems (JAI AIAA)
 * in 2018 with partial observability.
 * @author khan_ax
 *
 */

public class AIAA2018POExperiments extends ASynthesizerExperiment {
	
	@Test
	public void experimentMemory2WithFDEP() throws IOException {
		Fault tle = createDFT("/resources/aiaa/2018/obsMemory2WithFDEP.dft");
		DFT2BasicDFTConverter dft2BasicDFTConverter = new DFT2BasicDFTConverter();
		FaultTreeNode system = dft2BasicDFTConverter.convert(tle).getRoot();
		
		final float DELTA = 0.01f;
		
		FaultTreeEvaluator dftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(false, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		ModelCheckingResult result = dftEvaluator.evaluateFaultTree(tle);
		
		System.out.println("--------------------- Evaluation results for Memory2WithFDEP with DFT ---------------------");
		printResults(dftEvaluator, result, DELTA);
		
		POSynthesizer synthesizer = new POSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(system), null);
		saveRA(ra, "aiaa/2018/obsMemory2WithFDEP/synthesized");
		synthesizer = new POSynthesizer();
		ra = synthesizer.synthesize(new SynthesisQuery(system), null);
		saveRA(ra, "aiaa/2018/obsMemory2WithFDEP/synthesizedMinimized");
		
		FaultTreeEvaluator ndDFTftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		result = ndDFTftEvaluator.evaluateFaultTree(system);
		
		System.out.println("--------------------- Evaluation results for Memory2WithFDEP with NdDFT -----------------------");
		System.out.println("RA #States: " +  ra.getStates().size());
		System.out.println(ra.toDot());
		printResults(ndDFTftEvaluator, result, DELTA);
	}
	
	@Test
	public void experimentMemory2AndNBackups() {
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
		
		final float DELTA = 0.01f;
		final int MAX_BACKUPS = 10;
		
		DFT2BasicDFTConverter dft2BasicDFTConverter = new DFT2BasicDFTConverter();
		FaultTreeNode system = dft2BasicDFTConverter.convert(tle).getRoot();
		
		for (int i = 1; i <= MAX_BACKUPS; ++i) {
			Fault backup = ftBuilder.createBasicFault("B" + (i + 2), FAILURE_RATE, 0);
			ftBuilder.connectSpare(tle, backup, spare1);
			ftBuilder.connectSpare(tle, backup, spare2);
			
			BasicSynthesizer synthesizer = new BasicSynthesizer();
			synthesizer.setMinimizer(new ComposedMinimizer());
			RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(system), null);
			
			FaultTreeEvaluator ndDFTftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
			ndDFTftEvaluator.evaluateFaultTree(system);
			int statesMc = ((DFTEvaluator) ndDFTftEvaluator.getEvaluator()).getStatistics().maBuildStatistics.maxStates;
			System.out.println(i + " " + statesMc);
		}
	}
}
