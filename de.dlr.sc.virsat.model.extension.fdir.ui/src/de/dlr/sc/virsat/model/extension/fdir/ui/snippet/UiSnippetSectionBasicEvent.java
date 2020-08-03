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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.qudv.AUnit;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.util.BasicEventHolder;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetSectionBasicEvent extends AUiSnippetSectionBasicEvent implements IUiSnippet {
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);
		decorateDistributionEnumViewer(editingDomain);
		decorateHotFailureRateUnitViewer();
	}

	/**
	 * Equips the unit viewer of the hot failure rate property with the approriate filters
	 * for their current quantity kind.
	 */
	private void decorateHotFailureRateUnitViewer() {
		String propertyFqn = BasicEvent.FULL_QUALIFIED_CATEGORY_NAME + "." + BasicEvent.PROPERTY_HOTFAILURERATE;
		ComboViewer comboViewerUnit = mapPropertyToComboViewerUnit.get(propertyFqn);
		comboViewerUnit.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				BasicEvent be = new BasicEvent((CategoryAssignment) model);
				BasicEventHolder beHolder = new BasicEventHolder(be);
				String quantityKindToShow = beHolder.getQuantityKindForDistribution();
				if (element instanceof AUnit) {
					return ((AUnit) element).getQuantityKind().getName().equals(quantityKindToShow);
				}
				
				return true;
			}
		});
	}
	
	/**
	 * Equips the enum distribution viewer with the ability to switch the appropriate units
	 * when selected a different distribution.
	 * @param editingDomain the editing domain
	 */
	private void decorateDistributionEnumViewer(EditingDomain editingDomain) {
		String distributionPropertyFqn = BasicEvent.FULL_QUALIFIED_CATEGORY_NAME + "." + BasicEvent.PROPERTY_DISTRIBUTION;
		ComboViewer comboViewerEnum = mapPropertyToComboViewerEnum.get(distributionPropertyFqn);
		comboViewerEnum.addPostSelectionChangedListener(event -> {
			String hotFailureRatePropertyFqn = BasicEvent.FULL_QUALIFIED_CATEGORY_NAME + "." + BasicEvent.PROPERTY_HOTFAILURERATE;
			ComboViewer comboViewerUnit = mapPropertyToComboViewerUnit.get(hotFailureRatePropertyFqn);
			comboViewerUnit.refresh();
			
			BasicEvent be = new BasicEvent((CategoryAssignment) model);
			BasicEventHolder.synchronizeWithDistribution(editingDomain, be);
		});
	}
}
