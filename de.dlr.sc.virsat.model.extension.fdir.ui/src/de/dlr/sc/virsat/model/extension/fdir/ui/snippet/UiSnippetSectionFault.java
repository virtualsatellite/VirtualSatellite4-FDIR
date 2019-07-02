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

import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetSectionFault extends AUiSnippetSectionFault implements IUiSnippet {
	
	//private Button buttonSynth;
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject model) {
		super.createSwt(toolkit, editingDomain, composite, model);
			/*
		buttonSynth = toolkit.createButton(composite, "Synthesize RecoveryAutomaton", SWT.PUSH);
		
		buttonSynth.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BasicSynthesizer synth = new BasicSynthesizer();
				RecoveryAutomaton ra = synth.synthesize(new Fault((CategoryAssignment) model));
				RecordingCommand rc = new RecordingCommand((TransactionalEditingDomain) editingDomain) {
					@Override
					protected void doExecute() {
						StructuralElementInstance sei = (StructuralElementInstance) CategoryAssignmentHelper.getContainerFor((ATypeInstance) model);
						sei.getCategoryAssignments().add(ra.getTypeInstance());
					}
				};
				editingDomain.getCommandStack().execute(rc);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		*/
	}
}
