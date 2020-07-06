/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyDL;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyPL;

// *****************************************************************
// * Import Statements
// *****************************************************************



// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public class FDIRParametersTest extends AFDIRParametersTest {
	@Test
	public void testSetDefaultProbabilityLevels() {
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		fdirParameters.setDefaultProbablityThresholds();
		
		Object[] plTresholds = fdirParameters.getProbabilityLevelsBean()
				.stream().map(pl -> pl.getValue()).toArray();
		
		assertArrayEquals(OpClassifyPL.DEFAULT_PL_THRESHOLDS, plTresholds);
	}
	
	@Test
	public void testSetDefaultDetectabilityLevels() {
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		fdirParameters.setDefaultDetectabilityThresholds();
		
		Object[] dlTresholds = fdirParameters.getDetectionLevelsBean()
				.stream().map(dl -> dl.getValue()).toArray();
		
		assertArrayEquals(OpClassifyDL.DEFAULT_DL_THRESHOLDS, dlTresholds);
	}
	
	@Test
	public void testSetIsCritical() {
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		fdirParameters.setDefaultCriticalityMatrix();
		
		assertFalse(fdirParameters.isDefaultCritical(
			Integer.valueOf(CutSet.DETECTION_VeryLikely_VALUE), 
			Integer.valueOf(CutSet.SEVERITY_Minor_VALUE), 
			Integer.valueOf(CutSet.PROBABILITY_ExtremelyRemote_VALUE))
		);
		
		assertTrue(fdirParameters.isDefaultCritical(
			Integer.valueOf(CutSet.DETECTION_VeryLikely_VALUE), 
			Integer.valueOf(CutSet.SEVERITY_Catastrophic_VALUE), 
			Integer.valueOf(CutSet.PROBABILITY_ExtremelyRemote_VALUE))
		);
	}
}
