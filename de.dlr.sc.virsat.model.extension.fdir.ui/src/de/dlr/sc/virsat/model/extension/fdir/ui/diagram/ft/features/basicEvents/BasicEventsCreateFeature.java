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
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;

/**
 * Feature for creating new interface ends in an interface diagram.
 * @author muel_s8
 *
 */

public class BasicEventsCreateFeature extends AbstractCreateFeature {
	
    private static final int PADDING_FAULT_NODE_Y = 15;
    
    /**
     * Default Constructor.
     * @param fp the feature provider.
     */
    
	public BasicEventsCreateFeature(IFeatureProvider fp) {
		super(fp, "Basic Event", "Create a new basic event");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		Object bo = getBusinessObjectForPictogramElement(targetContainer);
		return bo instanceof Fault;
	}

	@Override
	public Object[] create(ICreateContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getTargetContainer());
		Fault fault = ((FaultTreeNode) object).getFault();
		
		Concept concept = fault.getConcept();
		BasicEvent be = new BasicEvent(concept);
		fault.getBasicEvents().add(be);
		
		// Redelegate the add to a new context. We dont add the failure modes directly to the fault but rather add
		// them to the diagram themselves and then connect the failure mode with the fault via a connection
		AddContext addContext = new AddContext();
		GraphicsAlgorithm ga = context.getTargetContainer().getGraphicsAlgorithm();
		addContext.setLocation(ga.getX() + context.getX(), ga.getY() + context.getY() + ga.getHeight() + PADDING_FAULT_NODE_Y);
		addContext.setTargetContainer(getDiagram());
		
		addGraphicalRepresentation(addContext, be.getTypeInstance());
		
		return new Object[] { be };
	}
	
	@Override
	public String getCreateImageId() {
		return BasicEvent.FULL_QUALIFIED_CATEGORY_NAME;
	}
}
