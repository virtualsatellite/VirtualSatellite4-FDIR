package de.dlr.sc.virsat.fdir.core.metrics;

public interface IQuantitativeMetric extends IMetric {
	boolean isProbability();
	
	/**
	 * Computes accumulated poisson binomial probability mass function
	 * @param probabilities the individual event probabilities
	 * @param k the number of events that have to occur
	 * @return the composed event probability
	 */
	static double composeProbabilities(double[] probabilities, long k) {
		double composedProbability = 1;
		if (k == 1) {
			for (double failRate : probabilities) {
				composedProbability *= 1 - failRate;
			}
			composedProbability = 1 - composedProbability;
		} else {
			for (double failRate : probabilities) {
				composedProbability *= failRate;
			}
		} 
		
		// TODO: Else for general k
		
		return composedProbability;
	}
}
