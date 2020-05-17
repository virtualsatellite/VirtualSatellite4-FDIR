/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * Class for testing the DFT2MAConverter
 * @author muel_s8
 *
 */

public class DFT2MAConverterTest extends ATestCase {
	
	protected DFT2MAConverter dft2MaConverter;
	
	@Before
	public void setup() {
		dft2MaConverter = new DFT2MAConverter();
		dft2MaConverter.getStateSpaceGenerator().setSemantics(DFTSemantics.createStandardDFTSemantics());
	}
	
	@Test
	public void testEvaluateTransientOrPermanent() throws IOException {
		Fault fault = createDFT("/resources/galileoRepair/transientOrPermanent.dft");
		
		MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(fault);
		final int EXPECTED_COUNT_STATES = 3;
		assertEquals(EXPECTED_COUNT_STATES, ma.getStates().size());
	}
	
	@Test
	public void testEvaluateTransientToPermanentConversion() throws IOException {
		Fault fault = createDFT("/resources/galileoRepair/transientToPermanentConversion.dft");
		
		MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(fault);
		final int EXPECTED_COUNT_STATES = 3;
		assertEquals(EXPECTED_COUNT_STATES, ma.getStates().size());
	}
	
	@Test
	public void testEvaluateCsp2Repair2() throws IOException {
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2BadPrimary.dft");
		
		MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(fault);
		
		final int EXPECTED_COUNT_STATES = 4;
		final int EXPECTED_COUNT_TRANSITIONS = 7;
		assertEquals(EXPECTED_COUNT_STATES, ma.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, ma.getTransitions().size());
	}
}
