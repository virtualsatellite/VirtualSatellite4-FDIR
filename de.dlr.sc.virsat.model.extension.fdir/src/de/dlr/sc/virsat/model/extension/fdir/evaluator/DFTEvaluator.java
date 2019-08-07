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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.PointAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
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

	private DFTSemantics defaultSemantics;
	private DFTSemantics poSemantics;
	
	private MarkovAutomaton<DFTState> mc;
	private RecoveryStrategy recoveryStrategy;
	private IMarkovModelChecker markovModelChecker;
	private DFT2MAConverter dft2MAConverter = new DFT2MAConverter();
	private Modularizer modularizer = new Modularizer();
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
		this.recoveryStrategy = recoveryStrategy;
	}

	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		statistics = new DFTEvaluationStatistics();
		statistics.time = System.currentTimeMillis();
		
		dft2MAConverter.setSemantics(chooseSemantics(root));
		dft2MAConverter.setRecoveryStrategy(recoveryStrategy);
		
		boolean canModularize = modularizer != null && root instanceof Fault;
		
		if (canModularize) {
			Fault rootFault = (Fault) root;
			Set<Module> modules = modularizer.getModules(rootFault.getFaultTree());
			
			IMetric[] composableMetrics = getComposableMetrics(metrics);
			Module topLevelModule = getTopLevelModule(rootFault, modules);
			Set<Module> modulesToModelCheck = getModulesToModelCheck(topLevelModule, modules);
			
			List<ModelCheckingResult> subModuleResults = new ArrayList<>();
			for (Module module : modulesToModelCheck) {
				subModuleResults.add(modelCheckModule(module, composableMetrics));
			}
			
			if (subModuleResults.size() == 1) {
				statistics.time = System.currentTimeMillis() - statistics.time;
				return subModuleResults.get(0);
			} 
			
			ModelCheckingResult result = composeMetrics(subModuleResults, topLevelModule);
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
	
	private Module getModule(Set<Module> modules, FaultTreeNode node) {
		return modules.stream().filter(module -> module.getRootNode().equals(node)).findAny().get();
	}
	
	private Module getTopLevelModule(Fault rootFault, Set<Module> modules) {
		Module topLevelModule = getModule(modules, rootFault);
		while (topLevelModule.getModuleRoot().getChildren().size() <= 1) {
			List<FaultTreeNodePlus> childrenPlus = topLevelModule.getModuleRoot().getChildren();
			FaultTreeNode node = childrenPlus.get(0).getFaultTreeNode();
			Module subModule = getModule(modules, node);
			if (subModule.getModuleNodes().size() > 1) {
				break;
			} else {
				topLevelModule = subModule;
			}
		}
		
		return topLevelModule;
	}
	
	private Set<Module> getModulesToModelCheck(Module topLevelModule, Set<Module> modules) {
		Set<Module> modulesToModelCheck = new HashSet<>();
		for (FaultTreeNodePlus ftChildPlus : topLevelModule.getModuleRoot().getChildren()) {
			Module subModule = getModule(modules, ftChildPlus.getFaultTreeNode());
			modulesToModelCheck.add(subModule);
		}
		return modulesToModelCheck;
	}
	
	private IMetric[] getComposableMetrics(IMetric[] metrics) {
		List<IMetric> composableMetrics = new ArrayList<>();
		for (IMetric metric : metrics) {
			if (metric instanceof Reliability || metric instanceof PointAvailability) {
				composableMetrics.add(metric);
			}
		}
		
		IMetric[] composableMetricsArray = new IMetric[composableMetrics.size()];
		return composableMetrics.toArray(composableMetricsArray);
	}
	
	private ModelCheckingResult modelCheckModule(Module module, IMetric[] metrics) {
		mc = dft2MAConverter.convert(module.getRootNode());
		ModelCheckingResult result = markovModelChecker.checkModel(mc, metrics);
			
		statistics.stateSpaceGenerationStatistics.compose(dft2MAConverter.getStatistics());
		statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());			
	
		return result;
	}
	
	private ModelCheckingResult composeMetrics(List<ModelCheckingResult> subModuleResults, Module topLevelModule) {
		FaultTreeNode topLevelNode = topLevelModule.getRootNode();
		List<FaultTreeNodePlus> childrenPlus = topLevelModule.getModuleNodes();
		
		ModelCheckingResult anySubModuleResult = subModuleResults.get(0);
		
		ModelCheckingResult composedResult = new ModelCheckingResult();	
		long k = getK(topLevelNode, childrenPlus);
		
		for (int i = 0; i < anySubModuleResult.getFailRates().size(); ++i) {
			List<Double> childFailRates = new ArrayList<>(anySubModuleResult.getFailRates().size());
			
			for (ModelCheckingResult subModuleResult : subModuleResults) {
				childFailRates.add(subModuleResult.getFailRates().get(i));
			}
			
			double composedFailRate = composeProbabilities(childFailRates, k);
			composedResult.getFailRates().add(composedFailRate);
		}
		
		double[] x = new double[composedResult.getFailRates().size()];
		for (int i = 0; i < composedResult.getFailRates().size(); ++i) {
			x[i] = i;
		}
		
		double[] y = new double[composedResult.getFailRates().size()];
		for (int i = 0; i < composedResult.getFailRates().size(); ++i) {
			y[i] = 1 - composedResult.getFailRates().get(i);
		}
		
		UnivariateInterpolator interpolator = new SplineInterpolator();
		UnivariateFunction function = interpolator.interpolate(x, y);
		
		UnivariateIntegrator integrator = new TrapezoidIntegrator();
		double integral = integrator.integrate(TrapezoidIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, function, 0, anySubModuleResult.getFailRates().size() - 1);
		double meanTimeToFailure = markovModelChecker.getDelta() * integral;
		composedResult.setMeanTimeToFailure(meanTimeToFailure);
		
		return composedResult;
	}
	
	private long getK(FaultTreeNode node, Collection<?> children) {
		long k = children.size();
		if (node instanceof Fault) {
			k = 1;
		} else if (node instanceof VOTE) {
			k = ((VOTE) node).getVotingThreshold();
		}
		
		return k;
	}
	
	private double composeProbabilities(List<Double> probabilities, long k) {
		double composedProbability = 1;
		if (k == 1) {
			for (double failRate : probabilities) {
				composedProbability *= 1 - failRate;
			}
			composedProbability = 1 - composedProbability;
		} else {
			for (double failRate : probabilities) {
				composedProbability *= failRate;
			}
		}
		
		return composedProbability;
	}
	
	/**
	 * Gets the internal statistics of the last call to the evaluation method
	 * @return the statistics of the last call of the evaluation method
	 */
	public DFTEvaluationStatistics getStatistics() {
		return statistics;
	}
	
	/**
	 * Chooses the semantics depending on the type of tree
	 * @param root the root of the tree
	 * @return the semantics required based on the tree type
	 */
	private DFTSemantics chooseSemantics(FaultTreeNode root) {
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
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
}
