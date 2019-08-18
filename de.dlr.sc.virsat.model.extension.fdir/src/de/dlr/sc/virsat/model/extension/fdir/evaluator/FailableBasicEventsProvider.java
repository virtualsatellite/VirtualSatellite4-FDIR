/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import java.util.HashSet;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;

/**
 * A provider  for nodes that need to fail for a fault tree to be considered failed
 * @author muel_s8
 *
 */

public class FailableBasicEventsProvider {
	private Set<BasicEvent> basicEvents;
	
	/**
	 * Default constructor for empty f set
	 */
	public FailableBasicEventsProvider() {
		this.basicEvents = new HashSet<>();
	}
	
	/**
	 * Standard constructor
	 * @param basicEvents the failable basic events
	 */
	public FailableBasicEventsProvider(Set<BasicEvent> basicEvents) {
		this.basicEvents = basicEvents;
	}
	
	/**
	 * Gets the fail nodes
	 * @return the fail nodes
	 */
	public Set<BasicEvent> getBasicEvents() {
		return basicEvents;
	}
}
