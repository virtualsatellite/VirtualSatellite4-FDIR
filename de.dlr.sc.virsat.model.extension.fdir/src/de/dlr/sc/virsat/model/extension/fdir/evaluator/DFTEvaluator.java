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
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.PointAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.FaultTreeSymmetryChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
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
public class DFTEvaluator implements IFaultTreeEvaluator {

	public static final int MODULE_SPLIT_SIZE_BES = 20;
	
	private DFTSemantics defaultSemantics;
	private DFTSemantics poSemantics;
	
	private MarkovAutomaton<DFTState> mc;
	private RecoveryStrategy recoveryStrategy;
	private IMarkovModelChecker markovModelChecker;
	private DFT2MAConverter dft2MAConverter = new DFT2MAConverter();
	private Modularizer modularizer;
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
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		statistics = new DFTEvaluationStatistics();
		statistics.time = System.currentTimeMillis();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		dft2MAConverter.setSemantics(chooseSemantics(ftHolder));
		dft2MAConverter.setRecoveryStrategy(recoveryStrategy);
		
		boolean canModularize = modularizer != null 
				&& root instanceof Fault
				&& dft2MAConverter.getDftSemantics() != poSemantics;
		
		Set<Module> modules = null;
		if (canModularize) {
			Fault rootFault = (Fault) root;
			modules = modularizer.getModules(rootFault.getFaultTree());
			canModularize = !modules.isEmpty();
		}
		
		if (canModularize) {
			Entry<IMetric[], IMetric[]> metricSplit = splitMetrics(metrics);
			IMetric[] composableMetrics = metricSplit.getKey();
			IMetric[] unComposableMetrics = metricSplit.getValue();
			
			Module topLevelModule = getModule(modules, root);
			Set<Module> modulesToModelCheck = getModulesToModelCheck(topLevelModule, modules);
			
			IMetric[] modelCheckerMetrics = modulesToModelCheck.size() == 1 ? metrics : composableMetrics;
			Map<Module, ModelCheckingResult> mapModuleToResult = new HashMap<>();
		
			Map<FaultTreeNode, FaultTreeNode> mapNodeToRepresentant = null;
			if (modulesToModelCheck.size() > 1) {
				mapNodeToRepresentant = new HashMap<>();
				FaultTreeSymmetryChecker ftSymmetryChecker = new FaultTreeSymmetryChecker();
				Map<FaultTreeNode, List<FaultTreeNode>> symmetryReduction = ftSymmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
				Map<FaultTreeNode, Set<FaultTreeNode>> symmetryReductionInverted = ftSymmetryChecker.invertSymmetryReduction(symmetryReduction);
						
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
						representantResult = modelCheckModule(representantModule, modelCheckerMetrics);
						mapModuleToResult.put(representantModule, representantResult);
					}
					mapModuleToResult.put(module, representantResult);
				} else {
					mapModuleToResult.put(module, modelCheckModule(module, modelCheckerMetrics));
				}
			}
			
			composeModuleResults(topLevelModule, modules, composableMetrics, mapModuleToResult);
			ModelCheckingResult result = mapModuleToResult.get(topLevelModule);
			
			if (modulesToModelCheck.size() > 1) {
				composer.compose(result, unComposableMetrics);
				result.setMeanTimeToFailure(result.getMeanTimeToFailure() * markovModelChecker.getDelta());
				int steps = (int) (1 / markovModelChecker.getDelta()) + 1;
				result.limitPointMetrics(steps);
			}
			
			statistics.time = System.currentTimeMillis() - statistics.time;
			return result;
		} 
		
		mc = dft2MAConverter.convert(root);
		ModelCheckingResult result = markovModelChecker.checkModel(mc, metrics);
			
		statistics.stateSpaceGenerationStatistics.compose(dft2MAConverter.getStatistics());
		statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());
		statistics.time = System.currentTimeMillis() - statistics.time;
		
		return result;
	}
	
	/**
	 * Composes the model checking results for the leaf modules into the model checking result
	 * of the top level module
	 * @param module the module we want to compute the metrics for
	 * @param modules the sub modules
	 * @param metrics the metrics we want to compute
	 * @param mapModuleToResult a map from module to the already computed results
	 */
	private void composeModuleResults(Module module, Set<Module> modules, IMetric[] metrics, Map<Module, ModelCheckingResult> mapModuleToResult) {
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
			ModelCheckingResult subModuleResult = mapModuleToResult.get(subModule);
			if (subModuleResult == null) {
				composeModuleResults(subModule, modules, metrics, mapModuleToResult);
				subModuleResult = mapModuleToResult.get(subModule);
			}
			subModuleResults.add(subModuleResult);
		}
		
		if (subModuleResults.size() > 1) {
			result = composer.compose(subModuleResults, metrics, module);
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
	private Entry<IMetric[], IMetric[]> splitMetrics(IMetric[] metrics) {
		List<IMetric> allMetrics = Arrays.asList(metrics);
		List<IMetric> composableMetrics = new ArrayList<>();
		List<IMetric> unComposableMetrics = new ArrayList<>();
		
		for (IMetric metric : metrics) {
			if (metric instanceof Reliability) {
				if (!allMetrics.contains(MTTF.MTTF)) {
					composableMetrics.add(metric);
				}
			} else if (metric instanceof PointAvailability) {
				composableMetrics.add(metric);
			} else if (metric instanceof MTTF) {
				composableMetrics.add(new Reliability(Double.POSITIVE_INFINITY));
				unComposableMetrics.add(metric);
			} else {
				unComposableMetrics.add(metric);
			}
		}
		
		IMetric[] composableMetricsArray = new IMetric[composableMetrics.size()];
		IMetric[] unComposableMetricsArray = new IMetric[unComposableMetrics.size()];
		return new SimpleEntry<>(composableMetrics.toArray(composableMetricsArray), unComposableMetrics.toArray(unComposableMetricsArray));
	}
	
	/**
	 * Model checks an inidivudal module
	 * @param module the module to model check
	 * @param metrics the metrics to model check
	 * @return the result object containing the metrics
	 */
	private ModelCheckingResult modelCheckModule(Module module, IMetric[] metrics) {
		mc = dft2MAConverter.convert(module.getRootNode());
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

	@Override
	public Set<Set<BasicEvent>> getMinimumCutSets() {
		// Construct the minimum cut sets as follows:
		// Get all states that are predecessors to a fail state
		// Then take all the memorized basic events from the predecessor state
		// and the basic event leading to the fail state

		Set<Set<BasicEvent>> minimumCutSets = new HashSet<>();

		Set<DFTState> failStates = mc.getFinalStates();
		for (DFTState failState : failStates) {
			List<MarkovTransition<DFTState>> predTransitions = mc.getPredTransitions(failState);
			for (MarkovTransition<DFTState> predTransition : predTransitions) {
				Set<BasicEvent> minimumCutSet = new HashSet<>();
				DFTState predecessor = (DFTState) predTransition.getFrom();
				Object event = predTransition.getEvent();

				minimumCutSet.add((BasicEvent) ((IDFTEvent) event).getNode());
				for (BasicEvent be : predecessor.getFailedBasicEvents()) {
					minimumCutSet.add(be);
				}

				minimumCutSets.add(minimumCutSet);
			}
		}

		return minimumCutSets;
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
}
