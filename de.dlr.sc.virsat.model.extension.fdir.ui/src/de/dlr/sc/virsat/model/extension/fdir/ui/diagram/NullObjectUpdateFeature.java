/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;

/**
 * Update feature for removing pictogram elements which no longer have an object associated with them.
 * @author muel_s8
 *
 */

public class NullObjectUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * @param fp the feature provider
	 */
	
	public NullObjectUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return object == null && super.canUpdate(context);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		return Reason.createTrueReason("Object no longer exists");
	}

	@Override
	public boolean update(IUpdateContext context) {
        PictogramElement pictogramElement = context.getPictogramElement();

    	RemoveContext removeContext = new RemoveContext(pictogramElement);
		IRemoveFeature feature = getFeatureProvider().getRemoveFeature(removeContext);
		feature.remove(removeContext);
		return true;
	}

}
