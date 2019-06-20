/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.recovery.IRecoveryStrategy;

/**
 * Evaluator for Dynamic Fault Trees, resolving non determinism using a recovery
 * strategy.
 * 
 * @author muel_s8
 *
 */
public class DFTEvaluator implements IFaultTreeEvaluator {
	
	private final IDFT2MAConverter dft2MAConverter;

	private MarkovAutomaton<DFTState> mc;
	private IRecoveryStrategy recoveryStrategy;
	private IMarkovModelChecker markovModelChecker;

	/**
	 * Constructor using the passed recovery strategy
	 * 
	 * @param dft2MAConverter
	 *            the converter
	 * @param markovModelChecker
	 *            Model Checker
	 */

	public DFTEvaluator(IDFT2MAConverter dft2MAConverter, IMarkovModelChecker markovModelChecker) {
		this.dft2MAConverter = dft2MAConverter;
		this.markovModelChecker = markovModelChecker;
	}

	@Override
	public void setRecoveryStrategy(IRecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}

	@Override
	public void evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		dft2MAConverter.setRecoveryStrategy(recoveryStrategy);
		mc = dft2MAConverter.convert(root);
		markovModelChecker.checkModel(mc, metrics);
	}

	/**
	 * Converts the markov chain into a .dot format adhering string
	 * 
	 * @return the .dot format string respresenting the markov chain
	 */

	public String toDot() {
		return mc.toDot();
	}

	@Override
	public List<Double> getFailRates() {
		return markovModelChecker.getFailRates();
	}

	@Override
	public double getMeanTimeToFailure() {
		return markovModelChecker.getMeanTimeToFailure();
	}

	@Override
	public List<Double> getPointAvailability() {
		return markovModelChecker.getPointAvailability();
	}

	
	@Override
	public double getSteadyStateAvailability() {
		return markovModelChecker.getSteadyStateAvailability();
	}

	@Override
	public Set<Set<BasicEvent>> getMinimumCutSets() {
		// Construct the minimum cut sets as follows:
		// Get all states that are predecessors to a fail state
		// Then take all the memorized basic events from the predecessor state
		// and the basic event leading to the fail state

		Set<Set<BasicEvent>> minimumCutSets = new HashSet<>();

		Set<DFTState> failStates = mc.getFinalStates();
		for (DFTState failState : failStates) {
			List<MarkovTransition<DFTState>> predTransitions = mc.getPredTransitions(failState);
			for (MarkovTransition<DFTState> predTransition : predTransitions) {
				Set<BasicEvent> minimumCutSet = new HashSet<>();
				ExplicitDFTState predecessor = (ExplicitDFTState) predTransition.getFrom();
				Object event = predTransition.getEvent();

				minimumCutSet.add((BasicEvent) ((IDFTEvent) event).getNode());
				for (BasicEvent be : predecessor.getFailedBasicEvents()) {
					minimumCutSet.add(be);
				}

				minimumCutSets.add(minimumCutSet);
			}
		}

		return minimumCutSets;
	}
	/**
	 * Gets the markov chain
	 * 
	 * @return gets the markov chain
	 */
	public MarkovAutomaton<?> getMc() {
		return mc;
	}
}
