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

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * A provider  for nodes that need to fail for a fault tree to be considered failed
 * @author muel_s8
 *
 */

public class FailNodeProvider {
	private Set<FaultTreeNode> failNodes;
	
	/**
	 * Default constructor for empty fail set
	 */
	public FailNodeProvider() {
		this.failNodes = new HashSet<>();
	}
	
	/**
	 * Standard constructor
	 * @param root the fault tree node
	 */
	public FailNodeProvider(FaultTreeNode root) {
		this();
		this.failNodes.add(root);
	}
	
	/**
	 * Gets the fail nodes
	 * @return the fail nodes
	 */
	public Set<FaultTreeNode> getFailNodes() {
		return failNodes;
	}
}
