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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.core.runtime.SubMonitor;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class tests the DelegateSynthesizer
 * @author muel_s8
 *
 */

public class DelegateSynthesizerTest extends ATestCase {
	
	@Test
	public void testChooseSynthesize() throws IOException {
		Fault fault = createDFT("/resources/galileo/failureMode.dft");
		Fault poFault = createDFT("/resources/galileoObs/obsCsp2.dft");
		
		DelegateSynthesizer synthesizer = new DelegateSynthesizer();
		assertTrue(synthesizer.chooseSynthesizer(new FaultTreeHolder(fault)) instanceof BasicSynthesizer);
		assertTrue(synthesizer.chooseSynthesizer(new FaultTreeHolder(poFault)) instanceof POSynthesizer);
	}
	
	@Test
	public void testSynthesize() throws IOException {
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		ISynthesizer mockSynthesizer = new ISynthesizer() {
			@Override
			public RecoveryAutomaton synthesize(SynthesisQuery synthesisQuery, SubMonitor subMonitor) {
				return ra;
			}

			@Override
			public SynthesisStatistics getStatistics() {
				return new SynthesisStatistics();
			}
		};
		
		DelegateSynthesizer synthesizer = new DelegateSynthesizer() {
			@Override
			public ISynthesizer chooseSynthesizer(FaultTreeHolder ftHolder) {
				return mockSynthesizer;
			}
		};
		
		Fault fault = createDFT("/resources/galileo/failureMode.dft");
		assertEquals(ra, synthesizer.synthesize(new SynthesisQuery(fault), null));
	}

}
