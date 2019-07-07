package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit;

import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

public class TimeEvent implements IDFTEvent {

	private double time;
	private State raState;
	
	public TimeEvent(double time, State raState) {
		this.time = time;
		this.raState = raState;
	}
	
	@Override
	public double getRate(ExplicitDFTState state) {
		return time;
	}

	@Override
	public boolean canOccur(ExplicitDFTState state) {
		RecoveryStrategy raStrategy = (RecoveryStrategy) state.getRecoveryStrategy();
		return raStrategy.getCurrentState().equals(raState);
	}

	@Override
	public void execute(ExplicitDFTState state, Set<BasicEvent> orderDependentBasicEvents,
			Set<FaultTreeNode> transientNodes) {
		state.setRecoveryStrategy(state.getRecoveryStrategy().onTime(time));
	}

	@Override
	public FaultTreeNode getNode() {
		return null;
	}

}
