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

import java.util.List;

import de.dlr.sc.virsat.model.calculation.compute.IExpressionResult;
import de.dlr.sc.virsat.model.calculation.compute.extensions.NumberLiteralResult;

/**
 * This class wraps an array for the calculation engine
 * @author muel_s8
 *
 */

public class ArrayResult implements IExpressionResult {

	private List<NumberLiteralResult> results;
	
	/**
	 * Standard constructor
	 * @param results the results
	 */
	public ArrayResult(List<NumberLiteralResult> results) {
		this.results = results;
	}
	
	@Override
	public boolean equals(IExpressionResult obj, double eps) {
		if (obj instanceof ArrayResult) {
			ArrayResult other = (ArrayResult) obj;
			
			if (results.size() != other.results.size()) {
				return false;
			}
			
			for (int i = 0; i < results.size(); ++i) {
				if (!results.get(i).equals(other.results.get(i))) {
					return false;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the wrapped results
	 * @return the results
	 */
	public List<NumberLiteralResult> getResults() {
		return results;
	}
	
	@Override
	public String toString() {
		return results.toString();
	}

}
