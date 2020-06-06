/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov;

/**
 * This enum defines the possible types a markov state can have:
 * - MARKOVIAN: The state has continous time markovian transitions
 * - NONDET: The state has immediate nondeterministic transitions, which may also have 
 * @author muel_s8
 *
 */

public enum MarkovStateType {
	MARKOVIAN, NONDET, PROBABILISTIC
}
