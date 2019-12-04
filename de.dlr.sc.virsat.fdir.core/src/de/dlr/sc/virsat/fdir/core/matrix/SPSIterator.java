/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix;

/**
 * @author piet_ci
 * 
 * Class representing an Iterator iterating a matrix and probability distribution over time 
 *
 */
public class SPSIterator extends MatrixIterator {
	
	private double rho;
	private final double big;
	private double[] v;
	private double[] vpro;
	private double[] vprotmp;
	private double[] vsum;
	private int m;
	
	private TransitionMatrix p;
	
	/**
	 * Implementation of a MatrixIterator using custom sparse matrices
	 * 
	 * @param tmTerminal transition matrix
	 * @param probabilityDistribution probability distribution
	 * @param delta delta
	 * @param eps epsilon
	 */
	public SPSIterator(TransitionMatrix tmTerminal, double[] probabilityDistribution, double delta, double eps) {
		super(tmTerminal, probabilityDistribution, delta, eps);		
		
		big = Math.pow(10, 100);
	}
	
	/**
	 * @return m
	 */
	private int findm() {
		
		double logEps = Math.log(eps);
		
		double atmp = 1 + (Math.pow((1 - ((18 * rho) / logEps)), 0.5));
	
		
		double btmp = (logEps / 3) * atmp;
		double mtmp = rho - btmp - 1;
		m = (int) (Math.ceil(mtmp));
		return m;
	}

	/**
	 * Performs one update iteration
	 */
	public void iterate() {
		this.rho = initRho();
		initP();
		v = probabilityDistribution.clone();
		m = findm();
		
		
		
		
		double b = calcNorm1();
		double c = 0;
		
		if (b > big) {
			for (int i = 0; i < v.length; i++) {
				v[i] = v[i] / b;
			}			
			c = c + Math.log(b);
			b = 1;
		}
		vsum = v.clone();
		vpro = vsum.clone();
		
		vprotmp = vpro.clone();
		
		int f = 1;
		
		for (int j = 1; j <= m; j++) {
			p.multiply(vpro, vprotmp);
			
			double[] tmp = vpro;
			vpro = vprotmp;
			vprotmp = tmp;
			
			for (int i = 0; i < vpro.length; i++) {
				vpro[i] = vpro[i] / f;
			}
			
			b = b * rho / f;
			
			////
			
			for (int i = 0; i < vsum.length; i++) {
				vsum[i] = vsum[i] + vpro[i];
			}
			
			///
			
			if (b > big) {
				for (int i = 0; i < vpro.length; i++) {
					vpro[i] = vpro[i] / b;					
				}
				
				for (int i = 0; i < vsum.length; i++) {
					vsum[i] = vsum[i] / b;
				}
				c = c + Math.log(b);
				b = 1;				
			}
			
			f = f + 1;		
			
		}
		
		double ecr = Math.exp(c - rho);
		for (int i = 0; i < vsum.length; i++) {
			vsum[i] = vsum[i] * ecr;
		}
		probabilityDistribution = vsum;
	}
	
	/**
	 * @return norm1
	 */
	private double calcNorm1() {
		double norm1 = 0;
		
		for (int i = 0; i < v.length; i++) {
			norm1 += Math.abs(v[i]);
		}
		return norm1;
	}
	
	
	
	/**
	 * 
	 */
	private void initP() {
		p = new TransitionMatrix(((TransitionMatrix) (matrix)).getCountStates());
		
		p.setDiagonal(((TransitionMatrix) (matrix)).getDiagonal());
		p.setStatePredIndices(((TransitionMatrix) (matrix)).getStatePredIndices());
		p.setStatePredRates(((TransitionMatrix) (matrix)).getStatePredRates());
		
		double[] diag = p.getDiagonal();
						
		for (int i = 0; i < diag.length; i++) {
			diag[i] += rho;
		}
	}
	
	/**
	 * @return 0
	 */
	private double initRho() {
		double maxVal = 0;
		double[] diag = ((TransitionMatrix) (matrix)).getDiagonal();
		int diagLength = diag.length;
		
		for (int i = 0; i < diagLength; i++) {
			maxVal = Math.max(maxVal, Math.abs(diag[i]));    
		}		
		return maxVal;
	}
}
