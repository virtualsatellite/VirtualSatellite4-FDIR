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

import de.dlr.sc.virsat.model.extension.fdir.calculation.FDIRExpressionEvaluatorTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.FDIRExpressionExtensionTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.FDIRParametersGetterTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyDLTest;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyPLTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.DFT2GalileoDFTTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.GalileoDFT2DFTTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft.analysis.DFTSymmetryCheckerTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft.DFT2BasicDFTConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTStateTest;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemanticsTest;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.DFTEvaluatorTest;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.DFTMetricsComposerTest;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.StormEvaluatorTest;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.FaultTreeTrimmerTest;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.ModularizerTest;
import de.dlr.sc.virsat.model.extension.fdir.modularizer.ModuleTest;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferencesTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.ParallelComposerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.CleanMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.ComposedMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.FinalStateMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.OrthogonalPartitionRefinementMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.PartitionRefinementMinimizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.DelegateSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.POSynthesizerTest;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.Schedule2RAConverterTest;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolderTest;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelperTest;
import de.dlr.sc.virsat.model.extension.fdir.validator.FaultValidatorTest;
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
		DFTStateTest.class,
		DFTSymmetryCheckerTest.class,
		GalileoDFT2DFTTest.class,
		BasicSynthesizerTest.class,
		POSynthesizerTest.class,
		DelegateSynthesizerTest.class,
		ModularSynthesizerTest.class,
		Schedule2RAConverterTest.class,
		DFT2GalileoDFTTest.class,
		OrthogonalPartitionRefinementMinimizerTest.class,
		PartitionRefinementMinimizerTest.class,
		FinalStateMinimizerTest.class,
		CleanMinimizerTest.class,
		ComposedMinimizerTest.class,
		StormEvaluatorTest.class,
		DFTEvaluatorTest.class,
		DFTMetricsComposerTest.class,
		FaultTreePreferencesTest.class,
		FaultTreeHolderTest.class,
		PONDDFTSemanticsTest.class,
		ModularizerTest.class,
		ModuleTest.class,
		RecoveryAutomatonHelperTest.class,
		ParallelComposerTest.class,
		OpClassifyPLTest.class,
		OpClassifyDLTest.class,
		FDIRParametersGetterTest.class,
		FDIRExpressionEvaluatorTest.class,
		FDIRExpressionExtensionTest.class,
		FaultTreeTrimmerTest.class,
		FaultValidatorTest.class
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