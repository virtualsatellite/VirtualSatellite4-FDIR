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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;

/**
 * This class handles the creation of new comments
 * @author muel_s8
 *
 */

public class CommentCreateFeature extends AbstractCreateFeature {

	public static final String DEFAULT_COMMENT_TEXT = "comment";

	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */
	public CommentCreateFeature(IFeatureProvider fp) {
		super(fp, "Comment", "Create a comment");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		ContainerShape cs = context.getTargetContainer();
		return cs instanceof ContainerShape && DiagramHelper.hasDiagramWritePermission(cs);
	}

	@Override
	public Object[] create(ICreateContext context) {
		addGraphicalRepresentation(context, DEFAULT_COMMENT_TEXT);
		return new Object[] { DEFAULT_COMMENT_TEXT };
	}

	@Override
	public String getCreateImageId() {
		return "Comment";
	}
}
