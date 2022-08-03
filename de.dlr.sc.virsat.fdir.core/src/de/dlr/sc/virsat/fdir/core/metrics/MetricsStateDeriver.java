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
import java.util.Map.Entry;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

public class MetricsStateDeriver {

	private Map<FailLabelProvider, Map<IMetric, Map<MarkovState, ?>>> baseResults;
	private Map<IMetric, Map<MarkovState, ?>> derivedResults;
	
	/**
	 * Computes the values on a per state basis for each derived metric
	 * @param baseResults the values of the base metrics
	 * @param metrics the derived mertics to compute
	 * @return a mapping from metric to state values
	 */
	public Map<IMetric, Map<MarkovState, ?>> derive(Map<FailLabelProvider, Map<IMetric, Map<MarkovState, ?>>> baseResults, IDerivedMetric... metrics) {
		this.baseResults = baseResults;
		derivedResults = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
		
		for (IDerivedMetric derivedMetric : metrics) {
			derivedMetric.accept(metricsVisitor);
		}
		
		return derivedResults;
	}
	
	@SuppressWarnings("unchecked")
	private IDerivedMetricVisitor metricsVisitor = new IDerivedMetricVisitor() {
		
		@Override
		public void visit(SteadyStateDetectability steadyStateDetectability) {
			Map<IMetric, Map<MarkovState, ?>> mapMetricToResultsFailed = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
			Map<IMetric, Map<MarkovState, ?>> mapMetricToResultsObserved = baseResults.get(FailLabelProvider.SINGLETON_OBSERVED);
			
			Map<MarkovState, Double> ssaResultsFailed = (Map<MarkovState, Double>) mapMetricToResultsFailed.get(SteadyStateAvailability.SSA);
			Map<MarkovState, Double> ssaResultsObserved = (Map<MarkovState, Double>) mapMetricToResultsObserved.get(SteadyStateAvailability.SSA);
			Map<MarkovState, Double> ssdResults = new HashMap<>();
			derivedResults.put(steadyStateDetectability, ssdResults);
			
			for (Entry<MarkovState, Double> entry : ssaResultsFailed.entrySet()) {
				double derivedSteadyStateDetectability = steadyStateDetectability.derive(entry.getValue(), ssaResultsObserved.get(entry.getKey()));
				ssdResults.put(entry.getKey(), derivedSteadyStateDetectability);
			}
		}
		
		@Override
		public void visit(MeanTimeToDetection meanTimeToDetectionMetric) {
			Map<IMetric, Map<MarkovState, ?>> mapMetricToResultsFailed = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
			Map<IMetric, Map<MarkovState, ?>> mapMetricToResultsObserved = baseResults.get(FailLabelProvider.SINGLETON_OBSERVED);
			
			Map<MarkovState, Double> mttfResultsFailed = (Map<MarkovState, Double>) mapMetricToResultsFailed.get(MeanTimeToFailure.MTTF);
			Map<MarkovState, Double> mttfResultsObserved = (Map<MarkovState, Double>) mapMetricToResultsObserved.get(MeanTimeToFailure.MTTF);
			Map<MarkovState, Double> mttdResults = new HashMap<>();
			derivedResults.put(meanTimeToDetectionMetric, mttdResults);
			
			for (Entry<MarkovState, Double> entry : mttfResultsFailed.entrySet()) {
				double derivedMeanTimeToDetection = meanTimeToDetectionMetric.derive(entry.getValue(), mttfResultsObserved.get(entry.getKey()));
				mttdResults.put(entry.getKey(), derivedMeanTimeToDetection);
			}
		}
		
		@Override
		public void visit(MeanTimeToObservedFailure meanTimeToObservedFailureMetric) {
			Map<IMetric, Map<MarkovState, ?>> mapMetricToResultsObserved = baseResults.get(FailLabelProvider.FAILED_OBSERVED);
			
			Map<MarkovState, Double> mttfResultsObserved = (Map<MarkovState, Double>) mapMetricToResultsObserved.get(MeanTimeToFailure.MTTF);
			Map<MarkovState, Double> mttdResults = new HashMap<>(mttfResultsObserved);
			derivedResults.put(meanTimeToObservedFailureMetric, mttdResults);
		}
		
		@Override
		public void visit(FaultTolerance faultTolerance) {
			Map<IMetric, Map<MarkovState, ?>> mapMetricToResultsFailed = baseResults.get(FailLabelProvider.SINGLETON_FAILED);
			
			Map<MarkovState, Set<Set<Object>>> minCutResultMap =  (Map<MarkovState, Set<Set<Object>>>) mapMetricToResultsFailed.get(MinimumCutSet.MINCUTSET);
			Map<MarkovState, Double> faultToleranceResults = new HashMap<>();
			derivedResults.put(faultTolerance, faultToleranceResults);
			
			for (Entry<MarkovState, Set<Set<Object>>> entry : minCutResultMap.entrySet()) {
				double derivedFaulTolerance = faultTolerance.derive(entry.getValue());
				faultToleranceResults.put(entry.getKey(), derivedFaulTolerance);
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
