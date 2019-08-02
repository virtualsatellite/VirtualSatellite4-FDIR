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

import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.PointAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;

/**
 * Class for implementing Markov Model Checker Logic
 * @author yoge_re
 *
 */
public class MarkovModelChecker implements IMarkovModelChecker {

	/**
	 * Encodes the transition matrix of the markov chain
	 * @author muel_s8
	 *
	 */
	private class Matrix {
		protected double[] diagonal;
		protected int[][] statePredIndices;
		protected double[][] statePredRates;
		
		/**
		 * Constructor creating the transition matrix for the mc
		 */
		Matrix() {
			int countStates = mc.getStates().size();
			diagonal = new double[countStates];
			statePredIndices = new int[countStates][];
			statePredRates = new double[countStates][];
		}
	}
	
	/**
	 * Creates a transition matrix
	 * @param failStatesAreTerminal whether fail states should be treated as terminal states
	 * @return a transition matrix
	 */
	private Matrix createTransitionMatrix(boolean failStatesAreTerminal) {
		Matrix matrix = new Matrix();
		int countStates = mc.getStates().size();
		
		for (Object event : mc.getEvents()) {
			for (MarkovTransition<? extends MarkovState> transition : mc.getTransitions(event)) {
				int fromIndex = transition.getFrom().getIndex();
				if (!failStatesAreTerminal || !mc.getFinalStates().contains(transition.getFrom())) {
					matrix.diagonal[fromIndex] -= transition.getRate() * delta;
				}
			}
		}
		
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			List<?> transitions = mc.getPredTransitions(state);
			
			matrix.statePredIndices[state.getIndex()] = new int[transitions.size()];
			matrix.statePredRates[state.getIndex()] = new double[transitions.size()];
			for (int j = 0; j < transitions.size(); ++j) {
				@SuppressWarnings("unchecked")
				MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
				if (!failStatesAreTerminal || !mc.getFinalStates().contains(transition.getFrom())) {
					matrix.statePredIndices[state.getIndex()][j] = transition.getFrom().getIndex();
					matrix.statePredRates[state.getIndex()][j] = transition.getRate() * delta;
				}
			}
		}
		
		return matrix;
	}
	
	/**
	 * Creates the iteration matrix for computing the Mean Time To Failure (MTTF) according to 
	 * the Bellman equation:
	 * MTTF(s) = 0 if s is a fail state
	 * MTTF(s) = 1/ExitRate(s) + SUM(s' successor of s: Prob(s, s') * MTTF(s')
	 * @return the matrix representing the fixpoint iteration
	 */
	private Matrix createBellmanMatrix() {
		Matrix matrix = new Matrix();
		int countStates = mc.getStates().size();
			
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			List<?> transitions = mc.getSuccTransitions(state);
			
			matrix.statePredIndices[state.getIndex()] = new int[transitions.size()];
			matrix.statePredRates[state.getIndex()] = new double[transitions.size()];

			if (!mc.getFinalStates().contains(state)) {
				double exitRate = 0;
				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
					exitRate += transition.getRate();
				}
				
				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
					matrix.statePredIndices[state.getIndex()][j] = transition.getTo().getIndex();
					matrix.statePredRates[state.getIndex()][j] = transition.getRate() / exitRate;
				}
			}
		}
		
		return matrix;
	}
	
	/**
	 * Gets the initial MTTF according to the Bellman equations with
	 * MTTF(s) = 0 if s is a fail state and 
	 * MTTF(s) = 1/ExitRate(s) if s is not a fail state
	 * @return the initial probability distribution
	 */
	private double[] getInitialMTTFVector() {
		int countStates = mc.getStates().size();
		double[] inititalVector = new double[countStates];
		
		for (int i = 0; i < countStates; ++i) {
			MarkovState state = mc.getStates().get(i);
			if (!mc.getFinalStates().contains(state)) {
				List<?> transitions = mc.getSuccTransitions(state);
				double exitRate = 0;
				for (int j = 0; j < transitions.size(); ++j) {
					@SuppressWarnings("unchecked")
					MarkovTransition<? extends MarkovState> transition = (MarkovTransition<? extends MarkovState>) transitions.get(j);
					exitRate += transition.getRate();
				}
				inititalVector[i] = 1 / exitRate;
			}
		}
		
		return inititalVector;
	}
	
	/* Parameters */
	
	private double delta;
	private double eps;
	
	/* Transition System */
	
	private MarkovAutomaton<? extends MarkovState> mc;
	private Matrix tm;
	private Matrix tmTerminal;
	private Matrix bellmanMatrix;
	
	/* Buffers */
	
	private double[] probabilityDistribution;
	private double[] resultBuffer;
	
	/* Results */
	private ModelCheckingResult modelCheckingResult;
	private ModelCheckingStatistics statistics;
	
	/**
	 * 
	 * @param delta time slice
	 * @param eps precision
	 */
	public MarkovModelChecker(double delta, double eps) {
		this.delta = delta;
		this.eps = eps;
	}
	
	/**
	 * Does model checking on the markov chain
	 * @param mc Markov Chain
	 * @param metrics metrics
	 * 
	 */
	@Override
	public ModelCheckingResult checkModel(MarkovAutomaton<? extends MarkovState> mc, IMetric... metrics) {
		statistics = new ModelCheckingStatistics();
		statistics.time = System.currentTimeMillis();
		
		this.mc = mc;
		this.modelCheckingResult = new ModelCheckingResult();
		
		for (IMetric metric : metrics) {
			metric.accept(this);
		}
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		
		return modelCheckingResult;
	}
	
	@Override
	public ModelCheckingStatistics getStatistics() {
		return statistics;
	}

	@Override
	public void visit(Reliability reliabilityMetric) {
		if (tmTerminal == null) {
			tmTerminal = createTransitionMatrix(true);
		}
		
		int steps = (int) (reliabilityMetric.getTime() / delta);
		
		probabilityDistribution = getInitialProbabilityDistribution();
		resultBuffer = new double[probabilityDistribution.length];
		
		for (int time = 0; time <= steps; ++time) {
			modelCheckingResult.failRates.add(getFailRate());
			iterate(tmTerminal);
		}	
	}

	@Override
	public void visit(MTTF mttfMetric) {
		if (bellmanMatrix == null) {
			bellmanMatrix = createBellmanMatrix();
		}
		
		double[] baseMTTFs = getInitialMTTFVector();
		probabilityDistribution = getInitialMTTFVector();
		resultBuffer = new double[probabilityDistribution.length];
		double[] res = probabilityDistribution;
		
		boolean convergence = false;
		while (!convergence) {
			multiply(bellmanMatrix, res, resultBuffer);
			
			for (int i = 0; i < baseMTTFs.length; ++i) {
				resultBuffer[i] += baseMTTFs[i];
			}
			
			double change = Math.abs(res[0] - resultBuffer[0]);
			
			if (change < eps || Double.isNaN(change)) {
				probabilityDistribution = resultBuffer;
				convergence = true;
			} else {
				double[] tmp = res;
				res = resultBuffer;
				resultBuffer = tmp;
			}
		}
		
		modelCheckingResult.setMeanTimeToFailure(probabilityDistribution[0]);
	}

	
	@Override
	public void visit(PointAvailability pointAvailabilityMetric) {
		if (tm == null) {
			tm = createTransitionMatrix(false);
		}
		
		int steps = (int) (pointAvailabilityMetric.getTime() / delta);

		probabilityDistribution = getInitialProbabilityDistribution();
		resultBuffer = new double[probabilityDistribution.length];

		for (int time = 0; time <= steps; ++time) {
			modelCheckingResult.pointAvailability.add(1 - getFailRate());
			iterate(tm);
		}
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
		if (tm == null) {
			tm = createTransitionMatrix(false);
		}
		
		probabilityDistribution = getInitialProbabilityDistribution();
		resultBuffer = new double[probabilityDistribution.length];
		double oldUnavailability = getFailRate();
		double difference = 0;
		boolean convergence = false;
		while (!convergence) {
			iterate(tm);
			double newUnavailability = getFailRate();
			difference = Math.abs(newUnavailability - oldUnavailability);
			if (difference < (eps / delta) || Double.isNaN(difference)) {
				convergence = true;
			}
			oldUnavailability = newUnavailability;
		}
		modelCheckingResult.setSteadyStateAvailability(1 - getFailRate());		
	}

	/**
	 * Gets the fail rate at the current iteration
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
	
	/**
	 * Performs one update iteration
	 * @param tm the transition matrix
	 */
	private void iterate(Matrix tm) {
		double[] res = probabilityDistribution;
		probabilityDistribution = new double[probabilityDistribution.length];
		
		double lambda = 1;
		int i = 0;
		boolean convergence = false;
		while (!convergence) {
			for (int j = 0; j < probabilityDistribution.length; ++j) {
				probabilityDistribution[j] += res[j] * lambda;
			}
			
			lambda = lambda / (i + 1);
			double change = lambda * multiply(tm, res, resultBuffer) / delta;
			
			// Swap the discrete time buffers
			double[] tmp = res;
			res = resultBuffer;
			resultBuffer = tmp;
			
			if (change < eps * eps || !Double.isFinite(change)) {
				for (int j = 0; j < probabilityDistribution.length; ++j) {
					probabilityDistribution[j] += res[j] * lambda;
				}
				
				convergence = true;
			} else {
				++i;
			}
		}
	}
	
	/**
	 * Performs a discrete time abstract step.
	 * @param tm the transition matrix
	 * @param vector the current probability distribution
	 * @param result the vector to put the result into
	 * @return squared length of the result vector
	 */
	private double multiply(Matrix tm, double[] vector, double[] result) {
		int countStates = vector.length;
		double res = 0;
		
		for (int i = 0; i < countStates; ++i) {
			result[i] = vector[i] * tm.diagonal[i];
			int[] predIndices = tm.statePredIndices[i];
			double[] predRates = tm.statePredRates[i];
			
			for (int j = 0; j < predIndices.length; ++j) {
				double change = vector[predIndices[j]] * predRates[j];
				res += change * change;
				result[i] += change;
			}
		}
		
		return res;
	}
}
