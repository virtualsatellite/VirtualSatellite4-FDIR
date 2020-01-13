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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import de.dlr.sc.virsat.fdir.galileo.GalileoDFTWriter;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.converter.DFT2GalileoDFT;
import de.dlr.sc.virsat.model.extension.fdir.ui.Activator;

/**
 * Wizard for importaing galileo .dft format file dfts.
 * @author muel_s8
 *
 */

public class ExportGalileoDFTWizard extends Wizard implements INewWizard {
	
	public static final String ID = "de.dlr.sc.virsat.model.extension.fdir.ui.exportGalileo";

	private ExportGalileoDFTPage page;
	private IContainer model;
	
	/**
	 * Default constructor
	 */
	public ExportGalileoDFTWizard() {
		super();
		setWindowTitle("Export Galileo DFT");
		
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
			DFT2GalileoDFT converter = new DFT2GalileoDFT(false);
			GalileoDft galileoDft = converter.convert(sei);
			new GalileoDFTWriter(path).write(galileoDft);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return false;
	}
	
    @Override
    public void addPages() {
        super.addPages();
        page = new ExportGalileoDFTPage(model);
        addPage(page);
    }
}
