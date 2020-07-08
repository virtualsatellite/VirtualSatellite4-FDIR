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

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.State;

public class CommentUtil {
	private static final String BELONGS_TO_FAULT_NODE = "belongs-to-fault-node";
	private static final String Y_POS_REL_TO_FAULT_NODE = "y-pos-rel-to-fault-node";
	private static final String X_POS_REL_TO_FAULT_NODE = "x-pos-rel-to-fault-node";

	private static final String IS_COMMENT = "is-comment";
	private static final String TRUE = "true";

	private CommentUtil() {
	}

	/**
	 * @param shape
	 * @param bean business object
	 * @return true if shape belongs to specified business object
	 */
	public static boolean shapeBelongsToEntity(Shape shape, Object bean) {
		String uuid = null;
		String propertyValue = Graphiti.getPeService().getPropertyValue(shape, BELONGS_TO_FAULT_NODE);
		if (propertyValue == null) {
			return false;
		}

		if (bean instanceof FaultTreeNode) {
			uuid = ((FaultTreeNode) bean).getUuid();
		} else if (bean instanceof State) {
			uuid = ((State) bean).getUuid();
		}
		return propertyValue.equals(uuid);
	}

	public static boolean isComment(Shape shape) {
		String propertyValue = Graphiti.getPeService().getPropertyValue(shape, IS_COMMENT);
		if (propertyValue == null) {
			return false;
		}
		return propertyValue.equals(TRUE);
	}

	/**
	 * @param context current move context
	 * @param shape comment shape
	 * @param businessObjectForPictogramElement
	 */
	public static void linkShapeWithEntity(IMoveShapeContext context, Shape shape, Object businessObjectForPictogramElement) {
		String faultUuid = null;

		if (businessObjectForPictogramElement instanceof FaultTreeNode) {
			faultUuid = ((FaultTreeNode) businessObjectForPictogramElement).getUuid();
		} else if (businessObjectForPictogramElement instanceof State) {
			faultUuid = ((State) businessObjectForPictogramElement).getUuid();
		}

		Graphiti.getPeService().setPropertyValue(shape, BELONGS_TO_FAULT_NODE, faultUuid);
		Graphiti.getPeService().setPropertyValue(shape, X_POS_REL_TO_FAULT_NODE, String.valueOf(context.getX()));
		Graphiti.getPeService().setPropertyValue(shape, Y_POS_REL_TO_FAULT_NODE, String.valueOf(context.getY()));
	}

	/**
	 * @param context current add context
	 * @param shape comment shape
	 * @param businessObjectForPictogramElement
	 */
	public static void linkShapeWithEntity(IAddContext context, ContainerShape shape, Object businessObjectForPictogramElement) {
		String faultUuid = null;

		if (businessObjectForPictogramElement instanceof FaultTreeNode) {
			faultUuid = ((FaultTreeNode) businessObjectForPictogramElement).getUuid();
		} else if (businessObjectForPictogramElement instanceof State) {
			faultUuid = ((State) businessObjectForPictogramElement).getUuid();
		}

		Graphiti.getPeService().setPropertyValue(shape, BELONGS_TO_FAULT_NODE, faultUuid);
		Graphiti.getPeService().setPropertyValue(shape, X_POS_REL_TO_FAULT_NODE, String.valueOf(context.getX()));
		Graphiti.getPeService().setPropertyValue(shape, Y_POS_REL_TO_FAULT_NODE, String.valueOf(context.getY()));
	}

	public static void setShapeDetached(Shape shape) {
		Graphiti.getPeService().setPropertyValue(shape, BELONGS_TO_FAULT_NODE, "");
	}

	/**
	 * @param containerShape
	 * @param bean
	 */
	public static void moveComments(ContainerShape containerShape, Object bean) {
		ContainerShape parent = containerShape.getContainer();

		EList<Shape> children = parent.getChildren();
		for (Object element : children) {
			Shape shape = (Shape) element;
			if (CommentUtil.isComment(shape) && CommentUtil.shapeBelongsToEntity(shape, bean)) {
				int xPos = Integer.parseInt(Graphiti.getPeService().getPropertyValue(shape, X_POS_REL_TO_FAULT_NODE));
				int yPos = Integer.parseInt(Graphiti.getPeService().getPropertyValue(shape, Y_POS_REL_TO_FAULT_NODE));
				Graphiti.getGaService().setLocation(shape.getGraphicsAlgorithm(), containerShape.getGraphicsAlgorithm().getX() + xPos, containerShape.getGraphicsAlgorithm().getY() + yPos);
			}
		}
	}
}
