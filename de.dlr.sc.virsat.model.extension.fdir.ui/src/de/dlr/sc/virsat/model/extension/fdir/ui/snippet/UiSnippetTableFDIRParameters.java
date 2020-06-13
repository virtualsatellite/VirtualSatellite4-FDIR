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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetTableFDIRParameters extends AUiSnippetTableFDIRParameters {
	public static final int SINGLE_LINE_TABLE_HEIGHT = 35;
	
	@Override
	protected Table createDefaultTable(FormToolkit toolkit, Composite sectionBody) {
		Table table = super.createDefaultTable(toolkit, sectionBody);
	
		GridData gridDataTable = (GridData) table.getLayoutData();
		gridDataTable.heightHint = SINGLE_LINE_TABLE_HEIGHT;
		
		table.setLayoutData(gridDataTable);
		return table;
	}
	
	@Override
	public boolean isActive(EObject model) {
		StructuralElementInstance sei = (StructuralElementInstance) model;
		return sei.getParent() == null && super.isActive(model);
	}
}
