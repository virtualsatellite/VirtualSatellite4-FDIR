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

import java.util.List;

/**
 * A derived metric is computed from a set of other metrics.
 * @author muel_s8
 *
 */

public interface IDerivedMetric extends IMetric {
	/**
	 * Either empty if the metric cannot be derived from other metrics,
	 * or otherwise the list of metrics required to derive this metric
	 * @return the metrics this metric is derived from
	 */
	List<IMetric> getDerivedFrom();
	
	/**
	 * Accept a visitor
	 * @param visitor the visitor
	 */
	void accept(IDerivedMetricVisitor visitor);
}
