/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents;

import java.util.Objects;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;

/**
 * Update feature for updating fault tree nodes in a fault tree diagram.
 * @author muel_s8
 *
 */

public class BasicEventsUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * @param fp the feature provider
	 */
	
	public BasicEventsUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return object instanceof BasicEvent && super.canUpdate(context);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		
		BasicEvent bean = (BasicEvent) getBusinessObjectForPictogramElement(pictogramElement);
		
        // retrieve name from business model
        String businessName = bean.getName();
		boolean updateNeeded = false;
        
        if (pictogramElement.getGraphicsAlgorithm() instanceof Ellipse) {
            ContainerShape cs = (ContainerShape) pictogramElement;
            for (Shape shape : cs.getChildren()) {
                if (shape.getGraphicsAlgorithm() instanceof Text) {
                	Text text = (Text) shape.getGraphicsAlgorithm();
                    String pictogramName = text.getValue();
                    updateNeeded |= !Objects.equals(pictogramName, businessName);
                }
            }
        } else if (pictogramElement.getGraphicsAlgorithm() instanceof Text) {
        	Text text = (Text) pictogramElement.getGraphicsAlgorithm();
            String pictogramName = text.getValue();
            updateNeeded = !Objects.equals(pictogramName, businessName);
        }
 
 
        // update needed, if changes have been found
        
        if (updateNeeded) {
            return Reason.createTrueReason("Out of date");
        } else {
            return Reason.createFalseReason();
        }
	}

	@Override
	public boolean update(IUpdateContext context) {
        // retrieve name from business model
        PictogramElement pictogramElement = context.getPictogramElement();
        BasicEvent bean = (BasicEvent) getBusinessObjectForPictogramElement(pictogramElement);
        String businessName = bean.getName();
        
        boolean changeDuringUpdate = false;
        
        // Set name in pictogram model
        ContainerShape cs = (ContainerShape) pictogramElement;
        if (pictogramElement.getGraphicsAlgorithm() instanceof Ellipse) {
	        for (Shape shape : cs.getChildren()) {
	            if (shape.getGraphicsAlgorithm() instanceof Text) {
	            	Text text = (Text) shape.getGraphicsAlgorithm();
	                text.setValue(businessName);
	                changeDuringUpdate = true;
	            }
	        } 
        } else if (pictogramElement.getGraphicsAlgorithm() instanceof Rectangle) {
        	Text text = (Text) ((ContainerShape) pictogramElement).getChildren().get(0).getGraphicsAlgorithm();
            text.setValue(bean.getHotFailureRateBean().getValueWithUnit());
            changeDuringUpdate = true;
        }
        
        if (changeDuringUpdate) {
        	layoutPictogramElement(pictogramElement);
        }
        
		return changeDuringUpdate;
	}

}
