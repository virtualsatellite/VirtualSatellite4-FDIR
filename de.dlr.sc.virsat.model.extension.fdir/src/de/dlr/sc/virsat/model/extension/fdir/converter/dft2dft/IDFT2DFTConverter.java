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

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * This class provides an interface for DFT Model to DFT Model conversions
 * @author muel_s8
 *
 */

public interface IDFT2DFTConverter {
	
	/**
	 * Takes a fault tree root node and returns the root node of the converted tree
	 * @param root a fault tree root node
	 * @return the root of the converted fault tree
	 */
	DFT2DFTConversionResult convert(FaultTreeNode root);
}
