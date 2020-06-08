/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.matrix;

import de.dlr.sc.virsat.fdir.core.matrix.iterator.BellmanIterator;
import de.dlr.sc.virsat.fdir.core.matrix.iterator.IMatrixIterator;

public class BellmanMatrix extends SparseMatrix {

	public BellmanMatrix(int countStates) {
		super(countStates);
	}
	
	@Override
	public IMatrixIterator getIterator(double[] probabilityDistribution, double eps) {
		return new BellmanIterator(this, probabilityDistribution);
	}
}
