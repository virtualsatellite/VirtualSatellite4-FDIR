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
	
	/**
	 * Implementation of a MatrixIterator using custom sparse matrices
	 * 
	 * @param tmTerminal transition matrix
	 * @param probabilityDistribution probability distribution
	 * @param eps epsilon
	 */
	public SPSIterator(IMatrix tmTerminal, double[] probabilityDistribution, double eps) {
		super(tmTerminal, probabilityDistribution);	
		this.iteratorParams = new IteratorParams(eps);
		
		this.vsum = probabilityDistribution;
		this.vpro = new double[probabilityDistribution.length];
		this.vprotmp = new double[probabilityDistribution.length];
		this.matrix = initUniformMatrix();
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
