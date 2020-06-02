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

import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;


/**
 * Interface for identifying and classifying modules in fault trees.
 * @author jord_ad
 */

public interface IModularizer {

	/**
	 * A method which modularizes a Fault Tree and returns the modules in a set.
	 * @param root the root node to the Fault Tree which is to be modularized
	 * @return a set of modules
	 */
	Set<Module> getModules(Fault root);
}
