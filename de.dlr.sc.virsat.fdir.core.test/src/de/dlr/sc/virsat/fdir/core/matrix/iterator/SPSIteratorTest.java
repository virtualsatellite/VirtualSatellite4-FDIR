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

public class SPSIteratorTest {

	@Test
	public void testIterate() {
		// CHECKSTYLE:OFF
		IMatrix matrix = new SparseMatrix(2);
		matrix.setValue(0, 0, -2);
		matrix.setValue(0, 1, 2);
		// CHECKSTYLE:ON
		
		final double EPS = 0.0000001;
		final double[] INITIAL_DISTRIBUTION = { 1, 0 };
		final double[] EXPECTED_VALUES = { Math.exp(-2), 1 - Math.exp(-2) };
		
		SPSIterator spsIterator = new SPSIterator(matrix, INITIAL_DISTRIBUTION, EPS);
		spsIterator.iterate();
		
		assertArrayEquals(EXPECTED_VALUES, spsIterator.getValues(), EPS);
	}

}
