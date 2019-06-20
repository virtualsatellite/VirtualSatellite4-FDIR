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

import java.util.Objects;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;
import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;

/**
 * Update feature for updating fault tree nodes in a fault tree diagram.
 * @author muel_s8
 *
 */

public class StateUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * @param fp the feature provider
	 */
	
	public StateUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return super.canUpdate(context) && context.getPictogramElement() instanceof ContainerShape;
	}
	
	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ContainerShape cs = (ContainerShape) context.getPictogramElement();
		ABeanCategoryAssignment bean = (ABeanCategoryAssignment) getBusinessObjectForPictogramElement(cs);
		
        // retrieve name from business model
        String businessName = bean.getName();
		boolean updateNeeded = false;
        
		Shape textShape = ((ContainerShape) cs.getChildren().get(0)).getChildren().get(0);
        Text text = (Text) textShape.getGraphicsAlgorithm();
        String pictogramName = text.getValue();
        updateNeeded |= !Objects.equals(pictogramName, businessName);
        
        Shape imageShape = cs.getChildren().get(1);
        RecoveryAutomaton ra = bean.getParentCaBeanOfClass(RecoveryAutomaton.class);
        boolean isInitialState = bean.equals(ra.getInitial());
        updateNeeded |= !Objects.equals(imageShape.isVisible(), isInitialState);
        
        // update needed, if changes have been found
        
        if (updateNeeded) {
            return Reason.createTrueReason("Out of date");
        } else {
            return Reason.createFalseReason();
        }
	}

	@Override
	public boolean update(IUpdateContext context) {
		ContainerShape cs = (ContainerShape) context.getPictogramElement();
		
		ContainerShape stateShape = (ContainerShape) cs.getChildren().get(0);
		Shape initialShape = cs.getChildren().get(1);
		
        ABeanCategoryAssignment bean = (ABeanCategoryAssignment) getBusinessObjectForPictogramElement(cs);
        String businessName = bean.getName();
        
        Font font = Graphiti.getGaService().manageDefaultFont(getDiagram(), false, true);	

        Shape textShape = stateShape.getChildren().get(0);
        Text text = (Text) textShape.getGraphicsAlgorithm();
    	text.setValue(businessName);
    	
    	Shape imageShape = cs.getChildren().get(1);
        RecoveryAutomaton ra = bean.getParentCaBeanOfClass(RecoveryAutomaton.class);
        boolean isInitialState = bean.equals(ra.getInitial());
    	imageShape.setVisible(isInitialState);
        int offset = isInitialState ? StateAddFeature.IMAGE_WIDTH :  0;
    	
		IDimension nameDimension = GraphitiUi.getUiLayoutService().calculateTextSize(" " + businessName + " ", font);
		int neededSize = Math.max(nameDimension.getWidth(), nameDimension.getHeight());
		int size = StateAddFeature.DEFAULT_SIZE >= neededSize ? StateAddFeature.DEFAULT_SIZE : neededSize;
		Graphiti.getGaService().setSize(cs.getGraphicsAlgorithm(), size + offset, size);
		Graphiti.getGaService().setLocationAndSize(stateShape.getGraphicsAlgorithm(), offset, 0, size, size);
		Graphiti.getGaService().setLocationAndSize(text, 0, 0, size, size);
		Graphiti.getGaService().setLocationAndSize(initialShape.getGraphicsAlgorithm(), 0, 0, offset, size);
		
		return true;
	}

}
