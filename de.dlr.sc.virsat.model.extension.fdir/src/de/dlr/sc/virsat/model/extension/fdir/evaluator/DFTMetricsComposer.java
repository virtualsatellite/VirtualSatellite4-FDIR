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
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.IQuantitativeMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.PointAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
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

public class DFTMetricsComposer implements IMetricVisitor {
	
	private ModelCheckingResult composedResult;
	private FaultTreeNode topLevelNode;
	private List<FaultTreeNodePlus> childrenPlus;
	private List<ModelCheckingResult> subModuleResults;
	
	/**
	 * Compose operation for composable metrics. Composes the results of the sub modules.
	 * @param subModuleResults the results of the sub modules
	 * @param metrics the metrics to compose
	 * @param topLevelModule the module
	 * @return the metrics for the module
	 */
	public ModelCheckingResult compose(List<ModelCheckingResult> subModuleResults, IMetric[] metrics, Module topLevelModule) {
		this.topLevelNode = topLevelModule.getRootNode();
		this.childrenPlus = topLevelModule.getModuleRoot().getChildren();
		this.subModuleResults = subModuleResults;
		this.composedResult = new ModelCheckingResult();
		
		for (IMetric metric : metrics) {
			metric.accept(this);
		}
		
		return composedResult;
	}
	
	
	/**
	 * Compose operation of uncomposable metrics. Calculates the metrics from
	 * already calculated metrics in the given result
	 * @param composedResult the given result
	 * @param metrics the metrics to compute
	 * @return 
	 */
	public void compose(ModelCheckingResult composedResult, IMetric[] metrics) {
		this.composedResult = composedResult;
		
		for (IMetric metric : metrics) {
			metric.accept(this);
		}
	}
	
	/**
	 * Gets the number of inputs that have to fail to fail the given node
	 * @param node the node
	 * @param children the children
	 * @return the number of inputs that have to fail, for the node to fail
	 */
	private long getK(FaultTreeNode node, Collection<?> children) {
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
		long k = getK(topLevelNode, childrenPlus);
		
		int countFailRates = 0;
		if (k == 1) {
			countFailRates = Integer.MAX_VALUE;
			for (ModelCheckingResult subModuleResult : subModuleResults) {
				countFailRates = Math.min(countFailRates, subModuleResult.getFailRates().size());
			}
		} else {
			for (ModelCheckingResult subModuleResult : subModuleResults) {
				countFailRates = Math.max(countFailRates, subModuleResult.getFailRates().size());
			}
		}
		
		double[] childFailRates = new double[subModuleResults.size()];
		for (int i = 0; i < countFailRates; ++i) {
			
			for (int j = 0; j < subModuleResults.size(); ++j) {
				ModelCheckingResult subModuleResult = subModuleResults.get(j);
				if (i < subModuleResult.getFailRates().size()) {
					childFailRates[j] = subModuleResult.getFailRates().get(i);
				} else {
					childFailRates[j] = 1;
				}
			}
			
			double composedFailRate = IQuantitativeMetric.composeProbabilities(childFailRates, k);
			composedResult.getFailRates().add(composedFailRate);
			
			if (composedFailRate == 1) {
				break;
			}
		}
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
		composedResult.setMeanTimeToFailure(integral);
	}

	@Override
	public void visit(PointAvailability pointAvailabilityMetric) {
		long k = getK(topLevelNode, childrenPlus);
		
		int countPoints = 0;
		for (ModelCheckingResult subModuleResult : subModuleResults) {
			countPoints = Math.max(countPoints, subModuleResult.getPointAvailability().size());
		}
		
		double[] childPointAvailabilities = new double[subModuleResults.size()];
		for (int i = 0; i < countPoints; ++i) {
			for (int j = 0; j < subModuleResults.size(); ++j) {
				ModelCheckingResult subModuleResult = subModuleResults.get(j);
				if (i < subModuleResult.getFailRates().size()) {
					childPointAvailabilities[j] = subModuleResult.getFailRates().get(i);
				} else {
					childPointAvailabilities[j] = 1;
				}
			}
			
			double composedAvailability = IQuantitativeMetric.composeProbabilities(childPointAvailabilities, k);
			composedResult.getPointAvailability().add(composedAvailability);
		}		
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
		long k = getK(topLevelNode, childrenPlus);
	
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
		// TODO Auto-generated method stub
		
	}
}
