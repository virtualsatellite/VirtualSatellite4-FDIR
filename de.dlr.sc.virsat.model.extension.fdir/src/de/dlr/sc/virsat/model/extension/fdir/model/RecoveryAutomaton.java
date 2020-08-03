/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ARecoveryAutomatonMinimizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizer;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */
public class RecoveryAutomaton extends ARecoveryAutomaton {

	/**
	 * Constructor of Concept Class
	 */
	public RecoveryAutomaton() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate a CategoryAssignment in
	 * the background from the given concept
	 * 
	 * @param concept
	 *            the concept where it will find the correct Category to instantiate
	 *            from
	 */
	public RecoveryAutomaton(Concept concept) {
		super(concept);
	}

	/**
	 * Constructor of Concept Class that can be initialized manually by a given
	 * Category Assignment
	 * 
	 * @param categoryAssignment
	 *            The category Assignment to be used for background initialization
	 *            of the Category bean
	 */
	public RecoveryAutomaton(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	/**
	 * Converts this automaton into a string adhering to the .dot format.
	 * 
	 * @return a string representation of the automaton in the .dot format.
	 */
	public String toDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph ra {\n");

		for (State state : getStates()) {
			sb.append(state.toString() + " [label=" + state.toString() + "]\n");
		}

		for (Transition transition : getTransitions()) {
			sb.append(transition.getFrom().toString() + " -> " + transition.getTo().toString() + " [label=\""
					+ transition.toString() + "\"]\n");
		}

		sb.append("}");
		return sb.toString();
	}

	/**
	 * 
	 * @param editingDomain editing domain
	 * @return command
	 */
	public Command performMinimize(TransactionalEditingDomain editingDomain) {
		ARecoveryAutomatonMinimizer minimizer = ComposedMinimizer.createDefaultMinimizer();
		return new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				minimizer.minimize(RecoveryAutomaton.this);
			}
		};
	}
	
	/**
	 * Maps all references from generated nodes to references of the generator nodes
	 * @param ra the recovery automaton
	 * @param mapGeneratedToGenerator from the generated fault tree nodes to the generated ones
	 */
	public void remapToGeneratorNodes(Map<FaultTreeNode, FaultTreeNode> mapGeneratedToGenerator) {
		for (Transition t : getTransitions()) {
			if (t instanceof FaultEventTransition) {
				FaultEventTransition fet = (FaultEventTransition) t;
				List<FaultTreeNode> generatorGuards = new ArrayList<>();
				
				for (FaultTreeNode guard : fet.getGuards()) {
					if (guard.getTypeInstance() != null) {
						FaultTreeNode generatorGuard = mapGeneratedToGenerator.get(guard);
						generatorGuards.add(generatorGuard);
					}
				}
				fet.getGuards().clear();
				fet.getGuards().addAll(generatorGuards);
			}
			
		    for (RecoveryAction recoveryAction : t.getRecoveryActions()) {
		    	if (recoveryAction instanceof ClaimAction) {
		    		ClaimAction claimAction = (ClaimAction) recoveryAction;
		    		claimAction.setClaimSpare(mapGeneratedToGenerator.get(claimAction.getClaimSpare()));
		    		claimAction.setSpareGate((SPARE) mapGeneratedToGenerator.get(claimAction.getSpareGate()));
		    	} else if (recoveryAction instanceof FreeAction) {
		    		FreeAction freeAction = (FreeAction) recoveryAction;
		    		freeAction.setFreeSpare(mapGeneratedToGenerator.get(freeAction.getFreeSpare()));
		    	}
		    }
		}
	}

}
