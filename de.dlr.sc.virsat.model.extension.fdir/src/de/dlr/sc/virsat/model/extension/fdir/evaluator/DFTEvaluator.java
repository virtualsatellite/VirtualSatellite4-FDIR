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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingQuery;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.AProbabilityCurve;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MetricsResultDeriver;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTSymmetryChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Modularizer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Evaluator for Dynamic Fault Trees, resolving non determinism using a recovery
 * strategy.
 * 
 * @author muel_s8
 *
 */
public class DFTEvaluator implements IFaultTreeEvaluator {
	
	private DFTSemantics defaultSemantics;
	private DFTSemantics poSemantics;
	
	private IMarkovModelChecker markovModelChecker;
	private DFT2MAConverter dft2MaConverter = new DFT2MAConverter();
	private Modularizer modularizer = new Modularizer();
	private DFTSymmetryChecker symmetryChecker = new DFTSymmetryChecker();
	private DFTMetricsComposer composer = new DFTMetricsComposer();
	private MetricsResultDeriver deriver = new MetricsResultDeriver();
	private DFTEvaluationStatistics statistics;
	
	/**
	 * Constructor using the passed recovery strategy
	 * 
	 * @param defaultSemantics the dft semantics
	 * @param poSemantics the semantics to be used for partial observable fault trees
	 * @param markovModelChecker the model Checker
	 */
	public DFTEvaluator(DFTSemantics defaultSemantics, DFTSemantics poSemantics, IMarkovModelChecker markovModelChecker) {
		this.defaultSemantics = defaultSemantics;
		this.poSemantics = poSemantics;
		this.markovModelChecker = markovModelChecker;
	}

	@Override
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		dft2MaConverter.getStateSpaceGenerator().setRecoveryStrategy(recoveryStrategy);
	}

	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, FailableBasicEventsProvider failableBasicEventsProvider, SubMonitor subMonitor, IMetric... metrics) {
		statistics = new DFTEvaluationStatistics();
		statistics.time = System.currentTimeMillis();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		dft2MaConverter.configure(ftHolder, chooseSemantics(ftHolder), metrics, failableBasicEventsProvider);
		
		DFTModularization modularization = getModularization(ftHolder, failableBasicEventsProvider);
		
		Map<FailLabelProvider, IMetric[]> partitioning = IMetric.partitionMetrics(modularization != null, metrics);
		IDerivedMetric[] derivedMetrics = (IDerivedMetric[]) partitioning.get(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER);
		
		subMonitor = SubMonitor.convert(subMonitor, partitioning.size());
		Map<FailLabelProvider, ModelCheckingResult> baseResults = evaluateFaultTree(ftHolder, failableBasicEventsProvider, partitioning, modularization, subMonitor);
		ModelCheckingResult result = deriver.derive(baseResults, markovModelChecker.getDelta(), derivedMetrics);
		
		int steps = getUniformStepCount(metrics);
		result.limitPointMetrics(steps);
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		return result;
	}
	
	/**
	 * Computes the uniform step count for curve type metrics
	 * @param metrics the metrics to consider
	 * @return the number of points each curve metric should have
	 */
	private int getUniformStepCount(IMetric[] metrics) {
		int steps = 0;
		for (IMetric metric : metrics) {
			if (metric instanceof AProbabilityCurve) {
				double time = ((AProbabilityCurve) metric).getTime();
				steps = Math.max(steps, (int) (time / markovModelChecker.getDelta()) + 1);
			}
		}
		
		return steps;
	}
	
	/**
	 * Performs a DFT evaluation for the base metrics in the given metrics partitioning
	 * @param ftHolder the fault tree holder
	 * @param failableBasicEventsProvider the node fail criteria
	 * @param modularization optionally a modularization of the dft
	 * @param subMonitor eclipse ui element for progress reporting
	 * @return a mapping from fail label to model checking result
	 */
	private Map<FailLabelProvider, ModelCheckingResult> evaluateFaultTree(FaultTreeHolder ftHolder, FailableBasicEventsProvider failableBasicEventsProvider, Map<FailLabelProvider, IMetric[]> partitioning, DFTModularization modularization, SubMonitor monitor) {
		final int COUNT_WORK = (modularization != null ? 2 * modularization.getModulesToModelCheck().size() : 2) + partitioning.size() - 1;
		SubMonitor subMonitor = SubMonitor.convert(monitor, COUNT_WORK);
		
		if (modularization == null) {
			MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(ftHolder.getRoot(), failableBasicEventsProvider, subMonitor.split(1));
			statistics.maBuildStatistics.compose(dft2MaConverter.getMaBuilder().getStatistics());
			
			return modelCheck(ma, subMonitor.split(1), partitioning);
		}
		
		Module topLevelModule = modularization.getTopLevelModule();
		Map<Module, Map<FailLabelProvider, ModelCheckingResult>> mapModuleToBaseResults = new HashMap<>();
		
		// Build the mapping of module to model checking results per fail label provider
		for (Module module : modularization.getModulesToModelCheck()) {
			FaultTreeNode representant = modularization.getMapNodeToRepresentant().get(module.getRootNode());
			Module representantModule =  modularization.getModule(representant);
			
			MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(module.getRootNode(), failableBasicEventsProvider, subMonitor.split(1));
			statistics.maBuildStatistics.compose(dft2MaConverter.getMaBuilder().getStatistics());
			
			Map<FailLabelProvider, ModelCheckingResult> representantBaseResults = mapModuleToBaseResults
					.computeIfAbsent(representantModule, key -> modelCheck(ma, subMonitor.split(1), partitioning));
			mapModuleToBaseResults.put(module, representantBaseResults);
		}
		// Extract for each fail label provider a mapping from module to results to compose the leaf modules
		// upwards until hitting the top level module
		for (Entry<FailLabelProvider, IMetric[]> entry : partitioning.entrySet()) {
			FailLabelProvider failLabelProvider = entry.getKey();
			if (!failLabelProvider.equals(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER)) {
				Map<Module, ModelCheckingResult> mapModuleToResult = new HashMap<>();
				for (Module module : modularization.getModulesToModelCheck()) {
					Map<FailLabelProvider, ModelCheckingResult> baseResults = mapModuleToBaseResults.get(module);
					mapModuleToResult.put(module, baseResults.get(failLabelProvider));
				}
				
				IBaseMetric[] baseMetrics = (IBaseMetric[]) entry.getValue();
				composer.composeModuleResults(topLevelModule, modularization, subMonitor.split(1), baseMetrics, mapModuleToResult);
				ModelCheckingResult topLevelResult = mapModuleToResult.get(topLevelModule);
				
				mapModuleToBaseResults.computeIfAbsent(topLevelModule, key -> new HashMap<>()).put(failLabelProvider, topLevelResult);
			}
		}
		
		return mapModuleToBaseResults.get(topLevelModule);
	}
	
	/**
	 * Gets the modularization for the DFT if possible
	 * @param ftHolder the fault tree holder
	 * @param failNodeProvider optionally a fail node criteria
	 * @return the modularization
	 */
	private DFTModularization getModularization(FaultTreeHolder ftHolder, FailableBasicEventsProvider failNodeProvider) {
		boolean canModularize = ftHolder.getRoot() instanceof Fault
				&& dft2MaConverter.getStateSpaceGenerator().getDftSemantics() != poSemantics
				&& failNodeProvider == null;
		
		if (!canModularize) {
			return null;
		}
		
		DFTModularization modularization = new DFTModularization(modularizer, ftHolder, symmetryChecker);
		
		if (modularization.getTopLevelModule() != null && modularization.getModulesToModelCheck().size() > 1) {
			return modularization;
		} else {
			return null;
		}
	}
	
	/**
	 * Model checks a markov automaton for base metric in the given metric partitioning
	 * @param ma the markov automaton of a dft or module of a dft
	 * @param subMonitor eclipse ui element for progress reporting
	 * @param partitioning a partitioning of the metrics
	 * @return a mapping from fail label provider to the results
	 */
	private Map<FailLabelProvider, ModelCheckingResult> modelCheck(MarkovAutomaton<DFTState> ma, SubMonitor subMonitor, Map<FailLabelProvider, IMetric[]> partitioning) {
		final int COUNT_WORK = partitioning.size() - 1;
		subMonitor = SubMonitor.convert(subMonitor, COUNT_WORK);
		
		Map<FailLabelProvider, ModelCheckingResult> baseResults = new HashMap<>(); 
		for (Entry<FailLabelProvider, IMetric[]> entry : partitioning.entrySet()) {
			if (!entry.getKey().equals(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER)) {
				ModelCheckingQuery<DFTState> modelCheckingQuery = new ModelCheckingQuery<>(ma, entry.getKey(), (IBaseMetric[]) entry.getValue());
				ModelCheckingResult result = markovModelChecker.checkModel(modelCheckingQuery, subMonitor.split(1));
				statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());	
				baseResults.put(entry.getKey(), result);
			}
		}
		
		return baseResults;
	}
	
	/**
	 * Chooses the semantics depending on the type of tree
	 * @param ftHolder the tree
	 * @return the semantics required based on the tree type
	 */
	private DFTSemantics chooseSemantics(FaultTreeHolder ftHolder) {
		if (ftHolder.isPartialObservable()) {
			return poSemantics;
		} else {
			return defaultSemantics;
		}
	}
	
	@Override
	public DFTEvaluationStatistics getStatistics() {
		return statistics;
	}
}
