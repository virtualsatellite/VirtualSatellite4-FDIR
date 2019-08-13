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

import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityRequirement;


/**
 * Interface for defining synthesizers for recovery automatons.
 * @author muel_s8
 *
 */

public interface ISynthesizer {
	
	/**
	 * Synthesizes a recovery automaton.
	 * @param fault the fault
	 * @param requirements map of requirements that need each fault needs to fulfill
	 * @return the synthesized recovery automaton
	 */
	RecoveryAutomaton synthesize(Fault fault, Map<ReliabilityRequirement, Fault> requirements);
}
