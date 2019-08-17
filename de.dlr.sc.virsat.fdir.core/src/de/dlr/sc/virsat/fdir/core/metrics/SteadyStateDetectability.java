package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Arrays;
import java.util.List;

public class SteadyStateDetectability implements IDerivedMetric {

	public static final SteadyStateDetectability STEADY_STATE_DETECTABILITY = new SteadyStateDetectability();

	private SteadyStateDetectability() {
		
	}
	
	@Override
	public List<IMetric> getDerivedFrom() {
		return Arrays.asList(SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
	}

	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}
}
