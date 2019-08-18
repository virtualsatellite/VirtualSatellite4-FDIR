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

import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.CutSet;
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
public class UiSnippetTableFDIRParametersDetectionLevels extends AUiSnippetTableFDIRParametersDetectionLevels
		implements IUiSnippet {

	public static final String[] DETECTABILITY_LEVEL_NAMES = { 
		CutSet.DETECTION_VeryLikely_NAME,
		CutSet.DETECTION_Likely_NAME, 
		CutSet.DETECTION_Unlikely_NAME,
		CutSet.DETECTION_ExtremelyUnlikely_NAME 
	};

	@Override
	protected ITableLabelProvider getTableLabelProvider() {
		ITableLabelProvider superLabelProvider = super.getTableLabelProvider();

		VirSatTransactionalAdapterFactoryLabelProvider labelProvider = new VirSatTransactionalAdapterFactoryLabelProvider(
				adapterFactory) {
			@Override
			public String getColumnText(Object object, int columnIndex) {
				if (columnIndex == 0) {
					APropertyInstance pi = (APropertyInstance) object;
					return DETECTABILITY_LEVEL_NAMES[getTableObjects().indexOf(pi)];
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
