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
import org.junit.Before;
import org.junit.Test;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the BasicSynthesizer.
 * @author muel_s8
 *
 */

public class BasicSynthesizerTest extends ATestCase {

	protected BasicSynthesizer synthesizer;
	
	@Before
	public void setUp() throws Exception {
		super.set();
		
		synthesizer = new BasicSynthesizer();
	}
	
	@Test
	public void testEvaluateCsp2WithoutModularization() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};

		Fault fault = createDFT("/resources/galileo/csp2.dft");
		
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
		
		Fault fault = createDFT("/resources/galileo/2csp2Shared.dft");

		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}

	@Test
	public void testEvaluateCsp2() throws IOException {
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
	@Test
	public void testEvaluateHECS11() throws IOException {
		Fault fault = createDFT("/resources/galileo/hecs_1_1_0_np.dft");
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
		Fault fault = createDFT("/resources/galileo/cm_simple.dft");
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
			6.297634506950505e-05,
			4.654260986536632e-04,
			0.0016928828332256056,  
			0.004302923685329981
		};
		Fault fault = createDFT("/resources/galileo/cm1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM2() throws IOException {
		final double[] EXPECTED = {
			3.791112982388163e-05,
			1.5198531228025698e-04,
			3.748693875510452e-04,
			7.984689349664446e-04
		};
		Fault fault = createDFT("/resources/galileo/cm2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM3() throws IOException {
		final double[] EXPECTED = {
			2.166214533082528e-07,
			2.166214533082528e-07,
			2.8658522870311545e-06,
			1.851515224177692e-05
		};
		Fault fault = createDFT("/resources/galileo/cm3.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	} 
	
	/*@Test
	public void testBCMSimpleOldWay() throws IOException {
		Fault rootCMSimple = createDFT("/resources/galileo/cm_simple.dft");
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCMSimple);
		
		System.out.println(ra.toDot());
		
		final int NUM_STATES = 3;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
	@Test
	public void testCM1OldWay() throws IOException {
		Fault rootCM1 = createDFT("/resources/galileo/cm1.dft");
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCM1);
		
		System.out.println(ra.toDot());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, NUM_MODULES);
	}
	
	@Test
	public void testCM2OldWay() throws IOException {
		Fault rootCM2 = createDFT("/resources/galileo/cm2.dft");
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCM2);
		
		System.out.println(ra.toDot());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, NUM_MODULES);
	}
	
	@Test
	public void testCM3OldWay() throws IOException {
		Fault rootCM3 = createDFT("/resources/galileo/cm3.dft");
		synthesizer.setModularizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(rootCM3);
		
		System.out.println(ra.toDot());
		
		final int NUM_MODULES = 8;
		assertEquals(NUM_MODULES, NUM_MODULES);
	}*/
	
}
