/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.ADFT2MAConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.IDFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;

/**
 * Tests the DFT Evaluator.
 * @author muel_s8
 *
 */

public class ExplicitDFT2MAConverterTest extends ADFT2MAConverterTest {
	
	@Override
	protected IDFT2MAConverter createDFT2MAConverter() {
		ExplicitDFT2MAConverter converter = new ExplicitDFT2MAConverter();
		converter.setSemantics(DFTSemantics.createStandardDFTSemantics());
		return converter;
	}
	
	@Test
	public void testEvaluateAnd2Repair1() throws IOException {
		final double[] EXPECTED = {
			3.1802e-5,
			1.2642e-4,
			2.8270e-4,
			4.9948e-4
		};
		final double EXPECTEDMTTF = 2.9435483;
		Fault fault = createDFT("/resources/galileoRepair/and2Repair1.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateTransientOrPermanent() throws IOException {
		Fault fault = createDFT("/resources/galileoRepair/transientOrPermanent.dft");
		
		ExplicitDFT2MAConverter dft2MaConverter = (ExplicitDFT2MAConverter) createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(fault);
		final int EXPECTED_COUNT_STATES = 3;
		assertEquals(EXPECTED_COUNT_STATES, ma.getStates().size());
	}
	
	@Test
	public void testEvaluateTransientToPermanentConversion() throws IOException {
		Fault fault = createDFT("/resources/galileoRepair/transientToPermanentConversion.dft");
		
		ExplicitDFT2MAConverter dft2MaConverter = (ExplicitDFT2MAConverter) createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(fault);
		final int EXPECTED_COUNT_STATES = 3;
		assertEquals(EXPECTED_COUNT_STATES, ma.getStates().size());
	}
	
	@Test
	public void testEvaluateCsp2Repair2() throws IOException {
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2.dft");
		
		ExplicitDFT2MAConverter dft2MaConverter = (ExplicitDFT2MAConverter) createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2MaConverter.convert(fault);
		final int EXPECTED_COUNT_STATES = 4;
		assertEquals(EXPECTED_COUNT_STATES, ma.getStates().size());
	}
}
