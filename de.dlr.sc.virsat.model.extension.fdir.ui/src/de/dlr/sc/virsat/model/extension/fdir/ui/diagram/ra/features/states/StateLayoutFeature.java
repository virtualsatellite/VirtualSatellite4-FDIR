/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.states;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatLayoutFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.comments.CommentUtil;

public class StateLayoutFeature extends VirSatLayoutFeature {

	private static final String Y_POS_REL_TO_FAULT_NODE = "y-pos-rel-to-fault-node";
	private static final String X_POS_REL_TO_FAULT_NODE = "x-pos-rel-to-fault-node";

	/**
	 * Default Constructor
	 * @param fp the feature provider
	 */

	public StateLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean layout(ILayoutContext context) {
		boolean anythingChanged = false;

		PictogramElement pe = context.getPictogramElement();
		State bean = (State) getBusinessObjectForPictogramElement(pe);

		if (pe instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pe;
			anythingChanged |= layoutComments(containerShape, bean);
		}
		return anythingChanged;
	}


	/**
	 * @param containerShape to layout comments for
	 * @param bean business object of pictogram
	 * @return true iff there is any change
	 */
	private boolean layoutComments(ContainerShape containerShape, State bean) {
		boolean anythingChanged = false;
		ContainerShape parent = containerShape.getContainer();

		EList<Shape> children = parent.getChildren();
		for (Object element : children) {
			Shape shape = (Shape) element;
			if (CommentUtil.isComment(shape) && CommentUtil.shapeBelongsToStateNode(shape, bean)) {
				int xPos = Integer.parseInt(Graphiti.getPeService().getPropertyValue(shape, X_POS_REL_TO_FAULT_NODE));
				int yPos = Integer.parseInt(Graphiti.getPeService().getPropertyValue(shape, Y_POS_REL_TO_FAULT_NODE));
				Graphiti.getGaService().setLocation(shape.getGraphicsAlgorithm(), containerShape.getGraphicsAlgorithm().getX() + xPos, containerShape.getGraphicsAlgorithm().getY() + yPos);
				anythingChanged = true;
			}
		}
		return anythingChanged;
	}
}
