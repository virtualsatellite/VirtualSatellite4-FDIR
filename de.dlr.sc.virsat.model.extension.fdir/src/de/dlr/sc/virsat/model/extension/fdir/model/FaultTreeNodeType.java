/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

/**
 * An enum containing the possible node types in the supported by our fault
 * trees.
 * 
 * @author muel_s8
 *
 */

public enum FaultTreeNodeType {
	FAULT, AND, OR, VOTE, SPARE, POR_I, PAND_I, SAND, POR, PAND, BASIC_EVENT, FDEP, RDEP, PDEP, OBSERVER, DELAY;
	
	/**
	 * Checks if this is an order dependent fault tree node type
	 * @return true iff the inpurt order matters
	 */
	public boolean isOrderDependent() {
		switch (this) {
			case POR_I:
			case PAND_I:
			case SAND:
			case POR:
			case PAND:
				return true;
			default:
				return false;
		}
	}
}
