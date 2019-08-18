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

import java.util.HashSet;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
import de.dlr.sc.virsat.model.dvlm.calculation.AdvancedFunction;
import de.dlr.sc.virsat.model.dvlm.calculation.CalculationFactory;
import de.dlr.sc.virsat.model.dvlm.calculation.Equation;
import de.dlr.sc.virsat.model.dvlm.calculation.ReferencedInput;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;

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
public  class FMECAEntry extends AFMECAEntry {
	
	/**
	 * Constructor of Concept Class
	 */
	public FMECAEntry() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FMECAEntry(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FMECAEntry(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Fills out an FMECA entry as much as possible
	 * @param ftEvaluator the fault tree evaluator
	 * @param fdirParameters the fdir parameters
	 */
	public void fill(FaultTreeEvaluator ftEvaluator, FDIRParameters fdirParameters) {
		setSeverity(getFailure().getSeverity());
		
		FaultTreeNode analysisNode = getFailureCause() != null ? getFailureCause() : getFailureMode();
		
		if (analysisNode != null) {			
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(analysisNode, MTTF.MTTF);
			getMeanTimeToFailureBean().setValueAsBaseUnit(result.getMeanTimeToFailure());
		} else {
			setMeanTimeToFailure(Double.NaN);
		}
		
		getFailureEffects().addAll(getFailure().getFaultTree().getAffectedFaults());
		
		Set<String> proposedRecoveryActions = new HashSet<>();
		proposedRecoveryActions.addAll(getFailure().getFaultTree().getPotentialRecoveryActions());
		if (getFailureMode() != null) {
			proposedRecoveryActions.addAll(getFailureMode().getFault().getFaultTree().getPotentialRecoveryActions());
		}
		if (getFailureCause() != null) {
			proposedRecoveryActions.addAll(getFailureCause().getFault().getFaultTree().getPotentialRecoveryActions());
		}
		
		
		CategoryInstantiator ci = new CategoryInstantiator();
		for (String proposedRecoveryAction : proposedRecoveryActions) {
			APropertyInstance pi = ci.generateInstance(getProposedRecovery().getArrayInstance());
			BeanPropertyString newBeanProperty = new BeanPropertyString();
			newBeanProperty.setTypeInstance((ValuePropertyInstance) pi);
			newBeanProperty.setValue(proposedRecoveryAction);
			getProposedRecovery().add(newBeanProperty);
		}
		

		if (fdirParameters != null) {
			Equation equation = getTypeInstance().getEquationSection().getEquations().get(0);
			AdvancedFunction opClassifyPL = (AdvancedFunction) equation.getExpression();
			ReferencedInput ri = CalculationFactory.eINSTANCE.createReferencedInput();
			ri.setReference(fdirParameters.getTypeInstance());
			opClassifyPL.getInputs().add(ri);
		}
	}
}
