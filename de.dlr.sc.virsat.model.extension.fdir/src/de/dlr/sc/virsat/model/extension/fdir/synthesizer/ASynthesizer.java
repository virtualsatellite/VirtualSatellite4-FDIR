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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2DFTConversionResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FreeAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeTrimmer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.IModularizer;
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
	protected IModularizer modularizer = new Modularizer();
	protected FaultTreeTrimmer ftTrimmer = new FaultTreeTrimmer();
	protected ParallelComposer pc = new ParallelComposer();
	protected Concept concept;
	protected SynthesisStatistics statistics;
	
	@Override
	public RecoveryAutomaton synthesize(Fault fault, SubMonitor subMonitor) {
		statistics = new SynthesisStatistics();
		statistics.time = System.currentTimeMillis();
		
		concept = fault.getConcept();
		
		DFT2BasicDFTConverter dft2BasicDFT = new DFT2BasicDFTConverter();
		DFT2DFTConversionResult conversionResult = dft2BasicDFT.convert(fault);
		fault = (Fault) conversionResult.getRoot();
		
		RecoveryAutomaton synthesizedRA;
		if (modularizer != null) {
			Set<Module> modules = modularizer.getModules(fault.getFaultTree());
			Set<Module> trimmedModules = ftTrimmer.trimModulesAll(modules);
			
			statistics.countModules = trimmedModules.size();
			statistics.countTrimmedModules = modules.size() - statistics.countModules;
			
			Set<RecoveryAutomaton> ras = new HashSet<>();
			for (Module module : trimmedModules) {
				statistics.maxModuleSize = Math.max(statistics.maxModuleSize, module.getNodes().size());
				
				Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator = module.getMapCopyToOriginal();
				mapGeneratedToGenerator.replaceAll((key, value) ->  conversionResult.getMapGeneratedToGenerator().get(value));
				RecoveryAutomaton ra = convertToRecoveryAutomaton(module.getRootNodeCopy(), mapGeneratedToGenerator, subMonitor);
				ras.add(ra);
			}
			
			synthesizedRA = pc.compose(ras, concept);
		} else {
			statistics.countModules = 1;
			statistics.maxModuleSize = conversionResult.getMapGeneratedToGenerator().values().size();
			
			synthesizedRA = convertToRecoveryAutomaton(fault, conversionResult.getMapGeneratedToGenerator(), subMonitor);
		}
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		return synthesizedRA;
	}

	/**
	 * @param subMonitor
	 * @param mapGeneratedToGenerator
	 * @param root
	 * @return
	 */
	private RecoveryAutomaton convertToRecoveryAutomaton(FaultTreeNode root, Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator, 
			SubMonitor subMonitor) {
		RecoveryAutomaton ra = convertToRecoveryAutomaton(root, subMonitor);
		
		if (minimizer != null) {
			minimizer.minimize(ra, root);
			statistics.minimizationStatistics.compose(minimizer.getStatistics());
		}
		
		remapToGeneratorNodes(ra, mapGeneratedToGenerator);
		
		statistics.maxModuleRaSize = ra.getStates().size();
		
		return ra;
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
	 * Sets the modularizer that will be used to modularize the fault tree
	 * @param modularizer the modularizer
	 */
	public void setModularizer(IModularizer modularizer) {
		this.modularizer = modularizer;
	}
	
	/**
	 * Gets the equipped modularizer
	 * @return the equipped modularizer
	 */
	public IModularizer getModularizer() {
		return modularizer;
	}
	
	/**
	 * Set the fault tree trimmer. If null, no trimmer will be used and fault tree will not be trimmed.
	 * @param ftTrimmer the trimmer
	 */
	public void setFaultTreeTrimmer(FaultTreeTrimmer ftTrimmer) {
		this.ftTrimmer = ftTrimmer;
	}
	
	/**
	 * Maps all references from generated nodes to references of the generator nodes
	 * @param ra the recovery automaton
	 * @param mapGeneratedToGenerator from the generated fault tree nodes to the generated ones
	 */
	protected void remapToGeneratorNodes(RecoveryAutomaton ra, Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator) {
		for (Transition t : ra.getTransitions()) {
			if (t instanceof FaultEventTransition) {
				FaultEventTransition fet = (FaultEventTransition) t;
				List<FaultTreeNode> generatorGuards = new ArrayList<>();
				
				for (FaultTreeNode guard : fet.getGuards()) {
					if (guard.getTypeInstance() != null) {
						generatorGuards.add(mapGeneratedToGenerator.get(guard));
					}
				}
				fet.getGuards().clear();
				fet.getGuards().addAll(generatorGuards);
			}
			
		    for (RecoveryAction recoveryAction : t.getRecoveryActions()) {
		    	if (recoveryAction instanceof ClaimAction) {
		    		ClaimAction claimAction = (ClaimAction) recoveryAction;
		    		claimAction.setClaimSpare(mapGeneratedToGenerator.get(claimAction.getClaimSpare()));
		    		claimAction.setSpareGate((SPARE) mapGeneratedToGenerator.get(claimAction.getSpareGate()));
		    	} else if (recoveryAction instanceof FreeAction) {
		    		FreeAction freeAction = (FreeAction) recoveryAction;
		    		freeAction.setFreeSpare(mapGeneratedToGenerator.get(freeAction.getFreeSpare()));
		    	}
		    }
		}
	}
	
	/**
	 * Convert a fault tree to recovery automaton
	 * @param root  the root of the fault tree
	 * @param subMonitor the progress monitor
	 * @return the recovery automaton
	 */
	private RecoveryAutomaton convertToRecoveryAutomaton(FaultTreeNode root, SubMonitor subMonitor) {
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		dft2maConverter.getStateSpaceGenerator().getStaticAnalysis().setSymmetryChecker(null);
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root);
		
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
