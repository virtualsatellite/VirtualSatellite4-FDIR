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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
	public Map<String, Integer> minimizers = new TreeMap<>();
	//CHECKSTYLE:ON
	
	public MinimizationStatistics(ARecoveryAutomatonMinimizer minimizer) {
		for (String name : minimizer.getMinimizerNames()) {
			minimizers.put(name, NA);
		}
	}
	
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
		for (Entry<String, Integer> entry : other.minimizers.entrySet()) {
			minimizers.merge(entry.getKey(), entry.getValue(), (v1, v2) -> v1 == NA ? v2 : v1 + v2);
		}
	}
	
	public List<String> getAllColumns() {
		List<String> columns = new ArrayList<>(getColumns());
		columns.addAll(minimizers.keySet());
		return columns;
	}
	
	public static List<String> getColumns() {
		return Arrays.asList("time", "removedStates", "removedTransitions");
	}
	
	@Override
	public List<String> getValues() {
		List<String> values = new ArrayList<>();
		values.add(getPrintValue(time));
		values.add(getPrintValue(removedStates));
		values.add(getPrintValue(removedTransitions));
		for (Integer removedStates : minimizers.values()) {
			values.add(getPrintValue(removedStates));
		}
		return values;
	}
}
