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

import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.math.ElkPadding;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.Direction;
import org.eclipse.elk.core.options.PortConstraints;
import org.eclipse.elk.core.service.LayoutMapping;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.ui.IWorkbenchPart;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.VirSatGraphitiDiagramLayoutConnector;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.faultTreeNodes.FaultTreeNodeAddFeature;

/**
 * Configuration for Fault Tree Diagram Layouting
 * @author muel_s8
 *
 */

public class FaultTreeLayoutConnector extends VirSatGraphitiDiagramLayoutConnector {
	private static final int TOP_PADDING = 30;
	private static final int LEFT_PADDING = 30;


	@Override
	public LayoutMapping buildLayoutGraph(IWorkbenchPart workbenchPart, Object diagramPart) {
		LayoutMapping mapping = super.buildLayoutGraph(workbenchPart, diagramPart);



		ElkNode topNode = mapping.getLayoutGraph();
		topNode.setProperty(CoreOptions.DIRECTION, Direction.UP);
		topNode.setProperty(LayeredOptions.MERGE_EDGES, true);

		// Set border margin for top left corner so context menu is not clipping
		topNode.setProperty(CoreOptions.PADDING, new ElkPadding(TOP_PADDING, LEFT_PADDING, 0, 0));
		return mapping;
	}

	@Override
	protected ElkNode createNode(LayoutMapping mapping, ElkNode parentNode, Shape shape) {
		ElkNode childNode = super.createNode(mapping, parentNode, shape);

		String typeProperty = Graphiti.getPeService().getPropertyValue(shape, FaultTreeNodeAddFeature.FAULT_TREE_NODE_TYPE_KEY);
		if (typeProperty != null) {
			FaultTreeNodeType type = FaultTreeNodeType.valueOf(typeProperty);
			if (type.isOrderDependent()) {
				childNode.setProperty(CoreOptions.PORT_CONSTRAINTS, PortConstraints.FIXED_ORDER);
				return childNode;
			}

			if (type == FaultTreeNodeType.SPARE || type == FaultTreeNodeType.MONITOR) {
				childNode.setProperty(CoreOptions.PORT_CONSTRAINTS, PortConstraints.FIXED_RATIO);
				return childNode;
			}
		}

		childNode.setProperty(CoreOptions.PORT_CONSTRAINTS, PortConstraints.FIXED_SIDE);
		return childNode;
	}
}
