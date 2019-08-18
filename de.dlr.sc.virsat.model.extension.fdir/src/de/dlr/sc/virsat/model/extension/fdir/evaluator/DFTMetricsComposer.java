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

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.Detectability;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.IQuantitativeMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToDetection;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeNodePlus;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;

/**
 * Performs composition of metrics calculated for the sub modules of a module
 * @author muel_s8
 *
 */

public class DFTMetricsComposer implements IBaseMetricVisitor, IDerivedMetricVisitor {
	
	private ModelCheckingResult composedResult;
	private Map<FailLabelProvider, ModelCheckingResult> baseResults;
	private long k;
	private List<ModelCheckingResult> subModuleResults;
	private double delta;
	
	/**
	 * Compose operation for composable metrics. Composes the results of the sub modules.
	 * @param subModuleResults the results of the sub modules
	 * @param k the combinatorial threshold
	 * @param metrics the metrics to compose
	 * @return the composed result
	 */
	public ModelCheckingResult compose(List<ModelCheckingResult> subModuleResults, long k, IBaseMetric... metrics) {
		this.k = k;
		this.subModuleResults = subModuleResults;
		this.composedResult = new ModelCheckingResult();
		
		for (IBaseMetric metric : metrics) {
			metric.accept(this);
		}
		
		return composedResult;
	}
	
	
	/**
	 * Derivation operation of deriavable metrics. Calculates the metrics from
	 * already calculated metrics in the given result
	 * @param baseResults the given results
	 * @param metrics the metrics to derive
	 * @param delta the model checking delta
	 * @return the derived results
	 */
	public ModelCheckingResult derive(Map<FailLabelProvider, ModelCheckingResult> baseResults, double delta, IDerivedMetric... metrics) {
		this.composedResult = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
		this.baseResults = baseResults;
		this.delta = delta;
		
		for (IDerivedMetric metric : metrics) {
			metric.accept(this);
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
	
	@Override
	public void visit(Reliability reliabilityMetric) {
		List<List<Double>> probabilityCurves = subModuleResults.stream().map(result -> result.getFailRates()).collect(Collectors.toList());
		reliabilityMetric.composeProbabilityCurve(probabilityCurves, composedResult.getFailRates(), k, 1);
	}

	@Override
	public void visit(MTTF mttfMetric) {
		int countFailRates = composedResult.getFailRates().size();
		double[] x = new double[countFailRates];
		for (int i = 0; i < countFailRates; ++i) {
			x[i] = i;
		}
		
		double[] y = new double[countFailRates];
		for (int i = 0; i < countFailRates; ++i) {
			y[i] = 1 - composedResult.getFailRates().get(i);
		}
		
		UnivariateInterpolator interpolator = new SplineInterpolator();
		UnivariateFunction function = interpolator.interpolate(x, y);
		
		UnivariateIntegrator integrator = new SimpsonIntegrator();
		double integral = integrator.integrate(SimpsonIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, function, 0, countFailRates - 1);
		composedResult.setMeanTimeToFailure(integral * delta);
	}

	@Override
	public void visit(Availability availabilityMetric) {
		List<List<Double>> probabilityCurves = subModuleResults.stream().map(result -> result.getAvailability()).collect(Collectors.toList());
		availabilityMetric.composeProbabilityCurve(probabilityCurves, composedResult.getAvailability(), k, -1);
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
		double[] childSteadyStateAvailabilities = new double[subModuleResults.size()];
		
		for (int j = 0; j < subModuleResults.size(); ++j) {
			ModelCheckingResult subModuleResult = subModuleResults.get(j);
			childSteadyStateAvailabilities[j] = subModuleResult.getSteadyStateAvailability();
		}
		
		double composedSteadyStateAvailability = IQuantitativeMetric.composeProbabilities(childSteadyStateAvailabilities, k);
		composedResult.setSteadyStateAvailability(composedSteadyStateAvailability);
	}


	@Override
	public void visit(MinimumCutSet minimumCutSet) {
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


	@Override
	public void visit(Detectability detectabilityMetrc) {
		ModelCheckingResult resultUnobservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
		ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED, FailLabel.OBSERVED));
		detectabilityMetrc.derive(resultUnobservedFailure.getAvailability(), resultObservedFailure.getAvailability(), composedResult.getDetectabiity());
	}


	@Override
	public void visit(MeanTimeToDetection meanTimeToDetectionMetric) {
		ModelCheckingResult resultUnobservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
		ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED, FailLabel.OBSERVED));
		double derivedMTTD = meanTimeToDetectionMetric.derive(resultUnobservedFailure.getMeanTimeToFailure(), resultObservedFailure.getMeanTimeToFailure());
		composedResult.setMeanTimeToDetection(derivedMTTD);
	}


	@Override
	public void visit(SteadyStateDetectability steadyStateDetectability) {
		ModelCheckingResult resultUnobservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
		ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED, FailLabel.OBSERVED));
		double derivedSteadyStateDetectability = steadyStateDetectability.derive(resultUnobservedFailure.getSteadyStateAvailability(), resultObservedFailure.getSteadyStateAvailability());
		composedResult.setSteadyStateDetectability(derivedSteadyStateDetectability);
	}
	
	/**
	 * Composes the model checking results for the leaf modules into the model checking result
	 * of the top level module
	 * @param module the module we want to compute the metrics for
	 * @param modularization the dft modularization
	 * @param metrics the metrics we want to compute
	 * @param mapModuleToResult a map from module to the already computed results
	 */
	void composeModuleResults(Module module, DFTModularization modularization, IBaseMetric[] metrics, Map<Module, ModelCheckingResult> mapModuleToResult) {
		ModelCheckingResult result = mapModuleToResult.get(module);
		if (result != null) {
			return;
		}
		
		List<FaultTreeNodePlus> children = module.getModuleRoot().getChildren();
		List<Module> subModules = children.stream()
				.map(child -> modularization.getModule(child.getFaultTreeNode()))
				.collect(Collectors.toList());
		List<ModelCheckingResult> subModuleResults = new ArrayList<>();
		for (Module subModule : subModules) {
			if (modularization.getMapNodeToRepresentant() != null) {
				FaultTreeNode representant = modularization.getMapNodeToRepresentant().get(subModule.getRootNode());
				Module representantSubModule = modularization.getModule(representant);
				ModelCheckingResult representantSubModuleResult = mapModuleToResult.get(subModule);
				if (representantSubModuleResult == null) {
					composeModuleResults(representantSubModule, modularization, metrics, mapModuleToResult);
					representantSubModuleResult = mapModuleToResult.get(subModule);
				}
				mapModuleToResult.put(subModule, representantSubModuleResult);
			}
			
			ModelCheckingResult subModuleResult = mapModuleToResult.get(subModule);
			if (subModuleResult == null) {
				composeModuleResults(subModule, modularization, metrics, mapModuleToResult);
				subModuleResult = mapModuleToResult.get(subModule);
			}
			subModuleResults.add(subModuleResult);
		}
		
		if (subModuleResults.size() > 1) {
			long k = getK(module.getRootNode(), module.getModuleRoot().getChildren());
			result = compose(subModuleResults, k, metrics);
		} else {
			result = subModuleResults.get(0);
		}
		
		mapModuleToResult.put(module, result);
		for (Module subModule : subModules) {
			mapModuleToResult.remove(subModule);
		}
	}
}
