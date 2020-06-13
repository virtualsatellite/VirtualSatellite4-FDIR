package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;

public class SynthesisQuery {
	private Fault fault;
	private IMetric objectiveMetric = MeanTimeToFailure.MTTF;
	
	public SynthesisQuery(Fault fault) {
		this.fault = fault;
	}
	
	public Fault getFault() {
		return fault;
	}
	
	public void setObjectiveMetric(IMetric objectiveMetric) {
		this.objectiveMetric = objectiveMetric;
	}
	
	public IMetric getObjectiveMetric() {
		return objectiveMetric;
	}
}
