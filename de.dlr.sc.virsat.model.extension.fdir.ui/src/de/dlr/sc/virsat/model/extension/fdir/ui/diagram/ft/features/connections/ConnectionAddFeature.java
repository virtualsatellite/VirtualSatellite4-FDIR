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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatAddConnectionFeature;
import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.concept.types.factory.BeanCategoryAssignmentFactory;
import de.dlr.sc.virsat.model.dvlm.categories.ATypeDefinition;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.AbstractFaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;

/**
 * Feature for adding connections to the fault tree diagram.
 * @author muel_s8
 *
 */

public class ConnectionAddFeature extends VirSatAddConnectionFeature {

	private static final IColorConstant CONNECTION_FOREGROUND = new ColorConstant(98, 131, 167);
	
	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public ConnectionAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	/**
	 * Gets an encapsulating bean object for the given ca in the context
	 * @param context the graphitit context
	 * @return the bean
	 */
	private FaultTreeEdge getBean(IAddContext context) {
		CategoryAssignment ca = (CategoryAssignment) context.getNewObject();
		BeanCategoryAssignmentFactory beanCaFactory = new BeanCategoryAssignmentFactory();
		try {
			return (FaultTreeEdge) beanCaFactory.getInstanceFor(ca);
		} catch (CoreException e) {
			return null;
		}
	}
	
	@Override
	public boolean canAdd(IAddContext context) {
		AbstractFaultTreeEdge fte = context.getNewObject() instanceof AbstractFaultTreeEdge ? (AbstractFaultTreeEdge) context.getNewObject() : getBean(context);
		for (Connection connection : getDiagram().getConnections()) {
			Object bo = getBusinessObjectForPictogramElement(connection);
			if (bo.equals(fte)) {
				return false;
			}
		}	
		return super.canAdd(context);
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		AbstractFaultTreeEdge fte = context.getNewObject() instanceof AbstractFaultTreeEdge ? (AbstractFaultTreeEdge) context.getNewObject() : getBean(context);
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		
		Anchor fromAnchor = null;
		Anchor toAnchor = null;
		
		if (context instanceof IAddConnectionContext) {
			IAddConnectionContext connectionContext = (IAddConnectionContext) context;
			fromAnchor = connectionContext.getSourceAnchor();
			toAnchor = connectionContext.getTargetAnchor();
		} else {
			FaultTreeNode[] ftns = { fte.getFrom(), fte.getTo() };
			
			// Find the type of the fault tree edge
			ArrayInstance ai = (ArrayInstance) fte.getTypeInstance().eContainer().eContainer();
			ATypeDefinition td = ai.getType();
			
			final PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(ftns);
			
			for (PictogramElement pe : pes) {
				if (pe instanceof Anchor) {
					Anchor anchor = (Anchor) pe;
					FaultTreeNode ftn = (FaultTreeNode) getBusinessObjectForPictogramElement(pe);
					AnchorType anchorType = AnchorUtil.getAnchorType(anchor);
					
					if (ftn.equals(ftns[0]) && anchorType.isSourceType()) {
						fromAnchor = (Anchor) pe;
					}

					if (ftn.equals(ftns[1]) 
							&& ((anchorType.equals(AnchorType.INPUT) && !td.getName().equals("spares")) || (anchorType.equals(AnchorType.SPARE) && td.getName().equals("spares")))) {
						toAnchor = (Anchor) pe;
					}
				}
			}
		}
		
		if (fromAnchor == null || toAnchor == null) {
			return null;
		}
		
		// Create a Polyline connection
		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(fromAnchor);
		connection.setEnd(toAnchor);
		
		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolyline(connection);
		
		polyline.setLineWidth(2);
		polyline.setForeground(manageColor(CONNECTION_FOREGROUND));
		link(connection, fte);
		
		// Create arrow
		ConnectionDecorator cd = peCreateService.createConnectionDecorator(connection, false, 1.0, true);
		DiagramHelper.createArrow(cd, manageColor(CONNECTION_FOREGROUND));
		
    	updatePictogramElement(toAnchor.getParent());
		
		return connection;
	}
}
