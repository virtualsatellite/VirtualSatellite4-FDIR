/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;

import de.dlr.sc.virsat.apps.api.external.ModelAPI;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.util.DVLMCopier;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.ps.model.ConfigurationTree;
import de.dlr.sc.virsat.model.extension.ps.model.ElementDefinition;

/**
 * Example App
 * 
 * @author VirSat
 *
 */
public class FaultTreeWithFDIRMechanismGenerator {
	private static ModelAPI modelAPI = new ModelAPI();
	
	private Concept fdirConcept = modelAPI.getConcept("de.dlr.sc.virsat.model.extension.fdir");
	
	public static void main(String[] args) throws IOException {
		ConfigurationTree root = modelAPI.getRootSeis(ConfigurationTree.class).get(0);
		FDIRMechanismSelectionDialog configDialog = FDIRMechanismSelectionDialog.create(modelAPI, root);
		int result = configDialog.open();
		
		if (result == 0) {
			// Cancel was not pressed
			FaultTreeWithFDIRMechanismGenerator generator = new FaultTreeWithFDIRMechanismGenerator();
			generator.generate(root, configDialog.getMapFaultToFDIRMechanism());
		}
	}
	
	private DVLMCopier rootCopier = new DVLMCopier();
	
	private IBeanStructuralElementInstance copyTree(ConfigurationTree root) throws IOException {
		EObject copy = rootCopier.copy(root.getStructuralElementInstance());
		rootCopier.copyReferences();
		BeanStructuralElementInstance copiedRoot = new BeanStructuralElementInstance((StructuralElementInstance) copy);
		
		modelAPI.createSeiStorage(copiedRoot);
		for (IBeanStructuralElementInstance beanChild : copiedRoot.getDeepChildren(IBeanStructuralElementInstance.class)) {
			modelAPI.createSeiStorage(beanChild);
		}
		modelAPI.addRootSei(copiedRoot);
		
		return copiedRoot;
	}
	
	private Fault copyMechanism(ElementDefinition fdirMechanism, Fault faultWithFDIRMechanism) {
		List<Fault> fdirFaults = fdirMechanism.getAll(Fault.class);
		List<CategoryAssignment> tis = fdirFaults.stream().map(fault -> fault.getTypeInstance()).collect(Collectors.toList());
		DVLMCopier faultCopier = new DVLMCopier();
		Collection<CategoryAssignment> copies = faultCopier.copyAll(tis);
		faultCopier.copyReferences();
		
		for (CategoryAssignment copy : copies) {
			if (copy.getType().equals(faultWithFDIRMechanism.getTypeInstance().getType())) {
				Fault copiedFault = new Fault((CategoryAssignment) copy);
				faultWithFDIRMechanism.getParent().add(copiedFault);
			}
		}
		
		Fault fdirTLE = fdirMechanism.getFirst(Fault.class);
		return new Fault((CategoryAssignment) faultCopier.get(fdirTLE.getTypeInstance()));
	}
	
	public void generate(ConfigurationTree root, Map<Fault, Set<ElementDefinition>> map) throws IOException {
		IBeanStructuralElementInstance copiedRoot = copyTree(root);
		String newRootName = root.getName() + "_WITH_FDIR";
		copiedRoot.setName(newRootName);
		
		FaultTreeHelper ftHelper = new FaultTreeHelper(fdirConcept);
		for (Entry<Fault, Set<ElementDefinition>> entry: map.entrySet()) {
			Fault faultWithFDIRMechanism = entry.getKey();
			faultWithFDIRMechanism = new Fault((CategoryAssignment) rootCopier.get(faultWithFDIRMechanism.getTypeInstance()));
			List<FaultTreeEdge> propagations = new ArrayList<>(faultWithFDIRMechanism.getFaultTree().getPropagations());
			Set<ElementDefinition> fdirMechanisms = entry.getValue();
			
			// Create the following structure:
			// Old_Fault
			//    ^
			//    |
			//   AND
			//    ^
			//   / \
			// FP  FP
			// |    |
			// G    G
			// \    /
			//  \  /
			//Old Source fault
			
			
			// The AND gate is optional and only needed if we have multiple mechanisms
			FaultTreeNode sinkNode = faultWithFDIRMechanism;
			if (fdirMechanisms.size() > 1) {
				sinkNode = ftHelper.createGate(faultWithFDIRMechanism, FaultTreeNodeType.AND);
				ftHelper.connect(faultWithFDIRMechanism, sinkNode, faultWithFDIRMechanism);
			}
			
			Set<FaultTreeEdge> toRemove = new HashSet<>();
			for (ElementDefinition fdirMechanism : fdirMechanisms) {
				Fault fdirTLE = copyMechanism(fdirMechanism, faultWithFDIRMechanism);
				
				List<Gate> fdirInputs = fdirTLE.getFaultTree().getGates();
				for (FaultTreeEdge fte : propagations) {
					if (fte.getTo().equals(faultWithFDIRMechanism)) {
						for (Gate fdirInput : fdirInputs) {
							ftHelper.connect(fdirTLE, fte.getFrom(), fdirInput);
						}
						ftHelper.connect(faultWithFDIRMechanism, fdirTLE, sinkNode);
						toRemove.add(fte);
					}
				}
				
				faultWithFDIRMechanism.getFaultTree().getPropagations().removeAll(toRemove);
			}
		}
		
		modelAPI.saveAll();
	}
}
