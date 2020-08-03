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

import org.eclipse.emf.common.command.Command;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatCreateFeature;
import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.Activator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;

/**
 * Feature for creating new interface ends in an interface diagram.
 * @author muel_s8
 *
 */

public class FaultTreeNodeCreateFeature extends VirSatCreateFeature {
	
    private FaultTreeNodeType type;
    
    /**
     * Default Constructor.
     * @param fp the feature provider.
     * @param type the type of the fault tree node created by this feature
     */
    
	public FaultTreeNodeCreateFeature(IFeatureProvider fp, FaultTreeNodeType type) {
		super(fp, type.toString(), "Create a new fault tree node of the type " +  type.toString());
		this.type = type;
	}

	
	@Override
	public String getCreateImageId() {
		return type.toString();
	}

	@Override
	protected Command createCreateCommand(VirSatTransactionalEditingDomain ed, ICreateContext context) {
		Concept concept = DiagramHelper.getConcept(ed, Activator.getPluginId());

		if (type == FaultTreeNodeType.FAULT) {
			StructuralElementInstance owningSei = DiagramHelper.getOwningStructuralElementInstance(getDiagram());
			BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance(owningSei);
			return beanSei.add(ed, new Fault(concept));
		} else {
			FaultTreeBuilder ftBuilder = new FaultTreeBuilder(concept);
			Gate gate = ftBuilder.createGate(type);
			
			Fault fault = (Fault) getBusinessObjectForPictogramElement(getDiagram().getChildren().get(0));
			return fault.getFaultTree().getGates().add(ed, gate);
		}
	}
}
