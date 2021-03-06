/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.connections;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.AbstractFaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;

/**
 * This feature creates a fault propagation
 * @author muel_s8
 *
 */

public class PropagationCreateFeature extends AbstractCreateConnectionFeature {

	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */
	public PropagationCreateFeature(IFeatureProvider fp) {
		super(fp, "Propagation", "Create Fault Propagation");
	}

	/**
	 * Adds the edge correctly into the fault
	 * @param fault the fault
	 * @param from the starting node
	 * @param to the ending node
	 * @param concept the concept
	 * @param context the context
	 * @return the created fault tree edge
	 */
	protected FaultTreeEdge addFaultTreeEdge(Fault fault, FaultTreeNode from, FaultTreeNode to, Concept concept, ICreateConnectionContext context) {
		FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);

		AnchorType type = AnchorUtil.getAnchorType(context.getTargetAnchor());

		if (type.equals(AnchorType.SPARE)) {
			return ftBuilder.connectSpare(fault, from, to);
		} else if (type.equals(AnchorType.OBSERVER)) {
			return ftBuilder.connectObserver(fault, from, to);
		} else {
			return ftBuilder.connect(fault, from, to);
		}
	}

	/**
	 * @param context current CreateConnectionContext
	 * @return Anchor for FaulTreeNode in given context
	 */
	private Anchor getSourceAnchorForFaultTreeNode(ICreateConnectionContext context) {
		Anchor sourceAnchor = null;
		PictogramElement sourcePictogramElement = context.getSourcePictogramElement();
		Object sourcePicObj = getBusinessObjectForPictogramElement(sourcePictogramElement);
		if (sourcePicObj instanceof FaultTreeNode) {
			sourceAnchor = AnchorUtil.getOutputAnchor((AnchorContainer) sourcePictogramElement);
		}
		return sourceAnchor;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		Anchor sourceAnchor = context.getSourceAnchor();
		if (sourceAnchor == null) {
			sourceAnchor = getSourceAnchorForFaultTreeNode(context);
		}
		AnchorType sourceAnchorType = AnchorUtil.getAnchorType(sourceAnchor);

		if (!sourceAnchorType.isSourceType()) {
			return false;
		}

		Object source = getBusinessObjectForPictogramElement(sourceAnchor);

		// Basic Events propagate according to their containment
		if (source instanceof BasicEvent) {
			return false;
		}

		if (source instanceof FaultTreeNode) {
			return DiagramHelper.hasDiagramWritePermission(sourceAnchor);
		}

		return false;
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		Anchor sourceAnchor = context.getSourceAnchor();
		Anchor targetAnchor = context.getTargetAnchor();

		if (sourceAnchor == null) {
			sourceAnchor = getSourceAnchorForFaultTreeNode(context);
		}

		// Check if the desired anchors are properly linked
		Object source = getBusinessObjectForPictogramElement(sourceAnchor);
		Object target = getBusinessObjectForPictogramElement(targetAnchor);

		if (source == null || target == null) {
			return false;
		}

		AnchorType sourceAnchorType = AnchorUtil.getAnchorType(sourceAnchor);
		AnchorType targetAnchorType = AnchorUtil.getAnchorType(targetAnchor);

		// Only allow connections from a source to a target, except for BEs, there we can also input DEP connections
		if (target instanceof BasicEvent) {
			if (!sourceAnchorType.isSourceType() && !(source instanceof ADEP)) {
				return false;
			}
		} else if (!sourceAnchorType.isSourceType() || !targetAnchorType.isTargetType()) {
			return false;
		} else if (source instanceof ADEP) {
			return false;
		}

		// Target anchors may only be used once
		if (!targetAnchor.getIncomingConnections().isEmpty()) {
			return false;
		}

		// Must have the right typing and write permission
		if (source instanceof FaultTreeNode && target instanceof FaultTreeNode) {
			return DiagramHelper.hasDiagramWritePermission(sourceAnchor);
		}

		return false;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		Anchor sourceAnchor = context.getSourceAnchor();
		Anchor targetAnchor = context.getTargetAnchor();

		if (sourceAnchor == null) {
			sourceAnchor = getSourceAnchorForFaultTreeNode(context);
		}

		FaultTreeNode source = (FaultTreeNode) getBusinessObjectForPictogramElement(sourceAnchor);
		FaultTreeNode target = (FaultTreeNode) getBusinessObjectForPictogramElement(targetAnchor);

		Concept concept = source.getConcept();

		Fault fault = (Fault) getBusinessObjectForPictogramElement(getDiagram().getChildren().get(0));
		AbstractFaultTreeEdge edge = addFaultTreeEdge(fault, source, target, concept, context);

		AddConnectionContext addContext = new AddConnectionContext(sourceAnchor, context.getTargetAnchor());
		addContext.setNewObject(edge);
		Connection newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		return newConnection;
	}

	@Override
	public String getCreateImageId() {
		return FaultTreeEdge.FULL_QUALIFIED_CATEGORY_NAME;
	}
}
