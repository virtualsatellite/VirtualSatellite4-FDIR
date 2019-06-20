/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.po;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.IStateGenerator;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class generates explicit partial observable dft states
 * @author muel_s8
 *
 */

public class ExplicitPODFTStateGenerator implements IStateGenerator {

	@Override
	public ExplicitPODFTState generateState(ExplicitDFTState baseState) {
		return new ExplicitPODFTState((ExplicitPODFTState) baseState);
	}

	@Override
	public ExplicitPODFTState generateState(FaultTreeHolder ftHolder) {
		return new ExplicitPODFTState(ftHolder);
	}

}
