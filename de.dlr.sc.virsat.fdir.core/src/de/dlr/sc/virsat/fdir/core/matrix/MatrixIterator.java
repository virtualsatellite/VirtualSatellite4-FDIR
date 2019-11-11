package de.dlr.sc.virsat.fdir.core.matrix;

public class MatrixIterator {

	private TransitionMatrix tmTerminal;
	private double[] probabilityDistribution;
	private double delta;
	private double eps;
	private double[] resultBuffer;

	public MatrixIterator(TransitionMatrix tmTerminal, double[] probabilityDistribution, double delta, double eps) {
		this.tmTerminal = tmTerminal;
		this.probabilityDistribution = probabilityDistribution;
		this.delta = delta;
		this.eps = eps;
		this.resultBuffer = new double[probabilityDistribution.length];
	}
	
	/**
	 * Performs one update iteration
	 * 
	 * @param probabilityDistribution probabilityDistribution
	 */
	public void iterate() {
		double[] res = getProbabilityDistribution();
		probabilityDistribution = new double[getProbabilityDistribution().length];

		double lambda = 1;
		int i = 0;
		boolean convergence = false;
		while (!convergence) {
			for (int j = 0; j < getProbabilityDistribution().length; ++j) {
				getProbabilityDistribution()[j] += res[j] * lambda;
			}

			lambda = lambda / (i + 1);
			double change = lambda * tmTerminal.multiply(res, resultBuffer) / delta;

			// Swap the discrete time buffers
			double[] tmp = res;
			res = resultBuffer;
			resultBuffer = tmp;

			if (change < eps * eps || !Double.isFinite(change)) {
				for (int j = 0; j < getProbabilityDistribution().length; ++j) {
					getProbabilityDistribution()[j] += res[j] * lambda;
				}

				convergence = true;
			} else {
				++i;
			}
		}
	}

	public double[] getProbabilityDistribution() {
		return probabilityDistribution;
	}
}
