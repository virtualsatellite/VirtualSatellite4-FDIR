/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;

/**
 * This class contains the state space generation results from a semantical node update
 * @author muel_s8
 *
 */

public class GenerationResult {
	private List<DFTState> generatedStates = new ArrayList<>();
	private Map<DFTState, List<RecoveryAction>> mapStateToRecoveryActions;
	private DFTState generatorState;
	
	/**
	 * Standard constructor
	 * @param generatorState the generator state
	 * @param mapStateToRecoveryActions injected map
	 */
	public GenerationResult(DFTState generatorState, Map<DFTState, List<RecoveryAction>> mapStateToRecoveryActions) {
		this.generatorState = generatorState;
		this.mapStateToRecoveryActions = mapStateToRecoveryActions;
	}

	/**
	 * Gets the set of states generated during the semantical update
	 * @return the set of newly generated states
	 */
	public List<DFTState> getGeneratedStates() {
		return generatedStates;
	}
	
	/**
	 * Gets the generator state
	 * @return the generator state
	 */
	public DFTState getGeneratorState() {
		return generatorState;
	}
	
	/**
	 * A map of recovery actions detailing which actions to apply to go to the desired state
	 * @return recovery actions needed to move to the newly generated states
	 */
	public Map<DFTState, List<RecoveryAction>> getMapStateToRecoveryActions() {
		return mapStateToRecoveryActions;
	}
}
