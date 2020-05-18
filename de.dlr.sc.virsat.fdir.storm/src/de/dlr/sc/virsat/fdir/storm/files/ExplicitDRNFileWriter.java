/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.files;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;

/**
 * This class converts the model to explicit drn format
 * 
 * @author yoge_re
 *
 */
public class ExplicitDRNFileWriter implements IExplicitFileWriter {

	private MarkovAutomaton<? extends MarkovState> ma;
	private String instanceFilePath;
	private boolean includeRewardModel = false;

	/**
	 * 
	 * @param ma
	 *            Markov Automaton
	 * @param instanceFilePath
	 *            File Path
	 * @param metrics
	 *            system metrics
	 */
	public ExplicitDRNFileWriter(MarkovAutomaton<? extends MarkovState> ma, String instanceFilePath,
			IMetric... metrics) {
		this.ma = ma;
		this.instanceFilePath = instanceFilePath;
		
		for (IMetric metric : metrics) {
			if (metric instanceof SteadyStateAvailability) {
				includeRewardModel = true;
				break;
			}
		}
	}

	@Override
	public void writeFile() throws IOException {
		String rewardModel = "";
		String setOneReward = "";
		String setZeroReward = "";
		
		if (includeRewardModel) {
			rewardModel = "operational";
			setOneReward = " [1]";
			setZeroReward = " [0]";
		}

		FileWriter fileWriter = new FileWriter(instanceFilePath);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println("@type: " + getModelType());
		printWriter.println("@parameters");
		printWriter.println("");
		printWriter.println("@reward_models");
		printWriter.println(rewardModel);
		printWriter.println("@nr_states");
		printWriter.println(ma.getStates().size());
		printWriter.println("@model");
		
		for (MarkovState state : ma.getStates()) {
			boolean isFinalState = ma.getFinalStates().contains(state);
			
			if (state.isMarkovian()) {
				double exitRate = ma.getExitRateForState(state);
				Map<MarkovState, Double> mapTargetStateToRate = new TreeMap<>(MarkovState.MARKOVSTATE_COMPARATOR);
				for (MarkovTransition<? extends MarkovState> transition : ma.getSuccTransitions(state)) {
					MarkovState targetState = transition.getTo();
					mapTargetStateToRate.merge(targetState, transition.getRate(), (r1, r2) -> r1 + r2);
				}
				
				if (isFinalState && ma.getSuccTransitions(state).isEmpty()) {
					printWriter.println("state " + state.getIndex() + " !1.0 " + setZeroReward + getLabel(state));
					printWriter.println("\taction 0" + setZeroReward);
					printWriter.println("\t\t" + state.getIndex() + " : 1.0");
				} else {
					String stateReward = isFinalState ? setZeroReward : setOneReward;
					printWriter.println("state " + state.getIndex() + " !" + exitRate + stateReward + " "
							+ getLabel(state));
					printWriter.println("\taction 0" + setZeroReward);
					for (Entry<MarkovState, Double> entry : mapTargetStateToRate.entrySet()) {
						double prob = entry.getValue() / exitRate;
						printWriter.println("\t\t" + entry.getKey().getIndex() + " : " + prob);
					}
				}
			} else {
				printWriter.println("state " + state.getIndex() + " !0.0 " + getLabel(state));
				int choice = 0;
				for (MarkovTransition<? extends MarkovState> transition : ma.getSuccTransitions(state)) {
					printWriter.println("\taction " + choice + setZeroReward);
					printWriter.println("\t\t" + transition.getTo().getIndex() + " : 1");
					choice++;
				}
			}
		}
		
		printWriter.close();
	}

	/**
	 * 
	 * @return model type
	 */
	private String getModelType() {
		return "Markov Automaton";

	}

	/**
	 * 
	 * @param state
	 *            markov state
	 * @return label
	 */
	private String getLabel(MarkovState state) {
		String label = DEFAULT_STATE;
		if (ma.getFinalStates().contains(state)) {
			label = FAILED_STATE;
		} else if (state.getIndex() == 0) {
			label = INITIAL_STATE;
		}
		return label;

	}
}
