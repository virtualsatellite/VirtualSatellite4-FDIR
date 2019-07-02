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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.storm.runner.StormExecutionEnvironment;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferences.FaultTreePreferenceInitalizer;

/**
 * This class test the FaultTreePreferences store class.
 * @author muel_s8
 *
 */

public class FaultTreePreferencesTest {

	@Test
	public void testSetStormExecutionEnvironmentPreference() {
		FaultTreePreferences.setStormExecutionEnvironmentPreference(StormExecutionEnvironment.Docker);
		assertEquals(StormExecutionEnvironment.Docker, FaultTreePreferences.getStormExecutionEnvironmentPreference());
		FaultTreePreferences.setStormExecutionEnvironmentPreference(StormExecutionEnvironment.Local);
		assertEquals(StormExecutionEnvironment.Local, FaultTreePreferences.getStormExecutionEnvironmentPreference());
	}
	
	@Test
	public void testSetEnginePreference() {
		FaultTreePreferences.setEnginePreference(EngineExecutionPreference.Custom.toString());
		assertEquals(EngineExecutionPreference.Custom.toString(), FaultTreePreferences.getEnginePreference());
		FaultTreePreferences.setEnginePreference(EngineExecutionPreference.StormDFT.toString());
		assertEquals(EngineExecutionPreference.StormDFT.toString(), FaultTreePreferences.getEnginePreference());
		FaultTreePreferences.setEnginePreference(EngineExecutionPreference.CustomDFT.toString());
		assertEquals(EngineExecutionPreference.CustomDFT.toString(), FaultTreePreferences.getEnginePreference());
		FaultTreePreferences.setEnginePreference(EngineExecutionPreference.Custom.toString());
	}
	
	@Test
	public void testInitializeDefaultPreferences() {
		FaultTreePreferenceInitalizer initializer = new FaultTreePreferences.FaultTreePreferenceInitalizer();
		initializer.initializeDefaultPreferences();
		
		assertEquals(FaultTreePreferences.INITIAL_PREFERENCE_VALUE_STORM_EXECUTION_ENVIRONEMT, FaultTreePreferences.getStormExecutionEnvironmentPreference());
		assertEquals(FaultTreePreferences.INITIAL_PREFERENCE_VALUE_ENGINE, FaultTreePreferences.getEnginePreference());
	}
}
