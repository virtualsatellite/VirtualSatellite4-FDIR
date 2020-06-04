package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class ImmediateFaultEvent extends FaultEvent {
	
	private boolean isNegative;
	
	public ImmediateFaultEvent(BasicEvent be, boolean isRepair, FaultTreeHolder ftHolder, DFTStaticAnalysis staticAnalysis, boolean isNegative) {
		super(be, isRepair, ftHolder, staticAnalysis);
		this.isNegative = isNegative;
	}
	
	@Override
	public double getRate(DFTState state) {
		double rate = super.getRate(state);
		if (isNegative) {
			rate = 1 - rate;
		}
		return rate;
	}
	
	@Override
	public String toString() {
		String occurenceType = isNegative ? "Not-" : "";
		return occurenceType + super.toString();
	}
	@Override
	public boolean getIsImmediate() {
		return true;
	}
	
	@Override
	public void execute(DFTState state) {
		if (isNegative) {
			// If this is a non-occurence we must mark the node as permanent
			// to ensure that the immediate event will not trigger again
			state.setFaultTreeNodePermanent(getNode(), true);
		} else {
			super.execute(state);
		}
	}
}
