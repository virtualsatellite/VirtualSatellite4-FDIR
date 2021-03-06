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
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;

/**
 * This class produces the experimental data for the AIAA Aerospace conference
 * in 2017.
 * @author muel_s8
 *
 */

public class AIAA2017Experiments extends ASynthesizerExperiment {
	
	@Test
	public void experimentSwitch2AndRedundancy() throws IOException {
		System.out.println("--------------------- Experiment: Switch2 and Redundancy  ---------------------");
		Fault tle = createDFT("/resources/aiaa/2017/switch2AndRedundancy.dft");
		
		final float DELTA = 0.01f;
		
		FaultTreeEvaluator dftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(false, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		ModelCheckingResult result = dftEvaluator.evaluateFaultTree(tle);
		
		System.out.println("--------------------- Evaluation results for switch2AndRedundancy with DFT ---------------------");
		printResults(dftEvaluator, result, DELTA);
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(tle), null);
		saveRA(ra, "aiaa/2017/switch2AndRedundancy/synthesized");
		synthesizer = new BasicSynthesizer();
		ra = synthesizer.synthesize(new SynthesisQuery(tle), null);
		saveRA(ra, "aiaa/2017/switch2AndRedundancy/synthesizedMinimized");
		
		FaultTreeEvaluator ndDFTftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		ndDFTftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		result = ndDFTftEvaluator.evaluateFaultTree(tle);
		
		System.out.println("--------------------- Evaluation results for switch2AndRedundancy with NdDFT -----------------------");
		System.out.println("RA #States: " +  ra.getStates().size());
		System.out.println(ra.toDot());
		printResults(ndDFTftEvaluator, result, DELTA);
	}
	
	@Test
	public void experimentMemory2() throws IOException {
		System.out.println("--------------------- Experiment: Memory2  ---------------------");
		Fault tle = createDFT("/resources/aiaa/2017/memory2.dft");
		
		final float DELTA = 0.01f;
		FaultTreeEvaluator dftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(false, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		ModelCheckingResult result = dftEvaluator.evaluateFaultTree(tle);
		
		System.out.println("--------------------- Evaluation results for Memory2 with DFT ---------------------");
		printResults(dftEvaluator, result, DELTA);
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(new SynthesisQuery(tle), null);
		saveRA(ra, "aiaa/2017/memory2/synthesized");
		synthesizer = new BasicSynthesizer();
		ra = synthesizer.synthesize(new SynthesisQuery(tle), null);
		saveRA(ra, "aiaa/2017/memory2/synthesizedMinimized");
		
		FaultTreeEvaluator ndDFTftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, DELTA, FaultTreeEvaluator.DEFAULT_EPS);
		result = ndDFTftEvaluator.evaluateFaultTree(tle);
		
		System.out.println("--------------------- Evaluation results for Memory2 with NdDFT -----------------------");
		System.out.println("RA #States: " +  ra.getStates().size());
		System.out.println(ra.toDot());
		printResults(ndDFTftEvaluator, result, DELTA);
	}
}
