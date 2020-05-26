/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events;


/**
 * Interface for events that can be repair or fail events
 * @author muel_s8
 *
 */

public interface IRepairableEvent {
	
	/**
	 * Returns true iff this is a repair event
	 * @return true iff this is a repair event
	 */
	boolean getIsRepair();
}
