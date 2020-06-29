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

import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

public class CommentUtil {
	private static final String BELONGS_TO_FAULT_NODE = "Belongs-to-fault-node";
	private static final String IS_COMMENT = "is-comment";
	private static final String TRUE = "true";

	private CommentUtil() {
	}

	public static boolean shapeBelongsToFaultNode(Shape shape, FaultTreeNode bean) {
		String propertyValue = Graphiti.getPeService().getPropertyValue(shape, BELONGS_TO_FAULT_NODE);
		if (propertyValue == null) {
			return false;
		}
		return propertyValue.equals(bean.getUuid());
	}

	public static boolean isComment(Shape shape) {
		String propertyValue = Graphiti.getPeService().getPropertyValue(shape, IS_COMMENT);
		if (propertyValue == null) {
			return false;
		}
		return propertyValue.equals(TRUE);
	}
}
