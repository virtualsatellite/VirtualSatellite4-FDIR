/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.dlr.sc.virsat.model.extension.fdir.preferences.EngineExecutionPreference;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferences;
import de.dlr.sc.virsat.model.extension.fdir.ui.Activator;

/**
 * Implements the preference page for common VirSat settings such as the
 * rounding of displayed floats
 * 
 * @author fisc_ph
 *
 */
public class FDIRWorkbenchPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PREFERENCE_FIELD_STORM_DOCKER = "STORM execution environment";
	public static final String PREFERENCE_FIELD_ENGINE = "Execution Engine";
	public static final String[][] PREFERENCE_FIELD_STORM_DOCKER_LABELS = {{ "Docker (Requires active Docker Daemon)", "Docker" }, { "Local (Requires local STORM installation)", "Local" } };
	public static final String[][] PREFERENCE_ENGINE_LABELS = {{"Custom", EngineExecutionPreference.Custom.toString()}, {"STORM DFT", EngineExecutionPreference.StormDFT.toString()}, {"Custom + STORM", EngineExecutionPreference.CustomDFT.toString()}};
	
	/**
	 * Constructor of the page
	 */
	public FDIRWorkbenchPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		RadioGroupFieldEditor useStormDockerField = new RadioGroupFieldEditor(
				FaultTreePreferences.PREFERENCE_FIELD_STORM_EXECUTION_ENVIRONMENT, PREFERENCE_FIELD_STORM_DOCKER, 1,
				PREFERENCE_FIELD_STORM_DOCKER_LABELS, getFieldEditorParent(), true);


		addField(useStormDockerField);

		ComboFieldEditor useEngine = new ComboFieldEditor(FaultTreePreferences.PREFERENCE_FIELD_ENGINE_ENVIRONMENT, PREFERENCE_FIELD_ENGINE, PREFERENCE_ENGINE_LABELS, getFieldEditorParent());
		addField(useEngine);
		
		Combo combo = (Combo)  getFieldEditorParent().getChildren()[2];
		combo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean requiresSTORM = !combo.getText().equals(FaultTreePreferences.INITIAL_PREFERENCE_VALUE_ENGINE);
				useStormDockerField.setEnabled(requiresSTORM, getFieldEditorParent());
			}
		});

	}
}
