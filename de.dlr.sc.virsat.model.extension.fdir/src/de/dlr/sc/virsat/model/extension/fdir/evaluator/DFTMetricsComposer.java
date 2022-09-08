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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.IQuantitativeMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;

/**
 * Performs composition of metrics calculated for the sub modules of a module
 * @author muel_s8
 *
 */

public class DFTMetricsComposer {
	
	private ModelCheckingResult composedResult;
	private long k;
	private List<ModelCheckingResult> subModuleResults;
	
	/**
	 * Compose operation for composable metrics. Composes the results of the sub modules.
	 * @param subModuleResults the results of the sub modules
	 * @param subMonitor eclipse ui element for progress reporting
	 * @param k the combinatorial threshold
	 * @param metrics the metrics to compose
	 * @return the composed result
	 */
	public ModelCheckingResult compose(List<ModelCheckingResult> subModuleResults, SubMonitor subMonitor, long k, IBaseMetric... metrics) {
		this.k = k;
		this.subModuleResults = subModuleResults;
		this.composedResult = new ModelCheckingResult();
		
		for (IBaseMetric metric : metrics) {
			metric.accept(metricVisitor, subMonitor);
		}
		
		return composedResult;
	}
	
	/**
	 * Gets the number of inputs that have to fail to fail the given node
	 * @param node the node
	 * @param children the children
	 * @return the number of inputs that have to fail, for the node to fail
	 */
	public long getK(FaultTreeNode node, Collection<?> children) {
		long k = children.size();
		if (node instanceof Fault) {
			k = 1;
		} else if (node instanceof VOTE) {
			k = ((VOTE) node).getVotingThreshold();
		}
		
		return k;
	}
	
	/**
	 * Composes the model checking results for the leaf modules into the model checking result
	 * of the top level module
	 * @param module the module we want to compute the metrics for
	 * @param modularization the dft modularization
	 * @param subMonitor eclipse ui element for progress reporting
	 * @param metrics the metrics we want to compute
	 * @param mapModuleToResult a map from module to the already computed results
	 */
	void composeModuleResults(Module module, DFTModularization modularization, SubMonitor subMonitor, IBaseMetric[] metrics, Map<Module, ModelCheckingResult> mapModuleToResult) {
		ModelCheckingResult result = mapModuleToResult.get(module);

		List<Module> subModules = modularization.getSubModules(module);		
		List<ModelCheckingResult> subModuleResults = new ArrayList<>();
		for (Module subModule : subModules) {
			FaultTreeNode representant = modularization.getMapNodeToRepresentant().get(subModule.getRootNode());
			Module representantSubModule = modularization.getModule(representant);
			ModelCheckingResult representantSubModuleResult = mapModuleToResult.get(subModule);
			if (representantSubModuleResult == null) {
				composeModuleResults(representantSubModule, modularization, subMonitor, metrics, mapModuleToResult);
				representantSubModuleResult = mapModuleToResult.get(subModule);
			}
			mapModuleToResult.put(subModule, representantSubModuleResult);
			subModuleResults.add(representantSubModuleResult);
		}
		
		if (subModuleResults.size() > 1) {
			long k = getK(module.getRootNode(), module.getModuleRoot().getChildren());
			result = compose(subModuleResults, subMonitor, k, metrics);
		} else {
			result = subModuleResults.get(0);
		}
		
		mapModuleToResult.put(module, result);
		for (Module subModule : subModules) {
			mapModuleToResult.remove(subModule);
		}
	}
	
	private IBaseMetricVisitor metricVisitor = new IBaseMetricVisitor() {
		@Override
		public void visit(Reliability reliabilityMetric, SubMonitor subMonitor) {
			List<List<Double>> probabilityCurves = subModuleResults.stream().map(result -> result.getFailRates()).collect(Collectors.toList());
			reliabilityMetric.composeProbabilityCurve(probabilityCurves, composedResult.getFailRates(), k, 1);
		}

		@Override
		public void visit(MeanTimeToFailure mttfMetric, SubMonitor subMonitor) {
			throw new UnsupportedOperationException("MTTF is not composable!");
		}

		@Override
		public void visit(Availability availabilityMetric, SubMonitor subMonitor) {
			List<List<Double>> probabilityCurves = subModuleResults.stream().map(result -> result.getAvailability()).collect(Collectors.toList());
			availabilityMetric.composeProbabilityCurve(probabilityCurves, composedResult.getAvailability(), k, -1);
		}

		@Override
		public void visit(SteadyStateAvailability steadyStateAvailabilityMetric, SubMonitor subMonitor) {
			double[] childSteadyStateAvailabilities = new double[subModuleResults.size()];
			
			for (int j = 0; j < subModuleResults.size(); ++j) {
				ModelCheckingResult subModuleResult = subModuleResults.get(j);
				childSteadyStateAvailabilities[j] = subModuleResult.getSteadyStateAvailability();
			}
			
			double composedSteadyStateAvailability = IQuantitativeMetric.composeProbabilities(childSteadyStateAvailabilities, k);
			composedResult.setSteadyStateAvailability(composedSteadyStateAvailability);
		}

		@Override
		public void visit(MinimumCutSet minimumCutSet, SubMonitor subMonitor) {
			if (k == 1) {
				for (ModelCheckingResult subModuleResult : subModuleResults) {
					composedResult.getMinCutSets().addAll(subModuleResult.getMinCutSets());
				}
			} else {
				@SuppressWarnings("unchecked")
				Set<Set<Object>>[] allMinCuts = new HashSet[subModuleResults.size()];
				for (int i = 0; i < subModuleResults.size(); ++i) {
					allMinCuts[i] = subModuleResults.get(i).getMinCutSets();
				}
				
				Set<Set<Object>> productMinCutSets = minimumCutSet.cartesianComposition(allMinCuts);
				composedResult.getMinCutSets().addAll(productMinCutSets);
			}
		}
	};
}
