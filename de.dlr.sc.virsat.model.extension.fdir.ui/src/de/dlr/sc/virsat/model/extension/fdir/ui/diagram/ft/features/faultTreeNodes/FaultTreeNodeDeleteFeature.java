/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IMultiDeleteInfo;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.MultiDeleteInfo;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.BeanDeleteFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * This feature takes care that all contained elements in a fault are deleted
 * @author muel_s8
 *
 */

public class FaultTreeNodeDeleteFeature extends BeanDeleteFeature {

	/**
	 * Inherited default constructor
	 * @param fp the feature provider
	 */
	
	public FaultTreeNodeDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public void delete(IDeleteContext context) {
		FaultTreeNode ftn = (FaultTreeNode) getBusinessObjectForPictogramElement(context.getPictogramElement());
	
		if (ftn instanceof Fault) {
			Fault fault = (Fault) ftn;
			
			List<FaultTreeNode> toDelete = new ArrayList<>();
			toDelete.addAll(fault.getFaultTree().getGates());
			toDelete.addAll(fault.getBasicEvents());
			
			FaultTreeNode[] faultEvents = toDelete.toArray(new FaultTreeNode[toDelete.size()]);
			PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(faultEvents);
			
			IMultiDeleteInfo multiDeleteInfo = context.getMultiDeleteInfo() == null ?  new MultiDeleteInfo(true, false, 1) : context.getMultiDeleteInfo();
			
			DeleteContext multiDeleteContext = (DeleteContext) context;
			multiDeleteContext.setMultiDeleteInfo(multiDeleteInfo);
			
			for (PictogramElement pe : pes) {
				DeleteContext deleteContext = new DeleteContext(pe);
				deleteContext.setMultiDeleteInfo(multiDeleteInfo);
				IDeleteFeature deleteFeature = getFeatureProvider().getDeleteFeature(deleteContext);
				if (deleteFeature != null && deleteFeature.canDelete(deleteContext)) {
					deleteFeature.delete(deleteContext);
				} else {
					RemoveContext removeContext = new RemoveContext(pe);
					IRemoveFeature removeFeature = getFeatureProvider().getRemoveFeature(removeContext);
					if (removeFeature != null && removeFeature.canRemove(removeContext)) {
						removeFeature.remove(removeContext);
					}
				}
			}
		} 
		
		List<PictogramElement> connectedTo = new ArrayList<>();
		ContainerShape cs = (ContainerShape) context.getPictogramElement();
		for (Anchor anchor : cs.getAnchors()) {
			List<Connection> connections = anchor.getOutgoingConnections();
			for (Connection connection : connections) {
				connectedTo.add(connection.getEnd().getParent());
			}
		}
		
		super.delete(context);
		
		connectedTo.stream().forEach(pe -> updatePictogramElement(pe));
	}

}
