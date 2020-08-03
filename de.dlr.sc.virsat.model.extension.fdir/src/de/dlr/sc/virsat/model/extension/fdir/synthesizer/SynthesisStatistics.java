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

import java.util.Arrays;
import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.algorithm.MarkovAutomatonBuildStatistics;
import de.dlr.sc.virsat.fdir.core.util.IStatistics;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.MinimizationStatistics;

/**
 * This class holds statistics data for synthesizers
 */

public class SynthesisStatistics implements IStatistics {
	//CHECKSTYLE:OFF
	public long time = NA;
	public int countModules = NA;
	public int countTrimmedModules = NA;
	public int maxModuleSize = NA;
	public int maxModuleRaSize = NA;
	public final MarkovAutomatonBuildStatistics maBuildStatistics = new MarkovAutomatonBuildStatistics();
	public final MinimizationStatistics minimizationStatistics = new MinimizationStatistics();
	//CHECKSTYLE:ON
	
	/**
	 * Composes the statistics with the statistics of another call of a synthesizer
	 * @param other statistics of another synthesizer
	 */
	public void compose(SynthesisStatistics other) {
		this.maxModuleRaSize = Math.max(maxModuleRaSize, other.maxModuleRaSize);
		this.maBuildStatistics.compose(other.maBuildStatistics);
		this.minimizationStatistics.compose(other.minimizationStatistics);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Synthesizer Statistics: \n");
		sb.append("\t* Synthesis Time   :\t\t" 		+ getPrintValue(time) + "\n");
		sb.append("\t* #Modules         :\t\t" 		+ getPrintValue(countModules) + "\n");
		sb.append("\t* #Trimmed         :\t\t" 		+ getPrintValue(countTrimmedModules) + "\n");
		sb.append("\t* Largest Module   :\t\t" 		+ getPrintValue(maxModuleSize) + "\n");
		sb.append("\t* Largest Module RA:\t\t" 		+ getPrintValue(maxModuleRaSize) + "\n");
		sb.append(maBuildStatistics);
		sb.append(minimizationStatistics);
		return sb.toString();
	}
	
	public static List<String> getColumns() {
		return Arrays.asList("time", "countModules", "countTrimmedModules", "maxModuleSize", "maxModuleRaSize");
	}
	
	@Override
	public List<String> getValues() {
		return Arrays.asList(getPrintValue(time), getPrintValue(countModules), getPrintValue(countTrimmedModules), 
				getPrintValue(maxModuleSize), getPrintValue(maxModuleRaSize));
	}
}
