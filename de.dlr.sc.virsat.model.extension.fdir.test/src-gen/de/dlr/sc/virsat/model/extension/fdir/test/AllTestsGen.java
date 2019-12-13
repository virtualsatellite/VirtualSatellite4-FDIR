/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.test;

// *****************************************************************
// * Import Statements
// *****************************************************************

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeTest;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimActionTest;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomatonTest;
import de.dlr.sc.virsat.model.extension.fdir.model.PANDTest;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITORTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEPTest;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEventTest;
import de.dlr.sc.virsat.model.extension.fdir.model.CriticalityMatrixTest;
import de.dlr.sc.virsat.model.extension.fdir.model.PDEPTest;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTETest;
import de.dlr.sc.virsat.model.extension.fdir.migrator.Migrator1v0Test;
import de.dlr.sc.virsat.model.extension.fdir.model.AvailabilityAnalysisTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransitionTest;
import de.dlr.sc.virsat.model.extension.fdir.model.MCSAnalysisTest;
import de.dlr.sc.virsat.model.extension.fdir.model.CutSetTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FreeActionTest;
import de.dlr.sc.virsat.model.extension.fdir.validator.StructuralElementInstanceValidatorTest;
import de.dlr.sc.virsat.model.extension.fdir.model.ORTest;
import de.dlr.sc.virsat.model.extension.fdir.migrator.Migrator1v1Test;
import de.dlr.sc.virsat.model.extension.fdir.model.PANDITest;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdgeTest;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEPTest;
import de.dlr.sc.virsat.model.extension.fdir.model.CriticalityVectorTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParametersTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FMECATest;
import de.dlr.sc.virsat.model.extension.fdir.model.ANDTest;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARETest;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAYTest;
import de.dlr.sc.virsat.model.extension.fdir.model.StateTest;
import de.dlr.sc.virsat.model.extension.fdir.model.DetectabilityAnalysisTest;
import de.dlr.sc.virsat.model.extension.fdir.model.SANDTest;
import de.dlr.sc.virsat.model.extension.fdir.model.PORITest;
import de.dlr.sc.virsat.model.extension.fdir.model.PORTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FMECAEntryTest;
import de.dlr.sc.virsat.model.extension.fdir.model.TimedTransitionTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeTest;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTest;
import de.dlr.sc.virsat.model.extension.fdir.model.ReliabilityAnalysisTest;

/**
 * 
 */
@RunWith(Suite.class)

@SuiteClasses({
	FaultTreeNodeTest.class,
	ANDTest.class,
	ORTest.class,
	VOTETest.class,
	PANDTest.class,
	SPARETest.class,
	PORTest.class,
	SANDTest.class,
	PORITest.class,
	PANDITest.class,
	MONITORTest.class,
	DELAYTest.class,
	FDEPTest.class,
	RDEPTest.class,
	PDEPTest.class,
	FaultTreeEdgeTest.class,
	FaultTreeTest.class,
	FaultTest.class,
	BasicEventTest.class,
	CutSetTest.class,
	CriticalityVectorTest.class,
	CriticalityMatrixTest.class,
	FDIRParametersTest.class,
	FMECATest.class,
	FMECAEntryTest.class,
	ReliabilityAnalysisTest.class,
	AvailabilityAnalysisTest.class,
	DetectabilityAnalysisTest.class,
	MCSAnalysisTest.class,
	RecoveryAutomatonTest.class,
	ClaimActionTest.class,
	FreeActionTest.class,
	StateTest.class,
	FaultEventTransitionTest.class,
	TimedTransitionTest.class,
	Migrator1v0Test.class,
	Migrator1v1Test.class,
	StructuralElementInstanceValidatorTest.class,
				})

/**
 * 
 * Test Collection
 *
 */
public class AllTestsGen {

	/**
	 * Constructor
	 */
	private AllTestsGen() {
	}
	
	/**
	 * Test Adapter
	 * @return Executable JUnit Tests
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}	
}
