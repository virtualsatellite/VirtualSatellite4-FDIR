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

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.MarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.storm.runner.StormModelChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2DFTConversionResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.IDFT2DFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
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
	
	public static final float DEFAULT_EPS = 0.00001f;
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
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, FailableBasicEventsProvider failNodeProvider, SubMonitor subMonitor, IMetric... metrics) {
		if (metrics.length == 0) {
			metrics = new IMetric[] { Reliability.UNIT_RELIABILITY, MeanTimeToFailure.MTTF };
		}
		
		FaultTreeNode convertedRoot = root;
		
		for (IDFT2DFTConverter converter : preprocessConverters) {
			conversionResult = converter.convert(root);
			convertedRoot = conversionResult.getRoot();
		}
		
		FailableBasicEventsProvider failNodeProviderRemapped = failNodeProvider != null ? remapFailLabelProvider(failNodeProvider) : null;
		
		ModelCheckingResult result = evaluator.evaluateFaultTree(convertedRoot, failNodeProviderRemapped, subMonitor, metrics);
		if (!result.getMinCutSets().isEmpty()) {
			remapMinCutSets(result);
		}
		return result;
	}	
	
	
	/**
	 * Maps the nodes in the given failLabelProvider to the nodes of the transformed tree
	 * @param failNodeProvider the failable provider
	 * @return the failable provider with remapped nodes
	 */
	private FailableBasicEventsProvider remapFailLabelProvider(FailableBasicEventsProvider failNodeProvider) {
		FailableBasicEventsProvider failNodeProviderRemapped = new FailableBasicEventsProvider();
		for (BasicEvent be : failNodeProvider.getBasicEvents()) {
			FaultTreeNode remappedNode = conversionResult.getMapGeneratedToGenerator().keySet().stream()
					.filter(generated -> generated.getUuid().equals(be.getUuid()))
					.findFirst().get();
			failNodeProviderRemapped.getBasicEvents().add((BasicEvent) remappedNode);
		}
		return failNodeProviderRemapped;
	}

	/**
	 * Remaps the events of the computed mincut sets to the events of the original tree
	 * @param result a model checking result
	 */
	private void remapMinCutSets(ModelCheckingResult result) {
		Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator = conversionResult.getMapGeneratedToGenerator();
		Set<Set<Object>> originalMinimumCutSets = new HashSet<>();
		
		for (Set<Object> minimumCutSet : result.getMinCutSets()) {
			Set<Object> originalMiniumCutSet = new HashSet<>();
			for (Object object : minimumCutSet) {
				if (object instanceof FaultEvent) {
					FaultEvent fe = (FaultEvent) object;
					BasicEvent originalBe = (BasicEvent) mapGeneratedToGenerator.get(fe.getNodes().iterator().next());
					originalMiniumCutSet.add(originalBe);
				}
			}
			originalMinimumCutSets.add(originalMiniumCutSet);
		}
		
		// Make sure all cuts are mincuts
		Set<Set<Object>> subsumedMinCuts = new HashSet<>();
		for (Set<Object> minCut : originalMinimumCutSets) {
			for (Set<Object> minCutOther : originalMinimumCutSets) {
				if (minCut != minCutOther && minCut.containsAll(minCutOther)) {
					subsumedMinCuts.add(minCut);
				}
			}
		}
		originalMinimumCutSets.removeAll(subsumedMinCuts);
		
		result.getMinCutSets().clear();
		result.getMinCutSets().addAll(originalMinimumCutSets);
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
	 * Creates a fault tree evaluator with a recovery automaton
	 * @param ra a recovery automaton
	 * @return a fault tree evaluator
	 */
	public static FaultTreeEvaluator createDefaultFaultTreeEvaluator(RecoveryAutomaton ra) {
		return createDefaultFaultTreeEvaluator(ra, DEFAULT_DELTA, DEFAULT_EPS);
	}
	
	/**
	 * Creates a fault tree evaluator with a recovery automaton
	 * @param ra a recovery automaton
	 * @return a fault tree evaluator
	 */
	public static FaultTreeEvaluator createDefaultFaultTreeEvaluator(RecoveryAutomaton ra, double delta, double eps) {
		FaultTreeEvaluator ftEvaluator = createDefaultFaultTreeEvaluator(ra != null, delta, eps);
		if (ra != null) {
			ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		}
		return ftEvaluator;
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
			return decorateFaultTreeEvaluator(new StormDFTEvaluator(delta));
		} else {
			DFTSemantics defaultSemantics = isNondeterministic ? DFTSemantics.createNDDFTSemantics() : DFTSemantics.createStandardDFTSemantics();
			DFTSemantics poSemantics = PONDDFTSemantics.createPONDDFTSemantics();
			
			IMarkovModelChecker markovModelChecker = preferences.equals(EngineExecutionPreference.Custom.toString()) ?  new MarkovModelChecker(delta, eps) : new StormModelChecker(delta, FaultTreePreferences.getStormExecutionEnvironmentPreference());
			DFTEvaluator dftEvaluator = new DFTEvaluator(defaultSemantics, poSemantics, markovModelChecker);
			return decorateFaultTreeEvaluator(dftEvaluator);
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

	@Override
	public Object getStatistics() {
		return evaluator.getStatistics();
	}
}
