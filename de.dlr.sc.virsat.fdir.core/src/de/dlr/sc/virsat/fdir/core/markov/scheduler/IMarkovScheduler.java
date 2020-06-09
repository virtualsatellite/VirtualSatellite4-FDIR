/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.markov.scheduler;

import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;

/**
 * Interface for schedulers on markov automata
 * @author muel_s8
 *
 * @param <S>
 */

public interface IMarkovScheduler<S extends MarkovState> {
	
	/**
	 * Computes the optimal schedule for a given ma
	 * @param ma the ma
	 * @param initialMa the initial state
	 * @return the schedule
	 */
	Map<S, List<MarkovTransition<S>>> computeOptimalScheduler(ScheduleQuery<S> scheduleQuery);
}
