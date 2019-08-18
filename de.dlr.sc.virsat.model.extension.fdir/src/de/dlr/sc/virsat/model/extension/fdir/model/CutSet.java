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

import org.eclipse.emf.ecore.EObject;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.calculation.AdvancedFunction;
import de.dlr.sc.virsat.model.dvlm.calculation.CalculationFactory;
import de.dlr.sc.virsat.model.dvlm.calculation.Equation;
import de.dlr.sc.virsat.model.dvlm.calculation.ReferencedInput;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;

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

	public void fill(ModelCheckingResult mcsResult, FDIRParameters fdirParameters) {
		setSeverity(getFailure().getSeverity());
		getMeanTimeToFailureBean().setValueAsBaseUnit(mcsResult.getMeanTimeToFailure());
		getMeanTimeToDetectionBean().setValueAsBaseUnit(mcsResult.getMeanTimeToDetection());
		getSteadyStateDetectabilityBean().setValueAsBaseUnit(mcsResult.getSteadyStateDetectability());

		if (fdirParameters != null) {
			Equation equation = getTypeInstance().getEquationSection().getEquations().get(0);
			AdvancedFunction opClassifyPL = (AdvancedFunction) equation.getExpression();
			ReferencedInput ri = CalculationFactory.eINSTANCE.createReferencedInput();
			ri.setReference(fdirParameters.getTypeInstance());
			opClassifyPL.getInputs().add(ri);
		}
	}
}
