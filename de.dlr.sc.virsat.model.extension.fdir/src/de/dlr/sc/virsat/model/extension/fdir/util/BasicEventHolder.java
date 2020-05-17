/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;

/**
 * This class provides simple access to basic data relevant for basic events
 * @author muel_s8
 *
 */
public class BasicEventHolder {
	Fault fault;
	double hotFailureRate;
	double coldFailureRate;
	double repairRate;
	
	/**
	 * Standard constructor
	 * @param basicEvent the basic event
	 */
	BasicEventHolder(BasicEvent basicEvent) {
		hotFailureRate = basicEvent.getHotFailureRateBean().isSet() 
				? basicEvent.getHotFailureRateBean().getValueToBaseUnit() : Double.NaN;
		coldFailureRate = basicEvent.getColdFailureRateBean().isSet() 
				? basicEvent.getColdFailureRateBean().getValueToBaseUnit() : Double.NaN;
		repairRate =  basicEvent.getRepairRateBean().isSet() 
				? basicEvent.getRepairRateBean().getValueToBaseUnit() : Double.NaN;
	}
}
