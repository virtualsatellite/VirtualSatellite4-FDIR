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
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFTEvent;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Evaluator for Dynamic Fault Trees, resolving non determinism using a recovery
 * strategy.
 * 
 * @author muel_s8
 *
 */
public class DFTEvaluator implements IFaultTreeEvaluator {

	private DFTSemantics defaultSemantics;
	private DFTSemantics poSemantics;
	
	private MarkovAutomaton<DFTState> mc;
	private RecoveryStrategy recoveryStrategy;
	private IMarkovModelChecker markovModelChecker;
	private DFT2MAConverter dft2MAConverter = new DFT2MAConverter();
	private DFTEvaluationStatistics statistics;

	/**
	 * Constructor using the passed recovery strategy
	 * 
	 * @param defaultSemantics the dft semantics
	 * @param poSemantics the semantics to be used for partial observable fault trees
	 * @param markovModelChecker the model Checker
	 */
	public DFTEvaluator(DFTSemantics defaultSemantics, DFTSemantics poSemantics, IMarkovModelChecker markovModelChecker) {
		this.defaultSemantics = defaultSemantics;
		this.poSemantics = poSemantics;
		this.markovModelChecker = markovModelChecker;
	}

	@Override
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}

	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		statistics = new DFTEvaluationStatistics();
		statistics.time = System.currentTimeMillis();
		
		dft2MAConverter.setSemantics(chooseSemantics(root));
		dft2MAConverter.setRecoveryStrategy(recoveryStrategy);
		mc = dft2MAConverter.convert(root);
		
		ModelCheckingResult result = markovModelChecker.checkModel(mc, metrics);
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		statistics.stateSpaceGenerationStatistics.compose(dft2MAConverter.getStatistics());
		statistics.modelCheckingStatistics.compose(markovModelChecker.getStatistics());
		return result;
	}
	
	/**
	 * Gets the internal statistics of the last call to the evaluation method
	 * @return the statistics of the last call of the evaluation method
	 */
	public DFTEvaluationStatistics getStatistics() {
		return statistics;
	}
	
	/**
	 * Chooses the semantics depending on the type of tree
	 * @param root the root of the tree
	 * @return the semantics required based on the tree type
	 */
	private DFTSemantics chooseSemantics(FaultTreeNode root) {
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		if (ftHolder.isPartialObservable()) {
			return poSemantics;
		} else {
			return defaultSemantics;
		}
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
				DFTState predecessor = (DFTState) predTransition.getFrom();
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
	
	/**
	 * Gets the DFT2MA converter
	 * @return the converter
	 */
	public DFT2MAConverter getDft2MAConverter() {
		return dft2MAConverter;
	}
}
