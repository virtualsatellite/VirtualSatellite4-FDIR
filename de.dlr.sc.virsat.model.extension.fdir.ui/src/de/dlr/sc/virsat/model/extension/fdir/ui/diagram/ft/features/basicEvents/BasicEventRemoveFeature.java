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
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatRemoveFeature;

/**
 * This feature takes care that when deleting the connection between a basic event and a fault tree node
 * the basic event is also removed.
 * @author muel_s8
 *
 */

public class BasicEventRemoveFeature extends VirSatRemoveFeature {
	
	/**
	 * Inherited default constructor
	 * @param fp the feature provider
	 */
	public BasicEventRemoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void remove(IRemoveContext context) {
		PictogramElement pe = context.getPictogramElement();	
		
		if (pe instanceof Shape) {
			Shape shape = (Shape) pe;
			Connection connection = shape.getAnchors().get(0).getOutgoingConnections().get(0);
			
			super.remove(context);
			
			PictogramElement peFault = connection.getEnd() != null ? connection.getEnd().getParent() : null;
			RemoveContext rc = new RemoveContext(connection);
			getFeatureProvider().getRemoveFeature(rc).remove(rc);
			
			if (peFault != null) {
				updatePictogramElement(peFault);
			}
		} else {
			super.remove(context);
		}
	}
}
