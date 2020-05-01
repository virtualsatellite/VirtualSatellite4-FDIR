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
public class SPSIterator extends MatrixIterator {
	
	private static final double BIG = Math.pow(10, 100);
	private static final int RHO_FACTOR = 18;
	private static final int TAYLOR_TRIM_FACTOR = 3;
	
	private double maxEntry;
	private double[] vpro;
	private double[] vprotmp;
	private double[] vsum;
	
	private int taylorTrim;
	
	private IMatrix uniformMatrix;
	
	/**
	 * Implementation of a MatrixIterator using custom sparse matrices
	 * 
	 * @param tmTerminal transition matrix
	 * @param probabilityDistribution probability distribution
	 * @param eps epsilon
	 */
	public SPSIterator(IMatrix tmTerminal, double[] probabilityDistribution, double eps) {
		super(tmTerminal, probabilityDistribution, eps);		
		this.maxEntry = initMaxEntry();
		this.vsum = probabilityDistribution;
		this.vpro = new double[probabilityDistribution.length];
		this.vprotmp = new double[probabilityDistribution.length];
		
		initUniformMatrix();
		taylorTrim = findTaylorTrim();
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
		
		for (int j = 1; j <= taylorTrim; j++) {
			uniformMatrix.multiply(vpro, vprotmp);
			
			double[] tmp = vpro;
			vpro = vprotmp;
			vprotmp = tmp;
			
			for (int i = 0; i < vpro.length; i++) {
				vpro[i] = vpro[i] / j;
				vsum[i] = vsum[i] + vpro[i];
			}
			
			b = b * maxEntry / j;
			
			if (b > BIG) {
				for (int i = 0; i < vpro.length; i++) {
					vpro[i] = vpro[i] / b;
					vsum[i] = vsum[i] / b;
				}
				c = c + Math.log(b);
				b = 1;				
			}			
		}
		
		double ecr = Math.exp(c - maxEntry);
		for (int i = 0; i < vsum.length; i++) {
			vsum[i] = vsum[i] * ecr;
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
	 */
	private void initUniformMatrix() {
		uniformMatrix = matrix.copy();
		double[] diag = uniformMatrix.getDiagonal();
						
		for (int i = 0; i < diag.length; i++) {
			diag[i] += maxEntry;
		}
		uniformMatrix.setDiagonal(diag);
	}
	
	/**
	 * @return returns the initial value for Rho
	 */
	private double initMaxEntry() {
		double maxVal = 0;
		double[] diag = matrix.getDiagonal();
		int diagLength = diag.length;
		
		for (int i = 0; i < diagLength; i++) {
			maxVal = Math.max(maxVal, Math.abs(diag[i]));    
		}		
		return maxVal;
	}
}
