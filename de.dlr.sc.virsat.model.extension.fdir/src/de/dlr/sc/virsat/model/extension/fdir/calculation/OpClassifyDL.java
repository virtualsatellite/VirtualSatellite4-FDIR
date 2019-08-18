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
	
	public static final int DL_VERY_LIKELY = Integer.valueOf(CutSet.DETECTION_VeryLikely_VALUE);
	public static final int DL_LIKELY = Integer.valueOf(CutSet.DETECTION_Likely_VALUE);
	public static final int DL_UNLIKELY = Integer.valueOf(CutSet.DETECTION_Unlikely_VALUE);
	public static final int DL_EXTREMELY_UNLIKELY = Integer.valueOf(CutSet.DETECTION_ExtremelyUnlikely_VALUE);
	
	public static final int[] DL_LEVELS = new int[] { DL_VERY_LIKELY, DL_LIKELY, DL_UNLIKELY, DL_EXTREMELY_UNLIKELY};
	public static final Double[] DEFAULT_DL_THRESHOLDS = new Double[] { LIMIT_VERY_LIKELY, LIMIT_LIKELY, LIMIT_UNLIKELY, LIMIT_EXTREMELY_UNLIKELY };
	
	@Override
	public double apply(double[] inputs) {
		if (inputs.length != OpClassifyPL.PL_LEVELS.length + DL_LEVELS.length + 1) {
			return Double.NaN;
		}
		
		double steadyStateDetectability = inputs[0];
		
		for (int i = OpClassifyPL.PL_LEVELS.length + 1; i < inputs.length; ++i) {
			if (steadyStateDetectability >= inputs[i]) {
				int j = i - OpClassifyPL.PL_LEVELS.length;
				return DL_LEVELS[j - 1];
			}
		}
		
		return Double.NaN;
	}
	
	@Override
	public Map<AQuantityKind, Double> applyOnQuantityKinds(AAdvancedFunction advancedFunction, Map<AQuantityKind, Double>[] inputQuantityKinds) {
		return new HashMap<>();
	}
}
