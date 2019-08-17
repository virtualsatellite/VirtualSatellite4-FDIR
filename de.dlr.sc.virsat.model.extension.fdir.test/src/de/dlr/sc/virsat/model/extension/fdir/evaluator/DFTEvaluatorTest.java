/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.MarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.FaultTreeSymmetryChecker;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class tests the DFTEvaluator
 * @author muel_s8
 *
 */

public class DFTEvaluatorTest extends ATestCase {

	protected IFaultTreeEvaluator ftEvaluator;
	protected DFTEvaluator dftEvaluator;

	@Before
	public void setup() {
		dftEvaluator = new DFTEvaluator(DFTSemantics.createStandardDFTSemantics(), null, new MarkovModelChecker(DELTA, TEST_EPSILON));
		ftEvaluator = FaultTreeEvaluator.decorateFaultTreeEvaluator(dftEvaluator);
	}

	@Test
	public void testEvaluateFailureMode() throws IOException {
		final double[] EXPECTED = {
			0.0009995002,
			0.001998001,
			0.002995504,
			0.003992011,
			0.004987521
		};
		final double EXPECTEDMTTF = 10;
		final int EXPECTEDSTATES = 2;
		final int EXPECTEDTRANSITIONS = 1;
		
		Fault fault = createDFT("/resources/galileo/failureMode.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("Markov Chain has correct transition count", EXPECTEDTRANSITIONS, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxTransitions);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testEvaluateOr2() throws IOException {
		final double[] EXPECTED = {
			0.0010994,
			0.0021976,
			0.0032946,
			0.0043903
		};
		final double EXPECTEDMTTF = 9.09091;
		final int EXPECTEDSTATES = 2;
		final int EXPECTEDTRANSITIONS = 2;
		
		Fault fault = createDFT("/resources/galileo/or2.dft");
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		BasicEvent a = ftHolder.getNodeByName("A", BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B", BasicEvent.class);
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Reliability.UNIT_RELIABILITY, MTTF.MTTF, MinimumCutSet.MINCUTSET);
		
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("Markov Chain has correct transition count", EXPECTEDTRANSITIONS, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxTransitions);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		
		final int EXPECTED_COUNT_MINCUTS = 2;
		assertEquals("Number of MinCut sets correct", EXPECTED_COUNT_MINCUTS, result.getMinCutSets().size());
		assertThat("MinCut sets correct", result.getMinCutSets(), hasItem(Collections.singleton(a)));
		assertThat("MinCut sets correct", result.getMinCutSets(), hasItem(Collections.singleton(b)));
	}
	
	@Test
	public void testEvaluateAnd2() throws IOException {
		final double[] EXPECTED = {
			3.18e-05,
			0.0001265,
			0.0002829,
			0.0004999
		};
		final double EXPECTEDMTTF = 2.9166666;
		final int EXPECTEDSTATES = 4;
		final int EXPECTEDTRANSITIONS = 4;
		
		Fault fault = createDFT("/resources/galileo/and2.dft");
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		BasicEvent a = ftHolder.getNodeByName("A", BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B", BasicEvent.class);
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Reliability.UNIT_RELIABILITY, MTTF.MTTF, MinimumCutSet.MINCUTSET);
		
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("Markov Chain has correct transition count", EXPECTEDTRANSITIONS, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxTransitions);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		
		final int EXPECTED_COUNT_MINCUTS = 1;
		assertEquals("Number of MinCut sets correct", EXPECTED_COUNT_MINCUTS, result.getMinCutSets().size());
		assertThat("MinCut sets correct", result.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList(a, b))));
	}
	
	@Test
	public void testEvaluateOr2And() throws IOException {
		final double[] EXPECTED = {
			2.38e-05,
			9.47e-05,
			0.0002115,
			0.0003734
		};
		final double EXPECTEDMTTF = 5.119047;
		Fault fault = createDFT("/resources/galileo/or2and.dft");
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(fault);
		BasicEvent a = ftHolder.getNodeByName("A", BasicEvent.class);
		BasicEvent b = ftHolder.getNodeByName("B", BasicEvent.class);
		BasicEvent c = ftHolder.getNodeByName("C", BasicEvent.class);
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Reliability.UNIT_RELIABILITY, MTTF.MTTF, MinimumCutSet.MINCUTSET);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		
		final int EXPECTED_COUNT_MINCUTS = 2;
		assertEquals("Number of MinCut sets correct", EXPECTED_COUNT_MINCUTS, result.getMinCutSets().size());
		assertThat("MinCut sets correct", result.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList(a, c))));
		assertThat("MinCut sets correct", result.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList(b, c))));
	}
	
	@Test
	public void testEvaluateAnd2Or() throws IOException {
		final double[] EXPECTED = {
			0.0239907,
			0.0479276,
			0.0717616,
			0.0954485,
			0.118949,
			0.142226
		};
		final double EXPECTEDMTTF = 0.315681444;
		Fault fault = createDFT("/resources/galileo/and2or.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd3() throws IOException {
		final double[] EXPECTED = {
			6.7e-06,
			5.22e-05,
			0.0001711,
			0.0003942,
			0.0007483,
			0.0012567
		};
		final double EXPECTEDMTTF = 0.9874231;
		Fault fault = createDFT("/resources/galileo/and3.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd3Symmetric() throws IOException {
		final double EXPECTEDMTTF = 3.666666666;
		final int EXPECTEDSTATES = 4;
		final int EXPECTEDTRANSITIONS = 3;
		
		Fault fault = createDFT("/resources/galileo/and3Symmetric.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("Markov Chain has correct transition count", EXPECTEDTRANSITIONS, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxTransitions);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd4() throws IOException {
		final double[] EXPECTED = {
			3e-07,
			3.9e-06,
			1.89e-05,
			5.69e-05,
			0.0001326,
			0.0002622
		};
		final double EXPECTEDMTTF = 1.011779;
		Fault fault = createDFT("/resources/galileo/and4.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd3Or() throws IOException {
		final double[] EXPECTED = {
			0.0029994,
			0.0060126,
			0.00906,
			0.01216
		};
		final double EXPECTEDMTTF = 0.92780513;
		Fault fault = createDFT("/resources/galileo/and3or.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2OrAnd2() throws IOException {
		final double[] EXPECTED = {
			0.0003248,
			0.0012786,
			0.0028313,
			0.0049536
		};
		final double EXPECTEDMTTF = 0.7770698;
		Fault fault = createDFT("/resources/galileo/and2OrAnd2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2OrAnd2Symmetric() throws IOException {
		final double EXPECTEDMTTF = 1.1388888;
		final int EXPECTEDSTATES = 8;
		final int EXPECTEDTRANSITIONS = 14;
		
		Fault fault = createDFT("/resources/galileo/and2OrAnd2Symmetric.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("Markov Chain has correct transition count", EXPECTEDTRANSITIONS, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxTransitions);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2OrAnd2OrAnd2Symmetric() throws IOException {
		final double EXPECTEDMTTF = 0.822011322;
		final int EXPECTEDSTATES = 20;
		Fault fault = createDFT("/resources/galileo/and2OrAnd2OrAnd2Symmetric.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2AndOr2() throws IOException {
		final double[] EXPECTED = {
			0.0005635,
			0.0021976,
			0.0048215,
			0.0083593
		};
		final double EXPECTEDMTTF = 0.686274;
		Fault fault = createDFT("/resources/galileo/or2AndOr2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2OrAnd2Shared() throws IOException {
		final double[] EXPECTED = {
			0.0004999,
			0.0019524,
			0.0042899,
			0.0074485
		};
		final double EXPECTEDMTTF = 0.7291666;
		Fault fault = createDFT("/resources/galileo/and2OrAnd2Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2OrAnd2SharedSymmetric() throws IOException {
		final double EXPECTEDMTTF = 3.888888888;
		final int EXPECTEDSTATES = 5;
		final int EXPECTEDTRANSITIONS = 6;
		
		Fault fault = createDFT("/resources/galileo/and2OrAnd2SharedSymmetric.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("Markov Chain has correct transition count", EXPECTEDTRANSITIONS, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxTransitions);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateVote2Of3() throws IOException {
		final double[] EXPECTED = {
			0.000744,
			0.002898,
			0.0063507,
			0.0109973
		};
		final double EXPECTEDMTTF = 0.5232843;
		Fault fault = createDFT("/resources/galileo/vote2Of3.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluatePand2() throws IOException {
		final double[] EXPECTED = {
			0.0001416,
			0.0005572,
			0.0012331,
			0.0021562
		};
		final double EXPECTEDMTTF = Double.POSITIVE_INFINITY;
		Fault fault = createDFT("/resources/galileo/pand2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluatePand3() throws IOException {
		final double[] EXPECTED = {
			7e-07,
			5.5e-06,
			1.81e-05,
			4.19e-05
		};
		final double EXPECTEDMTTF = Double.POSITIVE_INFINITY;
		Fault fault = createDFT("/resources/galileo/pand3.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluatePand4() throws IOException {
		final double[] EXPECTED = {
			1e-07,
			1.7e-06,
			6.14e-06,
			1.81e-05
		};
		final double EXPECTEDMTTF = Double.POSITIVE_INFINITY;
		Fault fault = createDFT("/resources/galileo/pand4.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluatePor2() throws IOException {
		final double[] EXPECTED = {
			0.0176974,
			0.0348032,
			0.0513372,
			0.0673186
		};
		final double EXPECTEDMTTF = Double.POSITIVE_INFINITY;
		Fault fault = createDFT("/resources/galileo/por2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateColdSpare2() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		final double EXPECTEDMTTF = 1.5;
		
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	
	@Test
	public void testEvaluateColdSpare2WithRa() throws IOException {
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		final double EXPECTEDMTTF = 1.5;
		
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		State s = raHelper.createSingleState(ra, 0);
		ra.setInitial(s);
		FaultEventTransition t = raHelper.createFaultEventTransition(ra, s, s);
		SPARE spareGate = (SPARE) ftHelper.getChildren(fault).get(0);
		Fault faultA = (Fault) ftHelper.getChildren(spareGate).get(0);
		BasicEvent be = faultA.getBasicEvents().get(0);
		raHelper.assignInputs(t, be);
		ClaimAction ca = new ClaimAction(concept);
		ca.setSpareGate(spareGate);
		ca.setClaimSpare(ftHelper.getSpares(spareGate).get(0));
		raHelper.assignAction(t, ca);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateColdSpare2WithTimedRa() throws IOException {
		ftEvaluator = FaultTreeEvaluator.decorateFaultTreeEvaluator(new DFTEvaluator(DFTSemantics.createNDDFTSemantics(), null, new MarkovModelChecker(DELTA, TEST_EPSILON * TEST_EPSILON)));
		
		final double[] EXPECTED = {
			0.0099009,
			0.0196078,
			0.0291264,
			0.0384621
		};
		final double EXPECTEDMTTF = 1.25;
		
		Fault fault = createDFT("/resources/galileo/csp2.dft");
		
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		State s0 = raHelper.createSingleState(ra, 0);
		ra.setInitial(s0);
		State s1 = raHelper.createSingleState(ra, 1);
		
		raHelper.createTimedTransition(ra, s0, s1, 1);
		
		FaultEventTransition t1 = raHelper.createFaultEventTransition(ra, s1, s1);
		SPARE spareGate = (SPARE) ftHelper.getChildren(fault).get(0);
		Fault faultA = (Fault) ftHelper.getChildren(spareGate).get(0);
		BasicEvent be = faultA.getBasicEvents().get(0);
		raHelper.assignInputs(t1, be);
		ClaimAction ca = new ClaimAction(concept);
		ca.setSpareGate(spareGate);
		ca.setClaimSpare(ftHelper.getSpares(spareGate).get(0));
		raHelper.assignAction(t1, ca);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertIterationResultsEquals(result, EXPECTED);
	}
	
	@Test
	public void testEvaluateColdSpare3() throws IOException {
		final double[] EXPECTED = {
			1.03e-05,
			7.96e-05,
			0.0002595,
			0.0005943
		};
		final double EXPECTEDMTTF = 0.875;
		Fault fault = createDFT("/resources/galileo/csp3.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateColdSpare4() throws IOException {
		final double[] EXPECTED = {
			6.1e-06,
			8.63e-05,
			0.0003894,
			0.0010994
		};
		final double EXPECTEDMTTF = 0.46875;
		Fault fault = createDFT("/resources/galileo/csp4.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2ColdSpare2Basic() throws IOException {
		final double[] EXPECTED = {
			0.0103384,
			0.0213084,
			0.0328456,
			0.0448899
		};
		final double EXPECTEDMTTF = 0.46666666;
		Fault fault = createDFT("/resources/galileo/or2ColdSpare2Basic.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2ColdSpare2() throws IOException {
		final double[] EXPECTED = {
			0.0006851,
			0.0026821,
			0.0059056,
			0.0102736
		};
		final double EXPECTEDMTTF = 0.5;
		Fault fault = createDFT("/resources/galileo/or2ColdSpare2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testEvaluateAnd2ColdSpare2() throws IOException {
		final double[] EXPECTED = {
			1.35e-05,
			0.0001888,
			0.0008394,
			0.0023336
		};
		final double EXPECTEDMTTF = 0.39583333;
		Fault fault = createDFT("/resources/galileo/and2ColdSpare2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2ColdSpare1Shared() throws IOException {
		final double[] EXPECTED = {
			0.0006823,
			0.0026602,
			0.0058349,
			0.0101132
		};
		final double EXPECTEDMTTF = 0.55;
		Fault fault = createDFT("/resources/galileo/or2ColdSpare1Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2ColdSpare2Shared() throws IOException {
		final double[] EXPECTED = {
			1.29e-05,
			9.97e-05,
			0.0003253,
			0.0007455
		};
		final double EXPECTEDMTTF = 0.730952;
		
		Fault fault = createDFT("/resources/galileo/or2ColdSpare2Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2ColdSpare3Shared() throws IOException {
		final double[] EXPECTED = {
			3e-07,
			4e-06,
			1.96e-05,
			5.93e-05
		};
		final double EXPECTEDMTTF = 0.86422902;
		Fault fault = createDFT("/resources/galileo/or2ColdSpare3Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2ColdSpare1Shared() throws IOException {
		final double[] EXPECTED = {
			3.9e-06,
			3.04e-05,
			0.0001002,
			0.0002318
		};
		final double EXPECTEDMTTF = 1.283333;
		Fault fault = createDFT("/resources/galileo/and2ColdSpare1Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd2ColdSpare3Shared() throws IOException {
		final double[] EXPECTED = {
			0.0001034,
			0.0021668,
			0.0109243,
			0.0309722
		};
		final double EXPECTEDMTTF = 0.14191;
		
		Fault fault = createDFT("/resources/galileo/and2ColdSpare3Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd3ColdSpare3Shared() throws IOException {
		final double[] EXPECTED = {
			7.78e-05,
			0.0024752,
			0.0144985,
			0.0433001
		};
		final double EXPECTEDMTTF = 0.132007;
		Fault fault = createDFT("/resources/galileo/and3ColdSpare3Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateAnd4ColdSpare3Shared() throws IOException {
		final double[] EXPECTED = {
			5.23e-05,
			0.0024342,
			0.0161565,
			0.0500889
		};
		final double EXPECTEDMTTF = 0.1291801;
		Fault fault = createDFT("/resources/galileo/and4ColdSpare3Shared.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateOr2Pand2ColdSpare2() throws IOException {
		final double[] EXPECTED = {
			0.0003924,
			0.00154,
			0.0033996,
			0.00593
		};
		final double EXPECTEDMTTF = 0.722222;
		
		Fault fault = createDFT("/resources/galileo/or2Pand2ColdSpare2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateColdSpare2ColdSpare2() throws IOException {
		final double[] EXPECTED = {
			9.802150100738359E-9,
			1.537365589925142E-7,
			7.629437043180311E-7,
			2.3638081031360547E-6
		};
		final double EXPECTEDMTTF = 2.083333333;
		Fault fault = createDFT("/resources/galileo/coldSpare2ColdSpare2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateWarmSpare2() throws IOException {
		final double[] EXPECTED = {
			0.0001482,
			0.0005855,
			0.0013015,
			0.0022859
		};
		final double EXPECTEDMTTF = 1.25;
		Fault fault = createDFT("/resources/galileo/wsp2.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateWarmSpare3() throws IOException {
		final double[] EXPECTED = {
			2.9e-06,
			2.3e-05,
			7.59e-05,
			0.0001761
		};
		final double EXPECTEDMTTF = 1.342517;
		Fault fault = createDFT("/resources/galileo/wsp3.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateFDEP1() throws IOException {
		final double[] EXPECTED = {
			0.0009995,
			0.001998,
			0.0029955,
			0.003992,
			0.0049875
		};
		final double EXPECTEDMTTF = 10;
		
		Fault fault = createDFT("/resources/galileo/fdep1.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateRDEP1() throws IOException {
		final double[] EXPECTED = {
			0.009955,
			0.019821,
			0.029597,
			0.039286,
			0.048887
		};
		final double EXPECTEDMTTF = 0.9545454;
		
		Fault fault = createDFT("/resources/galileo/rdep1.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateDelay1() throws IOException {
		final double EXPECTEDMTTF = 11;
		Fault fault = createDFT("/resources/galileo/delay1.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCMSimple() throws IOException {
		final double[] EXPECTED = {
			0.0060088,
			0.0122455,
			0.0191832,
			0.0273548
		};
		final double EXPECTEDMTTF = 0.25627188;
		Fault fault = createDFT("/resources/galileo/cm_simple.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}

	@Test
	public void testEvaluateAnd2And2Symmetric() throws IOException {
		final double EXPECTEDMTTF = 0.26041666666;
		final int EXPECTEDSTATES = 9;
		
		Fault fault = createDFT("/resources/galileo/and2and2Symmetric.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertEquals("Markov Chain has correct state size", EXPECTEDSTATES, dftEvaluator.getStatistics().stateSpaceGenerationStatistics.maxStates);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCM1() throws IOException {
		final double[] EXPECTED = {
			6.26785e-05,
			0.000463328,
			0.00168649,
			0.00428919
		};
		final double EXPECTEDMTTF = 0.2807429;
		Fault fault = createDFT("/resources/galileo/cm1.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCM2() throws IOException {
		final double[] EXPECTED = {
			3.79089e-05,
			0.000151929,
			0.000374514,
			0.00079721
		};
		final double EXPECTEDMTTF = 0.32798024839145234;
		
		Fault fault = createDFT("/resources/galileo/cm2.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCM3() throws IOException {
		final double[] EXPECTED = {
			3.9285940392535636E-9,
			2.14672716459039E-7,
			2.84423746211353E-6,
			1.839713418160933E-5
		};
		final double EXPECTEDMTTF = 0.36348663707847634;
		Fault fault = createDFT("/resources/galileo/cm3.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCM4() throws IOException {
		final double[] EXPECTED = {
			3.581002068280633E-5,
			1.4251151037843218E-4,
			3.202047837711682E-4,
			5.73300404984077E-4
		};
		final double EXPECTEDMTTF = 0.38349296618324935;
		Fault fault = createDFT("/resources/galileo/cm4.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateCM4WithDFTSymmetry() throws IOException {
		final double[] EXPECTED = {
			3.581002068280633E-5,
			1.4251151037843218E-4,
			3.202047837711682E-4,
			5.73300404984077E-4
		};
		final double EXPECTEDMTTF = 0.38349296618324985;
		Fault fault = createDFT("/resources/galileo/cm4.dft");
		dftEvaluator.setSymmetryChecker(new FaultTreeSymmetryChecker());
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	@Test
	public void testEvaluateRC1() throws IOException {
		final double[] EXPECTED = {
			6.018566771498309E-4,
			0.001207413434399935,
			0.001816650399162764,
			0.002429547750480854
		};
		final double EXPECTEDMTTF = 6.38093090;
		Fault fault = createDFT("/resources/galileo/rc1.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	
	/*
	@Test
	public void testEvaluateVGS1() throws IOException {
		final double[] EXPECTED = {
			9.200036653791825E-9,
			1.8400146615614586E-8,
			2.760032988613919E-8,
			3.6800586466036515E-8
		};
		
		Fault fault = createDFT("/resources/galileo/vgs1.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
		final double EXPECTEDMTTF = 39595.24895;
		final double TEST_EPSILON_SMALL = 1e-08;
		assertIterationResultsEquals(result, EXPECTED, TEST_EPSILON_SMALL);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
	}
	*/
	
	@Test
	public void testFTPP4() throws IOException {
		final double EXPECTEDMTTF = 4595.0333844;
		Fault fault = createDFT("/resources/galileo/ftpp4.dft");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
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
		final double EXPECTEDSTEADYSTATE = 0.09113563429425975;
		
		Fault fault = createDFT("/resources/galileoRepair/and2Repair1.dft");
		
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, Reliability.UNIT_RELIABILITY, MTTF.MTTF, 
				Availability.UNIT_AVAILABILITY, SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		assertIterationResultsEquals(result, EXPECTED);
		assertEquals("MTTF has correct value", EXPECTEDMTTF, result.getMeanTimeToFailure(), TEST_EPSILON);
		assertEquals("Steady State Availability has correct value", EXPECTEDSTEADYSTATE, result.getSteadyStateAvailability(), TEST_EPSILON);
	}
}
