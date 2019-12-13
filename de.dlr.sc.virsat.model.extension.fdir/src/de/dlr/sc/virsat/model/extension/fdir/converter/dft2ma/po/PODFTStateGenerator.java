/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IStateGenerator;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class generates explicit partial observable dft states
 * @author muel_s8
 *
 */

public class PODFTStateGenerator implements IStateGenerator {

	@Override
	public PODFTState generateState(DFTState baseState) {
		return new PODFTState((PODFTState) baseState);
	}

	@Override
	public PODFTState generateState(FaultTreeHolder ftHolder) {
		return new PODFTState(ftHolder);
	}

}
