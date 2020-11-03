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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.concept.types.IBeanObject;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyInt;
import de.dlr.sc.virsat.model.concept.types.property.IBeanProperty;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;

/**
 * Generic class for enabling editing of value properties.
 * @author muel_s8
 *
 */

public class BeanPropertyDirectEditValueFeature extends AbstractDirectEditingFeature {
	/**
	 * Public constructor
	 * @param fp the feature provider
	 */
	public BeanPropertyDirectEditValueFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}
	
	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
		
		if (bo instanceof IBeanProperty && ga instanceof Text) {
			return DiagramHelper.hasBothWritePermission(bo, pe);
		}
		
		return false;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		IBeanProperty<?, ?> bean = (IBeanProperty<?, ?>) getBusinessObjectForPictogramElement(pe);
		return String.valueOf(bean.getValue());
	}
	
	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		@SuppressWarnings("unchecked")
		IBeanObject<? extends ValuePropertyInstance> bean = 
				(IBeanObject<? extends ValuePropertyInstance>) getBusinessObjectForPictogramElement(pe);
		
		bean.getTypeInstance().setValue(value);
		
		PictogramElement updateablePe = DiagramHelper.getUpdateableElement(getFeatureProvider(), pe);
		updatePictogramElement(cs);
	}
	
	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		IBeanProperty<?, ?> bean = (IBeanProperty<?, ?>) getBusinessObjectForPictogramElement(pe);
		if (bean instanceof BeanPropertyFloat) {
			try {
				Double.valueOf(value);
			} catch (NumberFormatException e) {
				return "Not a valid floating point number!";
			}
		} else if (bean instanceof BeanPropertyInt) {
			try {
				Integer.valueOf(value);
			} catch (NumberFormatException e) {
				return "Not a valid integer number!";
			}
		}
		
		return super.checkValueValid(value, context);
	}
}
