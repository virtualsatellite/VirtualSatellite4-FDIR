package de.dlr.sc.virsat.fdir.core.markov.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.StronglyConnectedComponent;

/**
 * Computes all strongly connected components on a markov automaton:
 * A node not in any circle forms an SCC by itself
 *
 * Implements Tarjans algorithm.
 *
 * @author muel_s8
 *
 * @param <S> the type of the markov state
 */

public class StronglyConnectedComponentFinder<S extends MarkovState> {
	
	private MarkovAutomaton<S> ma;
	
	private Stack<S> toProcess;
	private int index;
	private Map<S, Integer> mapStateToIndex;
	private Map<S, Integer> mapStateToLowLink;
	private List<StronglyConnectedComponent<S>> sccs;
	
	public List<StronglyConnectedComponent<S>> getStronglyConnectedComponents(MarkovAutomaton<S> ma) {
		this.ma = ma;
		
		sccs = new ArrayList<>();
		mapStateToIndex = new HashMap<>();
		mapStateToLowLink = new HashMap<>();
		
		index = 0;
		toProcess = new Stack<>();
		
		for (S state : ma.getStates()) {
			if (isNewState(state)) {
				handleState(state);
			}
		}
		
		return sccs;
	}
	
	private void handleState(S state) {
		mapStateToIndex.put(state, index);
		mapStateToLowLink.put(state, index);
		index++;
		
		toProcess.push(state);
		
		// Handle the reachable states and check if they already form
		// an SCC by themselves or of they belong to the SCC with root state
		List<MarkovTransition<S>> succs = ma.getSuccTransitions(state);
		for (MarkovTransition<S> succTransition : succs) {
			S succ = succTransition.getTo();
			if (isNewState(succ)) {
				handleState(succ);
				int newLowLink = Math.min(mapStateToLowLink.get(state), mapStateToLowLink.get(succ));
				mapStateToLowLink.put(state, newLowLink);
			} else if (toProcess.contains(state)) {
				// 
				int newLowLink = Math.min(mapStateToLowLink.get(state), mapStateToIndex.get(succ));
				mapStateToLowLink.put(state, newLowLink);
			}
		}
		
		// If state is an SCC root, then generate a new SCC
		if (mapStateToLowLink.get(state) == mapStateToIndex.get(state)) {
			StronglyConnectedComponent<S> scc = new StronglyConnectedComponent<>(ma);
			sccs.add(scc);
			
			// Backtrack over the indexed tree to grab all members of the scc
			S sccMember = null;
			while (sccMember != state) {
				sccMember = toProcess.pop();
				scc.getStates().add(sccMember);
			}
		}
	}
	
	private boolean isNewState(S state) {
		return mapStateToIndex.get(state) == null;
	}
	
	public List<StronglyConnectedComponent<S>> getStronglyConnectedEndComponents(MarkovAutomaton<S> ma) {
		List<StronglyConnectedComponent<S>> sccs = getStronglyConnectedComponents(ma);
		List<StronglyConnectedComponent<S>> endSccs = sccs.stream().filter(StronglyConnectedComponent::isEndComponent).collect(Collectors.toList());
		return endSccs;
	}
}
