/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram;

import org.eclipse.elk.alg.graphiti.GraphitiDiagramLayoutConnector;
import org.eclipse.elk.core.math.KVector;
import org.eclipse.elk.core.math.KVectorChain;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.service.LayoutMapping;
import org.eclipse.elk.core.util.ElkUtil;
import org.eclipse.elk.graph.ElkConnectableShape;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkEdgeSection;
import org.eclipse.elk.graph.ElkGraphElement;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

import com.google.common.collect.BiMap;

/**
 * This class extends the default graphiti to elk graph conversion algorithm.
 * @author muel_s8
 *
 */

public class VirSatGraphitiDiagramLayoutConnector extends GraphitiDiagramLayoutConnector {
	
    @Override
    protected ElkEdge createEdge(final LayoutMapping mapping, final Connection connection) {
        BiMap<Object, ElkGraphElement> inverseGraphMap = mapping.getGraphMap().inverse();

        // set target
        Anchor targetAnchor = connection.getEnd();
        ElkConnectableShape targetShape = (ElkConnectableShape) inverseGraphMap.get(targetAnchor);
        if (targetShape == null) {
        	targetShape = (ElkConnectableShape) inverseGraphMap.get(targetAnchor.getParent());
        }
        
        if (targetShape == null) {
            return null;
        }

        // set source
        Anchor sourceAnchor = connection.getStart();
        ElkConnectableShape sourceShape = (ElkConnectableShape) inverseGraphMap.get(sourceAnchor);
        
        if (sourceShape == null) {
        	sourceShape = (ElkConnectableShape) inverseGraphMap.get(sourceAnchor.getParent());
        }
        
        if (sourceShape == null) {
            return null;
        }
        
        ElkEdge edge = ElkGraphUtil.createSimpleEdge(sourceShape, targetShape);

        // calculate offset for bend points and labels
        ElkNode referenceNode = edge.getContainingNode();
        KVector offset = new KVector();
        ElkUtil.toAbsolute(offset, referenceNode);

        // set source and target point
        ElkEdgeSection edgeSection = ElkGraphUtil.createEdgeSection(edge);
        
        KVector sourcePoint = calculateAnchorEnds(sourceShape, referenceNode);
        edgeSection.setStartLocation(sourcePoint.x, sourcePoint.y);
        
        KVector targetPoint = calculateAnchorEnds(targetShape, referenceNode);
        edgeSection.setEndLocation(targetPoint.x, targetPoint.y);
        
        // set bend points for the new edge
        KVectorChain allPoints = new KVectorChain();
        allPoints.add(sourcePoint);
        if (connection instanceof FreeFormConnection) {
            for (Point point : ((FreeFormConnection) connection).getBendpoints()) {
                KVector v = new KVector(point.getX(), point.getY());
                v.sub(offset);
                allPoints.add(v);
                ElkGraphUtil.createBendPoint(edgeSection, v.x, v.y);
            }
        }
        allPoints.add(targetPoint);
        
        // the modification flag would have been reset here, but that doesn't exist anymore

        mapping.getGraphMap().put(edge, connection);

        // find labels for the connection
        for (ConnectionDecorator decorator : connection.getConnectionDecorators()) {
            if (decorator.getGraphicsAlgorithm() instanceof AbstractText && ((AbstractText) decorator.getGraphicsAlgorithm()).getValue() != null) {
                createEdgeLabel(mapping, edge, decorator, allPoints);
            }
        }
        
        return edge;
    }
	
	@Override
	protected ElkLabel createEdgeLabel(LayoutMapping mapping, ElkEdge parentEdge, ConnectionDecorator decorator,
			KVectorChain allPoints) {
		ElkLabel label = super.createEdgeLabel(mapping, parentEdge, decorator, allPoints);

		GraphicsAlgorithm ga = decorator.getGraphicsAlgorithm();
		if (ga instanceof AbstractText) {
			AbstractText text = (AbstractText) ga;
			String labelText = text.getValue();
			label.setText(labelText);

			IGaService gaService = Graphiti.getGaService();
			Font font = gaService.getFont(text, true);
			label.setProperty(CoreOptions.FONT_NAME, font.getName());
			label.setProperty(CoreOptions.FONT_SIZE, font.getSize());
		}

		return label;
	}
}
