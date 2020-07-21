/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.util.IStatistics;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2DFTConversionResult;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeTrimmer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.IModularizer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Modularizer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;
import de.dlr.sc.virsat.model.extension.fdir.recovery.ParallelComposer;

public class ModularSynthesizer implements ISynthesizer {
	protected DFT2BasicDFTConverter dft2BasicDFT = new DFT2BasicDFTConverter();
	protected IModularizer modularizer = new Modularizer();
	protected FaultTreeTrimmer ftTrimmer = new FaultTreeTrimmer();
	protected ParallelComposer pc = new ParallelComposer();
	
	protected DelegateSynthesizer delegateSynthesizer = new DelegateSynthesizer();
	
	protected SynthesisStatistics statistics;
	protected Concept concept;
	
	@Override
	public RecoveryAutomaton synthesize(SynthesisQuery synthesisQuery, SubMonitor subMonitor) {
		statistics = new SynthesisStatistics();
		long startTime = System.currentTimeMillis();
		statistics.time = IStatistics.TIMEOUT;
		
		Fault fault = synthesisQuery.getRoot().getFault();
		concept = fault.getConcept();
		
		DFT2DFTConversionResult conversionResult = dft2BasicDFT.convert(fault);
		fault = (Fault) conversionResult.getRoot();
		
		RecoveryAutomaton synthesizedRA;
		if (modularizer != null) {
			Set<Module> modules = modularizer.getModules(fault);
			Set<Module> trimmedModules = ftTrimmer.trimModulesAll(modules);
			
			statistics.countModules = modules.size();
			statistics.countTrimmedModules = statistics.countModules - trimmedModules.size();
			
			Set<RecoveryAutomaton> ras = new HashSet<>();
			for (Module module : trimmedModules) {
				Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator = module.getMapCopyToOriginal();
				mapGeneratedToGenerator.replaceAll((key, value) ->  conversionResult.getMapGeneratedToGenerator().get(value));
				
				RecoveryAutomaton ra = synthesizeModule(synthesisQuery, module.getRootNodeCopy(), mapGeneratedToGenerator, subMonitor);
				ras.add(ra);
			}
			
			synthesizedRA = pc.compose(ras, concept);
		} else {
			statistics.countModules = 1;
			synthesizedRA = synthesizeModule(synthesisQuery, fault, conversionResult.getMapGeneratedToGenerator(), subMonitor);
		}
		
		statistics.time = System.currentTimeMillis() - startTime;
		return synthesizedRA;
	}

	/**
	 * Synthesizes the recovery automaton for a single module.
	 * @param synthesisQuery the original synthessi query
	 * @param moduleRoot the root of the module
	 * @param mapGeneratedToGenerator mapping from module nodes to original nodes
	 * @param subMonitor a monitor
	 * @return the synthesized ra
	 */
	private RecoveryAutomaton synthesizeModule(SynthesisQuery synthesisQuery, FaultTreeNode moduleRoot, Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator, SubMonitor subMonitor) {
		SynthesisQuery delegateSynthesisQuery = new SynthesisQuery(moduleRoot);
		delegateSynthesisQuery.setObjectiveMetric(synthesisQuery.getObjectiveMetric());
		statistics.maxModuleSize = Math.max(statistics.maxModuleSize, mapGeneratedToGenerator.size());
		RecoveryAutomaton synthesizedRA = delegateSynthesizer.synthesize(delegateSynthesisQuery, subMonitor);
		statistics.compose(delegateSynthesizer.getStatistics());
		synthesizedRA.remapToGeneratorNodes(mapGeneratedToGenerator);
		return synthesizedRA;
	}

	@Override
	public SynthesisStatistics getStatistics() {
		return statistics;
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
}
