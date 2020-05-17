/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTStaticAnalysis;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.StateUpdate.StateUpdateResult;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.events.FaultEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * This class tests the POND DFT Semantics
 * @author muel_s8
 *
 */

public class PONDDFTSemanticsTest extends ATestCase {
	
	private PONDDFTSemantics semantics;
	
	@Before
	public void setup() {
		semantics = PONDDFTSemantics.createPONDDFTSemantics();
	}
	
	@Test
	public void testPropagateUpdateFailedObserver() throws IOException {
		Fault root = (Fault) createBasicDFT("/resources/galileoObs/obsCsp2Unreliable.dft");
		
		// Setup variables for failing BEs a or c
		FaultTreeHolder ftHolder = new FaultTreeHolder(root);
		
		DFTStaticAnalysis staticAnalysis = new DFTStaticAnalysis();
		staticAnalysis.perform(ftHolder);
		
		FaultTreeNode csp = ftHolder.getNodeByName("tle", SPARE.class);
		FaultTreeNode tle = ftHolder.getNodeByName("tle", Fault.class);
		Fault faultA = ftHolder.getNodeByName("a", Fault.class);
		BasicEvent beA = ftHolder.getNodeByName("a", BasicEvent.class);
		BasicEvent beC = ftHolder.getNodeByName("c", BasicEvent.class);
		
		FaultEvent failA = new FaultEvent(beA, false, ftHolder);
		FaultEvent failC = new FaultEvent(beC, false, ftHolder);
		
		PODFTState initial = new PODFTState(ftHolder);
		
		// Ensure that everything is initially correct
		assertFalse("a has not failed yet", initial.hasFaultTreeNodeFailed(faultA));
		assertFalse("csp has not failed yet", initial.hasFaultTreeNodeFailed(csp));
		assertFalse("tle has not failed yet", initial.hasFaultTreeNodeFailed(tle));
		assertFalse("a has not been marked as fail observed", initial.isNodeFailObserved(faultA));
		assertFalse("csp has not been marked as fail observed", initial.isNodeFailObserved(csp));
		assertFalse("tle has not been marked as fail observed", initial.isNodeFailObserved(tle));
		
		// First fail a while it can be observed		
		StateUpdate stateUpdate = new StateUpdate(initial, failA);
		StateUpdateResult stateUpdateResult = semantics.performUpdate(stateUpdate, staticAnalysis);
		PODFTState succ = (PODFTState) stateUpdateResult.getSuccs().get(0);
		
		assertTrue("Observed the failure of a", succ.isNodeFailObserved(faultA));
		assertTrue("Observed the failure of csp", succ.isNodeFailObserved(csp));
		assertTrue("Observed the failure of tle", succ.isNodeFailObserved(tle));
		
		// Now fail the observer first and then a
		stateUpdate = new StateUpdate(initial, failC);
		stateUpdateResult = semantics.performUpdate(stateUpdate, staticAnalysis);
		succ = (PODFTState) stateUpdateResult.getSuccs().get(0);
		
		assertFalse("Observed the failure of a", succ.isNodeFailObserved(faultA));
		assertFalse("Observed the failure of csp", succ.isNodeFailObserved(csp));
		assertFalse("Observed the failure of tle", succ.isNodeFailObserved(tle));
		
		stateUpdate = new StateUpdate(succ, failA);
		stateUpdateResult = semantics.performUpdate(stateUpdate, staticAnalysis);
		succ = (PODFTState) stateUpdateResult.getSuccs().get(0);
		
		// Since the observer is failed, we expect that we could not observe the failure of a
		// and any propagated node
		assertFalse("Observed the failure of a", succ.isNodeFailObserved(faultA));
		assertFalse("Observed the failure of csp", succ.isNodeFailObserved(csp));
		assertFalse("Observed the failure of tle", succ.isNodeFailObserved(tle));
	}
}
