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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FMECAEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.project.ui.contentProvider.VirSatTransactionalAdapterFactoryContentProvider;
import de.dlr.sc.virsat.project.ui.labelProvider.VirSatTransactionalAdapterFactoryLabelProvider;
import de.dlr.sc.virsat.uiengine.ui.editor.provider.ArrayInstanceFilteredTransactionalAdapterFactoryContentProvider;

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
public class UiSnippetTableFault extends AUiSnippetTableFault {

	public static final int LEVEL_COLUMN_SIZE = 100;
	private static final int DEFAULT_EXPANSION_LEVEL = 1;
	
	protected TreeViewer treeViewer;
	protected Tree treeTable;
	
	protected TreeViewerColumn colName;
	protected TreeViewerColumn colSeverity;
	protected TreeViewerColumn colProbability;
	protected TreeViewerColumn colDetectability;
	protected TreeViewerColumn colCriticality;
	protected TreeViewerColumn colFailureModes;
	protected TreeViewerColumn colPotentialCauses;
	protected TreeViewerColumn colPotentialEffects;
	protected TreeViewerColumn colPotentialRecovery;
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);
		
		GridData gridDataTable = createDefaultGridData();
		gridDataTable.horizontalSpan = 2;
		gridDataTable.minimumHeight = DEFAULT_TABLE_HEIGHT;
		gridDataTable.heightHint = DEFAULT_TABLE_HEIGHT;
		gridDataTable.widthHint = DEFAULT_TABLE_WIDTH;

		treeTable = toolkit.createTree(sectionBody, SWT.FULL_SELECTION | SWT.BORDER | SWT.MULTI);
		treeTable.setLinesVisible(true);
		treeTable.setLayoutData(gridDataTable);
		treeTable.setHeaderVisible(true);
		
		treeViewer = new TreeViewer(treeTable);

		colName = createTreeViewerColumn(treeViewer, "Name");
		colFailureModes = createTreeViewerColumn(treeViewer, "Failure Modes");
		colPotentialCauses = createTreeViewerColumn(treeViewer, "Potential Causes");	
		colPotentialEffects = createTreeViewerColumn(treeViewer, "Potential Effects");
		
		colSeverity = createTreeViewerColumn(treeViewer, "Severity");
		colSeverity.getColumn().setWidth(LEVEL_COLUMN_SIZE);
		colProbability = createTreeViewerColumn(treeViewer, "Probability");
		colProbability.getColumn().setWidth(LEVEL_COLUMN_SIZE);
		colDetectability = createTreeViewerColumn(treeViewer, "Detectability");
		colDetectability.getColumn().setWidth(LEVEL_COLUMN_SIZE);
		colCriticality = createTreeViewerColumn(treeViewer, "Criticality");
		colCriticality.getColumn().setWidth(LEVEL_COLUMN_SIZE);
		
		colPotentialRecovery = createTreeViewerColumn(treeViewer, "Potential Recovery");
		
		// Now wrap the CP so that only CA Container and SEIs are presented by the CP	
		ArrayInstanceFilteredTransactionalAdapterFactoryContentProvider cpTree = new ArrayInstanceFilteredTransactionalAdapterFactoryContentProvider(new VirSatTransactionalAdapterFactoryContentProvider(adapterFactory).setUpdateCaContainerLabels(true)) {
			@Override
			public Object[] getElements(Object rootObject) {
				if (rootObject instanceof StructuralElementInstance) {
					StructuralElementInstance sei = (StructuralElementInstance) rootObject;
					List<Fault> faults = new ArrayList<Fault>();
					List<StructuralElementInstance> seis = new ArrayList<>();
					seis.add(sei);
					seis.addAll(sei.getDeepChildren());
					
					for (StructuralElementInstance child : seis) {
						BeanStructuralElementInstance beanStructuralElementInstance = new BeanStructuralElementInstance();
						beanStructuralElementInstance.setStructuralElementInstance(child);
						List<Fault> childFaults = beanStructuralElementInstance.getAll(Fault.class);
						faults.addAll(childFaults.stream().filter(Fault::isLocalTopLevelFault).collect(Collectors.toList()));
					}
					return faults.stream().map(fault -> fault.getTypeInstance()).toArray(Object[]::new);
				}
				
				return super.getElements(rootObject);
			}
			
			@Override
			public Object[] getChildren(Object parentObject) {
				CategoryAssignment ca = (CategoryAssignment) parentObject;
				Fault fault = new Fault(ca);
				Set<Fault> children = fault.getFaultTree().getChildFaults();
				Object[] tiChildren = children.stream().map(child -> child.getATypeInstance()).toArray();
				return tiChildren;
			}
		};
		
		cpTree.addClassFilterToGetElement(CategoryAssignment.class);
		cpTree.addCategoryIdFilter(Fault.FULL_QUALIFIED_CATEGORY_NAME);
		FMECATableLabelProvider lpTree = new FMECATableLabelProvider(adapterFactory);

		treeViewer.setContentProvider(cpTree);
		treeViewer.setLabelProvider(lpTree);
		treeViewer.setInput(new Object[] {model});

		createExcelButton(toolkit, sectionBody, treeViewer);
	}
	
	private Button buttonEvaluate;
	
	@Override
	protected void createButtons(FormToolkit toolkit, EditingDomain editingDomain, Composite sectionBody) {
		super.createButtons(toolkit, editingDomain, sectionBody);
		
		sectionBody = (Composite) sectionBody.getChildren()[1];
		buttonEvaluate = toolkit.createButton(sectionBody, "Evaluate", SWT.PUSH);
		buttonEvaluate.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FMECAEvaluator fmecaEvaluator = new FMECAEvaluator();
				BeanStructuralElementInstance beanStructuralElementInstance = new BeanStructuralElementInstance();
				beanStructuralElementInstance.setStructuralElementInstance((StructuralElementInstance) model);
				Map<Fault, Double> mapFaultsToMTTF = fmecaEvaluator.evaluate(beanStructuralElementInstance);
				
				CompoundCommand cc = new CompoundCommand("Evaluate FMECA");
				for (Fault fault : mapFaultsToMTTF.keySet()) {
					Double mttf = mapFaultsToMTTF.get(fault);
					Command setCommand = fault.getMeanTimeToFailureBean().setValue(editingDomain, mttf);
					cc.append(setCommand);
				}
				
				editingDomain.getCommandStack().execute(cc);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		checkWriteAccess(buttonEvaluate);	
	}
	
	/**
	 * Method to create the standard column
	 * @param treeViewer The Viewer to which to add a column
	 * @param columnText The Name of the Column
	 * @return The Column itself
	 */
	private TreeViewerColumn createTreeViewerColumn(TreeViewer treeViewer, String columnText) {
		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.NONE);
		column.getColumn().setText(columnText);
		column.getColumn().setWidth(DEFAULT_COLUMN_SIZE);
		
		return column;
	}
	
	@Override
	protected void setTableViewerInput() {
		super.setTableViewerInput();
		if (treeViewer != null) {
			treeViewer.setInput(model);
			treeViewer.expandToLevel(DEFAULT_EXPANSION_LEVEL);	
		}
	}
		
	/**
	 * The common label provider to display mass summary and mass equipment CAs correctly
	 * it also supports the interface needed by the pie chart viewers
	 * @author fisc_ph
	 *
	 */
	class FMECATableLabelProvider 
		extends VirSatTransactionalAdapterFactoryLabelProvider {
		
		/**
		 * Constructor with injected EMF AdapterfActory for correct notification etc.
		 * @param adapterFactory The EMF AdapterFactory to display the correct content
		 */ 
		FMECATableLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}
		
		@Override
		public Image getColumnImage(Object object, int columnIndex) {
			TreeColumn column = treeViewer.getTree().getColumn(columnIndex);

			if (column != colName.getColumn()) {
				return null;
			}
			
			Fault fault = new Fault((CategoryAssignment) object);
			return super.getColumnImage(fault.getTypeInstance(), columnIndex);
		}
		
		@Override
		public String getColumnText(Object object, int columnIndex) {
			CategoryAssignment ca = (CategoryAssignment) object;
			redirectNotification(ca, object);
			
			TreeColumn column = treeViewer.getTree().getColumn(columnIndex);

			Fault fault = new Fault(ca);
			redirectNotification(fault, ca, true);
			if (column == colName.getColumn()) {
				return fault.getParent().getName() + "." + fault.getName();
			} else if (column == colProbability.getColumn()) {
				if (fault.getProbabilityLevelBean().getValue() != null) {
					return String.valueOf((int) fault.getProbabilityLevelEnum());
				}
			} else if (column == colDetectability.getColumn()) {
				if (fault.getDetectionLevelBean().getValue() != null) {
					return String.valueOf((int) fault.getDetectionLevelEnum());
				}
			} else if (column == colSeverity.getColumn()) {
				if (fault.getSeverityLevelBean().getValue() != null) {
					return String.valueOf((int) fault.getSeverityLevelEnum());
				}
			} else if (column == colCriticality.getColumn()) {
				if (fault.isSetCriticalityLevel()) {
					return String.valueOf((int) fault.getCriticalityLevel());
				}
			} else if (column == colFailureModes.getColumn()) {
				Set<ABeanCategoryAssignment> allFailureModes = new HashSet<>();
				allFailureModes.addAll(fault.getFaultTree().getChildFaults());
				allFailureModes.addAll(fault.getBasicEvents());
				for (ABeanCategoryAssignment fm : allFailureModes) {
					redirectNotification(fm, fault, true);
				}
				return allFailureModes.stream().map(bean -> bean instanceof Fault ? bean.getParent().getName() + "." + bean.getName() : bean.getName()).collect(Collectors.joining(", "));
			} else if (column == colPotentialCauses.getColumn()) {
				Set<ABeanCategoryAssignment> allCauses = new HashSet<>();
				Set<Fault> childFaults = fault.getFaultTree().getChildFaults();
				for (Fault childFault : childFaults) {
					allCauses.addAll(childFault.getBasicEvents());
					allCauses.addAll(childFault.getFaultTree().getChildFaults());
				}
				for (ABeanCategoryAssignment cause : allCauses) {
					redirectNotification(cause, fault, true);
				}
				return allCauses.stream().map(bean -> bean.getParent().getName() + "." + bean.getName()).collect(Collectors.joining(", "));
			} else if (column == colPotentialEffects.getColumn()) {
				Set<Fault> affectedFaults = fault.getFaultTree().getAffectedFaults();
				return affectedFaults.stream().map(bean -> bean.getParent().getName() + "." + bean.getName()).collect(Collectors.joining(", "));
			} else if (column == colPotentialRecovery.getColumn()) {
				List<String> potentialRecoveryActions = fault.getFaultTree().getPotentialRecoveryActions();
				return potentialRecoveryActions.stream().collect(Collectors.joining(", "));
			}
			return "";
		}
	}
}
