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

import org.junit.Test;

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
		
		Object[] plTresholds = fdirParameters.getProbabilityLevels()
				.stream().map(pl -> pl.getValue()).toArray();
		
		assertArrayEquals(OpClassifyPL.DEFAULT_PL_THRESHOLDS, plTresholds);
	}
}
