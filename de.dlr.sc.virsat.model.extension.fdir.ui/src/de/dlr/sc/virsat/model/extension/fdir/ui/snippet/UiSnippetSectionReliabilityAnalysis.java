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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityAnalysis;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetSectionReliabilityAnalysis extends AUiSnippetSectionReliabilityAnalysis implements IUiSnippet {

	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);

		Composite buttonSectionBody = toolkit.createComposite(composite);
		GridLayout layout = new GridLayout(1, true);
		buttonSectionBody.setLayout(layout);
		Button buttonUpdate = toolkit.createButton(buttonSectionBody, "Perform Analysis", SWT.PUSH);
		buttonUpdate.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				Job job = new Job("Reliability Analysis") {
					
					@Override
					protected IStatus run(IProgressMonitor monitor) {	
						monitor.setTaskName("Reliability Analysis");
						ReliabilityAnalysis relAnalysis = new ReliabilityAnalysis((CategoryAssignment) model);
						Command reliabilityAnalysisCommand = relAnalysis
								.perform((TransactionalEditingDomain) editingDomain, monitor);
						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
						editingDomain.getCommandStack().execute(reliabilityAnalysisCommand);
						return Status.OK_STATUS;
					}

				};

				job.setUser(true);
				job.schedule();
				new UiSnippetTableReliabilityAnalysisReliabilityCurve().getXyPlotChartViewer().refresh();
			}
		});

	}

	
}
