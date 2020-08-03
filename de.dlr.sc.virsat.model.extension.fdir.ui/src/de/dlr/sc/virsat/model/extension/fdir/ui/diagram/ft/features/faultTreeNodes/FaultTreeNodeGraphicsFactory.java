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

import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;

/**
 * This class creates the shapes for the nodes in a fault tree diagram
 * @author muel_s8
 *
 */

public class FaultTreeNodeGraphicsFactory {
	
	public static final IColorConstant FTN_FOREGROUND = IColorConstant.GRAY;
	public static final IColorConstant FTN_BACKGROUND = IColorConstant.LIGHT_ORANGE;
	public static final IColorConstant OBSERVER_BACKGROUND = new ColorConstant(144, 238, 144);
	
	public static final int CORNER_WIDTH = 5;
	public static final int CORNER_HEIGHT = 5;
	public static final int LINE_WIDTH = 2;
	
	public static final int[] FDEP_TRIGGER_LINE = new int[] { 
		0, 0, 
		18, 20, 
		0, 40 
	};
	
	public static final int GATE_WIDTH = 50;
	public static final int GATE_HEIGHT = 40;
	
	public static final int[] SEQ_ARROW_LINE = new int[] { 
		5, 5,
		GATE_WIDTH - 5, 5, 
		GATE_WIDTH - 10, 3,
		GATE_WIDTH - 5, 5,
		GATE_WIDTH - 10, 8
	};
	
	public static final int[] AND_POLYGON = new int[] { 
		GATE_WIDTH * 4 / 6, 0,  
		GATE_WIDTH * 5 / 6, GATE_HEIGHT / 8,
		GATE_WIDTH, GATE_HEIGHT / 2,
		GATE_WIDTH, GATE_HEIGHT,
		0, GATE_HEIGHT,
		0, GATE_HEIGHT / 2,
		GATE_WIDTH * 1 / 6, GATE_HEIGHT / 8,
		GATE_WIDTH * 2 / 6, 0,
		GATE_WIDTH * 4 / 6, 0,
	};
	
	public static final int[] OR_POLYGON = new int[] { 
		GATE_WIDTH * 3 / 6, 0,  
		GATE_WIDTH * 5 / 6, GATE_HEIGHT / 8,
		GATE_WIDTH, GATE_HEIGHT / 2,
		GATE_WIDTH, GATE_HEIGHT,
		GATE_WIDTH * 3 / 4, GATE_HEIGHT * 4 / 5,
		GATE_WIDTH / 2, GATE_HEIGHT * 3 / 4,
		GATE_WIDTH * 1 / 4, GATE_HEIGHT * 4 / 5,
		0, GATE_HEIGHT,
		0, GATE_HEIGHT / 2,
		GATE_WIDTH * 1 / 6, GATE_HEIGHT / 8,
		GATE_WIDTH * 3 / 6, 0
	};
	
	public static final int [] AND_POLYGON_ROUNDING = new int[] {
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5
	};
	
	public static final int [] OR_POLYGON_ROUNDING = new int[] {
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
		5, 5,
	};
	
	private ContainerShape containerShape;
	private Diagram diagram;
	private Color colorFg;
	private Color colorBg;
	private Color observerColorBg;
	
	/**
	 * Sets up the factory
	 * @param containerShape the container shape
	 */
	public FaultTreeNodeGraphicsFactory(ContainerShape containerShape) {
		this.containerShape = containerShape;
		
		this.diagram = Graphiti.getPeService().getDiagramForShape(containerShape);
		this.colorFg = Graphiti.getGaService().manageColor(diagram, FTN_FOREGROUND);
		this.colorBg = Graphiti.getGaService().manageColor(diagram, FTN_BACKGROUND);
		this.observerColorBg = Graphiti.getGaService().manageColor(diagram, OBSERVER_BACKGROUND);
	}
	
	/**
	 * Creates the corresponding graphics algorithm for the given node type
	 * @param type the node type

	 * @return the created graphics algorithm
	 */
	public GraphicsAlgorithm createGaForFaultTreeNodeType(FaultTreeNodeType type) {
		switch (type) {
			case AND:
			case VOTE:
			case PAND_I:
			case PAND:
			case SAND:
				return createAndGa();
			case OR:
			case POR_I:
			case POR:
				return createOrGa();
			case SPARE:
				return createSpareGateGa();
			case FDEP:
			case PDEP:
			case RDEP:
				return createFDEPGa();
			case MONITOR:
				return createObserverGateGa();
			case SEQ:
				return createSEQGateGa();
			default:
				return createDefaultGa();
		}
	}

	/**
	 * Creates the default box shaped graphics algorithm
	 * @return the created graphics algorithm
	 */
	public GraphicsAlgorithm createDefaultGa() {
		RoundedRectangle roundedRectangle = Graphiti.getGaService().createRoundedRectangle(containerShape, CORNER_WIDTH, CORNER_HEIGHT);
		roundedRectangle.setForeground(colorFg);
		roundedRectangle.setBackground(colorBg);
		roundedRectangle.setLineWidth(LINE_WIDTH);
		return roundedRectangle;
	}
	
	/**
	 * Creates the spare gate graphics algorithm
	 * @return the created graphics algorithm
	 */
	public GraphicsAlgorithm createSpareGateGa() {
		GraphicsAlgorithm ga = createDefaultGa();
		
		Shape rectShape = Graphiti.getPeCreateService().createShape(containerShape, false);
		Rectangle spareRect = Graphiti.getGaService().createRectangle(rectShape);
		spareRect.setForeground(colorFg);
		spareRect.setBackground(colorBg);
		spareRect.setLineWidth(LINE_WIDTH);
		
		return ga;
	}
	
	/**
	 * Creates the FDEP graphics algorithm
	 * @return the created graphics algorithm
	 */
	public GraphicsAlgorithm createFDEPGa() {
		GraphicsAlgorithm ga = createDefaultGa();
		
		Shape triggerShape = Graphiti.getPeCreateService().createShape(containerShape, false);
		Polyline triggerLine = Graphiti.getGaCreateService().createPolyline(triggerShape, FDEP_TRIGGER_LINE);
		triggerLine.setLineWidth(LINE_WIDTH);
		triggerLine.setForeground(colorFg);
		triggerLine.setBackground(colorBg);
			
		return ga;
	}
	
	/**
	 * Creates the AND graphics algorithm
	 * @return the created graphics algorithm
	 */
	public GraphicsAlgorithm createAndGa() {
		Polygon andPolygon = Graphiti.getGaCreateService().createPolygon(containerShape, AND_POLYGON, AND_POLYGON_ROUNDING);
		andPolygon.setForeground(colorFg);
		andPolygon.setBackground(colorBg);
		andPolygon.setLineWidth(LINE_WIDTH);
		andPolygon.setWidth(GATE_WIDTH);
		andPolygon.setHeight(GATE_HEIGHT);
		return andPolygon;
	}
	
	/**
	 * Creates the AND graphics algorithm
	 * @return the created graphics algorithm
	 */
	public GraphicsAlgorithm createOrGa() {
		Polygon andPolygon = Graphiti.getGaCreateService().createPolygon(containerShape, OR_POLYGON, OR_POLYGON_ROUNDING);
		andPolygon.setForeground(colorFg);
		andPolygon.setBackground(colorBg);
		andPolygon.setLineWidth(LINE_WIDTH);
		andPolygon.setWidth(GATE_WIDTH);
		andPolygon.setHeight(GATE_HEIGHT);
		return andPolygon;
	}
	
	/**
	 * Creates the OBSERVER graphics algorithm
	 * @return the OBSERVER graphics algorithm
	 */
	public GraphicsAlgorithm createObserverGateGa() {
		GraphicsAlgorithm observerGa = createDefaultGa();
		observerGa.setBackground(observerColorBg);
		return observerGa;
	}
	
	/**
	 * Creates the SEQ graphics algorithm
	 * @return the SEQ graphics algorithm
	 */
	public GraphicsAlgorithm createSEQGateGa() {
		GraphicsAlgorithm seqGa = createDefaultGa();
		Shape seqArrowShape = Graphiti.getPeCreateService().createShape(containerShape, false);
		
		Polyline seqArrow = Graphiti.getGaCreateService().createPolyline(seqArrowShape, SEQ_ARROW_LINE);
		seqArrow.setLineWidth(LINE_WIDTH);
		seqArrow.setForeground(colorFg);
		seqArrow.setBackground(colorBg);
		
		return seqGa;
	}
}
