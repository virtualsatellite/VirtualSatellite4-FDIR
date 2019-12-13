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

public class OpClassifyPL extends AAdvancedFunctionOp {

	public static final int SECONDS_PER_HOUR = 3600;
	public static final double LIMIT_PROBABLE = 1E-1 / SECONDS_PER_HOUR;
	public static final double LIMIT_OCCASIONAL = 1E-3 / SECONDS_PER_HOUR;
	public static final double LIMIT_REMOTE = 1E-5 / SECONDS_PER_HOUR;
	public static final double LIMIT_EXTREMELY_REMOTE = 0;
	
	public static final Double[] DEFAULT_PL_THRESHOLDS = new Double[] { LIMIT_PROBABLE, LIMIT_OCCASIONAL, LIMIT_REMOTE, LIMIT_EXTREMELY_REMOTE };
	
	@Override
	public double apply(double[] inputs) {
		if (inputs.length != FMECAEntry.PL_LEVELS.length + CutSet.DL_LEVELS.length + 1) {
			return FMECAEntry.PL_UNKNOWN;
		}
		
		double failureRate = inputs[0];
		
		for (int i = 1; i < inputs.length - CutSet.DL_LEVELS.length; ++i) {
			if (failureRate >= inputs[i]) {
				return FMECAEntry.PL_LEVELS[i - 1];
			}
		}
		
		return FMECAEntry.PL_UNKNOWN;
	}
	
	@Override
	public Map<AQuantityKind, Double> applyOnQuantityKinds(AAdvancedFunction advancedFunction, Map<AQuantityKind, Double>[] inputQuantityKinds) {
		return new HashMap<>();
	}
}
