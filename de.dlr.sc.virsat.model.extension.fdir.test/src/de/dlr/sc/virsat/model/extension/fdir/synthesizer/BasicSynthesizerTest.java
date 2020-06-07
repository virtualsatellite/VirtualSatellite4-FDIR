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

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
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
	public void testEvaluateCsp1() throws IOException {
		final double EXPECTED_MTTF = 1;
		
		Fault fault = createDFT("/resources/galileo/csp1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 1;
		final int NUM_TRANSITIONS = 0;
		assertEquals(NUM_STATES, ra.getStates().size());
		assertEquals(NUM_TRANSITIONS, ra.getTransitions().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCsp2() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
	}
	
	@Test
	public void testEvaluateHECS11() throws IOException {
		Fault fault = createDFT("/resources/galileo/hecs_1_1_0_np.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);

		final int NUM_STATES = 2;
		assertEquals(NUM_STATES, ra.getStates().size());
	}
	
	@Test
	public void testEvaluateCMSimple1() throws IOException {
		final double EXPECTED_MTTF = 677.7777777777778;
		
		Fault fault = createDFT("/resources/galileo/cm_simple1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 3;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCMSimple2() throws IOException {
		final double[] EXPECTED = {
			0.0060088,
			0.0122455,
			0.0191832,
			0.0273547
		};
		Fault fault = createDFT("/resources/galileo/cm_simple2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertIterationResultsEquals(ftEvaluator.evaluateFaultTree(fault).getFailRates(), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM1() throws IOException {
		final double[] EXPECTED = {
			6.297637696713145E-5,
			4.654263248058321E-4,
			0.0016928835095991624,  
			0.004302925106080927

		};
		final double EXPECTED_MTTF = 0.28062467694389703;
		
		Fault fault = createDFT("/resources/galileo/cm1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 3;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertIterationResultsEquals(result.getFailRates(), EXPECTED);
	}
	
	@Test
	public void testEvaluateCM2() throws IOException {
		final double[] EXPECTED = {
			3.791152502763406E-5, 
			1.5199560899314957E-4, 
			3.749350399882001E-4, 
			7.987025488765953E-4
		};
		final double EXPECTED_MTTF = 0.32784729178994587;
		
		Fault fault = createDFT("/resources/galileo/cm2.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 5;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertIterationResultsEquals(result.getFailRates(), EXPECTED);

	}
	
	@Test
	public void testEvaluateCM3() throws IOException {
		final double[] EXPECTED = {
			2.166214533082528e-07,
			2.166214533082528e-07,
			2.8658522870311545e-06,
			1.851515224177692e-05
		};
		final double EXPECTED_MTTF = 0.36334134426339687;
		
		Fault fault = createDFT("/resources/galileo/cm3.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 9;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertIterationResultsEquals(result.getFailRates(), EXPECTED);
	} 
	
	
	@Test
	public void testSynthesizeVGS1() throws IOException {
		final double[] EXPECTED = {
			4.920028708951688E-8,
			9.840114835741478E-8,
			1.4760258380271557E-7,
			1.968045934244401E-7
		};
		final double EXPECTED_MTTF = 24365.03442441116;
		
		Fault fault = createDFT("/resources/galileo/vgs1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals(EXPECTED_MTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertIterationResultsEquals(result.getFailRates(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair1() throws IOException {
		final double[] EXPECTED = {
			0.9999016501226037,
			0.9996130705668653, 
			0.9991436794433161, 
			0.9985025233486501
		};
		final double EXPECTED_SSA = 0.5000005000000001;
		
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 1;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair2BadPrimary() throws IOException {
		final double[] EXPECTED = {
			0.9999019760517075, 
			0.9996156202699334, 
			0.999152094789556, 
			0.9985220320778355
		};
		final double EXPECTED_SSA = 0.7142864724603194;
		
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2BadPrimary.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 4;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeCsp2Repair2BadSpare() throws IOException {
		final double[] EXPECTED = {
			0.9999018132903527, 
			0.9996143485896306, 
			0.999147902777298, 
			0.9985123259997293
		};
		final double EXPECTED_SSA = 0.6470577570952566;
		
		Fault fault = createDFT("/resources/galileoRepair/csp2Repair2BadSpare.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 5;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}
	
	@Test
	public void testSynthesizeFdep1Csp2Repair1() throws IOException {
		final double[] EXPECTED = {
			0.9989127437420443,
			0.9976596933453932, 
			0.9962533670570343
		};
		final double EXPECTED_SSA = 0.6060612736223054;
		
		Fault fault = createDFT("/resources/galileoRepair/fdep1Csp2Repair1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 8;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Availability.UNIT_AVAILABILITY, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertEquals(EXPECTED_SSA, result.getSteadyStateAvailability(), TEST_EPSILON);
		assertIterationResultsEquals(result.getAvailability(), EXPECTED);
	}
	
	@Test
	public void testEvaluateCsp2Or2Exp1Prob1Exp1() throws IOException {
		Fault fault = createDFT("/resources/galileoUniform/csp2Or2Exp1Prob1Exp1.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		final int NUM_STATES = 2;
		assertEquals(NUM_STATES, ra.getStates().size());
		
		final double EXPECTED_MTTF = 0.4833333333333333;
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		assertEquals(EXPECTED_MTTF, ftEvaluator.evaluateFaultTree(fault).getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluatePand2ColdSpare1SharedWithRa() throws IOException {
		final double DELTA = 10;
		final double TIME = 4000;
		
		ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		
		Fault fault = createDFT("/resources/galileo/pand2ColdSpare1Shared.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		
		System.out.println(ra.toDot());
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		List<Double> reliability1 = ftEvaluator.evaluateFaultTree(fault, new Reliability(TIME)).getFailRates();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
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
		List<Double> reliability2 = ftEvaluator.evaluateFaultTree(fault, new Reliability(TIME)).getFailRates();
		
		for (int i = 0; i < reliability2.size(); ++i) {
			System.out.println(i + " " + reliability1.get(i) + " " + reliability2.get(i));
		}
	}
}
