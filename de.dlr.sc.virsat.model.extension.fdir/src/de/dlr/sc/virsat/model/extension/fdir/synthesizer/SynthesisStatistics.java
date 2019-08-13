/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConversionStatistics;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.MinimizationStatistics;

/**
 * This class holds statistics data for synthesizers
 */

public class SynthesisStatistics {
	//CHECKSTYLE:OFF
	public long time;
	public int countModules;
	public int countTrimmedModules;
	public int maxModuleSize;
	public int maxModuleRaSize;
	public final DFT2MAConversionStatistics stateSpaceGenerationStatistics = new DFT2MAConversionStatistics();
	public final MinimizationStatistics minimizationStatistics = new MinimizationStatistics();
	//CHECKSTYLE:ON
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Synthesizer Statistics: \n");
		sb.append("\t* Synthesis Time   :\t\t" 		+ time + "\n");
		sb.append("\t* #Modules         :\t\t" 		+ countModules + "\n");
		sb.append("\t* #Trimmed         :\t\t" 		+ countTrimmedModules + "\n");
		sb.append("\t* Largest Module   :\t\t" 		+ maxModuleSize + "\n");
		sb.append("\t* Largest Module RA:\t\t" 		+ maxModuleRaSize + "\n");
		sb.append(stateSpaceGenerationStatistics);
		sb.append(minimizationStatistics);
		return sb.toString();
	}
}
