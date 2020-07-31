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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.internal.util.T;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.tb.ContextEntryHelper;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IShapeSelectionInfo;
import org.eclipse.graphiti.tb.ShapeSelectionInfoImpl;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirsatCategoryAssignmentOpenEditorFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeCollapseFeature;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeCreateFeature;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Provides the tool behavior provider for fault tree diagrams.
 * @author muel_s8
 *
 */

@SuppressWarnings("restriction")
public class FaultTreeDiagramToolBehaviorProvider extends DefaultToolBehaviorProvider {

	private static final String COMPARTMENT_LABELNAME_EVENTS = "General";
	private static final String COMPARTMENT_LABELNAME_GATES = "Gates";
	private static final String COMPARTMENT_LABELNAME_CONNECTIONS = "Connections";

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

				FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
				Set<FaultTreeNode> allLocalSubNodes = ftHolder.getAllLocalSubNodes(fault);
				Set<Object> allLocalSubObjects = new HashSet<>(allLocalSubNodes);
				allLocalSubObjects.remove(fault);
				
				for (FaultTreeNode subNode : allLocalSubNodes) {
					Set<FaultTreeEdge> edges = ftHolder.getIncomingEdges(subNode);
					for (FaultTreeEdge edge : edges) {
						if (allLocalSubObjects.contains(edge.getFrom())) {
							allLocalSubObjects.addAll(edges);
						}
					}
				}

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

	@Override
	public IPaletteCompartmentEntry[] getPalette() {
		final String SIGNATURE = "getPaletteCompartments()";
		boolean info = T.racer().info();
		if (info) {
			T.racer().entering(DefaultToolBehaviorProvider.class, SIGNATURE, new Object[0]);
		}
		List<IPaletteCompartmentEntry> compartments = new ArrayList<IPaletteCompartmentEntry>();

		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(COMPARTMENT_LABELNAME_CONNECTIONS, null);
		compartments.add(compartmentEntry);

		IFeatureProvider featureProvider = getFeatureProvider();
		ICreateConnectionFeature[] createConnectionFeatures = featureProvider.getCreateConnectionFeatures();

		for (ICreateConnectionFeature createConnectionFeature : createConnectionFeatures) {
			ConnectionCreationToolEntry ccTool = new ConnectionCreationToolEntry(
					createConnectionFeature.getCreateName(), createConnectionFeature.getCreateDescription(),
					createConnectionFeature.getCreateImageId(), createConnectionFeature.getCreateLargeImageId());
			ccTool.addCreateConnectionFeature(createConnectionFeature);

			compartmentEntry.addToolEntry(ccTool);
		}

		compartmentEntry = new PaletteCompartmentEntry(COMPARTMENT_LABELNAME_EVENTS, null);
		compartments.add(compartmentEntry);

		ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();

		for (ICreateFeature createFeature : createFeatures) {
			if (isInEventsCompartment(createFeature)) {
				createCompartmentEntry(compartmentEntry, createFeature);
			}
		}

		compartmentEntry = new PaletteCompartmentEntry(COMPARTMENT_LABELNAME_GATES, null);
		compartments.add(compartmentEntry);

		for (ICreateFeature createFeature : createFeatures) {
			if (isInGateCompartment(createFeature)) {
				createCompartmentEntry(compartmentEntry, createFeature);
			}
		}

		IPaletteCompartmentEntry[] res = compartments.toArray(new IPaletteCompartmentEntry[compartments.size()]);
		if (info) {
			T.racer().exiting(DefaultToolBehaviorProvider.class, SIGNATURE, res);
		}
		return res;
	}

	private boolean isInEventsCompartment(ICreateFeature createFeature) {
		return !(createFeature instanceof FaultTreeNodeCreateFeature) || createFeature.getCreateImageId().equals(FaultTreeNodeType.FAULT.toString());
	}

	private boolean isInGateCompartment(ICreateFeature createFeature) {
		return createFeature instanceof FaultTreeNodeCreateFeature && !(createFeature.getCreateImageId().equals(FaultTreeNodeType.FAULT.toString()));
	}

	private void createCompartmentEntry(PaletteCompartmentEntry compartmentEntry, ICreateFeature createFeature) {
		ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
				createFeature.getCreateName(), createFeature.getCreateDescription(),
				createFeature.getCreateImageId(), createFeature.getCreateLargeImageId(), createFeature);

		compartmentEntry.addToolEntry(objectCreationToolEntry);
	}

	@Override
	public IShapeSelectionInfo getSelectionInfoForShape(Shape shape) {
		IShapeSelectionInfo si = new ShapeSelectionInfoImpl();
		si.setColor(IColorConstant.SHAPE_SELECTION_FG);
		si.setLineStyle(LineStyle.DASH);
		si.setPrimarySelectionBackgroundColor(IColorConstant.LIGHT_GRAY);
		si.setSecondarySelectionBackgroundColor(IColorConstant.LIGHT_GRAY);
		return si;
	}

	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		return new VirsatCategoryAssignmentOpenEditorFeature(getFeatureProvider());
	}
}
