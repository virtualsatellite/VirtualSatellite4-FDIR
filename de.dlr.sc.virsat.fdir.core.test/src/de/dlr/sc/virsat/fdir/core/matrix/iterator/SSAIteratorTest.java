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

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.matrix.IMatrix;
import de.dlr.sc.virsat.fdir.core.matrix.SparseMatrix;

public class SSAIteratorTest {
	
	@Test
	public void testIterate() {
		// CHECKSTYLE:OFF
		IMatrix matrix = new SparseMatrix(3);
		double[] c1 = { 0, 0.1, 2 };
		double[] c2 = { 1, 10, 2 };
		// CHECKSTYLE:ON
		
		final double EPS = 0.0000001;
		final double[] EXPECTED_VALUES = { 0, 0.01, 1 };
		
		SSAIterator<MarkovState> ssaIterator = new SSAIterator<MarkovState>(matrix, c1, c2);
		ssaIterator.iterate();
		
		assertArrayEquals(EXPECTED_VALUES, ssaIterator.getValues(), EPS);
	}

}
