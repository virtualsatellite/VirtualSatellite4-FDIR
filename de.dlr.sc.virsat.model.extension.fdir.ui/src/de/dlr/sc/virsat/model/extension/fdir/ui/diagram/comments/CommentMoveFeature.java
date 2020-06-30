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

import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatMoveShapeFeature;
import de.dlr.sc.virsat.graphiti.util.DiagramHelper;

/**
 * This class handles the drag & drop behavior of Fault Tree nodes for easy reconnection
 * @author muel_s8
 *
 */
public class CommentMoveFeature extends VirSatMoveShapeFeature {

	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */
	public CommentMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		ContainerShape target = context.getTargetContainer();
		if (context.getSourceContainer() != null && target != null) {
			return DiagramHelper.hasDiagramWritePermission(context.getPictogramElement()) && (target instanceof ContainerShape);
		}
		return false;
	}

	@Override
	public void moveShape(IMoveShapeContext context) {
		Shape shape = context.getShape();
		ContainerShape target = context.getTargetContainer();
		ContainerShape targetParent = target.getContainer();

		ILocation locationRelativeToDiagram = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(target);

		if (targetParent != null && target instanceof ContainerShape) {
			Graphiti.getGaService().setLocation(shape.getGraphicsAlgorithm(), locationRelativeToDiagram.getX() + context.getX(), locationRelativeToDiagram.getY() + context.getY());
			Graphiti.getPeService().sendToFront(shape);

			Object businessObjectForPictogramElement = getBusinessObjectForPictogramElement(target);
			CommentUtil.linkShapeWithFaultTreeNode(context, shape, businessObjectForPictogramElement);
		} else if (targetParent == null && target instanceof Diagram) {
			CommentUtil.setShapeDetached(shape);
			super.moveShape(context);
		} else {
			super.moveShape(context);
		}
	}
}
