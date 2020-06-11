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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class MetricsStateDeriver<S extends MarkovState> {

	private Map<FailLabelProvider, Map<IMetric, Map<S, Double>>> baseResults;
	private Map<IMetric, Map<S, Double>> derivedResults;
	
	/**
	 * Computes the values on a per state basis for each derived metric
	 * @param baseResults the values of the base metrics
	 * @param metrics the derived mertics to compute
	 * @return a mapping from metric to state values
	 */
	public Map<IMetric, Map<S, Double>> derive(Map<FailLabelProvider, Map<IMetric, Map<S, Double>>> baseResults, IDerivedMetric... metrics) {
		this.baseResults = baseResults;
		derivedResults = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
		
		for (IDerivedMetric derivedMetric : metrics) {
			derivedMetric.accept(metricsVisitor);
		}
		
		return derivedResults;
	}
	
	private IDerivedMetricVisitor metricsVisitor = new IDerivedMetricVisitor() {
		
		@Override
		public void visit(SteadyStateDetectability steadyStateDetectability) {
			Map<IMetric, Map<S, Double>> mapMetricToResultsFailed = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
			Map<IMetric, Map<S, Double>> mapMetricToResultsObserved = baseResults.get(FailLabelProvider.SINGLETON_OBSERVED);
			
			Map<S, Double> ssaResultsFailed = mapMetricToResultsFailed.get(SteadyStateAvailability.SSA);
			Map<S, Double> ssaResultsObserved = mapMetricToResultsObserved.get(SteadyStateAvailability.SSA);
			Map<S, Double> ssdResults = new HashMap<>();
			derivedResults.put(steadyStateDetectability, ssdResults);
			Set<S> states = ssaResultsFailed.keySet();
			
			for (S state : states) {
				double derivedSteadyStateDetectability = steadyStateDetectability.derive(ssaResultsFailed.get(state), ssaResultsObserved.get(state));
				ssdResults.put(state, derivedSteadyStateDetectability);
			}
		}
		
		@Override
		public void visit(MeanTimeToDetection meanTimeToDetectionMetric) {
			Map<IMetric, Map<S, Double>> mapMetricToResultsFailed = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
			Map<IMetric, Map<S, Double>> mapMetricToResultsObserved = baseResults.get(FailLabelProvider.SINGLETON_OBSERVED);
			
			Map<S, Double> mttfResultsFailed = mapMetricToResultsFailed.get(MeanTimeToFailure.MTTF);
			Map<S, Double> mttfResultsObserved = mapMetricToResultsObserved.get(MeanTimeToFailure.MTTF);
			Map<S, Double> mttdResults = new HashMap<>();
			derivedResults.put(meanTimeToDetectionMetric, mttdResults);
			Set<S> states = mttfResultsFailed.keySet();
			
			for (S state : states) {
				double derivedMeanTimeToDetection = meanTimeToDetectionMetric.derive(mttfResultsFailed.get(state), mttfResultsObserved.get(state));
				mttdResults.put(state, derivedMeanTimeToDetection);
			}
		}
		
		@Override
		public void visit(Detectability detectabilityMetric) {
			throw new UnsupportedOperationException("Point Detectability cannot be derived on a per state basis!");
		}
		
		@Override
		public void visit(MeanTimeToFailure mttfMetric) {
			throw new UnsupportedOperationException("Mean Time To Failure cannot be derived on a per state basis!");
		}
	};
	
}
