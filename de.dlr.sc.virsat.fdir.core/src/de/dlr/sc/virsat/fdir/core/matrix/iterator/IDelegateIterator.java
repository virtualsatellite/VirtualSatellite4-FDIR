package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

public interface IDelegateIterator<S extends MarkovState> {
	void delegateProbabilisticUpdate(int index, double value, List<MarkovTransition<S>> transitions);
}
