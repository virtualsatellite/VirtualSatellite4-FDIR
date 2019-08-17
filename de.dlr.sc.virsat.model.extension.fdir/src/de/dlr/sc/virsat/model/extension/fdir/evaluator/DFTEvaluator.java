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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IQualitativeMetric;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.FaultTreeSymmetryChecker;
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
public class DFTEvaluator extends AFaultTreeEvaluator {
	
	private DFTSemantics defaultSemantics;
	private DFTSemantics poSemantics;
	
	private MarkovAutomaton<DFTState> mc;
	private RecoveryStrategy recoveryStrategy;
	private IMarkovModelChecker markovModelChecker;
	private DFT2MAConverter dft2MAConverter = new DFT2MAConverter();
	private Modularizer modularizer;
	private FaultTreeSymmetryChecker symmetryChecker;
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
		this.modularizer = new Modularizer();
		this.modularizer.setBEOptimization(false);
	}

	@Override
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}

	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, FailNodeProvider failNodeProvider, IMetric... metrics) {
		statistics = new DFTEvaluationStatistics();
		statistics.time = System.currentTimeMillis();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		configureDFT2MAConverter(ftHolder, metrics);
		
		DFTModularization modularization = getModularization(ftHolder, failNodeProvider);
		
		Map<Class<?>, IMetric[]> partitionedMetrics = IMetric.partitionMetrics(metrics, modularization != null);
		IBaseMetric[] baseMetrics = (IBaseMetric[]) partitionedMetrics.get(IBaseMetric.class);
		IDerivedMetric[] derivedMetrics = (IDerivedMetric[]) partitionedMetrics.get(IDerivedMetric.class);
		ModelCheckingResult result = evaluateFaultTree(ftHolder, failNodeProvider, null, modularization, baseMetrics);
		
		composer.derive(result, markovModelChecker.getDelta(), derivedMetrics);
		int steps = (int) (1 / markovModelChecker.getDelta()) + 1;
		result.limitPointMetrics(steps);
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		return result;
	}
	
	/**
	 * Performs a DFT evaluation for the given base metrics with and the given fail criteria
	 * @param ftHolder the fault tree holder
	 * @param failNodeProvider the node fail criteria
	 * @param failLabelProvider the labeling fail criteria
	 * @param modularization optionally a modularization of the dft
	 * @param baseMetrics the metrics to model check
	 * @return the model checking result
	 */
	private ModelCheckingResult evaluateFaultTree(FaultTreeHolder ftHolder, FailNodeProvider failNodeProvider, FailLabelProvider failLabelProvider, DFTModularization modularization, IBaseMetric... baseMetrics) {
		if (modularization != null) {
			Module topLevelModule = modularization.getTopLevelModule();
			Map<Module, ModelCheckingResult> mapModuleToResult = new HashMap<>();
			
			for (Module module : modularization.getModulesToModelCheck()) {
				if (modularization.getMapNodeToRepresentant() != null) {
					FaultTreeNode representant = modularization.getMapNodeToRepresentant().get(module.getRootNode());
					Module representantModule =  modularization.getModule(representant);
					ModelCheckingResult representantResult = mapModuleToResult.get(representantModule);
					if (representantResult == null) {
						representantResult = modelCheck(representantModule.getRootNode(), baseMetrics, failNodeProvider, failLabelProvider);
						mapModuleToResult.put(representantModule, representantResult);
					}
					mapModuleToResult.put(module, representantResult);
				} else {
					ModelCheckingResult moduleResult = modelCheck(module.getRootNode(), baseMetrics, failNodeProvider, failLabelProvider);
					mapModuleToResult.put(module, moduleResult);
				}
			}
			
			composer.composeModuleResults(topLevelModule, modularization, baseMetrics, mapModuleToResult);
			return mapModuleToResult.get(modularization.getTopLevelModule());
		} else {
			return modelCheck(ftHolder.getRoot(), baseMetrics, failNodeProvider, failLabelProvider);
		}
	}
	
	/**
	 * Gets the modularization for the DFT if possible
	 * @param ftHolder the fault tree holder
	 * @param failNodeProvider optionally a fail node criteria
	 * @return the modularization
	 */
	private DFTModularization getModularization(FaultTreeHolder ftHolder, FailNodeProvider failNodeProvider) {
		boolean canModularize = modularizer != null 
				&& ftHolder.getRoot() instanceof Fault
				&& dft2MAConverter.getDftSemantics() != poSemantics
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
	 * Configures the state space generator
	 * @param ftHolder the fault ree to convert
	 * @param metrics the metrics to evaluate
	 */
	private void configureDFT2MAConverter(FaultTreeHolder ftHolder, IMetric[] metrics) {
		dft2MAConverter.setSemantics(chooseSemantics(ftHolder));
		dft2MAConverter.setRecoveryStrategy(recoveryStrategy);
		dft2MAConverter.getDftSemantics().setAllowsRepairEvents(!hasQualitativeMetric(metrics));
		
		if (dft2MAConverter.getDftSemantics() == poSemantics) {
			dft2MAConverter.setSymmetryChecker(null);
			dft2MAConverter.setAllowsDontCareFailing(false);
		} else {
			dft2MAConverter.setSymmetryChecker(new FaultTreeSymmetryChecker());
			dft2MAConverter.setAllowsDontCareFailing(true);
		}
	}
	
	/**
	 * Checks if there is a qualitative metric
	 * @param metrics the metrics
	 * @return true iff at least one metric is qualitative
	 */
	private boolean hasQualitativeMetric(IMetric[] metrics) {
		for (IMetric metric : metrics) {
			if (metric instanceof IQualitativeMetric) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Model checks a tree
	 * @param root the root of the tree
	 * @param metrics the metrics to model check
	 * @param failNodeProvider the nodes that need to fail
	 * @param failLabelProvider the labels that will make a node considered to be failed
	 * @return the result object containing the metrics
	 */
	private ModelCheckingResult modelCheck(FaultTreeNode root, IBaseMetric[] metrics, FailNodeProvider failNodeProvider, FailLabelProvider failLabelProvider) {
		mc = dft2MAConverter.convert(root, failNodeProvider, failLabelProvider);
		ModelCheckingResult result = markovModelChecker.checkModel(mc, metrics);
			
		statistics.stateSpaceGenerationStatistics.compose(dft2MAConverter.getStatistics());
		statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());	
		
		return result;
	}
	
	@Override
	public DFTEvaluationStatistics getStatistics() {
		return statistics;
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
	
	/**
	 * Gets the DFT2MA converter
	 * @return the converter
	 */
	public DFT2MAConverter getDft2MAConverter() {
		return dft2MAConverter;
	}
	
	/**
	 * Configures the modularizer
	 * @param modularizer the modularizer
	 */
	public void setModularizer(Modularizer modularizer) {
		this.modularizer = modularizer;
	}
	
	/**
	 * Configures the symmetry checker
	 * @param symmetryChecker the symmetry checker
	 */
	public void setSymmetryChecker(FaultTreeSymmetryChecker symmetryChecker) {
		this.symmetryChecker = symmetryChecker;
	}
}
