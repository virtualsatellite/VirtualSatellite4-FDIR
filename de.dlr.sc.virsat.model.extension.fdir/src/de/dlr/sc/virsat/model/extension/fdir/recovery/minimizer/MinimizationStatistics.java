/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import de.dlr.sc.virsat.fdir.core.util.IStatistics;

/**
 * This class holds internal statistics of ARecoveryMinimizers.
 * @author muel_s8
 *
 */

public class MinimizationStatistics implements IStatistics {
	//CHECKSTYLE:OFF
	public long time = NA;
	public int removedStates = NA;
	public int removedTransitions = NA;
	//CHECKSTYLE:ON
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Minimization Statistics: \n");
		sb.append("\t* Minimization Time  :\t\t" 	+ getPrintValue(time) + "\n");
		sb.append("\t* #RemovedStates     :\t\t" 	+ getPrintValue(removedStates) + "\n");
		sb.append("\t* #RemovedTransitions:\t\t" 	+ getPrintValue(removedTransitions) + "\n");
		return sb.toString();
	}
	
	/**
	 * Composes the statistics with the statistics of another call of a minimizer
	 * @param other statistics of another minimizion call
	 */
	public void compose(MinimizationStatistics other) {
		time = composeAdd(time, other.time);
		removedStates = composeAdd(removedStates, other.removedStates);
		removedTransitions = composeAdd(removedTransitions, other.removedTransitions);
	}
}
