/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events;

import java.util.Collection;
import java.util.Comparator;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Data class for encapsulating event information
 * @author muel_s8
 *
 */
public interface IDFTEvent {
	
	/**
	 * Gets the occurence rate of this event
	 * @param state the current state
	 * @return the occurence rate
	 */
	double getRate(DFTState state);
	
	/**
	 * Checks if a given fault event can occur in a given state.
	 * @param state the state
	 * @return true iff the event can occur in the given state
	 */
	boolean canOccur(DFTState state);
	
	/**
	 * Executes a single basic event
	 * @param state the current state
	 */
	void execute(DFTState state);
	
	/**
	 * Gets the related DFT node
	 * @return the related dft node
	 */
	Collection<FaultTreeNode> getNodes();
	
	/**
	 * Checks if the given event is an immediate events.
	 * In a DFTstate all immediate events occur before non-immediate events. 
	 * @return true iff the event is an immediate event
	 */
	boolean isImmediate();
	
	/**
	 * Standard comparator for IDFTEvents.
	 * Usefule for sorting lists and ensuring deterministic behavior.
	 */
	Comparator<IDFTEvent> IDFTEVENT_COMPARATOR = Comparator.comparing(Object::toString);
}
