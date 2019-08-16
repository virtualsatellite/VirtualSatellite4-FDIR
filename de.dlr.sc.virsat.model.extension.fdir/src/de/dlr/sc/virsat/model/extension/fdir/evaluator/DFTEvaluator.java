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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

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
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeNodePlus;
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

	public static final int MODULE_SPLIT_SIZE_BES = 20;
	
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
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, FailLabelProvider failLabelProvider, IMetric... metrics) {
		statistics = new DFTEvaluationStatistics();
		statistics.time = System.currentTimeMillis();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
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
		
		boolean canModularize = modularizer != null 
				&& root instanceof Fault
				&& dft2MAConverter.getDftSemantics() != poSemantics
				&& failLabelProvider == null;
		
		Set<Module> modules = null;
		if (canModularize) {
			Fault rootFault = (Fault) root;
			modules = modularizer.getModules(rootFault.getFaultTree());
			canModularize = !modules.isEmpty();
		}
		
		if (canModularize) {
			Entry<IBaseMetric[], IDerivedMetric[]> metricSplit = splitMetrics(metrics);
			IBaseMetric[] composableMetrics = metricSplit.getKey();
			IDerivedMetric[] derivedMetrics = metricSplit.getValue();
			
			Module topLevelModule = getModule(modules, root);
			Set<Module> modulesToModelCheck = getModulesToModelCheck(topLevelModule, modules);
			
			IMetric[] modelCheckerMetrics = modulesToModelCheck.size() == 1 ? metrics : composableMetrics;
			Map<Module, ModelCheckingResult> mapModuleToResult = new HashMap<>();
		
			Map<FaultTreeNode, FaultTreeNode> mapNodeToRepresentant = null;
			if (modulesToModelCheck.size() > 1 && symmetryChecker != null) {
				mapNodeToRepresentant = new HashMap<>();
				Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
				Map<FaultTreeNode, Set<FaultTreeNode>> symmetryReductionInverted = symmetryChecker.invertSymmetryReduction(symmetryReduction);
						
				for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : symmetryReduction.entrySet()) {
					if (symmetryReductionInverted.get(entry.getKey()).isEmpty()) {
						mapNodeToRepresentant.put(entry.getKey(), entry.getKey());
						for (FaultTreeNode biggerNode : entry.getValue()) {
							mapNodeToRepresentant.put(biggerNode, entry.getKey());
						}
					}
				}
			}
			
			for (Module module : modulesToModelCheck) {
				if (mapNodeToRepresentant != null) {
					FaultTreeNode representant = mapNodeToRepresentant.get(module.getRootNode());
					Module representantModule = getModule(modules, representant);
					ModelCheckingResult representantResult = mapModuleToResult.get(representantModule);
					if (representantResult == null) {
						representantResult = modelCheckModule(representantModule, modelCheckerMetrics, failLabelProvider);
						mapModuleToResult.put(representantModule, representantResult);
					}
					mapModuleToResult.put(module, representantResult);
				} else {
					mapModuleToResult.put(module, modelCheckModule(module, modelCheckerMetrics, failLabelProvider));
				}
			}
			
			composeModuleResults(topLevelModule, modules, composableMetrics, mapModuleToResult, mapNodeToRepresentant);
			ModelCheckingResult result = mapModuleToResult.get(topLevelModule);
			
			if (modulesToModelCheck.size() > 1) {
				composer.derive(result, derivedMetrics);
				result.setMeanTimeToFailure(result.getMeanTimeToFailure() * markovModelChecker.getDelta());
				int steps = (int) (1 / markovModelChecker.getDelta()) + 1;
				result.limitPointMetrics(steps);
			}
			
			statistics.time = System.currentTimeMillis() - statistics.time;
			return result;
		} 
		
		mc = dft2MAConverter.convert(root, failLabelProvider);
		ModelCheckingResult result = markovModelChecker.checkModel(mc, metrics);
			
		statistics.stateSpaceGenerationStatistics.compose(dft2MAConverter.getStatistics());
		statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());
		statistics.time = System.currentTimeMillis() - statistics.time;
		
		return result;
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
	 * Composes the model checking results for the leaf modules into the model checking result
	 * of the top level module
	 * @param module the module we want to compute the metrics for
	 * @param modules the sub modules
	 * @param metrics the metrics we want to compute
	 * @param mapModuleToResult a map from module to the already computed results
	 * @param mapNodeToRepresentant 
	 */
	private void composeModuleResults(Module module, Set<Module> modules, IMetric[] metrics, Map<Module, ModelCheckingResult> mapModuleToResult, Map<FaultTreeNode, FaultTreeNode> mapNodeToRepresentant) {
		ModelCheckingResult result = mapModuleToResult.get(module);
		if (result != null) {
			return;
		}
		
		List<FaultTreeNodePlus> children = module.getModuleRoot().getChildren();
		List<Module> subModules = children.stream()
				.map(child -> getModule(modules, child.getFaultTreeNode()))
				.collect(Collectors.toList());
		List<ModelCheckingResult> subModuleResults = new ArrayList<>();
		for (Module subModule : subModules) {
			if (mapNodeToRepresentant != null) {
				FaultTreeNode representant = mapNodeToRepresentant.get(subModule.getRootNode());
				Module representantSubModule = getModule(modules, representant);
				ModelCheckingResult representantSubModuleResult = mapModuleToResult.get(subModule);
				if (representantSubModuleResult == null) {
					composeModuleResults(representantSubModule, modules, metrics, mapModuleToResult, mapNodeToRepresentant);
					representantSubModuleResult = mapModuleToResult.get(subModule);
				}
				mapModuleToResult.put(subModule, representantSubModuleResult);
			}
			
			ModelCheckingResult subModuleResult = mapModuleToResult.get(subModule);
			if (subModuleResult == null) {
				composeModuleResults(subModule, modules, metrics, mapModuleToResult, mapNodeToRepresentant);
				subModuleResult = mapModuleToResult.get(subModule);
			}
			subModuleResults.add(subModuleResult);
		}
		
		if (subModuleResults.size() > 1) {
			long k = composer.getK(module.getRootNode(), module.getModuleRoot().getChildren());
			result = composer.compose(subModuleResults, k, metrics);
		} else {
			result = subModuleResults.get(0);
		}
		
		mapModuleToResult.put(module, result);
		for (Module subModule : subModules) {
			mapModuleToResult.remove(subModule);
		}
	}

	/**
	 * Gets the module for a given fault tree node
	 * @param modules the modules
	 * @param node a fault tree node
	 * @return the module for the fault tree node, or null of no such module exists
	 */
	private Module getModule(Set<Module> modules, FaultTreeNode node) {
		return modules.stream().filter(module -> module.getRootNode().equals(node)).findAny().orElse(null);
	}
	
	/**
	 * Counts the total number of basic events in a module
	 * including all of its sub modules
	 * @param modules all modules
	 * @param module the module we wish to know the total be count for
	 * @return the total number of basic events in a module including all of its sub modules
	 */
	private int getTotalCountBasicEvents(Set<Module> modules, Module module) {
		Queue<Module> toProcess = new LinkedList<>();
		toProcess.add(module);
		
		int totalCountBEs = 0;
		
		while (!toProcess.isEmpty()) {
			Module subModule = toProcess.poll();
			for (FaultTreeNodePlus nodePlus : subModule.getModuleNodes()) {
				if (nodePlus.getFaultTreeNode() instanceof BasicEvent) {
					totalCountBEs++;
				}
				
				for (FaultTreeNodePlus childPlus : nodePlus.getChildren()) {
					Module subSubModule = getModule(modules, childPlus.getFaultTreeNode());
					if (subSubModule != null && subSubModule != subModule) {
						toProcess.add(subSubModule);
					}
				}
			}
		}
		
		return totalCountBEs;
	}
	
	/**
	 * Determines the leaf modules that we should model check
	 * @param topLevelModule the top level module
	 * @param modules all modules
	 * @return a set of modules that should be model checked
	 */
	private Set<Module> getModulesToModelCheck(Module topLevelModule, Set<Module> modules) {
		Set<Module> modulesToModelCheck = new HashSet<>();		
		
		Queue<Module> toProcess = new LinkedList<>();
		toProcess.add(topLevelModule);
		
		while (!toProcess.isEmpty()) {
			Module module = toProcess.poll();
			
			boolean shouldModelCheck = module.getModuleNodes().size() > 1;
			if (!shouldModelCheck) {
				FaultTreeNode moduleRoot = module.getModuleNodes().get(0).getFaultTreeNode();
				
				if (moduleRoot instanceof Fault) {
					shouldModelCheck = !((Fault) moduleRoot).getBasicEvents().isEmpty();
				} else if (moduleRoot instanceof VOTE) {
					long votingThreshold = ((VOTE) moduleRoot).getVotingThreshold();
					shouldModelCheck = votingThreshold != 1 && votingThreshold != module.getModuleRoot().getChildren().size();
				} else {
					shouldModelCheck = false;
				}
			}
			
			if (!shouldModelCheck) {
				shouldModelCheck = getTotalCountBasicEvents(modules, module) < MODULE_SPLIT_SIZE_BES;
			}
			
			if (shouldModelCheck) {
				modulesToModelCheck.add(module);
			} else {
				for (FaultTreeNodePlus ftChildPlus : module.getModuleRoot().getChildren()) {
					Module subModule = getModule(modules, ftChildPlus.getFaultTreeNode());
					toProcess.add(subModule);
				}
			}
		}
		
		return modulesToModelCheck;
	}
	
	/**
	 * Splits the given set of metrics into a set of composable and uncomposable metrics.
	 * Also, if necessary, adds new metrics that are required to perform the composition.
	 * @param metrics the original metrics
	 * @return a pair of composable and uncomposable metric sets
	 */
	private Entry<IBaseMetric[], IDerivedMetric[]> splitMetrics(IMetric[] metrics) {
		List<IBaseMetric> composableMetrics = new ArrayList<>();
		List<IDerivedMetric> derivedMetrics = new ArrayList<>();
		
		Queue<IMetric> toProcess = new LinkedList<>(Arrays.asList(metrics));
		
		while (!toProcess.isEmpty()) {
			IMetric metric = toProcess.poll();
			if (metric instanceof IDerivedMetric) {
				IDerivedMetric derivedMetric = (IDerivedMetric) metric;
				toProcess.addAll(derivedMetric.getDerivedFrom());
				derivedMetrics.add(derivedMetric);
				
				composableMetrics = composableMetrics.stream()
						.filter(composableMetric -> !derivedMetric.getDerivedFrom().stream().anyMatch(other -> other.getClass().equals(composableMetric.getClass())))
						.collect(Collectors.toList());
			} else if (metric instanceof IBaseMetric) {
				composableMetrics.add((IBaseMetric) metric);
			}
		}
		
		IBaseMetric[] composableMetricsArray = new IBaseMetric[composableMetrics.size()];
		IDerivedMetric[] derivedMetricsArray = new IDerivedMetric[derivedMetrics.size()];
		return new SimpleEntry<>(composableMetrics.toArray(composableMetricsArray), derivedMetrics.toArray(derivedMetricsArray));
	}
	
	/**
	 * Model checks an inidivudal module
	 * @param module the module to model check
	 * @param metrics the metrics to model check
	 * @param failLabelProvider 
	 * @return the result object containing the metrics
	 */
	private ModelCheckingResult modelCheckModule(Module module, IBaseMetric[] metrics, FailLabelProvider failLabelProvider) {
		mc = dft2MAConverter.convert(module.getRootNode(), failLabelProvider);
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
