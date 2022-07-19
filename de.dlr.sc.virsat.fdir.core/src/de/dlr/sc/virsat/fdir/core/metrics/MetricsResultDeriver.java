/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Map;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

public class MetricsResultDeriver {

	private ModelCheckingResult derivedResult;
	private Map<FailLabelProvider, ModelCheckingResult> baseResults;
	private double delta;
	
	/**
	 * Derivation operation of deriavable metrics. Calculates the metrics from
	 * already calculated metrics in the given result
	 * @param baseResults the given results
	 * @param metrics the metrics to derive
	 * @param delta the model checking delta
	 * @return the derived results
	 */
	public ModelCheckingResult derive(Map<FailLabelProvider, ModelCheckingResult> baseResults, double delta, IDerivedMetric... metrics) {
		this.derivedResult = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
		this.baseResults = baseResults;
		this.delta = delta;
		
		for (IDerivedMetric metric : metrics) {
			metric.accept(metricVisitor);
		}
		
		return derivedResult;
	}
	
	private IDerivedMetricVisitor metricVisitor = new IDerivedMetricVisitor() {
		@Override
		public void visit(MeanTimeToFailure mttfMetric) {
			int countFailRates = derivedResult.getFailRates().size();
			
			if (countFailRates == 1) {
				derivedResult.setMeanTimeToFailure(Double.POSITIVE_INFINITY);
				return;
			}
			
			double[] x = new double[countFailRates];
			for (int i = 0; i < countFailRates; ++i) {
				x[i] = i;
			}
			
			double[] y = new double[countFailRates];
			for (int i = 0; i < countFailRates; ++i) {
				y[i] = 1 - derivedResult.getFailRates().get(i);
			}
			
			UnivariateInterpolator interpolator = new SplineInterpolator();
			UnivariateFunction function = interpolator.interpolate(x, y);
			
			UnivariateIntegrator integrator = new SimpsonIntegrator();
			double integral = integrator.integrate(SimpsonIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, function, 0, countFailRates - 1);
			derivedResult.setMeanTimeToFailure(integral * delta);
		}
		
		@Override
		public void visit(Detectability detectabilityMetrc) {
			ModelCheckingResult resultUnobservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
			ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.OBSERVED));
			detectabilityMetrc.derive(resultUnobservedFailure.getAvailability(), resultObservedFailure.getAvailability(), derivedResult.getDetectabiity());
		}
		
		@Override
		public void visit(MeanTimeToDetection meanTimeToDetectionMetric) {
			ModelCheckingResult resultUnobservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
			ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.OBSERVED));
			double derivedMTTD = meanTimeToDetectionMetric.derive(resultUnobservedFailure.getMeanTimeToFailure(), resultObservedFailure.getMeanTimeToFailure());
			derivedResult.setMeanTimeToDetection(derivedMTTD);
		}
		
		@Override
		public void visit(MeanTimeToObservedFailure meanTimeToObservedFailureMetric) {
			ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED, FailLabel.OBSERVED));
			derivedResult.setMeanTimeToObservedFailure(resultObservedFailure.getMeanTimeToFailure());
		}
	
		@Override
		public void visit(SteadyStateDetectability steadyStateDetectability) {
			ModelCheckingResult resultUnobservedFailure = baseResults.get(new FailLabelProvider(FailLabel.FAILED));
			ModelCheckingResult resultObservedFailure = baseResults.get(new FailLabelProvider(FailLabel.OBSERVED));
			double derivedSteadyStateDetectability = steadyStateDetectability.derive(resultUnobservedFailure.getSteadyStateAvailability(), resultObservedFailure.getSteadyStateAvailability());
			derivedResult.setSteadyStateDetectability(derivedSteadyStateDetectability);
		}

		@Override
		public void visit(FaultTolerance faultTolerance) {
			ModelCheckingResult resultMinCutSets = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
			long derivedFaultTolerance = faultTolerance.derive(resultMinCutSets.getMinCutSets());
			derivedResult.setFaultTolerance(derivedFaultTolerance);
		}
	};
}
