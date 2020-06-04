/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.snippet;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.AProperty;
import de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.EnumProperty;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.util.BasicEventHolder;
import de.dlr.sc.virsat.uiengine.ui.cellEditor.aproperties.EnumPropertyCellEditingSupport;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * Failure modes that directly cause this Fault
 * 
 */
public class UiSnippetTableFaultBasicEventsBasicEvent extends AUiSnippetTableFaultBasicEventsBasicEvent implements IUiSnippet {
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);
	}
	
	@Override
	protected EditingSupport createEditingSupport(EditingDomain editingDomain, AProperty property) {
		if (property.getName().equals(BasicEvent.PROPERTY_DISTRIBUTION)) {
			EnumPropertyCellEditingSupport editingSupport = new EnumPropertyCellEditingSupport(editingDomain, columnViewer, (EnumProperty) property) {
				@Override
				protected void setValue(Object element, Object userInputValue) {
					super.setValue(element, userInputValue);
					APropertyInstance pi = getPropertyInstance(element);
					
					BasicEvent be = new BasicEvent((CategoryAssignment) pi.eContainer());
					BasicEventHolder beHolder = new BasicEventHolder(be);
					String unit = beHolder.getDefaultUnitForDistribution();
					
					CompoundCommand cc = new CompoundCommand();
					cc.append(be.getHotFailureRateBean().setUnit(editingDomain, unit));
					editingDomain.getCommandStack().execute(cc);
				}
			};
			
			return editingSupport;
		}
		
		return super.createEditingSupport(editingDomain, property);
	}
}
