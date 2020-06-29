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

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dlr.sc.virsat.apps.ui.Activator;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomatonGen;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ISynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;
import de.dlr.sc.virsat.project.structure.VirSatProjectCommons;
import de.dlr.sc.virsat.uiengine.ui.editor.snippets.IUiSnippet;


/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class UiSnippetSectionRecoveryAutomatonGen extends AUiSnippetSectionRecoveryAutomatonGen implements IUiSnippet {
	
	private static final String BUTTON_GENERATE_RECOVERY_AUTOMATON_TEXT = "Generate Recovery Automaton";
	
	private ISynthesizer synthesizer = new ModularSynthesizer();
	
	@Override
	public void createSwt(FormToolkit toolkit, EditingDomain editingDomain, Composite composite, EObject initModel) {
		super.createSwt(toolkit, editingDomain, composite, initModel);
		
		Composite buttonSectionBody = toolkit.createComposite(composite);
		GridLayout layout = new GridLayout(1, true);
		buttonSectionBody.setLayout(layout);
		
		Button buttonGenerateRecoverystrategy = toolkit.createButton(buttonSectionBody, BUTTON_GENERATE_RECOVERY_AUTOMATON_TEXT, SWT.PUSH);
		buttonGenerateRecoverystrategy.addSelectionListener(new SelectionListener() { 
			@Override
			public void widgetSelected(SelectionEvent event) {
				VirSatTransactionalEditingDomain virSatEd = (VirSatTransactionalEditingDomain) editingDomain;
				RecoveryAutomatonGen beanRaGen = new RecoveryAutomatonGen((CategoryAssignment) model);
				String objectiveMetricName = beanRaGen.getObjectiveMetric();
				IMetric objectiveMetric = RecoveryAutomatonGen.getObjectiveMetricFromName(objectiveMetricName);
				IBeanStructuralElementInstance beanSei = beanRaGen.getParent();
				Fault fault = beanSei.getFirst(Fault.class);
				
				if (fault != null) {
					SynthesisQuery synthesisQuery = new SynthesisQuery(fault);
					if (objectiveMetric != null) {
						synthesisQuery.setObjectiveMetric(objectiveMetric);
					}
					RecoveryAutomaton ra = synthesizer.synthesize(synthesisQuery, null);
					String statistics = synthesizer.getStatistics().toString();
					
					IFolder documentFolder = VirSatProjectCommons.getDocumentFolder(beanSei.getStructuralElementInstance());
					IFile logFile = documentFolder.getFile(RecoveryAutomatonGen.LOG_FILE_NAME);
					
					try {
						if (logFile.exists()) {
							logFile.delete(true, new NullProgressMonitor());
						}
						logFile.create(new ByteArrayInputStream(statistics.getBytes()), true, new NullProgressMonitor());
						String logFileFilePath = logFile.getFullPath().toOSString();
						URI uri = URI.createPlatformResourceURI(logFileFilePath, false);
						
						Command setCommand = beanRaGen.setLastGenerationLog(virSatEd, uri);
						virSatEd.getCommandStack().execute(setCommand);
					} catch (CoreException e) {
						Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.getPluginId(), "Failed to create log file for synthesis", e));
					}
					
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
}
