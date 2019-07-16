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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.MarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.storm.runner.StormModelChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2DFTConversionResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.IDFT2DFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.preferences.EngineExecutionPreference;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferences;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

/**
 * This class provides a configureable evaluation workflow for fault trees.
 * Use instances of this class in the client code to actually evaluate fault trees.
 * @author muel_s8
 *
 */

public class FaultTreeEvaluator implements IFaultTreeEvaluator {
	
	public static final float DEFAULT_EPS = 0.001f;
	public static final float DEFAULT_DELTA = 0.1f;
	
	private IFaultTreeEvaluator evaluator;
	private List<IDFT2DFTConverter> preprocessConverters = new ArrayList<>();
	private DFT2DFTConversionResult conversionResult;
	
	/**
	 * Default public constructor
	 */
	public FaultTreeEvaluator() {
	
	}
	
	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		if (metrics.length == 0) {
			metrics = new IMetric[] { Reliability.UNIT_RELIABILITY, MTTF.MTTF };
		}
		
		FaultTreeNode convertedRoot = root;
		
		for (IDFT2DFTConverter converter : preprocessConverters) {
			conversionResult = converter.convert(root);
			convertedRoot = conversionResult.getRoot();
		}
		
		return evaluator.evaluateFaultTree(convertedRoot, metrics);
	}
	
	/**
	 * Gets the minimum cut sets from the fault tree
	 * @return the minimum cut sets causing the top level event
	 */
	public Set<Set<BasicEvent>> getMinimumCutSets() {
		Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator = conversionResult.getMapGeneratedToGenerator();
		Set<Set<BasicEvent>> minimumCutSets = evaluator.getMinimumCutSets();
		Set<Set<BasicEvent>> originalMinimumCutSets = new HashSet<>();
		
		for (Set<BasicEvent> minimumCutSet : minimumCutSets) {
			Set<BasicEvent> originalMiniumCutSet = new HashSet<>();
			for (BasicEvent be : minimumCutSet) {
				BasicEvent originalBe = (BasicEvent) mapGeneratedToGenerator.get(be);
				originalMiniumCutSet.add(originalBe);
			}
			originalMinimumCutSets.add(originalMiniumCutSet);
		}
		
		return originalMinimumCutSets;
	}

	@Override
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		evaluator.setRecoveryStrategy(recoveryStrategy);
	}
	
	/**
	 * Adss a new preprocession converter to the workflow
	 * @param dft2DftConverter the dftConverter
	 */
	public void addPreprocessConverter(IDFT2DFTConverter dft2DftConverter) {
		preprocessConverters.add(dft2DftConverter);
	}

	/**
	 * Creates a fault tree evaluator with a default setup
	 * @param isNondeterministic whether the evaluator uses Non-deterministic fault trees
	 * @return a fault tree evaluator
	 */
	public static FaultTreeEvaluator createDefaultFaultTreeEvaluator(boolean isNondeterministic) {
		return createDefaultFaultTreeEvaluator(isNondeterministic, DEFAULT_DELTA, DEFAULT_EPS);
	}
	
	/**
	 * Creates a fault tree evaluator with a default setup and a specified dft2MaConverter
	 * @param isNondeterministic whether the evaluator uses Non-deterministic fault trees
	 * @param delta the timestep slice
	 * @param eps the precision
	 * @return a fault tree evaluator
	 */
	public static FaultTreeEvaluator createDefaultFaultTreeEvaluator(boolean isNondeterministic, double delta, double eps) {
		
		String preferences = FaultTreePreferences.getEnginePreference();
		if (preferences.equals(EngineExecutionPreference.StormDFT.toString())) {
			return decorateFaultTreeEvaluator(new StormEvaluator(delta));
		} else {
			DFTSemantics defaultSemantics = isNondeterministic ? DFTSemantics.createNDDFTSemantics() : DFTSemantics.createStandardDFTSemantics();
			DFTSemantics poSemantics = PONDDFTSemantics.createPONDDFTSemantics();
			
			IMarkovModelChecker markovModelChecker = preferences.equals(EngineExecutionPreference.Custom.toString()) ?  new MarkovModelChecker(delta, eps) : new StormModelChecker(delta, FaultTreePreferences.getStormExecutionEnvironmentPreference());
			
			return decorateFaultTreeEvaluator(new DFTEvaluator(defaultSemantics, poSemantics, markovModelChecker));
		}
	}
	
	/**
	 * Creates a fault tree evaluator with a default setup and a specified dft2MaConverter
	 * @param evaluator the decorated fault tree evaluator
	 * @return a fault tree evaluator
	 */
	public static FaultTreeEvaluator decorateFaultTreeEvaluator(IFaultTreeEvaluator evaluator) {
		FaultTreeEvaluator ftEvaluator = new FaultTreeEvaluator();
		ftEvaluator.evaluator = evaluator;
		ftEvaluator.addPreprocessConverter(new DFT2BasicDFTConverter());
		return ftEvaluator;
	}
	
	/**
	 * Gets the internal evaluator
	 * @return the internal evaluator
	 */
	public IFaultTreeEvaluator getEvaluator() {
		return evaluator;
	}

}
