package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.List;

public interface IDerivedMetric extends IMetric {
	/**
	 * Either empty if the metric cannot be derived from other metrics,
	 * or otherwise the list of metrics required to derive this metric
	 * @return the metrics this metric is derived from
	 */
	List<IMetric> getDerivedFrom();
	
	/**
	 * Accept a visitor
	 * @param visitor the visitor
	 */
	void accept(IDerivedMetricVisitor visitor);
}
