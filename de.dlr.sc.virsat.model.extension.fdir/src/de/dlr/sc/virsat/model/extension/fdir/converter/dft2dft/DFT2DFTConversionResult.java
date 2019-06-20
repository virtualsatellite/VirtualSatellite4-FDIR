/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft;

import java.util.Collections;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Helper class representing the result of a DFT to DFT Conversion
 */

public class DFT2DFTConversionResult {
	private final FaultTreeNode root;
	private final Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator;
	
	/**
	 * Default constructor for initialization
	 * @param root the root of the conversion result
	 * @param mapGeneratedToGenerator the mapping from the generated nodes to the generator nodes
	 */
	
	public DFT2DFTConversionResult(FaultTreeNode root, Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator) {
		this.root = root;
		this.mapGeneratedToGenerator = Collections.unmodifiableMap(mapGeneratedToGenerator);
	}
	
	/**
	 * Gets the root of the conversion result
	 * @return the root
	 */
	public FaultTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Gets the map associating the generated nodes with their generators
	 * @return the association map
	 */
	public Map<FaultTreeNode, FaultTreeNode> getMapGeneratedToGenerator() {
		return mapGeneratedToGenerator;
	}
}
