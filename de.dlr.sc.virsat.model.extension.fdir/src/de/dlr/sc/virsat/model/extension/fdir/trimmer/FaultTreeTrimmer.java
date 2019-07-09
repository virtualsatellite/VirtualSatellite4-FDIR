/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.trimmer;

import java.util.HashSet;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Module;

/**
 * Class for trimming fault trees
 * @author jord_ad
 *
 */
public class FaultTreeTrimmer {

	/**
	 * Trim a fault tree
	 * @param ft the fault tree
	 * @return the trimmed fault tree
	 */
	public FaultTree trimFaultTree(FaultTree ft) {
		if (ft == null || ft.getChildFaults().size() == 0) {
			return ft;
		}
		
		return new FaultTree(ft.getConcept());
	}
	
	/**
	 * Trim a set of modules from a modularized fault tree
	 * @param modules the modules
	 * @return the trimmed set of modules
	 */
	public Set<Module> trimModules(Set<Module> modules) {
		if (modules == null || modules.isEmpty()) {
			return modules;
		}
		
		Set<Module> trimmedModules = trimStaticModules(modules);
		return trimmedModules;
	}
	
	/**
	 * Trim the static modules from a set of modules
	 * @param modules the set of modules
	 * @return the dynamic modules
	 */
	private static Set<Module> trimStaticModules(Set<Module> modules) {
		Set<Module> dynamicModules = new HashSet<Module>();
		for (Module module : modules) {
			if (module.isDynamic()) {
				dynamicModules.add(module);
			}
		}
		return dynamicModules;
	}
	
}
