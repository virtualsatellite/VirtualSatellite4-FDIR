/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.HashSet;
import java.util.Set;

/**
 * The minimumcut set metric describes minimum sets of events that 
 * can reach a fail state
 * @author muel_s8
 *
 */

public class MinimumCutSet implements IQualitativeMetric, IBaseMetric {

	public static final MinimumCutSet MINCUTSET = new MinimumCutSet(0);
	
	private long maxSize;
	
	/**
	 * Standard constructor
	 * @param maxSize the maximum mincut set
	 */
	public MinimumCutSet(long maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public void accept(IBaseMetricVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * The maximum mincut set
	 * @return the maximum mincut set
	 */
	public long getMaxSize() {
		return maxSize;
	}
	
	/**
	 * Computes the cartesian product on the given mincut sets
	 * @param minCutSets the mincut sets to combine
	 * @return the result of performing the cartesian product on the mincust sets
	 */
	public Set<Set<Object>> cartesianComposition(@SuppressWarnings("unchecked") Set<Set<Object>>... minCutSets) {
		return cartesianComposition(0, minCutSets);
	}
	
	/**
	 * Computes the cartesian product on the given mincut sets as follows:
	 * CARTESIAN_PRODUCT { } := { { } }
	 * CARTESIAN_PRODUCT { {a_1, ..., a_n} , A_1, ..., A_n } := { a_1 UNION A, ..., a_n UNION A MID A IN CARTESIAN_PRODUCT { A_1, ..., A_n } }
	 * @param index the current set to create all permuatations for
	 * @param minCutSets the mincut sets to compose
	 * @return the cartesian product of the given mincut sets
	 */
	private Set<Set<Object>> cartesianComposition(int index, @SuppressWarnings("unchecked") Set<Set<Object>>... minCutSets) {
		Set<Set<Object>> productMinCuts = new HashSet<>();
		if (index == minCutSets.length) {
			productMinCuts.add(new HashSet<>());
		} else {
			for (Set<Object> minCutSet : minCutSets[index]) {
				for (Set<Object> minCutSetOther : cartesianComposition(index + 1, minCutSets)) {
					minCutSetOther.addAll(minCutSet);
					if (minCutSetOther.size() <= maxSize || maxSize == 0) {
						productMinCuts.add(minCutSetOther);
					}
				}
			}
		}
		return productMinCuts;
	}
}
