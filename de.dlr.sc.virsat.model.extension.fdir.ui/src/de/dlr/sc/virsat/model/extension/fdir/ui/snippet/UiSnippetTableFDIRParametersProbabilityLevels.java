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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
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
public class UiSnippetTableFDIRParametersProbabilityLevels extends AUiSnippetTableFDIRParametersProbabilityLevels implements IUiSnippet {
	
	public static final String[] PROBABILITY_LEVEL_NAMES = { 
		FMECAEntry.PROBABILITY_Probable_NAME,
		FMECAEntry.PROBABILITY_Occasional_NAME,
		FMECAEntry.PROBABILITY_Remote_NAME,
		FMECAEntry.PROBABILITY_ExtremelyRemote_NAME
	}; 
	
	public static final int TABLE_HEIGHT = 21 * 4;
	
	@Override
	protected Table createDefaultTable(FormToolkit toolkit, Composite sectionBody) {
		Table table = super.createDefaultTable(toolkit, sectionBody);
		GridData gridDataTable = (GridData) table.getLayoutData();
		gridDataTable.heightHint = TABLE_HEIGHT;
		return table;
	}
	
	@Override
	protected ITableLabelProvider getTableLabelProvider() {
		ITableLabelProvider superLabelProvider = super.getTableLabelProvider();
		
		VirSatTransactionalAdapterFactoryLabelProvider labelProvider = new VirSatTransactionalAdapterFactoryLabelProvider(adapterFactory) {
			@Override
			public String getColumnText(Object object, int columnIndex) {
				if (columnIndex == 0) {
					APropertyInstance pi = (APropertyInstance) object;
					return PROBABILITY_LEVEL_NAMES[getTableObjects().indexOf(pi)];
				} else {
					return superLabelProvider.getColumnText(object, columnIndex);
				}
			}
			
			@Override
			public Image getColumnImage(Object object, int columnIndex) {
				return superLabelProvider.getColumnImage(object, columnIndex);
			}
		};
		
		return labelProvider;
	}
}
