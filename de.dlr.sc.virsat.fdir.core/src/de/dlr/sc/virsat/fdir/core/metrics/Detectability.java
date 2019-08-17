package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Arrays;
import java.util.List;

public class Detectability implements IDerivedMetric {

	public static final Detectability UNIT_DETECTABILITY = new Detectability(1);
	
	private double time;
	
	public Detectability(double time) {
		this.time = time;
	}
	
	@Override
	public List<IMetric> getDerivedFrom() {
		return Arrays.asList(new Availability(time));
	}

	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}

}
