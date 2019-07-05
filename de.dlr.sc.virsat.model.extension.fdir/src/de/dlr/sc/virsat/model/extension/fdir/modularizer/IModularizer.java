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

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;

import java.util.Set;


/**
 * Interface for identifying and classifying modules in fault trees.
 * @author jord_ad
 */

interface IModularizer {

	/**
	 * A method which modularizes a Fault Tree and returns the modules in a list.
	 * @param ft the fault tree
	 * @return a list of fault trees which are the modules
	 */
	Set<Module> getModules(FaultTree ft);
}
