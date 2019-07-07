/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.po;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.MarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.explicit.ExplicitDFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.DFTEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.IFaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * This class tests the POND DFT Semantics
 * @author muel_s8
 *
 */

public class PONDDFTSemanticsTest extends ATestCase {

	private ExplicitDFT2MAConverter converter;
	private RecoveryAutomatonHelper raHelper;
	private FaultTreeHelper ftHelper;
	private IFaultTreeEvaluator ftEvaluator;
	
	@Before
	public void setup() {
		converter = new ExplicitDFT2MAConverter();
		converter.setSemantics(PONDDFTSemantics.createPONDDFTSemantics());
		
		raHelper = new RecoveryAutomatonHelper(concept);
		ftHelper = new FaultTreeHelper(concept);
		ftEvaluator = new DFTEvaluator(converter, new MarkovModelChecker(DELTA, TEST_EPSILON * TEST_EPSILON));
	}
	
	@Test
	public void testObsCsp2() throws IOException {		
		Fault root = (Fault) createBasicDFT("/resources/galileoObs/obsCsp2.dft");
		final double[] EXPECTED = {
			9.9e-05,
			0.0003921,
			0.0008735,
			0.0015375
		};
		
		// Hand create the recovery automaton
		// -> initial ------------- TLE : Claim(TLE, b) ----------> FAIL
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		raHelper.createStates(ra, 2);
		ra.setInitial(ra.getStates().get(0));
		
		FaultEventTransition transition = raHelper.createFaultEventTransition(ra, ra.getStates().get(0), ra.getStates().get(1));
		List<FaultTreeNode> allNodes = ftHelper.getAllNodes(root);
		
		ClaimAction ca = new ClaimAction(concept);
		ca.setSpareGate((SPARE) allNodes.stream().filter(node -> node instanceof SPARE && node.getName().equals("tle")).findFirst().get());
		ca.setClaimSpare(allNodes.stream().filter(node -> node instanceof Fault && node.getName().equals("b")).findFirst().get());
		
		raHelper.assignInputs(transition, allNodes.stream().filter(node -> node instanceof SPARE && node.getName().equals("tle")).findFirst().get());
		raHelper.assignAction(transition, ca);
		
		ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		ftEvaluator.evaluateFaultTree(root, Reliability.UNIT_RELIABILITY);
		assertIterationResultsEquals(ftEvaluator, EXPECTED);
	}
	
	@Test
	public void testObsCsp2Delayed() throws IOException {		
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsCsp2Delayed.dft");
		MarkovAutomaton<DFTState> ma = converter.convert(root);
		System.out.println(ma.toDot());
	}
	
	@Test
	public void testObsOr2Csp2() throws IOException {		
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2.dft");
		MarkovAutomaton<DFTState> ma = converter.convert(root);
		System.out.println(ma.toDot());
	}

	@Test
	public void testObsOr2Csp2Delayed() throws IOException {		
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2Delayed.dft");
		MarkovAutomaton<DFTState> ma = converter.convert(root);
		System.out.println(ma.toDot());
	}

	@Test
	public void testObsOr2Csp2BE() throws IOException {		
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2ObsBE.dft");
		MarkovAutomaton<DFTState> ma = converter.convert(root);
		System.out.println(ma.toDot());
	}
	
	@Test
	public void testObsOr2Csp2BEUnreliable() throws IOException {		
		FaultTreeNode root = createBasicDFT("/resources/galileoObs/obsOr2Csp2ObsBEUnreliable.dft");
		MarkovAutomaton<DFTState> ma = converter.convert(root);
		System.out.println(ma.toDot());
	}
}
