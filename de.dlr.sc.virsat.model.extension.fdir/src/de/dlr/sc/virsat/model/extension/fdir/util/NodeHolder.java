/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * This class provides general data relevant for fault tree nodes
 * @author muel_s8
 *
 */
public class NodeHolder {
	int index;
	Map<EdgeType, List<FaultTreeNode>> mapEdgeTypeToNodes;
	
	/**
	 * Standard consturctor.
	 * Inititalizes all internal datastructures.
	 */
	NodeHolder() {
		mapEdgeTypeToNodes = new HashMap<>();
		for (EdgeType edgeType : EdgeType.values()) {
			mapEdgeTypeToNodes.put(edgeType, new ArrayList<>());
		}
	}
	
	/**
	 * Gets the adjacent nodes in the fault tree graph of the given edge types
	 * @param edgeTypes the edge types
	 * @return the adjacent nodes
	 */
	List<FaultTreeNode> getNodes(EdgeType... edgeTypes) {
		if (edgeTypes.length == 0) {
			return Collections.emptyList();
		} else if (edgeTypes.length == 1) {
			return mapEdgeTypeToNodes.get(edgeTypes[0]);
		} else {
			ArrayList<FaultTreeNode> nodes = new ArrayList<>();
			for (EdgeType edgeType : edgeTypes) {
				nodes.addAll(getNodes(edgeType));
			}
			return nodes;
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(index);
	}
}
