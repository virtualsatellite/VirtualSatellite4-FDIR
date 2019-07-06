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
 * Generic creation feature for transitions
 * @author muel_s8
 *
 */

public abstract class TransitionCreateFeature extends AbstractCreateConnectionFeature {

	/**
	 * Standard consturctor
	 * @param fp the feature provider
	 * @param name the name
	 * @param description the description
	 */
	public TransitionCreateFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
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
		
		State source = (State) getBusinessObjectForPictogramElement(context.getSourceAnchor());
		State target = (State) getBusinessObjectForPictogramElement(context.getTargetAnchor());
		
		if (source == null || target == null) {
			return null;
		}
		
		Concept concept = source.getConcept();
		
		Transition transition = createTransition(concept);
		transition.setFrom(source);
		transition.setTo(target);
		RecoveryAutomaton ra = source.getParentCaBeanOfClass(RecoveryAutomaton.class);
		ra.getTransitions().add(transition);
		
		AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
		addContext.setNewObject(transition);
		Connection newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		
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
	
	/**
	 * Creates the actual transition data
	 * @param concept the concept
	 * @return the created transition
	 */
	public abstract Transition createTransition(Concept concept);
}
