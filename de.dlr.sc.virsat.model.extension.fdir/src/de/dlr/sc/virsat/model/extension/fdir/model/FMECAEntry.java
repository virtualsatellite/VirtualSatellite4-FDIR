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
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
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
	
	public static final String[] SL_NAMES = { 
		SEVERITY_Catastrophic_NAME,   
		SEVERITY_Critical_NAME,
		SEVERITY_Major_NAME,
		SEVERITY_Minor_NAME
	}; 
	
	public static final String[] PL_NAMES = { 
		PROBABILITY_ExtremelyRemote_NAME,   
		PROBABILITY_Remote_NAME,
		PROBABILITY_Occasional_NAME,
		PROBABILITY_Probable_NAME
	};
	
	public static final int PL_UNKNOWN = Integer.valueOf(FMECAEntry.PROBABILITY_Unknown_VALUE);
	public static final int PL_PROBABLE = Integer.valueOf(FMECAEntry.PROBABILITY_Probable_VALUE);
	public static final int PL_OCCASIONAL = Integer.valueOf(FMECAEntry.PROBABILITY_Occasional_VALUE);
	public static final int PL_REMOTE = Integer.valueOf(FMECAEntry.PROBABILITY_Remote_VALUE);
	public static final int PL_EXTREMELY_REMOTE = Integer.valueOf(FMECAEntry.PROBABILITY_ExtremelyRemote_VALUE);
	
	public static final int[] PL_LEVELS = new int[] { PL_PROBABLE, PL_OCCASIONAL, PL_REMOTE, PL_EXTREMELY_REMOTE};
	
	public static final int SL_UNKNOWN = Integer.valueOf(FMECAEntry.SEVERITY_Unknown_VALUE);
	public static final int SL_CATASTROPHIC = Integer.valueOf(FMECAEntry.SEVERITY_Catastrophic_VALUE);
	public static final int SL_CRITICAL = Integer.valueOf(FMECAEntry.SEVERITY_Critical_VALUE);
	public static final int SL_MAJOR = Integer.valueOf(FMECAEntry.SEVERITY_Major_VALUE);
	public static final int SL_MINOR = Integer.valueOf(FMECAEntry.SEVERITY_Minor_VALUE);
	
	public static final int[] SL_LEVELS = new int[] { SL_CATASTROPHIC, SL_CRITICAL, SL_MAJOR, SL_MINOR};
	
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
	 * Gets the failure label of this entry
	 * @return the failure label
	 */
	public String getFailureLabel() {
		return getEntryLabel(getFailure());
	}
	
	/**
	 * Gets the failure mode label of this entry
	 * @return the failure mode label
	 */
	public String getFailureModeLabel() {
		return getEntryLabel(getFailureMode());
	}
	
	/**
	 * Gets the failure cause label of this entry
	 * @return the failure cause label
	 */
	public String getFailureCauseLabel() {
		return getEntryLabel(getFailureCause());
	}
	
	/**
	 * Helper method to label the entry
	 * @param bean the bean for which we want a label
	 * @return a label for the bean
	 */
	private String getEntryLabel(ABeanCategoryAssignment bean) {
		if (bean == null) {
			return "";
		}
		return (bean.getParent() != null ? bean.getParent().getName() + "." : "") + bean.getName();
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
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(analysisNode, MeanTimeToFailure.MTTF);
			getMeanTimeToFailureBean().setValueAsBaseUnit(result.getMeanTimeToFailure());
		} else {
			setMeanTimeToFailure(Double.NaN);
		}
		
		getFailureEffects().addAll(getFailure().getFaultTree().getAffectedFaults());
		
		Set<String> compensations = new HashSet<>();
		compensations.addAll(getFailure().getFaultTree().getCompensations());
		if (getFailureMode() != null) {
			compensations.addAll(getFailureMode().getFault().getFaultTree().getCompensations());
		}
		if (getFailureCause() != null) {
			compensations.addAll(getFailureCause().getFault().getFaultTree().getCompensations());
		}
		
		CategoryInstantiator ci = new CategoryInstantiator();
		for (String compensation : compensations) {
			APropertyInstance pi = ci.generateInstance(getCompensationBean().getArrayInstance());
			BeanPropertyString newBeanProperty = new BeanPropertyString();
			newBeanProperty.setTypeInstance((ValuePropertyInstance) pi);
			newBeanProperty.setValue(compensation);
			getCompensationBean().add(newBeanProperty);
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
