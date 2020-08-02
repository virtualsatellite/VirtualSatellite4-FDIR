/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.core.util;

import java.util.List;

public interface IStatistics {
	
	int TIMEOUT = -1;
	int NA = -2;
	int OOM = -3;
	
	/**
	 * Gets a print value for a statistics entry.
	 * @param value the value to print
	 * @return a string representation
	 */
	default String getPrintValue(long value) {
		if (value == NA) {
			return "N/A";
		} else if (value == TIMEOUT) {
			return "Timeout";
		} else if (value == OOM) {
			return "OOM";
		} else {
			return String.valueOf(value);
		}
	}
	
	/**
	 * Adds two statistics values while considering special states such as NA
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return the composed addition
	 */
	default long composeAdd(long value1, long value2) {
		if (value1 == NA) {
			return value2;
		} else if (value2 == NA) {
			return value1;
		}
		
		return value1 + value2;
	}
	
	/**
	 * Adds two statistics values while considering special states such as NA
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return the composed addition
	 */
	default int composeAdd(int value1, int value2) {
		if (value1 == NA) {
			return value2;
		} else if (value2 == NA) {
			return value1;
		}
		
		return value1 + value2;
	}
	
	/**
	 * Get the formatted values of the statistic as a list
	 * @return the values in a list
	 */
	List<String> getValues();
}
