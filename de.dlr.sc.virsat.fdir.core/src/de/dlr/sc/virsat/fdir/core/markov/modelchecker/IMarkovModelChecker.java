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

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetricVisitor;
/**
 * Interface for model checkers
 * @author yoge_re
 *
 */
public interface IMarkovModelChecker extends IBaseMetricVisitor {

	/**
	 * Checks the markov chain model for the given metrics
	 * @param mc markov chain
	 * @param subMonitor eclipse ui element for progress reporting
	 * @param metrics metrics
	 * @return a model checking result with the contained metrics
	 */
	ModelCheckingResult checkModel(MarkovAutomaton<? extends MarkovState> mc, SubMonitor subMonitor, IBaseMetric... metrics);
	
	/**
	 * Gets the internal statistics for the last model checking call
	 * @return the model checking statistics of the last model checking call
	 */
	ModelCheckingStatistics getStatistics();
	
	/**
	 * Returns the internally configured time step
	 * @return the internal timestep
	 */
	double getDelta();
}
