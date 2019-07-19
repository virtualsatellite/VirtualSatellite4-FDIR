/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
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
	 * @param orderDependentBasicEvents set of all basic events that are order dependent 
	 * @param transientNodes set of all transient nodes
	 */
	void execute(DFTState state, Set<BasicEvent> orderDependentBasicEvents, Set<FaultTreeNode> transientNodes);
	
	/**
	 * Gets the related DFT node
	 * @return the related dft node
	 */
	FaultTreeNode getNode();
}
