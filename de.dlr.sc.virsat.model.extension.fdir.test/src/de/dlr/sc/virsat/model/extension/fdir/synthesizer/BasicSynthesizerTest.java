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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

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
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
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
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
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
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
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
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
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
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
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
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
	} 
	
	
	@Test
	public void testSynthesizeVGS1() throws IOException {
		Fault fault = createDFT("/resources/galileo/vgs1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testSynthesizeCsp2Repair1() throws IOException {
		final double[] EXPECTED = {
			0.9999016501226037,
			0.9996130705668653, 
			0.9991436794433161, 
			0.9985025233486501
		};
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY).getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair2BadPrimary() throws IOException {
		final double[] EXPECTED = {
			0.9999018128802991,
			0.9996143421328886, 
			0.9991478706086724, 
			0.9985122259438284
		};
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2BadPrimary.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		System.out.println(ra.toDot());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY).getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair2BadSpare() throws IOException {
		final double[] EXPECTED = {
			0.9999018128802991,
			0.9996143421328886, 
			0.9991478706086724, 
			0.9985122259438284
		};
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2BadSpare.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY).getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeFdep1Csp2Repair1() throws IOException {
		final double[] EXPECTED = {
			0.9989127437420443,
			0.9976596933453932, 
			0.9962533670570343
		};
		Fault fault = createDFT("/resources/galileoRepair/fdep1Csp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY).getAvailability(), EXPECTED);
	}
	
	@Test
	public void testEvaluatePand2ColdSpare1SharedWithRa() throws IOException {
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, 10, TEST_EPSILON);
		
		Fault fault = createDFT("/resources/galileo/pand2ColdSpare1Shared.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		System.out.println(ra.toDot());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		List<Double> reliability1 = ftEvaluator.evaluateFaultTree(fault, new Reliability(4000)).getFailRates();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		SPARE spareGate1 = ftHolder.getNodeByName("SPARE1", SPARE.class);
		SPARE spareGate2 = ftHolder.getNodeByName("SPARE2", SPARE.class);
		FaultTreeNode spare = ftHolder.getNodeByName("B", Fault.class);
		FaultTreeNode beA = ftHolder.getNodeByName("A", BasicEvent.class);
		FaultTreeNode beC = ftHolder.getNodeByName("A", BasicEvent.class);
		
		ra = new RecoveryAutomaton(concept);
		State s0 = raHelper.createSingleState(ra, 0);
		State s1 = raHelper.createSingleState(ra, 1);
		ra.setInitial(s0);
		
		FaultEventTransition t01 = raHelper.createFaultEventTransition(ra, s0, s1);
		raHelper.assignInputs(t01, beA);
		
		FaultEventTransition t11 = raHelper.createFaultEventTransition(ra, s1, s1);
		raHelper.assignInputs(t11, beC);
		ClaimAction ca = new ClaimAction(concept);
		ca.setSpareGate(spareGate2);
		ca.setClaimSpare(spare);
		raHelper.assignAction(t11, ca);
		
		System.out.println(ra.toDot());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		List<Double> reliability2 = ftEvaluator.evaluateFaultTree(fault, new Reliability(4000)).getFailRates();
		
		for (int i = 0; i < reliability2.size(); ++i) {
			System.out.println(i + " " + reliability1.get(i) + " " + reliability2.get(i));
		}
	}
}
