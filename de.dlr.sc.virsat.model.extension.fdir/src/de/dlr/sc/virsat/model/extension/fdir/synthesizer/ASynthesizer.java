/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.util.IStatistics;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ARecoveryAutomatonMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;

/**
 * Abstract class providing some default implementations for the ISynthesizer interface.
 * @author muel_s8
 *
 */

public abstract class ASynthesizer implements ISynthesizer {

	protected ARecoveryAutomatonMinimizer minimizer = ComposedMinimizer.createDefaultMinimizer();
	
	protected Concept concept;
	protected SynthesisStatistics statistics;
	
	@Override
	public RecoveryAutomaton synthesize(SynthesisQuery synthesisQuery, SubMonitor subMonitor) {
		statistics = new SynthesisStatistics();
		long startTime = System.currentTimeMillis();
		statistics.time = IStatistics.TIMEOUT;
		statistics.countModules = 1;
		
		int steps = 1 + (minimizer != null ? 1 : 0);
		subMonitor = SubMonitor.convert(subMonitor, steps);
		
		FaultTreeNode root  = synthesisQuery.getFTHolder().getRoot();
		concept = root.getConcept();
		
		RecoveryAutomaton synthesizedRA = convertToRecoveryAutomaton(root, subMonitor.split(1));
		
		if (minimizer != null) {
			minimizer.minimize(synthesizedRA, root, subMonitor.split(1));
			statistics.minimizationStatistics.compose(minimizer.getStatistics());
		}
		
		statistics.maxModuleRaSize = synthesizedRA.getStates().size();
		statistics.time = System.currentTimeMillis() - startTime;
		return synthesizedRA;
	}

	/**
	 * Creates the converter for creating markov automata out of dft
	 * @return the converter
	 */
	protected abstract DFT2MAConverter createDFT2MAConverter();

	/**
	 * Performs the actual synthesis of the recovery automaton by optimizing the ma scheduler
	 * @param ma the markov automaton
	 * @param initial the initial markov automaton state
	 * @param subMonitor the monitor
	 * @return the schedule represented as a recovery automaton
	 */
	protected abstract RecoveryAutomaton convertToRecoveryAutomaton(MarkovAutomaton<DFTState> ma, DFTState initial, SubMonitor subMonitor);
	
	/**
	 * Sets the minimizer that will be used to synthesize the recovery automaton
	 * @param minimizer the minimizer
	 */
	public void setMinimizer(ARecoveryAutomatonMinimizer minimizer) {
		this.minimizer = minimizer;
	}
	
	/**
	 * Convert a fault tree to recovery automaton
	 * @param root  the root of the fault tree
	 * @param subMonitor the progress monitor
	 * @return the recovery automaton
	 */
	private RecoveryAutomaton convertToRecoveryAutomaton(FaultTreeNode root, SubMonitor subMonitor) {
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, subMonitor);
		
		RecoveryAutomaton ra = convertToRecoveryAutomaton(ma, dft2maConverter.getMaBuilder().getInitialState(), subMonitor);
		
		statistics.maBuildStatistics.compose(dft2maConverter.getMaBuilder().getStatistics());
		return ra;
	}
	
	/**
	 * Gets the measured statistics
	 * @return the statistics object
	 */
	public SynthesisStatistics getStatistics() {
		return statistics;
	}
}
