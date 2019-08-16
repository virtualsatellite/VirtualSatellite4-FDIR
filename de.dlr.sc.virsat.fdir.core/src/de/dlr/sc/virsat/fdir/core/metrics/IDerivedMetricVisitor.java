package de.dlr.sc.virsat.fdir.core.metrics;

public interface IDerivedMetricVisitor {	
	/**
	 * Handle the case of a mean time to failure metric
	 * @param mttfMetric the mttf metric
	 */
	void visit(MTTF mttfMetric);
	
	/**
	 * Handle the case of a mean time to steady state availability metric
	 * @param steadyStateAvailabilityMetric the steadyStateAvailability metric
	 */
	void visit(SteadyStateAvailability steadyStateAvailabilityMetric);
}
