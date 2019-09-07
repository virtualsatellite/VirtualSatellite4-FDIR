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

import java.util.stream.Collectors;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.model.dvlm.calculation.AdvancedFunction;
import de.dlr.sc.virsat.model.dvlm.calculation.CalculationFactory;
import de.dlr.sc.virsat.model.dvlm.calculation.Equation;
import de.dlr.sc.virsat.model.dvlm.calculation.ReferencedInput;
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
public  class CutSet extends ACutSet {
	
	public static final int DL_UNKNOWN = Integer.valueOf(DETECTION_Unknown_VALUE);
	public static final int DL_VERY_LIKELY = Integer.valueOf(DETECTION_VeryLikely_VALUE);
	public static final int DL_LIKELY = Integer.valueOf(DETECTION_Likely_VALUE);
	public static final int DL_UNLIKELY = Integer.valueOf(DETECTION_Unlikely_VALUE);
	public static final int DL_EXTREMELY_UNLIKELY = Integer.valueOf(DETECTION_ExtremelyUnlikely_VALUE);
	
	public static final int[] DL_LEVELS = new int[] { DL_VERY_LIKELY, DL_LIKELY, DL_UNLIKELY, DL_EXTREMELY_UNLIKELY};
	
	public static final String[] DL_NAMES = { 
		DETECTION_ExtremelyUnlikely_NAME,   
		DETECTION_Unlikely_NAME,
		DETECTION_Likely_NAME,
		DETECTION_VeryLikely_NAME
	};
	
	/**
	 * Constructor of Concept Class
	 */
	public CutSet() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public CutSet(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public CutSet(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	/**
	 * FIlls out the Cut Set metrics with the given model checking result
	 * @param mcsResult the model checking result for this mcs
	 * @param fdirParameters the fdir parameter set
	 */
	public void fill(ModelCheckingResult mcsResult, FDIRParameters fdirParameters) {
		setSeverity(getFailure().getSeverity());
		getMeanTimeToFailureBean().setValueAsBaseUnit(mcsResult.getMeanTimeToFailure());
		getMeanTimeToDetectionBean().setValueAsBaseUnit(mcsResult.getMeanTimeToDetection());
		getSteadyStateDetectabilityBean().setValueAsBaseUnit(mcsResult.getSteadyStateDetectability());

		if (fdirParameters != null) {
			Equation equation0 = getTypeInstance().getEquationSection().getEquations().get(0);
			Equation equation1 = getTypeInstance().getEquationSection().getEquations().get(1);
			AdvancedFunction opClassifyPL = (AdvancedFunction) equation0.getExpression();
			AdvancedFunction opClassifyDL = (AdvancedFunction) equation1.getExpression();
			ReferencedInput ri0 = CalculationFactory.eINSTANCE.createReferencedInput();
			ri0.setReference(fdirParameters.getTypeInstance());
			opClassifyPL.getInputs().add(ri0);
			ReferencedInput ri1 = CalculationFactory.eINSTANCE.createReferencedInput();
			ri1.setReference(fdirParameters.getTypeInstance());
			opClassifyDL.getInputs().add(ri1);
		}
	}

	/**
	 * Gets a labeling for contained basic events
	 * @return a labeling for the basic events
	 */
	public String getBasicEventsLabel() {
		return getBasicEvents().stream().map(be -> (be.getParent() != null ? be.getParent().getName() + "." : "") + be.getName()).collect(Collectors.joining(","));
	}
}
