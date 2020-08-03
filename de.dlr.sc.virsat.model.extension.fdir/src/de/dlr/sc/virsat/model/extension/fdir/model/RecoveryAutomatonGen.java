/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import java.util.HashMap;
import java.util.Map;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public  class RecoveryAutomatonGen extends ARecoveryAutomatonGen {
	
	public static final String LOG_FILE_NAME = "synthesis.log";
	
	/**
	 * Constructor of Concept Class
	 */
	public RecoveryAutomatonGen() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public RecoveryAutomatonGen(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public RecoveryAutomatonGen(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	private static final Map<String, IMetric> MAP_NAME_TO_OBJECTIVE = new HashMap<>();
	static  {
		MAP_NAME_TO_OBJECTIVE.put(RecoveryAutomatonGen.OBJECTIVEMETRIC_MeanTimeToFailure_NAME, MeanTimeToFailure.MTTF);
		MAP_NAME_TO_OBJECTIVE.put(RecoveryAutomatonGen.OBJECTIVEMETRIC_SteadyStateAvailability_NAME, SteadyStateAvailability.SSA);
		MAP_NAME_TO_OBJECTIVE.put(RecoveryAutomatonGen.OBJECTIVEMETRIC_SteadyStateDetectability_NAME, SteadyStateDetectability.SSD);
	}
	
	public static IMetric getObjectiveMetricFromName(String objectMetricName) {
		return MAP_NAME_TO_OBJECTIVE.get(objectMetricName);
	}
}
