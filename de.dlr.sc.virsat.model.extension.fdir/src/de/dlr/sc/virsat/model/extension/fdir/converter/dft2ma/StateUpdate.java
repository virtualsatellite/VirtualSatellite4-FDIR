/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;

public class StateUpdate {
	private DFTState state;
	private IDFTEvent event;
	private double rate;
	
	public StateUpdate(DFTState state, IDFTEvent event, int multiplier) {
		this.state = state;
		this.event = event;
		this.rate = event.getRate(state) * multiplier;
	}
	
	public DFTState getState() {
		return state;
	}
	
	public IDFTEvent getEvent() {
		return event;
	}
	
	public double getRate() {
		return rate;
	}
	
	@Override
	public String toString() {
		return state.toString() + " --- " + event.toString() + " : " + rate + " ---> ? ";
	}
	
	/**
	 * Contains the results from an executed state update
	 *
	 */
	public static class StateUpdateResult {
		private Map<DFTState, List<RecoveryAction>> mapStateToRecoveryActions = new HashMap<>();
		private List<DFTState> succs = new ArrayList<>();
		private List<FaultTreeNode> changedNodes = new ArrayList<>();
		
		private DFTState baseSucc;

		/**
		 * Standard constructor
		 * @param state the base state
		 */
		StateUpdateResult(StateUpdate stateUpdate) {
			baseSucc = stateUpdate.state.copy();
			baseSucc.setRecoveryStrategy(stateUpdate.state.getRecoveryStrategy());
			
			succs.add(baseSucc);
			mapStateToRecoveryActions.put(baseSucc, Collections.emptyList());
		}
		
		public Map<DFTState, List<RecoveryAction>> getMapStateToRecoveryActions() {
			return mapStateToRecoveryActions;
		}
		
		public List<DFTState> getSuccs() {
			return succs;
		}
		
		public List<FaultTreeNode> getChangedNodes() {
			return changedNodes;
		}
		
		public DFTState getBaseSucc() {
			return baseSucc;
		}
		
		public void setBaseSucc(DFTState baseSucc) {
			this.baseSucc = baseSucc;
		}
	}
}