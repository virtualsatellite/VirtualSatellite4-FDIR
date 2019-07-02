/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.wizards;

import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.Repository;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.converter.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.ui.Activator;
import de.dlr.sc.virsat.project.editingDomain.VirSatEditingDomainRegistry;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;

/**
 * Wizard for importaing galileo .dft format file dfts.
 * @author muel_s8
 *
 */

public class ImportGalileoDFTWizard extends Wizard implements INewWizard {
	
	public static final String ID = "de.dlr.sc.virsat.model.extension.fdir.ui.importGalileo";

	private ImportGalileoDFTPage page;
	private IContainer model;
	
	/**
	 * Default constructor
	 */
	public ImportGalileoDFTWizard() {
		super();
		setWindowTitle("Import Galileo DFT");
		
		// Setup persistency if necessary
        IDialogSettings pluginSettings = Activator.getDefault().getDialogSettings();
        IDialogSettings wizardSettings = pluginSettings.getSection(ID);
        if (wizardSettings == null) {
            wizardSettings = new DialogSettings(ID);
            pluginSettings.addSection(wizardSettings);
        }
        setDialogSettings(wizardSettings);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.model = ResourcesPlugin.getWorkspace().getRoot();
	}

	@Override
	public boolean performFinish() {
		StructuralElementInstance sei = (StructuralElementInstance) page.getSelection();
		
		// grab an input
		String path = page.getDestination();
		try {
			InputStream inputStream = new FileInputStream(path);
			
			// grab the concept
			VirSatTransactionalEditingDomain ed = VirSatEditingDomainRegistry.INSTANCE.getEd(sei);
			Repository repo = ed.getResourceSet().getRepository(); 
			ActiveConceptHelper acHelper = new ActiveConceptHelper(repo);
			Concept concept = acHelper.getConcept(de.dlr.sc.virsat.model.extension.fdir.Activator.getPluginId());
			
			RecordingCommand importCommand = new RecordingCommand(ed, "Import Galileo DFT") {
				@Override
				protected void doExecute() {
					BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance();
					beanSei.setStructuralElementInstance(sei);
					GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, inputStream, beanSei);
					converter.convert();
				}
			};
			ed.getCommandStack().execute(importCommand);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return false;
	}
	
    @Override
    public void addPages() {
        super.addPages();
        page = new ImportGalileoDFTPage(model);
        addPage(page);
    }
}
