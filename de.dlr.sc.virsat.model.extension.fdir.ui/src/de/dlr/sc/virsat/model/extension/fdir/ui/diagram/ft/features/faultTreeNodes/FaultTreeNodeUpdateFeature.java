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
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.util.IColorConstant;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil;
import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.AnchorUtil.AnchorType;

/**
 * Update feature for updating fault tree nodes in a fault tree diagram.
 * @author muel_s8
 *
 */

public class FaultTreeNodeUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * @param fp the feature provider
	 */
	
	public FaultTreeNodeUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public IReason updateNeeded(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		FaultTreeNode bean = (FaultTreeNode) getBusinessObjectForPictogramElement(pictogramElement);
		List<String> neededUpdates = new ArrayList<>();

		if (pictogramElement instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pictogramElement;
			
			if (bean instanceof Fault) {
		        // check name
		        Shape nameShape = cs.getChildren().get(FaultTreeNodeAddFeature.INDEX_NAME_TEXT_SHAPE);
		        Text text = (Text) nameShape.getGraphicsAlgorithm();
		        String businessName = bean.getParent().getName() + "." + bean.getName();
		        String pictogramName = text.getValue();
		        if (!Objects.equals(pictogramName, businessName)) {
		        	neededUpdates.add("Name out of date");
		        }
			} else if (bean instanceof VOTE) {
				VOTE vote = (VOTE) bean;
				String votingThreshold = String.valueOf("\u2265" + vote.getVotingThreshold());
		        Shape votingTresholdShape = cs.getChildren().get(FaultTreeNodeAddFeature.INDEX_VOTE_TRESHOLD_SHAPE);
		        Text text = (Text) votingTresholdShape.getGraphicsAlgorithm();
		        if (!Objects.equals(votingThreshold, text.getValue())) {
		        	neededUpdates.add("Voting Treshold out of date");
		        }
			} else if (bean instanceof DELAY) {
				DELAY delayNode = (DELAY) bean;
				String delay = String.valueOf(delayNode.getTimeBean().getValueWithUnit());
		        Shape delayShape = cs.getChildren().get(FaultTreeNodeAddFeature.INDEX_DELAY_SHAPE);
		        Text text = (Text) delayShape.getGraphicsAlgorithm();
		        if (!Objects.equals(delay, text.getValue())) {
		        	neededUpdates.add("Delay out of date");
		        }
			}
	        
	        if (AnchorUtil.getFreeAnchors(cs, AnchorType.INPUT).size() != 1) {
	        	neededUpdates.add("Number of inputs out of date");
	        }
	        
	        if (bean instanceof SPARE) {
		        if (AnchorUtil.getFreeAnchors(cs, AnchorType.SPARE).size() != 1) {
		        	neededUpdates.add("Number of spares out of date");
		        }
	        } else if (bean instanceof OBSERVER) {
		        if (AnchorUtil.getFreeAnchors(cs, AnchorType.OBSERVER).size() != 1) {
		        	neededUpdates.add("Number of observed nodes out of date");
		        }
	        }
		}

        if (neededUpdates.isEmpty()) {
        	return Reason.createFalseReason();
        } else {
            return Reason.createTrueReason(neededUpdates.stream().collect(Collectors.joining("\n")));
        }
	}

	@Override
	public boolean update(IUpdateContext context) {
        PictogramElement pictogramElement = context.getPictogramElement();
        FaultTreeNode bean = (FaultTreeNode) getBusinessObjectForPictogramElement(pictogramElement);
        boolean changeDuringUpdate = false;
        
        if (pictogramElement instanceof ContainerShape) {
            ContainerShape cs = (ContainerShape) pictogramElement;
            
            // update name
            if (bean instanceof Fault) {
	            Shape nameShape = cs.getChildren().get(FaultTreeNodeAddFeature.INDEX_NAME_TEXT_SHAPE);
	            Text text = (Text) nameShape.getGraphicsAlgorithm();
	            String businessName = bean.getParent().getName() + "." + bean.getName();
	            text.setValue(businessName);
	            changeDuringUpdate = true;
            } else if (bean instanceof VOTE) {
				VOTE vote = (VOTE) bean;
				String votingThreshold = String.valueOf("\u2265" + vote.getVotingThreshold());
		        Shape votingTresholdShape = cs.getChildren().get(FaultTreeNodeAddFeature.INDEX_VOTE_TRESHOLD_SHAPE);
		        Text text = (Text) votingTresholdShape.getGraphicsAlgorithm();
		        text.setValue(votingThreshold);
            } else if (bean instanceof DELAY) {
            	DELAY delayNode = (DELAY) bean;
				String delay = String.valueOf(delayNode.getTimeBean().getValueWithUnit());
		        Shape delayShape = cs.getChildren().get(FaultTreeNodeAddFeature.INDEX_DELAY_SHAPE);
		        Text text = (Text) delayShape.getGraphicsAlgorithm();
		        text.setValue(delay);
            }
            
            changeDuringUpdate |= updateAnchors(cs, bean, FaultTreeNodeAddFeature.PORT_COLOR, AnchorType.INPUT);
            if (bean instanceof SPARE) {
            	changeDuringUpdate |= updateAnchors(cs, bean, FaultTreeNodeAddFeature.PORT_COLOR, AnchorType.SPARE);
            } else if (bean instanceof OBSERVER) {
            	changeDuringUpdate |= updateAnchors(cs, bean, FaultTreeNodeAddFeature.OBSERVER_PORT_COLOR, AnchorType.OBSERVER);
            }
        } 
        
        if (changeDuringUpdate) {
        	layoutPictogramElement(pictogramElement);
        }
        
		return changeDuringUpdate;
	}
	
	/**
	 * Update the number of anchors
	 * @param cs the container shape
	 * @param bean the bean
	 * @param color the color
	 * @param type the anchor type
	 * @return true iff there was a change
	 */
	private boolean updateAnchors(ContainerShape cs, FaultTreeNode bean, IColorConstant color, AnchorType type) {
		boolean changeDuringUpdate = false;
		
        List<Anchor> freeSpareAnchors = AnchorUtil.getFreeAnchors(cs, type);
        if (freeSpareAnchors.isEmpty()) {
        	// if there are no free anchors, create a new one
    		Anchor spareAnchor = AnchorUtil.createAnchor(cs, manageColor(color), type);
    		link(spareAnchor, bean);
    		changeDuringUpdate = true;
        } else {
        	// if there are more than one free anchor, reduce the size
        	// until only one input anchor remains
        	for (int i = 1; i < freeSpareAnchors.size(); ++i) {
            	Graphiti.getPeService().deletePictogramElement(freeSpareAnchors.get(i));
            	changeDuringUpdate = true;
            }
        }
        
        return changeDuringUpdate;
	}

}
