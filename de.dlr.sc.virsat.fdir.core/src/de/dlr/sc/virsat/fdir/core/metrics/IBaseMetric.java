package de.dlr.sc.virsat.fdir.core.metrics;

public interface IBaseMetric extends IMetric {
	/**
	 * Accept a visitor
	 * @param visitor the visitor
	 */
	void accept(IBaseMetricVisitor visitor);
}
