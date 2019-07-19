/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.calculation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.dlr.sc.virsat.model.calculation.compute.IExpressionResult;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the array getter.
 * @author muel_s8
 *
 */

public class ArrayGetterTest extends ATestCase {

	@Test
	public void testIsApplicableFor() {
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		
		ArrayGetter arrayGetter = new ArrayGetter();
		assertTrue(arrayGetter.isApplicableFor(fdirParameters.getProbabilityLevels().getArrayInstance()));
		assertFalse(arrayGetter.isApplicableFor(fdirParameters.getMissionTimeBean().getTypeInstance()));
	}
	
	@Test
	public void testGet() {
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		fdirParameters.setDefaultProbablityThresholds();
		
		ArrayGetter arrayGetter = new ArrayGetter();
		assertNull(arrayGetter.get(fdirParameters.getMissionTimeBean().getTypeInstance()));
		IExpressionResult result = arrayGetter.get(fdirParameters.getProbabilityLevels().getArrayInstance());
		assertTrue(result instanceof ArrayResult);
		
		ArrayResult arrayResult = (ArrayResult) result;
		
		assertEquals(OpClassifyPL.PL_LEVELS.length, arrayResult.getResults().size());
		Object[] plTresholds = arrayResult.getResults().stream()
				.map(nlr -> Double.valueOf(nlr.getNumberLiteral().getValue()))
				.toArray();
		assertArrayEquals(OpClassifyPL.DEFAULT_PL_THRESHOLDS, plTresholds);
	}
}
