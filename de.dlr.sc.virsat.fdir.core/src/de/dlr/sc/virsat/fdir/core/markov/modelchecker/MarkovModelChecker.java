/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.StronglyConnectedComponent;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.StronglyConnectedComponentFinder;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrixFactory;
import de.dlr.sc.virsat.fdir.core.matrix.MatrixFactory;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;

/**
 * Class for implementing Markov Model Checker Logic
 * @author yoge_re
 *
 */
public class MarkovModelChecker implements IMarkovModelChecker {
	
	/* Parameters */

	private double delta;
	private double eps;

	/* Transition System */

	private IMatrix generatorMatrix;
	private IMatrix generatorMatrixTerminal;
	private IMatrix mttfBellmanMatrix;

	/* Buffers */

	private double[] probabilityDistribution;

	/* Results */
	
	private ModelCheckingResult modelCheckingResult;
	private ModelCheckingStatistics statistics;
	
	private ModelCheckingQuery<? extends MarkovState> modelCheckingQuery;
	private IMatrixFactory matrixFactory;
	/**
	 * 
	 * @param delta time slice
	 * @param eps precision
	 */
	public MarkovModelChecker(double delta, double eps) {
		this.delta = delta;
		this.eps = eps;
		this.matrixFactory = new MatrixFactory();
	}

	/**
	 * Does model checking on the markov chain
	 * @param mc Markov Chain
	 * @param metrics metrics
	 * 
	 */
	@Override
	public ModelCheckingResult checkModel(ModelCheckingQuery<? extends MarkovState> modelCheckingQuery, SubMonitor subMonitor) {
		statistics = new ModelCheckingStatistics();
		statistics.time = System.currentTimeMillis();
		subMonitor = SubMonitor.convert(subMonitor);

		generatorMatrix = null;
		generatorMatrixTerminal = null;
		mttfBellmanMatrix = null;

		this.modelCheckingQuery = modelCheckingQuery;
		this.modelCheckingResult = new ModelCheckingResult();

		for (IBaseMetric metric : modelCheckingQuery.getMetrics()) {
			metric.accept(this, subMonitor);
		}

		statistics.time = System.currentTimeMillis() - statistics.time;

		return modelCheckingResult;
	}

	@Override
	public ModelCheckingStatistics getStatistics() {
		return statistics;
	}

	@Override
	public void visit(Reliability reliabilityMetric, SubMonitor subMonitor) {
		probabilityDistribution = getInitialProbabilityDistribution();

		if (generatorMatrixTerminal == null) {
			generatorMatrixTerminal = matrixFactory.createGeneratorMatrix(modelCheckingQuery.getMa(), modelCheckingQuery.getFailStates(), delta);
		}
		
		subMonitor.setTaskName("Running Markov Checker on Model");					
		
		IMatrixIterator mtxIterator = reliabilityMetric.iterator(generatorMatrixTerminal, modelCheckingQuery.getMa(), probabilityDistribution, eps);
		
		if (Double.isFinite(reliabilityMetric.getTime())) {
			int steps = (int) (reliabilityMetric.getTime() / delta);
			subMonitor.setWorkRemaining(steps);
			
			for (int time = 0; time <= steps; ++time) {
				subMonitor.split(1);
				probabilityDistribution = mtxIterator.getValues();
				modelCheckingResult.failRates.add(getFailRate());
				mtxIterator.iterate();
			}
		} else {
			final int PROGRESS_COUNT = 100;
			
			double oldFailRate = getFailRate();
			modelCheckingResult.failRates.add(oldFailRate);

			boolean convergence = false;
			while (!convergence) {
				subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
				
				mtxIterator.iterate();
				probabilityDistribution = mtxIterator.getValues();
				double newFailRate = getFailRate();
				double change = Math.abs(newFailRate - oldFailRate);
				oldFailRate = newFailRate;
				double relativeChange = change / newFailRate;
											
				if (relativeChange < eps || !Double.isFinite(change) || change == 0) {
					convergence = true;
					subMonitor.split(PROGRESS_COUNT);
				} else {
					modelCheckingResult.failRates.add(newFailRate);
				}
			}
		}
	}
	
	@Override
	public void visit(MeanTimeToFailure mttfMetric) {
		if (mttfBellmanMatrix == null) {
			mttfBellmanMatrix = matrixFactory.createBellmanMatrix(modelCheckingQuery.getMa(), modelCheckingQuery.getStates(), modelCheckingQuery.getFailStates(), true);
		}
		
		IMatrixIterator mxIterator = mttfMetric.iterator(mttfBellmanMatrix, modelCheckingQuery.getMa(), modelCheckingQuery.getStates(), modelCheckingQuery.getFailLabelProvider());
		
		probabilityDistribution = mxIterator.converge(eps);
		if (Double.isInfinite(mxIterator.getOldValues()[0])) {
			probabilityDistribution[0] = Double.POSITIVE_INFINITY;
		}
		
		modelCheckingResult.setMeanTimeToFailure(probabilityDistribution[0]);		
	}

	@Override
	public void visit(Availability availabilityMetric, SubMonitor subMonitor) {
		if (generatorMatrix == null) {
			generatorMatrix = matrixFactory.createGeneratorMatrix(modelCheckingQuery.getMa(), Collections.emptySet(), delta);
		}
		
		subMonitor.setTaskName("Running Markov Checker on Model");
				
		probabilityDistribution = getInitialProbabilityDistribution();
		IMatrixIterator mtxIterator = availabilityMetric.iterator(generatorMatrix, modelCheckingQuery.getMa(), probabilityDistribution, eps);
		
		int steps = (int) (availabilityMetric.getTime() / delta);
		
		subMonitor.setWorkRemaining(steps);
		
		for (int time = 0; time <= steps; ++time) {
			subMonitor.split(1);
			modelCheckingResult.availability.add(1 - getFailRate());
			mtxIterator.iterate();
		}
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
		StronglyConnectedComponentFinder<? extends MarkovState> sccFinder = new StronglyConnectedComponentFinder<>(modelCheckingQuery.getMa());
		List<StronglyConnectedComponent> sccs = sccFinder.getStronglyConnectedComponents();
		List<StronglyConnectedComponent> endSCCs = sccFinder.getStronglyConnectedEndComponents(sccs);
		
		Set<MarkovState> endSCCStates = StronglyConnectedComponent.union(endSCCs);
		double[] ssas = new double[modelCheckingQuery.getMa().getStates().size()];
		
		for (StronglyConnectedComponent endSCC : endSCCs) {
			IMatrix bellmanMatrixSCC = matrixFactory.createBellmanMatrix(modelCheckingQuery.getMa(), endSCC.getStates(), Collections.emptySet(), true);
			IMatrixIterator mtxIterator = steadyStateAvailabilityMetric.iterator(bellmanMatrixSCC, modelCheckingQuery.getMa(), endSCC.getStates(), modelCheckingQuery.getFailLabelProvider());
			probabilityDistribution = mtxIterator.converge(eps);
			if (!Double.isFinite(probabilityDistribution[0])) {
				probabilityDistribution[0] = 1;
			}
			
			for (MarkovState sccState : endSCC.getStates()) {
				ssas[sccState.getIndex()] += probabilityDistribution[0];
			}
		}
		
		IMatrix transitionMatrixToEndSCCs = matrixFactory.createBellmanMatrix(modelCheckingQuery.getMa(), modelCheckingQuery.getStates(), endSCCStates, true);
		IMatrixIterator mtxIterator = steadyStateAvailabilityMetric.iterator(transitionMatrixToEndSCCs, modelCheckingQuery.getMa(), ssas);
		probabilityDistribution = mtxIterator.converge(eps);
		
		double ssa = 1 - probabilityDistribution[0];
		modelCheckingResult.setSteadyStateAvailability(ssa);
	}

	@Override
	public void visit(MinimumCutSet minimumCutSet) {
		// Construct the minimum cut sets as follows:
		// MinCuts(s) = UNION_{(s, a, s')} ( {a} \cross MinCuts(s') )
		// MinCuts(f) = \emptyset for any fail state f
		
		Set<? extends MarkovState> failStates = modelCheckingQuery.getFailStates();
		Queue<MarkovState> toProcess = new LinkedList<>();
		toProcess.addAll(failStates);

		Map<MarkovState, Set<Set<Object>>> mapStateToMinCuts = new HashMap<>();

		while (!toProcess.isEmpty()) {
			MarkovState state = toProcess.poll();

			boolean shouldEnqueuePredecessors = false;
			if (!failStates.contains(state)) {
				// Update the mincuts
				Set<Set<Object>> oldMinCuts = mapStateToMinCuts.get(state);
				Set<Set<Object>> minCuts = new HashSet<>();

				List<?> succTransitions = modelCheckingQuery.getMa().getSuccTransitions(state);
				for (Object succTransition : succTransitions) {
					MarkovTransition<?> transition = (MarkovTransition<?>) succTransition;
					MarkovState successor = (MarkovState) transition.getTo();
					Set<Set<Object>> succMinCuts = mapStateToMinCuts.getOrDefault(successor, Collections.emptySet());

					if (succMinCuts.isEmpty()) {
						Set<Object> minCut = new HashSet<>();
						minCut.add(transition.getEvent());
						minCuts.add(minCut);
					} else {
						for (Set<Object> succMinCut : succMinCuts) {
							if (succMinCut.size() < minimumCutSet.getMaxSize() || minimumCutSet.getMaxSize() == 0) {
								Set<Object> minCut = new HashSet<>(succMinCut);
								minCut.add(transition.getEvent());
								minCuts.add(minCut);
							}
						}
					}

					// Make sure all cuts are mincuts
					Set<Set<Object>> subsumedMinCuts = new HashSet<>();
					for (Set<Object> minCut : minCuts) {
						for (Set<Object> minCutOther : minCuts) {
							if (minCut != minCutOther && minCut.containsAll(minCutOther)) {
								subsumedMinCuts.add(minCut);
							}
						}
					}
					minCuts.removeAll(subsumedMinCuts);
				}

				if (!Objects.equals(oldMinCuts, minCuts)) {
					shouldEnqueuePredecessors = true;
					mapStateToMinCuts.put(state, minCuts);
				}
			} else {
				shouldEnqueuePredecessors = true;
			}

			// Enqueue predecessors if necessary
			if (shouldEnqueuePredecessors) {
				List<?> predTransitions = modelCheckingQuery.getMa().getPredTransitions(state);
				for (Object predTransition : predTransitions) {
					MarkovTransition<?> transition = (MarkovTransition<?>) predTransition;
					MarkovState predecessor = (MarkovState) transition.getFrom();
					if (!toProcess.contains(predecessor) && !failStates.contains(predecessor)) {
						toProcess.add(predecessor);
					}
				}
			}
		}

		Set<Set<Object>> minCuts = mapStateToMinCuts.getOrDefault(modelCheckingQuery.getMa().getStates().get(0), Collections.emptySet());
		modelCheckingResult.getMinCutSets().addAll(minCuts);
	}

	/**
	 * Gets the fail rate at the current iteration
	 * 
	 * @return the fail rate at the current iteration
	 */
	private double getFailRate() {
		double res = 0;

		for (MarkovState state : modelCheckingQuery.getMa().getStates()) {
			if (state.getFailLabels().contains(FailLabel.FAILED)) {
				res += probabilityDistribution[state.getIndex()];
			}
		}

		return res;
	}

	/**
	 * Gets the initial probaility distribution, that is 1 on the initial state
	 * and 0 everywhere else
	 * @return the initial probability distribution
	 */
	private double[] getInitialProbabilityDistribution() {
		double[] inititalVector = new double[modelCheckingQuery.getMa().getStates().size()];
		inititalVector[0] = 1;
		return inititalVector;
	}

	@Override
	public double getDelta() {
		return delta;
	}
	
	public double[] getProbabilityDistribution() {
		return probabilityDistribution;
	}
}
