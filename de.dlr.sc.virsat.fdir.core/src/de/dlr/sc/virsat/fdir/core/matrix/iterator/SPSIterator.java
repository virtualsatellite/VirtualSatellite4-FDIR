/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import java.util.ArrayList;
import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;

/**
 * @author piet_ci
 * 
 * Class representing an Iterator iterating a matrix and probability distribution over time
 * 
 * This iterator implementation uses the Single Positive Series (SPS) - Algorithm for the sake of 
 * good numerical stability at reasonable speeds as described in: 
 * 
 * 	C. Sherlock. Simple, fast and accurate evaluation of the action of the exponential 
 * 	of a rate matrix on a probability vector. In: 1809.07110, arXiv, 2018. 
 *
 */
public class SPSIterator extends AMatrixIterator {
	
	private static final double BIG = Math.pow(10, 100);
	private static final int RHO_FACTOR = 18;
	private static final int TAYLOR_TRIM_FACTOR = 3;
	
	private IteratorParams iteratorParams;
	private double[] vpro;
	private double[] vprotmp;
	private double[] vsum;
	private List<MarkovState> probabilisticStates;
	private MarkovAutomaton<? extends MarkovState> ma;
	
	/**
	 * Implementation of a MatrixIterator using custom sparse matrices
	 * 
	 * @param generatorMatrix the generator matrix of a markov chain
	 * @param probabilityDistribution the initial probability distribution
	 * @param eps the precision epsilon
	 */
	public SPSIterator(IMatrix generatorMatrix, double[] probabilityDistribution, MarkovAutomaton<? extends MarkovState> ma, double eps) {
		super(generatorMatrix, probabilityDistribution);	
		this.iteratorParams = new IteratorParams(eps);
		
		this.vsum = probabilityDistribution;
		this.vpro = new double[probabilityDistribution.length];
		this.vprotmp = new double[probabilityDistribution.length];
		this.matrix = initUniformMatrix();
		this.ma = ma;
		
		probabilisticStates = new ArrayList<>();
		for (MarkovState state : ma.getStates()) {
			if (state.isProbabilisic()) {
				probabilisticStates.add(state);
			}
		}
	}

	/**
	 * Performs one update iteration
	 */
	public void iterate() {
		double b = calcManhattanNorm();
		double c = 0;
		
		if (b > BIG) {
			for (int i = 0; i < vsum.length; i++) {
				vsum[i] = vsum[i] / b;
			}
			c = c + Math.log(b);
			b = 1;
		}
		
		for (int i = 0; i < vsum.length; ++i) {
			vpro[i] = vsum[i];
		}
		
		for (int j = 1; j <= iteratorParams.taylorTrim; j++) {
			matrix.multiply(vpro, vprotmp);
			
			double[] tmp = vpro;
			vpro = vprotmp;
			vprotmp = tmp;
			
			probabilisticIteration();
			
			for (int i = 0; i < vpro.length; i++) {
				vpro[i] = vpro[i] / j;
				vsum[i] = vsum[i] + vpro[i];
			}
			
			b = b * iteratorParams.maxEntry / j;
			
			if (b > BIG) {
				for (int i = 0; i < vpro.length; i++) {
					vpro[i] = vpro[i] / b;
					vsum[i] = vsum[i] / b;
				}
				c = c + Math.log(b);
				b = 1;
			}
		}
		
		double ecr = Math.exp(c - iteratorParams.maxEntry);
		
		for (int i = 0; i < vsum.length; i++) {
			vsum[i] = vsum[i] * ecr;
		}
		
		// Ensure that the total probability mass is always 1 also after numerical instabilites
		double probabilityMass = calcManhattanNorm();
		for (int i = 0; i < vsum.length; ++i) {
			vsum[i] /= probabilityMass;
		}
	}
	
	/**
	 * Propagate immediate probabilistic mass as far as possible
	 */
	private void probabilisticIteration() {
		boolean immediateTransfer = true;
		while (immediateTransfer) {
			immediateTransfer = false;
			for (MarkovState probabilisticState : probabilisticStates) {
				int indexFrom = probabilisticState.getIndex();
				List<?> succTransitions = ma.getSuccTransitions(probabilisticState);
				for (Object succTransition : succTransitions) {
					if (vpro[indexFrom] != 0) {
						MarkovTransition<?> markovTransition = (MarkovTransition<?>) succTransition;
						MarkovState stateTo = (MarkovState) markovTransition.getTo();
						int indexTo = stateTo.getIndex();
						vpro[indexTo] += vpro[indexFrom] * markovTransition.getRate();
						immediateTransfer = true;
					}
				}
				if (succTransitions.size() > 0) {
					vpro[indexFrom] = 0;
				}
			}			
		}
	}

	/**
	 * @return returns the Manhattan Norm of vector
	 */
	private double calcManhattanNorm() {
		double norm1 = 0;
		
		for (int i = 0; i < vsum.length; i++) {
			norm1 += Math.abs(vsum[i]);
		}
		return norm1;
	}	
	
	/**
	 * Initializes the Uniform Matrix
	 * @return 
	 */
	private IMatrix initUniformMatrix() {
		IMatrix uniformMatrix = matrix.copy();
						
		for (int i = 0; i < uniformMatrix.size(); i++) {
			uniformMatrix.setValue(i, i, uniformMatrix.getValue(i,  i) + iteratorParams.maxEntry);
		}
		return uniformMatrix;
	}
	
	private class IteratorParams {
		private double maxEntry;
		private int taylorTrim;
		protected double eps;
		
		IteratorParams(double eps) {
			this.eps = eps;
			this.maxEntry = initMaxEntry();
			this.taylorTrim = findTaylorTrim();
		}
		
		/**
		 * @return returns the initial value for Rho
		 */
		private double initMaxEntry() {
			double maxVal = 0;
			for (int i = 0; i < matrix.size(); i++) {
				maxVal = Math.max(maxVal, Math.abs(matrix.getValue(i, i)));
			}		
			return maxVal;
		}
		
		/**
		 * @return returns truncation point m for trimmed Taylor series
		 */
		private int findTaylorTrim() {		
			double logEps = Math.log(eps);		
			double atmp = 1 + (Math.sqrt((1 - ((RHO_FACTOR * maxEntry) / logEps))));			
			double btmp = (logEps / TAYLOR_TRIM_FACTOR) * atmp;
			double mtmp = maxEntry - btmp - 1;
			taylorTrim = (int) (Math.ceil(mtmp));
			return taylorTrim;
		}
	}
}
