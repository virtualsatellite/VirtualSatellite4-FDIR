/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.modularizer;

import java.util.HashSet;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;

/**
 * Class for trimming fault trees
 * @author jord_ad
 *
 */
public class FaultTreeTrimmer {
	
	/**
	 * Trim a set of modules from a modularized fault tree with all possible trimmings in a given order
	 * @param modules the modules
	 * @return the trimmed set of modules
	 */
	public Set<Module> trimModulesAll(Set<Module> modules) {
		Set<Module> trimmedModules = trimDeterministicModules(modules);
		trimmedModules.stream().forEach(Module::constructFaultTreeCopy);
		trimmedModules = trimDeterministicNodes(trimmedModules);
		return trimmedModules;
	}
	
	/**
	 * Trim the static modules from a set of modules
	 * @param modules the set of modules
	 * @return the dynamic modules
	 */
	public Set<Module> trimDeterministicModules(Set<Module> modules) {
		Set<Module> nondeterministicModules = new HashSet<Module>();
		for (Module module : modules) {
			if (module.isNondeterministic()) {
				nondeterministicModules.add(module);
			}
		}
		return nondeterministicModules;
	}
	
	
	/**
	 * Trim the unneeded nodes in a module.
	 * This includes:
	 * 		- nodes which are "side nodes"
	 * @param modules the modules to trim
	 * @return the set of trimmed modules
	 */
	public Set<Module> trimDeterministicNodes(Set<Module> modules) {
		if (!modules.isEmpty()) {
			FaultTreeBuilder ftBuilder = new FaultTreeBuilder(modules.iterator().next().getRootNode().getConcept());
		
			for (Module module : modules) {
				for (FaultTreeEdge edge : ftBuilder.getFtHelper().getAllEdges(module.getRootNodeCopy().getFault())) {
					FaultTreeNode from = edge.getFrom();
					
					if (!module.hasPriorityAbove(from)
							&& !module.hasSpareBelow(from)
							&& !module.hasSpareAbove(from)
							&& !from.getFaultTreeNodeType().equals(FaultTreeNodeType.SPARE)
							&& !module.isPartialObservable()) {
						ftBuilder.removeEdgeFromFaultTree(edge, module.getRootNodeCopy().getFault().getFaultTree());
					}
				}
			}
		}
		return modules;
	}
	
}
