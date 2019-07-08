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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.converter.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.Modularizer;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryAutomatonStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.test.TestActivator;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class BasicSynthesizerTest extends ATestCase {

	@Test
	public void testEvaluateCsp2() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/csp2.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}

	@Test
	public void testEvaluate2Csp2Shared() throws IOException {
		final double[] EXPECTED = {
			1.55e-05,
			0.0001194,
			0.0003891,
			0.0008909
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/2csp2Shared.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
	@Test
	public void testEvaluateHECS11() throws IOException {
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/hecs_1_1_0_np.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		Modularizer modularizer = new Modularizer();
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(modularizer);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		final int NUM_STATES = 2;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
	@Test
	public void testEvaluateCMSimple() throws IOException {
		final double[] EXPECTED = {
			0.0060088,
			0.0122455,
			0.0191832,
			0.0273548
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm_simple.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		Modularizer modularizer = new Modularizer();
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(modularizer);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		System.out.println(ra.toDot());
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
	
	@Test
	public void testEvaluateCM1() throws IOException {
		final double[] EXPECTED = {
			6.26785e-05,
			0.000463328,
			0.00168649,
			0.00428919
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm1.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		Modularizer modularizer = new Modularizer();
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(modularizer);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
	@Test
	public void testEvaluateCM2() throws IOException {
		final double[] EXPECTED = {
			3.79089e-05,
			0.000151929,
			0.000374514,
			0.00079721
		};

		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm2.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		Modularizer modularizer = new Modularizer();
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(modularizer);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
	@Test
	public void testEvaluateCM3() throws IOException {
		final double[] EXPECTED = {
			3.9285940392535636E-9,
			2.14672716459039E-7,
			2.84423746211353E-6,
			1.839713418160933E-5
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm3.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		Modularizer modularizer = new Modularizer();
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(modularizer);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
		ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
}
