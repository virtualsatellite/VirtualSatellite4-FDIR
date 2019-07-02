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

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;

/**
 * This class handles the editing of the comment content
 * @author muel_s8
 *
 */

public class CommentDirectEditFeature extends AbstractDirectEditingFeature {

	/**
	 * Standard constructor 
	 * @param fp the feature provider
	 */
	public CommentDirectEditFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_MULTILINETEXT;
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		return Graphiti.getPeService().getPropertyValue(pe, CommentAddFeature.IS_COMMENT_KEY) != null 
				&& DiagramHelper.hasDiagramWritePermission(pe) 
				&& super.canDirectEdit(context);
	}
	
	@Override
	public String getInitialValue(IDirectEditingContext context) {
		Text text = (Text) context.getPictogramElement().getGraphicsAlgorithm();
		return text.getValue();
	}
	
	@Override
	public void setValue(String value, IDirectEditingContext context) {
		Text text = (Text) context.getPictogramElement().getGraphicsAlgorithm();
		text.setValue(value);
		
		Font font = Graphiti.getGaService().manageDefaultFont(getDiagram());
		int width = 0;
		int height = 0;
		
		for (String line : value.split("\n")) {
			IDimension textDimension = GraphitiUi.getUiLayoutService().calculateTextSize(line, font);
			width = Math.max(width, textDimension.getWidth());
			height += textDimension.getHeight();
		}
		
		width += CommentAddFeature.PADDING_X * 2;
		height += CommentAddFeature.PADDING_Y * 2;
		
		Graphiti.getGaService().setSize(text, width, height);
		
		ContainerShape cs = (ContainerShape) context.getPictogramElement().eContainer();
		Graphiti.getGaService().setSize(cs.getGraphicsAlgorithm(), width, height);
	}
}
