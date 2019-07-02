/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;

/**
 * This evaluates all the faults in an fmeca table and the child faults
 * @author muel_s8
 *
 */

public class FMECAEvaluator {
	
	/** Performs the actuak evaluation
	 * @param beanSei the fault container
	 * @return a map from a each fault to its evaluated mean time to failure
	 */
	public Map<Fault, Double> evaluate(BeanStructuralElementInstance beanSei) {
		Set<Fault> faults = new HashSet<Fault>();
		
		Queue<Fault> toProcess = new LinkedList<>();
		toProcess.addAll(beanSei.getAll(Fault.class));
		while (!toProcess.isEmpty()) {
			Fault fault = toProcess.poll();
			faults.add(fault);
			
			Set<Fault> childFaults = fault.getFaultTree().getChildFaults();
			toProcess.addAll(childFaults);
		}
		
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(true);
		Map<Fault, Double> mapFaultToMTTF = new HashMap<>();
		
		for (Fault fault : faults) {
			ftEvaluator.evaluateFaultTree(fault);
			double mttf = ftEvaluator.getMeanTimeToFailure();
			mapFaultToMTTF.put(fault, mttf);
		}
		
		return mapFaultToMTTF;
	}
}
