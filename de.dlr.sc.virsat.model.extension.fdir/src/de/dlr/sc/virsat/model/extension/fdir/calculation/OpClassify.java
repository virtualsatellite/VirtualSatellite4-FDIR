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

import java.util.HashMap;
import java.util.Map;

import de.dlr.sc.virsat.model.calculation.compute.AAdvancedFunctionOp;
import de.dlr.sc.virsat.model.dvlm.calculation.AAdvancedFunction;
import de.dlr.sc.virsat.model.dvlm.qudv.AQuantityKind;

/**
 * Operation that sums all the inputs
 * @author muel_s8
 *
 */

public class OpClassify extends AAdvancedFunctionOp {

	public static final double LIMIT_PROBABLE = 1E-1;
	public static final double LIMIT_OCCASIONAL = 1E-3;
	public static final double LIMIT_REMOTE = 1E-5;
	
	public static final int PROBABLE_VALUE = 4;
	public static final int OCCASIONAL_VALUE = 3;
	public static final int REMOTE_VALUE = 2;
	public static final int EXTREMELY_REMOTE_VALUE = 1;
	
	public static final int SECONDS_PER_HOUR = 3600;
	
	@Override
	public double apply(double[] inputs) {
		
		if (inputs.length > 1) {
			return Double.NaN;
		}
		
		double failureRate = inputs[0] * SECONDS_PER_HOUR;
		
		if (failureRate > LIMIT_PROBABLE) {
			return PROBABLE_VALUE;
		} else if (failureRate > LIMIT_OCCASIONAL) {
			return OCCASIONAL_VALUE;
		} else if (failureRate > LIMIT_REMOTE) {
			return REMOTE_VALUE;
		}  else {
			return EXTREMELY_REMOTE_VALUE;
		}
	}
	
	@Override
	public Map<AQuantityKind, Double> applyOnQuantityKinds(AAdvancedFunction advancedFunction, Map<AQuantityKind, Double>[] inputQuantityKinds) {
		return new HashMap<>();
	}
}
