/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Detectability;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToDetection;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the MA2BeliefMAConverter.
 * @author khan_ax
 *
 */

public class MA2BeliefMAConverterTest extends ATestCase {

	private FaultTreeEvaluator ftEvaluator;
	private MA2BeliefMAConverter ma2BeliefMAConverter;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ma2BeliefMAConverter = new MA2BeliefMAConverter(true, true);
	}
	
	protected DFT2MAConverter createDFT2MAConverter() {
		DFT2MAConverter dft2MaConverter = new DFT2MAConverter();
		dft2MaConverter.getStateSpaceGenerator().setSemantics(PONDDFTSemantics.createPONDDFTSemantics());
		return dft2MaConverter;
	}
	
	@Test
	public void testEvaluateObsOr2ObsBe2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2ObsBe2Delayed.dft");
		
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, null);
		
		// Build the actual belief ma
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) dft2maConverter.getMaBuilder().getInitialState(), null);
		
		final double EXPECTED_MTTD = 0.75;
		final double EXPECTED_STEADY_STATE_DETECTABILITY = 1;
		final int EXPECTED_COUNT_STATES = 8;
		final int EXPECTED_COUNT_TRANSITIONS = 9;
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, 
				Detectability.UNIT_DETECTABILITY, MeanTimeToDetection.MTTD, SteadyStateDetectability.SSD);
		
		assertEquals(EXPECTED_COUNT_STATES, beliefMa.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, beliefMa.getTransitions().size());
		assertEquals("MTTD has correct value", EXPECTED_MTTD, result.getMeanTimeToDetection(), TEST_EPSILON);
		final double TEST_EPSILON = 0.00001;
		assertEquals("Steady State Detectability has correct value", EXPECTED_STEADY_STATE_DETECTABILITY, result.getSteadyStateDetectability(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsCsp2.dft");
		
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, null);
		
		// Build the actual belief ma
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) dft2maConverter.getMaBuilder().getInitialState(), null);

		final int EXPECTED_COUNT_STATES = 7;
		final int EXPECTED_COUNT_TRANSITIONS = 6;
		final double EXPECTED_MTTF = 1.5;
		
		assertEquals(EXPECTED_COUNT_STATES, beliefMa.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, beliefMa.getTransitions().size());
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsCsp2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsCsp2Delayed.dft");
		
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, null);
		
		// Build the actual belief ma
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) dft2maConverter.getMaBuilder().getInitialState(), null);
		
		final int EXPECTED_COUNT_STATES = 13;
		final int EXPECTED_COUNT_TRANSITIONS = 15;
		final double EXPECTED_MTTF = 1;
		final double EXPECTED_MTTD = 1;
		
		assertEquals(EXPECTED_COUNT_STATES, beliefMa.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, beliefMa.getTransitions().size());
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(root, MeanTimeToFailure.MTTF, MeanTimeToDetection.MTTD);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals(EXPECTED_MTTD, result.getMeanTimeToDetection(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2BEDelayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2BEDelayed.dft");
		
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, null);
		
		// Build the actual belief ma
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) dft2maConverter.getMaBuilder().getInitialState(), null);
		
		final int EXPECTED_COUNT_STATES = 20;
		final int EXPECTED_COUNT_TRANSITIONS = 26;
		final double EXPECTED_MTTF = 0.333333333333;
		
		assertEquals(EXPECTED_COUNT_STATES, beliefMa.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, beliefMa.getTransitions().size());
		
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2.dft");
		
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, null);
		
		// Build the actual belief ma
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) dft2maConverter.getMaBuilder().getInitialState(), null);
		
		final int EXPECTED_COUNT_STATES = 16;
		final int EXPECTED_COUNT_TRANSITIONS = 15;
		final double EXPECTED_MTTF = 1.125;
		
		assertEquals(EXPECTED_COUNT_STATES, beliefMa.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, beliefMa.getTransitions().size());
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testSynthesizeObsOr2Csp2Delayed() throws IOException {
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2Delayed.dft");
		
		DFT2MAConverter dft2maConverter = createDFT2MAConverter();
		MarkovAutomaton<DFTState> ma = dft2maConverter.convert(root, null, null);
		
		final int EXPECTED_COUNT_STATES = 14;
		final int EXPECTED_COUNT_TRANSITIONS = 19;
		final double EXPECTED_MTTF = 0.5;
		
		// Build the actual belief ma
		MarkovAutomaton<BeliefState> beliefMa = ma2BeliefMAConverter.convert(ma, (PODFTState) dft2maConverter.getMaBuilder().getInitialState(), null);
				
		
		assertEquals(EXPECTED_COUNT_STATES, beliefMa.getStates().size());
		assertEquals(EXPECTED_COUNT_TRANSITIONS, beliefMa.getTransitions().size());
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(root).getMeanTimeToFailure(), TEST_EPSILON);
	}
}
