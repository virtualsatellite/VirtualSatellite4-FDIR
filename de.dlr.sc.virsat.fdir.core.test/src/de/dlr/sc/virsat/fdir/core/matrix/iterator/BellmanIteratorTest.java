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

import org.eclipse.core.runtime.SubMonitor;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.SparseMatrix;

public class BellmanIteratorTest {

	@Test
	public void testIterate() {
		//CHECKSTYLE:OFF
		IMatrix matrix = new SparseMatrix(3);
		matrix.setValue(0, 1, 0.4);
		matrix.setValue(0, 2, 0.6);
		matrix.setValue(1, 2, 1);
		//CHECKSTYLE:ON
		
		final double[] STATE_COSTS = { 0.1, 2, 0.5 };
		final double[] EXPECTED_VALUES = { 0.1, 2.04, 2.56 };
		
		LinearProgramIterator lpIterator = new BellmanIterator(matrix, STATE_COSTS);
		lpIterator.iterate();
		
		assertArrayEquals(STATE_COSTS, lpIterator.getOldValues(), 0);
		assertArrayEquals(EXPECTED_VALUES, lpIterator.getValues(), 0);
		
		final double[] EXPECTED_CONVERGENCE = { 0.1, 2.04, 2.6 };
		final double EPS = 0.000001;
		lpIterator.converge(EPS, SubMonitor.convert(null));
		
		assertArrayEquals(EXPECTED_CONVERGENCE, lpIterator.getValues(), EPS);
	}

}
