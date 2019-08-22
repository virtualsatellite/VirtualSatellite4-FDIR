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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.AProperty;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ComposedPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.CriticalityMatrix;
import de.dlr.sc.virsat.model.extension.fdir.model.CriticalityVector;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;
import de.dlr.sc.virsat.model.extension.fdir.model.FMECAEntry;
import de.dlr.sc.virsat.project.ui.labelProvider.VirSatTransactionalAdapterFactoryLabelProvider;
import de.dlr.sc.virsat.uiengine.ui.cellEditor.aproperties.BooleanPropertyCellEditingSupport;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetTableCriticalityMatrixCriticalityMatrix extends AUiSnippetTableCriticalityMatrixCriticalityMatrix implements IUiSnippet {
	
	public static final Color CRITICAL_COLOR = ColorConstants.orange;
	public static final Color UNCRITICAL_COLOR = new Color(Display.getDefault(), 144, 238, 144);
	
	public static final String COLUMN_SEVERTY_NAME = "Severity";
	public static final String[] PL_NAMES = { 
		FMECAEntry.PROBABILITY_ExtremelyRemote_NAME,   
		FMECAEntry.PROBABILITY_Remote_NAME,
		FMECAEntry.PROBABILITY_Occasional_NAME,
		FMECAEntry.PROBABILITY_Probable_NAME
	};
	
	public static final String[] SL_NAMES = { 
		FMECAEntry.SEVERITY_Catastrophic_NAME,   
		FMECAEntry.SEVERITY_Critical_NAME,
		FMECAEntry.SEVERITY_Major_NAME,
		FMECAEntry.SEVERITY_Minor_NAME
	}; 
	
	public static final int DEFAULT_TABLE_HEIGHT = 21 * 4;
	public static final int DEFAULT_COLUMN_SIZE = 125;
	
	@Override
	protected Table createDefaultTable(FormToolkit toolkit, Composite sectionBody) {
		GridData gridDataTable = createDefaultGridData();
		gridDataTable.horizontalSpan = 1;
		gridDataTable.minimumHeight = DEFAULT_TABLE_HEIGHT;
		gridDataTable.heightHint = DEFAULT_TABLE_HEIGHT;
		gridDataTable.widthHint = DEFAULT_TABLE_WIDTH;
		
		Table table = toolkit.createTable(sectionBody, SWT.FULL_SELECTION);
		table.setLayoutData(gridDataTable);
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		
		return table;
	}
	
	@Override
	protected void setUpTableViewer(EditingDomain editingDomain, FormToolkit toolkit) {
		super.setUpTableViewer(editingDomain, toolkit);
		ITableLabelProvider labelProvider = (ITableLabelProvider) columnViewer.getLabelProvider();
		columnViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				String lhString = labelProvider.getColumnText(e1, 0);
				String rhString = labelProvider.getColumnText(e2, 0);
				
				if (lhString != null && rhString != null) { 
					return rhString.compareTo(lhString);
				} else {
					return 0;
				}
			}
		});
	}
	
	@Override
	protected ViewerColumn createDefaultColumn(String columnText) {
		TableViewer tableViewer = (TableViewer) columnViewer;
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setText(columnText);
		column.getColumn().setWidth(DEFAULT_COLUMN_SIZE);
		
		return column;
	}
	
	@Override
	protected void createTableColumns(EditingDomain editingDomain) {
		createDefaultColumn(COLUMN_SEVERTY_NAME);
		
		CriticalityMatrix cm = new CriticalityMatrix((CategoryAssignment) model);
		AProperty isCriticalProperty = (AProperty) cm.getCriticalityMatrix().get(0).getIsCritical().get(0).getTypeInstance().getType();
		for (int i = 0; i < PL_NAMES.length; ++i) {
			final int level = i + 1;
			String columnName = level + " - " + PL_NAMES[i];
			TableViewerColumn colPL = (TableViewerColumn) createDefaultColumn(columnName);
			colPL.getColumn().setToolTipText("Probability level " + PL_NAMES[i]);
			colPL.setEditingSupport(new BooleanPropertyCellEditingSupport(editingDomain, columnViewer, isCriticalProperty) {
				
				@Override
				protected boolean canEdit(Object element) {
					return super.canEdit(getCriticalityElement(element));
				}
				
				@Override
				protected void setValue(Object element, Object userInputValue) {
					super.setValue(getCriticalityElement(element), userInputValue);
				}
				
				@Override
				protected Object getValue(Object element) {
					return super.getValue(getCriticalityElement(element));
				}
				
				protected Object getCriticalityElement(Object element) {
					if (element instanceof ValuePropertyInstance) {
						return element;
					}
					
					ComposedPropertyInstance cpi = (ComposedPropertyInstance) element;
					CriticalityVector cv = new CriticalityVector(cpi.getTypeInstance());
					Object criticalityElement = cv.getIsCritical().get(level - 1).getTypeInstance();
					return criticalityElement;
				}
			});
		}
	}
	
	/**
	 * Label provider that displays the criticality and marks the field
	 * @author muel_s8
	 *
	 */
	private class CriticalityLabelProvider extends VirSatTransactionalAdapterFactoryLabelProvider 
		implements ITableColorProvider {
		
		/**
		 * Standard constructor
		 * @param adapterFactory the adapter factory
		 */
		CriticalityLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public String getColumnText(Object object, int columnIndex) {
			CriticalityMatrix cm = new CriticalityMatrix((CategoryAssignment) model);
			ComposedPropertyInstance cpi = (ComposedPropertyInstance) object;
			CriticalityVector cv = new CriticalityVector(cpi.getTypeInstance());
			FDIRParameters fdirParameters = cm.getParentCaBeanOfClass(FDIRParameters.class);
			
			int index = cm.getCriticalityMatrix().indexOf(cv);
			int severity = cm.getCriticalityMatrix().size() - index;
			int detection = fdirParameters.getCriticalityMatrices().indexOf(cm) + 1;
			
			if (columnIndex == 0) {
				String label = severity + " - " + SL_NAMES[index];
				return label;
			} else {
				redirectNotification(cv.getIsCritical().get(columnIndex - 1).getTypeInstance(), object);
				int criticality = columnIndex * severity * detection;
				return String.valueOf(criticality);
			}
		}
		
		@Override
		public Image getColumnImage(Object object, int columnIndex) {
			return null;
		}
		
		@Override
		public Color getBackground(Object object, int columnIndex) {
			ComposedPropertyInstance cpi = (ComposedPropertyInstance) object;
			CriticalityVector cv = new CriticalityVector(cpi.getTypeInstance());
			if (columnIndex > 0) {
				boolean isCritical = cv.getIsCritical().get(columnIndex - 1).getValue();
				if (isCritical) {
					return CRITICAL_COLOR;
				} else {
					return UNCRITICAL_COLOR;
				}
			}
			
			return super.getBackground(object, columnIndex);
		}
	}
	
	@Override
	protected ITableLabelProvider getTableLabelProvider() {
		return new CriticalityLabelProvider(adapterFactory);
	}
	
	@Override
	protected void createButtons(FormToolkit toolkit, EditingDomain editingDomain, Composite sectionBody) {

	}
}
