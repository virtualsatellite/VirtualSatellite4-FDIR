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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Utility class for handling anchors
 * @author muel_s8
 *
 */

public class AnchorUtil {
	
	public static final double OUTPUT_ANCHOR_X = 0.5;
	public static final double OUTPUT_ANCHOR_Y = 0;

	public static final double INPUT_ANCHOR_X = 1;
	public static final double INPUT_ANCHOR_Y = 1;
	
	public static final double OBSERVER_ANCHOR_X = 1;
	public static final double OBSERVER_ANCHOR_Y = 1;
	
	public static final int ANCHOR_WIDTH = 6;
	public static final int ANCHOR_HEIGHT = 6;
	
	public static final String ANCHOR_TYPE_KEY = "anchor-type";
	
	/**
	 * Anchor type
	 * @author muel_s8
	 *
	 */
	public enum AnchorType {
		INPUT, SPARE, OUTPUT, OBSERVER, NO_TYPE;
			
		/**
		 * Checks if this anchor type can be used as the source of a connection
		 * @return true iff the anchor type can be a connection source
		 */
		public boolean isSourceType() {
			return this == AnchorType.OUTPUT;
		}
			
		/**
		 * Checks if this anchor type can be used as a target of a connection
		 * @return true iff the anchor type can be a connection target
		 */
		public boolean isTargetType() {
			return this == AnchorType.INPUT || this == AnchorType.SPARE || this == AnchorType.OBSERVER;
		}
	}
	
	/**
	 * Hidden constructor
	 */
	private AnchorUtil() {
		
	}
	
	/**
	 * Gets the anchor type of a given anchor
	 * @param anchor the anchor
	 * @return the type of the anchor
	 */
	public static AnchorType getAnchorType(Anchor anchor) {
		String propertyValue = Graphiti.getPeService()
				.getPropertyValue(anchor, ANCHOR_TYPE_KEY);
		if (propertyValue != null) {
			return AnchorType.valueOf(propertyValue);
		} else {
			return AnchorType.NO_TYPE;
		}
	}
	
	/**
	 * Checks if there are unoccupied source anchors
	 * @param anchorContainer the anchor container
	 * @param anchorType the anchor type
	 * @return true iff there are unoccupied source anchors
	 */
	public static List<Anchor> getFreeAnchors(AnchorContainer anchorContainer, AnchorType anchorType) {       
        return anchorContainer.getAnchors().stream()
        		.filter(anchor -> getAnchorType(anchor) == anchorType && anchor.getIncomingConnections().isEmpty())
        		.collect(Collectors.toList());
	}
	
	/**
	 * Gets all anchors of a chosen type
	 * @param anchorContainer the anchor container
	 * @param anchorType the anchor type
	 * @return true iff there are unoccupied source anchors
	 */
	public static List<Anchor> getAnchors(AnchorContainer anchorContainer, AnchorType anchorType) {       
		return anchorContainer.getAnchors().stream()
    		   	.filter(anchor -> getAnchorType(anchor) == anchorType)
    		   	.collect(Collectors.toList());
	}
	
	public static Anchor getOutputAnchor(AnchorContainer anchorContainer) {
		List<Anchor> anchors = getAnchors(anchorContainer, AnchorType.OUTPUT);
		return anchors.isEmpty() ? null : anchors.get(0);
	}
	
	/**
	 * Sorts anchors according to their X position
	 * @param anchors the list of anchors that will be sorted
	 */
	public static void sortAnchorsForXPosition(List<Anchor> anchors) {
		Collections.sort(anchors, new Comparator<Anchor>() {
			@Override
			public int compare(Anchor a1, Anchor a2) {
				return Double.compare(((BoxRelativeAnchor) a1).getRelativeWidth(), ((BoxRelativeAnchor) a2).getRelativeWidth());
			}
		});
	}
	
	/**
	 * Creates an anchor of the given type
	 * @param containerShape the container shape
	 * @param color the anchor color
	 * @param type the anchor type
	 * @return created output anchor
	 */
	public static BoxRelativeAnchor createAnchor(Shape containerShape, Color color, AnchorType type) {
		BoxRelativeAnchor anchor = Graphiti.getPeCreateService().createBoxRelativeAnchor(containerShape);
		anchor.setReferencedGraphicsAlgorithm(containerShape.getGraphicsAlgorithm());
		
		Rectangle anchorRect = Graphiti.getGaService().createRectangle(anchor);
		anchorRect.setBackground(color);
		Graphiti.getPeService().setPropertyValue(anchor, ANCHOR_TYPE_KEY, type.toString());
		
		switch (type) {
			case INPUT:
			case SPARE:
			case OBSERVER:
				anchor.setRelativeWidth(INPUT_ANCHOR_X);
				anchor.setRelativeHeight(INPUT_ANCHOR_Y);
				Graphiti.getGaService().setLocationAndSize(anchorRect, -ANCHOR_WIDTH / 2, -ANCHOR_HEIGHT, ANCHOR_WIDTH, ANCHOR_HEIGHT);
				break;
			case OUTPUT:
				anchor.setRelativeWidth(OUTPUT_ANCHOR_X);
				anchor.setRelativeHeight(OUTPUT_ANCHOR_Y);
				Graphiti.getGaService().setLocationAndSize(anchorRect, -ANCHOR_WIDTH / 2, 0, ANCHOR_WIDTH, ANCHOR_HEIGHT);
				break;
			default:
				break;
		}
		
		return anchor;
	}
	
	/**
	 * Gets anchor for given pictogram element
	 * @param pictogramElement the pictogram element
	 * @return the anchor for given pictogram element. Null if no anchor is present for given pictogram element
	 */
	public static Anchor getAnchorForPictogramElement(PictogramElement pictogramElement) {
		Anchor anchor = null;
		if (pictogramElement instanceof Anchor) {
			anchor = (Anchor) pictogramElement;
		} else if (pictogramElement instanceof AnchorContainer) {
			anchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) pictogramElement);
		}
		return anchor;
	}
}
