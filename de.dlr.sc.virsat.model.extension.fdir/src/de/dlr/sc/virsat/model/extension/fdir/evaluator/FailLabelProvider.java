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

/**
 * This class gives the criterion which labels a state should have to be considered failed
 * @author muel_s8
 *
 */

public class FailLabelProvider {
	
	/**
	 * This enum lists the relevant fail labels
	 * @author muel_s8
	 *
	 */
	public enum FailLabel {
		FAILED, OBSERVED, UNOBSERVED, PERMANENT
	}

	private Set<FailLabel> failLabels;

	/**
	 * Standard constructor
	 */
	public FailLabelProvider() {
		this.failLabels = new HashSet<>();
	}

	/**
	 * Fail label provider for failure criteria
	 * @param failLabel the fail label
	 */
	public FailLabelProvider(FailLabel failLabel) {
		this();
		this.failLabels.add(failLabel);
	}
	
	/**
	 * Gets the encapsulated fail labels
	 * @return the fail labels
	 */
	public Set<FailLabel> getFailLabels() {
		return failLabels;
	}
}
