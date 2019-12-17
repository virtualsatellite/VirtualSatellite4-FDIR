package de.dlr.sc.virsat.fdir.core.matrix;

public class BellmanMatrix extends TransitionMatrix {

	public BellmanMatrix(int countStates) {
		super(countStates);
		}
	
	@Override
	public MatrixIterator getIterator(double[] probabilityDistribution, double eps) {
		return new BellmanIterator(this, probabilityDistribution, eps);
	}

}
