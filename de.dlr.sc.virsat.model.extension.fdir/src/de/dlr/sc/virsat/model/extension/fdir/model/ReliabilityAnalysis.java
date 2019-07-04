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
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryAutomatonStrategy;

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

	private static final double TO_PERCENT = 100;
	private static final double EPS = 0.0001;
	public static final int COUNT_RELIABILITY_POINTS = 100;

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
	 * Gets the first fault attached to the same structural element instance
	 * 
	 * @return the top level fault to be analysed
	 */
	public Fault getFault() {
		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance(
				(StructuralElementInstance) getTypeInstance().eContainer());
		return parent.getFirst(Fault.class);
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
		FaultTreeNode fault = getFault();
		if (fault != null) {
			SubMonitor subMonitor = null;
			if (monitor != null) {
				monitor.setTaskName("Reliability Analysis");
				final int COUNT_TASKS = 3;
				subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
				subMonitor.setTaskName("Creating Data Model");
				subMonitor.split(1);
			}
			double delta = getTimestepBean().getValueToBaseUnit();
			IBeanStructuralElementInstance parent = new BeanStructuralElementInstance(
					(StructuralElementInstance) getTypeInstance().eContainer());
			RecoveryAutomaton ra = parent.getFirst(RecoveryAutomaton.class);
			FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra != null, delta, EPS);
			if (ra != null) {
				ftEvaluator.setRecoveryStrategy(new RecoveryAutomatonStrategy(ra));
			}

			double maxTime = getRemainingMissionTimeBean().getValueToBaseUnit();
			double pointDelta = maxTime / COUNT_RELIABILITY_POINTS;
			
			if (monitor != null) {
				if (monitor.isCanceled()) {
					return UnexecutableCommand.INSTANCE;
				}
				subMonitor.setTaskName("Performing Model Checking");
				subMonitor.split(1);
			}
			ftEvaluator.evaluateFaultTree(fault, new Reliability(maxTime), MTTF.MTTF);
			if (monitor != null) {
				if (monitor.isCanceled()) {
					return UnexecutableCommand.INSTANCE;
				}
				subMonitor.setTaskName("Updating Results");
				subMonitor.split(1);
			}
			double mttf = ftEvaluator.getMeanTimeToFailure();
			return new RecordingCommand(ed, "Reliability Analysis") {
				@Override
				protected void doExecute() {
					setReliability(
							TO_PERCENT * (1 - ftEvaluator.getFailRates().get(ftEvaluator.getFailRates().size() - 1)));
					getMeanTimeToFailureBean().setValueAsBaseUnit(mttf);

					getReliabilityCurve().clear();

					double accDelta = pointDelta;
					for (int i = 0; i < ftEvaluator.getFailRates().size(); ++i) {
						accDelta += delta;
						if (accDelta >= pointDelta) {
							createNewReliabilityCurveEntry(TO_PERCENT * (1 - ftEvaluator.getFailRates().get(i)));
							accDelta -= pointDelta;
						}
					}

				}
			};

		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}

	/**
	 * Creates a new entry for the reliability curve
	 * 
	 * @param value
	 *            the new entry in the reliability curve
	 */
	private void createNewReliabilityCurveEntry(double value) {
		CategoryInstantiator ci = new CategoryInstantiator();
		APropertyInstance pi = ci.generateInstance(getReliabilityCurve().getArrayInstance());
		BeanPropertyFloat newBeanProperty = new BeanPropertyFloat();
		newBeanProperty.setTypeInstance((UnitValuePropertyInstance) pi);
		newBeanProperty.setValue(value);
		getReliabilityCurve().add(newBeanProperty);
	}

}