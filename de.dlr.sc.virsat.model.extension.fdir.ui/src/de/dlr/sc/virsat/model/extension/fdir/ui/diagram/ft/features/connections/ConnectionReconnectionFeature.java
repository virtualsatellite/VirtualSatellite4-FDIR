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
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.features.impl.DefaultReconnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;

import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;

/**
 * Feature for interface reconnection in an interface diagram.
 * @author muel_s8
 *
 */

public class ConnectionReconnectionFeature extends DefaultReconnectionFeature {

	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public ConnectionReconnectionFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public void postReconnect(IReconnectionContext context) {
		Connection connection = context.getConnection();
		
		Anchor oldAnchor = context.getOldAnchor();
		Anchor newAnchor = context.getNewAnchor();
		
		AnchorType oldType = AnchorUtil.getAnchorType(oldAnchor);
		AnchorType newType = AnchorUtil.getAnchorType(newAnchor);
		
		FaultTreeNode ftnNew = (FaultTreeNode) getBusinessObjectForPictogramElement(newAnchor);
		Fault fault = ftnNew.getFault();

		Object bo = getBusinessObjectForPictogramElement(connection);
		
		if (bo instanceof FaultTreeEdge) {
			FaultTreeEdge fte = (FaultTreeEdge) bo;
			
			if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
				fte.setTo(ftnNew);
			} else {
				fte.setFrom(ftnNew);
			}
			
			if (oldType != newType) {
				if (oldType.equals(AnchorType.SPARE)) {
					fault.getFaultTree().getSpares().remove(fte);
				} else if (oldType.equals(AnchorType.OBSERVER)) {
					fault.getFaultTree().getObservations().remove(fte);
				} else {
					fault.getFaultTree().getPropagations().remove(fte);
				}
				
				if (newType.equals(AnchorType.SPARE)) {
					fault.getFaultTree().getSpares().add(fte);
				} else if (newType.equals(AnchorType.OBSERVER)) {
					fault.getFaultTree().getObservations().add(fte);
				} else {
					fault.getFaultTree().getPropagations().add(fte);
				}
			}
		} 
		
		updatePictogramElement(context.getNewAnchor().getParent());
		updatePictogramElement(oldAnchor.getParent());
	}
	
	@Override
	public boolean canReconnect(IReconnectionContext context) {
		Anchor oldAnchor = context.getOldAnchor();
		Anchor newAnchor = context.getNewAnchor();
		
		AnchorType oldType = AnchorUtil.getAnchorType(oldAnchor);
		AnchorType newType = AnchorUtil.getAnchorType(newAnchor);
		
		FaultTreeNode ftnNew = (FaultTreeNode) getBusinessObjectForPictogramElement(newAnchor);
		FaultTreeNode ftnOld = (FaultTreeNode) getBusinessObjectForPictogramElement(oldAnchor);
		
		Connection connection = context.getConnection();
		FaultTreeNode ftnStart = (FaultTreeNode) getBusinessObjectForPictogramElement(connection.getStart());
		
		if ((ftnOld instanceof BasicEvent && !(ftnStart instanceof ADEP))
				|| (ftnStart instanceof BasicEvent && ftnOld != ftnNew)) {
			return false;
		}
		
		if (oldType.isSourceType() != newType.isSourceType()) {
			return false;
		}
		
		if (oldType.isTargetType() != newType.isTargetType()) {
			return false;
		}
		
		if (!AnchorUtil.getFreeAnchors(newAnchor.getParent(), newType).contains(newAnchor)) {
			return false;
		}
		
		return super.canReconnect(context);
	}
}
