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

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatLayoutFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;

/**
 * Feature for layouting fault tree nodes.
 * @author muel_s8
 *
 */

public class BasicEventsLayoutFeature extends VirSatLayoutFeature {

	/**
	 * Default Constructor
	 * @param fp the feature provider
	 */
	
	public BasicEventsLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		PictogramElement pe = context.getPictogramElement();		
		Object businessObject = getBusinessObjectForPictogramElement(pe);
		return businessObject instanceof BasicEvent && super.canLayout(context);
	}

	@Override
	public boolean layout(ILayoutContext context) {
		boolean anythingChanged = false;
		
		PictogramElement pe = context.getPictogramElement();
		
		if (!(pe instanceof ContainerShape)) {
			return false;
		}
		
		ContainerShape containerShape = (ContainerShape) pe;
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		
		int containerWidth = containerGa.getWidth();
		int containerHeight = containerGa.getHeight();
		
		IGaService gaService = Graphiti.getGaService();
		
		// Adjust the size of the attached shapes
		for (Shape shape : containerShape.getChildren()) {
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IDimension size = gaService.calculateSize(ga);
			
			if (containerWidth != size.getWidth()) {
				gaService.setWidth(ga, containerWidth);
				anythingChanged = true;
			}
			
			if (ga instanceof MultiText) {
				if (containerHeight != size.getHeight()) {
					gaService.setHeight(ga, containerHeight);
					anythingChanged = true;
				}
			} else {
				int supposedY = containerHeight - size.getHeight();
				if (supposedY != ga.getY()) {
					gaService.setLocation(ga, ga.getX(), supposedY);
					anythingChanged = true;
				}
			}
			
			if (shape instanceof ContainerShape) {
				ContainerShape cs = (ContainerShape) shape;
				for (Shape shapeChild : cs.getChildren()) {
					GraphicsAlgorithm gaChild = shapeChild.getGraphicsAlgorithm();
					if (containerWidth != gaChild.getWidth()) {
						gaService.setWidth(gaChild, containerWidth);
						anythingChanged = true;
					}
				}
			}
		}
		
		return anythingChanged;
	}
}
