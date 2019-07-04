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
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.BeanDeleteFeature;

/**
 * This class handles the deletion of edges
 * @author muel_s8
 *
 */
public class ConnectionDeleteFeature extends BeanDeleteFeature {
	
	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */
	public ConnectionDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void delete(IDeleteContext context) {
		Connection connection = (Connection) context.getPictogramElement();
		PictogramElement pe = connection.getEnd().getParent();
		
		super.delete(context);
		
    	updatePictogramElement(pe);
	}
}
