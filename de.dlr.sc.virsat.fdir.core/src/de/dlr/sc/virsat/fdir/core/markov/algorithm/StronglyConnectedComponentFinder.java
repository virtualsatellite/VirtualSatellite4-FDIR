package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.StronglyConnectedComponent;

public class StronglyConnectedComponentFinder<S extends MarkovState> {
	public List<StronglyConnectedComponent<S>> getStronglyConnectedComponents(MarkovAutomaton<S> ma) {
		List<StronglyConnectedComponent<S>> sccs = new ArrayList<>();
		Map<S, Integer> mapStateToIndex = new HashMap<>();
		Map<S, Integer> mapStateToLowLink = new HashMap<>();
		
		int index = 0;
		Stack<S> toProcess = new Stack<>();
		for (S state : ma.getStates()) {
			boolean shouldProcess = mapStateToIndex.get(state) == null;
			if (shouldProcess) {
				mapStateToIndex.put(state, index);
				mapStateToLowLink.put(state, index);
				index++;
			}
		}
		
		return sccs;
	}
	
	public List<StronglyConnectedComponent<S>> getStronglyConnectedEndComponents(MarkovAutomaton<S> ma) {
		List<StronglyConnectedComponent<S>> sccs = getStronglyConnectedComponents(ma);
		List<StronglyConnectedComponent<S>> endSccs = sccs.stream().filter(StronglyConnectedComponent::isEndComponent).collect(Collectors.toList());
		return endSccs;
	}
}
