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

import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class generates explicit dft states
 * @author muel_s8
 *
 */

public class DFTStateGenerator implements IStateGenerator {

	@Override
	public DFTState generateState(DFTState baseState) {
		return new DFTState(baseState);
	}

	@Override
	public DFTState generateState(FaultTreeHolder ftHolder) {
		return new DFTState(ftHolder);
	}

}
