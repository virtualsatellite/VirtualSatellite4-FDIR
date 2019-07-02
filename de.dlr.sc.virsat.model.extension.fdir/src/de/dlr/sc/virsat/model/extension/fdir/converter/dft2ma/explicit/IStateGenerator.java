/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit;

import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Generator interface for creating new states
 * @author muel_s8
 *
 */

public interface IStateGenerator {
	/**
	 * Generates a state by copying the base state
	 * @param baseState the base state
	 * @return a new state copying data from the base state
	 */
	ExplicitDFTState generateState(ExplicitDFTState baseState);
	
	/**
	 * Generates an empty new state
	 * @param ftHolder the fault tree
	 * @return a new empty state
	 */
	ExplicitDFTState generateState(FaultTreeHolder ftHolder);
}
