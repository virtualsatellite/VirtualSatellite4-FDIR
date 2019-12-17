package de.dlr.sc.virsat.fdir.core.matrix;

public class BellmanIterator extends MatrixIterator {
	
	private double[] baseMTTFs;
	private double[] result = new double[probabilityDistribution.length];
	private double[] oldProbabilityDistribution;

	public BellmanIterator(IMatrix matrix, double[] probabilityDistribution, double eps) {
		super(matrix, probabilityDistribution, eps);
		baseMTTFs = probabilityDistribution.clone();
	}

	@Override
	public void iterate() {
		oldProbabilityDistribution = probabilityDistribution.clone();
		matrix.multiply(probabilityDistribution, result);
		
		for (int i = 0; i < baseMTTFs.length; ++i) {
			result[i] += baseMTTFs[i];
		}
		probabilityDistribution = result.clone();	
	}
	
	@Override
	public double[] getOldProbabilityDistribution() {
		return oldProbabilityDistribution;
	}
	
	@Override
	public double getChange() {
		return Math.abs(oldProbabilityDistribution[0] - probabilityDistribution[0]);	
	}
}
