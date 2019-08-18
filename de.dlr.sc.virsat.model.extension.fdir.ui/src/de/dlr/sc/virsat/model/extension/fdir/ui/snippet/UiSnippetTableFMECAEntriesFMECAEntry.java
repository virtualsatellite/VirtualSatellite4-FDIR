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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ComposedPropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.FMECAEntry;
import de.dlr.sc.virsat.project.ui.labelProvider.VirSatTransactionalAdapterFactoryLabelProvider;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetTableFMECAEntriesFMECAEntry extends AUiSnippetTableFMECAEntriesFMECAEntry implements IUiSnippet {
	
	public static final int CRITICALITY_COLUMN = 7;
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		hideNameColumn = true;
		super.createSwt(toolkit, editingDomain, composite, initModel);
	}
	
	@Override
	protected ITableLabelProvider getTableLabelProvider() {
		ITableLabelProvider tableLabelProvider = super.getTableLabelProvider();
		VirSatTransactionalAdapterFactoryLabelProvider localTableLabelProvider = new VirSatTransactionalAdapterFactoryLabelProvider(adapterFactory) {
			
			@Override
			public String getColumnText(Object object, int columnIndex) {
				ComposedPropertyInstance cpi = (ComposedPropertyInstance) object;
				CategoryAssignment ca = cpi.getTypeInstance();
				FMECAEntry fmecaEntry = new FMECAEntry(ca);
				redirectNotification(fmecaEntry, object, true);
				
				if (columnIndex == CRITICALITY_COLUMN) {
					if (fmecaEntry.getCriticality() == 0) {
						return FMECAEntry.SEVERITY_Unknown_NAME;
					} else {
						return tableLabelProvider.getColumnText(object, columnIndex);
					}
				} else {
					return tableLabelProvider.getColumnText(object, columnIndex);
				}
			}
			
			@Override
			public Image getColumnImage(Object object, int columnIndex) {
				return tableLabelProvider.getColumnImage(object, columnIndex);
			}
		};
		
		return localTableLabelProvider;
	}
}
