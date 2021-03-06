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
	FAULT, AND, OR, VOTE, SPARE, POR_I, PAND_I, SAND, POR, PAND, BASIC_EVENT, FDEP, RDEP, PDEP, MONITOR, DELAY, SEQ;
	
	/**
	 * Checks if this is an order dependent fault tree node type
	 * @return true iff the input order matters
	 */
	public boolean isOrderDependent() {
		switch (this) {
			case POR_I:
			case PAND_I:
			case SAND:
			case POR:
			case PAND:
			case SEQ:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Checks if this is a static node type
	 * @return true iff the node is a static node type
	 */
	public boolean isStatic() {
		switch (this) {
			case FAULT:
			case BASIC_EVENT:
			case OR:
			case VOTE:
			case AND:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Checks if this is a nondetermiistic fault tree node type
	 * @return true iff the node allows for nondeterministic behavior
	 */
	public boolean isNondeterministic() {
		switch (this) {
			case SPARE:
				return true;
			default: 
				return false;
		}
	}
	
	/**
	 * Returns whether this is a dependency gate or not
	 * @return true iff dependency gate, false otherwise
	 */
	public boolean isDependency() {
		switch (this) {
			case FDEP:
			case PDEP:
			case RDEP:
				return true;
			default: 
				return false;
		}
	}

	/**
	 * Returns true iff this gate has an output
	 * @return true iff the gate has an output
	 */
	public boolean hasOutput() {
		return !this.equals(SEQ);
	}
}
