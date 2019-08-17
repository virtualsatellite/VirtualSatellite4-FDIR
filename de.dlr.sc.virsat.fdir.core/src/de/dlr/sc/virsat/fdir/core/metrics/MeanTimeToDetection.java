package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Arrays;
import java.util.List;

public class MeanTimeToDetection implements IDerivedMetric {

	public static final MeanTimeToDetection MTTD = new MeanTimeToDetection();

	private MeanTimeToDetection() {
		
	}
	
	@Override
	public List<IMetric> getDerivedFrom() {
		return Arrays.asList(MTTF.MTTF);
	}

	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}
}
