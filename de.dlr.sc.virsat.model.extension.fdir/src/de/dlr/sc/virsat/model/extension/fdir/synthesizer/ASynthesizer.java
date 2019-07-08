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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2DFTConversionResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityRequirement;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
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
	protected Set<Object> faultEvents;
	protected MarkovAutomaton<DFTState> ma;
	
	@Override
	public RecoveryAutomaton synthesize(Fault fault, Map<ReliabilityRequirement, Fault> requirements) {
		concept = fault.getConcept();
		
		DFT2BasicDFTConverter dft2BasicDFT = new DFT2BasicDFTConverter();
		DFT2DFTConversionResult conversionResult = dft2BasicDFT.convert(fault);
		fault = (Fault) conversionResult.getRoot();
		
		ExplicitDFT2MAConverter dft2Ma = createDFT2MAConverter();
		
		ma = dft2Ma.convert(fault);
		faultEvents = ma.getEvents();
		normalizeRates();	
		
		RecoveryAutomaton ra = computeMarkovAutomatonSchedule(dft2Ma.getInitial());
		
		if (minimizer != null) {
			minimizer.minimize(ra);
		}
		
		remapToGeneratorNodes(ra, conversionResult.getMapGeneratedToGenerator());
		
		return ra;
	}
	
	/**
	 * Creates the state space generator
	 * @return the state space generator
	 */
	protected abstract ExplicitDFT2MAConverter createDFT2MAConverter();

	/**
	 * Performs the actual synthesis of the recovery automaton by optimizing the ma scheduler
	 * @param initial the initial markov automaton state
	 * @return the schedule represented as a recovery automaton
	 */
	protected abstract RecoveryAutomaton computeMarkovAutomatonSchedule(DFTState initial);

	@Override
	public RecoveryAutomaton synthesize(Fault fault) {
		return synthesize(fault, Collections.emptyMap());
	}

	@Override
	public void setMinimizer(ARecoveryAutomatonMinimizer minimizer) {
		this.minimizer = minimizer;
	}
	
	/**
	 * Maps all references from generated nodes to references of the generator nodes
	 * @param ra the recovery automaton
	 * @param mapGeneratedToGenerator from the generated fault tree nodes to the generated ones
	 */
	protected void remapToGeneratorNodes(RecoveryAutomaton ra, Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator) {
		// Finally we need to correctly set the recovery actions
		// We do this by comparing two successive states and using the delta
				
		for (Transition t : ra.getTransitions()) {
			if (t instanceof FaultEventTransition) {
				FaultEventTransition fet = (FaultEventTransition) t;
				List<FaultTreeNode> generatorGuards = new ArrayList<>();
				
				for (FaultTreeNode guard : fet.getGuards()) {
					generatorGuards.add(mapGeneratedToGenerator.get(guard));
				}
			
				fet.getGuards().clear();
				fet.getGuards().addAll(generatorGuards);
			}
			
		    for (RecoveryAction recoveryAction : t.getRecoveryActions()) {
		    	if (recoveryAction instanceof ClaimAction) {
		    		ClaimAction claimAction = (ClaimAction) recoveryAction;
		    		claimAction.setClaimSpare(mapGeneratedToGenerator.get(claimAction.getClaimSpare()));
		    		claimAction.setSpareGate((SPARE) mapGeneratedToGenerator.get(claimAction.getSpareGate()));
		    	}
		    }
		}
	}
	
	/**
	 * Normalizes the transition rates in the given markov automaton
	 */
	protected void normalizeRates() {
		float totalRate = 0;
		for (Object event : faultEvents) {
			for (MarkovTransition<DFTState> transition : ma.getTransitions(event)) {
				if (transition.isMarkovian()) {
					totalRate += transition.getRate();
				}
			}
		}
		
		for (Object event : faultEvents) {
			for (MarkovTransition<DFTState> transition : ma.getTransitions(event)) {
				if (transition.isMarkovian()) {
					transition.setRate(transition.getRate() / totalRate);
				}
			}
		}
	}
}
