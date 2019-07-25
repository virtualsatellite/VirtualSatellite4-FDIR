/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2dft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.IFaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the conversion of a DFT to a BasicDFT
 * 
 * @author muel_s8
 *
 */

public class DFT2BasicDFTConverterTest extends ATestCase {

	/**
	 * Compares equivalence of two fault trees by evaluating their reliability curves
	 * 
	 * @param root1 The evaluator for the first root
	 * @param root2 The evaluator for the second root
	 */
	protected void assertIterationResultsEquals(FaultTreeNode root1, FaultTreeNode root2) {
		IFaultTreeEvaluator evaluator1 = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ModelCheckingResult result1 = evaluator1.evaluateFaultTree(root1);

		IFaultTreeEvaluator evaluator2 = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true, DELTA, TEST_EPSILON);
		ModelCheckingResult result2 = evaluator2.evaluateFaultTree(root2);
		
		final int TIMESTEPS = 5;
		for (int i = 0; i < TIMESTEPS; ++i) {
			double failRateOld = result1.getFailRates().get(i);
			double failRateNew = result2.getFailRates().get(i);
			assertEquals("Evaluation result is correct after time: " + ((i + 1) * DELTA), failRateOld, failRateNew, TEST_EPSILON);
		}
	}

	@Test
	public void testTransformOr2ToVote() throws IOException {
		Fault root = createDFT("/resources/galileo/or2.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode newRoot = converter.convert(root).getRoot();
		
		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(newRoot));
		
		// Recreate the tree without syntactic sugar
		Fault basicFaultTreeRoot = createDFT("/resources/galileo/1of2.dft");
		assertIterationResultsEquals(newRoot, basicFaultTreeRoot);
	}

	@Test
	public void testTransformOr4ToVote() throws IOException {
		Fault root = createDFT("/resources/galileo/or4.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();
		
		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		
		// Recreate the fault tree without syntactic sugar
		Fault basicFaultTreeRoot = createDFT("/resources/galileo/1of4.dft");
		assertIterationResultsEquals(convertedRoot, basicFaultTreeRoot);
	}

	@Test
	public void testTransformAnd2ToVote() throws IOException {
		Fault root = createDFT("/resources/galileo/and2.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode newRoot = converter.convert(root).getRoot();
		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(newRoot));
		
		Fault basicFaultTreeRoot = createDFT("/resources/galileo/2of2.dft");
		assertIterationResultsEquals(basicFaultTreeRoot, newRoot);
	}

	@Test
	public void testTransformAnd4ToVote() throws IOException {
		Fault root = createDFT("/resources/galileo/and4.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();

		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		
		// Recreate a tree without syntactic sugar
		Fault basicFaultTreeRoot = createDFT("/resources/galileo/4of4.dft");
		assertIterationResultsEquals(convertedRoot, basicFaultTreeRoot);
	}

	@Test
	public void testTransformSand2ToExclusivePor() throws IOException {
		Fault root = createDFT("/resources/galileo/sand2Fdep.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();
		
		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		
		// Recreate a fault tree without syntactic sugar
		Fault basicFaultTreeRoot = createDFT("/resources/galileo/sand2FdepBasic.dft");
		assertIterationResultsEquals(convertedRoot, basicFaultTreeRoot);
	}

	@Test
	public void testTransformSand4ToExclusivePor() throws IOException {
		Fault root = createDFT("/resources/galileo/sand4Fdep.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();

		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		
		// Recreate a fault tree without syntactic sugar

		Fault basicFaultTreeRootNode = createDFT("/resources/galileo/sand4FdepBasic.dft");
		assertIterationResultsEquals(convertedRoot, basicFaultTreeRootNode);
	}

	@Test
	public void testTransformPand2ToExclusivePor() throws IOException {
		Fault root = createDFT("/resources/galileo/pand2.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();

		// Recreate a fault tree without syntactic sugar

		Fault basicFaultTreeRoot = createDFT("/resources/galileo/pand2Basic.dft");
		
		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		assertIterationResultsEquals(basicFaultTreeRoot, convertedRoot);
	}

	@Test
	public void testTransformInclusivePor2ToExclusivePor() throws IOException {
		Fault root = createDFT("/resources/galileo/por2InclusiveFdep.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();

		// Recreate a fault tree without syntactic sugar

		Fault basicFaultTreeRoot = createDFT("/resources/galileo/por2InclusiveFdepBasic.dft");

		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		assertIterationResultsEquals(basicFaultTreeRoot, convertedRoot);
	}

	@Test
	public void testTransformInclusivePand2ToExclusivePand() throws IOException {
		Fault root = createDFT("/resources/galileo/pand2InclusiveFdep.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();

		// Recreate a fault tree without syntactic sugar

		Fault basicFaultTreeRoot = createDFT("/resources/galileo/pand2InclusiveFdepBasic.dft");

		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		assertIterationResultsEquals(basicFaultTreeRoot, convertedRoot);
	}

	@Test
	public void testTransformFDEPToFDEP() throws IOException {
		Fault root = createDFT("/resources/galileo/fdep1.dft");

		DFT2BasicDFTConverter converter = new DFT2BasicDFTConverter();
		FaultTreeNode convertedRoot = converter.convert(root).getRoot();

		assertFalse("The tree still contains syntactic sugar.", !ftHelper.checkSyntacticSugar(convertedRoot));
		assertIterationResultsEquals(root, convertedRoot);
	}
}
