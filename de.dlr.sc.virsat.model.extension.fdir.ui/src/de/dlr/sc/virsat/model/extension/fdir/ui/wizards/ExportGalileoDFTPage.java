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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import de.dlr.sc.virsat.uiengine.ui.wizard.AImportExportPage;

/**
 * Wizard page for selecting a structural element instance to add the import data to.
 * @author muel_s8
 *
 */

public class ExportGalileoDFTPage extends AImportExportPage {

	private static final String DIALOG_TEXT = "File name";
	private static final String[] DIALOG_EXTENSIONS = { "*.dft" };

	/**
	 * Default constructor
	 * @param model the model
	 */
	protected ExportGalileoDFTPage(IContainer model) {
		super("Galileo DFT Import");
		setTitle("Please enter an export location and a VirSat object as export target.");
		setModel(model);
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		createTreeUI();
		createFileDestinationUI();
	}
	
	@Override
	protected String openDialog() {
		FileDialog dialog = new FileDialog(getContainer().getShell(), SWT.SAVE | SWT.SHEET);
		dialog.setText(DIALOG_TEXT);
		dialog.setFilterExtensions(DIALOG_EXTENSIONS);
		return dialog.open();
	}
}
