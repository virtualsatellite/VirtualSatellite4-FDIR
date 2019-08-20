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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.CriticalityMatrix;
import de.dlr.sc.virsat.model.extension.fdir.model.CutSet;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetTableFDIRParametersCriticalityMatrices extends AUiSnippetTableFDIRParametersCriticalityMatrices implements IUiSnippet {
	public static final String[] DL_NAMES = { 
		CutSet.DETECTION_VeryLikely_NAME,   
		CutSet.DETECTION_Likely_NAME,
		CutSet.DETECTION_Unlikely_NAME,
		CutSet.DETECTION_ExtremelyUnlikely_NAME
	}; 
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		FDIRParameters fdirParameters = new FDIRParameters((CategoryAssignment) initModel);
		
		for (int i = 0; i < fdirParameters.getCriticalityMatrices().size(); ++i) {
			CriticalityMatrix cm = fdirParameters.getCriticalityMatrices().get(i);
			UiSnippetTableCriticalityMatrixCriticalityMatrix criticalityMatrixSnippet = new UiSnippetTableCriticalityMatrixCriticalityMatrix();
			criticalityMatrixSnippet.createSwt(toolkit, editingDomain, composite, cm.getTypeInstance());
			int detectionLevel = i + 1;
			criticalityMatrixSnippet.getSection().setText("Section for: Criticality Matrix - Detection Level - " + detectionLevel + " - " + DL_NAMES[i]);
		}
	}
}
