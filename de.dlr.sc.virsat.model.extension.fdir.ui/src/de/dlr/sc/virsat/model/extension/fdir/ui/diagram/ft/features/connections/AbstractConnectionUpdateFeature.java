/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections;

import java.util.Objects;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.AbstractFaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Update feature for updating elements in an interface diagram.
 * @author muel_s8
 *
 */

public class AbstractConnectionUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * @param fp the feature provider
	 */
	
	public AbstractConnectionUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return (object instanceof FaultTreeEdge || object == null) && super.canUpdate(context);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		FreeFormConnection pictogramElement = (FreeFormConnection) context.getPictogramElement();
		
		AbstractFaultTreeEdge bean = (AbstractFaultTreeEdge) getBusinessObjectForPictogramElement(pictogramElement);
		
		if (bean == null) {
			return Reason.createTrueReason("Edge no longer exists");
		}
           
        FaultTreeNode ftnFrom = (FaultTreeNode) getBusinessObjectForPictogramElement(pictogramElement.getStart());
        boolean ieFromUpdateNeeded = !Objects.equals(ftnFrom, bean.getFrom());     
        if (ieFromUpdateNeeded) {
            return Reason.createTrueReason("Starting Fault Tree Node out of date");
        }
           
        FaultTreeNode ftnTo = (FaultTreeNode) getBusinessObjectForPictogramElement(pictogramElement.getEnd());
        boolean ieToUpdateNeeded = !Objects.equals(ftnTo, bean.getTo());     
        if (ieToUpdateNeeded) {
            return Reason.createTrueReason("Ending Fault Tree Node out of date");
        }
        
        return Reason.createFalseReason();
        
	}

	@Override
	public boolean update(IUpdateContext context) {
        // retrieve name from business model
		FreeFormConnection pictogramElement = (FreeFormConnection) context.getPictogramElement();
		AbstractFaultTreeEdge bean = (AbstractFaultTreeEdge) getBusinessObjectForPictogramElement(pictogramElement);
        
        // check if the interface still exists if not then we also remove the pictogram element
		if (bean == null) {
        	RemoveContext removeContext = new RemoveContext(pictogramElement);
			IRemoveFeature feature = getFeatureProvider().getRemoveFeature(removeContext);
			feature.remove(removeContext);
			return true;
		}
        
        String businessName = bean.getName();
        Text text = (Text) pictogramElement.getConnectionDecorators().get(0).getGraphicsAlgorithm();
        text.setValue(businessName);
        
        // Now update the start and end anchors
        FaultTreeNode ftnFrom = bean.getFrom();
        FaultTreeNode ftnTo = bean.getTo();
        FaultTreeNode[] ftns = { ftnFrom, ftnTo };
		
		final PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(ftns);
		
		boolean foundStart = false;
		boolean foundEnd = false;
		
		for (PictogramElement pe : pes) {
			if (pe instanceof Anchor) {
				FaultTreeNode ftn = (FaultTreeNode) getBusinessObjectForPictogramElement(pe);
				if (Objects.equals(ftn, ftnFrom)) {
					pictogramElement.setStart((Anchor) pe);
					foundStart = true;
				}
				
				if (Objects.equals(ftn, ftnTo)) {
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
