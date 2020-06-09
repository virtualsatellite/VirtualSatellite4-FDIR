package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;

public class ModelCheckingQuery {
	private MarkovAutomaton<? extends MarkovState> ma;
	private IBaseMetric[] metrics;
	private List<? extends MarkovState> states;
	
	public ModelCheckingQuery(MarkovAutomaton<? extends MarkovState> ma, IBaseMetric... metrics) {
		this.ma = ma;
		this.metrics = metrics;
		this.states = ma.getStates();
	}
	
	public MarkovAutomaton<? extends MarkovState> getMa() {
		return ma;
	}
	
	public IBaseMetric[] getMetrics() {
		return metrics;
	}
	
	public List<? extends MarkovState> getStates() {
		return states;
	}
	
	public void setStates(List<? extends MarkovState> states) {
		this.states = states;
	}
}
