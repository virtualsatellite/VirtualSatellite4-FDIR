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

import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyBoolean;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyDL;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyPL;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public  class FDIRParameters extends AFDIRParameters {
	
	/**
	 * Constructor of Concept Class
	 */
	public FDIRParameters() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FDIRParameters(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FDIRParameters(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Sets the probability level thresholds to the default ones
	 */
	public void setDefaultProbablityThresholds() {
		for (int i = 0; i < OpClassifyPL.DEFAULT_PL_THRESHOLDS.length; ++i) {
			BeanPropertyFloat beanFloat = getProbabilityLevelsBean().get(i);
			beanFloat.setValueAsBaseUnit(OpClassifyPL.DEFAULT_PL_THRESHOLDS[i]);
		}
	}

	/**
	 * Sets the detectability level thresholds to the default ones
	 */
	public void setDefaultDetectabilityThresholds() {
		for (int i = 0; i < OpClassifyDL.DEFAULT_DL_THRESHOLDS.length; ++i) {
			BeanPropertyFloat beanFloat = getDetectionLevelsBean().get(i);
			beanFloat.setValueAsBaseUnit(OpClassifyDL.DEFAULT_DL_THRESHOLDS[i]);
		}
	}

	/**
	 * Sets the default criticality matrix according to the default criticality check.
	 */
	public void setDefaultCriticalityMatrix() {
		for (int detectable = 1; detectable <= getCriticalityMatrices().size(); ++detectable) {
			CriticalityMatrix cm = getCriticalityMatrices().get(detectable - 1);
			for (int severity = 1; severity <= cm.getCriticalityMatrix().size(); ++severity) {
				CriticalityVector cv = cm.getCriticalityMatrix().get(cm.getCriticalityMatrix().size() - severity);
				for (int probability = 1; probability <= cv.getIsCriticalBean().size(); ++probability) {
					BeanPropertyBoolean isCriticalProperty = cv.getIsCriticalBean().get(probability - 1);
					isCriticalProperty.setValue(isDefaultCritical(detectable, severity, probability));
				}
			}
		}
	}

	public static final int GENERAL_CRITICALITY_THRESHOLD = 12;
	public static final int OBSERVABLE_CRITICALITY_TRESHOLD = 6;
	public static final int PARTIAL_OBSERVABLE_SEVERITY_THRESHOLD = 3;
	public static final int PARTIAL_OBSERVABLE_PROBABILITY_THRESHOLD = 4;
	
	/**
	 * Checks if the given constellation is considered critical in the default
	 * configuration.
	 * 
	 * @param detectable the detectability level
	 * @param severity the severity level
	 * @param probability the probability level
	 * @return true iff the constellation is critical by default
	 */
	public boolean isDefaultCritical(int detectable, int severity, int probability) {
		int critical = detectable * severity * probability;
		
		// Any constellation over the general criticality threshold is critical
		if (critical >= GENERAL_CRITICALITY_THRESHOLD) {
			return true;
		}
		
		// Any catastrophic event is critical
		if (severity >= Integer.valueOf(FMECAEntry.SEVERITY_Catastrophic_VALUE)) {
			return true;
		}
		
		// Severity and probability bounds check for partial observable events
		if (detectable >= 2 && (severity >= PARTIAL_OBSERVABLE_SEVERITY_THRESHOLD || probability >= PARTIAL_OBSERVABLE_PROBABILITY_THRESHOLD)) {
			return true;
		}
		
		// Criticality bounds check for fully observable events
		if (detectable == 1 && critical >= OBSERVABLE_CRITICALITY_TRESHOLD) {
			return true;
		} 
		
		return false;
	}
	
	/**
	 * Performs a lookup in the criticality matrices to see if the given constellation
	 * is considered critical.
	 * 
	 * @param detectable the detectability level
	 * @param severity the severity level
	 * @param probability the probability level
	 * @return true iff the configuration is defined as critical
	 */
	public boolean isCritical(int detectable, int severity, int probability) {
		CriticalityMatrix cm = getCriticalityMatrices().get(detectable - 1);
		CriticalityVector cv = cm.getCriticalityMatrix().get(cm.getCriticalityMatrix().size() - severity);
		boolean isCritical = cv.getIsCriticalBean().get(probability - 1).getValue();
		return isCritical;
	}
}
