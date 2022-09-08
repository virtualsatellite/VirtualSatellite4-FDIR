/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Checks the passed fault tree and delegates the synthesis call
 * to the fitting synthesizer.
 * @author muel_s8
 *
 */

public class DelegateSynthesizer implements ISynthesizer {

	protected ISynthesizer basicSynthesizer = new BasicSynthesizer();
	protected ISynthesizer poSynthesizer = new POSynthesizer();
	protected ISynthesizer delegate;

	@Override
	public RecoveryAutomaton synthesize(SynthesisQuery synthesisQuery, SubMonitor subMonitor) {
		delegate = chooseSynthesizer(synthesisQuery.getFTHolder());
		return delegate.synthesize(synthesisQuery, subMonitor);
	}
	
	/**
	 * Chooses the synthesizer based on the fault tree
	 * @param ftHolder the fault tree data
	 * @return the chosen synthesizer
	 */
	public ISynthesizer chooseSynthesizer(FaultTreeHolder ftHolder) {
		return ftHolder.isPartialObservable() ? poSynthesizer : basicSynthesizer;
	}
	
	@Override
	public SynthesisStatistics getStatistics() {
		return delegate.getStatistics();
	}
	
	public ISynthesizer getBasicSynthesizer() {
		return basicSynthesizer;
	}
	
	public ISynthesizer getPoSynthesizer() {
		return poSynthesizer;
	}
}
