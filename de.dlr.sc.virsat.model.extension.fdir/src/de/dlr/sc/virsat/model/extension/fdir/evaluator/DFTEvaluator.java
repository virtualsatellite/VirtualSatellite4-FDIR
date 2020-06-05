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
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.AProbabilityCurve;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
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
	
	private MarkovAutomaton<DFTState> mc;
	private IMarkovModelChecker markovModelChecker;
	private DFT2MAConverter dft2MaConverter = new DFT2MAConverter();
	private Modularizer modularizer = new Modularizer();
	private DFTSymmetryChecker symmetryChecker = new DFTSymmetryChecker();
	private DFTMetricsComposer composer = new DFTMetricsComposer();
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
		
		Map<FailLabelProvider, IMetric[]> partitioning = IMetric.partitionMetrics(metrics, modularization != null);
		IDerivedMetric[] derivedMetrics = (IDerivedMetric[]) partitioning.get(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER);
		
		subMonitor = SubMonitor.convert(subMonitor, partitioning.size());
		Map<FailLabelProvider, ModelCheckingResult> baseResults = computeBaseResults(partitioning, failableBasicEventsProvider, ftHolder, modularization, subMonitor);
		ModelCheckingResult result = composer.derive(baseResults, markovModelChecker.getDelta(), derivedMetrics);
		
		int steps = getUniformStepCount(metrics);
		result.limitPointMetrics(steps);
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		return result;
	}
	
	/**
	 * Evaluates all base modules
	 * @param partitioning a partitioning of metrics to which fail labels are required to compute them
	 * @param failableBasicEventsProvider the set of basic events that is allowed to fail
	 * @param ftHolder the fault tree 
	 * @param modularization a modularization of the dft
	 * @param subMonitor a progress monitor
	 * @return the model checking result for each fail criteria
	 */
	private Map<FailLabelProvider, ModelCheckingResult> computeBaseResults(Map<FailLabelProvider, IMetric[]> partitioning, FailableBasicEventsProvider failableBasicEventsProvider, 
			FaultTreeHolder ftHolder, DFTModularization modularization, SubMonitor subMonitor) {
		Map<FailLabelProvider, ModelCheckingResult> baseResults = new HashMap<>();
		for (Entry<FailLabelProvider, IMetric[]> metricPartition : partitioning.entrySet()) {
			SubMonitor partitionMonitor = subMonitor.split(1);
			if (!metricPartition.getKey().equals(FailLabelProvider.EMPTY_FAIL_LABEL_PROVIDER)) {
				IBaseMetric[] baseMetrics = (IBaseMetric[]) metricPartition.getValue();
				ModelCheckingResult result = evaluateFaultTree(ftHolder, failableBasicEventsProvider, metricPartition.getKey(), modularization, partitionMonitor, baseMetrics);
				baseResults.put(metricPartition.getKey(), result);
			}
		}
		
		return baseResults;
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
	 * Performs a DFT evaluation for the given base metrics with the given fail criteria
	 * @param ftHolder the fault tree holder
	 * @param failableBasicEventsProvider the node fail criteria
	 * @param failLabelProvider the labeling fail criteria
	 * @param modularization optionally a modularization of the dft
	 * @param subMonitor eclipse ui element for progress reporting
	 * @param baseMetrics the metrics to model check
	 * @return the model checking result
	 */
	private ModelCheckingResult evaluateFaultTree(FaultTreeHolder ftHolder, FailableBasicEventsProvider failableBasicEventsProvider, FailLabelProvider failLabelProvider, DFTModularization modularization, SubMonitor subMonitor, IBaseMetric... baseMetrics) {
		if (modularization == null) {
			return modelCheck(ftHolder.getRoot(), subMonitor, baseMetrics, failableBasicEventsProvider, failLabelProvider);
		}
		
		Module topLevelModule = modularization.getTopLevelModule();
		Map<Module, ModelCheckingResult> mapModuleToResult = new HashMap<>();
		
		for (Module module : modularization.getModulesToModelCheck()) {
			FaultTreeNode representant = modularization.getMapNodeToRepresentant().get(module.getRootNode());
			Module representantModule =  modularization.getModule(representant);
			ModelCheckingResult representantResult = mapModuleToResult
					.computeIfAbsent(representantModule, key -> modelCheck(key.getRootNode(), subMonitor, baseMetrics, failableBasicEventsProvider, failLabelProvider));
			mapModuleToResult.put(module, representantResult);
		}
		
		composer.composeModuleResults(topLevelModule, modularization, subMonitor, baseMetrics, mapModuleToResult);
		return mapModuleToResult.get(modularization.getTopLevelModule());
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
	 * Model checks a tree
	 * @param root the root of the tree
	 * @param subMonitor eclipse ui element for progress reporting
	 * @param metrics the metrics to model check
	 * @param failableBasicEventsProvider the nodes that need to fail
	 * @param failLabelProvider the labels that will make a node considered to be failed
	 * @return the result object containing the metrics
	 */
	private ModelCheckingResult modelCheck(FaultTreeNode root, SubMonitor subMonitor, IBaseMetric[] metrics, FailableBasicEventsProvider failableBasicEventsProvider, FailLabelProvider failLabelProvider) {
		final int COUNT_WORK = 2;
		subMonitor = SubMonitor.convert(subMonitor, COUNT_WORK);
		
		mc = dft2MaConverter.convert(root, failableBasicEventsProvider, failLabelProvider, subMonitor.split(1));
		ModelCheckingResult result = markovModelChecker.checkModel(mc, subMonitor.split(1), metrics);
			
		statistics.maBuildStatistics.compose(dft2MaConverter.getMaBuilder().getStatistics());
		statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());	
		
		return result;
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
