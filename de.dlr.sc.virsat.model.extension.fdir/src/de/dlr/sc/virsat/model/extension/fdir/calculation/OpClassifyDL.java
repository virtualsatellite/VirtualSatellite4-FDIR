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
import de.dlr.sc.virsat.model.extension.fdir.model.CutSet;
import de.dlr.sc.virsat.model.extension.fdir.model.FMECAEntry;

/**
 * Operation that classifies a quantitative failure rate in terms of
 * a qualitative probability level. 
 * @author muel_s8
 *
 */

public class OpClassifyDL extends AAdvancedFunctionOp {

	public static final double LIMIT_VERY_LIKELY = 0.75;
	public static final double LIMIT_LIKELY = 0.5;
	public static final double LIMIT_UNLIKELY = 0.25;
	public static final double LIMIT_EXTREMELY_UNLIKELY = 0;
	
	public static final Double[] DEFAULT_DL_THRESHOLDS = new Double[] { LIMIT_VERY_LIKELY, LIMIT_LIKELY, LIMIT_UNLIKELY, LIMIT_EXTREMELY_UNLIKELY };
	
	@Override
	public double apply(double[] inputs) {
		if (inputs.length != FMECAEntry.PL_LEVELS.length + CutSet.DL_LEVELS.length + 1) {
			return CutSet.DL_UNKNOWN;
		}
		
		double steadyStateDetectability = inputs[0];
		
		for (int i = FMECAEntry.PL_LEVELS.length + 1; i < inputs.length; ++i) {
			if (steadyStateDetectability >= inputs[i]) {
				int j = i - FMECAEntry.PL_LEVELS.length;
				return CutSet.DL_LEVELS[j - 1];
			}
		}
		
		return CutSet.DL_UNKNOWN;
	}
	
	@Override
	public Map<AQuantityKind, Double> applyOnQuantityKinds(AAdvancedFunction advancedFunction, Map<AQuantityKind, Double>[] inputQuantityKinds) {
		return new HashMap<>();
	}
}
