package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Collections;
import java.util.List;

public class MinimumCutSet implements IQualitativeMetric {

	public static final MinimumCutSet MINCUTSET = new MinimumCutSet(0);
	
	private int maxSize;
	
	/**
	 * Standard constructor
	 * @param maxSize the maximum mincut set
	 */
	public MinimumCutSet(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public void accept(IMetricVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<IMetric> getDerivedFrom() {
		return Collections.emptyList();
	}
	
	/**
	 * The maximum mincut set
	 * @return the maximum mincut set
	 */
	public int getMaxSize() {
		return maxSize;
	}
}
