/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import java.util.Arrays;
import java.util.List;

import de.dlr.sc.virsat.fdir.core.util.IStatistics;

/**
 * This class holds statistical data for a call to the model checker
 * @author muel_s8
 *
 */

public class ModelCheckingStatistics implements IStatistics {
	// CHECKSTYLE:OFF
	public long time;
	// CHECKSTYLE:ON
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Model Checking Statistics: \n");
		sb.append("\t* Model Checking Time:\t\t" 	+ time + "\n");
		return sb.toString();
	}
	
	/**
	 * Composes the statistics with statistics of another call
	 * @param other statistics of another call to the model checker
	 */
	public void compose(ModelCheckingStatistics other) {
		this.time += other.time;
	}
	
	public static List<String> getColumns() {
		return Arrays.asList("time");
	}
	
	@Override
	public List<String> getValues() {
		return Arrays.asList(getPrintValue(time));
	}
}
