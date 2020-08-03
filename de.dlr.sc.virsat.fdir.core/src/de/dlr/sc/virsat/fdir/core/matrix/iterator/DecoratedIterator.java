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

/**
 * Decorates an iterator with additional functionality.
 * @author muel_s8
 *
 */
public abstract class DecoratedIterator implements IMatrixIterator {

	private IMatrixIterator decoratedIterator;
	
	public DecoratedIterator(IMatrixIterator decoratedIterator) {
		this.decoratedIterator = decoratedIterator;
	}
	
	@Override
	public void iterate() {
		decoratedIterator.iterate();
	}

	@Override
	public double[] getValues() {
		return decoratedIterator.getValues();
	}
	
	@Override
	public double[] getOldValues() {
		return decoratedIterator.getOldValues();
	}
	
	@Override
	public double getChangeSquared() {
		return decoratedIterator.getChangeSquared();
	}
	
	public IMatrixIterator getDecoratedIterator() {
		return decoratedIterator;
	}

}
