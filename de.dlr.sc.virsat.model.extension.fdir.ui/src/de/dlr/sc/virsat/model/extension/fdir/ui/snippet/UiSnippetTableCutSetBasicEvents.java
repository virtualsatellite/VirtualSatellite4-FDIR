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

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
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
public class UiSnippetTableCutSetBasicEvents extends AUiSnippetTableCutSetBasicEvents implements IUiSnippet {
	@Override
	protected ITableLabelProvider getTableLabelProvider() {
		ITableLabelProvider tableLabelProvider = super.getTableLabelProvider();
		return new VirSatTransactionalAdapterFactoryLabelProvider(adapterFactory) {
			
			@Override
			public String getColumnText(Object object, int columnIndex) {
				if (columnIndex == 1) {
					ReferencePropertyInstance rpi = (ReferencePropertyInstance) object;
					CategoryAssignment ca = (CategoryAssignment) rpi.getReference();
					if (ca == null) {
						return "";
					} else {
						BasicEvent be = new BasicEvent(ca);
						return be.getParent().getName() + "." + be.getName();
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
	}
}
