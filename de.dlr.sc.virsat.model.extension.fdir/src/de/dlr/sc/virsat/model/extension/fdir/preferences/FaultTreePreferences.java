/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import de.dlr.sc.virsat.fdir.storm.runner.StormExecutionEnvironment;
import de.dlr.sc.virsat.model.extension.fdir.Activator;

/**
 * Class for managing the preferences for fault trees,
 * including and especially regarding solver information
 * @author muel_s8
 *
 */

public class FaultTreePreferences {
	
	public static final String PREFERENCE_FIELD_STORM_EXECUTION_ENVIRONMENT = "de.dlr.sc.virsat.fdir.preference.field.storm.execution";
	public static final String PREFERENCE_FIELD_ENGINE_ENVIRONMENT = "de.dlr.sc.virsat.fdir.preference.field.engine";
	
	public static final StormExecutionEnvironment INITIAL_PREFERENCE_VALUE_STORM_EXECUTION_ENVIRONEMT = StormExecutionEnvironment.Docker;
	public static final String INITIAL_PREFERENCE_VALUE_ENGINE = EngineExecutionPreference.Custom.toString();
	
	/**
	 * Hidden private constructor
	 */
	
	private FaultTreePreferences() {
		
	}

	/**
	 * Gets the execution environment for STORM
	 * @return the execution environment for STORM
	 */
	public static StormExecutionEnvironment getStormExecutionEnvironmentPreference() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId() + ".ui");
		StormExecutionEnvironment executionEnvironment = StormExecutionEnvironment.valueOf(preferences.get(PREFERENCE_FIELD_STORM_EXECUTION_ENVIRONMENT, INITIAL_PREFERENCE_VALUE_STORM_EXECUTION_ENVIRONEMT.toString()));
		return executionEnvironment;
	}
	
	/**
	 * Gets the preference for model checking
	 * @return the engine preference for model checking
	 */
	public static String getEnginePreference() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId() + ".ui");
		String enginePreference = preferences.get(PREFERENCE_FIELD_ENGINE_ENVIRONMENT, INITIAL_PREFERENCE_VALUE_ENGINE);
		return enginePreference;
	}
	
	/**
	 * Sets the execution environment for STORM
	 * @param value use docker iff value is true
	 */
	public static void setStormExecutionEnvironmentPreference(StormExecutionEnvironment value) {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId() + ".ui");
		preferences.put(PREFERENCE_FIELD_STORM_EXECUTION_ENVIRONMENT, value.toString());
	}
	
	/**
	 * Sets the Engine preference selected for model checking
	 * @param value use custom,storm dft or storm +custom
	 */
	public static void setEnginePreference(String value) {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId() + ".ui");
		preferences.put(PREFERENCE_FIELD_ENGINE_ENVIRONMENT, value);
	}
	/**
	 * Initalizes the Fault Tree Preferences
	 * @author muel_s8
	 *
	 */
	
	public static class FaultTreePreferenceInitalizer extends AbstractPreferenceInitializer {
		@Override
		public void initializeDefaultPreferences() {
			setStormExecutionEnvironmentPreference(INITIAL_PREFERENCE_VALUE_STORM_EXECUTION_ENVIRONEMT);
			setEnginePreference(INITIAL_PREFERENCE_VALUE_ENGINE);
		}
	}
}
