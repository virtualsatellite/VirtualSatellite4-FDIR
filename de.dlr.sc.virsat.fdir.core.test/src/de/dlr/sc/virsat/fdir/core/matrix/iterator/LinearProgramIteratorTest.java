/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.matrix.iterator;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.SparseMatrix;

public class LinearProgramIteratorTest {

	@Test
	public void testIterate() {
		//CHECKSTYLE:OFF
		IMatrix matrix = new SparseMatrix(2);
		matrix.setValue(0, 0, 0.3);
		matrix.setValue(0, 1, 0.7);
		matrix.setValue(1, 1, 1);
		//CHECKSTYLE:ON
		
		final double[] INITIAL_VALUES = { 1, 0 };
		final double[] EXPECTED_VALUES = { 0.3, 0.7 };
		
		LinearProgramIterator lpIterator = new LinearProgramIterator(matrix, INITIAL_VALUES);
		lpIterator.iterate();
		
		assertArrayEquals(INITIAL_VALUES, lpIterator.getOldValues(), 0);
		assertArrayEquals(EXPECTED_VALUES, lpIterator.getValues(), 0);
		
		final double[] EXPECTED_CONVERGENCE = { 0, 1};
		final double EPS = 0.000001;
		lpIterator.converge(EPS);
		
		assertArrayEquals(EXPECTED_CONVERGENCE, lpIterator.getValues(), EPS);
	}

}
