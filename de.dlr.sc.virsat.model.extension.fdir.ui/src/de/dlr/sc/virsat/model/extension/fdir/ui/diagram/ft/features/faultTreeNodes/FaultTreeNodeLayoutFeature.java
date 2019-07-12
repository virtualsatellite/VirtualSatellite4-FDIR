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

import java.util.List;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatLayoutFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;

/**
 * Feature for layouting fault tree nodes.
 * @author muel_s8
 *
 */

public class FaultTreeNodeLayoutFeature extends VirSatLayoutFeature {

	public static final int DEFAULT_WIDTH = 80;
	public static final int DEFAULT_HEIGHT = 40;
	
	public static final double SPARE_RELATIVE_TYPE_Y = 2d / 3;
	public static final int SPARE_RECT_POS_Y = 8;
	public static final double SPARE_RECT_REL_POS_X = 0.5;
	
	
	/**
	 * Default Constructor
	 * @param fp the feature provider
	 */
	
	public FaultTreeNodeLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		// return true, if pictogram element is linked to an element
		PictogramElement pe = context.getPictogramElement();
		
		Object businessObject = getBusinessObjectForPictogramElement(pe);
		return businessObject instanceof FaultTreeNode && super.canLayout(context);
	}

	@Override
	public boolean layout(ILayoutContext context) {
		boolean anythingChanged = false;
		
		PictogramElement pe = context.getPictogramElement();
		FaultTreeNode bean = (FaultTreeNode) getBusinessObjectForPictogramElement(pe);
		
		if (pe instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pe;
			GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();

			int indexOffset = Integer.valueOf(Graphiti.getPeService().getPropertyValue(containerShape, FaultTreeNodeAddFeature.COUNT_BASE_SUB_SHAPES_KEY));
			
			Shape typeShape = containerShape.getChildren().get(indexOffset + FaultTreeNodeAddFeature.INDEX_TYPE_TEXT_SHAPE);
			Text typeText = (Text) typeShape.getGraphicsAlgorithm();
			IDimension typeDimension = GraphitiUi.getUiLayoutService().calculateTextSize("   " + typeText.getValue() + "   ", typeText.getFont());
			int minWidth = typeDimension.getWidth();
			int minHeight = typeDimension.getHeight();
			
			Text nameText = null;
			int nameHeight = 0;
			if (bean instanceof Fault) {
				Shape nameShape = containerShape.getChildren().get(indexOffset + FaultTreeNodeAddFeature.INDEX_NAME_TEXT_SHAPE);
				nameText = (Text) nameShape.getGraphicsAlgorithm();
				IDimension nameDimension = GraphitiUi.getUiLayoutService().calculateTextSize("   " + nameText.getValue() + "   ", nameText.getFont());
				minWidth = Math.max(minWidth, nameDimension.getWidth());
				nameHeight = nameDimension.getHeight();
				minHeight = 2 * minHeight + nameHeight;
			}
			
			int width = minWidth;
			int height = minHeight;
			
			if (containerGa instanceof Polygon) {
				width = containerGa.getWidth();
				height = containerGa.getHeight();
			} else {
				width = Math.max(DEFAULT_WIDTH, minWidth);
				height = Math.max(DEFAULT_HEIGHT, minHeight);
			}
			
			if (typeText.getWidth() != width) {
				typeText.setWidth(width);
				anythingChanged = true;
			}
			
			if (bean instanceof Fault) {
				if (typeText.getY() != typeDimension.getHeight() / 2) {
					typeText.setY(typeDimension.getHeight() / 2);
					anythingChanged = true;
				}
				
				if (typeText.getHeight() != typeDimension.getHeight()) {
					typeText.setHeight(typeDimension.getHeight());
					anythingChanged = true;
				}
			} else {
				int typeHeight = bean instanceof SPARE ? (int) (height * SPARE_RELATIVE_TYPE_Y) : height;
				if (typeText.getHeight() != typeHeight) {
					typeText.setHeight(typeHeight);
					anythingChanged = true;
				}
			}
			
			if (nameText != null) {
				anythingChanged |= layoutGa(nameText, 0, typeText.getY() + typeDimension.getHeight(), width, nameHeight);
			}
			
			anythingChanged |= layoutGa(containerGa, containerGa.getX(), containerGa.getY(), width, height);
			
			List<Anchor> inputAnchors = AnchorUtil.getAnchors(containerShape, AnchorType.INPUT);
			AnchorUtil.sortAnchorsForXPosition(inputAnchors);
			
			for (int i = 0; i < inputAnchors.size(); ++i) {
				BoxRelativeAnchor anchor = (BoxRelativeAnchor) inputAnchors.get(i);
				double relativeWidth = (double) (i + 1) / (inputAnchors.size() + 1);
				if (bean instanceof SPARE || bean instanceof OBSERVER) {
					relativeWidth /= 2;
				} 
				
				if (bean instanceof ADEP) {
					anchor.setRelativeWidth(0);
					anchor.setRelativeHeight(relativeWidth);
					GraphicsAlgorithm anchorGa = anchor.getGraphicsAlgorithm();
					anchorGa.setX(0);
					anchorGa.setY(-anchorGa.getHeight() / 2);
				} else {
					anchor.setRelativeWidth(relativeWidth);
				}
			}
			
			if (bean instanceof SPARE) {
				anythingChanged |= layoutSPARE(containerShape, width, typeDimension.getHeight(), bean);
			} else if (bean instanceof VOTE) {
				anythingChanged |= layoutVOTE(containerShape, width, height, typeText.getFont(), bean);
			} else if (bean instanceof DELAY) {
				anythingChanged |= layoutDELAY(containerShape, width, height, typeText.getFont(), bean);
			}
			
			if (bean instanceof OBSERVER) {
				List<Anchor> observerAnchors = AnchorUtil.getAnchors(containerShape, AnchorType.OBSERVER);
				for (int i = 0; i < observerAnchors.size(); ++i) {
					BoxRelativeAnchor anchor = (BoxRelativeAnchor) observerAnchors.get(i);
					double relativeWidth = (1 + ((double) (i + 1) / (observerAnchors.size() + 1))) / 2;
					anchor.setRelativeWidth(relativeWidth);
				}
			}
		} 
        
		return anythingChanged;
	}
	
	/**
	 * Layouts a SPARE gate
	 * @param containerShape the container shape
	 * @param width the width
	 * @param height the height
	 * @param bean the space
	 * @return true iff there is any change
	 */
	private boolean layoutSPARE(ContainerShape containerShape, int width, int height, FaultTreeNode bean) {
		Shape spareRectShape = containerShape.getChildren()
				.get(FaultTreeNodeAddFeature.INDEX_SPARE_RECT_SHAPE);
		GraphicsAlgorithm spareGa = spareRectShape.getGraphicsAlgorithm();

		int spareRectX = width / 2;
		int spareRectY = height + SPARE_RECT_POS_Y;
		int spareRectWidth = spareRectX;
		int spareRectHeight = height - spareRectY;
	
		List<Anchor> spareAnchors = AnchorUtil.getAnchors(containerShape, AnchorType.SPARE);
		for (int i = 0; i < spareAnchors.size(); ++i) {
			BoxRelativeAnchor anchor = (BoxRelativeAnchor) spareAnchors.get(i);
			double relativeWidth = (1 + ((double) (i + 1) / (spareAnchors.size() + 1))) / 2;
			anchor.setRelativeWidth(relativeWidth);
		}
		
		return layoutGa(spareGa, spareRectX, spareRectY, spareRectWidth, spareRectHeight);
	}
	
	/**
	 * Layouts a VOTE gate
	 * @param containerShape the container shape
	 * @param width the width
	 * @param height the height
	 * @param font the font
	 * @param bean the space
	 * @return true iff there is any change
	 */
	private boolean layoutVOTE(ContainerShape containerShape, int width, int height, Font font, FaultTreeNode bean) {
		Shape voteTresholdShape = containerShape.getChildren()
				.get(FaultTreeNodeAddFeature.INDEX_VOTE_TRESHOLD_SHAPE);
		Text voteTresholdGa = (Text) voteTresholdShape.getGraphicsAlgorithm();
		IDimension voteTresholdDimension = GraphitiUi.getUiLayoutService()
				.calculateTextSize(voteTresholdGa.getValue(), font);
		
		int voteTresholdX = 0;
		int voteTresholdY = height - voteTresholdDimension.getHeight() - AnchorUtil.ANCHOR_HEIGHT;
		int voteTresholdWidth = width;
		int voteTresholdHeight = voteTresholdDimension.getHeight();
		
		return layoutGa(voteTresholdGa, voteTresholdX, voteTresholdY, voteTresholdWidth, voteTresholdHeight);
	}
	
	/**
	 * Layouts a DELAY gate
	 * @param containerShape the container shape
	 * @param width the width
	 * @param height the height
	 * @param font the font
	 * @param bean the space
	 * @return true iff there is any change
	 */
	private boolean layoutDELAY(ContainerShape containerShape, int width, int height, Font font, FaultTreeNode bean) {
		Shape delayShape = containerShape.getChildren()
				.get(FaultTreeNodeAddFeature.INDEX_DELAY_SHAPE);
		Text delayGa = (Text) delayShape.getGraphicsAlgorithm();
		IDimension delayDimension = GraphitiUi.getUiLayoutService()
				.calculateTextSize(delayGa.getValue(), font);
		
		int delayX = 0;
		int delayY = height - delayDimension.getHeight() - AnchorUtil.ANCHOR_HEIGHT / 2;
		int delayWidth = width;
		int delayHeight = delayDimension.getHeight();
		
		return layoutGa(delayGa, delayX, delayY, delayWidth, delayHeight);
	}
	
	/**
	 * Sets the x, y, width and height of the passed ga.
	 * Also checks if there were any changes
	 * @param ga the graphics algorithm
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @return true iff there was a change
	 */
	private boolean layoutGa(GraphicsAlgorithm ga, int x, int y, int width, int height) {
		boolean anythingChanged = false;
		
		if (ga.getWidth() != width) {
			ga.setWidth(width);
			anythingChanged = true;
		}
		
		if (ga.getHeight() != height) {
			ga.setHeight(height);
			anythingChanged = true;
		}
		
		if (ga.getX() != x) {
			ga.setX(x);
			anythingChanged = true;
		}
		
		if (ga.getY() != y) {
			ga.setY(y);
			anythingChanged = true;
		}
		
		return anythingChanged;
	}
}
