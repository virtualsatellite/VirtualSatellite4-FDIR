/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatMoveShapeFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections.PropagationCreateFeature;

/**
 * This class handles the drag & drop behavior of Fault Tree nodes for easy reconnection
 * @author muel_s8
 *
 */
public class FaultTreeNodeMoveFeature extends VirSatMoveShapeFeature {

	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */
	public FaultTreeNodeMoveFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public void moveShape(IMoveShapeContext context) {
		ContainerShape containerShape = (ContainerShape) context.getShape();
		
		Connection targetConnection = context.getTargetConnection();
		if (targetConnection != null) {
			Anchor start = targetConnection.getStart();
			Anchor end = targetConnection.getEnd();
			
			Anchor inputAnchor = AnchorUtil.getFreeAnchors(containerShape, AnchorType.INPUT).get(0);
			Anchor outputAnchor = AnchorUtil.getOutputAnchor(containerShape);
			
			ReconnectionContext reconnectionContext = new ReconnectionContext(targetConnection, start, inputAnchor, null);
			reconnectionContext.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
			getFeatureProvider().getReconnectionFeature(reconnectionContext).reconnect(reconnectionContext);
			
			CreateConnectionContext createConnectionContext = new CreateConnectionContext();
			createConnectionContext.setSourceAnchor(outputAnchor);
			createConnectionContext.setTargetAnchor(end);
			new PropagationCreateFeature(getFeatureProvider()).create(createConnectionContext);
		}
		
		super.moveShape(context);
	}
	
}
