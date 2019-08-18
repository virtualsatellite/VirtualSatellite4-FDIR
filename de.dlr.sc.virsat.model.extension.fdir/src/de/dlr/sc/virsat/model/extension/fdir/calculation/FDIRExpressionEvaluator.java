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

import java.util.ArrayList;
import java.util.List;

import de.dlr.sc.virsat.model.calculation.compute.IExpressionEvaluator;
import de.dlr.sc.virsat.model.calculation.compute.IExpressionResult;
import de.dlr.sc.virsat.model.calculation.compute.extensions.NumberLiteralResult;
import de.dlr.sc.virsat.model.calculation.compute.extensions.NumberLiteralSetFunctionHelper;
import de.dlr.sc.virsat.model.dvlm.calculation.AAdvancedFunction;
import de.dlr.sc.virsat.model.dvlm.calculation.ALeftOpRightExpression;
import de.dlr.sc.virsat.model.dvlm.calculation.AOpRightExpression;

/**
 * This class connects the FDIR specific calculation functionality
 * @author muel_s8
 *
 */

public class FDIRExpressionEvaluator implements IExpressionEvaluator {
	
	@Override
	public IExpressionResult caseAAdvancedFunction(AAdvancedFunction advancedFunction, List<IExpressionResult> set) {
		if (advancedFunction.getOperator().equals("classifyPL")) {
			NumberLiteralResult failureRate = (NumberLiteralResult) set.get(0);
			ArrayResult thresholds = (ArrayResult) set.get(1);
			
			List<NumberLiteralResult> inputs = new ArrayList<>();
			inputs.add(failureRate);
			inputs.addAll(thresholds.getResults());
			
			NumberLiteralSetFunctionHelper setHelper = new NumberLiteralSetFunctionHelper(inputs);
			return setHelper.applySetOperator(advancedFunction);
		} else if (advancedFunction.getOperator().equals("classifyDL")) {
			NumberLiteralResult failureRate = (NumberLiteralResult) set.get(0);
			ArrayResult thresholds = (ArrayResult) set.get(1);
			
			List<NumberLiteralResult> inputs = new ArrayList<>();
			inputs.add(failureRate);
			inputs.addAll(thresholds.getResults());
			
			NumberLiteralSetFunctionHelper setHelper = new NumberLiteralSetFunctionHelper(inputs);
			return setHelper.applySetOperator(advancedFunction);
		}
		
		return null;
	}

	@Override
	public IExpressionResult caseALeftOpRightExpression(ALeftOpRightExpression object, IExpressionResult left, IExpressionResult right) {
		return null;
	}

	@Override
	public IExpressionResult caseAOpRightExpression(AOpRightExpression object, IExpressionResult right) {
		return null;
	}

}
