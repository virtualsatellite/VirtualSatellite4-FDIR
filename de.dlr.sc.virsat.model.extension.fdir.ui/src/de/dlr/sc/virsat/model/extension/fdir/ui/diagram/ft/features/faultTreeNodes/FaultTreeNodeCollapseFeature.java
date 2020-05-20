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

import org.eclipse.elk.core.service.DiagramLayoutEngine;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.ui.IWorkbenchPart;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.AbstractFaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

/**
 * This feature takes care of expanding and collapsing fault trees.
 * @author muel_s8
 *
 */

public class FaultTreeNodeCollapseFeature extends AbstractCustomFeature {

	public static final String IS_COLLAPSE_PROPERTY = "IS_COLLAPSE_PROPERTY";
	
	/**
	 * Default constructor
	 * @param fp the feature provider
	 */
	
	public FaultTreeNodeCollapseFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		return DiagramHelper.hasDiagramWritePermission(getDiagram());
	}
	
	@Override
	public void execute(ICustomContext context) {
		boolean isCollapse = (boolean) context.getProperty(IS_COLLAPSE_PROPERTY);
		
		for (PictogramElement pe : context.getPictogramElements()) {
			Fault fault = (Fault) getBusinessObjectForPictogramElement(pe);
			FaultTreeHelper ftHelper = new FaultTreeHelper();
			
			if (isCollapse) {
				List<FaultTreeNode> allSubNodes = ftHelper.getAllNodes(fault);
				allSubNodes.remove(fault);
				
				PictogramElement[] allSubPes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(allSubNodes.toArray());
				
				for (PictogramElement subPe : allSubPes) {
					RemoveContext removeContext = new RemoveContext(subPe);
					IRemoveFeature feature = getFeatureProvider().getRemoveFeature(removeContext);
					if (feature != null) {
						feature.remove(removeContext);
					}
				}
				
		    	updatePictogramElement(pe);
			} else {
				List<FaultTreeNode> allLocalSubNodes = ftHelper.getAllLocalNodes(fault);
				allLocalSubNodes.remove(fault);
				
				for (FaultTreeNode localSubNode : allLocalSubNodes) {
					AddContext addContext = new AddContext();
					addContext.setTargetContainer(getDiagram());
					addGraphicalRepresentation(addContext, localSubNode.getTypeInstance());
				}
				
				List<AbstractFaultTreeEdge> edges = new ArrayList<>();
				edges.addAll(fault.getFaultTree().getPropagations());
				edges.addAll(fault.getFaultTree().getSpares());
				edges.addAll(fault.getFaultTree().getDeps());
				
				for (AbstractFaultTreeEdge edge : edges) {
					AddContext addContext = new AddContext();
					addContext.setTargetContainer(getDiagram());
					addGraphicalRepresentation(addContext, edge);
				}
			}
		}
		
		IWorkbenchPart part = ((DiagramBehavior) getDiagramBehavior()).getDiagramContainer().getWorkbenchPart();
        DiagramLayoutEngine.invokeLayout(part, null, null);
	}

	@Override
	public String getName() {
		return "Collapse / Expand";
	}
	
	@Override
	public String getDescription() {
		return "Collapses / Expands the Fault Tree";
	}
}
