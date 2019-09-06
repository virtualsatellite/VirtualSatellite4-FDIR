/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.model.calculation.compute.IExpressionResult;
import de.dlr.sc.virsat.model.calculation.compute.extensions.NumberLiteralResult;
import de.dlr.sc.virsat.model.calculation.compute.extensions.ValuePropertyGetter;
import de.dlr.sc.virsat.model.dvlm.calculation.CalculationFactory;
import de.dlr.sc.virsat.model.dvlm.calculation.SetFunction;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;
import de.dlr.sc.virsat.model.extension.fdir.model.FMECAEntry;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the FDIR expression evaluator
 * @author muel_s8
 *
 */

public class FDIRExpressionEvaluatorTest extends ATestCase {

	@Test
	public void testCaseAAdvancedFunction() {
		FDIRExpressionEvaluator evaluator = new FDIRExpressionEvaluator();
		
		SetFunction setFunction = CalculationFactory.eINSTANCE.createSetFunction();
		setFunction.setOperator("classifyPL");
		
		List<IExpressionResult> inputs = new ArrayList<>();
		
		FDIRParameters fdirParameters = new FDIRParameters(concept);
		fdirParameters.setMissionTime(1);
		fdirParameters.setDefaultProbablityThresholds();
		
		NumberLiteralResult nlr = (NumberLiteralResult) new ValuePropertyGetter().get(fdirParameters.getMissionTimeBean().getTypeInstance());
		ArrayResult ar = (ArrayResult) new FDIRParametersGetter().get(fdirParameters.getTypeInstance());
		
		inputs.add(nlr);
		inputs.add(ar);
		
		IExpressionResult classificationResult = evaluator.caseAAdvancedFunction(setFunction, inputs);
		assertTrue(classificationResult instanceof NumberLiteralResult);
		double plLevel = Double.valueOf(((NumberLiteralResult) classificationResult).getNumberLiteral().getValue());
		assertEquals(FMECAEntry.PL_PROBABLE, plLevel, TEST_EPSILON);
	}
}
