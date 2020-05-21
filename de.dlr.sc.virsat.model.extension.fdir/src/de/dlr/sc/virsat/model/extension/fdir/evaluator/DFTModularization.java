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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTSymmetryChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.SymmetryReduction;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeNodePlus;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Modularizer;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class holds a modularization of a DFT.
 * @author muel_s8
 *
 */

public class DFTModularization {
	
	public static final int MODULE_SPLIT_SIZE_BES = 20;
	
	private Set<Module> modules;
	private Set<Module> modulesToModelCheck;
	private Module topLevelModule;
	private Map<FaultTreeNode, FaultTreeNode> mapNodeToRepresentant;
	
	/**
	 * Standard constructor.
	 * Extracts the modularization from the modularizer
	 * @param modularizer the modularizer used to compute the DFT modularization
	 * @param ftHolder the fault tree holder
	 * @param symmetryChecker optionally a symmetry checker
	 */
	public DFTModularization(Modularizer modularizer, FaultTreeHolder ftHolder, DFTSymmetryChecker symmetryChecker) {
		Fault rootFault = (Fault) ftHolder.getRoot();
		modules = modularizer.getModules(rootFault.getFaultTree());
		
		if (!modules.isEmpty()) {
			topLevelModule = getModule(rootFault);
			modulesToModelCheck = computeModulesToModelCheck();
			
			if (modulesToModelCheck.size() > 1) {
				mapNodeToRepresentant = createMapNodeToRepresentant(ftHolder, symmetryChecker);
			}
		}
	}
	
	/**
	 * Gets the top level module
	 * @return the top elvel module
	 */
	public Module getTopLevelModule() {
		return topLevelModule;
	}
	
	/**
	 * Gets the leaf modules that have to be model checked
	 * @return the leaf modules
	 */
	public Set<Module> getModulesToModelCheck() {
		return modulesToModelCheck;
	}
	
	/**
	 * Gets a mapping from a node to its symmetric representant.
	 * Null if no symmetry checker was given.
	 * @return the mapping
	 */
	public Map<FaultTreeNode, FaultTreeNode> getMapNodeToRepresentant() {
		return mapNodeToRepresentant;
	}
	
	/**
	 * Creates a mapping from a node to a representant from its symmetry equivalence class
	 * @param ftHolder the fault tree holder
	 * @param symmetryChecker the symmetry checker
	 * @return a mapping from a node to the symmetric representant
	 */
	private Map<FaultTreeNode, FaultTreeNode> createMapNodeToRepresentant(FaultTreeHolder ftHolder, DFTSymmetryChecker symmetryChecker) {
		Map<FaultTreeNode, FaultTreeNode> mapNodeToRepresentant = new HashMap<>();
		SymmetryReduction symmetryReduction = symmetryChecker.computeSymmetryReduction(ftHolder, ftHolder);
				
		for (Entry<FaultTreeNode, List<FaultTreeNode>> entry : symmetryReduction.getBiggerRelation().entrySet()) {
			if (symmetryReduction.getSmallerNodes(entry.getKey()).isEmpty()) {
				mapNodeToRepresentant.put(entry.getKey(), entry.getKey());
				for (FaultTreeNode biggerNode : entry.getValue()) {
					mapNodeToRepresentant.put(biggerNode, entry.getKey());
				}
			}
		}
		
		return mapNodeToRepresentant;
	}
	
	/**
	 * Gets the module for a given fault tree node
	 * @param node a fault tree node
	 * @return the module for the fault tree node, or null of no such module exists
	 */
	Module getModule(FaultTreeNode node) {
		return modules.stream().filter(module -> module.getRootNode().equals(node)).findAny().orElse(null);
	}
	
	/**
	 * Counts the total number of basic events in a module
	 * including all of its sub modules
	 * @param module the module we wish to know the total be count for
	 * @return the total number of basic events in a module including all of its sub modules
	 */
	private int getTotalCountBasicEvents(Module module) {
		Queue<Module> toProcess = new LinkedList<>();
		toProcess.add(module);
		
		int totalCountBEs = 0;
		
		while (!toProcess.isEmpty()) {
			Module subModule = toProcess.poll();
			for (FaultTreeNodePlus nodePlus : subModule.getModuleNodes()) {
				if (nodePlus.getFaultTreeNode() instanceof BasicEvent) {
					totalCountBEs++;
				}
				
				for (FaultTreeNodePlus childPlus : nodePlus.getChildren()) {
					Module subSubModule = getModule(childPlus.getFaultTreeNode());
					if (subSubModule != null && subSubModule != subModule) {
						toProcess.add(subSubModule);
					}
				}
			}
		}
		
		return totalCountBEs;
	}
	
	/**
	 * Determines the leaf modules that we should model check
	 * @return a set of modules that should be model checked
	 */
	private Set<Module> computeModulesToModelCheck() {
		Set<Module> modulesToModelCheck = new HashSet<>();		
		
		Queue<Module> toProcess = new LinkedList<>();
		toProcess.add(topLevelModule);
		
		while (!toProcess.isEmpty()) {
			Module module = toProcess.poll();
			
			boolean shouldModelCheck = module.getModuleNodes().size() > 1;
			if (!shouldModelCheck) {
				FaultTreeNode moduleRoot = module.getModuleNodes().get(0).getFaultTreeNode();
				
				if (moduleRoot instanceof Fault) {
					shouldModelCheck = !((Fault) moduleRoot).getBasicEvents().isEmpty();
				} else if (moduleRoot instanceof VOTE) {
					long votingThreshold = ((VOTE) moduleRoot).getVotingThreshold();
					shouldModelCheck = votingThreshold != 1 && votingThreshold != module.getModuleRoot().getChildren().size();
				} else {
					shouldModelCheck = false;
				}
			}
			
			if (!shouldModelCheck) {
				shouldModelCheck = getTotalCountBasicEvents(module) < MODULE_SPLIT_SIZE_BES;
			}
			
			if (shouldModelCheck) {
				modulesToModelCheck.add(module);
			} else {
				for (FaultTreeNodePlus ftChildPlus : module.getModuleRoot().getChildren()) {
					Module subModule = getModule(ftChildPlus.getFaultTreeNode());
					toProcess.add(subModule);
				}
			}
		}
		
		return modulesToModelCheck;
	}
}
