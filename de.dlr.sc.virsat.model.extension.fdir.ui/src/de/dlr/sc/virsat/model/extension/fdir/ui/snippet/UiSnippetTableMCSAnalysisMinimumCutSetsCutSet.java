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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ComposedPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
import de.dlr.sc.virsat.model.extension.fdir.model.CutSet;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;
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
public class UiSnippetTableMCSAnalysisMinimumCutSetsCutSet extends AUiSnippetTableMCSAnalysisMinimumCutSetsCutSet implements IUiSnippet {
	
	public static final int CUT_SET_COLUMN = 2;
	public static final int CRITICALITY_COLUMN = 6;
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		hideNameColumn = true;
		super.createSwt(toolkit, editingDomain, composite, initModel);
	}
	
	/**
	 * Label Provider that wraps a label provider and exetnds it to also marks the critical
	 * @author muel_s8
	 *
	 */
	private class CriticalityLabelProvider extends VirSatTransactionalAdapterFactoryLabelProvider
		implements ITableColorProvider {

		private ITableLabelProvider tableLabelProvider;
		
		/**
		 * Standard constructor
		 * @param tableLabelProvider the original label provider
		 * @param adapterFactory the adapter factory
		 */
		CriticalityLabelProvider(ITableLabelProvider tableLabelProvider, AdapterFactory adapterFactory) {
			super(adapterFactory);
			this.tableLabelProvider = tableLabelProvider;
		}
		
		@Override
		public String getColumnText(Object object, int columnIndex) {
			ComposedPropertyInstance cpi = (ComposedPropertyInstance) object;
			CategoryAssignment ca = cpi.getTypeInstance();
			
			if (ca == null) {
				return tableLabelProvider.getColumnText(ca, columnIndex);
			}
			
			CutSet cutSet = new CutSet(ca);
			redirectNotification(cutSet, object, true);
			
			if (columnIndex == CUT_SET_COLUMN) {
				return cutSet.getBasicEventsLabel();
			} else if (columnIndex == CRITICALITY_COLUMN) {
				if (cutSet.getCriticality() == 0) {
					return CutSet.SEVERITY_Unknown_NAME;
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
		
		@Override
		public Color getBackground(Object object, int columnIndex) {
			ComposedPropertyInstance cpi = (ComposedPropertyInstance) object;
			CategoryAssignment ca = cpi.getTypeInstance();
			
			if (ca == null) {
				return null;
			}
			
			CutSet cutSet = new CutSet(ca);
			
			if (columnIndex == CRITICALITY_COLUMN) {
				if (cutSet.getCriticality() == 0) {
					return ColorConstants.lightGray;
				} else {
					StructuralElementInstance root = (StructuralElementInstance) VirSatEcoreUtil.getRootContainer(cutSet.getParent().getStructuralElementInstance());
					BeanStructuralElementInstance rootBean = new BeanStructuralElementInstance(root);
					List<IBeanStructuralElementInstance> beanSeis = new ArrayList<>(rootBean.getDeepChildren(IBeanStructuralElementInstance.class));
					beanSeis.add(rootBean);
					List<FDIRParameters> fdirParameters = beanSeis.stream()
						.flatMap(bean -> bean.getAll(FDIRParameters.class).stream())
						.collect(Collectors.toList());
					
					if (!fdirParameters.isEmpty()) {
						int detecatable = (int) cutSet.getDetectionEnum();
						int severity = (int) cutSet.getSeverityEnum();
						int probability = (int) cutSet.getProbabilityEnum();
						boolean isCritical = fdirParameters.get(0).isCritical(detecatable, severity, probability);
						if (isCritical) {
							return UiSnippetTableCriticalityMatrixCriticalityMatrix.CRITICAL_COLOR;
						} else {
							return UiSnippetTableCriticalityMatrixCriticalityMatrix.UNCRITICAL_COLOR;
						}
					}
				}
			}
		
			return super.getBackground(object, columnIndex);
		}
	}
		
	@Override
	protected ITableLabelProvider getTableLabelProvider() {
		ITableLabelProvider tableLabelProvider = super.getTableLabelProvider();
		return new CriticalityLabelProvider(tableLabelProvider, adapterFactory);
	}
}
