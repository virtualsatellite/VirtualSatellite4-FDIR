/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeAddFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeGraphicsFactory;

/**
 * This class handles the addition of features
 * @author muel_s8
 *
 */

public class CommentAddFeature extends AbstractAddFeature {

	public static final IColorConstant COMMENT_FOREGROUND = IColorConstant.GRAY;
	public static final IColorConstant COMMENT_BACKGROUND = IColorConstant.YELLOW;
	public static final int LINE_WIDTH = 1;

	public static final String IS_COMMENT_KEY = "is-comment";

	public static final int PADDING_X = 10;
	public static final int PADDING_Y = 10;

	public static final double TEXT_TRANSPARENCY = 0.3;
	public static final double BOX_TRANSPARENCY = 0.3;

	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */

	public CommentAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		ContainerShape cs = context.getTargetContainer();
		return context.getNewObject() instanceof String && cs instanceof ContainerShape && DiagramHelper.hasDiagramWritePermission(cs);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		String comment = (String) context.getNewObject();
		ContainerShape containerShape = null;

		ContainerShape target = context.getTargetContainer();
		ContainerShape targetParent = target.getContainer();
		if (targetParent != null) {
			containerShape = Graphiti.getPeService().createContainerShape(context.getTargetContainer().getContainer(), true);
		} else {
			containerShape = Graphiti.getPeService().createContainerShape(context.getTargetContainer(), true);
		}

		ILocation locationRelativeToDiagram = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(context.getTargetContainer());

		Font font = Graphiti.getGaService().manageDefaultFont(getDiagram());
		IDimension textDimension = GraphitiUi.getUiLayoutService().calculateTextSize(comment, font);
		int width = textDimension.getWidth() + PADDING_X * 2;
		int height = textDimension.getHeight() + PADDING_Y * 2;

		// Create and set elliptic graphics algorithm
		RoundedRectangle roundedRectangle = Graphiti.getGaService().createRoundedRectangle(containerShape, FaultTreeNodeGraphicsFactory.CORNER_WIDTH, FaultTreeNodeGraphicsFactory.CORNER_HEIGHT);
		roundedRectangle.setForeground(manageColor(COMMENT_FOREGROUND));
		roundedRectangle.setBackground(manageColor(COMMENT_BACKGROUND));
		roundedRectangle.setLineWidth(LINE_WIDTH);
		roundedRectangle.setLineStyle(LineStyle.DASH);
		roundedRectangle.setTransparency(BOX_TRANSPARENCY);
		Graphiti.getGaService().setLocationAndSize(roundedRectangle, locationRelativeToDiagram.getX() + context.getX(), locationRelativeToDiagram.getY() + context.getY(), width, height);

		// Create the comment text
		Shape commentTextShape = Graphiti.getPeService().createShape(containerShape, false);
		Text commentText = Graphiti.getGaService().createText(commentTextShape, comment);
		commentText.setForeground(manageColor(FaultTreeNodeAddFeature.FTN_TEXT_FOREGROUND));
		commentText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		commentText.setFont(font);
		commentText.setTransparency(TEXT_TRANSPARENCY);
		commentText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		Graphiti.getGaService().setLocationAndSize(commentText, 0, 0, width, height);

		Graphiti.getPeService().setPropertyValue(containerShape, IS_COMMENT_KEY, "true");
		Graphiti.getPeService().setPropertyValue(commentTextShape, IS_COMMENT_KEY, "true");

		//Associate Comment with Fault Node
		Object businessObjectForPictogramElement = getBusinessObjectForPictogramElement(target);
		CommentUtil.linkShapeWithFaultTreeNode(context, containerShape, businessObjectForPictogramElement);

		return containerShape;
	}

}
