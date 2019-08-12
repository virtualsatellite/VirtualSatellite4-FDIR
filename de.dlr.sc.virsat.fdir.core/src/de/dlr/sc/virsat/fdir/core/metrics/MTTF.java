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

import java.util.Collections;
import java.util.List;

/**
 * Metric representing the Mean Time To Failure
 * @author sascha
 *
 */
public class MTTF implements IQuantitativeMetric {
	public static final MTTF MTTF = new MTTF();
	
	/**
	 * Hidden constructor
	 */
	private MTTF() {
		
	}
	
	@Override
	public void accept(IMetricVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public List<IMetric> getDerivedFrom() {
		return Collections.singletonList(Reliability.INF_RELIABILITY);
	}

	@Override
	public boolean isProbability() {
		return false;
	}
}
