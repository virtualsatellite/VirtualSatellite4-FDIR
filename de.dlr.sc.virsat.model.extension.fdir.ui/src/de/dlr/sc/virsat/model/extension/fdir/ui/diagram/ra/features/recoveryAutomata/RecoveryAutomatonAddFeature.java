/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.recoveryAutomata;

import org.eclipse.elk.core.service.DiagramLayoutEngine;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.ui.IWorkbenchPart;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatAddShapeFeature;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * This feature handles the addition of recovery automata to a diagram.
 * @author muel_s8
 *
 */

public class RecoveryAutomatonAddFeature extends VirSatAddShapeFeature {

	public static final IColorConstant ELEMENT_TEXT_FOREGROUND = IColorConstant.BLACK;
	public static final IColorConstant ELEMENT_FOREGROUND = IColorConstant.GRAY;
	public static final IColorConstant ELEMENT_BACKGROUND = IColorConstant.LIGHT_ORANGE;
		
	public static final int LINE_WIDTH = 2;  		
	public static final int DEFAULT_WIDTH = 200;
	public static final int DEFAULT_HEIGHT = 200;
	
	/**
	 * Default constructor.
	 * @param fp the feature provider
	 */
	public RecoveryAutomatonAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return super.canAdd(context) && context.getTargetContainer() instanceof Diagram;
	} 
	
	@Override
	public PictogramElement add(IAddContext context) {
		RecoveryAutomaton ra = new RecoveryAutomaton((CategoryAssignment) context.getNewObject());
		
		// add the states of the recovery automaton
		for (State state : ra.getStates()) {
			addGraphicalRepresentation(context, state.getTypeInstance());
		}
		
		// add the transitions
		for (Transition t : ra.getTransitions()) {	
			addGraphicalRepresentation(context, t);
		}
		
		IWorkbenchPart part = ((DiagramBehavior) getDiagramBehavior()).getDiagramContainer().getWorkbenchPart();
        DiagramLayoutEngine.invokeLayout(part, null, null);
		
		return null;
	}

}
