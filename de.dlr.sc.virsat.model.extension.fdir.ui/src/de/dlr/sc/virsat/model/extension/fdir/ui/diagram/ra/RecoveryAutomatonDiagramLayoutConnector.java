/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra;

import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.Direction;
import org.eclipse.elk.core.service.LayoutMapping;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.ui.IWorkbenchPart;

import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.VirSatGraphitiDiagramLayoutConnector;

/**
 * Configuration of Recovery Automaton Diagram Layout
 * @author muel_s8
 *
 */

public class RecoveryAutomatonDiagramLayoutConnector extends VirSatGraphitiDiagramLayoutConnector {
	@Override
	public LayoutMapping buildLayoutGraph(IWorkbenchPart workbenchPart, Object diagramPart) {
		LayoutMapping mapping = super.buildLayoutGraph(workbenchPart, diagramPart);

		ElkNode topNode = mapping.getLayoutGraph();
		topNode.setProperty(CoreOptions.DIRECTION, Direction.DOWN);
		topNode.setProperty(LayeredOptions.MERGE_EDGES, true);
		
		return mapping;
	}

	@Override
	protected ElkNode createNode(LayoutMapping mapping, ElkNode parentNode, Shape shape) {
		ElkNode childNode = super.createNode(mapping, parentNode, shape);
		childNode.setProperty(CoreOptions.DIRECTION, Direction.DOWN);
		return childNode;
	}
}
