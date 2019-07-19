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

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

/**
 * This class tests the OpClassifyPL class.
 * @author muel_s8
 *
 */
public class OpClassifyPLTest {

	private static final double TEST_EPS = 0;
	
	@Test
	public void testClassifyNoInputs() {
		OpClassifyPL op = new OpClassifyPL();
		double result = op.apply(new double[] {});
		assertEquals(Double.NaN, result, TEST_EPS);
	}

	@Test
	public void testClassifyTooManyInputs() {
		OpClassifyPL op = new OpClassifyPL();
		double result = op.apply(new double[] {0, 1});
		assertEquals(Double.NaN, result, TEST_EPS);
	}
	
	@Test
	public void testClassify2() {
		OpClassifyPL op = new OpClassifyPL();
		double result = op.apply(new double[] { 0 });
		assertEquals(OpClassifyPL.PL_EXTREMELY_REMOTE, result, TEST_EPS);
	}
	
	@Test
	public void testClassify1() {
		OpClassifyPL op = new OpClassifyPL();
		double result = op.apply(new double[] { 1 });
		assertEquals(OpClassifyPL.PL_PROBABLE, result, TEST_EPS);
	}
	
	@Test
	public void testApplyOnQuantityKinds() {
		OpClassifyPL op = new OpClassifyPL();
		assertEquals(Collections.emptyMap(), op.applyOnQuantityKinds(null, null));
	}
}
