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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import com.google.common.base.Objects;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatAddShapeFeature;
import de.dlr.sc.virsat.model.concept.types.factory.BeanCategoryAssignmentFactory;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;

/**
 * Feature handling the addition of fault tree nodes.
 * @author muel_s8
 *
 */

public class FaultTreeNodeAddFeature extends VirSatAddShapeFeature {

	public static final IColorConstant FTN_TEXT_FOREGROUND = IColorConstant.BLACK;
	public static final IColorConstant PORT_COLOR = IColorConstant.BLACK;
	public static final IColorConstant OBSERVER_PORT_COLOR = IColorConstant.LIGHT_GRAY;
	
	public static final int INDEX_TYPE_TEXT_SHAPE = 0;
	public static final int INDEX_NAME_TEXT_SHAPE = 1;
	public static final int INDEX_SPARE_RECT_SHAPE = 0;
	public static final int INDEX_VOTE_TRESHOLD_SHAPE = 1;
	
	public static final String FAULT_TREE_NODE_TYPE_KEY = "fault-tree-node-type";
	public static final String COUNT_BASE_SUB_SHAPES_KEY = "count-base-sub-shapes";
	
	/**
	 * Default consturctor
	 * @param fp the feature provider
	 */
	
	public FaultTreeNodeAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isDiagramContainer = context.getTargetContainer() instanceof Diagram;
		
		FaultTreeNode addedNode = getBean(context);
		for (Shape shape : getDiagram().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(shape);
			if (Objects.equal(bo, addedNode)) {
				return false;
			}
		}	
		
		return super.canAdd(context) && isDiagramContainer;
	}
	
	/**
	 * Gets an encapsulating bean object for the given ca in the context
	 * @param context the graphitit context
	 * @return the bean
	 */
	private FaultTreeNode getBean(IAddContext context) {
		CategoryAssignment ca = (CategoryAssignment) context.getNewObject();
		BeanCategoryAssignmentFactory beanCaFactory = new BeanCategoryAssignmentFactory();
		try {
			return (FaultTreeNode) beanCaFactory.getInstanceFor(ca);
		} catch (CoreException e) {
			return null;
		}
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		FaultTreeNode addedNode = getBean(context);
		Diagram targetDiagram = (Diagram) context.getTargetContainer();
		
		// Create a container shape with a rounded rectangle
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);
		
		// Create and set graphics algorithm
		FaultTreeNodeGraphicsFactory gaFactory = new FaultTreeNodeGraphicsFactory(containerShape);
		gaFactory.createGaForFaultTreeNodeType(addedNode.getFaultTreeNodeType());
		Graphiti.getPeService().setPropertyValue(containerShape, COUNT_BASE_SUB_SHAPES_KEY, String.valueOf(containerShape.getChildren().size()));
		link(containerShape, addedNode);
		
		// Create Shape with Type Label
		String typeName = "\u00ab" + addedNode.getFaultTreeNodeType().toString() + "\u00bb";
		createLabel(typeName, containerShape);
		
		// Create Shape with Name Label
		if (addedNode instanceof Fault) {
			String name = addedNode.getParent().getName() + "." + addedNode.getName();
			Shape nameShape = createLabel(name, containerShape);
			link(nameShape, addedNode);
		} else if (addedNode instanceof VOTE) {
			VOTE vote = (VOTE) addedNode;
			String votingThreshold = String.valueOf(vote.getVotingThreshold());
			Shape votingTresholdShape = createLabel("\u2265" + votingThreshold, containerShape);
			link(votingTresholdShape, vote);
		} 
		
		Anchor outputAnchor = AnchorUtil.createAnchor(containerShape, manageColor(PORT_COLOR), AnchorType.OUTPUT);
		link(outputAnchor, addedNode);
				
		Graphiti.getPeService().setPropertyValue(containerShape, FAULT_TREE_NODE_TYPE_KEY, addedNode.getFaultTreeNodeType().toString());
		updatePictogramElement(containerShape);
		
		MoveShapeContext moveContext = new MoveShapeContext(containerShape);
		moveContext.setLocation(context.getX(), context.getY());
		moveContext.setTargetContainer(context.getTargetContainer());
		moveContext.setTargetConnection(context.getTargetConnection());
		getFeatureProvider().getMoveShapeFeature(moveContext).execute(moveContext);
		
		return containerShape;
	}
	
	/**
	 * Creates a label
	 * @param label the label
	 * @param containerShape the container shape
	 * @return the label
	 */
	public Shape createLabel(String label, ContainerShape containerShape) {
		Shape shape = Graphiti.getPeCreateService().createShape(containerShape, false);
		Font font = Graphiti.getGaService().manageDefaultFont(getDiagram(), false, true);
		Text text = Graphiti.getGaService().createText(shape, label);
		text.setForeground(manageColor(FTN_TEXT_FOREGROUND));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setFont(font);
		return shape;
	}
}
