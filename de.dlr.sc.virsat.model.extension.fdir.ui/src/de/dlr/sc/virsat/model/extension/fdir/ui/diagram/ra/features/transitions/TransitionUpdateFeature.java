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

import java.util.Objects;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Update feature for updating transitions in a recovery automaton diagram.
 * @author muel_s8
 *
 */

public class TransitionUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * @param fp the feature provider
	 */
	
	public TransitionUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return (object instanceof Transition || object == null) && super.canUpdate(context);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Connection pictogramElement = (Connection) context.getPictogramElement();
		
		Transition bean = (Transition) getBusinessObjectForPictogramElement(pictogramElement);
		
		if (bean == null) {
			return Reason.createTrueReason("Edge no longer exists");
		}
		
		String label = bean.toString();
		Text text = (Text) pictogramElement.getConnectionDecorators().get(0).getGraphicsAlgorithm();
		boolean labelUpdateNeeded = !Objects.equals(label, text.getValue());
		if (labelUpdateNeeded) {
			return Reason.createTrueReason("Transition label out of date");
		}
           
		State stateFrom = (State) getBusinessObjectForPictogramElement(pictogramElement.getStart());
        boolean ieFromUpdateNeeded = !Objects.equals(stateFrom, bean.getFrom());     
        if (ieFromUpdateNeeded) {
            return Reason.createTrueReason("Starting Node out of date");
        }
           
        State stateTo = (State) getBusinessObjectForPictogramElement(pictogramElement.getEnd());
        boolean ieToUpdateNeeded = !Objects.equals(stateTo, bean.getTo());     
        if (ieToUpdateNeeded) {
            return Reason.createTrueReason("Ending Node out of date");
        }
        
        return Reason.createFalseReason();
        
	}

	@Override
	public boolean update(IUpdateContext context) {
        // retrieve name from business model
		Connection pictogramElement = (Connection) context.getPictogramElement();
		Transition bean = (Transition) getBusinessObjectForPictogramElement(pictogramElement);
        
        // check if the interface still exists if not then we also remove the pictogram element
		if (bean == null) {
        	RemoveContext removeContext = new RemoveContext(pictogramElement);
			IRemoveFeature feature = getFeatureProvider().getRemoveFeature(removeContext);
			feature.remove(removeContext);
			return true;
		}
        
        String label = bean.toString();
        Text text = (Text) pictogramElement.getConnectionDecorators().get(0).getGraphicsAlgorithm();
        text.setValue(label);
        
        // Now update the start and end anchors
        State stateFrom = bean.getFrom();
        State stateTo = bean.getTo();
        State[] ftns = { stateFrom, stateTo };
		
		final PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(ftns);
		
		boolean foundStart = false;
		boolean foundEnd = false;
		
		for (PictogramElement pe : pes) {
			if (pe instanceof Anchor) {
				State state = (State) getBusinessObjectForPictogramElement(pe);
				if (Objects.equals(state, stateFrom)) {
					pictogramElement.setStart((Anchor) pe);
					foundStart = true;
				}
				
				if (Objects.equals(state, stateTo)) {
					pictogramElement.setEnd((Anchor) pe);
					foundEnd = true;
				}
			}
		}
		
		if (!foundStart || !foundEnd) {
			// We have not found any anchors for the start or the ending location
			// This means that the Interface has been changed from the outside in a way
			// Such that it no longer points to an interface end that is currently on the diagram
			// As such we now remove this Interface from the diagram as well
        	RemoveContext removeContext = new RemoveContext(pictogramElement);
			IRemoveFeature feature = getFeatureProvider().getRemoveFeature(removeContext);
			feature.remove(removeContext);
		}
        
		return true;
	}

}
