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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.StronglyConnectedComponent;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.StronglyConnectedComponentFinder;
import de.dlr.sc.virsat.fdir.core.matrix.BellmanMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.MatrixFactory;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.LinearProgramIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.MarkovAutomatonValueIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.SSAIterator;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
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

	private MarkovAutomaton<? extends MarkovState> mc;
	private IMatrix tm;
	private IMatrix tmTerminal;
	private BellmanMatrix bellmanMatrixTerminal;

	/* Buffers */

	private double[] probabilityDistribution;

	/* Results */
	private ModelCheckingResult modelCheckingResult;
	private ModelCheckingStatistics statistics;
	
	private MatrixFactory matrixFactory;
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
	public ModelCheckingResult checkModel(MarkovAutomaton<? extends MarkovState> mc, SubMonitor subMonitor, IBaseMetric... metrics) {
		statistics = new ModelCheckingStatistics();
		statistics.time = System.currentTimeMillis();
		subMonitor = SubMonitor.convert(subMonitor);

		tm = null;
		tmTerminal = null;
		bellmanMatrixTerminal = null;

		this.mc = mc;
		this.modelCheckingResult = new ModelCheckingResult();

		for (IBaseMetric metric : metrics) {
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

		if (tmTerminal == null) {
			tmTerminal = matrixFactory.getTransitionMatrix(mc, true, delta);
		}
		
		subMonitor.setTaskName("Running Markov Checker on Model");					
		
		IMatrixIterator mtxIterator = new MarkovAutomatonValueIterator<>(tmTerminal.getIterator(probabilityDistribution, eps), mc);
		
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
	public void visit(MTTF mttfMetric) {
		if (bellmanMatrixTerminal == null) {
			bellmanMatrixTerminal = matrixFactory.getBellmanMatrix(mc, mc.getStates(), mc.getFinalStates(), true);
		}
		
		probabilityDistribution = mc.getNonFailSoujornTimes(); 
		IMatrixIterator mxIterator = new MarkovAutomatonValueIterator<>(bellmanMatrixTerminal.getIterator(probabilityDistribution, eps), mc);
		
		boolean convergence = false;
		while (!convergence) {			
			mxIterator.iterate();			
			double change = mxIterator.getChangeSquared();
			if (change < eps * eps || Double.isNaN(change)) {
				probabilityDistribution = mxIterator.getValues();
				convergence = true;				
				if (Double.isInfinite(mxIterator.getOldValues()[0])) {
					probabilityDistribution[0] = Double.POSITIVE_INFINITY;
				}				
			}			
		}		
		modelCheckingResult.setMeanTimeToFailure(probabilityDistribution[0]);		
	}

	@Override
	public void visit(Availability availabilityMetric, SubMonitor subMonitor) {
		if (tm == null) {
			tm = matrixFactory.getTransitionMatrix(mc, false, delta);
		}
		
		subMonitor.setTaskName("Running Markov Checker on Model");
				
		probabilityDistribution = getInitialProbabilityDistribution();
		IMatrixIterator mtxIterator = new MarkovAutomatonValueIterator<>(tm.getIterator(probabilityDistribution, eps), mc);

		if (Double.isFinite(availabilityMetric.getTime())) {
			int steps = (int) (availabilityMetric.getTime() / delta);
			
			subMonitor.setWorkRemaining(steps);
			
			for (int time = 0; time <= steps; ++time) {
				subMonitor.split(1);
				modelCheckingResult.availability.add(1 - getFailRate());
				mtxIterator.iterate();
			}
		} else {
			final int PROGRESS_COUNT = 100;
			double oldFailRate = getFailRate();
			modelCheckingResult.availability.add(oldFailRate);

			boolean convergence = false;
			while (!convergence) {
				subMonitor.setWorkRemaining(PROGRESS_COUNT).split(1);
				mtxIterator.iterate();
				double newFailRate = getFailRate();
				modelCheckingResult.availability.add(1 - newFailRate);
				double change = Math.abs(newFailRate - oldFailRate);
				oldFailRate = newFailRate;
				double relativeChange = change / newFailRate;
				if (relativeChange < eps || !Double.isFinite(change)) {
					convergence = true;
					subMonitor.split(PROGRESS_COUNT);
				}
			}
		}
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
		StronglyConnectedComponentFinder<? extends MarkovState> sccFinder = new StronglyConnectedComponentFinder<>(mc);
		List<StronglyConnectedComponent> sccs = sccFinder.getStronglyConnectedComponents();
		List<StronglyConnectedComponent> endSCCs = sccFinder.getStronglyConnectedEndComponents(sccs);
		
		Set<MarkovState> endSCCStates = new HashSet<>();
		for (StronglyConnectedComponent endSCC : endSCCs) {
			endSCCStates.addAll(endSCC.getStates());
		}
		
		double[] ssas = new double[mc.getStates().size()];
		for (StronglyConnectedComponent endSCC : endSCCs) {
			IMatrix bellmanMatrixSCC = matrixFactory.getBellmanMatrix(mc, endSCC.getStates(), Collections.emptySet(), true);
			
			double[] baseFailCosts = mc.getFailSoujournTimes(endSCC.getStates());
			double[] baseTotalCosts = mc.getSoujournTimes(endSCC.getStates());
			IMatrixIterator mtxIterator = new MarkovAutomatonValueIterator<>(
					new SSAIterator<>(bellmanMatrixSCC, baseFailCosts, baseTotalCosts), mc, endSCC.getStates(), false
			);
			
			boolean convergence = false;
			while (!convergence) {
				mtxIterator.iterate();
				double change = mtxIterator.getChangeSquared();
				if (change < eps * eps || Double.isNaN(change)) {
					probabilityDistribution = mtxIterator.getValues();
					convergence = true;
					if (!Double.isFinite(mtxIterator.getValues()[0])) {
						probabilityDistribution[0] = 1;
					}
				}
			}
			
			for (MarkovState sccState : endSCC.getStates()) {
				ssas[sccState.getIndex()] += probabilityDistribution[0];
			}
		}
		
		IMatrix transitionMatrixToEndSCCs = matrixFactory.getBellmanMatrix(mc, mc.getStates(), endSCCStates, true);
		LinearProgramIterator lpIterator = new LinearProgramIterator(transitionMatrixToEndSCCs, ssas);
		probabilityDistribution = lpIterator.converge(eps);
		
		double ssa = 1 - probabilityDistribution[0];
		modelCheckingResult.setSteadyStateAvailability(ssa);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void visit(MinimumCutSet minimumCutSet) {
		// Construct the minimum cut sets as follows:
		// MinCuts(s) = UNION_{(s, a, s')} ( {a} \cross MinCuts(s') )
		// MinCuts(f) = \emptyset for any fail state f

		Set<? extends MarkovState> failStates = mc.getFinalStates();
		Queue<MarkovState> toProcess = new LinkedList<>();
		toProcess.addAll(failStates);

		Map<MarkovState, Set<Set<Object>>> mapStateToMinCuts = new HashMap<>();

		while (!toProcess.isEmpty()) {
			MarkovState state = toProcess.poll();

			boolean shouldEnqueuePredecessors = false;
			if (!mc.getFinalStates().contains(state)) {
				// Update the mincuts
				Set<Set<Object>> oldMinCuts = mapStateToMinCuts.get(state);
				Set<Set<Object>> minCuts = new HashSet<>();

				List<?> succTransitions = mc.getSuccTransitions(state);
				for (Object succTransition : succTransitions) {
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) succTransition;
					MarkovState successor = transition.getTo();
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
				List<?> predTransitions = mc.getPredTransitions(state);
				for (Object predTransition : predTransitions) {
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) predTransition;
					MarkovState predecessor = transition.getFrom();
					if (!toProcess.contains(predecessor) && !mc.getFinalStates().contains(predecessor)) {
						toProcess.add(predecessor);
					}
				}
			}
		}

		Set<Set<Object>> minCuts = mapStateToMinCuts.getOrDefault(mc.getStates().get(0), Collections.emptySet());
		modelCheckingResult.getMinCutSets().addAll(minCuts);
	}

	/**
	 * Gets the fail rate at the current iteration
	 * 
	 * @return the fail rate at the current iteration
	 */
	private double getFailRate() {
		double res = 0;

		for (MarkovState state : mc.getStates()) {
			if (mc.getFinalStates().contains(state)) {
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
		double[] inititalVector = new double[mc.getStates().size()];
		inititalVector[0] = 1;
		return inititalVector;
	}

	@Override
	public double getDelta() {
		return delta;
	}
}
