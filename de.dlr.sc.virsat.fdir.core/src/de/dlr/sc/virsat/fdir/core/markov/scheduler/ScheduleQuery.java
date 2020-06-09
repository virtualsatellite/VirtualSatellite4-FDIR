package de.dlr.sc.virsat.fdir.core.markov.scheduler;

import java.util.Collections;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;

public class ScheduleQuery<S extends MarkovState> {
	private MarkovAutomaton<S> ma;
	private S initialState;
	private Map<IMetric, Double> constraints;
	
	public ScheduleQuery(MarkovAutomaton<S> ma, S initialState) {
		this.ma  = ma;
		this.initialState = initialState;
		this.constraints = Collections.emptyMap();
	}
	
	public MarkovAutomaton<S> getMa() {
		return ma;
	}
	
	public S getInitialState() {
		return initialState;
	}
	
	public Map<IMetric, Double> getConstraints() {
		return constraints;
	}
}
