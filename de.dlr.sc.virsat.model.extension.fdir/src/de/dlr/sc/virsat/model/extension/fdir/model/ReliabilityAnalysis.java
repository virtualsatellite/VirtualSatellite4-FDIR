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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.model.concept.types.category.BeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
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
public class ReliabilityAnalysis extends AReliabilityAnalysis {

	private static final double EPS = 0.0001;

	/**
	 * Constructor of Concept Class
	 */
	public ReliabilityAnalysis() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate a CategoryAssignment in
	 * the background from the given concept
	 * 
	 * @param concept
	 *            the concept where it will find the correct Category to instantiate
	 *            from
	 */
	public ReliabilityAnalysis(Concept concept) {
		super(concept);
	}

	/**
	 * Constructor of Concept Class that can be initialized manually by a given
	 * Category Assignment
	 * 
	 * @param categoryAssignment
	 *            The category Assignment to be used for background initialization
	 *            of the Category bean
	 */
	public ReliabilityAnalysis(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	/**
	 * Performs a reliability analysis on the top level fault of the attached
	 * structural element instance
	 * 
	 * @param ed
	 *            the editing domain
	 * @param monitor
	 *            progressMonitor
	 * @return a command that sets the analysis results
	 */
	public Command perform(TransactionalEditingDomain ed, IProgressMonitor monitor) {
		FaultTreeNode fault = getParentCaBeanOfClass(Fault.class);
		
		final int COUNT_TASKS = 3;
		SubMonitor subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
		subMonitor.setTaskName("Reliability Analysis");
		subMonitor.split(1);
		subMonitor.subTask("Creating Data Model");
		
		double delta = getTimestepBean().getValueToBaseUnit();
		RecoveryAutomaton ra = getRecoveryAutomaton(fault);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, delta, EPS);

		double maxTime = getRemainingMissionTimeBean().getValueToBaseUnit();
		
		subMonitor.split(1);
		subMonitor.subTask("Performing Model Checking");
		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, subMonitor.split(1), new Reliability(maxTime), MeanTimeToFailure.MTTF);
		
		subMonitor.split(1);
		subMonitor.subTask("Updating Results");

		double mttf = result.getMeanTimeToFailure();
		return new RecordingCommand(ed, "Reliability Analysis") {
			@Override
			protected void doExecute() {
				getReliabilityBean().setValueAsBaseUnit(1 - result.getFailRates().get(result.getFailRates().size() - 1));
				getMeanTimeToFailureBean().setValueAsBaseUnit(mttf);
				getReliabilityCurveBean().clear();
				for (int i = 0; i < result.getFailRates().size(); ++i) {
					createNewReliabilityCurveEntry(1 - result.getFailRates().get(i));
				}
			}
		};
	}
	
	/**
	 * Gets the recovery automaton that should be used for determining
	 * the reliability.
	 * @param fault the fault under analysis
	 * @return the recovery automaton
	 */
	protected RecoveryAutomaton getRecoveryAutomaton(FaultTreeNode fault) {
		BeanCategoryAssignment beanCa = new BeanCategoryAssignment();
		beanCa.setTypeInstance(fault.getTypeInstance());
		IBeanStructuralElementInstance parent = fault.getParent();
		return parent.getFirst(RecoveryAutomaton.class);
	}

	/**
	 * Creates a new entry for the reliability curve
	 * 
	 * @param value
	 *            the new entry in the reliability curve
	 */
	private void createNewReliabilityCurveEntry(double value) {
		CategoryInstantiator ci = new CategoryInstantiator();
		APropertyInstance pi = ci.generateInstance(getReliabilityCurveBean().getArrayInstance());
		BeanPropertyFloat newBeanProperty = new BeanPropertyFloat();
		newBeanProperty.setTypeInstance((UnitValuePropertyInstance) pi);
		newBeanProperty.setValueAsBaseUnit(value);
		getReliabilityCurveBean().add(newBeanProperty);
	}
}