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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.FMECAEntry;

/**
 * This class tests the OpClassifyPL class.
 * @author muel_s8
 *
 */
public class OpClassifyPLTest {

	private static final double TEST_EPS = 0;
	private List<Double> inputs;
	private OpClassifyPL op = new OpClassifyPL();
	
	@Before
	public void setUp() {
		inputs = new ArrayList<>();
		inputs.addAll(Arrays.asList(OpClassifyPL.DEFAULT_PL_THRESHOLDS));
		inputs.addAll(Arrays.asList(OpClassifyDL.DEFAULT_DL_THRESHOLDS));
	}
	
	@Test
	public void testClassifyNoInputs() {
		double result = op.apply(new double[] {});
		assertEquals(FMECAEntry.PL_UNKNOWN, result, TEST_EPS);
	}

	@Test
	public void testClassifyTooManyInputs() {
		double result = op.apply(new double[] {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1});
		assertEquals(FMECAEntry.PL_UNKNOWN, result, TEST_EPS);
	}
	
	@Test
	public void testClassify0() {
		inputs.add(0, 0d);
		double result = op.apply(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		assertEquals(FMECAEntry.PL_EXTREMELY_REMOTE, result, TEST_EPS);
	}
	
	@Test
	public void testClassify1() {
		inputs.add(0, 1d);
		double result = op.apply(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		assertEquals(FMECAEntry.PL_PROBABLE, result, TEST_EPS);
	}
	
	@Test
	public void testClassifyNaN() {
		inputs.add(0, Double.NaN);
		double result = op.apply(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		assertEquals(FMECAEntry.PL_UNKNOWN, result, TEST_EPS);
	}
	
	@Test
	public void testApplyOnQuantityKinds() {
		assertEquals(Collections.emptyMap(), op.applyOnQuantityKinds(null, null));
	}
}
