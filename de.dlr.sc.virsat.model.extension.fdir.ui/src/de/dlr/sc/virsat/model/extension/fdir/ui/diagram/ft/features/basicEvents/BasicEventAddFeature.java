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

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatAddShapeFeature;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeAddFeature;

/**
 * Feature handling the addition of fault tree nodes.
 * @author muel_s8
 *
 */

public class BasicEventAddFeature extends VirSatAddShapeFeature {
	
	public static final IColorConstant ELEMENT_TEXT_FOREGROUND = IColorConstant.BLACK;
	
	public static final IColorConstant ELEMENT_FOREGROUND = IColorConstant.GRAY;
	
	public static final IColorConstant ELEMENT_BACKGROUND = IColorConstant.LIGHT_ORANGE;
	
	public static final int DEFAULT_SIZE = 60;
	
	public static final int LINE_WIDTH = 2;

	public static final int FAILURE_RATE_SIZE_Y = 16;
	
	public static final int FONT_SIZE = 7;
	
	/**
	 * Default consturctor
	 * @param fp the feature provider
	 */
	
	public BasicEventAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isDiagramContainer = context.getTargetContainer() instanceof Diagram;
		
		BasicEvent addedBasicEvent = (BasicEvent) new BasicEvent((CategoryAssignment) context.getNewObject());
		for (Shape shape : getDiagram().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(shape);
			if (Objects.equals(bo, addedBasicEvent)) {
				return false;
			}
		}
		
		// Check if there exists a picogram mapped to the parent fault in the diagram
		Fault[] faults = { addedBasicEvent.getFault() };
		PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(faults);
		if (pes.length == 0) {
			// If not, then we disallow adding the BE since it would have no fault to be associated to
			return false;
		}
		
		return super.canAdd(context) && isDiagramContainer;
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		BasicEvent addedBasicEvent = (BasicEvent) new BasicEvent((CategoryAssignment) context.getNewObject());
		
		// Create a container shape with a rounded rectangle
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
		
		IGaService gaService = Graphiti.getGaService();
		
		String name = addedBasicEvent.getName();
		
		Font font = gaService.manageFont(getDiagram(), name, FONT_SIZE, false, false);
		IDimension nameDimension = GraphitiUi.getUiLayoutService().calculateTextSize(name, font);
		
		// Make it so that we have enough width and height to display the sub elements such as name, interfaceends, etc
		int neededSize = Math.max(nameDimension.getWidth(), nameDimension.getHeight());
		int size = DEFAULT_SIZE >= neededSize ? DEFAULT_SIZE : neededSize;
		
		// Create and set elliptic graphics algorithm
		Ellipse circle = gaService.createEllipse(containerShape);
		circle.setForeground(manageColor(ELEMENT_FOREGROUND));
		circle.setBackground(manageColor(ELEMENT_BACKGROUND));
		circle.setLineWidth(LINE_WIDTH);
		gaService.setLocationAndSize(circle, context.getX(), context.getY(), size, size);
		link(containerShape, addedBasicEvent);
		
		// Create Shape with Name Label
		Shape nameShape = peCreateService.createShape(containerShape, false);
		Text nameText = gaService.createText(nameShape, name);
		nameText.setForeground(manageColor(ELEMENT_TEXT_FOREGROUND));
		nameText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		nameText.setFont(font);
		nameText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		gaService.setLocationAndSize(nameText, 0, 0, size, size);
		link(nameShape, addedBasicEvent);

		// Create shape for rectangle containing the failure rate
		ContainerShape failureRateRectShape = peCreateService.createContainerShape(containerShape, false);
		Rectangle failureRateRect = gaService.createRectangle(failureRateRectShape);
		failureRateRect.setForeground(manageColor(ELEMENT_FOREGROUND));
		failureRateRect.setBackground(manageColor(ELEMENT_BACKGROUND));
		failureRateRect.setLineWidth(LINE_WIDTH);
		gaService.setLocationAndSize(failureRateRect, 0, size - FAILURE_RATE_SIZE_Y, size, FAILURE_RATE_SIZE_Y);
		link(failureRateRectShape, addedBasicEvent);
		
		// Create text field for the failure rate
		Shape failureRateTextShape = peCreateService.createShape(failureRateRectShape, false);
		Text failureRateText = gaService.createText(failureRateTextShape, String.valueOf(addedBasicEvent.getHotFailureRateBean().getValueWithUnit()));
		failureRateText.setForeground(manageColor(ELEMENT_TEXT_FOREGROUND));
		failureRateText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		failureRateText.setFont(font);
		failureRateText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		gaService.setLocationAndSize(failureRateText, 0, 0, failureRateRect.getWidth(), failureRateRect.getHeight());
		link(failureRateTextShape, addedBasicEvent.getHotFailureRateBean());
		
		// Create the anchor
		Anchor anchor = AnchorUtil.createAnchor(containerShape, manageColor(FaultTreeNodeAddFeature.PORT_COLOR), AnchorType.OUTPUT);
		link(anchor, addedBasicEvent);
		
		Fault[] faults = { addedBasicEvent.getFault() };
		
		PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(faults);
		
		// Get the PEs we want to automatically connect this failure mode to
		// This should only be the faults containing the failure modes
		for (PictogramElement pe : pes) {
			if (pe instanceof ContainerShape) {
				ContainerShape cs = (ContainerShape) pe;
				Anchor targetAnchor = AnchorUtil.getFreeAnchors(cs, AnchorType.INPUT).get(0);
				AddConnectionContext addLineContext = new AddConnectionContext(anchor, targetAnchor);
				addLineContext.setTargetContainer(getDiagram());
				addLineContext.setNewObject(addedBasicEvent);
				IAddFeature feature = getFeatureProvider().getAddFeature(addLineContext);
				feature.add(addLineContext);
				updatePictogramElement(cs);
			}
		}
		return containerShape;
	}

}
