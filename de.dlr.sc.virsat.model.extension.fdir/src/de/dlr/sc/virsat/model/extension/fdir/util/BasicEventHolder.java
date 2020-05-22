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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.RepairAction;

/**
 * This class provides simple access to basic data relevant for basic events
 * @author muel_s8
 *
 */
public class BasicEventHolder {
	private Fault fault;
	private double hotFailureRate;
	private double coldFailureRate;
	private Map<List<FaultTreeNode>, Double> repairRates;
	
	/**
	 * Standard constructor
	 * @param basicEvent the basic event
	 */
	public BasicEventHolder(BasicEvent basicEvent) {
		repairRates = new HashMap<>();
		fault = basicEvent.getFault();
		hotFailureRate = getRateValue(basicEvent.getHotFailureRateBean());
		coldFailureRate = getRateValue(basicEvent.getColdFailureRateBean());
		
		double transientRepairRate = getRateValue(basicEvent.getRepairRateBean());
		repairRates.put(Collections.emptyList(), transientRepairRate);
				
		for (RepairAction repairAction : basicEvent.getRepairActions()) {
			List<FaultTreeNode> observations = new ArrayList<>(repairAction.getObservations());
			double repairRate = getRateValue(repairAction.getRepairRateBean());
			repairRates.put(observations, repairRate + transientRepairRate);
		}
	}
	
	public static boolean isRateDefined(double rate) {
		return Double.isFinite(rate) && rate > 0;
	}
	
	public static double getRateValue(BeanPropertyFloat rateBean) {
		return rateBean.isSet() ? rateBean.getValueToBaseUnit() : Double.NaN;
	}
	
	public boolean isFailureDefined() {
		return isRateDefined(hotFailureRate) || isRateDefined(coldFailureRate);
	}
	
	public boolean isRepairDefined() {
		for (double repairRate : repairRates.values()) {
			if (isRateDefined(repairRate)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Fault getFault() {
		return fault;
	}
	
	public double getHotFailureRate() {
		return hotFailureRate;
	}
	
	public double getColdFailureRate() {
		return coldFailureRate;
	}
	
	public double getRepairRate() {
		return repairRates.get(Collections.emptyList());
	}
	
	public Map<List<FaultTreeNode>, Double> getRepairRates() {
		return repairRates;
	}
}
