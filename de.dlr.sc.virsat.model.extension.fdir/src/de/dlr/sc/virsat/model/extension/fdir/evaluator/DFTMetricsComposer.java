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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IDerivedMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.IQuantitativeMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;

/**
 * Performs composition of metrics calculated for the sub modules of a module
 * @author muel_s8
 *
 */

public class DFTMetricsComposer implements IBaseMetricVisitor, IDerivedMetricVisitor {
	
	private ModelCheckingResult composedResult;
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
	 * @param composedResult the given result
	 * @param metrics the metrics to derive
	 * @param delta 
	 * @return 
	 */
	public void derive(ModelCheckingResult composedResult, double delta, IDerivedMetric... metrics) {
		this.composedResult = composedResult;
		this.delta = delta;
		
		for (IDerivedMetric metric : metrics) {
			metric.accept(this);
		}
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
		IQuantitativeMetric.composeProbabilityCurve(probabilityCurves, composedResult.getFailRates(), k, 1);
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
	public void visit(Availability pointAvailabilityMetric) {
		List<List<Double>> probabilityCurves = subModuleResults.stream().map(result -> result.getAvailability()).collect(Collectors.toList());
		IQuantitativeMetric.composeProbabilityCurve(probabilityCurves, composedResult.getFailRates(), k, -1);
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
}
