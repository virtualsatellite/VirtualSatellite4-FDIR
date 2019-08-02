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

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingStatistics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConversionStatistics;

/**
 * This class holds the internal statistics of a call to the DFTEvaluator
 * @author muel_s8
 *
 */

public class DFTEvaluationStatistics {
	// CHECKSTYLE:OFF
	public long time;
	public final DFT2MAConversionStatistics stateSpaceGenerationStatistics = new DFT2MAConversionStatistics();
	public final ModelCheckingStatistics modelCheckingStatistics = new ModelCheckingStatistics();
	// CHECKSYTLE:ON
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DFT Evaluation Statistics: \n");
		sb.append(stateSpaceGenerationStatistics);
		sb.append(modelCheckingStatistics);
		return sb.toString();
	}
}
