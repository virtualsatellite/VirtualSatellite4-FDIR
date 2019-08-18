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
import java.util.Map;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider.FailLabel;

/**
 * Metric representing the Mean Time To Failure
 * @author sascha
 *
 */
public class MTTF implements IQuantitativeMetric, IBaseMetric, IDerivedMetric {
	public static final MTTF MTTF = new MTTF();
	
	/**
	 * Hidden constructor
	 */
	private MTTF() {
		
	}
	
	@Override
	public void accept(IBaseMetricVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void accept(IDerivedMetricVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public Map<FailLabelProvider, Set<IMetric>> getDerivedFrom() {
		return Collections.singletonMap(new FailLabelProvider(FailLabel.FAILED), Collections.singleton(Reliability.INF_RELIABILITY));
	}
}
