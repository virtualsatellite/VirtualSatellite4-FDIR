package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.SubMonitor;

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
		statistics.time = System.currentTimeMillis();
		
		Fault fault = synthesisQuery.getRoot().getFault();
		concept = fault.getConcept();
		
		DFT2DFTConversionResult conversionResult = dft2BasicDFT.convert(fault);
		fault = (Fault) conversionResult.getRoot();
		
		RecoveryAutomaton synthesizedRA;
		if (modularizer != null) {
			Set<Module> modules = modularizer.getModules(fault);
			Set<Module> trimmedModules = ftTrimmer.trimModulesAll(modules);
			
			statistics.countTrimmedModules = modules.size() - trimmedModules.size();
			
			Set<RecoveryAutomaton> ras = new HashSet<>();
			for (Module module : trimmedModules) {
				Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator = module.getMapCopyToOriginal();
				mapGeneratedToGenerator.replaceAll((key, value) ->  conversionResult.getMapGeneratedToGenerator().get(value));
				
				SynthesisQuery moduleSynthesisQuery = new SynthesisQuery(module.getRootNode());
				RecoveryAutomaton ra = delegateSynthesizer.synthesize(moduleSynthesisQuery, subMonitor);
				statistics.compose(delegateSynthesizer.getStatistics());
				statistics.maxModuleSize = Math.max(statistics.maxModuleSize, module.getNodes().size());
				
				ra.remapToGeneratorNodes(mapGeneratedToGenerator);
				ras.add(ra);
			}
			
			synthesizedRA = pc.compose(ras, concept);
		} else {
			synthesizedRA = delegateSynthesizer.synthesize(synthesisQuery, subMonitor);
			statistics.compose(delegateSynthesizer.getStatistics());
			statistics.maxModuleSize = conversionResult.getMapGeneratedToGenerator().values().size();
			
			synthesizedRA.remapToGeneratorNodes(conversionResult.getMapGeneratedToGenerator());
		}
		
		statistics.time = System.currentTimeMillis() - statistics.time;
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
