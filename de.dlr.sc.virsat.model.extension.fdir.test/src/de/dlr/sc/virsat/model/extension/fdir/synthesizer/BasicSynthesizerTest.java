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

import org.junit.Before;
import org.junit.Test;
import de.dlr.sc.virsat.model.extension.fdir.converter.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.test.TestActivator;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class BasicSynthesizerTest extends ATestCase {

	@Before
	public void setUp() throws Exception {
		super.set();
	}
	
	@Test
	public void testEvaluateCsp2WithoutModularization() throws IOException {
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
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}

	@Test
	public void testEvaluate2Csp2SharedWithoutModularization() throws IOException {
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
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}

	@Test
	public void testEvaluateCsp2() throws IOException {
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/csp2.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
	@Test
	public void testEvaluateHECS11() throws IOException {
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/hecs_1_1_0_np.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
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
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 3;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM1() throws IOException {
		final double[] EXPECTED = {
			2.803584434531355e-04,
			0.001814592976612034,
			0.005429907093144522,
			0.011686562300949988
		};
		
		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm1.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM2() throws IOException {
		final double[] EXPECTED = {
			5.3201174491172875e-05,
			3.447393133310826e-04,
			0.0011910623555364203,
			0.002982714823326954
		};

		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm2.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM3() throws IOException {
		final double[] EXPECTED = {
			6.16556000812423e-08,
			2.4408204974576935e-06,
			2.1472266922680997e-05,
			9.927550244312611e-05
		};

		InputStream is = TestActivator.getResourceContentAsString("/resources/galileo/cm3.dft");
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, is);
		Fault fault = converter.convert();
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	} 
	
	/*@Test
	public void testBCMSimpleOldWay() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCMSimple);
		
		System.out.println(ra.toDot());
		
		final int NUM_STATES = 3;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
	@Test
	public void testCM1OldWay() throws IOException {
		Fault rootCM1 = createDFT("/resources/galileo/cm1.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCM1);
		
		System.out.println(ra.toDot());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, NUM_MODULES);
	}
	
	@Test
	public void testCM2OldWay() throws IOException {
		Fault rootCM2 = createDFT("/resources/galileo/cm2.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCM2);
		
		System.out.println(ra.toDot());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, NUM_MODULES);
	}
	
	@Test
	public void testCM3OldWay() throws IOException {
		Fault rootCM3 = createDFT("/resources/galileo/cm3.dft");
		
		BasicSynthesizer synthesizer = new BasicSynthesizer();
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCM3);
		
		System.out.println(ra.toDot());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, NUM_MODULES);
	}*/
	
}
