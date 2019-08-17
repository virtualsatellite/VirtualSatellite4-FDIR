/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

/**
 * A base metric is a metric that can be model checker for
 * @author muel_s8
 *
 */

public interface IBaseMetric extends IMetric {
	/**
	 * Accept a visitor
	 * @param visitor the visitor
	 */
	void accept(IBaseMetricVisitor visitor);
}
