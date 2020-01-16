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
	protected FaultTreeEvaluator ftEvaluator;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		synthesizer = new BasicSynthesizer();
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
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
	public void testEvaluateCsp2WithoutBEOptimization() throws IOException {
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		synthesizer.setBEOptimizationOn(false);
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
		
		final int NUM_STATES = 4;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM1() throws IOException {
		final double[] EXPECTED = {
			6.297634506950505e-05,
			4.633278718727809e-04,
			0.0016864867216674444,  
			0.004289188056220271
		};
		Fault fault = createDFT("/resources/galileo/cm1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
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
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	} 
	
	
	@Test
	public void testSynthesizeVGS1() throws IOException {
		Fault fault = createDFT("/resources/galileo/vgs1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testSynthesizeCsp2Repair1() throws IOException {
		final double[] EXPECTED = {
			9.867825542307891E-5,
			3.895175202883881E-4, 
			8.649262706560498E-4, 
			0.0015175732434922476
		};
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair1.dft");
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair2() throws IOException {
		final double[] EXPECTED = {
			9.867825542307891E-5,
			3.895175202883881E-4, 
			8.649262706560498E-4, 
			0.0015175732434922476
		};
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2.dft");
		synthesizer.setMinimizer(null);
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault), EXPECTED);
	} 
}
