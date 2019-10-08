/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ContextEntryHelper;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.eclipse.graphiti.tb.IContextButtonPadData;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeCollapseFeature;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

/**
 * Provides the tool behavior provider for fault tree diagrams.
 * @author muel_s8
 *
 */

public class FaultTreeDiagramToolBehaviorProvider extends DefaultToolBehaviorProvider {

	/**
	 * Default constructor
	 * @param diagramTypeProvider the diagram type provider
	 */
	public FaultTreeDiagramToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
		IContextButtonPadData data = super.getContextButtonPad(context);
		PictogramElement pe = context.getPictogramElement();

		setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE | CONTEXT_BUTTON_REMOVE | CONTEXT_BUTTON_UPDATE);

		CustomContext cc = new CustomContext(new PictogramElement[] { pe });
		
		ICustomFeature[] cf = getFeatureProvider().getCustomFeatures(cc);
		for (int i = 0; i < cf.length; i++) {
			ICustomFeature iCustomFeature = cf[i];
			if (iCustomFeature instanceof FaultTreeNodeCollapseFeature) {
				Fault fault = (Fault) getFeatureProvider().getBusinessObjectForPictogramElement(pe);
				
				FaultTreeHelper ftHelper = new FaultTreeHelper(fault.getConcept());
				List<Object> allLocalSubObjects = new ArrayList<>(ftHelper.getAllLocalNodes(fault));
				allLocalSubObjects.remove(fault);
				allLocalSubObjects.addAll(fault.getFaultTree().getPropagations());
				allLocalSubObjects.addAll(fault.getFaultTree().getSpares());
				allLocalSubObjects.addAll(fault.getFaultTree().getDeps());
				allLocalSubObjects.addAll(fault.getFaultTree().getObservations());
				
				if (allLocalSubObjects.isEmpty()) {
					break;
				}
				
				// We check if there exists a local sub node that has no pictograph element in the
				// diagram, if so then we offer the option to expand
				// If every node is present in the diagram, then we offer the option to collapse
				PictogramElement[] pes = getFeatureProvider().getDiagramTypeProvider().getNotificationService().calculateRelatedPictogramElements(allLocalSubObjects.toArray());
				boolean isCollapse = true;
				
				for (Object subObject : allLocalSubObjects) {
					boolean hasPe = false;
					for (PictogramElement subPe : pes) {
						Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(subPe);
						if (Objects.equals(subObject, bo)) {
							hasPe = true;
							break;
						}
					}
					
					if (!hasPe) {
						isCollapse = false;
						break;
					}
				}
				
				cc.putProperty(FaultTreeNodeCollapseFeature.IS_COLLAPSE_PROPERTY, isCollapse);
				
				IContextButtonEntry collapseButton = ContextEntryHelper.
						createCollapseContextButton(isCollapse, iCustomFeature, cc);
				data.setCollapseContextButton(collapseButton);
				break;
			}
		}		

		return data;
	}
	
	@Override
	public Object getToolTip(GraphicsAlgorithm ga) {
		PictogramElement pe = ga.getPictogramElement();
		Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
		
		if (bo instanceof Fault) {
			Fault fault = (Fault) bo;
			String detail = (fault.getDetail() != null && !fault.getDetail().equals("")) ? fault.getDetail() : "detail";
			return fault.getName() + "\n\n" +  detail;
		}
		
		return super.getToolTip(ga);
	}
}
