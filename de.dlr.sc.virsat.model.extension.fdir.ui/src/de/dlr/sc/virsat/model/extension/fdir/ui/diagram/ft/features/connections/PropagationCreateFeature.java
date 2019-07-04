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
import org.eclipse.graphiti.features.context.ICreateConnectionContext;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

/**
 * This feature creates a fault propagation
 * @author muel_s8
 *
 */

public class PropagationCreateFeature extends AbstractConnectionCreateFeature {

	/**
	 * Standard constructor
	 * @param fp the feature provider
	 */
	public PropagationCreateFeature(IFeatureProvider fp) {
		super(fp, "Propagation", "Create Fault Propagation");
	}

	@Override
	protected FaultTreeEdge addFaultTreeEdge(Fault fault, FaultTreeNode from, FaultTreeNode to, Concept concept, ICreateConnectionContext context) {
		FaultTreeHelper ftHelper = new FaultTreeHelper(concept);
		
		AnchorType type = AnchorUtil.getAnchorType(context.getTargetAnchor());
		
		if (type.equals(AnchorType.SPARE)) {
			return ftHelper.connectSpare(fault, from, to);
		} else if (type.equals(AnchorType.OBSERVER)) {
			return ftHelper.connectObserver(fault, from, to);
		} else {
			return ftHelper.connect(fault, from, to);
		}
	}
}
