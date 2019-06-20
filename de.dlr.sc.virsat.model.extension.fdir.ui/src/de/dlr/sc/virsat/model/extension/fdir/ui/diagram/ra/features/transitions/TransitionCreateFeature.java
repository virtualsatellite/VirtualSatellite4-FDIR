/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Feature for creating new transitions in a graphiti recovery automaton diagram.
 * @author muel_s8
 *
 */

public class TransitionCreateFeature extends AbstractCreateConnectionFeature {

	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public TransitionCreateFeature(IFeatureProvider fp) {
		super(fp, "Transition", "Create a transition");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		// Check if the desired anchors are properly linked
		Object source = getBusinessObjectForPictogramElement(context.getSourceAnchor());
		Object target = getBusinessObjectForPictogramElement(context.getTargetAnchor());
		
		if (source instanceof State && target instanceof State) {
			return DiagramHelper.hasDiagramWritePermission(context.getSourceAnchor());
		}
		return false;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {

		Connection newConnection = null;
		
		State source = (State) getBusinessObjectForPictogramElement(context.getSourceAnchor());
		State target = (State) getBusinessObjectForPictogramElement(context.getTargetAnchor());
		
		if (source != null && target != null) {
			Concept concept = source.getConcept();
			
			Transition transition = new Transition(concept);
			transition.setFrom(source);
			transition.setTo(target);
			RecoveryAutomaton ra = source.getParentCaBeanOfClass(RecoveryAutomaton.class);
			ra.getTransitions().add(transition);
			
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
			addContext.setNewObject(transition);
			newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		}
		
		return newConnection;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		Object source = getBusinessObjectForPictogramElement(context.getSourceAnchor());
		if (source instanceof State) {
			return DiagramHelper.hasDiagramWritePermission(context.getSourceAnchor());
		}
		return false;
	}

	@Override
	public String getCreateImageId() {
		return Transition.FULL_QUALIFIED_CATEGORY_NAME;
	}
	
}
