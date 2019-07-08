/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatAddConnectionFeature;
import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Feature for adding transitions to a recovery automaton graphiti diagram.
 * @author muel_s8
 *
 */

public class TransitionAddFeature extends VirSatAddConnectionFeature {

	private static final IColorConstant CONNECTION_FOREGROUND = new ColorConstant(98, 131, 167);
	private static final double DECORATOR_LOCATION = 0.5;
	
	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public TransitionAddFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canAdd(IAddContext context) {
		Transition addedTransition = context.getNewObject() instanceof Transition ? (Transition) context.getNewObject() : new FaultEventTransition((CategoryAssignment) context.getNewObject());
		
		for (Connection connection : getDiagram().getConnections()) {
			Object bo = getBusinessObjectForPictogramElement(connection);
			if (bo.equals(addedTransition)) {
				return false;
			}
		}	
		
		return super.canAdd(context);
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		Transition addedTransition = context.getNewObject() instanceof Transition ? (Transition) context.getNewObject() : new FaultEventTransition((CategoryAssignment) context.getNewObject());
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		
		Anchor fromAnchor = null;
		Anchor toAnchor = null;
		
		if (context instanceof IAddConnectionContext) {
			IAddConnectionContext connectionContext = (IAddConnectionContext) context;
			fromAnchor = connectionContext.getSourceAnchor();
			toAnchor = connectionContext.getTargetAnchor();
		} else {
			State[] states = { addedTransition.getFrom(), addedTransition.getTo() };

			final PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(states);
			
			for (PictogramElement pe : pes) {
				if (pe instanceof Anchor) {
					State state = (State) getBusinessObjectForPictogramElement(pe);
					if (state.equals(states[0])) {
						fromAnchor = (Anchor) pe;
					}

					if (state.equals(states[1])) {
						toAnchor = (Anchor) pe;
					}
				}
			}
		}
		
		Connection connection = null;
		if (fromAnchor != null && toAnchor != null) {
			// Create a Polyline connection
			
			connection = peCreateService.createFreeFormConnection(getDiagram());
			connection.setStart(fromAnchor);
			connection.setEnd(toAnchor);
			
			// Create the line
			IGaService gaService = Graphiti.getGaService();
			Polyline polyline = gaService.createPolyline(connection);
			polyline.setLineWidth(2);
			polyline.setForeground(manageColor(CONNECTION_FOREGROUND));
			link(connection, addedTransition);
			
			// Create the label
			ConnectionDecorator textDecorator = peCreateService.createConnectionDecorator(connection, true, DECORATOR_LOCATION, true);
			Text text = gaService.createDefaultText(getDiagram(), textDecorator);
			text.setFont(manageFont(getDiagram(), text.getFont().getName(), text.getFont().getSize() - 1));
			text.setForeground(manageColor(IColorConstant.BLACK));
			String label = addedTransition.toString();
			IDimension nameDimension = GraphitiUi.getUiLayoutService().calculateTextSize(label, text.getFont());
			gaService.setLocationAndSize(text, 0, 0, nameDimension.getWidth(), nameDimension.getHeight());
			text.setValue(label);
			link(text.getPictogramElement(), addedTransition);	
			
			// Create arrow
			ConnectionDecorator cd = peCreateService.createConnectionDecorator(connection, false, 1.0, true);
			DiagramHelper.createArrow(cd, manageColor(CONNECTION_FOREGROUND));
		}
		
		return connection;
	}
}
