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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2DFTConversionResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityRequirement;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Modularizer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;
import de.dlr.sc.virsat.model.extension.fdir.recovery.ParallelComposer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ARecoveryAutomatonMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;

/**
 * Abstract class providing some default implementations for the ISynthesizer interface.
 * @author muel_s8
 *
 */

public abstract class ASynthesizer implements ISynthesizer {

	protected ARecoveryAutomatonMinimizer minimizer = ComposedMinimizer.createDefaultMinimizer();
	protected Modularizer modularizer = new Modularizer();
	protected Concept concept;
	
	@Override
	public RecoveryAutomaton synthesize(Fault fault, Map<ReliabilityRequirement, Fault> requirements) {
		concept = fault.getConcept();
		
		DFT2BasicDFTConverter dft2BasicDFT = new DFT2BasicDFTConverter();
		DFT2DFTConversionResult conversionResult = dft2BasicDFT.convert(fault);
		fault = (Fault) conversionResult.getRoot();
		
		RecoveryAutomaton synthesizedRA = new RecoveryAutomaton(fault.getConcept());
		if (modularizer != null) {
			Set<Module> modules = modularizer.getModules(fault.getFaultTree());
			Set<Module> trimmedModules = trimStaticModules(modules);
			
			Set<RecoveryAutomaton> ras = trimmedModules.stream()
						.map(module -> convertToRecoveryAutomaton(module))
						.collect(Collectors.toSet());
			
			if (minimizer != null) {
				ras.forEach(ra -> minimizer.minimize(ra));
			}
			ras.stream().forEach(ra -> remapToGeneratorNodes(ra, conversionResult.getMapGeneratedToGenerator()));
			
			ParallelComposer pc = new ParallelComposer();
			synthesizedRA = pc.compose(ras, concept);
		} else {
			synthesizedRA = convertToRecoveryAutomaton(fault);
			
			if (minimizer != null) {
				minimizer.minimize(synthesizedRA);
			}
			remapToGeneratorNodes(synthesizedRA, conversionResult.getMapGeneratedToGenerator());
		}
		
		return synthesizedRA;
	}
	
	/**
	 * Creates the state space generator
	 * @return the state space generator
	 */
	protected abstract DFT2MAConverter createDFT2MAConverter();

	/**
	 * Performs the actual synthesis of the recovery automaton by optimizing the ma scheduler
	 * @param ma the markov automaton
	 * @param initial the initial markov automaton state
	 * @return the schedule represented as a recovery automaton
	 */
	protected abstract RecoveryAutomaton computeMarkovAutomatonSchedule(MarkovAutomaton<DFTState> ma, DFTState initial);

	/**
	 * Synthesies a recovery automaton.
	 * @param fault the fault
	 * @return the synthesized recovery automaton
	 */
	public RecoveryAutomaton synthesize(Fault fault) {
		return synthesize(fault, Collections.emptyMap());
	}

	/**
	 * Sets the minimizer that will be used to synthesize the recovery automaton
	 * @param minimizer the minimizer
	 */
	public void setMinimizer(ARecoveryAutomatonMinimizer minimizer) {
		this.minimizer = minimizer;
	}
	
	/**
	 * Sets the modularizer that will be used to modularize the fault tree
	 * @param modularizer the modularizer
	 */
	public void setModularizer(Modularizer modularizer) {
		this.modularizer = modularizer;
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
	
	protected double normalizationRate;
	
	/**
	 * Normalizes the transition rates in the given markov automaton
	 * @param ma the markov automaton
	 * @param faultEvents the fault events
	 */
	protected void normalizeRates(MarkovAutomaton<DFTState> ma, Set<Object> faultEvents) {
		normalizationRate = 0;
		for (Object event : faultEvents) {
			for (MarkovTransition<DFTState> transition : ma.getTransitions(event)) {
				if (transition.isMarkovian()) {
					normalizationRate += transition.getRate();
				}
			}
		}
		
		for (Object event : faultEvents) {
			for (MarkovTransition<DFTState> transition : ma.getTransitions(event)) {
				if (transition.isMarkovian()) {
					transition.setRate(transition.getRate() / normalizationRate);
				}
			}
		}
	}
	
	/**
	 * Trim the static modules out of a set of modules
	 * @param modules the original set of modules
	 * @return the set of modules without static modules
	 */
	private static Set<Module> trimStaticModules(Set<Module> modules) {
		Set<Module> result = new HashSet<Module>();
		for (Module module : modules) {
			if (module.isDynamic()) {
				result.add(module);
			}
		}
		return result;
	}
	
	/**
	 * Convert a module to recovery automaton
	 * @param module the module
	 * @return the recovery automaton
	 */
	private RecoveryAutomaton convertToRecoveryAutomaton(Module module) {
		return convertToRecoveryAutomaton(module.getRootNode());
	}
	
	/**
	 * Convert a fault tree to recovery automaton
	 * @param root  the root of the fault tree
	 * @return the recovery automaton
	 */
	private RecoveryAutomaton convertToRecoveryAutomaton(FaultTreeNode root) {
		DFT2MAConverter dft2ma = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2ma.convert(root);
		Set<Object> faultEvents = ma.getEvents();
		normalizeRates(ma, faultEvents);
		
		RecoveryAutomaton ra = computeMarkovAutomatonSchedule(ma, dft2ma.getInitial());
		return ra;
	}
}
