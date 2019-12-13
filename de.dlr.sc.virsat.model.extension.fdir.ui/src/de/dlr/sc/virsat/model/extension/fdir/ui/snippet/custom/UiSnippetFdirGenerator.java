/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.snippet.custom;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.DelegateSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ISynthesizer;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.AUiSectionSnippet;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;

/**
 * This snippet holds the buttons for performing all the model
 * generation for FDIR
 * @author muel_s8
 *
 */

public class UiSnippetFdirGenerator extends AUiSectionSnippet implements IUiSnippet {

	private static final String SECTION_NAME = "FDIR Generation";
	private static final String SECTION_HEADING = "Table Section FDIR generation: ";
	
	//private static final String BUTTON_GENERATE_FAULT_TREE_TEXT = "Generate Fault Trees";
	private static final String BUTTON_GENERATE_RECOVERY_AUTOMATON_TEXT = "Generate Recovery Automaton";
	
	//private Button buttonGenerateFaultTrees;
	private Button buttonGenerateRecoverystrategy;
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);
		
		Composite sectionBody = createSectionBody(toolkit, SECTION_HEADING + SECTION_NAME, null, 1);
		
		buttonGenerateRecoverystrategy = toolkit.createButton(sectionBody, BUTTON_GENERATE_RECOVERY_AUTOMATON_TEXT, SWT.PUSH);
		buttonGenerateRecoverystrategy.addSelectionListener(new SelectionListener() { 
			@Override
			public void widgetSelected(SelectionEvent e) {
				VirSatTransactionalEditingDomain virSatEd = (VirSatTransactionalEditingDomain) editingDomain;
				IBeanStructuralElementInstance beanSei = new BeanStructuralElementInstance((StructuralElementInstance) model);
				Fault fault = beanSei.getFirst(Fault.class);
				
				if (fault != null) {
					ISynthesizer synthesizer = new DelegateSynthesizer();
					RecoveryAutomaton ra = synthesizer.synthesize(fault);
					Command addCommand = beanSei.add(virSatEd, ra);
					virSatEd.getCommandStack().execute(addCommand);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {		
	}

	@Override
	public ISelection getSelection() {
		return null;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
	}

	@Override
	public void setSelection(ISelection selection) {
	}

	@Override
	public boolean isActive(EObject model) {
		return model instanceof StructuralElementInstance;
	}

	@Override
	public QualifiedName getSectionExpansionStateKey() {
		return new QualifiedName(UI_SECTION_SNIPPET_ID, SECTION_NAME);
	}

}
