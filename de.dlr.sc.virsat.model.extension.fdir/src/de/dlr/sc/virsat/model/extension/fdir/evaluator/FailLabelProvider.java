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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

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
		FAILED, OBSERVED, PERMANENT
	}

	private Map<FaultTreeNode, Set<FailLabel>> failLabels;

	/**
	 * Standard constructor
	 * @param failLabels the fail labels
	 */
	public FailLabelProvider(Map<FaultTreeNode, Set<FailLabel>> failLabels) {
		this.failLabels = failLabels;
	}

	/**
	 * Fail label provider for considering top level event occurence
	 * @param root the root of the fault tree
	 */
	public FailLabelProvider(FaultTreeNode root) {
		this.failLabels = new HashMap<>();
		this.failLabels.put(root, Collections.singleton(FailLabel.FAILED));
	}
	
	/**
	 * Gets the encapsulated fail labels
	 * @return the fail labels
	 */
	public Map<FaultTreeNode, Set<FailLabel>> getFailLabels() {
		return failLabels;
	}
}
