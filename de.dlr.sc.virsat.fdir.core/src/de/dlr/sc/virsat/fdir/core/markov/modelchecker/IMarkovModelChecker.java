/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.markov.modelchecker;

import java.util.List;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetricVisitor;
/**
 * Interface for model checkers
 * @author yoge_re
 *
 */
public interface IMarkovModelChecker extends IMetricVisitor {
	/**
	 * 
	 * @return MTTF
	 */
	double getMeanTimeToFailure();
	/**
	 * 
	 * @return failrates
	 */
	List<Double> getFailRates();
	/**
	 * 
	 * @return point availability
	 */
	List<Double> getPointAvailability();
	/**
	 * 
	 * @return steadyStateAvailability
	 */
	double getSteadyStateAvailability();
	/**
	 * 
	 * @param mc markov chain
	 * @param metrics metrics
	 */
	void checkModel(MarkovAutomaton<? extends MarkovState> mc, IMetric... metrics);
}
