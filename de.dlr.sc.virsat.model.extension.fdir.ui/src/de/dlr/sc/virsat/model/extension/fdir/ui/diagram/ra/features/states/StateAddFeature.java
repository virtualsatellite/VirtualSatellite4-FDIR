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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import com.google.common.base.Objects;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatAddShapeFeature;
import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;

/**
 * This feature handles the addition of recovery automaton states to 
 * graphiti diagrams.
 * @author muel_s8
 *
 */

public class StateAddFeature extends VirSatAddShapeFeature {

	public static final IColorConstant ELEMENT_TEXT_FOREGROUND = IColorConstant.BLACK;
	public static final IColorConstant ELEMENT_FOREGROUND = IColorConstant.GRAY;
	public static final IColorConstant ELEMENT_BACKGROUND = IColorConstant.LIGHT_ORANGE;
	
	public static final int LINE_WIDTH = 2; 
	public static final int DEFAULT_SIZE = 40;
	public static final int IMAGE_WIDTH = 15;	
	
	/**
	 * Default constructor
	 * @param fp the feature provider
	 */
	
	public StateAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		StructuralElementInstance owningSei = DiagramHelper.getOwningStructuralElementInstance(context.getTargetContainer());
		BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance(owningSei);
		RecoveryAutomaton ra = beanSei.getFirst(RecoveryAutomaton.class);
		State addedState = new State((CategoryAssignment) context.getNewObject());
		
		for (Shape shape : getDiagram().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(shape);
			if (Objects.equal(bo, addedState)) {
				return false;
			}
		}	
		
		return super.canAdd(context) && context.getTargetContainer() instanceof Diagram && ra != null && ra.getStates().contains(addedState);
	} 
	
	@Override
	public PictogramElement add(IAddContext context) {
		ContainerShape cs = context.getTargetContainer();
		State addedState = (State) new State((CategoryAssignment) context.getNewObject());
		RecoveryAutomaton ra = addedState.getParentCaBeanOfClass(RecoveryAutomaton.class);
		
		boolean isInitialState = ra.getInitial() != null ? ra.getInitial().equals(addedState) : false;
		
		IGaService gaService = Graphiti.getGaService();
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(cs, true);
		gaService.createInvisibleRectangle(containerShape);
		link(containerShape, addedState);
		
		Font font = gaService.manageDefaultFont(getDiagram(), false, true);	
		
		ContainerShape ellipseShape = peCreateService.createContainerShape(containerShape, false);
		
		// Create and set graphics algorithm		
		Ellipse ellipse = gaService.createEllipse(ellipseShape);		
		ellipse.setLineWidth(LINE_WIDTH);		
		ellipse.setForeground(manageColor(ELEMENT_FOREGROUND));
		ellipse.setBackground(manageColor(ELEMENT_BACKGROUND));	
		
		// Create a text for the name
    	Shape nameShape = peCreateService.createShape(ellipseShape, false);	
    	Text nameText = gaService.createText(nameShape, addedState.getName());		
    	nameText.setForeground(manageColor(IColorConstant.BLACK));	
    	nameText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);	
    	nameText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
    	nameText.setFont(font);			
    	link(nameShape, addedState);
    	
		// Create an indicator for the initial state
    	Shape imageShape =  peCreateService.createShape(containerShape, false);	
    	gaService.createImage(imageShape, RecoveryAutomaton.PROPERTY_INITIAL);	
    	imageShape.setVisible(isInitialState);
    	link(imageShape, addedState);
		
		// Create the anchor for making transitions
        ChopboxAnchor boxAnchor = peCreateService.createChopboxAnchor(containerShape);
        boxAnchor.setReferencedGraphicsAlgorithm(ellipse);
    	link(boxAnchor, addedState);		
    	
    	updatePictogramElement(containerShape);
    	
    	MoveShapeContext moveContext = new MoveShapeContext(containerShape);
    	moveContext.setLocation(context.getX(), context.getY());
    	moveContext.setTargetConnection(context.getTargetConnection());
    	getFeatureProvider().getMoveShapeFeature(moveContext).execute(moveContext);
    	
		return containerShape;
	}

}
