/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft.features.basicEvents;

import java.util.Objects;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

import de.dlr.sc.virsat.graphiti.ui.diagram.feature.VirSatUpdateFeature;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;

/**
 * Update feature for updating fault tree nodes in a fault tree diagram.
 * 
 * @author muel_s8
 *
 */

public class BasicEventsUpdateFeature extends VirSatUpdateFeature {

	/**
	 * Default public constructor.
	 * 
	 * @param fp the feature provider
	 */

	public BasicEventsUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return object instanceof BasicEvent && super.canUpdate(context);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();

		BasicEvent bean = (BasicEvent) getBusinessObjectForPictogramElement(pictogramElement);

		boolean updateNeeded = false;

		if (pictogramElement.getGraphicsAlgorithm() instanceof Ellipse) {
			ContainerShape cs = (ContainerShape) pictogramElement;
			Shape nameShape = cs.getChildren().get(0);
			updateNeeded |= updateNeededName(nameShape, bean);
			Shape failureRateShape = ((ContainerShape) cs.getChildren().get(1)).getChildren().get(0);
			updateNeeded |= updateNeededFailureRate(failureRateShape, bean);
		} else if (pictogramElement.getGraphicsAlgorithm() instanceof Text) {
			updateNeeded |= updateNeededName(pictogramElement, bean);
		} else if (pictogramElement.getGraphicsAlgorithm() instanceof Rectangle
				&& pictogramElement instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pictogramElement;
			updateNeeded |= updateNeededFailureRate(cs.getChildren().get(0), bean);
		}

		// update needed, if changes have been found

		if (updateNeeded) {
			return Reason.createTrueReason("Out of date");
		} else {
			return Reason.createFalseReason();
		}
	}

	private boolean updateNeededName(PictogramElement pe, BasicEvent be) {
		Text text = (Text) pe.getGraphicsAlgorithm();
		String pictogramName = text.getValue();
		return !Objects.equals(pictogramName, be.getName());
	}

	private boolean updateNeededFailureRate(PictogramElement pe, BasicEvent be) {
		Text text = (Text) pe.getGraphicsAlgorithm();
		String pictogramName = text.getValue();
		return !Objects.equals(pictogramName, be.getHotFailureRateBean().getValueWithUnit());
	}

	@Override
	public boolean update(IUpdateContext context) {
		// retrieve name from business model
		PictogramElement pictogramElement = context.getPictogramElement();
		BasicEvent bean = (BasicEvent) getBusinessObjectForPictogramElement(pictogramElement);

		boolean changeDuringUpdate = false;

		// Set name in pictogram model
		ContainerShape cs = (ContainerShape) pictogramElement;
		if (pictogramElement.getGraphicsAlgorithm() instanceof Ellipse) {
			Shape nameShape = cs.getChildren().get(0);
			changeDuringUpdate |= updateName(nameShape, bean);
			Shape failureRateShape = ((ContainerShape) cs.getChildren().get(1)).getChildren().get(0);
			changeDuringUpdate |= updateFailureRate(failureRateShape, bean);
		} else if (pictogramElement.getGraphicsAlgorithm() instanceof Rectangle) {
			changeDuringUpdate |= updateFailureRate(((ContainerShape) pictogramElement).getChildren().get(0), bean);
		}

		if (changeDuringUpdate) {
			layoutPictogramElement(pictogramElement);
		}

		return changeDuringUpdate;
	}

	private boolean updateName(PictogramElement pe, BasicEvent be) {
		Text text = (Text) pe.getGraphicsAlgorithm();
		text.setValue(be.getName());
		return true;
	}

	private boolean updateFailureRate(PictogramElement pe, BasicEvent be) {
		Text text = (Text) pe.getGraphicsAlgorithm();
		text.setValue(be.getHotFailureRateBean().getValueWithUnit());
		return true;
	}
}
