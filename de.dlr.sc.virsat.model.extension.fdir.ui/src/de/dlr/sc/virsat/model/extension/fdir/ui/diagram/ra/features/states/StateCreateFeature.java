/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatCreateFeature;
import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.Activator;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;

/**
 * This feature handles the creation of recovery automata states
 * @author muel_s8
 *
 */

public class StateCreateFeature extends VirSatCreateFeature {

	/**
	 * Default constructor
	 * @param fp the feature provider
	 */
	public StateCreateFeature(IFeatureProvider fp) {
		super(fp, "State", "Creates a new Recovery Automaton State");
	}
	
	@Override
	protected Command createCreateCommand(VirSatTransactionalEditingDomain ed, ICreateContext context) {
		Concept concept = DiagramHelper.getConcept(ed, Activator.getPluginId());
		
		StructuralElementInstance owningSei = DiagramHelper.getOwningStructuralElementInstance(context.getTargetContainer());
		BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance(owningSei);
		
		CompoundCommand cc = new CompoundCommand() {
			@Override
			public Collection<?> getResult() {
				return getCommandList().get(0).getResult();
			}
		};
		
		RecoveryAutomaton ra = beanSei.getFirst(RecoveryAutomaton.class);
		if (ra == null) {
			ra = new RecoveryAutomaton(concept);
			cc.append(beanSei.add(ed, ra));
		}
		
		State state = new State(concept);
		Command addCommand = ra.getStates().add(ed, state);
		cc.append(addCommand);
		cc.setLabel(addCommand.getLabel());
		return cc;
	}

	@Override
	public String getCreateImageId() {
		return State.FULL_QUALIFIED_CATEGORY_NAME;
	}
	
}
