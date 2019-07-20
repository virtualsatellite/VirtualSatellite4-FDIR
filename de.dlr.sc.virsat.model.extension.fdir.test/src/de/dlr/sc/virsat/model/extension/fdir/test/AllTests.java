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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.dlr.sc.virsat.model.extension.fdir.calculation.FDIRParametersGetterTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.FDIRExpressionEvaluatorTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.FDIRExpressionExtensionTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyPLTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.DFT2GalileoDFTTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.GalileoDFT2DFTTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemanticsTest;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.StormEvaluatorTest;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.ModularizerTest;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferencesTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.ParallelComposerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.CleanMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.FinalStateMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.OrthogonalPartitionRefinementMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.PartitionRefinementMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.DelegateSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.POSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.Schedule2RAConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolderTest;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelperTest;
import junit.framework.JUnit4TestAdapter;

/**
 * 
 * @author muel_s8
 *
 */
@RunWith(Suite.class)

@SuiteClasses({ 
		DFT2BasicDFTConverterTest.class, 
		DFT2MAConverterTest.class, 
		GalileoDFT2DFTTest.class,
		BasicSynthesizerTest.class,
		POSynthesizerTest.class,
		DelegateSynthesizerTest.class,
		Schedule2RAConverterTest.class,
		DFT2GalileoDFTTest.class,
		OrthogonalPartitionRefinementMinimizerTest.class,
		PartitionRefinementMinimizerTest.class,
		FinalStateMinimizerTest.class,
		CleanMinimizerTest.class,
		ComposedMinimizerTest.class,
		StormEvaluatorTest.class,
		FaultTreePreferencesTest.class,
		FaultTreeHolderTest.class,
		PONDDFTSemanticsTest.class,
		ModularizerTest.class,
		RecoveryAutomatonHelperTest.class,
		ParallelComposerTest.class,
		OpClassifyPLTest.class,
		FDIRParametersGetterTest.class,
		FDIRExpressionEvaluatorTest.class,
		FDIRExpressionExtensionTest.class
		})

public class AllTests {

	/**
	 * Constructor for Test Suite
	 */
	private AllTests() {
	}

	/**
	 * entry point for test suite
	 * 
	 * @return the test suite
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}
}