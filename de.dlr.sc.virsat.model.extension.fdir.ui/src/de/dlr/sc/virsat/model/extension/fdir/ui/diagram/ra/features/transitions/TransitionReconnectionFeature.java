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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.features.impl.DefaultReconnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;

import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Feature for interface reconnection in an interface diagram.
 * @author muel_s8
 *
 */

public class TransitionReconnectionFeature extends DefaultReconnectionFeature {

	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public TransitionReconnectionFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public void preReconnect(IReconnectionContext context) {
		Connection connection = context.getConnection();
		Transition transition = (Transition) getBusinessObjectForPictogramElement(connection);
		
		State state = (State) getBusinessObjectForPictogramElement(context.getNewAnchor());

		if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
			transition.setTo(state);
		} else {
			transition.setFrom(state);
		}
	}

}
