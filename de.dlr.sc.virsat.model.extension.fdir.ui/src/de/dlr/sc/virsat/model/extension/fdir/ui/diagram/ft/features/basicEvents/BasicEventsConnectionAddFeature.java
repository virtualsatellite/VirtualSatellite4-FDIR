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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Feature for adding connections to the fault tree diagram.
 * @author muel_s8
 *
 */

public class BasicEventsConnectionAddFeature extends AbstractAddFeature {

	private static final IColorConstant CONNECTION_FOREGROUND = new ColorConstant(98, 131, 167);
	
	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public BasicEventsConnectionAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		Object object = context.getNewObject();
		if (object instanceof CategoryAssignment) {
			CategoryAssignment ca = (CategoryAssignment) object;
			if (ca.getType().getName().equals(FaultTreeNode.class.getSimpleName())) {
				return true;
			}
		}
		
		if (object instanceof FaultTreeEdge) {
			return true;
		}
		
		return false;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		BasicEvent be = context.getNewObject() instanceof BasicEvent ? (BasicEvent) context.getNewObject() : new BasicEvent((CategoryAssignment) context.getNewObject());
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		
		IAddConnectionContext connectionContext = (IAddConnectionContext) context;
		Anchor fromAnchor = connectionContext.getSourceAnchor();
		Anchor toAnchor = connectionContext.getTargetAnchor();
		
		if (fromAnchor == null || toAnchor == null) {
			return null;
		}
		
		// Create a Polyline connection
		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(fromAnchor);
		connection.setEnd(toAnchor);
		
		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolyline(connection);
		polyline.setLineWidth(2);
		polyline.setForeground(manageColor(CONNECTION_FOREGROUND));
		link(connection, be);
		
		return connection;
	}
}
